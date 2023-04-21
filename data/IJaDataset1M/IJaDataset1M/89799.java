package org.chon.jcr.client.service.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import javax.jcr.ItemNotFoundException;
import javax.jcr.Node;
import javax.jcr.NodeIterator;
import javax.jcr.Property;
import javax.jcr.PropertyIterator;
import javax.jcr.PropertyType;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.chon.jcr.client.service.RepoService;
import org.chon.jcr.client.service.model.AttributeType;
import org.chon.jcr.client.service.model.NodeAttribute;
import org.chon.jcr.client.service.model.NodeInfo;
import org.chon.jcr.client.service.model.Status;

public class RepoServicesImpl implements RepoService {

    private static final Log log = LogFactory.getLog(RepoServicesImpl.class);

    private Node getNodeById(Session session, String id) throws ItemNotFoundException, RepositoryException {
        return session.getNodeByIdentifier(id);
    }

    private void editNodeAttributes(Node node, NodeAttribute[] attributes) throws Exception {
        if (attributes != null) {
            for (NodeAttribute attr : attributes) {
                if (AttributeType.BOOLEAN == attr.getType()) {
                    node.setProperty(attr.name, Boolean.valueOf(attr.getData()));
                } else if (AttributeType.NUMBER == attr.getType()) {
                    node.setProperty(attr.name, new BigDecimal(attr.getData()));
                } else if (AttributeType.TEXT == attr.getType()) {
                    node.setProperty(attr.name, attr.getData());
                } else if (AttributeType.DATE == attr.getType()) {
                    Calendar c = Calendar.getInstance();
                    c.setTime(AttributeType.DATE_FORMAT.parse(attr.getData()));
                    node.setProperty(attr.name, c);
                } else {
                    throw new Exception("Invalid attribute type " + attr.getType() + " for attribute name " + attr.getName());
                }
            }
        }
    }

    @Override
    public Status nodeExists(Session session, String path) {
        Status status = Status.DEFAULT;
        try {
            boolean hasNode = session.getRootNode().hasNode(path);
            status = hasNode ? Status.TRUE : Status.FALSE;
        } catch (RepositoryException e) {
            status = new Status(-1, e.getMessage());
        }
        return status;
    }

    @Override
    public Status attrExists(Session session, String id, String attrName) {
        Status status = Status.DEFAULT;
        try {
            boolean hasProp = getNodeById(session, id).hasProperty(attrName);
            status = hasProp ? Status.TRUE : Status.FALSE;
        } catch (Exception e) {
            status = new Status(-1, e.getMessage());
        }
        return status;
    }

    private Node createNode(Session session, String parentId, String name, String primaryType) throws Exception {
        Node parent;
        if (parentId == null) {
            parent = session.getRootNode();
        } else {
            parent = getNodeById(session, parentId);
        }
        if (parent.hasNode(name)) {
            throw new Exception("Node with name " + name + " already exists.");
        }
        Node node = parent.addNode(name, primaryType);
        return node;
    }

    @Override
    public Status createNode(Session session, String parentId, String name, NodeAttribute[] attributes) {
        Status status = Status.DEFAULT;
        try {
            Node node = createNode(session, parentId, name, "nt:unstructured");
            editNodeAttributes(node, attributes);
            node.setProperty("jcr:created", Calendar.getInstance());
            session.save();
            status = new Status(node.getIdentifier());
        } catch (Exception e) {
            status = new Status(-1, e.getMessage());
        }
        return status;
    }

    @Override
    public Status deleteAttr(Session session, String id, String attrName) {
        Status status = Status.DEFAULT;
        try {
            getNodeById(session, id).getProperty(attrName).remove();
            session.save();
            status = Status.OK;
        } catch (Exception e) {
            status = new Status(-1, e.toString());
        }
        return status;
    }

    @Override
    public Status deleteNode(Session session, String id) {
        Status status = Status.DEFAULT;
        try {
            getNodeById(session, id).remove();
            session.save();
            status = Status.OK;
        } catch (Exception e) {
            status = new Status(-1, e.getMessage());
        }
        return status;
    }

    @Override
    public Status editNode(Session session, String id, NodeAttribute[] attributes) {
        Status status = Status.DEFAULT;
        try {
            Node node = getNodeById(session, id);
            editNodeAttributes(node, attributes);
            session.save();
            status = Status.OK;
        } catch (Exception e) {
            status = new Status(-1, e.getMessage());
        }
        return status;
    }

