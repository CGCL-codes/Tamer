package sfa.projetIHM.horloge.view;

import java.util.Observable;
import java.util.Observer;
import javax.swing.JComponent;
import javax.swing.JPanel;
import sfa.projetIHM.horloge.models.HorlogeM;

public class HorlogeV extends JComponent implements Observer {

    private int taille;

    private HorlogeM horloge;

    private JPanel parent;

    private String date = "";

    private Boolean visible = true;

    public HorlogeV(int t, HorlogeM m) {
        super();
        horloge = m;
        horloge.addObserver(this);
        taille = t;
        setSize(taille, taille);
    }

    public void setParent(JPanel p) {
        parent = p;
    }

    public JPanel getParent() {
        return parent;
    }

    public void setDate(String s) {
        date = s;
    }

    public String getDate() {
        return date;
    }

    public HorlogeM getModele() {
        return horloge;
    }

    public void setDixieme(Boolean b) {
        visible = b;
    }

    public Boolean getVisible() {
        return visible;
    }

    public void update(Observable obs, Object obj) {
        repaint();
    }
}
