package krowdix.interfaz.botones;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.Vector;
import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JEditorPane;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
import krowdix.control.Controlador;
import krowdix.interfaz.AreaTrabajo;
import krowdix.interfaz.Botonera;
import krowdix.interfaz.Interfaz;
import krowdix.interfaz.VentanaHerramientas;

/**
 * @author Daniel Alonso Fernández
 */
public class BotonAbrir extends Boton {

    private static final long serialVersionUID = 1L;

    public BotonAbrir() {
        setIcon(new ImageIcon(getClass().getResource("/iconos/abrir.png")));
        setMargin(new Insets(0, 0, 0, 0));
        setToolTipText("Cargar una red social previamente guardada");
        addActionListener(this);
    }

    @Override
    public void abrirAyuda() throws IOException {
        JEditorPane editorPane = new JEditorPane(getClass().getResource("/ayuda/abrir.htm"));
        editorPane.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(editorPane);
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.setPreferredSize(new Dimension(400, 500));
        JOptionPane.showMessageDialog(Interfaz.getInterfaz(), scrollPane, "Krowdix » Abrir red", JOptionPane.PLAIN_MESSAGE);
    }

    public void actionPerformed(ActionEvent e) {
        if (Botonera.getBotonera().owner == this) {
            Botonera.getBotonera().cerrarPanelSecundario();
        } else {
            Botonera.getBotonera().owner = this;
            Color colorBG = new Color(226, 226, 223);
            Botonera.getBotonera().panelSecundario.setBackground(colorBG);
            Box bS = Botonera.getBotonera().botoneraSecundaria;
            bS.removeAll();
            bS.add(Botonera.nuevoSeparador());
            bS.add(Botonera.nuevoSeparador());
            File[] encontrados = new File(".").listFiles(new FilenameFilter() {

                public boolean accept(File dir, String name) {
                    return name.endsWith(".sqlite");
                }
            });
            final Vector<String> archivos = new Vector<String>(encontrados.length);
            for (File file : encontrados) {
                archivos.add(file.getName().replace(".sqlite", ""));
            }
            if (archivos.size() == 0) {
                Botonera.getBotonera().textoPanelSecundario.setText("No hay ningún archivo guardado. ¿Desea crear una nueva red?");
                JButton nuevaRed = new JButton("Crear nueva red", new ImageIcon(getClass().getResource("/iconos/nuevopeque.png")));
                nuevaRed.setAlignmentY(BOTTOM_ALIGNMENT);
                nuevaRed.setBackground(colorBG);
                nuevaRed.setToolTipText("Abrir el panel de creación de nueva red");
                nuevaRed.addActionListener(new ActionListener() {

                    public void actionPerformed(ActionEvent e1) {
                        Botonera.getBotonera().nuevaRed.doClick();
                    }
                });
                bS.add(nuevaRed);
            } else {
                Botonera.getBotonera().textoPanelSecundario.setText("Se han encontrado las siguientes redes. Seleccione la que desea abrir:");
                final JComboBox comboBox = new JComboBox(archivos.toArray());
                comboBox.setMaximumSize(new Dimension(190, 24));
                comboBox.setAlignmentY(BOTTOM_ALIGNMENT);
                bS.add(comboBox);
                bS.add(Botonera.nuevoSeparador());
                final JButton abrirRed = new JButton("Abrir esta red", new ImageIcon(getClass().getResource("/iconos/abrirpeque.png")));
                abrirRed.setAlignmentY(BOTTOM_ALIGNMENT);
                abrirRed.setBackground(colorBG);
                abrirRed.setToolTipText("Abrir el archivo seleccionado");
                abrirRed.addActionListener(new ActionListener() {

                    public void actionPerformed(ActionEvent e1) {
                        String tmp = archivos.get(comboBox.getSelectedIndex());
                        Interfaz.getInterfaz().reiniciar();
                        if (Controlador.getControlador().cargar(tmp)) {
                            Interfaz.getInterfaz().bdActual = tmp;
                            Interfaz.getInterfaz().setTitle("Krowdix [" + Interfaz.getInterfaz().bdActual + "]");
                            Interfaz.getInterfaz().actualizar();
                            AreaTrabajo.getAreaTrabajo().reordenar();
                            VentanaHerramientas.getVentanaHerramientas().reiniciar();
                        } else {
                            JOptionPane.showMessageDialog(Interfaz.getInterfaz(), "Error al cargar la base de datos", "Krowdix", JOptionPane.ERROR_MESSAGE);
                        }
                        Botonera.getBotonera().cerrarPanelSecundario();
                    }
                });
                comboBox.addKeyListener(new KeyListener() {

                    public void keyPressed(KeyEvent e1) {
                    }

                    public void keyReleased(KeyEvent e1) {
                        if (e1.getKeyCode() == KeyEvent.VK_ENTER) {
                            abrirRed.doClick();
                        }
                    }

                    public void keyTyped(KeyEvent e1) {
                    }
                });
                bS.add(abrirRed);
            }
            Botonera.getBotonera().panelSecundario.setVisible(true);
            Botonera.getBotonera().validate();
        }
    }
}
