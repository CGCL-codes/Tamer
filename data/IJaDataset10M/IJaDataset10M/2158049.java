package ac.hiu.j314.elmve.comp;

import ac.hiu.j314.elmve.ui.*;
import java.io.*;
import java.awt.*;
import javax.swing.*;
import javax.swing.border.*;
import java.util.*;

public class EContainer2DUI extends Elm2DUIBase {

    int w = 300;

    int h = 300;

    public void init(Serializable data) {
        setLayout(new ELayout());
        Iterator i = ((ArrayList) data).iterator();
        double width = ((Double) i.next()).doubleValue();
        double height = ((Double) i.next()).doubleValue();
        w = (int) (width * ppm);
        h = (int) (height * ppm);
        while (i.hasNext()) {
            Elm2DData d = (Elm2DData) i.next();
            Elm2DUIInterface ui = d.makeUI();
            ui.setElm(d.elm);
            ui.setClient(client);
            ui.init(d.data);
            add((JComponent) ui);
        }
        setBorder(new TitledBorder(elm.getName()));
    }

    public void update(Serializable data) {
        Iterator i = ((ArrayList) data).iterator();
        double width = ((Double) i.next()).doubleValue();
        double height = ((Double) i.next()).doubleValue();
        w = (int) (width * ppm);
        h = (int) (height * ppm);
    }

    public Dimension getPreferredSize() {
        return new Dimension(w, h);
    }
}
