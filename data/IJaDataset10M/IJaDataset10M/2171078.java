package net.sourceforge.mords.docs.client;

import com.tdcs.docs.common.DocServer;
import com.tdcs.docs.common.Document;
import com.tdcs.docs.common.Index;
import java.rmi.RemoteException;
import java.util.Collection;
import java.util.Vector;
import javax.swing.ListSelectionModel;

/**
 *
 * @author  david
 */
public class DocumentListPanel extends javax.swing.JPanel implements ServerClient {

    private Index index;

    private String category;

    private Manager mgr;

    private DocServer ds;

    private boolean selectOnClick = false;

    /** Creates new form DocumentListPanel */
    public DocumentListPanel() {
        initComponents();
        list.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    }

    private void initComponents() {
        scrollPane = new javax.swing.JScrollPane();
        list = new javax.swing.JList();
        setLayout(new java.awt.BorderLayout());
        setBorder(javax.swing.BorderFactory.createTitledBorder(""));
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
        list.addMouseListener(new java.awt.event.MouseAdapter() {

            public void mouseClicked(java.awt.event.MouseEvent evt) {
                listMouseClicked(evt);
            }
        });
        scrollPane.setViewportView(list);
        add(scrollPane, java.awt.BorderLayout.CENTER);
    }

    private void listMouseClicked(java.awt.event.MouseEvent evt) {
        if (selectOnClick) {
            mgr.documentSelected(getDocument());
        }
    }

    public void setDocServer(DocServer ds) {
        this.ds = ds;
    }

    public void setIndex(Index index) {
        this.index = index;
    }

    public Index getIndex() {
        return index;
    }

    public Document getDocument() {
        Document doc = null;
        try {
            doc = ds.getDocument(index, category, getSelected());
        } catch (RemoteException re) {
            mgr.showException(re);
            mgr.setMsg("Problem loading document.");
        }
        return doc;
    }

    public void setManager(Manager mgr) {
        this.mgr = mgr;
    }

    public void reload() {
        try {
            if (ds != null) {
                Collection<String> c = ds.listDocs(index, category);
                list.setListData(new Vector<String>(c));
            }
        } catch (RemoteException re) {
            mgr.showException(re);
            mgr.setMsg("Problem loading documents for " + category);
        }
    }

    public void clear() {
        reload();
    }

    public String getSelected() {
        String s = null;
        if (list.getSelectedIndex() != -1) {
            s = (String) list.getSelectedValue();
        }
        return s;
    }

    public void setCategory(String category) {
        this.category = category;
        setBorder(javax.swing.BorderFactory.createTitledBorder(category));
    }

    public String getCategory() {
        return null;
    }

    public void addDocument(String id, byte[] data) {
    }

    public void addDocument(Document doc) {
    }

    private javax.swing.JList list;

    private javax.swing.JScrollPane scrollPane;
}
