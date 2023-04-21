package org.digitall.lib.calendar;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Calendar;
import org.digitall.deprecatedlibs.Proced;
import org.digitall.lib.components.basic.BasicButton;
import org.digitall.lib.components.basic.BasicDialog;
import org.digitall.lib.components.basic.BasicLabel;
import org.digitall.lib.components.basic.BasicPanel;
import org.digitall.lib.components.basic.BasicTextInput;
import org.digitall.lib.components.buttons.CloseButton;

public class SelectorFecha extends BasicDialog {

    private SelectorFechaPanel calendar = new SelectorFechaPanel(Calendar.MONDAY, true, 1900, 2099, this);

    private BasicButton bseleccionar = new BasicButton();

    private CloseButton bcerrar = new CloseButton();

    private BasicLabel jLabel2 = new BasicLabel();

    private BasicPanel jPanel1 = new BasicPanel();

    private BasicTextInput jtfechaselec = new BasicTextInput();

    private String fechax = "";

    static String fechaselec, lafecha = "";

    static String band = "";

    private BasicTextInput jtfecha = new BasicTextInput();

    /**
     * FORMULARIO PARA LLEVAR UN CALENDARIO LABORAL, SE DEBERA CARGAR LOS DIAS QUE NO SON LABORALES.
     * YA SEA POR SABADO Y DOMINGO, FERIADOS U OTROS.
     */
    public SelectorFecha(BasicTextInput _jtfecha) {
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
        jLabel2.setFont(new Font("Dialog", 1, 13));
        jLabel2.setOpaque(true);
        jPanel1.setLayout(null);
        jPanel1.setBounds(new Rectangle(20, 310, 325, 45));
        jtfechaselec.setForeground(Color.red);
        jtfechaselec.setFont(new Font("Dialog", 1, 15));
        jLabel2.setBounds(new Rectangle(30, 300, 138, 15));
        jLabel2.setText(" Fecha Seleccionada ");
        jtfechaselec.setBounds(new Rectangle(105, 10, 100, 25));
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
            jtfecha.setValue(Proced.setFormatDate(fechax, true));
            dispose();
        } catch (Exception x) {
            x.printStackTrace();
            org.digitall.lib.components.Advisor.messageBox("Error al asignar la fecha", "Error");
        }
    }
}
