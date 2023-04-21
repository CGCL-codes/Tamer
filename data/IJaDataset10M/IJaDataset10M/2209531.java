package view.model;

import java.awt.Cursor;
import java.awt.Toolkit;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import model.Entity;
import model.Field;
import model.Schema;
import operating.FramesConfiguration;
import operating.Manager;
import view.components.JTableModel;

/**
 *
 * @author  William
 */
public class SchemaView extends view.jscroll.widgets.JScrollInternalFrame {

    private static final long serialVersionUID = 1L;

    private Schema schema;

    private boolean isNew;

    String frameTitle = "Propriedades do Módulo";

    JTable jtbEntities;

    JTableModel jtmEntities;

    Toolkit toolkit = Toolkit.getDefaultToolkit();

    /** 
     * 
     * Creates new form SchemaView
     * @param schema
     */
    public SchemaView(Schema schema, boolean isNew) {
        initComponents();
        this.setFrameIcon(new util.Imagem().getFrameIcon());
        this.setTitle(schema.getName() + " [" + schema.getId() + "] - " + this.frameTitle);
        FramesConfiguration frameConfiguration = Manager.getFramesConfiguration(this.frameTitle);
        this.setFramesConfiguration(frameConfiguration);
        this.setId(schema.getId());
        this.isNew = isNew;
        this.schema = schema;
        this.jtfName.setText(schema.getName());
        this.btnAddEntity.setEnabled(!isNew);
        if (this.isNew) {
            this.btnSalvar.setText("Criar");
        }
        this.initTblEntities();
        this.loadTblEntities();
        this.jtfName.requestFocus();
    }

