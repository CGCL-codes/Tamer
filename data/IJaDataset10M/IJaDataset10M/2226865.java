package de.uni_leipzig.lots.server.services.impl;

import static de.uni_leipzig.lots.common.util.StringUtil.getDefaultIndent;
import de.uni_leipzig.lots.server.persist.Repository;
import de.uni_leipzig.lots.server.services.RestoreException;
import de.uni_leipzig.lots.server.services.RestoreService;
import org.dom4j.*;
import org.dom4j.io.DocumentResult;
import org.dom4j.io.DocumentSource;
import org.dom4j.io.SAXReader;
import org.jetbrains.annotations.NotNull;
import org.springframework.dao.DataAccessException;
import org.springframework.transaction.annotation.Transactional;
import javax.xml.transform.*;
import java.io.File;
import java.io.InputStream;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * TODO: FastStreamingResolver is the most actual implementation - update this!
 *
 * @author Alexander Kiel
 * @version $Id: RestoreServiceImpl.java,v 1.7 2007/10/23 06:29:26 mai99bxd Exp $
 */
public class RestoreServiceImpl implements RestoreService {

    private static final Logger logger = Logger.getLogger(RestoreServiceImpl.class.getName());

    private RestoreConfig config;

    /**
     * Map of already persisted entities.
     */
    private Map<String, Map<Long, EntityDescriptor>> entityDescriptorMapMap;

    private Set<EntityDescriptor> visitedEntities;

    private int numberOfRestoredEntities;

    public RestoreServiceImpl() {
        cleanUp();
    }

    public RestoreConfig getConfig() {
        return config;
    }

    public void setConfig(RestoreConfig config) {
        this.config = config;
    }

    @Transactional(rollbackFor = RestoreException.class)
    public void restore(@NotNull Document document) throws RestoreException {
        try {
            final int currentAppDataVersion = config.getDataVersion();
            int dataVersion = getDataVersion(document);
            while (dataVersion < currentAppDataVersion) {
                if (logger.isLoggable(Level.FINE)) {
                    logger.fine("The data version (" + dataVersion + ") of the given document is older " + "than the current application data version (" + currentAppDataVersion + "). Starting to convert one version up.");
                }
                DocumentTransformer documentTransformer;
                try {
                    documentTransformer = new DocumentTransformer(config.getVersionTransformStyleSheet(dataVersion, dataVersion + 1));
                } catch (TransformerConfigurationException e) {
                    throw new RestoreException("TransformerConfigurationException while creating a " + "transformer.", e);
                }
                try {
                    document = documentTransformer.transform(document);
                } catch (TransformerException e) {
                    throw new RestoreException("TransformerException while transforming.", e);
                }
                if (logger.isLoggable(Level.FINE)) {
                    logger.fine("End transforming from data version " + dataVersion + " to data version " + (dataVersion + 1) + ".");
                }
                dataVersion = getDataVersion(document);
            }
            if (dataVersion > currentAppDataVersion) {
                throw new RestoreException("The data version (" + dataVersion + ") of the backup file is newer as the data version (" + currentAppDataVersion + ") of the current installation of LOTS. A restore is not possible. Please " + "install a newer version of LOTS.");
            }
            Element rootElement = document.getRootElement();
            assert rootElement != null;
            Element entities = (Element) rootElement.selectSingleNode("entities");
            if (entities == null) {
                throw new RestoreException("No node \"entities\".");
            }
            buildEntityDescriptors(entities);
            restoreEntities(entities);
        } finally {
            cleanUp();
        }
    }

    @Transactional(rollbackFor = RestoreException.class)
    public void restore(@NotNull File file) throws RestoreException {
        SAXReader saxReader = createSAXReader();
        Document document;
        try {
            document = saxReader.read(file);
        } catch (DocumentException e) {
            throw new RestoreException("DocumentException while restoring: " + file.getAbsolutePath(), e);
        }
        saxReader.resetHandlers();
        restore(document);
    }

    private SAXReader createSAXReader() {
        SAXReader saxReader = new SAXReader();
        saxReader.setStringInternEnabled(true);
        saxReader.setIgnoreComments(true);
        saxReader.setMergeAdjacentText(true);
        saxReader.setStripWhitespaceText(true);
        return saxReader;
    }

    @Transactional(rollbackFor = RestoreException.class)
    public void restore(@NotNull InputStream in) throws RestoreException {
        SAXReader saxReader = createSAXReader();
        Document document;
        try {
            document = saxReader.read(in);
        } catch (DocumentException e) {
            throw new RestoreException("DocumentException while restoring.", e);
        }
        restore(document);
    }

