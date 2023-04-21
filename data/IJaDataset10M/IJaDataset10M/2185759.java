package BisnessLogic.VFS;

import java.io.*;
import java.util.*;
import javax.xml.transform.*;
import javax.xml.transform.dom.*;
import org.w3c.dom.*;
import javax.xml.parsers.*;
import javax.xml.transform.stream.StreamResult;

/**
 * All work with VFS
 * @author Андрейка
 */
public class VirtualFileSystem implements VFSInterface {

    /**
     * Load VFS or create new VFS if file not found
     * @param path real path to VFS file
     * @return loaded or not loaded
     */
    public boolean loadVFS(String path) {
        File f = new File(path);
        try {
            Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(f);
            root = doc;
            return true;
        } catch (Exception e) {
            try {
                Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
                root = doc;
                Element el;
                el = root.getOwnerDocument().createElement("c:");
                root.appendChild(el);
            } catch (ParserConfigurationException ex) {
                return false;
            }
            return false;
        }
    }

    /**
     * Save VFS to hdd
     * @param path real path to VFS file
     */
    public void saveVFS(String path) {
        File f = new File(path);
        Transformer transformer;
        try {
            transformer = TransformerFactory.newInstance().newTransformer();
            transformer.transform(new DOMSource(root), new StreamResult(f));
        } catch (Exception e) {
            System.out.println("file not saved");
        }
    }

    /**
     * Is directory or file exist?
     * @param path pathway to directory or file
     * @return result of exist
     */
    public boolean isExisted(String path) {
        boolean result = true;
        StringTokenizer restructed = restructPath(path);
        Node vrem = root;
        String nextString = restructed.nextToken();
        if (nextString.compareTo("c:") == 0) {
            vrem = ((Document) vrem).getDocumentElement();
            while ((result) && (restructed.hasMoreTokens())) {
                nextString = restructed.nextToken();
                if (getFolderByName(vrem, nextString) != null) {
                    vrem = getFolderByName(vrem, nextString);
                } else result = false;
            }
        } else result = false;
        return result;
    }

    public String createDir(String path) {
        if ((getWithoutLastPart(path) != null) && (getLastPartOfPath(path) != null)) {
            String folder = getWithoutLastPart(path);
            String name = getLastPartOfPath(path);
            if (isDirExisted(folder)) {
                StringTokenizer restructed = restructPath(folder);
                Node vrem = root;
                String nextString = restructed.nextToken();
                if (nextString.compareTo("c:") == 0) {
                    vrem = ((Document) vrem).getDocumentElement();
                    while (restructed.hasMoreTokens()) {
                        nextString = restructed.nextToken();
                        if (getFolderByName(vrem, nextString) != null) {
                            vrem = getFolderByName(vrem, nextString);
                        }
                    }
                }
                Element element;
                element = vrem.getOwnerDocument().createElement("folder");
                element.setAttribute("name", name);
                vrem.appendChild(element);
                return "Directory '" + path + "' was created successfull.";
            } else return "Error: folder '" + folder + "' not exist.";
        } else return "Error: command false.";
    }

    /**
     * cd command from user
     * @param path pathway
     * @return pathway
     */
    public String changeDir(String path) {
        if (isDirExisted(path)) return path; else return "!!!";
    }

    /**
     * rd command
     * @param path pathway to directory
     * @return message of result of operation
     */
    public String removeDir(String path) {
        if ((getWithoutLastPart(path) != null) && (getLastPartOfPath(path) != null)) {
            String folder = getWithoutLastPart(path);
            String name = getLastPartOfPath(path);
            if (isDirExisted(folder)) {
                StringTokenizer restructed = restructPath(folder);
                Node vrem = root;
                String nextString = restructed.nextToken();
                if (nextString.compareTo("c:") == 0) {
                    vrem = ((Document) vrem).getDocumentElement();
                    while (restructed.hasMoreTokens()) {
                        nextString = restructed.nextToken();
                        if (getFolderByName(vrem, nextString) != null) vrem = getFolderByName(vrem, nextString);
                    }
                }
                Node needDel = getFolderByName(vrem, name);
                if (needDel.getNodeName().compareTo("folder") == 0) {
                    if (needDel.hasChildNodes()) return "Error: This folder is not empty."; else vrem.removeChild(needDel);
                } else return "Error: It is not a folder!";
                return "Folder '" + path + "' was deleted successfull.";
            } else return "Error: folder '" + folder + "' not exist.";
        } else return "Error: command false.";
    }

