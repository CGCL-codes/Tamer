package cliente.dialogos;

import java.awt.Container;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.event.*;
import javax.swing.*;
import cliente.InfoJuego;

/**
 * Di&aacute;logo de selecci&oacute;n de actividades iniciales: jugar un juego de 1 jugador,
 * o de 2 jugadores remotos.
 */
public class DialogoBienvenida extends JDialog implements ActionListener {

    public static final long serialVersionUID = 1;

    private InfoJuego info;

    private JButton unJugador;

    private JButton dosJugadores;

    private JButton salir;

    private Frame parent;

    public DialogoBienvenida(Frame parent, boolean modal) {
        super(parent, modal);
        this.parent = parent;
        info = new InfoJuego();
        InitComponents();
    }

    private void InitComponents() {
        setTitle("Bienvenida");
        setSize(300, 200);
        addWindowListener(new WindowAdapter() {

            @Override
            public void windowClosing(WindowEvent e) {
                setVisible(false);
                dispose();
            }
        });
        Container contentPane = getContentPane();
        contentPane.setLayout(new GridLayout(3, 1));
        unJugador = new JButton();
        unJugador.setName("unJugador");
        unJugador.setText("Jugar un juego contra la computadora");
        unJugador.addActionListener(this);
        contentPane.add(unJugador);
        dosJugadores = new JButton();
        dosJugadores.setName("dosJugadores");
        dosJugadores.setText("Crear o unirse a un juego en red");
        dosJugadores.addActionListener(this);
        contentPane.add(dosJugadores);
        salir = new JButton();
        salir.setText("Salir");
        salir.addActionListener(this);
        contentPane.add(salir);
    }

    public InfoJuego Mostrar() {
        setVisible(true);
        return info;
    }

    @Override
    public void actionPerformed(ActionEvent evento) {
        Object origen = evento.getSource();
        if (origen == unJugador) {
            DialogoUnJugador d = new DialogoUnJugador(parent, true);
            info = d.Mostrar();
            info.jugar = true;
            info.esLocal = true;
            setVisible(false);
            dispose();
        }
        if (origen == dosJugadores) {
            DialogoConectar d = new DialogoConectar(parent, true);
            info = d.Mostrar();
            info.jugar = true;
            info.esLocal = false;
            setVisible(false);
            dispose();
        }
        if (origen == salir) {
            info.jugar = false;
            setVisible(false);
            dispose();
        }
    }
}
