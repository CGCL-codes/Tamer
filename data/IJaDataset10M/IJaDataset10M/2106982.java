package sightmusic.home;

import java.awt.BorderLayout;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;
import model.user.User;

/**
 *
 * @author  sandie
 */
public class HomestatPanelMulti extends javax.swing.JPanel implements HomeStatInterface {

    private static final long serialVersionUID = 1L;

    User user1;

    User user2;

    /** Creates new form HomestatPanel */
    public HomestatPanelMulti() {
        initComponents();
    }

    private void initComponents() {
        VuPanel = new javax.swing.JPanel();
        SelectPanel = new javax.swing.JPanel();
        DiversPanel = new javax.swing.JPanel();
        setBackground(new java.awt.Color(0, 0, 51));
        VuPanel.setBackground(new java.awt.Color(0, 0, 51));
        VuBorder = javax.swing.BorderFactory.createTitledBorder(null, "Pourcentage Vu", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 0, 11), new java.awt.Color(255, 255, 255));
        VuPanel.setBorder(VuBorder);
        VuPanel.setForeground(new java.awt.Color(255, 255, 255));
        VuPanel.setLayout(new BorderLayout());
        SelectPanel.setBackground(new java.awt.Color(0, 0, 51));
        SelectBorder = javax.swing.BorderFactory.createTitledBorder(null, "Musiques selectionnees", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 0, 11), new java.awt.Color(255, 255, 255));
        SelectPanel.setBorder(SelectBorder);
        SelectPanel.setLayout(new BorderLayout());
        DiversPanel.setBackground(new java.awt.Color(0, 0, 51));
        DiversBorder = javax.swing.BorderFactory.createTitledBorder(null, "Divers", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 0, 11), new java.awt.Color(255, 255, 255));
        DiversPanel.setBorder(DiversBorder);
        DiversPanel.setLayout(new BorderLayout());
        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addContainerGap().addComponent(VuPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED).addComponent(SelectPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(DiversPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addContainerGap()));
        layout.setVerticalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addContainerGap().addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false).addComponent(DiversPanel, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).addComponent(SelectPanel, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).addComponent(VuPanel, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)).addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));
    }

    private javax.swing.JPanel DiversPanel;

    private javax.swing.JPanel SelectPanel;

    private javax.swing.JPanel VuPanel;

    private TitledBorder DiversBorder;

    private TitledBorder SelectBorder;

    private TitledBorder VuBorder;

    public void changeDiversPanel(JPanel panelContenu, String titleBorder) {
        DiversBorder.setTitle(titleBorder);
        changePanel(DiversPanel, panelContenu);
    }

    public void changeSelectPanel(JPanel panelContenu, String titleBorder) {
        SelectBorder.setTitle(titleBorder);
        changePanel(SelectPanel, panelContenu);
    }

    public void changeVuPanel(JPanel panelContenu, String titleBorder) {
        VuBorder.setTitle(titleBorder);
        changePanel(VuPanel, panelContenu);
    }

    private void changePanel(JPanel panelConteneur, JPanel panelContenu) {
        panelConteneur.removeAll();
        panelConteneur.add(BorderLayout.CENTER, panelContenu);
        panelConteneur.setVisible(false);
        panelConteneur.setVisible(true);
    }

    public void setUser1(User user) {
        this.user1 = user;
    }

    public void setUser2(User user) {
        this.user2 = user;
    }

    public void setGenre(String genre) {
    }

    public void updateUsers() {
    }
}