    /**
     * delete catalog with subdirectories
     * @param path pathway to catalog
     * @return message of result of operation
     */
    public String delTree(String path) {
        if ((getWithoutLastPart(path) != null) && (getLastPartOfPath(path) != null)) {
            String folder = getWithoutLastPart(path);
            String name = getLastPartOfPath(path);
            if (isDirExisted(path)) {
                if (isDirExisted(folder)) {
                    StringTokenizer restructed = restructPath(folder);
                    Node vrem = root;
                    String nextString = restructed.nextToken();
                    if (nextString.compareTo("c:") == 0) {
                        vrem = ((Document) vrem).getDocumentElement();
                        while (restructed.hasMoreTokens()) {
                            nextString = restructed.nextToken();
                            if (getFolderByName(vrem, nextString) != null) vrem = getFolderByName(vrem, nextString);
                        }
                    }
                    Node needDel = getFolderByName(vrem, name);
                    include = false;
                    if (includeLockedFiles(needDel)) {
                        return "Catalog include locked files and cannot be deleted.";
                    } else vrem.removeChild(needDel);
                    return "Directory '" + path + "' was deleted successfull.";
                } else return "Error: folder '" + folder + "' not exist.";
            } else return "Error: It is not directory'" + folder + "'.";
        } else return "Error: comand false.";
    }

    /**
     * Creating file
     * @param path pathway to new file
     * @return essage of result of operation
     */
    public String createFile(String path) {
        if ((getWithoutLastPart(path) != null) && (getLastPartOfPath(path) != null)) {
            String folder = getWithoutLastPart(path);
            String name = getLastPartOfPath(path);
            if (isDirExisted(folder)) {
                StringTokenizer restructed = restructPath(folder);
                Node vrem = root;
                String nextString = restructed.nextToken();
                if (nextString.compareTo("c:") == 0) {
                    vrem = ((Document) vrem).getDocumentElement();
                    while (restructed.hasMoreTokens()) {
                        nextString = restructed.nextToken();
                        if (getFolderByName(vrem, nextString) != null) vrem = getFolderByName(vrem, nextString);
                    }
                }
                Element element;
                element = vrem.getOwnerDocument().createElement("file");
                element.setAttribute("name", name);
                element.setAttribute("lock", "false");
                vrem.appendChild(element);
                return "File '" + path + "' was created successfull.";
            } else return "Error: folder '" + folder + "' not exist.";
        } else return "Error: command false";
    }

    /**
     * delete file
     * @param path pathway to file
     * @return message of result of operation
     */
    public String delFile(String path) {
        if ((getWithoutLastPart(path) != null) && (getLastPartOfPath(path) != null)) {
            String folder = getWithoutLastPart(path);
            String name = getLastPartOfPath(path);
            if (isDirExisted(folder)) {
                StringTokenizer restructed = restructPath(folder);
                Node vrem = root;
                String nextString = restructed.nextToken();
                if (nextString.compareTo("c:") == 0) {
                    vrem = ((Document) vrem).getDocumentElement();
                    while (restructed.hasMoreTokens()) {
                        nextString = restructed.nextToken();
                        if (getFolderByName(vrem, nextString) != null) vrem = getFolderByName(vrem, nextString);
                    }
                }
                Node needDel = getFolderByName(vrem, name);
                if (needDel.getNodeName().compareTo("file") == 0) {
                    NamedNodeMap map = needDel.getAttributes();
                    if ((getAttrValueByName(map, "lock") != null) && (getAttrValueByName(map, "lock").compareTo("true") != 0)) {
                        vrem.removeChild(needDel);
                    } else return "File '" + path + "' is locked and cannot be removed.";
                } else return "Error: It if not a file!";
                return "File '" + path + "' was deleted successfull.";
            } else return "Error: folder '" + folder + "' not exist.";
        } else return "Error: fcommand false.";
    }

