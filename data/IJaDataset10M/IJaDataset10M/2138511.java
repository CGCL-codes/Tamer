package net.sourceforge.mords.docs.client;

import com.tdcs.docs.common.DocServer;
import com.tdcs.docs.common.Document;
import com.tdcs.docs.common.Index;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetEvent;
import java.awt.dnd.DropTargetListener;
import java.io.File;
import java.rmi.RemoteException;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

/**
 *
 * @author  david
 */
public class IndexListPanel extends javax.swing.JPanel implements ServerClient, DropTargetListener {

    private String title = "Indices";

    private Manager mgr;

    private DocServer ds;

    /** Creates new form IndexListPanel */
    public IndexListPanel(Manager mgr, DocServer ds, String title) {
        this.mgr = mgr;
        this.ds = ds;
        if (title != null) {
            this.title = title;
        }
        initComponents();
        list.setDropTarget(new DropTarget(list, this));
        reload();
    }

    public IndexListPanel() {
        initComponents();
        reload();
    }

    public void setTitle(String title) {
        this.title = title;
        setBorder(javax.swing.BorderFactory.createTitledBorder(getTitle()));
    }

    public String getTitle() {
        return title;
    }

    private void initComponents() {
        scrollPane = new javax.swing.JScrollPane();
        list = new javax.swing.JList();
        setLayout(new java.awt.BorderLayout());
        setBorder(javax.swing.BorderFactory.createTitledBorder(getTitle()));
        list.setModel(new javax.swing.AbstractListModel() {

            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };

            public int getSize() {
                return strings.length;
            }

            public Object getElementAt(int i) {
                return strings[i];
            }
        });
        list.setDragEnabled(true);
        scrollPane.setViewportView(list);
        add(scrollPane, java.awt.BorderLayout.CENTER);
    }

    public void setDocServer(DocServer ds) {
        this.ds = ds;
        reload();
    }

    public void setIndex(Index index) {
    }

    public Index getIndex() {
        Index selected = null;
        if (list.getSelectedIndex() != -1) {
            try {
                selected = ds.getIndex((String) list.getSelectedValue());
            } catch (RemoteException re) {
                mgr.showException(re);
                mgr.setMsg("Problem retrieving index.");
            }
        }
        mgr.setMsg(selected.toString() + " selected.");
        return selected;
    }

    public Document getDocument() {
        return null;
    }

    public void setManager(Manager mgr) {
        this.mgr = mgr;
    }

    public void reload() {
        if (ds != null) {
            try {
                Collection<String> c = ds.listIndices();
                list.setListData(new Vector<String>(c));
            } catch (Exception e) {
                mgr.showException(e);
            }
        }
    }

    public void clear() {
    }

    public String getCategory() {
        return null;
    }

    public void addDocument(String id, byte[] data) {
    }

    public void addDocument(Document doc) {
    }

    public void dragEnter(DropTargetDragEvent dtde) {
    }

    public void dragOver(DropTargetDragEvent dtde) {
    }

    public void dropActionChanged(DropTargetDragEvent dtde) {
    }

    public void dragExit(DropTargetEvent dte) {
    }

    public void drop(DropTargetDropEvent dtde) {
        Transferable t = dtde.getTransferable();
        try {
            List fileList = (List) t.getTransferData(DataFlavor.javaFileListFlavor);
            Iterator it = fileList.iterator();
            while (it.hasNext()) {
                Object obj = it.next();
                File f = (File) obj;
            }
        } catch (Exception e) {
        }
    }

    private javax.swing.JList list;

    private javax.swing.JScrollPane scrollPane;
}
