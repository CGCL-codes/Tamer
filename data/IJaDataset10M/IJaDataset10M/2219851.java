package org.jecars.apps;

import java.io.*;
import java.net.URL;
import java.util.Calendar;
import javax.jcr.Node;
import javax.jcr.NodeIterator;
import javax.jcr.Session;
import nl.msd.jdots.JD_Taglist;
import org.jecars.CARS_Factory;
import org.jecars.CARS_Main;
import org.jecars.CARS_Utils;
import org.jecars.support.CARS_Mime;

/**
 * CARS_DirectoryApp
 *
 * @version $Id: CARS_DirectoryApp.java,v 1.7 2009/01/16 13:56:27 weertj Exp $
 */
public class CARS_DirectoryApp extends CARS_DefaultInterface implements CARS_Interface {

    /** Creates a new instance of CARS_DirectoryApp */
    public CARS_DirectoryApp() {
    }

    /** getVersion
   * 
   * @return
   */
    @Override
    public String getVersion() {
        return getClass().getName() + ": JeCARS version=" + CARS_Main.VERSION_ID + " $Id: CARS_DirectoryApp.java,v 1.7 2009/01/16 13:56:27 weertj Exp $";
    }

    private File getStartDirectory(Node pInterfaceNode) throws Exception {
        File f = new File(pInterfaceNode.getProperty("jecars:StorageDirectory").getString(), pInterfaceNode.getName());
        return f;
    }

    public void synchronizeDirectory(CARS_Main pMain, Node pInterfaceNode, Node pParentNode, String pRelative) throws Exception {
        System.out.println("a=-=--= " + pInterfaceNode.getProperty("jecars:StorageDirectory").getString() + " ::: " + pRelative);
        File f = new File(pInterfaceNode.getProperty("jecars:StorageDirectory").getString(), pRelative);
        if (f.exists()) {
            Calendar synchTime = Calendar.getInstance();
            Thread.sleep(10);
            File files[] = f.listFiles();
            if (files != null) {
                for (int i = 0; i < files.length; i++) {
                    if (files[i].isDirectory()) {
                        if (pParentNode.hasNode(files[i].getName()) == false) {
                            Node n = pParentNode.addNode(files[i].getName(), "jecars:datafolder");
                            Calendar mod = Calendar.getInstance();
                            mod.setTimeInMillis(files[i].lastModified());
                            Calendar c = Calendar.getInstance();
                            if (mod.before(c)) c.setTime(mod.getTime());
                            n.setProperty("jecars:Modified", mod);
                            directoryConfigurationEvent(pMain.getLoginUser(), n, "update", "EXTERNAL ADDED DIRECTORY: " + n.getPath());
                        }
                    } else {
                        if (pParentNode.hasNode(files[i].getName()) == false) {
                            Node n = pParentNode.addNode(files[i].getName(), "jecars:datafile");
                            Calendar mod = Calendar.getInstance();
                            mod.setTimeInMillis(files[i].lastModified());
                            Calendar c = Calendar.getInstance();
                            if (mod.before(c)) c.setTime(mod.getTime());
                            n.setProperty("jecars:Modified", mod);
                            n.setProperty("jcr:lastModified", mod);
                            n.setProperty("jcr:data", "");
                            n.setProperty("jcr:mimeType", CARS_Mime.getMIMEType(files[i].getName(), null));
                            n.setProperty("jecars:URL", files[i].toURI().toURL().toExternalForm());
                            directoryConfigurationEvent(pMain.getLoginUser(), n, "update", "EXTERNAL ADDED FILE: " + n.getPath());
                        }
                    }
                    Node n = pParentNode.getNode(files[i].getName());
                    n.setProperty("jecars:LastAccessed", Calendar.getInstance());
                }
                NodeIterator ni = pParentNode.getNodes();
                Node n;
                boolean remove;
                while (ni.hasNext()) {
                    n = ni.nextNode();
                    remove = false;
                    if (n.isNodeType("jecars:dataresource")) {
                        if (n.hasProperty("jecars:LastAccessed")) {
                            if (n.getProperty("jecars:LastAccessed").getDate().before(synchTime)) {
                                remove = true;
                            }
                        } else {
                            remove = true;
                        }
                    }
                    if (remove == true) {
                        if (n.isNodeType("jecars:datafile")) {
                            directoryConfigurationEvent(pMain.getLoginUser(), n, "update", "EXTERNAL REMOVED FILE: " + n.getPath());
                        } else {
                            directoryConfigurationEvent(pMain.getLoginUser(), n, "update", "EXTERNAL REMOVED DIRECTORY: " + n.getPath());
                        }
                        n.remove();
                    }
                }
            }
        } else {
        }
        return;
    }