    /**
     * set lock or unlock file
     * @param userName user that lock or unlock file
     * @param path pathway to file
     * @param lock true or false <=> lock or unlock
     * @return message of result of operation
     */
    public String setLock(String userName, String path, boolean lock) {
        if ((getWithoutLastPart(path) != null) && (getLastPartOfPath(path) != null)) {
            String folder = getWithoutLastPart(path);
            String name = getLastPartOfPath(path);
            if (isDirExisted(folder)) {
                StringTokenizer restructed = restructPath(folder);
                Node vrem = root;
                String nextString = restructed.nextToken();
                if (nextString.compareTo("c:") == 0) {
                    vrem = ((Document) vrem).getDocumentElement();
                    while (restructed.hasMoreTokens()) {
                        nextString = restructed.nextToken();
                        if (getFolderByName(vrem, nextString) != null) {
                            vrem = getFolderByName(vrem, nextString);
                        }
                    }
                }
                Node needChange = getFolderByName(vrem, name);
                if (lock) {
                    NamedNodeMap map = needChange.getAttributes();
                    boolean flag = false;
                    for (int i = 0; i < map.getLength(); i++) if (((Attr) map.item(i)).getName().compareTo(userName) == 0) {
                        ((Attr) map.item(i)).setValue("true");
                        flag = true;
                    }
                    if (!flag) ((Element) needChange).setAttribute(userName, "true");
                    if (((Element) needChange).getAttribute("lock").compareTo("false") == 0) ((Element) needChange).setAttribute("lock", "true");
                    return "file '" + path + "' is locked by " + userName;
                } else {
                    NamedNodeMap map = needChange.getAttributes();
                    for (int i = 0; i < map.getLength(); i++) {
                        if (((Attr) map.item(i)).getName().compareTo(userName) == 0) {
                            ((Attr) map.item(i)).setValue("false");
                        }
                    }
                    if (getUsersLocked(map).length() < 2) {
                        for (int i = 0; i < map.getLength(); i++) {
                            if (((Attr) map.item(i)).getName().compareTo("lock") == 0) {
                                ((Attr) map.item(i)).setValue("false");
                                return "file '" + path + "' is unlocked";
                            }
                        }
                    }
                    return "file '" + path + "' is unlocked by " + userName;
                }
            } else return "Error: folder '" + folder + "' not exist.";
        } else return "Error: command false.";
    }

    /**
     * copy file or directory
     * @param source from..
     * @param destination to..
     * @return message of result of operation
     */
    public String copy(String source, String destination) {
        if ((getWithoutLastPart(source) != null) && (getLastPartOfPath(source) != null) && (getWithoutLastPart(destination) != null) && (getLastPartOfPath(destination) != null)) {
            String folderS = getWithoutLastPart(source);
            String nameS = getLastPartOfPath(source);
            String folderD = getWithoutLastPart(destination);
            String nameD = getLastPartOfPath(destination);
            Node needCopy;
            if (nameS.compareTo("c:") == 0) return "Cannot copy 'c:' !!!"; else if (isDirExisted(folderS)) {
                StringTokenizer restructed = restructPath(folderS);
                Node vrem = root;
                String nextString = restructed.nextToken();
                if (nextString.compareTo("c:") == 0) {
                    vrem = ((Document) vrem).getDocumentElement();
                    while (restructed.hasMoreTokens()) {
                        nextString = restructed.nextToken();
                        if (getFolderByName(vrem, nextString) != null) vrem = getFolderByName(vrem, nextString);
                    }
                }
                needCopy = getFolderByName(vrem, nameS);
                if (needCopy.getNodeName().compareTo("folder") == 0) {
                    include = false;
                    if (includeLockedFiles(needCopy)) return "Catalog include locked files and cannot be deleted.";
                }
            } else return "Error: folder '" + folderS + "' not exist.";
            if (nameD.compareTo("c:") == 0) {
                Node vrem = ((Document) root).getDocumentElement();
                vrem.appendChild(needCopy.cloneNode(true));
                return "Operation successfully complite.";
            } else if (isDirExisted(destination)) {
                if (isDirExisted(folderD)) {
                    StringTokenizer restructed = restructPath(folderD);
                    Node vrem = root;
                    String nextString = restructed.nextToken();
                    if (nextString.compareTo("c:") == 0) {
                        vrem = ((Document) vrem).getDocumentElement();
                        while (restructed.hasMoreTokens()) {
                            nextString = restructed.nextToken();
                            if (getFolderByName(vrem, nextString) != null) vrem = getFolderByName(vrem, nextString);
                        }
                    }
                    Node toCopy = getFolderByName(vrem, nameD);
                    toCopy.appendChild(needCopy.cloneNode(true));
                    return "Operation successfully complite.";
                } else return "Error: folder '" + folderD + "' not exist.";
            } else return "Error: It is not directory'" + destination + "'.";
        } else return "Error: command false.";
    }

