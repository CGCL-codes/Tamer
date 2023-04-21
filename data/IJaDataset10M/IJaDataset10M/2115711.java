package doanlythuyet_javaoutlook.MyUserControl;

import doanlythuyet_javaoutlook.DoAnLyThuyet_JavaOutLookApp;
import doanlythuyet_javaoutlook.EventListener_ClickChuotVaoCayDuyetFile;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.*;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import org.w3c.dom.*;
import java.io.IOException;
import java.text.AttributedCharacterIterator.Attribute;
import java.util.Enumeration;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.JTree;
import javax.swing.UIManager;
import javax.swing.event.MouseInputListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.MutableTreeNode;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.xml.sax.SAXException;

/**
 *
 * @author ShineShiao
 */
public class JTreeFromXMLFile extends javax.swing.JPanel {

    private boolean m_chiDoc;

    private final JPopupMenu m_popupMenu;

    private String m_fileName;

    /** Creates new form JTreeFromXMLFile */
    public void expandTree(TreePath path) {
        jTreeThuMucNguoiDung.expandPath(path);
    }

    public static void expandAll(JTree tree, boolean expand) {
        TreeNode root = (TreeNode) tree.getModel().getRoot();
        expandAll(tree, new TreePath(root), expand);
    }

    private static void expandAll(JTree tree, TreePath parent, boolean expand) {
        TreeNode node = (TreeNode) parent.getLastPathComponent();
        if (node.getChildCount() >= 0) {
            for (Enumeration e = node.children(); e.hasMoreElements(); ) {
                TreeNode n = (TreeNode) e.nextElement();
                TreePath path = parent.pathByAddingChild(n);
                expandAll(tree, path, expand);
            }
        }
        if (expand) {
            tree.expandPath(parent);
        } else {
            tree.collapsePath(parent);
        }
    }

    public JTreeFromXMLFile(String xmlFilePath, Boolean chiDoc) {
        m_chiDoc = chiDoc;
        m_fileName = xmlFilePath;
        m_popupMenu = new JPopupMenu();
        if (!chiDoc) {
            String cacHanhViThayDoiCauTruc[] = { "Thêm mới", "Xóa", "Đổi Tên" };
            for (String HanhVi : cacHanhViThayDoiCauTruc) {
                JMenuItem menuItem = new JMenuItem(HanhVi);
                menuItem.addMouseListener(new PopupMenu_MouseListerner_MyCayHienThiThuMuc(menuItem.getText()));
                m_popupMenu.add(menuItem);
            }
            m_popupMenu.addSeparator();
        }
        String cacHanhViKhongThayDoiCauTruc[] = { "Đánh dấu đã đọc", "Đánh dấu chưa đọc", "Gỡ dấu" };
        for (String HanhVi : cacHanhViKhongThayDoiCauTruc) {
            JMenuItem menuItem = new JMenuItem(HanhVi);
            menuItem.addMouseListener(new PopupMenu_MouseListerner_MyCayHienThiThuMuc(menuItem.getText()));
            m_popupMenu.add(menuItem);
        }
        initComponents();
        try {
            thayDoiIconCuaTree();
            TaoCay();
            expandAll(jTreeThuMucNguoiDung, true);
        } catch (SAXException ex) {
            Logger.getLogger(JTreeFromXMLFile.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(null, "Tao cay thu muc tu file XML loi: " + ex.getMessage());
        } catch (IOException ex) {
            Logger.getLogger(JTreeFromXMLFile.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(null, "Tao cay thu muc tu file XML loi: " + ex.getMessage());
        } catch (ParserConfigurationException ex) {
            Logger.getLogger(JTreeFromXMLFile.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(null, "Tao cay thu muc tu file XML loi: " + ex.getMessage());
        }
        jTreeThuMucNguoiDung.addMouseListener(new MouseListener() {

            public void mouseClicked(MouseEvent e) {
                if (jTreeThuMucNguoiDung.getSelectionPath() == null) return;
                String strPath = StringPathFromTreePath(jTreeThuMucNguoiDung.getSelectionPath());
                getM_popupMenu().setToolTipText(strPath);
                if (e.getButton() == MouseEvent.BUTTON3) {
                    getM_popupMenu().show(e.getComponent(), e.getX(), e.getY());
                }
            }

            public void mousePressed(MouseEvent e) {
            }

            public void mouseReleased(MouseEvent e) {
            }

            public void mouseEntered(MouseEvent e) {
            }

            public void mouseExited(MouseEvent e) {
            }
        });
    }

    public static TreePath TreePathFromStringPath(String stringPath) {
        String cacPath[] = stringPath.split("\\\\");
        MutableTreeNode cacNode[] = new MutableTreeNode[cacPath.length];
        for (int i = 0; i < cacPath.length; i++) cacNode[i] = new DefaultMutableTreeNode(cacPath[i]);
        return new TreePath(cacNode);
    }

    public static String StringPathFromTreePath(TreePath treePath) {
        String Kq = "";
        if (treePath == null) return Kq;
        Object cacNode[] = treePath.getPath();
        for (int i = 0; i < cacNode.length; i++) Kq += ((DefaultMutableTreeNode) cacNode[i]).getUserObject().toString() + "\\";
        return Kq;
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    private void initComponents() {
        jScrollPane1 = new javax.swing.JScrollPane();
        jTreeThuMucNguoiDung = new javax.swing.JTree();
        setName("Form");
        jScrollPane1.setName("jScrollPane1");
        jTreeThuMucNguoiDung.setName("jTreeThuMucNguoiDung");
        jTreeThuMucNguoiDung.addTreeSelectionListener(new javax.swing.event.TreeSelectionListener() {

            public void valueChanged(javax.swing.event.TreeSelectionEvent evt) {
                jTreeThuMucNguoiDungValueChanged(evt);
            }
        });
        jScrollPane1.setViewportView(jTreeThuMucNguoiDung);
        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 336, Short.MAX_VALUE));
        layout.setVerticalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 265, Short.MAX_VALUE));
    }

