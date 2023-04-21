package columnasMenu;

import java.util.*;
import javax.microedition.lcdui.*;
import com.nokia.mid.ui.*;

public class CTextChoice {

    private int seleccion;

    private Vector listaOpciones;

    private Font fuente;

    private Image imgFlecha = null;

    public CTextChoice() {
        seleccion = 0;
        listaOpciones = new Vector(5);
        fuente = Font.getFont(Font.FACE_SYSTEM, Font.STYLE_BOLD, Font.SIZE_MEDIUM);
        try {
            imgFlecha = Image.createImage("/res/flechaSeleccion.png");
        } catch (Exception e) {
            System.err.println("Error E/S: " + e.getMessage());
            e.printStackTrace();
            imgFlecha = null;
        }
    }

    public void Add(String s) {
        listaOpciones.addElement(s);
    }

    public void Next() {
        if (seleccion < listaOpciones.size() - 1) {
            seleccion++;
        }
    }

    public void Last() {
        if (seleccion > 0) {
            seleccion--;
        }
    }

    public String getSeleccion() {
        return (String) listaOpciones.elementAt(seleccion);
    }

    public void Draw(Graphics g, int x, int y) {
        g.setFont(fuente);
        int altoFuente = fuente.getHeight();
        if (imgFlecha != null) {
            int altoFlecha = imgFlecha.getHeight();
            int anchoFlecha = imgFlecha.getWidth();
            g.drawImage(imgFlecha, x, y + (seleccion * altoFuente), Graphics.HCENTER | Graphics.VCENTER);
            for (int i = 0; i < listaOpciones.size(); ++i) {
                g.drawString((String) listaOpciones.elementAt(i), x + anchoFlecha, y + (i * (altoFuente)) - (altoFuente / 2), Graphics.LEFT | Graphics.TOP);
            }
        } else {
            DirectGraphics dg = DirectUtils.getDirectGraphics(g);
            dg.fillTriangle(x, y + seleccion * altoFuente - 5, x + (altoFuente / 2) - 1, y + seleccion * altoFuente - 2, x, y + seleccion * altoFuente + 2, 0xFF000000 | g.getColor());
            for (int i = 0; i < listaOpciones.size(); ++i) {
                g.drawString((String) listaOpciones.elementAt(i), x + (altoFuente / 2) + 5, y + (i * altoFuente), Graphics.LEFT | Graphics.BASELINE);
            }
        }
    }
}