    /**
     * move file or directory
     * @param source from..
     * @param destination to..
     * @return message of result of operation
     */
    public String move(String source, String destination) {
        if ((getWithoutLastPart(source) != null) && (getLastPartOfPath(source) != null) && (getWithoutLastPart(destination) != null) && (getLastPartOfPath(destination) != null)) {
            String folderS = getWithoutLastPart(source);
            String nameS = getLastPartOfPath(source);
            String folderD = getWithoutLastPart(destination);
            String nameD = getLastPartOfPath(destination);
            Node needMove;
            Node fromMove;
            if (nameS.compareTo("c:") == 0) {
                return "Cannot move 'c:' !!!";
            } else if (isDirExisted(folderS)) {
                StringTokenizer restructed = restructPath(folderS);
                Node vrem = root;
                String nextString = restructed.nextToken();
                if (nextString.compareTo("c:") == 0) {
                    vrem = ((Document) vrem).getDocumentElement();
                    while (restructed.hasMoreTokens()) {
                        nextString = restructed.nextToken();
                        if (getFolderByName(vrem, nextString) != null) vrem = getFolderByName(vrem, nextString);
                    }
                }
                fromMove = vrem;
                needMove = getFolderByName(vrem, nameS);
                if (needMove.getNodeName().compareTo("folder") == 0) {
                    include = false;
                    if (includeLockedFiles(needMove)) return "Catalog include locked files and cannot be deleted.";
                }
            } else return "Error: folder '" + folderS + "' not exist.";
            if (nameD.compareTo("c:") == 0) {
                Node vrem = ((Document) root).getDocumentElement();
                vrem.appendChild(needMove.cloneNode(true));
                fromMove.removeChild(needMove);
                return "Operation successfully complite.";
            } else if (isDirExisted(destination)) {
                if (isDirExisted(folderD)) {
                    StringTokenizer restructed = restructPath(folderD);
                    Node vrem = root;
                    String nextString = restructed.nextToken();
                    if (nextString.compareTo("c:") == 0) {
                        vrem = ((Document) vrem).getDocumentElement();
                        while (restructed.hasMoreTokens()) {
                            nextString = restructed.nextToken();
                            if (getFolderByName(vrem, nextString) != null) vrem = getFolderByName(vrem, nextString);
                        }
                    }
                    Node toCopy = getFolderByName(vrem, nameD);
                    toCopy.appendChild(needMove.cloneNode(true));
                    fromMove.removeChild(needMove);
                    return "Operation successfully complite.";
                } else return "Error: folder '" + folderD + "' not exist.";
            } else return "Error: It is not directory'" + destination + "'.";
        } else return "Error: command false.";
    }

    /**
     * Print tree of vfs
     * @return tree in text form
     */
    public String print() {
        return printDomTree(root, 0);
    }

