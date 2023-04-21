package alecava;

/**
 *
 * @author  alecava
 */
public class Lyrics extends javax.swing.JFrame {

    private AbstractCavaID3 mp3;

    /** Creates new form Lyrics */
    public Lyrics(AbstractCavaID3 e) {
        initComponents();
        this.setVisible(true);
        this.mp3 = e;
        textLyrics.setText(mp3.getLyric());
    }

    private void initComponents() {
        jScrollPane1 = new javax.swing.JScrollPane();
        textLyrics = new javax.swing.JTextArea();
        btnSalva = new javax.swing.JButton();
        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Lyrics Editor");
        textLyrics.setColumns(20);
        textLyrics.setRows(5);
        jScrollPane1.setViewportView(textLyrics);
        btnSalva.setText("Salva");
        btnSalva.addMouseListener(new java.awt.event.MouseAdapter() {

            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnSalvaMouseClicked(evt);
            }
        });
        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addContainerGap().addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 381, Short.MAX_VALUE).addComponent(btnSalva, javax.swing.GroupLayout.Alignment.TRAILING)).addContainerGap()));
        layout.setVerticalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addContainerGap().addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 261, Short.MAX_VALUE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(btnSalva).addContainerGap()));
        pack();
    }

    private void btnSalvaMouseClicked(java.awt.event.MouseEvent evt) {
        this.mp3.setLyric(textLyrics.getText());
        this.dispose();
    }

    private javax.swing.JButton btnSalva;

    private javax.swing.JScrollPane jScrollPane1;

    private javax.swing.JTextArea textLyrics;
}
