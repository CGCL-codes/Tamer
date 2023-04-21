package org.netbeans.cubeon.trac.query;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import org.netbeans.cubeon.tasks.spi.query.TaskQuery;
import org.netbeans.cubeon.trac.repository.TracTaskRepository;
import org.openide.filesystems.FileLock;
import org.openide.filesystems.FileObject;
import org.openide.util.Exceptions;
import org.openide.xml.XMLUtil;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/**
 *
 * @author Anuradha G
 */
class PersistenceHandler {

    private static final String FILESYSTEM_FILE_TAG = "queries.xml";

    private static final String TAG_ROOT = "querys";

    private static final String TAG_QUERYS = "querys";

    private static final String TAG_QUERY = "query";

    private static final String TAG_IDS = "ids";

    private static final String TAG_ID = "id";

    private static final String TAG_NAME = "name";

    private static final String TAG_TRAC_QUERY = "trac_query";

    private static final String TAG_TYPE = "type";

    private static final String TAG_TASK = "task";

    private static final String TAG_NEXT_ID = "next";

    private TracQuerySupport querySupport;

    private FileObject baseDir;

    private static final Object LOCK = new Object();

    PersistenceHandler(TracQuerySupport querySupport, FileObject fileObject) {
        this.querySupport = querySupport;
        this.baseDir = fileObject;
    }

    void vaidate(TaskQuery query) {
    }