    private void jTreeThuMucNguoiDungValueChanged(javax.swing.event.TreeSelectionEvent evt) {
        String strPath = StringPathFromTreePath(jTreeThuMucNguoiDung.getSelectionPath());
        initEvent_ClickChuotVaoCayDuyetFile(strPath);
    }

    public void TaoCay() throws SAXException, IOException, ParserConfigurationException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document doc = builder.parse(new File(m_fileName));
        Element root = (Element) doc.getDocumentElement();
        DefaultTreeModel model = (DefaultTreeModel) getJTreeThuMucNguoiDung().getModel();
        MutableTreeNode newNode = new DefaultMutableTreeNode(root.getAttribute("name"));
        model.setRoot(newNode);
        NodeList list = root.getChildNodes();
        for (int i = 0; i < list.getLength(); ++i) {
            if (list.item(i) instanceof Element) {
                Element ele = (Element) list.item(i);
                ThemXMLElementVaoTree(model, ele, newNode);
            }
        }
    }

    public void ThemXMLElementVaoTree(DefaultTreeModel model, Element ele, MutableTreeNode nodeCha) {
        MutableTreeNode newNode = new DefaultMutableTreeNode(ele.getAttribute("name"));
        model.insertNodeInto(newNode, nodeCha, nodeCha.getChildCount());
        for (int i = 0; i < ele.getChildNodes().getLength(); i++) {
            ThemXMLElementVaoTree(model, (Element) ele.getChildNodes().item(i), newNode);
        }
    }

    /** Thêm 1 thư mục vào vây
     *  strPath:đường dẫn thư mục cha
     *  strName:Tên thư mục cần thêm
     *  strFileName:Tên file xml
     */
    public void ThemThuMucMoiVaoXML(String strPath, String strName, String strFileName) throws ParserConfigurationException, IOException, SAXException, TransformerConfigurationException, TransformerException {
        String[] strCacChuoi = null;
        strCacChuoi = strPath.split("\\\\");
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document doc = builder.parse(new File(m_fileName));
        Element root = (Element) doc.getDocumentElement();
        Element ElementCha = root;
        for (int i = 1; i < strCacChuoi.length; i++) {
            ElementCha = LayNodeTuXML(ElementCha, strCacChuoi[i]);
        }
        Element ThuMuc = doc.createElement("ThuMuc");
        ThuMuc.setAttribute("name", strName);
        ElementCha.appendChild(ThuMuc);
        Transformer xformer = TransformerFactory.newInstance().newTransformer();
        xformer.transform(new DOMSource(doc), new StreamResult(new File(strFileName)));
        TaoCay();
    }

    /** xóa 1 thư mục khỏi cây
     *  strPath:đường dẫn thư mục cần xóa

     *  strFileName:Tên file xml
     */
    public void XoaThuMucTrongXML(String strPath, String strFileName) throws ParserConfigurationException, IOException, SAXException, TransformerConfigurationException, TransformerException {
        String[] strCacChuoi = null;
        strCacChuoi = strPath.split("\\\\");
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document doc = builder.parse(new File(m_fileName));
        Element root = (Element) doc.getDocumentElement();
        Element ElementCha = root;
        if (strCacChuoi.length == 1) {
            JOptionPane.showMessageDialog(null, "Không Thể Xóa Thư Mục Này ");
            return;
        }
        for (int i = 1; i < strCacChuoi.length; i++) {
            ElementCha = LayNodeTuXML(ElementCha, strCacChuoi[i]);
        }
        ElementCha.getParentNode().removeChild(ElementCha);
        Transformer xformer = TransformerFactory.newInstance().newTransformer();
        xformer.transform(new DOMSource(doc), new StreamResult(new File(strFileName)));
        TaoCay();
    }

    /** Đổi Tên 1 thư mục vào vây
     *  strPath:đường dẫn thư mục cần đổi
     *  strName:Tên thư mục cần đổi
     *  strFileName:Tên file xml
     */
    public void DoiTenThuMucTrongXML(String strPath, String strName, String strFileName) throws ParserConfigurationException, IOException, SAXException, TransformerConfigurationException, TransformerException {
        String[] strCacChuoi = null;
        strCacChuoi = strPath.split("\\\\");
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document doc = builder.parse(new File(m_fileName));
        Element root = (Element) doc.getDocumentElement();
        Element ElementCha = root;
        for (int i = 1; i < strCacChuoi.length; i++) {
            ElementCha = LayNodeTuXML(ElementCha, strCacChuoi[i]);
        }
        ElementCha.setAttribute("name", strName);
        Transformer xformer = TransformerFactory.newInstance().newTransformer();
        xformer.transform(new DOMSource(doc), new StreamResult(new File(strFileName)));
        TaoCay();
    }

    /** tìm Element con có tên tương ứng
     *  Element:node cha
     *  strName:Tên thư mục cần thêm
     *  return Element tương ứng
     */
    public Element LayNodeTuXML(Element NodeCha, String strName) {
        NodeList List = NodeCha.getChildNodes();
        for (int i = 0; i < List.getLength(); i++) {
            Element ele = (Element) List.item(i);
            String tennode = ele.getAttribute("name");
            if (tennode.equals(strName)) return ele;
        }
        return null;
    }

    /** Thay đổi icon của cây
     *
     *
     */
    public void thayDoiIconCuaTree() {
        Icon leafIcon = new ImageIcon("leaf.gif");
        Icon openIcon = new ImageIcon("open.gif");
        Icon closedIcon = new ImageIcon("closed.gif");
        DefaultTreeCellRenderer renderer = (DefaultTreeCellRenderer) jTreeThuMucNguoiDung.getCellRenderer();
        renderer.setLeafIcon(leafIcon);
        renderer.setClosedIcon(closedIcon);
        renderer.setOpenIcon(openIcon);
        renderer.setLeafIcon(null);
        renderer.setClosedIcon(null);
        renderer.setOpenIcon(null);
        UIManager.put("Tree.leafIcon", leafIcon);
        UIManager.put("Tree.openIcon", openIcon);
        UIManager.put("Tree.closedIcon", closedIcon);
    }

    private javax.swing.JScrollPane jScrollPane1;

    private javax.swing.JTree jTreeThuMucNguoiDung;

    /**
     * @return the jTreeThuMucNguoiDung
     */
    public javax.swing.JTree getJTreeThuMucNguoiDung() {
        return jTreeThuMucNguoiDung;
    }

    /**
     * @param jTreeThuMucNguoiDung the jTreeThuMucNguoiDung to set
     */
    public void setJTreeThuMucNguoiDung(javax.swing.JTree jTreeThuMucNguoiDung) {
        this.jTreeThuMucNguoiDung = jTreeThuMucNguoiDung;
    }

    protected javax.swing.event.EventListenerList listenerList = new javax.swing.event.EventListenerList();

    void initEvent_ClickChuotVaoCayDuyetFile(String evt) {
        Object[] listeners = listenerList.getListenerList();
        for (int i = 0; i < listeners.length; i += 2) {
            if (listeners[i] == EventListener_ClickChuotVaoCayDuyetFile.class) {
                ((EventListener_ClickChuotVaoCayDuyetFile) listeners[i + 1]).Event_ClickChuotVaoCayDuyetFile_Occurred(evt);
            }
        }
    }

    /**
     * Đăng ký sự kiện cho classes
     * @param listener  Sự kiện cần đăng ký
     */
    public void addEventListener_ClickChuotVaoCayDuyetFile(EventListener_ClickChuotVaoCayDuyetFile listener) {
        listenerList.add(EventListener_ClickChuotVaoCayDuyetFile.class, listener);
    }

    /**
     * Gỡ bỏ sự kiện khỏi classes
     * @param listener  Sự kiện cần gỡ bỏ
     */
    public void delEventListener_ClickChuotVaoCayDuyetFile(EventListener_ClickChuotVaoCayDuyetFile listener) {
        listenerList.remove(EventListener_ClickChuotVaoCayDuyetFile.class, listener);
    }

    /**
     * @return the m_popupMenu
     */
    public JPopupMenu getM_popupMenu() {
        return m_popupMenu;
    }
}
