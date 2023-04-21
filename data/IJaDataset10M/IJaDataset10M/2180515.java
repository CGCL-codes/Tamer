package com.onetwork.core.ui.components;

import com.onetwork.core.ui.components.actions.CancelarAction;
import com.onetwork.core.ui.components.actions.ExcluirAction;
import com.onetwork.core.ui.components.actions.ImprimirAction;
import com.onetwork.core.ui.components.actions.LimparAction;
import com.onetwork.core.ui.components.actions.NovoAction;
import com.onetwork.core.ui.components.actions.PesquisarAction;
import com.onetwork.core.ui.components.actions.SalvarAction;
import com.onetwork.core.ui.components.dialog.ListagemDialog;
import com.sun.scenario.scenegraph.JSGPanel;
import java.awt.CardLayout;
import java.awt.Font;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeCellRenderer;
import javax.swing.tree.TreePath;

public abstract class BasicWindow extends JPanel {

    protected DefaultMutableTreeNode root;

    private JLabel mainLabel;

    private JLabel indexLabel;

    private JLabel totalLabel;

    private JLabel deLabel;

    private ListagemDialog listagemDialog;

    protected ImageIcon icon;

    public BasicWindow() {
        this.listagemDialog = new ListagemDialog();
        this.mainLabel = new JLabel();
        this.indexLabel = new JLabel("0");
        this.totalLabel = new JLabel("0");
        this.deLabel = new JLabel(" de ");
        Font font = new Font("Tahoma", 1, 11);
        this.mainLabel.setFont(font);
        this.indexLabel.setFont(font);
        this.totalLabel.setFont(font);
        this.deLabel.setFont(font);
        this.initialize();
    }

    protected void initialize() {
        initComponents();
        this.root = new DefaultMutableTreeNode("root");
        this.configureToolBar();
        this.configurePanels();
        this.configureNavigationTree();
    }

    protected void setTextForMainLabel(String string) {
        this.mainLabel.setText(string);
    }

    public abstract URL createImage();

