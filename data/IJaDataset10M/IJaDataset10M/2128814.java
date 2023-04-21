package net.sf.archimede.model.metadata;

import java.io.InputStream;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.jcr.ItemExistsException;
import javax.jcr.Node;
import javax.jcr.NodeIterator;
import javax.jcr.PathNotFoundException;
import javax.jcr.Property;
import javax.jcr.PropertyIterator;
import javax.jcr.PropertyType;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.jcr.Value;
import javax.jcr.ValueFormatException;
import javax.jcr.lock.LockException;
import javax.jcr.nodetype.ConstraintViolationException;
import javax.jcr.nodetype.NodeDefinition;
import javax.jcr.nodetype.NodeType;
import javax.jcr.nodetype.NodeTypeIterator;
import javax.jcr.nodetype.NodeTypeManager;
import javax.jcr.version.VersionException;
import net.sf.archimede.NodesTypes;
import net.sf.archimede.model.CredentialsWrapper;
import net.sf.archimede.model.DatabaseUtil;
import net.sf.archimede.model.ObjectExistsException;
import net.sf.archimede.model.ObjectLockedException;
import net.sf.archimede.model.content.ContentDao;
import net.sf.archimede.model.folder.Folder;
import net.sf.archimede.model.folder.FolderDao;
import net.sf.archimede.model.metadata.AttributeTypeEnum.UnsupportedTypeException;
import net.sf.archimede.util.ISO9075;

public class MetadataDao {

    public static final String ROOT_ELEMENT_NAME = "rootElement";

    public static final String NAME = "metadata";

    public static final String VALUE_ELEMENT_PROPERTY_NAME = "value";

    public static final String VALUE_ELEMENT_NAME = "valueElement";

    public static final String VALUE_ELEMENT_HOLDER = "valueElementHolder";

    private MetadataDao() {
    }

    public static MetadataDao createInstance() {
        return new MetadataDao();
    }

    public List listTypes() {
        List types = new ArrayList();
        try {
            Session session = (Session) DatabaseUtil.getSingleton().getDaoSession();
            NodeTypeManager ntm = session.getWorkspace().getNodeTypeManager();
            for (NodeTypeIterator nti = ntm.getPrimaryNodeTypes(); nti.hasNext(); ) {
                NodeType nt = nti.nextNodeType();
                if (nt.isNodeType(NodesTypes.PREFIX + NAME) && !nt.getName().equals(NodesTypes.PREFIX + NAME)) {
                    types.add(nt.getName());
                }
            }
        } catch (RepositoryException e) {
            throw new RuntimeException(e);
        }
        return types;
    }

    private Element loadElementFromNode(Metadata metadata, Node node) throws RepositoryException {
        return loadElementFromNode(metadata, null, node);
    }

    private Element loadElementFromNode(Metadata metadata, Element parent, Node node) throws RepositoryException {
        if (metadata == null) {
            throw new NullPointerException("Metadata reference is null");
        }
        Element rootElement;
        if (node.isNodeType(NodesTypes.PREFIX + MetadataDao.VALUE_ELEMENT_NAME)) {
            rootElement = new ValueElementImpl(metadata, parent);
        } else {
            rootElement = new DefaultElementImpl(metadata, parent);
            rootElement.setName(ISO9075.decode(node.getName()));
        }
        List elements = new ArrayList();
        for (NodeIterator ni = node.getNodes(); ni.hasNext(); ) {
            Element element = loadElementFromNode(metadata, rootElement, ni.nextNode());
            elements.add(element);
        }
        rootElement.setElements(elements);
        List attributes = new ArrayList();
        for (PropertyIterator pi = node.getProperties(); pi.hasNext(); ) {
            Property currentProperty = pi.nextProperty();
            if (currentProperty.getName().startsWith("jcr:")) {
                continue;
            }
            AttributeTypeEnum attributeTypeEnum;
            try {
                attributeTypeEnum = AttributeTypeEnum.getAttributeTypeFromJcrType(currentProperty.getType());
            } catch (UnsupportedTypeException e) {
                throw new RuntimeException(e);
            }
            Attribute attribute = this.getAttributeFromValue(rootElement, attributeTypeEnum, currentProperty.getValue());
            attribute.setName(ISO9075.decode(currentProperty.getName()));
            attributes.add(attribute);
        }
        rootElement.setAttributes(attributes);
        List childElementTypes = new ArrayList();
        NodeType nodeType = node.getPrimaryNodeType();
        String primaryName = nodeType.getPrimaryItemName();
        NodeDefinition[] childDefinitions = nodeType.getChildNodeDefinitions();
        for (int i = 0; i < childDefinitions.length; i++) {
            NodeDefinition nodeDefinition = childDefinitions[i];
            NodeType currentNodeType = nodeDefinition.getDefaultPrimaryType();
            ElementType elementType = new ElementType(nodeDefinition.getName(), currentNodeType.getName());
            if (currentNodeType.getName().equals(primaryName)) {
                rootElement.setDefaultElementType(elementType);
            } else {
                childElementTypes.add(elementType);
            }
        }
        rootElement.setNamedChildElementTypes(childElementTypes);
        return rootElement;
    }

