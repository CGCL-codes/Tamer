package view.componentes;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JLabel;

public class JLabelButton extends JLabel {

    private static final long serialVersionUID = 7993477873055939440L;

    private String titulo;

    public JLabelButton(String title) {
        titulo = title;
        this.setText(title);
        this.setForeground(new Color(0, 102, 204));
        this.setCursor(new Cursor(Cursor.HAND_CURSOR));
        this.addMouseListener(new MouseAdapter() {

            public void mouseClicked(MouseEvent e) {
            }
        });
        this.addMouseListener(new MouseAdapter() {

            public void mouseEntered(MouseEvent e) {
                setForegroundMouseEntered();
            }
        });
        this.addMouseListener(new MouseAdapter() {

            public void mouseExited(MouseEvent e) {
                setForegroundMouseExited();
            }
        });
    }

    public void setForegroundMouseEntered() {
        this.setForeground(new Color(178, 33, 136));
    }

    public void setForegroundMouseExited() {
        this.setForeground(new Color(0, 102, 204));
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }
}