    /**
     * 
     */
    private void initTblEntities() {
        String[] columnNames = { "Nome", "Prefixo", "Referência", "Descrição", "Itervençao", "Gerar" };
        this.jtmEntities = new JTableModel(columnNames);
        this.jtbEntities = new JTable(this.jtmEntities);
        this.jtbEntities.setAutoCreateRowSorter(true);
        this.jtbEntities.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        this.jtbEntities.setName("jtbEntities");
        this.jtbEntities.setGridColor(Manager.getGridColor());
        this.jtbEntities.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        this.jtbEntities.addMouseListener(new java.awt.event.MouseAdapter() {

            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnDownEntity.setEnabled(true);
                btnUpEntity.setEnabled(true);
                btnRemoveEntity.setEnabled(true);
                if (evt.getClickCount() == 2) {
                    openEntity();
                }
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                super.mouseEntered(e);
                jtbEntities.setCursor(new Cursor(Cursor.HAND_CURSOR));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                super.mouseExited(e);
                jtbEntities.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
            }
        });
        this.jtbEntities.getColumnModel().getColumn(0).setPreferredWidth(150);
        this.jtbEntities.getColumnModel().getColumn(1).setPreferredWidth(100);
        this.jtbEntities.getColumnModel().getColumn(2).setPreferredWidth(100);
        this.jtbEntities.getColumnModel().getColumn(3).setPreferredWidth(100);
        this.jpnTblEntities.add(new JScrollPane(this.jtbEntities));
    }

    /**
     * 
     */
    private void loadTblEntities() {
        for (int i = 0; i < this.schema.getEntityList().size(); i++) {
            Entity entity = this.schema.getEntityList().get(i);
            this.addRow(entity);
        }
    }

    /**
     * 
     * @param object
     */
    @Override
    public void addRow(Object object) {
        if (object instanceof Entity) {
            Entity linha = (Entity) object;
            Object[] row = { linha.getName(), linha.getPrefix(), linha.getReference(), linha.getDescription(), new Boolean(linha.isNeverGenerate()), new Boolean(linha.isGenerate()) };
            this.jtmEntities.addRow(row);
            this.jtbEntities.updateUI();
        }
    }

    /**
     * 
     * @param index
     */
    @Override
    public void removeRow(int index, int table) {
        if (table == 0) {
            if (this.jtbEntities.getSelectedRow() != -1) {
                this.jtmEntities.removeRow(index);
                Manager.removeObject(this.schema.getEntityList().get(index));
                this.jtbEntities.clearSelection();
                this.jtbEntities.updateUI();
            } else {
                this.toolkit.beep();
            }
        }
        Manager.reloadProjectTree();
        Manager.setModified(true);
    }

    /**
     * 
     */
    private void openEntity() {
        Manager.openEntity(this.schema.getEntityList().get(this.jtbEntities.getSelectedRow()), false);
    }

    /**
     * 
     * @param entities
     */
    public void updateEntities(ArrayList<Entity> entities) {
        this.schema.setEntityList(entities);
        Manager.reloadProjectTree();
        Manager.setModified(true);
    }

    /**
     * 
     * @return
     */
    @Override
    public boolean salvar() {
        boolean salvar = false;
        if (!this.jtfName.getText().isEmpty()) {
            this.schema.setName(this.jtfName.getText());
            if (this.isNew) {
                this.schema.setId("sch_" + Manager.getNumberSchemas());
                Manager.updateFrame(this.getId(), this.schema.getId());
                this.setId(this.schema.getId());
                salvar = Manager.addSchema(this.schema);
            } else {
                salvar = Manager.updateSchema(this.schema);
            }
            if (salvar) {
                this.close();
            }
        } else {
            JOptionPane.showMessageDialog(null, "Favor informar um nome para o Módulo");
        }
        return salvar;
    }

    /**
     * 
     */
    @Override
    public void updateTable(int table) {
        if (table == 0) {
            for (int i = this.jtmEntities.getRowCount() - 1; i > -1; i--) {
                this.jtmEntities.removeRow(i);
            }
            for (int i = 0; i < this.schema.getEntityList().size(); i++) {
                Entity linha = this.schema.getEntityList().get(i);
                this.addRow(linha);
            }
        }
    }

    /**
     * 
     * @param type
     */
    @Override
    public void novo(String type) {
        Manager.openEntity(new Entity(), true);
    }

    /**
     * 
     * @return 
     */
    @Override
    public boolean close() {
        boolean fechar = false;
        this.getFramesConfiguration().setTitle(this.frameTitle);
        this.getFramesConfiguration().setBounds(getBounds());
        this.getFramesConfiguration().setMaximum(isMaximum());
        Manager.updateFramesConfiguration(this.getFramesConfiguration());
        fechar = Manager.removeFrame(getId());
        if (fechar) {
            this.dispose();
        }
        return fechar;
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    private void initComponents() {
        jLabel1 = new javax.swing.JLabel();
        jtfName = new javax.swing.JTextField();
        jPanel3 = new javax.swing.JPanel();
        btnCancelar = new javax.swing.JButton();
        btnSalvar = new javax.swing.JButton();
        jpnTblEntities = new javax.swing.JPanel();
        jToolBar = new javax.swing.JToolBar();
        btnAddEntity = new javax.swing.JButton();
        btnRemoveEntity = new javax.swing.JButton();
        btnUpEntity = new javax.swing.JButton();
        btnDownEntity = new javax.swing.JButton();
        setClosable(true);
        setIconifiable(true);
        setMaximizable(true);
        setResizable(true);
        setMinimumSize(new java.awt.Dimension(617, 343));
        setName("Form");
        addInternalFrameListener(new javax.swing.event.InternalFrameListener() {

            public void internalFrameActivated(javax.swing.event.InternalFrameEvent evt) {
            }

            public void internalFrameClosed(javax.swing.event.InternalFrameEvent evt) {
            }

            public void internalFrameClosing(javax.swing.event.InternalFrameEvent evt) {
                formInternalFrameClosing(evt);
            }

            public void internalFrameDeactivated(javax.swing.event.InternalFrameEvent evt) {
            }

            public void internalFrameDeiconified(javax.swing.event.InternalFrameEvent evt) {
            }

            public void internalFrameIconified(javax.swing.event.InternalFrameEvent evt) {
            }

            public void internalFrameOpened(javax.swing.event.InternalFrameEvent evt) {
            }
        });
        org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance(operating.ProjectingApp.class).getContext().getResourceMap(SchemaView.class);
        jLabel1.setText(resourceMap.getString("jLabel1.text"));
        jLabel1.setName("jLabel1");
        jtfName.setText(resourceMap.getString("jtfName.text"));
        jtfName.setName("jtfName");
        jPanel3.setName("jPanel3");
        btnCancelar.setText(resourceMap.getString("btnCancelar.text"));
        btnCancelar.setName("btnCancelar");
        btnCancelar.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelarActionPerformed(evt);
            }
        });
        btnSalvar.setText(resourceMap.getString("btnSalvar.text"));
        btnSalvar.setName("btnSalvar");
        btnSalvar.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSalvarActionPerformed(evt);
            }
        });
        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup().addContainerGap(457, Short.MAX_VALUE).addComponent(btnSalvar).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(btnCancelar).addContainerGap()));
        jPanel3Layout.setVerticalGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(jPanel3Layout.createSequentialGroup().addContainerGap().addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(btnCancelar).addComponent(btnSalvar)).addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));
        jpnTblEntities.setName("jpnTblEntities");
        jpnTblEntities.setLayout(new java.awt.BorderLayout());
        jToolBar.setFloatable(false);
        jToolBar.setOrientation(1);
        jToolBar.setRollover(true);
        jToolBar.setName("jToolBar");
        btnAddEntity.setIcon(resourceMap.getIcon("btnAddEntity.icon"));
        btnAddEntity.setEnabled(false);
        btnAddEntity.setFocusable(false);
        btnAddEntity.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnAddEntity.setMinimumSize(new java.awt.Dimension(29, 27));
        btnAddEntity.setName("btnAddEntity");
        btnAddEntity.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnAddEntity.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAddEntityActionPerformed(evt);
            }
        });
        jToolBar.add(btnAddEntity);
        btnRemoveEntity.setIcon(resourceMap.getIcon("btnRemoveEntity.icon"));
        btnRemoveEntity.setBorderPainted(false);
        btnRemoveEntity.setEnabled(false);
        btnRemoveEntity.setFocusable(false);
        btnRemoveEntity.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnRemoveEntity.setMinimumSize(new java.awt.Dimension(29, 27));
        btnRemoveEntity.setName("btnRemoveEntity");
        btnRemoveEntity.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnRemoveEntity.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRemoveEntityActionPerformed(evt);
            }
        });
        jToolBar.add(btnRemoveEntity);
        btnUpEntity.setIcon(resourceMap.getIcon("btnUpEntity.icon"));
        btnUpEntity.setBorderPainted(false);
        btnUpEntity.setEnabled(false);
        btnUpEntity.setFocusable(false);
        btnUpEntity.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnUpEntity.setName("btnUpEntity");
        btnUpEntity.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnUpEntity.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnUpEntityActionPerformed(evt);
            }
        });
        jToolBar.add(btnUpEntity);
        btnDownEntity.setIcon(resourceMap.getIcon("btnDownEntity.icon"));
        btnDownEntity.setBorderPainted(false);
        btnDownEntity.setEnabled(false);
        btnDownEntity.setFocusable(false);
        btnDownEntity.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnDownEntity.setName("btnDownEntity");
        btnDownEntity.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnDownEntity.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDownEntityActionPerformed(evt);
            }
        });
        jToolBar.add(btnDownEntity);
        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addContainerGap().addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addComponent(jLabel1).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(jtfName, javax.swing.GroupLayout.DEFAULT_SIZE, 546, Short.MAX_VALUE)).addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup().addComponent(jpnTblEntities, javax.swing.GroupLayout.DEFAULT_SIZE, 550, Short.MAX_VALUE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(jToolBar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))).addContainerGap()).addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE));
        layout.setVerticalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup().addContainerGap().addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(jLabel1).addComponent(jtfName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(jToolBar, javax.swing.GroupLayout.PREFERRED_SIZE, 221, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(jpnTblEntities, javax.swing.GroupLayout.PREFERRED_SIZE, 221, javax.swing.GroupLayout.PREFERRED_SIZE)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)));
        java.awt.Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
        setBounds((screenSize.width - 617) / 2, (screenSize.height - 343) / 2, 617, 343);
    }

    private void btnCancelarActionPerformed(java.awt.event.ActionEvent evt) {
        this.close();
    }

    private void btnSalvarActionPerformed(java.awt.event.ActionEvent evt) {
        this.salvar();
    }

    private void formInternalFrameClosing(javax.swing.event.InternalFrameEvent evt) {
        this.close();
    }

    private void btnAddEntityActionPerformed(java.awt.event.ActionEvent evt) {
        this.novo("ATRIBUTTE");
    }

    private void btnRemoveEntityActionPerformed(java.awt.event.ActionEvent evt) {
        this.removeRow(this.jtbEntities.getSelectedRow(), 0);
    }

    private void btnUpEntityActionPerformed(java.awt.event.ActionEvent evt) {
        if (this.jtbEntities.getSelectedRow() > 0) {
            int target_position = this.jtbEntities.getSelectedRow() - 1;
            int source_position = this.jtbEntities.getSelectedRow();
            this.jtmEntities.moveRow(source_position, source_position, target_position);
            this.jtbEntities.setRowSelectionInterval(target_position, target_position);
            ArrayList<Entity> entities = (ArrayList<Entity>) this.schema.getEntityList().clone();
            Object source = entities.get(source_position).clone();
            Object target = entities.get(target_position).clone();
            entities.set(source_position, (Entity) target);
            entities.set(target_position, (Entity) source);
            this.updateEntities(entities);
        } else {
            this.toolkit.beep();
        }
    }

    private void btnDownEntityActionPerformed(java.awt.event.ActionEvent evt) {
        if (this.jtbEntities.getSelectedRow() < this.jtbEntities.getRowCount() - 1 && this.jtbEntities.getSelectedRow() != -1) {
            int target_position = this.jtbEntities.getSelectedRow() + 1;
            int source_position = this.jtbEntities.getSelectedRow();
            this.jtmEntities.moveRow(source_position, source_position, target_position);
            this.jtbEntities.setRowSelectionInterval(target_position, target_position);
            ArrayList<Entity> entities = (ArrayList<Entity>) this.schema.getEntityList().clone();
            Object source = entities.get(source_position).clone();
            Object target = entities.get(target_position).clone();
            entities.set(source_position, (Entity) target);
            entities.set(target_position, (Entity) source);
            this.updateEntities(entities);
        } else {
            this.toolkit.beep();
        }
    }

    private javax.swing.JButton btnAddEntity;

    private javax.swing.JButton btnCancelar;

    private javax.swing.JButton btnDownEntity;

    private javax.swing.JButton btnRemoveEntity;

    private javax.swing.JButton btnSalvar;

    private javax.swing.JButton btnUpEntity;

    private javax.swing.JLabel jLabel1;

    private javax.swing.JPanel jPanel3;

    private javax.swing.JToolBar jToolBar;

    private javax.swing.JPanel jpnTblEntities;

    private javax.swing.JTextField jtfName;
}
