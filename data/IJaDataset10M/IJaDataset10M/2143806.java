package ui.editpoke;

import clase.*;
import estru.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;
import javax.swing.event.*;
import mi_comp.*;
import st.*;
import ui.EditorV;

/**
 *
 * @author Julián
 */
public class EditPoke_Genera extends javax.swing.JPanel {

    private JLabel nmEO_jLabe;

    private Mi_JTextField nmEO_jTxFl;

    private JLabel gnEO_jLabe;

    private ButtonGroup gnEO_BtGr;

    private Mi_JRadioButton msEO_jRdBt;

    private Mi_JRadioButton fmEO_jRdBt;

    private JLabel nmID_jLabe;

    private Mi_JTextField nmID_jTxFl;

    private JLabel nmSD_jLabe;

    private Mi_JTextField nmSD_jTxFl;

    private Mi_JButton dtEn_jButt;

    private DatosEntrenador datoEntrenad;

    private final Edicion EditPokemo;

    private final ResourceBundle RB_EditPoke_Gene;

    /** Creates new form EditPoke_Genera */
    public EditPoke_Genera(Edicion editBate) {
        this.EditPokemo = editBate;
        this.RB_EditPoke_Gene = ResourceBundle.getBundle(Static.DireBaseRB + getClass().getSimpleName(), EditorV.Idioma);
        initComponents();
        init_Components();
    }