    @Override
    public Status moveNode(Session session, String id, String newParentId) {
        Status status = Status.DEFAULT;
        try {
            Node nodeToBeMoved = getNodeById(session, id);
            Node newParent = getNodeById(session, newParentId);
            session.move(nodeToBeMoved.getPath(), newParent.getPath() + "/" + nodeToBeMoved.getName());
            status = Status.OK;
        } catch (Exception e) {
            status = new Status(-1, e.getMessage());
        }
        return status;
    }

    @Override
    public Status renameNode(Session session, String id, String newName) {
        Status status = Status.DEFAULT;
        try {
            Node node = getNodeById(session, id);
            String path = node.getParent().getPath();
            session.move(node.getPath(), (path.endsWith("/") ? path : path + "/") + newName);
            status = Status.OK;
        } catch (Exception e) {
            status = new Status(-1, e.getMessage());
        }
        return status;
    }

    @SuppressWarnings("deprecation")
    @Override
    public Status addFile(Session session, String nodeId, File file, String mimeType) {
        Status status = Status.DEFAULT;
        FileInputStream fis = null;
        try {
            Node fileNode = createNode(session, nodeId, file.getName(), "nt:file");
            Node resNode = fileNode.addNode("jcr:content", "nt:resource");
            fis = new FileInputStream(file);
            resNode.setProperty("jcr:data", fis);
            Calendar lastModified = Calendar.getInstance();
            lastModified.setTimeInMillis(file.lastModified());
            resNode.setProperty("jcr:lastModified", lastModified);
            if (mimeType != null) {
                resNode.setProperty("jcr:mimeType", mimeType);
            }
            status = new Status(fileNode.getIdentifier());
        } catch (Exception e) {
            status = new Status(-1, e.getMessage());
        } finally {
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException e) {
                }
            }
        }
        return status;
    }

    private NodeAttribute[] listAttr(Node node) throws Exception {
        PropertyIterator pi = node.getProperties();
        int size = (int) pi.getSize();
        NodeAttribute[] arr = new NodeAttribute[size];
        int i = 0;
        while (pi.hasNext()) {
            Property p = pi.nextProperty();
            String name = p.getName();
            AttributeType type = null;
            String value = null;
            if (!p.isMultiple()) {
                switch(p.getType()) {
                    case PropertyType.BOOLEAN:
                        value = p.getBoolean() ? "true" : "false";
                        type = AttributeType.BOOLEAN;
                        break;
                    case PropertyType.LONG:
                    case PropertyType.DECIMAL:
                    case PropertyType.DOUBLE:
                        value = p.getString();
                        type = AttributeType.NUMBER;
                        break;
                    case PropertyType.STRING:
                        value = p.getString();
                        type = AttributeType.TEXT;
                        break;
                    case PropertyType.DATE:
                        value = AttributeType.DATE_FORMAT.format(p.getDate().getTime());
                        type = AttributeType.DATE;
                        break;
                    case PropertyType.NAME:
                        value = p.getValue().getString();
                        break;
                    default:
                        value = ":::>UNDEFINED";
                }
            } else {
                value = ":::>[TODO: MULTIPLE]";
            }
            arr[i++] = new NodeAttribute(name, type, value);
        }
        return arr;
    }

    public NodeAttribute[] listAttr(Session session, String id) throws Exception {
        Node node = getNodeById(session, id);
        return listAttr(node);
    }

    private NodeInfo getNodeInfo(Node node, int depth, boolean includeAttributes) throws Exception {
        NodeAttribute[] attributes = null;
        if (includeAttributes) {
            attributes = listAttr(node);
        }
        List<NodeInfo> childs = null;
        if (depth > 0) {
            childs = new ArrayList<NodeInfo>();
            NodeIterator it = node.getNodes();
            while (it.hasNext()) {
                Node c = it.nextNode();
                NodeInfo cni = getNodeInfo(c, depth - 1, includeAttributes);
                childs.add(cni);
            }
        }
        NodeInfo ni = new NodeInfo(node.getIdentifier(), node.getName(), node.getPath(), attributes, childs, getNodeType(node), node);
        return ni;
    }

    private String getNodeType(Node node) {
        try {
            String type = node.getPrimaryNodeType().getName();
            if (node.hasProperty("type")) {
                return node.getProperty("type").getString();
            }
            return type;
        } catch (RepositoryException e) {
            log.error("Error while getting node type", e);
        }
        return null;
    }

    @Override
    public NodeInfo getNode(Session session, String id, int depth, boolean includeAttributes) throws Exception {
        Node node;
        if (id == null) {
            node = session.getRootNode();
        } else {
            node = getNodeById(session, id);
        }
        return getNodeInfo(node, depth, includeAttributes);
    }
}
