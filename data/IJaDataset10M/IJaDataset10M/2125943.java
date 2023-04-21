package interfaz_visual;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JEditorPane;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.ListModel;
import javax.swing.WindowConstants;
import javax.swing.border.BevelBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.SwingUtilities;
import espectaculo.EmpresaDeEspectaculos;

/**
* This code was edited or generated using CloudGarden's Jigloo
* SWT/Swing GUI Builder, which is free for non-commercial
* use. If Jigloo is being used commercially (ie, by a corporation,
* company or business for any purpose whatever) then you
* should purchase a license for each developer using Jigloo.
* Please visit www.cloudgarden.com for details.
* Use of Jigloo implies acceptance of these licensing terms.
* A COMMERCIAL LICENSE HAS NOT BEEN PURCHASED FOR
* THIS MACHINE, SO JIGLOO OR THIS CODE CANNOT BE USED
* LEGALLY FOR ANY CORPORATE OR COMMERCIAL PURPOSE.
*/
public class MenuModificacion extends javax.swing.JFrame {

    private JPanel jPanel1;

    private JList espectaculosJList1;

    private JLabel jLabel4;

    private JLabel jLabel5;

    private JLabel jLabel12;

    private JTextField jTextField8;

    private JLabel jLabel11;

    private JTextField jTextField7;

    private JLabel jLabel10;

    private JTextField jTextField6;

    private JButton jButton4;

    private JButton jButton3;

    private JButton jButton2;

    private JButton jButton1;

    private JTextField jTextField10;

    private JLabel jLabel13;

    private JTextField jTextField9;

    private JLabel jLabel9;

    private JTextField jTextField5;

    private JLabel jLabel8;

    private JTextField jTextField4;

    private JLabel jLabel7;

    private JTextField jTextField3;

    private JLabel jLabel6;

    private JEditorPane jEditorPane1;

    private JPanel jPanel3;

    private JTextField jTextField2;

    private JTextField jTextField1;

    private JLabel jLabel3;

    private JLabel jLabel2;

    private JPanel jPanel2;

    private JLabel jLabel1;

    private NuevoEspectaculo ne = new NuevoEspectaculo();