    private void buildEntityDescriptors(Element entities) throws RestoreException {
        if (logger.isLoggable(Level.FINE)) {
            logger.logp(Level.FINE, getClass().getName(), "buildEntityDescriptors", "start building EntityDescriptors");
        }
        for (Element entitySet : (List<Element>) entities.elements()) {
            String superType = config.getTypeOfEntitySet(entitySet);
            if (superType == null) {
                throw new RestoreException("Unknown type \"" + superType + "\".");
            }
            Map<Long, EntityDescriptor> superDescriptorMap = getOrCreateEntityDescriptorMap(superType);
            for (Element entity : (List<Element>) entitySet.elements()) {
                assert entity != null;
                String type = config.getTypeOfEntity(entity);
                if (type == null) {
                    throw new RestoreException("Unknown type \"" + type + "\".");
                }
                Long id = config.getId(entity);
                if (id == null) {
                    throw new RestoreException("The entity \"" + entity + "\" has no id.");
                }
                Map<Long, EntityDescriptor> descriptorMap = getOrCreateEntityDescriptorMap(type);
                EntityDescriptor entityDescriptor = new EntityDescriptor(type, entity);
                superDescriptorMap.put(id, entityDescriptor);
                descriptorMap.put(id, entityDescriptor);
            }
        }
        if (logger.isLoggable(Level.FINE)) {
            logger.logp(Level.FINE, getClass().getName(), "buildEntityDescriptors", "end building EntityDescriptors");
        }
    }

    private void restoreEntities(Element entities) throws RestoreException {
        if (logger.isLoggable(Level.FINE)) {
            logger.logp(Level.FINE, getClass().getName(), "restoreEntities", "start restore Entities");
        }
        numberOfRestoredEntities = 0;
        for (Element entitySet : (List<Element>) entities.elements()) {
            for (Element entity : (List<Element>) entitySet.elements()) {
                assert entity != null;
                String type = config.getTypeOfEntity(entity);
                if (type == null) {
                    throw new RestoreException("Unknown type.");
                }
                restoreIntern(type, entity, 0);
            }
        }
        if (logger.isLoggable(Level.FINE)) {
            logger.logp(Level.FINE, getClass().getName(), "restoreEntities", "end restore Entities");
        }
    }

    private void restoreIntern(@NotNull String type, @NotNull Element entity, int level) throws RestoreException {
        Long id = config.getId(entity);
        if (id == null) {
            throw new RestoreException("The entity \"" + entity + "\" has no id.");
        }
        EntityDescriptor entityDescriptor = getEntityDescriptor(type, id);
        if (entityDescriptor.isPersisted()) {
            if (logger.isLoggable(Level.FINER)) {
                logger.finer(getDefaultIndent(level) + "skip already restored entity (" + type + ", " + id + ")");
            }
            return;
        }
        if (entityDescriptor.isVisited()) {
            if (logger.isLoggable(Level.FINER)) {
                logger.finer(getDefaultIndent(level) + "skip already visited entity (" + type + ", " + id + ")");
            }
            return;
        } else {
            entityDescriptor.setVisited(true);
            visitedEntities.add(entityDescriptor);
        }
        if (logger.isLoggable(Level.FINER)) {
            logger.finer(getDefaultIndent(level) + "start restoring (" + type + ", " + id + ") at level " + level);
        }
        List references = referencesXPath.selectNodes(entity);
        cascadeReferences(entity, references, level, "many-to-one");
        Repository repository = config.getRepository(type);
        if (repository == null) {
            throw new RestoreException("No repository for entities of type \"" + type + "\" available.");
        }
        if (logger.isLoggable(Level.FINE)) {
            logger.fine(getDefaultIndent(level) + "RESTORE entity (" + type + ", " + id + ") at level " + level);
        }
        entityDescriptor.setPersisted(true);
        entityDescriptor.setVisited(false);
        visitedEntities.remove(entityDescriptor);
        try {
            repository.importDOM4J(entity);
        } catch (DataAccessException e) {
            throw new RestoreException("DataAccessException while restoring entity (" + type + ", " + id + ") at level " + level + ".", e);
        }
        numberOfRestoredEntities++;
        List collectionReferences = collectionReferencesXPath.selectNodes(entity);
        cascadeReferences(entity, collectionReferences, level, "collection");
        if (numberOfRestoredEntities > 100 && level == 0 && visitedEntities.isEmpty()) {
            if (logger.isLoggable(Level.FINE)) {
                logger.fine("\n\n\n\nFlush at " + numberOfRestoredEntities + " newly restored entities.\n" + "\n" + "\n" + "");
            }
            try {
                repository.flushDOM4J();
            } catch (DataAccessException e) {
                throw new RestoreException("DataAccessException while flushing entities.", e);
            }
            numberOfRestoredEntities = 0;
        }
    }

