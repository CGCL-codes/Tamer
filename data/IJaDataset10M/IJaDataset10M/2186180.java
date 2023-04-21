package org.semtinel.plugins.crowdsourcing;

import edu.smu.tspell.wordnet.NounSynset;
import edu.smu.tspell.wordnet.Synset;
import edu.smu.tspell.wordnet.SynsetType;
import edu.smu.tspell.wordnet.WordNetDatabase;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Logger;
import javax.sound.midi.SysexMessage;
import org.openide.util.NbBundle;
import org.openide.windows.TopComponent;
import org.openide.windows.WindowManager;

/**
 * Top component which displays something.
 */
final class CrowdsourcingTopComponent extends TopComponent {

    private static CrowdsourcingTopComponent instance;

    private static final String PREFERRED_ID = "CrowdsourcingTopComponent";

    private CrowdsourcingTopComponent() {
        initComponents();
        setName(NbBundle.getMessage(CrowdsourcingTopComponent.class, "CTL_CrowdsourcingTopComponent"));
        setToolTipText(NbBundle.getMessage(CrowdsourcingTopComponent.class, "HINT_CrowdsourcingTopComponent"));
    }

    private void initComponents() {
        jButton1 = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTextArea1 = new javax.swing.JTextArea();
        org.openide.awt.Mnemonics.setLocalizedText(jButton1, org.openide.util.NbBundle.getMessage(CrowdsourcingTopComponent.class, "CrowdsourcingTopComponent.jButton1.text"));
        jButton1.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        jTextArea1.setColumns(20);
        jTextArea1.setRows(5);
        jScrollPane1.setViewportView(jTextArea1);
        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addContainerGap().addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 380, Short.MAX_VALUE).addComponent(jButton1)).addContainerGap()));
        layout.setVerticalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addContainerGap().addComponent(jButton1).addGap(18, 18, 18).addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 237, Short.MAX_VALUE).addContainerGap()));
    }

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {
        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException cnfe) {
            throw new RuntimeException("MySQL driver not found!", cnfe);
        }
        try {
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/inpho", "root", "mannheim1234");
            String query = "SELECT i1.label as idea1,i1.id, i2.label as idea2, i2.id, i3.label " + "FROM idea as i1, idea as i2, idea_evaluation as ie, ontotree as ot, idea as i3 " + "WHERE i1.id=ie.ante_id and i2.id=ie.cons_id and " + "ie.cons_id=ot.concept_id and i3.id=ot.area_id " + "GROUP BY i1.label,i1.id, i2.label, i2.id, ot.area_id " + "HAVING count(ie.id) > 0 " + "ORDER BY count(ie.id) desc";
            ResultSet rs = con.createStatement().executeQuery(query);
            int count1 = 0, count2 = 0, count3 = 0;
            StringBuffer sb = new StringBuffer();
            while (rs.next()) {
                count3++;
                System.setProperty("wordnet.database.dir", "C:\\Users\\kai\\Desktop\\CROWDSOURCING\\dict");
                WordNetDatabase wordnet = WordNetDatabase.getFileInstance();
                String idea1 = rs.getString("idea1");
                String idea2 = rs.getString("idea2");
                Synset[] synsets1 = wordnet.getSynsets(idea1, SynsetType.NOUN);
                Synset[] synsets2 = wordnet.getSynsets(idea2, SynsetType.NOUN);
                if (synsets1.length > 0 && synsets2.length > 0) {
                    count2++;
                    sb.append("\n   DEFINITIONS: " + idea1);
                    for (Synset s : synsets1) {
                        NounSynset ns = (NounSynset) s;
                        sb.append("\n     " + s.getDefinition());
                        sb.append("\n       Rootline: ");
                        for (NounSynset h : ns.getHypernyms()) {
                            sb.append(h + ", ");
                        }
                    }
                    sb.append("\n   DEFINITIONS: " + idea2);
                    for (Synset s : synsets2) {
                        NounSynset ns = (NounSynset) s;
                        sb.append("\n     " + s.getDefinition());
                        sb.append("\n       Rootline: ");
                        for (NounSynset h : ns.getHypernyms()) {
                            sb.append(h + ", ");
                        }
                    }
                } else if (synsets1.length > 0 || synsets2.length > 0) {
                    count1++;
                }
                sb.append("\n" + idea1 + " (" + synsets1.length + ") - " + idea2 + " (" + synsets2.length + ")");
            }
            sb.append("\nCount 1: " + count1 + " Count 2: " + count2);
            sb.append("\nRows: " + count3);
            jTextArea1.setText(sb.toString());
        } catch (SQLException sqle) {
            throw new RuntimeException("DB Error!", sqle);
        }
    }

    private javax.swing.JButton jButton1;

    private javax.swing.JScrollPane jScrollPane1;

    private javax.swing.JTextArea jTextArea1;

    /**
     * Gets default instance. Do not use directly: reserved for *.settings files only,
     * i.e. deserialization routines; otherwise you could get a non-deserialized instance.
     * To obtain the singleton instance, use {@link #findInstance}.
     */
    public static synchronized CrowdsourcingTopComponent getDefault() {
        if (instance == null) {
            instance = new CrowdsourcingTopComponent();
        }
        return instance;
    }

    /**
     * Obtain the CrowdsourcingTopComponent instance. Never call {@link #getDefault} directly!
     */
    public static synchronized CrowdsourcingTopComponent findInstance() {
        TopComponent win = WindowManager.getDefault().findTopComponent(PREFERRED_ID);
        if (win == null) {
            Logger.getLogger(CrowdsourcingTopComponent.class.getName()).warning("Cannot find " + PREFERRED_ID + " component. It will not be located properly in the window system.");
            return getDefault();
        }
        if (win instanceof CrowdsourcingTopComponent) {
            return (CrowdsourcingTopComponent) win;
        }
        Logger.getLogger(CrowdsourcingTopComponent.class.getName()).warning("There seem to be multiple components with the '" + PREFERRED_ID + "' ID. That is a potential source of errors and unexpected behavior.");
        return getDefault();
    }

    @Override
    public int getPersistenceType() {
        return TopComponent.PERSISTENCE_ALWAYS;
    }

    @Override
    public void componentOpened() {
    }

    @Override
    public void componentClosed() {
    }

    /** replaces this in object stream */
    @Override
    public Object writeReplace() {
        return new ResolvableHelper();
    }

    @Override
    protected String preferredID() {
        return PREFERRED_ID;
    }

    static final class ResolvableHelper implements Serializable {

        private static final long serialVersionUID = 1L;

        public Object readResolve() {
            return CrowdsourcingTopComponent.getDefault();
        }
    }
}
