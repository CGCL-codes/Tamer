package controlador;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import modelo.Frase;
import modelo.FraseDao;
import modelo.Nivel;
import modelo.NivelDao;
import vista.Mensaje;
import vista.VentanaFrase;

public class ControladorVentanaFrase extends WindowAdapter implements ActionListener, KeyListener {

    private VentanaFrase ventanaFrase;

    private FraseDao fraseDAO;

    private ControladorPrincipal controladorPrincipal;

    private int nivel;

    public ControladorVentanaFrase(ControladorPrincipal controladorPrincipal) {
        this.controladorPrincipal = controladorPrincipal;
        this.ventanaFrase = new VentanaFrase();
        this.ventanaFrase.addListener((ActionListener) this);
        this.ventanaFrase.addListener((WindowListener) this);
        this.ventanaFrase.addListener((KeyListener) this);
        this.ventanaFrase.setVisible(true);
        this.ventanaFrase.setLocationRelativeTo(null);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("Guardar")) {
            registrarFrase();
        } else if (e.getActionCommand().equals("Salir")) {
            ventanaFrase.setVisible(false);
            ventanaFrase.dispose();
            controladorPrincipal.visualizarVentanaPrincipal();
        }
    }

    public void registrarFrase() {
        try {
            if (ventanaFrase.getNivel().equals("") || ventanaFrase.getFrase().equals("")) Mensaje.mostrarInformacion("Llene todo"); else {
                fraseDAO = new FraseDao();
                nivel = 0;
                if (ventanaFrase.getNivel().equals("BASICO")) {
                    nivel = 1;
                } else if (ventanaFrase.getNivel().equals("INTERMEDIO")) {
                    nivel = 2;
                } else if (ventanaFrase.getNivel().equals("AVANZADO")) {
                    nivel = 3;
                }
                NivelDao nivelDao = new NivelDao();
                int min = nivelDao.getMinPalabras(nivel);
                int max = nivelDao.getMaxPalabras(nivel);
                String[] palabras = ventanaFrase.getFrase().split(" ");
                String fraseGuardar = "";
                for (String palabra : palabras) {
                    if (!palabra.trim().isEmpty()) {
                        fraseGuardar += palabra + " ";
                    }
                }
                fraseGuardar = fraseGuardar.trim();
                palabras = fraseGuardar.split(" ");
                if (palabras.length >= min && palabras.length <= max) {
                    Nivel objNivel = new Nivel();
                    objNivel.setCodigo(nivel);
                    Frase frase = new Frase(fraseGuardar, objNivel);
                    fraseDAO.registrarFrase(frase);
                    Mensaje.mostrarInformacion("La Frase se ha insertado con Exito");
                } else {
                    if (nivel == 1) Mensaje.mostrarError("Las Frases en este Nivel deben tener maximo " + max + " palabras"); else Mensaje.mostrarError("Las Frases en este Nivel deben tener entre " + min + " y " + max + " palabras");
                }
                ventanaFrase.reiniciar();
            }
        } catch (Exception e) {
            Mensaje.mostrarError("No se pudo Guardar");
        }
    }

    @Override
    public void windowClosing(WindowEvent arg0) {
        controladorPrincipal.visualizarVentanaPrincipal();
        ventanaFrase.setVisible(false);
        ventanaFrase.dispose();
    }

    @Override
    public void keyTyped(KeyEvent e) {
        if (!(Character.isLetter(e.getKeyChar()) || Character.isSpaceChar(e.getKeyChar()))) {
            e.consume();
        } else {
            e.setKeyChar(Character.toUpperCase(e.getKeyChar()));
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }
}