    private Attribute getAttributeFromValue(Element rootElement, AttributeTypeEnum attributeTypeEnum, Value value) throws ValueFormatException, IllegalStateException, RepositoryException {
        Attribute attribute = new AttributeImpl(rootElement, attributeTypeEnum);
        int type = value.getType();
        if (type == PropertyType.STRING) {
            attribute.setValue(value.getString());
        } else if (type == PropertyType.BINARY) {
            attribute = (Attribute) Proxy.newProxyInstance(attribute.getClass().getClassLoader(), attribute.getClass().getInterfaces(), new LazyAttributeInvocationHandler((AttributeImpl) attribute));
        } else if (type == PropertyType.LONG) {
            attribute.setValue(new Long(value.getLong()));
        } else if (type == PropertyType.DOUBLE) {
            attribute.setValue(new Double(value.getDouble()));
        } else if (type == PropertyType.DATE) {
            attribute.setValue(value.getDate());
        } else if (type == PropertyType.BOOLEAN) {
            attribute.setValue(new Boolean(value.getBoolean()));
        }
        return attribute;
    }

    public Metadata retrieve(String id) {
        try {
            Session session = (Session) DatabaseUtil.getSingleton().getDaoSession();
            Node node = session.getNodeByUUID(id);
            Folder folder;
            folder = FolderDao.createInstance().retrieve(node.getParent().getParent().getUUID());
            System.out.println("Name retrieved: " + node.getName());
            String name = ISO9075.decode(node.getName());
            MetadataImpl retrievedMetadata = new MetadataImpl(folder);
            retrievedMetadata.setId(id);
            retrievedMetadata.setName(name);
            NodeIterator ni = node.getNodes();
            if (ni.hasNext()) {
                Node currentNode = ni.nextNode();
                Element rootElement = loadElementFromNode(retrievedMetadata, currentNode);
                retrievedMetadata.setRootElement(rootElement);
            }
            if (ni.hasNext()) {
                throw new RuntimeException("More than one root element found for metadata: " + node.getPath());
            }
            ContentDao contentDao = ContentDao.createInstance();
            contentDao.populateFromRepository(retrievedMetadata, session);
            return retrievedMetadata;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void saveElement(Element rootElement, Node parentNode) throws ItemExistsException, PathNotFoundException, VersionException, ConstraintViolationException, LockException, RepositoryException {
        String isoName = ISO9075.encode(rootElement.getName());
        Node elementNode;
        if (rootElement.getParent() == null) {
            if (!parentNode.hasNode(isoName)) {
                elementNode = parentNode.addNode(isoName);
            } else {
                elementNode = parentNode.getNode(isoName);
            }
        } else {
            elementNode = parentNode.addNode(isoName);
        }
        List attributes = rootElement.getAttributes();
        for (Iterator attrIt = attributes.iterator(); attrIt.hasNext(); ) {
            Attribute attribute = (Attribute) attrIt.next();
            AttributeTypeEnum type = attribute.getType();
            if (type == AttributeTypeEnum.BOOLEAN_TYPE) {
                elementNode.setProperty(ISO9075.encode(attribute.getName()), ((Boolean) attribute.getValue()).booleanValue());
            } else if (type == AttributeTypeEnum.CALENDAR_TYPE) {
                parentNode.setProperty(ISO9075.encode(attribute.getName()), ((Calendar) attribute.getValue()));
            } else if (type == AttributeTypeEnum.DOUBLE_TYPE) {
                elementNode.setProperty(ISO9075.encode(attribute.getName()), ((Double) attribute.getValue()).doubleValue());
            } else if (type == AttributeTypeEnum.LONG_TYPE) {
                elementNode.setProperty(ISO9075.encode(attribute.getName()), ((Long) attribute.getValue()).longValue());
            } else if (type == AttributeTypeEnum.INPUT_STREAM_TYPE) {
                elementNode.setProperty(ISO9075.encode(attribute.getName()), ((InputStream) attribute.getValue()));
            } else if (type == AttributeTypeEnum.STRING_TYPE) {
                elementNode.setProperty(ISO9075.encode(attribute.getName()), ((String) attribute.getValue()));
            } else {
                throw new RuntimeException("Invalid type for attribute");
            }
        }
        List elements = rootElement.getElements();
        if (elements != null) {
            for (Iterator it = elements.iterator(); it.hasNext(); ) {
                Element element = (Element) it.next();
                saveElement(element, elementNode);
            }
        }
    }

    public void save(Metadata metadata) {
        try {
            Folder parentFolder = metadata.getParent();
            if (parentFolder == null) {
                throw new IllegalArgumentException("Parent folder cannot be null");
            } else if (parentFolder.getId() == null) {
                throw new IllegalArgumentException("Parent folder needs to be saved first.");
            }
            String parentId = parentFolder.getId();
            Session session = (Session) DatabaseUtil.getSingleton().getDaoSession();
            Node parentNode = session.getNodeByUUID(parentId);
            Node metadataNode;
            for (Iterator it = parentFolder.getMetadatas().iterator(); it.hasNext(); ) {
                Metadata currentMetadata = (Metadata) it.next();
                if (currentMetadata.getName().equals(metadata.getName()) && currentMetadata.getId() != null) {
                    throw new IllegalArgumentException("This metadata already exists");
                }
            }
            if (parentFolder.getMetadatas() != null) {
                Node metadatasNode = parentNode.getNode(NodesTypes.PREFIX + FolderDao.METADATAS_NODE_NAME);
                metadataNode = metadatasNode.addNode(ISO9075.encode(metadata.getName()), metadata.getName());
            } else {
                throw new IllegalArgumentException("Metadata is not registered to a folder");
            }
            if (metadata.getRootElement() != null) {
                saveElement(metadata.getRootElement(), metadataNode);
            }
            ContentDao contentDao = ContentDao.createInstance();
            if (metadataNode.isNodeType(NodesTypes.PREFIX + ContentDao.NAME)) {
                contentDao.writeToNode(metadata, metadataNode);
            }
            session.save();
            ((MetadataImpl) metadata).setId(metadataNode.getUUID());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void remove(Metadata metadata) throws ObjectLockedException, ObjectExistsException {
        try {
            Session session = (Session) DatabaseUtil.getSingleton().getDaoSession();
            Metadata retrievedCopy = this.retrieve(metadata.getId());
            Node node = session.getNodeByUUID(retrievedCopy.getId());
            node.remove();
            session.save();
        } catch (LockException e) {
            throw new ObjectLockedException(metadata);
        } catch (ItemExistsException e) {
            throw new ObjectExistsException(metadata);
        } catch (RepositoryException e) {
            throw new RuntimeException(e);
        }
    }

    public void update(Metadata metadata) throws ObjectLockedException, ObjectExistsException {
        try {
            Session session = (Session) DatabaseUtil.getSingleton().getDaoSession();
            Metadata retrievedCopy = this.retrieve(metadata.getId());
            Node node = session.getNodeByUUID(retrievedCopy.getId());
            node.remove();
            metadata.setId(null);
            Folder parentFolder = metadata.getParent();
            List metadatas = parentFolder.getMetadatas();
            if (!metadatas.contains(metadata)) {
                metadatas.add(metadata);
            }
            this.save(metadata);
            session.save();
        } catch (LockException e) {
            throw new ObjectLockedException(metadata);
        } catch (ItemExistsException e) {
            throw new ObjectExistsException(metadata);
        } catch (RepositoryException e) {
            throw new RuntimeException(e);
        }
    }

    private class LazyAttributeInvocationHandler implements InvocationHandler {

        private AttributeImpl attribute;

        public LazyAttributeInvocationHandler(AttributeImpl attribute) {
            this.attribute = attribute;
        }

        public Object invoke(Object subject, Method method, Object[] args) throws IllegalArgumentException {
            try {
                Session session = (Session) DatabaseUtil.getSingleton().getDaoSession();
                if (method.getName().startsWith("getValue")) {
                    StringBuffer path = new StringBuffer();
                    path.insert(0, "/" + ISO9075.encode(attribute.getName()));
                    Element parent = attribute.getParent();
                    path.insert(0, "/" + ISO9075.encode(parent.getName()));
                    while (parent.getParent() != null) {
                        parent = parent.getParent();
                        path.insert(0, "/" + ISO9075.encode(parent.getName()));
                    }
                    path.delete(0, 1);
                    Node metadataNode = (Node) session.getItem(((MetadataImpl) parent.getMetadataParent()).getId());
                    Property property = metadataNode.getProperty(path.toString());
                    InputStream inputStream = property.getStream();
                    attribute.setValue(inputStream);
                    return attribute.getValue();
                } else {
                    return method.invoke(attribute, args);
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        public boolean equals(Object object) {
            return this.attribute.equals(object);
        }

        public int hashCode() {
            return this.attribute.hashCode();
        }
    }
}