    /**
   */
    protected void directoryConfigurationEvent(Node pWho, Node pWhat, String pAction, String pMessage) throws Exception {
        if (pAction.equals("browse")) {
            Node event = CARS_Factory.getEventManager().addEvent(pWho, pWhat, "Applications/Directory", "DIR", "READ", "BROWSE: " + pMessage);
        }
        if (pAction.equals("update")) {
            Node event = CARS_Factory.getEventManager().addEvent(pWho, pWhat, "Applications/Directory", "DIR", "UPDATE", "UPDATE: " + pMessage);
        }
        if (pAction.equals("retrieve")) {
            Node event = CARS_Factory.getEventManager().addEvent(pWho, pWhat, "Applications/Directory", "DIR", "READ", "RETRIEVE: " + pMessage);
        }
        if (pAction.equals("create")) {
            Node event = CARS_Factory.getEventManager().addEvent(pWho, pWhat, "Applications/Directory", "DIR", "CREATE", "CREATE: " + pMessage);
        }
        if (pAction.equals("delete")) {
            Node event = CARS_Factory.getEventManager().addEvent(pWho, pWhat, "Applications/Directory", "DIR", "DELETE", "DELETE: " + pMessage);
        }
        return;
    }

    @Override
    public void removeNode(CARS_Main pMain, Node pInterfaceNode, Node pNode, JD_Taglist pParams) throws Exception {
        Node parent = pNode.getParent();
        if (pNode.hasProperty("jecars:URL") == true) {
            String furl = pNode.getProperty("jecars:URL").getString();
            File f = new File(new URL(furl).toURI());
            boolean deleted = f.delete();
            directoryConfigurationEvent(pMain.getLoginUser(), pNode, "delete", " Local URL: " + furl);
            if (deleted == false) {
                CARS_Factory.getEventManager().addEvent(pMain, pMain.getLoginUser(), pNode, "Applications/Directory", "DIR", "WARNING", "Cannot delete " + f.getCanonicalPath());
            }
        }
        directoryConfigurationEvent(pMain.getLoginUser(), pNode, "delete", pNode.getName());
        pNode.remove();
        parent.save();
        return;
    }

    /** Store a binary stream, on default the jecars:datafile node type is supported.
   *  If the pNode is an other type the method will stored the data in a Binary property
   * @param pMain the CARS_Main object
   * @param pInterfaceNode the Node which defines the application source or NULL
   * @param pNode the node in which the data will be stored
   * @param pMimeType the mime type of the data if known, otherwise NULL
   * @return true when a update on the node is performed
   * @throws Exception when an error occurs.
   */
    @Override
    public boolean setBodyStream(CARS_Main pMain, Node pInterfaceNode, Node pNode, InputStream pBody, String pMimeType) throws Exception {
        nodeAdded(pMain, pInterfaceNode, pNode, pBody);
        return true;
    }

    @Override
    public void nodeAdded(CARS_Main pMain, Node pInterfaceNode, Node pNewNode, InputStream pBody) throws Exception {
        if (pNewNode.isNodeType("jecars:datafile")) {
            File f = getStartDirectory(pInterfaceNode);
            String xtra = pNewNode.getPath().substring(pInterfaceNode.getPath().length());
            f = new File(f, xtra);
            if (f.exists()) {
                if (f.delete() == false) {
                    throw new IOException("Cannot delete file: " + f.getCanonicalPath());
                }
            }
            if (f.createNewFile() == false) {
                throw new IOException("Cannot create new file: " + f.getCanonicalPath());
            }
            if (pBody != null) {
                FileOutputStream fos = new FileOutputStream(f);
                try {
                    directoryConfigurationEvent(pMain.getLoginUser(), pNewNode, "create", pNewNode.getPath() + " (Started)");
                    CARS_Utils.sendInputStreamToOutputStream(50000, pBody, fos);
                    pNewNode.setProperty("jecars:URL", f.toURI().toURL().toExternalForm());
                    directoryConfigurationEvent(pMain.getLoginUser(), pNewNode, "create", pNewNode.getPath() + " (Ended)");
                } finally {
                    fos.flush();
                    fos.close();
                }
            }
        }
        super.nodeAdded(pMain, pInterfaceNode, pNewNode, pBody);
        return;
    }

    @Override
    public void getNodes(CARS_Main pMain, Node pInterfaceNode, Node pParentNode, String pLeaf) throws Exception {
        Session appSession = CARS_Factory.getSystemApplicationSession();
        Node sysParentNode = appSession.getRootNode().getNode(pParentNode.getPath().substring(1));
        synchronized (appSession) {
            synchronizeDirectory(pMain, pInterfaceNode, sysParentNode, pLeaf);
            sysParentNode.save();
        }
        return;
    }
}