    private EmpresaDeEspectaculos modeloEmpresaDeEspectaculos = new EmpresaDeEspectaculos();

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {

            public void run() {
                MenuModificacion inst = new MenuModificacion();
                inst.setLocationRelativeTo(null);
                inst.setVisible(true);
            }
        });
    }

    public MenuModificacion() {
        super();
        initGUI();
    }

    private void initGUI() {
        try {
            setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
            getContentPane().setLayout(null);
            this.setResizable(false);
            this.setPreferredSize(new java.awt.Dimension(630, 742));
            {
                jPanel1 = new JPanel();
                getContentPane().add(jPanel1);
                jPanel1.setBounds(12, 27, 504, 245);
                jPanel1.setBorder(BorderFactory.createEtchedBorder(BevelBorder.LOWERED));
                jPanel1.setLayout(null);
                {
                    espectaculosJList1 = new JList();
                    jPanel1.add(espectaculosJList1);
                    espectaculosJList1.setListData(this.modeloEmpresaDeEspectaculos.pasar());
                    espectaculosJList1.setBounds(13, 15, 465, 217);
                    espectaculosJList1.addListSelectionListener(new ListSelectionListener() {

                        public void valueChanged(ListSelectionEvent evt) {
                            espectaculosJList1ValueChanged(evt);
                        }
                    });
                }
            }
            {
                jLabel1 = new JLabel();
                getContentPane().add(jLabel1);
                jLabel1.setText("Espectaculos disponibles");
                jLabel1.setBounds(12, 7, 179, 14);
            }
            {
                jPanel2 = new JPanel();
                getContentPane().add(jPanel2);
                jPanel2.setBounds(12, 308, 504, 371);
                jPanel2.setBorder(BorderFactory.createEtchedBorder(BevelBorder.LOWERED));
                jPanel2.setToolTipText("Morosos incobrables: Cooper, David. Fontanes, Juan Manuel. Gonzalez, Pablo Gabriel.");
                jPanel2.setLayout(null);
                {
                    jLabel3 = new JLabel();
                    jPanel2.add(jLabel3);
                    jLabel3.setText("Nombre");
                    jLabel3.setBounds(14, 23, 64, 14);
                }
                {
                    jTextField1 = new JTextField();
                    jPanel2.add(jTextField1);
                    jTextField1.setBounds(115, 20, 361, 21);
                }
                {
                    jLabel4 = new JLabel();
                    jPanel2.add(jLabel4);
                    jLabel4.setText("Artista(s)");
                    jLabel4.setBounds(14, 60, 73, 14);
                }
                {
                    jTextField2 = new JTextField();
                    jPanel2.add(jTextField2);
                    jTextField2.setBounds(115, 53, 361, 21);
                }
                {
                    jLabel5 = new JLabel();
                    jPanel2.add(jLabel5);
                    jLabel5.setText("Descripcion");
                    jLabel5.setBounds(14, 92, 101, 14);
                }
                {
                    jPanel3 = new JPanel();
                    jPanel2.add(jPanel3);
                    jPanel3.setBounds(14, 106, 462, 132);
                    jPanel3.setBorder(BorderFactory.createEtchedBorder(BevelBorder.LOWERED));
                    jPanel3.setLayout(null);
                    {
                        jEditorPane1 = new JEditorPane();
                        jPanel3.add(jEditorPane1);
                        jEditorPane1.setBounds(14, 14, 434, 104);
                    }
                }
                {
                    jLabel6 = new JLabel();
                    jPanel2.add(jLabel6);
                    jLabel6.setText("Lugar de realizacion");
                    jLabel6.setBounds(10, 250, 162, 14);
                }
                {
                    jTextField3 = new JTextField();
                    jPanel2.add(jTextField3);
                    jTextField3.setBounds(122, 247, 354, 21);
                }
                {
                    jLabel7 = new JLabel();
                    jPanel2.add(jLabel7);
                    jLabel7.setText("Fecha(DD/MM/AAAA)");
                    jLabel7.setBounds(10, 288, 126, 14);
                }
                {
                    jTextField4 = new JTextField();
                    jPanel2.add(jTextField4);
                    jTextField4.setBounds(122, 285, 21, 21);
                }
                {
                    jLabel8 = new JLabel();
                    jPanel2.add(jLabel8);
                    jLabel8.setText("/");
                    jLabel8.setBounds(150, 288, 10, 14);
                }
                {
                    jTextField5 = new JTextField();
                    jPanel2.add(jTextField5);
                    jTextField5.setBounds(160, 285, 21, 21);
                }
                {
                    jLabel9 = new JLabel();
                    jPanel2.add(jLabel9);
                    jLabel9.setText("/");
                    jLabel9.setBounds(190, 288, 10, 14);
                }
                {
                    jTextField6 = new JTextField();
                    jPanel2.add(jTextField6);
                    jTextField6.setBounds(207, 285, 51, 21);
                }
                {
                    jLabel10 = new JLabel();
                    jPanel2.add(jLabel10);
                    jLabel10.setText("Hora(HH/MM)");
                    jLabel10.setBounds(333, 288, 91, 14);
                }
                {
                    jTextField7 = new JTextField();
                    jPanel2.add(jTextField7);
                    jTextField7.setBounds(424, 285, 20, 21);
                }
                {
                    jLabel11 = new JLabel();
                    jPanel2.add(jLabel11);
                    jLabel11.setText(":");
                    jLabel11.setBounds(446, 288, 10, 14);
                }
                {
                    jTextField8 = new JTextField();
                    jPanel2.add(jTextField8);
                    jTextField8.setBounds(456, 285, 20, 21);
                }
                {
                    jLabel12 = new JLabel();
                    jPanel2.add(jLabel12);
                    jLabel12.setText("Valor de la entrada");
                    jLabel12.setBounds(12, 326, 126, 14);
                }
                {
                    jTextField9 = new JTextField();
                    jPanel2.add(jTextField9);
                    jTextField9.setBounds(122, 323, 55, 21);
                }
                {
                    jLabel13 = new JLabel();
                    jPanel2.add(jLabel13);
                    jLabel13.setText("Cantidad de entradas");
                    jLabel13.setBounds(246, 326, 146, 14);
                }
                {
                    jTextField10 = new JTextField();
                    jPanel2.add(jTextField10);
                    jTextField10.setBounds(389, 323, 86, 21);
                }
            }
            {
                jLabel2 = new JLabel();
                getContentPane().add(jLabel2);
                jLabel2.setText("Espectaculos");
                jLabel2.setBounds(12, 288, 129, 14);
            }
            {
                jButton1 = new JButton();
                getContentPane().add(jButton1);
                jButton1.setText("Nuevo");
                jButton1.setBounds(522, 27, 84, 21);
                jButton1.addActionListener(new ActionListener() {

                    public void actionPerformed(ActionEvent evt) {
                        jButton1ActionPerformed(evt);
                        ne.setVisible(true);
                    }
                });
            }
            {
                jButton2 = new JButton();
                getContentPane().add(jButton2);
                jButton2.setText("Eliminar");
                jButton2.setBounds(522, 69, 84, 21);
            }
            {
                jButton3 = new JButton();
                getContentPane().add(jButton3);
                jButton3.setText("Aplicar");
                jButton3.setBounds(522, 308, 84, 21);
            }
            {
                jButton4 = new JButton();
                getContentPane().add(jButton4);
                jButton4.setText("Cerrar");
                jButton4.setBounds(522, 679, 84, 21);
                jButton4.addActionListener(new ActionListener() {

                    public void actionPerformed(ActionEvent evt) {
                        jButton4ActionPerformed(evt);
                    }
                });
            }
            pack();
            this.setSize(630, 742);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public JPanel getJPanel2() {
        return jPanel2;
    }

    public void setJPanel2(JPanel panel2) {
        jPanel2 = panel2;
    }

    private void jButton1ActionPerformed(ActionEvent evt) {
        System.out.println("Ingresa nuevo espectaculo");
        jPanel1.setVisible(true);
    }

    private void jButton4ActionPerformed(ActionEvent evt) {
        System.out.println("Cerrando");
        this.dispose();
    }

    private void espectaculosJList1ValueChanged(ListSelectionEvent evt) {
        System.out.println("espectaculosJList1.valueChanged,event=" + evt);
        if (this.espectaculosJList1.getSelectedValue() != null) {
            espectaculo.Espectaculo espectaculo = (espectaculo.Espectaculo) this.espectaculosJList1.getSelectedValue();
        }
    }
}
