package globalwars;

import java.util.ArrayList;
import javax.swing.DefaultListModel;

public class GameList extends javax.swing.JFrame {

    private Player player;

    /** Creates new form GameList */
    public GameList() {
        initComponents();
    }

    public void setPlayer(Player p) {
        player = p;
    }

    public void setList(ArrayList<GamePost> lol) {
        int index = jList1.getSelectedIndex();
        index = (index >= 0 ? index : 0);
        DefaultListModel model = new DefaultListModel();
        for (GamePost p : lol) {
            model.addElement(p);
        }
        jList1.setModel(model);
        jList1.revalidate();
        jList1.setSelectedIndex(index);
    }

    private void initComponents() {
        jScrollPane1 = new javax.swing.JScrollPane();
        jList1 = new javax.swing.JList();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Global Wars - Select Game");
        jList1.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        jScrollPane1.setViewportView(jList1);
        jButton1.setText("Join selected game");
        jButton1.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        jButton2.setText("Refresh list");
        jButton2.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });
        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addContainerGap().addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false).addComponent(jButton1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 221, Short.MAX_VALUE)).addComponent(jButton2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 221, Short.MAX_VALUE)).addContainerGap()));
        layout.setVerticalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addContainerGap(25, Short.MAX_VALUE).addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 251, javax.swing.GroupLayout.PREFERRED_SIZE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(jButton1).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(jButton2)));
        pack();
    }

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {
        GamePost game = (GamePost) jList1.getSelectedValue();
        Main.main.output("Joining game " + game.name + "...");
        player.getClient().joinGame(game.id);
        this.setVisible(false);
        Main.main.disposeGL();
    }

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {
        player.getClient().listGames();
    }

    private javax.swing.JButton jButton1;

    private javax.swing.JButton jButton2;

    private javax.swing.JList jList1;

    private javax.swing.JScrollPane jScrollPane1;
}
