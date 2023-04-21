package org.digitall.projects.gdigitall.lib.components;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Calendar;
import java.util.Timer;
import java.util.Vector;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import org.digitall.projects.gdigitall.lib.misc.OP_Proced;

public class SelectorFecha extends JDialog {

    private SelectorFechaPanel calendar = new SelectorFechaPanel(Calendar.MONDAY, true, 1940, 2099, this);

    private JButton bseleccionar = new JButton();

    private JButton bcerrar = new JButton();

    private JLabel jLabel2 = new JLabel();

    private JPanel jPanel1 = new JPanel();

    private JTextField jtfechaselec = new JTextField();

    private String Consulta = "", ConsultaCount = "", cfiltro = "";

    private Vector datos1, datosx = new Vector();

    private int[] vcol = { 2 };

    private int[] tcol = { 80, 320 };

    private String fechax = "", mesx = "", aniox = "";

    static String fechaselec, lafecha = "";

    static String band = "";

    private Timer timer1 = new Timer();

    private JTextField jtfecha = new JTextField();

    /**
   * FORMULARIO PARA LLEVAR UN CALENDARIO LABORAL, SE DEBERA CARGAR LOS DIAS QUE NO SON LABORALES.
   * YA SEA POR SABADO Y DOMINGO, FERIADOS U OTROS.
   */
    public SelectorFecha(JTextField _jtfecha) {
        jtfecha = _jtfecha;
        try {
            jbInit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void jbInit() throws Exception {
        this.getContentPane().setLayout(null);
        this.setSize(new Dimension(353, 285));
        this.setTitle("Calendario");
        this.setBackground(new Color(112, 145, 204));
        calendar.setBounds(new Rectangle(10, 10, 320, 210));
        calendar.setBackground(Color.white);
        bseleccionar.setText("Seleccionar");
        bseleccionar.setBounds(new Rectangle(25, 360, 129, 25));
        bseleccionar.setMnemonic('s');
        bseleccionar.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                bseleccionar_actionPerformed(e);
            }
        });
        bcerrar.setText("Cerrar");
        bcerrar.setBounds(new Rectangle(240, 225, 92, 25));
        bcerrar.setMnemonic('c');
        jPanel1.setBackground(new Color(112, 145, 204));
        jtfechaselec.setEditable(false);
        jtfechaselec.setDisabledTextColor(Color.red);
        jtfechaselec.setEnabled(false);
        jtfechaselec.setBackground(Color.white);
        calendar.initializeCalendar();
        this.getContentPane().add(jLabel2, null);
        jPanel1.add(jtfechaselec, null);
        this.getContentPane().add(jPanel1, null);
        this.getContentPane().add(bcerrar, null);
        this.getContentPane().add(bseleccionar, null);
        this.getContentPane().add(calendar, null);
        jtfechaselec.setText("00/00/0000");
        jLabel2.setForeground(Color.blue);
        jLabel2.setFont(new Font("Dialog", 1, 13));
        jLabel2.setOpaque(true);
        jPanel1.setLayout(null);
        jPanel1.setBorder(BorderFactory.createLineBorder(Color.blue, 1));
        jPanel1.setBounds(new Rectangle(20, 310, 325, 45));
        jtfechaselec.setForeground(Color.red);
        jtfechaselec.setFont(new Font("Dialog", 1, 15));
        jLabel2.setBounds(new Rectangle(30, 300, 138, 15));
        jLabel2.setText(" Fecha Seleccionada ");
        jtfechaselec.setBounds(new Rectangle(105, 10, 100, 25));
        ((JPanel) this.getContentPane()).setBackground(new Color(112, 145, 204));
        bcerrar.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                bcerrar_actionPerformed(e);
            }
        });
    }

    private void bcerrar_actionPerformed(ActionEvent e) {
        this.dispose();
    }

    /**
   * SELECCIONA UNA FECHA DETERMINADA Y LA DEVUELVE AL FORMULARIO QUE LA SOLICITO
   */
    private void bseleccionar_actionPerformed(ActionEvent e) {
        jtfecha.setText(jtfechaselec.getText());
        this.dispose();
    }

    public void setFechaX(String _fechax) {
        fechax = _fechax;
        try {
            jtfecha.setText(OP_Proced.Fecha2(fechax, true));
            dispose();
        } catch (Exception x) {
            x.printStackTrace();
            OP_Proced.Mensaje("Error al asignar la fecha", "Error");
        }
    }
}