    private void init_Components() {
        jLyPn.setPreferredSize(new Dimension(512 - 6, 304 - 6));
        jLyPn.setBounds(new Rectangle(new Point(0, 0), jLyPn.getPreferredSize()));
        nmEO_jLabe = new JLabel();
        nmEO_jLabe.setText("texto");
        nmEO_jLabe.setName("nmEO_jLabe");
        nmEO_jLabe.setPreferredSize(new Dimension(80, 16));
        nmEO_jLabe.setBounds(new Rectangle(new Point(186, 20), nmEO_jLabe.getPreferredSize()));
        jLyPn.add(nmEO_jLabe, JLayeredPane.DEFAULT_LAYER);
        nmEO_jTxFl = new Mi_JTextField();
        nmEO_jTxFl.modoAlfanumerico(7);
        nmEO_jTxFl.setName("nmEO_jTxFl");
        nmEO_jTxFl.setPreferredSize(new Dimension(110, 28));
        nmEO_jTxFl.setBounds(new Rectangle(new Point(274, 14), nmEO_jTxFl.getPreferredSize()));
        jLyPn.add(nmEO_jTxFl, JLayeredPane.DEFAULT_LAYER);
        gnEO_jLabe = new JLabel();
        gnEO_jLabe.setText("texto");
        gnEO_jLabe.setName("gnEO_jLabe");
        gnEO_jLabe.setPreferredSize(new Dimension(80, 16));
        gnEO_jLabe.setBounds(new Rectangle(new Point(186, 45), nmEO_jLabe.getPreferredSize()));
        jLyPn.add(gnEO_jLabe, JLayeredPane.DEFAULT_LAYER);
        gnEO_BtGr = new ButtonGroup();
        msEO_jRdBt = new Mi_JRadioButton();
        msEO_jRdBt.setForeground(Color.BLUE);
        msEO_jRdBt.setText("texto");
        msEO_jRdBt.setName("msEO_jRdBt");
        msEO_jRdBt.setBounds(new Rectangle(new Point(276, 44), msEO_jRdBt.getPreferredSize()));
        jLyPn.add(msEO_jRdBt, JLayeredPane.DEFAULT_LAYER);
        gnEO_BtGr.add(msEO_jRdBt);
        fmEO_jRdBt = new Mi_JRadioButton();
        fmEO_jRdBt.setForeground(Color.RED);
        fmEO_jRdBt.setText("texto");
        fmEO_jRdBt.setName("fmEO_jRdBt");
        fmEO_jRdBt.setBounds(new Rectangle(new Point(330, 44), msEO_jRdBt.getPreferredSize()));
        jLyPn.add(fmEO_jRdBt, JLayeredPane.DEFAULT_LAYER);
        gnEO_BtGr.add(fmEO_jRdBt);
        nmID_jLabe = new JLabel();
        nmID_jLabe.setText("texto");
        nmID_jLabe.setName("nmID_jLabe");
        nmID_jLabe.setPreferredSize(new Dimension(80, 16));
        nmID_jLabe.setBounds(new Rectangle(new Point(186, 70), nmEO_jLabe.getPreferredSize()));
        jLyPn.add(nmID_jLabe, JLayeredPane.DEFAULT_LAYER);
        nmID_jTxFl = new Mi_JTextField();
        nmID_jTxFl.modoNumerico(0xFFFF);
        nmID_jTxFl.setName("nmID_jTxFl");
        nmID_jTxFl.setPreferredSize(new Dimension(56, 28));
        nmID_jTxFl.setBounds(new Rectangle(new Point(301, 64), nmID_jTxFl.getPreferredSize()));
        jLyPn.add(nmID_jTxFl, JLayeredPane.DEFAULT_LAYER);
        nmSD_jLabe = new JLabel();
        nmSD_jLabe.setText("texto");
        nmSD_jLabe.setName("nmSD_jLabe");
        nmSD_jLabe.setPreferredSize(new Dimension(80, 16));
        nmSD_jLabe.setBounds(new Rectangle(new Point(186, 100), nmSD_jLabe.getPreferredSize()));
        jLyPn.add(nmSD_jLabe, JLayeredPane.DEFAULT_LAYER);
        nmSD_jTxFl = new Mi_JTextField();
        nmSD_jTxFl.modoNumerico(0xFFFF);
        nmSD_jTxFl.setName("nmID_jTxFl");
        nmSD_jTxFl.setPreferredSize(new Dimension(56, 28));
        nmSD_jTxFl.setBounds(new Rectangle(new Point(301, 94), nmSD_jTxFl.getPreferredSize()));
        jLyPn.add(nmSD_jTxFl, JLayeredPane.DEFAULT_LAYER);
        dtEn_jButt = new Mi_JButton();
        dtEn_jButt.setText("texto");
        dtEn_jButt.setName("dtEn_jButt");
        dtEn_jButt.setBounds(new Rectangle(new Point(302, 124), dtEn_jButt.getPreferredSize()));
        jLyPn.add(dtEn_jButt, JLayeredPane.DEFAULT_LAYER);
        EditorV.Init_Container(this, RB_EditPoke_Gene);
        init_jTxFl_jCmBx_DocumentListener(jLyPn);
        init_jButt_ActionPerformed(jLyPn);
    }

    private void init_jTxFl_jCmBx_DocumentListener(Container conten) {
        for (Component comp : conten.getComponents()) {
            if (comp instanceof Mi_JTextField) {
                ((Mi_JTextField) comp).getDocument().addDocumentListener(new DocumentListener() {

                    public void insertUpdate(DocumentEvent e) {
                        jTxFl_jCmBx_DocumentListener(e);
                    }

                    public void removeUpdate(DocumentEvent e) {
                        jTxFl_jCmBx_DocumentListener(e);
                    }

                    public void changedUpdate(DocumentEvent e) {
                        jTxFl_jCmBx_DocumentListener(e);
                    }
                });
            } else if (comp instanceof Mi_JComboBox) {
                Component jTxFl = ((Mi_JComboBox) comp).getEditor().getEditorComponent();
                ((JTextField) jTxFl).getDocument().addDocumentListener(new DocumentListener() {

                    public void insertUpdate(DocumentEvent e) {
                        jTxFl_jCmBx_DocumentListener(e);
                    }

                    public void removeUpdate(DocumentEvent e) {
                        jTxFl_jCmBx_DocumentListener(e);
                    }

                    public void changedUpdate(DocumentEvent e) {
                        jTxFl_jCmBx_DocumentListener(e);
                    }
                });
            } else if (comp instanceof Container) {
                init_jTxFl_jCmBx_DocumentListener((Container) comp);
            }
        }
    }