    private void cascadeReferences(@NotNull Element parent, @NotNull List<Element> references, int level, String refKind) throws RestoreException {
        for (Element reference : references) {
            String refType = config.getTypeOfEntityRef(reference);
            if (refType == null) {
                throw new RestoreException("The reference \"" + reference + "\" is not valid.");
            }
            if (!config.shouldCascadeToEntity(refType)) continue;
            Long refId = config.getRefId(reference);
            if (refId == null) {
                throw new RestoreException("The value of the reference \"" + reference + "\" is not valid.");
            }
            EntityDescriptor referentDescriptor = getEntityDescriptor(refType, refId);
            if (referentDescriptor.isPersisted()) {
                if (logger.isLoggable(Level.FINER)) {
                    logger.finer(getDefaultIndent(level) + "skip already persisted child (" + refType + ", " + refId + ") " + "at level " + level);
                }
                continue;
            }
            Element referent = referentDescriptor.getElement();
            String referentType = referentDescriptor.getType();
            if (referent.equals(parent)) continue;
            if (logger.isLoggable(Level.FINER)) {
                logger.finer(getDefaultIndent(level) + "cascade " + refKind + " ref of (" + config.getTypeOfEntity(parent) + ", " + config.getId(parent) + ") to child (" + referentType + ", " + refId + ") " + "at level " + level);
            }
            restoreIntern(referentType, referent, level + 1);
        }
    }

    @NotNull
    private EntityDescriptor getEntityDescriptor(String type, Long id) throws RestoreException {
        Map<Long, EntityDescriptor> map = entityDescriptorMapMap.get(type);
        if (map == null) {
            throw new RestoreException("Unknown type \"" + type + "\".");
        }
        EntityDescriptor entityDescriptor = map.get(id);
        if (entityDescriptor == null) {
            throw new RestoreException("Entity of type \"" + type + "\" with id \"" + id + "\" not found.");
        }
        return entityDescriptor;
    }

    @NotNull
    private Map<Long, EntityDescriptor> getOrCreateEntityDescriptorMap(String type) {
        Map<Long, EntityDescriptor> persistedIds = entityDescriptorMapMap.get(type);
        if (persistedIds == null) {
            persistedIds = new HashMap<Long, EntityDescriptor>();
            entityDescriptorMapMap.put(type, persistedIds);
        }
        return persistedIds;
    }

    private void cleanUp() {
        entityDescriptorMapMap = new HashMap<String, Map<Long, EntityDescriptor>>();
        visitedEntities = new HashSet<EntityDescriptor>();
        numberOfRestoredEntities = 0;
    }

    private static int getDataVersion(Document document) throws RestoreException {
        Element rootElement = document.getRootElement();
        assert rootElement != null;
        return Integer.valueOf(rootElement.attribute("data-version").getText());
    }

    private XPath referencesXPath = DocumentFactory.getInstance().createXPath("descendant::*/@*[ends-with(name(), 'ref')]/parent::*");

    private XPath collectionReferencesXPath = DocumentFactory.getInstance().createXPath("descendant::*[ends-with(name(), 'ref')]");

    private static class EntityDescriptor {

        private final String type;

        private final Element element;

        private boolean visited;

        private boolean persisted;

        public EntityDescriptor(String type, Element element) {
            this.type = type;
            this.element = element;
            visited = false;
            persisted = false;
        }

        public String getType() {
            return type;
        }

        public Element getElement() {
            return element;
        }

        public boolean isVisited() {
            return visited;
        }

        public void setVisited(boolean visited) {
            this.visited = visited;
        }

        public boolean isPersisted() {
            return persisted;
        }

        public void setPersisted(boolean persisted) {
            this.persisted = persisted;
        }
    }

    private static class DocumentTransformer {

        private Transformer transformer;

        public DocumentTransformer(Source aStyleSheet) throws TransformerConfigurationException {
            TransformerFactory factory = TransformerFactory.newInstance();
            transformer = factory.newTransformer(aStyleSheet);
        }

        public Document transform(Document aDocument) throws TransformerException {
            DocumentSource source = new DocumentSource(aDocument);
            DocumentResult result = new DocumentResult();
            transformer.transform(source, result);
            return result.getDocument();
        }
    }
}
