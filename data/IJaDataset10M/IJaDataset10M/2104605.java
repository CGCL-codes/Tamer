package org.meta.workspace.impl;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.util.Date;
import java.util.Enumeration;
import java.util.Vector;
import org.meta.common.Utility;
import org.meta.common.resource.StringResource;
import org.meta.workspace.Workspace;
import org.meta.workspace.WorkspaceIOException;
import org.meta.workspace.WorkspaceItem;
import org.meta.workspace.WorkspaceProperty;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

/**
 * The default implementation of the Workspace interface.
 *
 * @author  V.Ganesh
 * @version 2.0 (Part of MeTA v2.0)
 */
public class WorkspaceImpl extends Workspace {

    /** Creates a new instance of WorkspaceImpl */
    public WorkspaceImpl(String workspaceConfigurationFile) {
        super(workspaceConfigurationFile);
        this.implClass = "org.meta.workspace.impl.WorkspaceImpl";
    }

    /**
     * The methods implements of how a workspace is opened.
     *
     * @throws WorkspaceIOException in case an error occurs.
     */
    @Override
    public void open() throws WorkspaceIOException {
        try {
            Document wcf = Utility.parseXML(workspaceConfigurationFile);
            this.workspaceItems = new Vector<WorkspaceItem>();
            this.workspaceProperties = new Vector<WorkspaceProperty>();
            updateIt(wcf);
            this.dirty = false;
            wcf = null;
        } catch (Exception e) {
            System.err.println("Error reading workspace configuration file: " + e.toString());
            e.printStackTrace();
            throw new WorkspaceIOException("Error reading workspace configuration file: " + e.toString());
        }
    }

    /**
     * Initiates a close operation on the workspace if currently active.
     *      
     * @throws WorkspaceIOException in case an error occurs.
     */
    @Override
    public void close() throws WorkspaceIOException {
    }

    /**
     * Initiates a save operation of the workspace on a non-volatile store..
     * .. possibly on a disk, which may in-turn invoke other save methods.
     *      
     * @throws WorkspaceIOException in case an error occurs.
     */
    @Override
    public void save() throws WorkspaceIOException {
        Enumeration items = workspaceItems.elements();
        WorkspaceItem item;
        while (items.hasMoreElements()) {
            item = (WorkspaceItem) items.nextElement();
            item.save();
        }
        try {
            FileOutputStream fis = new FileOutputStream(workspaceConfigurationFile);
            fis.write(StringResource.getInstance().getXmlHeader().getBytes());
            fis.write("\n".getBytes());
            if (!(new File(workspaceDirectory)).exists()) {
                workspaceDirectory = (new File(workspaceConfigurationFile)).getPath();
            }
            fis.write(("<workspace version=\"" + version + "\" " + "ID=\"" + ID + "\" " + "creationDate=\"" + creationDate.getTime() + "\" " + "lastModifiedDate=\"" + lastModifiedDate.getTime() + "\" " + "author=\"" + author + "\" " + "description=\"" + description + "\" " + "internalName=\"" + internalName + "\" " + "implClass=\"" + implClass + "\" " + "workspaceDirectory=\"" + workspaceDirectory + "\" " + "> \n").getBytes());
            items = workspaceItems.elements();
            while (items.hasMoreElements()) {
                item = (WorkspaceItem) items.nextElement();
                item.setBaseDirectory(workspaceDirectory);
                fis.write(("\t<workspaceItem name=\"" + item.getName() + "\" " + "ID=\"" + item.getID() + "\" " + "file=\"" + item.getWorkspaceItemFile() + "\" " + "description=\"" + item.getDescription() + "\" " + "implClass=\"" + item.getImplClass() + "\" " + "type=\"" + item.getType() + "\" " + " > \n").getBytes());
                fis.write(("\t</workspaceItem>\n").getBytes());
            }
            fis.write(("</workspace> \n").getBytes());
            fis.close();
            this.dirty = false;
        } catch (IOException ioe) {
            throw new WorkspaceIOException("I/O Exception while saving " + "workspace : " + ioe.toString());
        }
    }

    /**
     * method to update this object based on the XML configuration data.
     * Warning: this is a recursive procedure
     */
    private void updateIt(Node n) throws WorkspaceIOException {
        int type = n.getNodeType();
        switch(type) {
            case Node.ATTRIBUTE_NODE:
                String nodeName = n.getNodeName();
                if (nodeName.equals("version")) {
                    version = n.getNodeValue();
                } else if (nodeName.equals("ID")) {
                    ID = n.getNodeValue();
                } else if (nodeName.equals("creationDate")) {
                    try {
                        creationDate = new Date(Long.parseLong(n.getNodeValue()));
                    } catch (Exception e) {
                        creationDate = new Date();
                    }
                } else if (nodeName.equals("lastModifiedDate")) {
                    try {
                        lastModifiedDate = new Date(Long.parseLong(n.getNodeValue()));
                    } catch (Exception e) {
                        lastModifiedDate = new Date();
                    }
                } else if (nodeName.equals("author")) {
                    author = n.getNodeValue();
                } else if (nodeName.equals("description")) {
                    description = n.getNodeValue();
                } else if (nodeName.equals("internalName")) {
                    internalName = n.getNodeValue();
                } else if (nodeName.equals("implClass")) {
                    if (!implClass.equals(n.getNodeValue())) {
                        throw new WorkspaceIOException("Implementation class " + "different!! can't read - confusion!");
                    }
                } else if (nodeName.equals("workspaceDirectory")) {
                    workspaceDirectory = n.getNodeValue();
                    if (!(new File(workspaceDirectory)).exists()) {
                        workspaceDirectory = (new File(workspaceConfigurationFile)).getPath();
                    }
                }
                break;
            case Node.ELEMENT_NODE:
                String element = n.getNodeName();
                NamedNodeMap atts = n.getAttributes();
                if (element.equals("workspaceItem")) {
                    try {
                        String impClz = atts.getNamedItem("implClass").getNodeValue();
                        String wif = atts.getNamedItem("file").getNodeValue();
                        Class wic = Class.forName(impClz);
                        Constructor wicConstructor = wic.getDeclaredConstructor(new Class[] { String.class });
                        WorkspaceItem wi = (WorkspaceItem) wicConstructor.newInstance(wif);
                        wi.setBaseDirectory(workspaceDirectory);
                        wi.open();
                        wi.setName(atts.getNamedItem("name").getNodeValue());
                        wi.setID(atts.getNamedItem("ID").getNodeValue());
                        wi.setDescription(atts.getNamedItem("description").getNodeValue());
                        wi.setType(atts.getNamedItem("type").getNodeValue());
                        addWorkspaceItem(wi);
                    } catch (Exception e) {
                        System.out.println("Unexpected error : " + e + " can't read - confusion!");
                        e.printStackTrace();
                        throw new WorkspaceIOException("Unexpected error : " + e + " can't read - confusion!");
                    }
                } else {
                    if (atts == null) return;
                    for (int i = 0; i < atts.getLength(); i++) {
                        Node att = atts.item(i);
                        updateIt(att);
                    }
                }
                break;
            default:
                break;
        }
        for (Node child = n.getFirstChild(); child != null; child = child.getNextSibling()) {
            updateIt(child);
        }
    }
}