    /**
     * Get terminal name in path
     * @param path pathway
     * @return name of terminal folder or file in path
     */
    private String getLastPartOfPath(String path) {
        try {
            StringTokenizer st = restructPath(path);
            String result = null;
            while (st.hasMoreTokens()) result = st.nextToken();
            return result;
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Get path to terminal folder or file
     * @param path pathway
     * @return path to terminal folder or file
     */
    private String getWithoutLastPart(String path) {
        try {
            String last = getLastPartOfPath(path);
            String newpath = path;
            return newpath.replaceAll(last, "");
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Get node in root, path to folder
     * @param node
     * @param name
     * @return
     */
    private Node getFolderByName(Node node, String name) {
        Node result = null;
        if (node.hasChildNodes()) {
            NodeList children = node.getChildNodes();
            for (int i = 0; i < children.getLength(); i++) {
                node = children.item(i);
                NamedNodeMap map = node.getAttributes();
                try {
                    if ((getAttrValueByName(map, "name") != null) && (getAttrValueByName(map, "name").compareToIgnoreCase(name) == 0)) {
                        result = node;
                        break;
                    }
                } catch (Exception e) {
                }
            }
        }
        return result;
    }

    /**
     * divide break down String into parts
     * @param path (String)
     * @return  parts
     */
    private StringTokenizer restructPath(String path) {
        String s = path.replace('\\', ' ');
        return new StringTokenizer(s.toString());
    }

    /**
     * get string with users, that blocked this file
     * @param attrs attributes
     * @return string with users, that blocked this file
     */
    private String getUsersLocked(NamedNodeMap attrs) {
        String result = "";
        for (int i = 0; i < attrs.getLength(); i++) if ((((Attr) attrs.item(i)).getName().compareTo("name") != 0) && ((((Attr) attrs.item(i)).getName().compareTo("lock") != 0))) if (((Attr) attrs.item(i)).getValue().compareTo("true") == 0) result += ((Attr) attrs.item(i)).getName() + ";";
        return result;
    }

    /**
     * it include locked files
     * @param node source
     * @return include or not
     */
    private boolean includeLockedFiles(Node node) {
        int type = node.getNodeType();
        switch(type) {
            case Node.DOCUMENT_NODE:
                {
                    res = "";
                    includeLockedFiles(((Document) node).getDocumentElement());
                    break;
                }
            case Node.ELEMENT_NODE:
                {
                    NamedNodeMap attrs = node.getAttributes();
                    if ((getAttrValueByName(attrs, "lock") != null) && (getAttrValueByName(attrs, "lock").compareTo("true") == 0)) include = true;
                    if (node.hasChildNodes()) {
                        NodeList children = node.getChildNodes();
                        for (int i = 0; i < children.getLength(); i++) includeLockedFiles(children.item(i));
                    }
                    break;
                }
        }
        return include;
    }

    /**
     * Get attribut of node
     * @param attrs all atributes
     * @param name name of atrinbute
     * @return value of attribute
     */
    private String getAttrValueByName(NamedNodeMap attrs, String name) {
        String result = null;
        for (int i = 0; i < attrs.getLength(); i++) if (((Attr) attrs.item(i)).getName().compareTo(name) == 0) result = ((Attr) attrs.item(i)).getValue();
        return result;
    }

    /**
     * Print text tree
     * @param node head of tree
     * @param rec recurs level
     * @return text tree
     */
    private String printDomTree(Node node, int rec) {
        int type = node.getNodeType();
        switch(type) {
            case Node.DOCUMENT_NODE:
                {
                    res = "";
                    printDomTree(((Document) node).getDocumentElement(), rec);
                    break;
                }
            case Node.ELEMENT_NODE:
                {
                    if (node.getNodeName().compareTo("c:") == 0) res += node.getNodeName(); else {
                        for (int i = 0; i < rec; i++) res += "|_";
                    }
                    NamedNodeMap attrs = node.getAttributes();
                    if (getAttrValueByName(attrs, "name") != null) res += getAttrValueByName(attrs, "name");
                    if ((getAttrValueByName(attrs, "lock") != null) && (getAttrValueByName(attrs, "lock").compareTo("true") == 0)) res += " [Locked by " + getUsersLocked(attrs) + "]";
                    res += "\n";
                    if (node.hasChildNodes()) {
                        NodeList children = node.getChildNodes();
                        for (int i = 0; i < children.getLength(); i++) printDomTree(children.item(i), rec + 1);
                    }
                    break;
                }
        }
        return res;
    }

    /**
     * directory exist or not exist
     * @param path pathway to directory
     * @return exist or not exist (true or false)
     */
    private boolean isDirExisted(String path) {
        boolean result = true;
        StringTokenizer restructed = restructPath(path);
        Node vrem = root;
        String nextString = restructed.nextToken();
        if (nextString.compareTo("c:") == 0) {
            vrem = ((Document) vrem).getDocumentElement();
            while ((result) && (restructed.hasMoreTokens())) {
                nextString = restructed.nextToken();
                if (getFolderByName(vrem, nextString) != null) {
                    vrem = getFolderByName(vrem, nextString);
                    if (vrem.getNodeName().compareTo("file") == 0) result = false;
                } else result = false;
            }
        } else result = false;
        return result;
    }

    private Document root;

    private String res = "";

    private boolean include = false;
}
