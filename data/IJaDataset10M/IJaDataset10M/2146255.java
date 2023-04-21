package ru.spb.leti.g6351.kinpo.adressbook.main.forms;

import ru.spb.leti.g6351.kinpo.adressbook.handler.ButtonListener;
import ru.spb.leti.g6351.kinpo.adressbook.handler.CloseTableHandler;
import ru.spb.leti.g6351.kinpo.adressbook.handler.DeleteHandler;
import ru.spb.leti.g6351.kinpo.adressbook.handler.EditSelectedHandler;
import ru.spb.leti.g6351.kinpo.adressbook.handler.SaveHandler;
import ru.spb.leti.g6351.kinpo.adressbook.handler.TableSelectionListener;

/**
 *
 * @author nikita
 */
@SuppressWarnings("serial")
public class TableFrame extends javax.swing.JFrame {

    /** Creates new form TableFrame */
    public TableFrame() {
        this.setTitle("Записи");
        initComponents();
    }

    private void initComponents() {
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jEditButton = new javax.swing.JButton();
        jCloseButton = new javax.swing.JButton();
        jSaveButton = new javax.swing.JButton();
        jDeleteButton = new javax.swing.JButton();
        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        jTable1.setModel(new javax.swing.table.DefaultTableModel(new Object[][] {}, new String[] {}));
        jTable1.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        jScrollPane1.setViewportView(jTable1);
        jEditButton.setText("Редактировать");
        jCloseButton.setText("Закрыть");
        jSaveButton.setText("Экспорт");
        jDeleteButton.setText("Удалить");
        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addContainerGap().addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 748, Short.MAX_VALUE).addGroup(layout.createSequentialGroup().addComponent(jEditButton).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(jDeleteButton, javax.swing.GroupLayout.PREFERRED_SIZE, 115, javax.swing.GroupLayout.PREFERRED_SIZE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 271, Short.MAX_VALUE).addComponent(jSaveButton, javax.swing.GroupLayout.PREFERRED_SIZE, 108, javax.swing.GroupLayout.PREFERRED_SIZE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(jCloseButton, javax.swing.GroupLayout.PREFERRED_SIZE, 122, javax.swing.GroupLayout.PREFERRED_SIZE))).addContainerGap()));
        layout.setVerticalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addContainerGap().addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 460, javax.swing.GroupLayout.PREFERRED_SIZE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(jEditButton, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(jCloseButton, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(jSaveButton, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(jDeleteButton, javax.swing.GroupLayout.DEFAULT_SIZE, 30, Short.MAX_VALUE)).addContainerGap()));
        pack();
    }

    private javax.swing.JButton jCloseButton;

    private javax.swing.JButton jDeleteButton;

    private javax.swing.JButton jEditButton;

    private javax.swing.JButton jSaveButton;

    private javax.swing.JScrollPane jScrollPane1;

    private javax.swing.JTable jTable1;

    public javax.swing.JTable getTable() {
        return jTable1;
    }

    public final void initHandlers() {
        jCloseButton.addActionListener(new ButtonListener(new CloseTableHandler()));
        jSaveButton.addActionListener(new ButtonListener(new SaveHandler()));
        jEditButton.addActionListener(new ButtonListener(new EditSelectedHandler()));
        jDeleteButton.addActionListener(new ButtonListener(new DeleteHandler()));
        jTable1.getSelectionModel().addListSelectionListener(new TableSelectionListener());
    }
}
