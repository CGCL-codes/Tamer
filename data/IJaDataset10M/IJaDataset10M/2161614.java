package main;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JOptionPane;
import estructuras.Estrategia;

public class correlationDialog extends JDialog {

    /**
	 * 
	 */
    private static final long serialVersionUID = 4794454297193092486L;

    window_main window;

    Estrategia estr;

    File file;

    private final JLabel seleccioneElArchivoLabel = new JLabel();

    private final JPanel panel = new JPanel();

    private final JButton aceptarButton = new JButton();

    private final JButton cargarDataButton = new JButton();

    private final JPanel panel_1 = new JPanel();

    private final JPanel panel_2 = new JPanel();

    private final JTextField textField = new JTextField();

    private final JLabel decaimientoLabel = new JLabel();

    public correlationDialog(window_main window, Estrategia estr) {
        super(window.frame, "Creaci�n de matriz de correlaciones", true);
        try {
            jbInit();
        } catch (Throwable e) {
            e.printStackTrace();
        }
        this.window = window;
        this.estr = estr;
    }

    private void jbInit() throws Exception {
        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(panel, BorderLayout.SOUTH);
        this.setSize(478, 130);
        panel.add(cargarDataButton);
        cargarDataButton.addActionListener(new CargarDataButtonActionListener());
        cargarDataButton.setPreferredSize(new Dimension(120, 25));
        cargarDataButton.setText("Cargar Data");
        panel.add(aceptarButton);
        aceptarButton.addActionListener(new AceptarButtonActionListener());
        aceptarButton.setEnabled(false);
        aceptarButton.setPreferredSize(new Dimension(120, 25));
        aceptarButton.setText("Aceptar");
        getContentPane().add(panel_1);
        panel_1.setLayout(new BorderLayout());
        panel_1.add(seleccioneElArchivoLabel, BorderLayout.NORTH);
        seleccioneElArchivoLabel.setText("  Seleccione el archivo que contiene la data hist�rica y el factor de decaimiento");
        panel_1.add(panel_2, BorderLayout.CENTER);
        panel_2.setLayout(null);
        panel_2.add(textField);
        textField.setText("0.97");
        textField.setBounds(100, 10, 100, 20);
        textField.setPreferredSize(new Dimension(100, 20));
        panel_2.add(decaimientoLabel);
        decaimientoLabel.setText("Decaimiento");
        decaimientoLabel.setBounds(8, 12, 86, 16);
    }

    private class CargarDataButtonActionListener implements ActionListener {

        public void actionPerformed(ActionEvent e) {
            cargarDataButton_actionPerformed(e);
        }
    }

    private class AceptarButtonActionListener implements ActionListener {

        public void actionPerformed(ActionEvent e) {
            aceptarButton_actionPerformed(e);
        }
    }

    protected void cargarDataButton_actionPerformed(ActionEvent e) {
        try {
            JFileChooser fc = new JFileChooser();
            int ret = fc.showOpenDialog(window.frame);
            if (ret != JFileChooser.APPROVE_OPTION) return;
            file = fc.getSelectedFile();
            this.aceptarButton.setEnabled(true);
        } catch (Exception ex) {
            this.aceptarButton.setEnabled(false);
        }
    }

    protected void aceptarButton_actionPerformed(ActionEvent e) {
        Double decai = null;
        try {
            decai = Double.parseDouble(textField.getText());
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "El formato del n�mero es incorrecto");
            return;
        }
        window.decaiment = decai.doubleValue();
        window.corrfile = file;
        this.setVisible(false);
        this.dispose();
    }
}