    void addTaskQuery(AbstractTracQuery abstractTracQuery) {
        synchronized (LOCK) {
            Document document = getDocument();
            Element root = getRootElement(document);
            Element tasksElement = findElement(root, TAG_QUERYS);
            if (tasksElement == null) {
                tasksElement = document.createElement(TAG_QUERYS);
                root.appendChild(tasksElement);
            }
            Element taskQuery = null;
            NodeList nodeList = tasksElement.getElementsByTagName(TAG_QUERY);
            for (int i = 0; i < nodeList.getLength(); i++) {
                Node node = nodeList.item(i);
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element element = (Element) node;
                    String id = element.getAttribute(TAG_ID);
                    if (abstractTracQuery.getId().equals(id)) {
                        taskQuery = element;
                        break;
                    }
                }
            }
            if (taskQuery == null) {
                taskQuery = document.createElement(TAG_QUERY);
                tasksElement.appendChild(taskQuery);
            }
            taskQuery.setAttribute(TAG_ID, abstractTracQuery.getId());
            taskQuery.setAttribute(TAG_TYPE, abstractTracQuery.getType().name());
            switch(abstractTracQuery.getType()) {
                case FILTER:
                    {
                        TracFilterQuery filterQuery = abstractTracQuery.getLookup().lookup(TracFilterQuery.class);
                        if (filterQuery.getQuery() != null) {
                            taskQuery.setAttribute(TAG_TRAC_QUERY, filterQuery.getQuery());
                        }
                        taskQuery.setAttribute(TAG_NAME, filterQuery.getName());
                        Element idsElement = getEmptyElement(document, taskQuery, TAG_IDS);
                        for (String id : filterQuery.getIds()) {
                            Element idElement = document.createElement(TAG_TASK);
                            idsElement.appendChild(idElement);
                            idElement.setAttribute(TAG_ID, id);
                        }
                    }
                    break;
            }
            save(document);
        }
    }

    private Element getEmptyElement(Document document, Element root, String tag) {
        Element taskpriorities = findElement(root, tag);
        if (taskpriorities != null) {
            root.removeChild(taskpriorities);
        }
        taskpriorities = document.createElement(tag);
        root.appendChild(taskpriorities);
        return taskpriorities;
    }

    void removeTaskQuery(TaskQuery query) {
        synchronized (LOCK) {
            Document document = getDocument();
            Element root = getRootElement(document);
            Element tasksElement = findElement(root, TAG_QUERYS);
            Element taskElement = null;
            NodeList taskNodes = tasksElement.getElementsByTagName(TAG_QUERY);
            for (int i = 0; i < taskNodes.getLength(); i++) {
                Node node = taskNodes.item(i);
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element element = (Element) node;
                    String id = element.getAttribute(TAG_ID);
                    if (query.getId().equals(id)) {
                        taskElement = element;
                        break;
                    }
                }
            }
            assert taskElement != null;
            tasksElement.removeChild(taskElement);
            save(document);
        }
    }

    String nextTaskId() {
        String id = querySupport.getTaskRepository().getId().toUpperCase();
        synchronized (LOCK) {
            Document document = getDocument();
            Element root = getRootElement(document);
            Element nextElement = findElement(root, TAG_NEXT_ID);
            int nextID = 0;
            if (nextElement == null) {
                nextElement = document.createElement(TAG_NEXT_ID);
                nextElement.setAttribute(TAG_ID, String.valueOf(++nextID));
                root.appendChild(nextElement);
            } else {
                nextID = Integer.parseInt(nextElement.getAttribute(TAG_ID));
                nextElement.setAttribute(TAG_ID, String.valueOf(++nextID));
            }
            save(document);
            id = id + "-" + nextID;
        }
        return id;
    }

    void refresh() {
        synchronized (LOCK) {
            List<AbstractTracQuery> localQuerys = new ArrayList<AbstractTracQuery>();
            Document document = getDocument();
            Element root = getRootElement(document);
            Element tasksElement = findElement(root, TAG_QUERYS);
            if (tasksElement != null) {
                NodeList taskNodes = tasksElement.getElementsByTagName(TAG_QUERY);
                for (int i = 0; i < taskNodes.getLength(); i++) {
                    Node node = taskNodes.item(i);
                    if (node.getNodeType() == Node.ELEMENT_NODE) {
                        Element element = (Element) node;
                        String id = element.getAttribute(TAG_ID);
                        String typeString = element.getAttribute(TAG_TYPE);
                        AbstractTracQuery.Type type = AbstractTracQuery.Type.valueOf(typeString);
                        AbstractTracQuery abstractTracQuery = null;
                        switch(type) {
                            case FILTER:
                                {
                                    TracFilterQuery filterQuery = new TracFilterQuery(querySupport.getTaskRepository(), id);
                                    String name = element.getAttribute(TAG_NAME);
                                    String query = element.getAttribute(TAG_TRAC_QUERY);
                                    TracTaskRepository repository = querySupport.getTaskRepository();
                                    Element idsElement = findElement(element, TAG_IDS);
                                    List<String> ids = new ArrayList<String>();
                                    if (idsElement != null) {
                                        NodeList idsNodeList = idsElement.getElementsByTagName(TAG_TASK);
                                        for (int j = 0; j < idsNodeList.getLength(); j++) {
                                            Node idNode = idsNodeList.item(j);
                                            if (idNode.getNodeType() == Node.ELEMENT_NODE) {
                                                Element idElement = (Element) idNode;
                                                String idTag = idElement.getAttribute(TAG_ID);
                                                ids.add(idTag);
                                            }
                                        }
                                    }
                                    filterQuery.setName(name);
                                    filterQuery.setQuery(query);
                                    filterQuery.setIds(ids);
                                    abstractTracQuery = filterQuery;
                                }
                                break;
                        }
                        localQuerys.add(abstractTracQuery);
                    }
                }
                querySupport.setTaskQuery(localQuerys);
            }
        }
    }

    private Document getDocument() {
        final FileObject config = baseDir.getFileObject(FILESYSTEM_FILE_TAG);
        Document doc = null;
        if (config != null) {
            InputStream in = null;
            try {
                in = config.getInputStream();
                doc = XMLUtil.parse(new InputSource(in), false, true, null, null);
            } catch (SAXException ex) {
                Exceptions.printStackTrace(ex);
            } catch (IOException ex) {
                Exceptions.printStackTrace(ex);
            } finally {
                if (in != null) {
                    try {
                        in.close();
                    } catch (IOException ex) {
                        Exceptions.printStackTrace(ex);
                    }
                }
            }
        } else {
            doc = XMLUtil.createDocument(TAG_ROOT, null, null, null);
        }
        return doc;
    }

    private Element getRootElement(Document doc) {
        Element rootElement = doc.getDocumentElement();
        if (rootElement == null) {
            rootElement = doc.createElement(TAG_ROOT);
        }
        return rootElement;
    }

    private void save(Document doc) {
        FileObject config = baseDir.getFileObject(FILESYSTEM_FILE_TAG);
        FileLock lck = null;
        OutputStream out = null;
        try {
            if (config == null) {
                config = baseDir.createData(FILESYSTEM_FILE_TAG);
            }
            lck = config.lock();
            out = config.getOutputStream(lck);
            XMLUtil.write(doc, out, "UTF-8");
        } catch (IOException ex) {
            Exceptions.printStackTrace(ex);
        } finally {
            if (out != null) {
                try {
                    out.close();
                } catch (IOException ex) {
                    Exceptions.printStackTrace(ex);
                }
            }
            if (lck != null) {
                lck.releaseLock();
            }
        }
    }

    private static Element findElement(Element parent, String name) {
        NodeList l = parent.getChildNodes();
        int len = l.getLength();
        for (int i = 0; i < len; i++) {
            if (l.item(i).getNodeType() == Node.ELEMENT_NODE) {
                Element el = (Element) l.item(i);
                if (name.equals(el.getNodeName())) {
                    return el;
                }
            }
        }
        return null;
    }
}