    public ImageIcon icon() {
        if (this.icon == null) {
            Image newImage24X24 = null;
            try {
                BufferedImage bufferedImage = ImageIO.read(createImage());
                if (bufferedImage.getWidth() > 24 || bufferedImage.getHeight() > 24) newImage24X24 = bufferedImage.getScaledInstance(24, 24, Image.SCALE_SMOOTH); else newImage24X24 = bufferedImage;
                icon = new ImageIcon(newImage24X24);
            } catch (IOException ex) {
                Logger.getLogger(BasicWindow.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return this.icon;
    }

    protected abstract void configurePanels();

    public abstract void imprimir();

    public abstract void salvar();

    public abstract void cancelar();

    public abstract void excluir();

    public abstract void novo();

    public abstract void pesquisar();

    public abstract void limpar();

    protected void configureNavigationTree() {
        ((ControlPanelTreeModel) this.navegacaoTree.getModel()).setRoot(this.root);
        this.configureControlPanelTree();
        this.setNavigationTreeRenderer(new ONetworkTreeCellRenderer());
    }

    protected void setNavigationTreeRenderer(TreeCellRenderer treeCellRenderer) {
        this.navegacaoTree.setCellRenderer(treeCellRenderer);
    }

    private void configureToolBar() {
        this.utilidadesToolBar.add(new NovoAction(this));
        this.utilidadesToolBar.add(new CancelarAction(this));
        this.utilidadesToolBar.add(new SalvarAction(this));
        this.utilidadesToolBar.add(new ExcluirAction(this));
        this.utilidadesToolBar.add(new PesquisarAction(this));
        this.utilidadesToolBar.add(new LimparAction(this));
        this.utilidadesToolBar.add(new ImprimirAction(this));
        this.utilidadesToolBar.addSeparator();
        this.utilidadesToolBar.add(this.mainLabel);
    }

    protected void atualizaVizualizacaoDaToolbar(String index, String total) {
        this.indexLabel.setText(index);
        this.totalLabel.setText(total);
    }

    protected void configureControlPanelTree() {
        this.navegacaoTree.setRootVisible(false);
        this.navegacaoTree.addTreeSelectionListener(new TreeSelectionListener() {

            public void valueChanged(TreeSelectionEvent event) {
                TreePath path = event.getNewLeadSelectionPath();
                DefaultMutableTreeNode selected = (DefaultMutableTreeNode) path.getLastPathComponent();
                CardLayout layout = (CardLayout) mainPanel.getLayout();
                String name = "";
                Object object = selected.getUserObject();
                if (object instanceof JPanel) name = ((JPanel) object).getName(); else if (object instanceof JSGPanel) name = ((JSGPanel) object).getName();
                layout.show(mainPanel, name);
            }
        });
    }

    protected class ControlPanelTreeModel extends DefaultTreeModel {

        private static final long serialVersionUID = 1L;

        ControlPanelTreeModel() {
            super(new DefaultMutableTreeNode("root"));
        }

        public void setRoot(DefaultMutableTreeNode root) {
            super.setRoot(root);
        }
    }

    protected void addNavegationElement(JPanel panel, boolean isMain) {
        NavigatorMutableTreeNode geral = new NavigatorMutableTreeNode(panel);
        this.root.add(geral);
        JScrollPane scrollPane = new JScrollPane(panel);
        if (isMain) this.mainPanel.add(scrollPane, panel.getName()); else this.mainPanel.add(panel, panel.getName());
    }

    protected void addNavegationElement(JSGPanel panel, boolean isMain) {
        NavigatorMutableTreeNode geral = new NavigatorMutableTreeNode(panel);
        this.root.add(geral);
        JScrollPane scrollPane = new JScrollPane(panel);
        if (isMain) this.mainPanel.add(scrollPane, panel.getName()); else this.mainPanel.add(panel, panel.getName());
    }

    protected class NavigatorMutableTreeNode extends DefaultMutableTreeNode {

        private static final long serialVersionUID = 1L;

        public NavigatorMutableTreeNode(JPanel panel) {
            super(panel);
        }

        public NavigatorMutableTreeNode(JSGPanel panel) {
            super(panel);
        }

        @Override
        public String toString() {
            String name = "";
            Object object = this.getUserObject();
            if (object instanceof JPanel) name = ((JPanel) object).getName(); else if (object instanceof JSGPanel) name = ((JSGPanel) object).getName();
            return name;
        }
    }

    private void initComponents() {
        utilidadesToolBar = new javax.swing.JToolBar();
        mainSplitPane = new javax.swing.JSplitPane();
        jScrollPane1 = new javax.swing.JScrollPane();
        navegacaoTree = new javax.swing.JTree();
        mainPanel = new javax.swing.JPanel();
        utilidadesToolBar.setFloatable(false);
        utilidadesToolBar.setRollover(true);
        mainSplitPane.setDividerLocation(150);
        mainSplitPane.setDividerSize(10);
        mainSplitPane.setOneTouchExpandable(true);
        navegacaoTree.setModel(new ControlPanelTreeModel());
        jScrollPane1.setViewportView(navegacaoTree);
        mainSplitPane.setLeftComponent(jScrollPane1);
        mainPanel.setLayout(new java.awt.CardLayout());
        mainSplitPane.setRightComponent(mainPanel);
        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(utilidadesToolBar, javax.swing.GroupLayout.DEFAULT_SIZE, 463, Short.MAX_VALUE).addComponent(mainSplitPane, javax.swing.GroupLayout.DEFAULT_SIZE, 463, Short.MAX_VALUE));
        layout.setVerticalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addComponent(utilidadesToolBar, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE).addGap(0, 0, 0).addComponent(mainSplitPane, javax.swing.GroupLayout.DEFAULT_SIZE, 341, Short.MAX_VALUE)));
    }

    private javax.swing.JScrollPane jScrollPane1;

    protected javax.swing.JPanel mainPanel;

    private javax.swing.JSplitPane mainSplitPane;

    protected javax.swing.JTree navegacaoTree;

    private javax.swing.JToolBar utilidadesToolBar;
}
