package sfa.projetIHM.horloge.view;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.util.Observable;
import java.util.Observer;
import javax.swing.JLabel;
import javax.swing.JPanel;
import sfa.projetIHM.horloge.models.HorlogeM;
import sfa.projetIHM.horloge.models.PanelCentralM;

public abstract class PanelCentralV extends JPanel implements Observer {

    private JPanel panelCentral;

    private HorlogeV horloge;

    protected String name;

    private boolean start = false;

    protected JPanel west;

    private JLabel text;

    private JLabel mode;

    protected PanelCentralM modele;

    public PanelCentralV(String n, PanelCentralM modele) {
        super();
        this.modele = modele;
        modele.addObserver(this);
        name = n;
        setLayout(new BorderLayout());
        panelCentral = new JPanel();
        panelCentral.setLayout(new BorderLayout());
        JPanel bande = new JPanel();
        bande.setLayout(new GridLayout(1, 2));
        mode = new JLabel("pas de mode");
        west = new JPanel();
        west.setLayout(new FlowLayout());
        JLabel l2 = new JLabel(n);
        text = new JLabel("");
        west.add(l2);
        west.add(text);
        bande.add(west);
        bande.add(mode);
        add(bande, BorderLayout.NORTH);
        add(panelCentral, BorderLayout.CENTER);
    }

    public void setMode(String s) {
        mode.setText(s);
    }

    public void setText(String s) {
        text.setText(s);
    }

    public void setHorloge(HorlogeV h) {
        horloge = h;
        panelCentral.add(h, BorderLayout.CENTER);
        horloge.setParent(panelCentral);
    }

    public HorlogeV getHorloge() {
        return horloge;
    }

    public HorlogeM getHorlogeModele() {
        return horloge.getModele();
    }

    public String getName() {
        return name;
    }

    public void update(Observable obs, Object obj) {
        horloge.update(obs, obj);
    }

    public PanelCentralM getModel() {
        return modele;
    }

    public JLabel getMode() {
        return mode;
    }

    public boolean isStart() {
        return start;
    }

    public void setStart(boolean start) {
        this.start = start;
    }
}
