package gui.agent;

import gui.GuiUtils;
import gui.document.DocumentPresentation;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.rmi.RemoteException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.LayoutStyle;
import javax.swing.UIManager;
import javax.swing.WindowConstants;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableColumnModel;
import javax.swing.table.TableColumn;
import logic.Document;
import logic.DocumentDescription;
import logic.Template;
import net.rmi.List;
import ui.logger.GlobalLogger;
import ui.logger.LogEvent;
import ui.logger.LogEvent.Types;
import client.Config;
import client.Factory;

public class DocumnetListFrame extends javax.swing.JFrame {

    private static final SimpleDateFormat SIMPLE_DATE_FORMAT = new SimpleDateFormat("dd.MM.yyyy");

    /**
	 *
	 */
    private static final long serialVersionUID = -783059169335896930L;

    private JScrollPane sp;

    private JTable documentTable;

    private JButton bAdd;

    private JButton bEdit;

    private JButton bDelete;

    private final List list;

    private final DocumentPresentation documentPresentation = new DocumentPresentation();

    private java.util.List<DocumentDescription> docDescrList;

    private AbstractTableModel documentTableModel;

    /**
	 * Auto-generated main method to display this JFrame
	 */
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        DocumnetListFrame inst = new DocumnetListFrame();
        inst.setVisible(true);
    }

    public DocumnetListFrame() {
        super();
        list = Factory.getFactory().getList();
        initGUI();
    }

    private void initGUI() {
        try {
            setTitle("������ ����������");
            GroupLayout thisLayout = new GroupLayout(getContentPane());
            docDescrList = new ArrayList<DocumentDescription>();
            getContentPane().setLayout(thisLayout);
            setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
            sp = new JScrollPane();
            documentTableModel = new AbstractTableModel() {

                /**
				 * 
				 */
                private static final long serialVersionUID = 5050211948111165418L;

                public int getColumnCount() {
                    return 3;
                }

                public int getRowCount() {
                    return docDescrList.size();
                }

                public Object getValueAt(int rowIndex, int columnIndex) {
                    DocumentDescription documentDescription = docDescrList.get(rowIndex);
                    if (columnIndex == 0) return "sales".equals(documentDescription.getTemplateCode()) ? "�������" : "������";
                    if (columnIndex == 1) return documentDescription.getName();
                    if (columnIndex == 2) return SIMPLE_DATE_FORMAT.format(documentDescription.getDate());
                    return null;
                }
            };
            documentTable = new JTable();
            sp.setViewportView(documentTable);
            documentTable.setModel(documentTableModel);
            DefaultTableColumnModel columnModel = new DefaultTableColumnModel();
            columnModel.addColumn(new TableColumn(0));
            columnModel.addColumn(new TableColumn(1));
            columnModel.addColumn(new TableColumn(2));
            columnModel.getColumn(0).setHeaderValue("��� ����");
            columnModel.getColumn(1).setHeaderValue("� ���������");
            columnModel.getColumn(2).setHeaderValue("���� ���������");
            documentTable.setColumnModel(columnModel);
            refresh();
            bDelete = new JButton();
            bDelete.setIcon(GuiUtils.DELETE_ICON);
            bDelete.setText("Удалить");
            bDelete.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    int selectedRow = documentTable.getSelectedRow();
                    if (selectedRow == -1) {
                        GlobalLogger.getInstance().show(new LogEvent(Types.INFORMATION, "���������� ������� ������"));
                        return;
                    }
                    DocumentDescription documentDescription = docDescrList.get(selectedRow);
                    if (JOptionPane.showConfirmDialog(DocumnetListFrame.this, "�� ������������� ������ ������� ������?", "�������������", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                        try {
                            Factory.getFactory().getDocumentRetriever().delete(documentDescription.getName(), Config.getUsername());
                            refresh();
                        } catch (RemoteException e1) {
                            GlobalLogger.getInstance().show(new LogEvent(e1));
                            e1.printStackTrace();
                        }
                    }
                }
            });
            bEdit = new JButton();
            bEdit.setIcon(GuiUtils.EDIT_ICON);
            bEdit.setText("Редактировать");
            bEdit.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    int selectedRow = documentTable.getSelectedRow();
                    if (selectedRow == -1) {
                        GlobalLogger.getInstance().show(new LogEvent(Types.INFORMATION, "���������� ������� ������"));
                        return;
                    }
                    DocumentDescription documentDescription = docDescrList.get(selectedRow);
                    try {
                        Document doc = Factory.getFactory().getDocumentRetriever().get(documentDescription.getName(), Config.getUsername());
                        doc.setTemplateCode(documentDescription.getTemplateCode() != null ? documentDescription.getTemplateCode() : "sales");
                        if (showDialog(doc)) {
                            Factory.getFactory().getDocumentRetriever().set(documentDescription.getName(), doc, client.Config.getUsername());
                            refresh();
                        }
                    } catch (RemoteException e1) {
                        GlobalLogger.getInstance().show(new LogEvent(e1));
                        e1.printStackTrace();
                    }
                }
            });
            bAdd = new JButton();
            bAdd.setIcon(GuiUtils.ADD_ICON);
            bAdd.setText("Добавить");
            final JPopupMenu popup = new JPopupMenu();
            popup.add(new JMenuItem("��� �������")).addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    try {
                        Template template = Factory.getFactory().getTemplateRetriever().get("sales", "");
                        template.setTemplateCode("sales");
                        Document doc = new Document(template);
                        if (showDialog(doc)) {
                            Factory.getFactory().getDocumentRetriever().add(doc, client.Config.getUsername());
                            refresh();
                        }
                    } catch (Exception e1) {
                        GlobalLogger.getInstance().show(new LogEvent(e1));
                        e1.printStackTrace();
                    }
                }
            });
            popup.add(new JMenuItem("��� ������")).addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    try {
                        Template template = Factory.getFactory().getTemplateRetriever().get("rent", "");
                        template.setTemplateCode("rent");
                        Document doc = new Document(template);
                        if (showDialog(doc)) {
                            Factory.getFactory().getDocumentRetriever().add(doc, client.Config.getUsername());
                            refresh();
                        }
                    } catch (RemoteException e1) {
                        GlobalLogger.getInstance().show(new LogEvent(e1));
                        e1.printStackTrace();
                    }
                }
            });
            bAdd.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    popup.show(bAdd, 0, 0);
                }
            });
            thisLayout.setVerticalGroup(thisLayout.createSequentialGroup().addGap(6).addComponent(sp, 0, 399, Short.MAX_VALUE).addPreferredGap(LayoutStyle.ComponentPlacement.RELATED).addGroup(thisLayout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(bDelete, GroupLayout.Alignment.BASELINE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE).addComponent(bEdit, GroupLayout.Alignment.BASELINE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE).addComponent(bAdd, GroupLayout.Alignment.BASELINE, GroupLayout.PREFERRED_SIZE, 25, GroupLayout.PREFERRED_SIZE)).addContainerGap(20, 20));
            thisLayout.setHorizontalGroup(thisLayout.createSequentialGroup().addGap(6).addGroup(thisLayout.createParallelGroup().addComponent(sp, GroupLayout.Alignment.LEADING, 0, 504, Short.MAX_VALUE).addGroup(GroupLayout.Alignment.LEADING, thisLayout.createSequentialGroup().addGap(142, 142, Short.MAX_VALUE).addComponent(bAdd, GroupLayout.PREFERRED_SIZE, 115, GroupLayout.PREFERRED_SIZE).addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, 0, GroupLayout.PREFERRED_SIZE).addComponent(bEdit, GroupLayout.PREFERRED_SIZE, 141, GroupLayout.PREFERRED_SIZE).addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED, 1, GroupLayout.PREFERRED_SIZE).addComponent(bDelete, GroupLayout.PREFERRED_SIZE, 110, GroupLayout.PREFERRED_SIZE))).addContainerGap());
            this.setSize(580, 487);
            pack();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void refresh() {
        try {
            docDescrList = list.list("all", Config.getUsername());
            documentTableModel.fireTableDataChanged();
        } catch (RemoteException e) {
            GlobalLogger.getInstance().show(new LogEvent(e));
        } catch (RuntimeException e) {
            GlobalLogger.getInstance().show(new LogEvent(e));
        }
    }

    private boolean showDialog(Document doc) {
        if (doc.getTemplate() == null && doc.getTemplateCode() != null) doc.setTemplate(getTBycode(doc.getTemplateCode()));
        final JDialog dialog = new JDialog();
        dialog.setModal(true);
        dialog.setTitle("�������������� ���������");
        dialog.setLayout(new BorderLayout());
        dialog.add(documentPresentation.vizulize(doc), BorderLayout.NORTH);
        final boolean[] saved = new boolean[1];
        JPanel p = new JPanel();
        ((JButton) p.add(new JButton("������"))).addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                dialog.setVisible(false);
                saved[0] = false;
            }
        });
        ((JButton) p.add(new JButton("���������"))).addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                dialog.setVisible(false);
                saved[0] = true;
            }
        });
        dialog.add(p, BorderLayout.SOUTH);
        dialog.pack();
        dialog.setSize(dialog.getWidth() + 200, dialog.getHeight() + 15);
        dialog.setVisible(true);
        return saved[0];
    }

    private static Template getTBycode(String code) {
        try {
            return Factory.getFactory().getTemplateRetriever().get(code, "");
        } catch (RemoteException e) {
            e.printStackTrace();
        } catch (RuntimeException e) {
            e.printStackTrace();
        }
        return null;
    }
}
