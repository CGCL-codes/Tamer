package bibtex.rdf;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import org.apache.log4j.Logger;
import bibtex.model.AbstractEntry;
import bibtex.model.EntryType;
import bibtex.model.Field;
import bibtex.model.FieldType;
import bibtex.rdf.labeling.Labeler;
import bibtex.util.BibtexUtil;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.vocabulary.RDF;

/**
 * @author siberski
 */
public class EntryHandler {

    private static Logger log = Logger.getLogger(EntryHandler.class);

    private BibtexSchema schema;

    private Map<FieldType, PropertyHandler> propertyMap;

    private Set<FieldType> ignoredProperties = new HashSet<FieldType>();

    /**
     * @param bibProperty
     * @param rdfProperty
     */
    public EntryHandler(BibtexSchema schema, PersonCollection persons, PublicationCollection collections, String baseUri) {
        this.schema = schema;
        propertyMap = new HashMap<FieldType, PropertyHandler>();
        propertyMap.put(FieldType.AUTHOR, new PersonListHandler(schema, persons, FieldType.AUTHOR, schema.getAuthor()));
        propertyMap.put(FieldType.EDITOR, new PersonListHandler(schema, persons, FieldType.EDITOR, schema.getEditor()));
        propertyMap.put(FieldType.BOOKTITLE, new CollectionHandler(schema, persons, collections, FieldType.BOOKTITLE, schema.getBooktitle()));
        propertyMap.put(FieldType.JOURNAL, new CollectionHandler(schema, persons, collections, FieldType.JOURNAL, schema.getJournal()));
        propertyMap.put(FieldType.SERIES, new CollectionHandler(schema, persons, collections, FieldType.SERIES, schema.getSeries()));
        propertyMap.put(FieldType.CROSSREF, new CrossrefHandler(schema, baseUri));
        propertyMap.put(FieldType.YEAR, new DateHandler(schema, false));
        propertyMap.put(FieldType.PAGES, new PagesHandler(schema));
        propertyMap.put(FieldType.PUBLISHER, new OrganizationHandler(schema, persons, FieldType.PUBLISHER, schema.getPublisher()));
        propertyMap.put(FieldType.INSTITUTION, new OrganizationHandler(schema, persons, FieldType.INSTITUTION, schema.getInstitution()));
        propertyMap.put(FieldType.ORGANIZATION, new OrganizationHandler(schema, persons, FieldType.ORGANIZATION, schema.getOrganization()));
        propertyMap.put(FieldType.SCHOOL, new OrganizationHandler(schema, persons, FieldType.SCHOOL, schema.getSchool()));
        for (FieldType bibtexVerbatimField : schema.getVerbatimProperties()) {
            propertyMap.put(bibtexVerbatimField, new VerbatimPropertyHandler(bibtexVerbatimField, schema.getProperty(bibtexVerbatimField), schema));
        }
    }

    public void addTriples(AbstractEntry entry, Resource sourceFile, Resource e) {
        addType(entry.getType(), e);
        boolean inCollection = false;
        for (Field f : entry.getFields()) {
            FieldType ft = f.getType();
            boolean ch = (propertyMap.get(ft) instanceof CollectionHandler);
            inCollection = inCollection || ch;
            if (ch || schema.outputEntryProperty(ft)) {
                addTriples(entry, sourceFile, e, ft);
            } else {
                if (!schema.outputCollectionProperty(ft) && !ignoredProperties.contains(ft)) {
                    log.info("property <" + ft + "> is not declared as output property; ignoring");
                    ignoredProperties.add(ft);
                }
            }
        }
        if (!inCollection) {
            for (Field f : entry.getFields()) {
                FieldType ft = f.getType();
                if (!schema.outputEntryProperty(ft) && schema.outputCollectionProperty(ft)) {
                    addTriples(entry, sourceFile, e, ft);
                }
            }
        }
        if (sourceFile != null && schema.outputEntryProperty(FieldType.SOURCE_FILE)) {
            e.addProperty(schema.getSourceFile(), sourceFile);
        }
        if (schema.outputEntryProperty(FieldType.LABEL) && !e.hasProperty(schema.getLabel())) {
            String labelPattern = schema.getLabelPattern(entry.getType());
            e.addProperty(schema.getLabel(), Labeler.getLabel(entry, labelPattern));
        }
    }

    private void addTriples(AbstractEntry entry, Resource sourceFile, Resource e, FieldType bibProperty) {
        PropertyHandler h = (PropertyHandler) propertyMap.get(bibProperty);
        if (h != null) {
            h.addTriples(entry, sourceFile, e);
        } else {
            String o = BibtexUtil.getValue(entry, bibProperty);
            Property rdfProperty = schema.getProperty(bibProperty);
            if (rdfProperty != null) {
                if (o != null && !o.equals("")) {
                    BibtexUtil.addProperty(e, o, rdfProperty, schema.getDatatype(rdfProperty));
                }
            } else {
                if (!schema.outputCollectionProperty(bibProperty) && schema.getNamespaceForUnknown() != null) {
                    String ns = schema.getNamespaceForUnknown();
                    rdfProperty = e.getModel().createProperty(ns, bibProperty.getName());
                    BibtexUtil.addProperty(e, o, rdfProperty, schema.getDatatype(rdfProperty));
                } else {
                    if (!ignoredProperties.contains(bibProperty)) {
                        log.warn("ignoring property " + bibProperty + " in entry " + entry.getKey() + "; no handler or RDF property registered");
                        ignoredProperties.add(bibProperty);
                    }
                }
            }
        }
    }

    /**
     * @param string
     * @param r
     * @param model
     */
    private void addType(EntryType type, Resource r) {
        Resource rdfType = schema.getEntryType(type);
        if (rdfType != null) {
            r.addProperty(RDF.type, rdfType);
        }
    }
}