    private void init_jButt_ActionPerformed(Container conten) {
        for (Component comp : conten.getComponents()) {
            if (comp instanceof Mi_JButton) {
                ((Mi_JButton) comp).addActionListener(new ActionListener() {

                    public void actionPerformed(ActionEvent e) {
                        jButt_ActionPerformed(e);
                    }
                });
            } else if (comp instanceof Container) {
                init_jButt_ActionPerformed((Container) comp);
            }
        }
    }

    private void jTxFl_jCmBx_DocumentListener(DocumentEvent e) {
        EditPokemo.habilitarEdicion();
    }

    private void jButt_ActionPerformed(ActionEvent e) {
        JButton jButt = (JButton) e.getSource();
        if (jButt == dtEn_jButt) {
            nmEO_jTxFl.setTexto(datoEntrenad.getNombreEO());
            if (datoEntrenad.getGeneroEO()) {
                msEO_jRdBt.setSelected(datoEntrenad.getGeneroEO());
            } else {
                fmEO_jRdBt.setSelected(!datoEntrenad.getGeneroEO());
            }
            nmID_jTxFl.setNumero(datoEntrenad.getID());
            nmSD_jTxFl.setNumero(datoEntrenad.getSID());
        }
    }

    public void leerPokemon(PokemonBuffer_V pokBuf) {
        nmEO_jTxFl.setTexto(pokBuf.getNombreEO());
        if (pokBuf.getGeneroEO()) {
            msEO_jRdBt.setSelected(pokBuf.getGeneroEO());
        } else {
            fmEO_jRdBt.setSelected(!pokBuf.getGeneroEO());
        }
        nmID_jTxFl.setNumero(pokBuf.getID());
        nmSD_jTxFl.setNumero(pokBuf.getSID());
    }

    public void escribirPokemon(PokemonBuffer_V pokBuf) {
        pokBuf.setNombreEO(nmEO_jTxFl.getTexto());
        pokBuf.setGeneroEO(msEO_jRdBt.isSelected());
        pokBuf.setID((int) nmID_jTxFl.getNumero());
        pokBuf.setSID((int) nmSD_jTxFl.getNumero());
    }

    public void reiniciarConten() {
        EditorV.ReinicarEntrad(jLyPn);
    }

    public void datosEntrenador(DatosEntrenador datoEntr) {
        this.datoEntrenad = datoEntr;
    }

    public JButton getDtEn_jButt() {
        return this.dtEn_jButt;
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    private void initComponents() {
        jLyPn = new javax.swing.JLayeredPane();
        setName("EditPoke_Genera");
        jLyPn.setName("jLyPn");
        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(jLyPn, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE));
        layout.setVerticalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(jLyPn, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE));
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {

            public void run() {
                try {
                    javax.swing.UIManager.setLookAndFeel("com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel");
                } catch (Exception ex) {
                    java.util.logging.Logger.getLogger(EditPoke_Genera.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
                }
                javax.swing.JFrame frame = new javax.swing.JFrame();
                frame.setTitle("EditPoke_Genera");
                frame.setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
                EditPoke_Genera jPanel = new EditPoke_Genera(null);
                jPanel.setName("EditPoke_Genera");
                javax.swing.GroupLayout layout = new javax.swing.GroupLayout(frame.getContentPane());
                frame.getContentPane().setLayout(layout);
                layout.setHorizontalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(jPanel, javax.swing.GroupLayout.DEFAULT_SIZE, jPanel.getWidth(), Short.MAX_VALUE));
                layout.setVerticalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(jPanel, javax.swing.GroupLayout.DEFAULT_SIZE, jPanel.getHeight(), Short.MAX_VALUE));
                frame.pack();
                frame.setLocationRelativeTo(null);
                frame.setVisible(true);
            }
        });
    }

    private javax.swing.JLayeredPane jLyPn;
}
