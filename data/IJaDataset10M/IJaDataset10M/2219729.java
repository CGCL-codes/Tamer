package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.LineBorder;
import tabellone.*;

/**
 * Classe deputata al disegno di un JPanel simile alle caselle del  
 * tabellone del Monopoli originale.
 * @author   Delfino & Di Marco
 */
@SuppressWarnings("serial")
public class DrawCasella extends JPanel {

    private GroupColors colori;

    private Tabellone gameTable;

    /**
	 * Disegna un pannello contenente la casella  
	 * del tabellone. 
	 * @param pos Posizione della casella
	 * @param tavolo Tabellone del gioco. 
	 */
    public DrawCasella(int pos, Tabellone tavolo) {
        super();
        gameTable = tavolo;
        colori = new GroupColors(gameTable);
        Casella slot = gameTable.getCasellaPos(pos);
        if (slot instanceof Terreno) drawTerreno(slot); else if (slot instanceof Servizi) drawServizi(slot); else if (slot instanceof Speciali) drawSpeciali(slot);
    }

    /**
	 * Disegna un pannello contenente la casella di tipo {@link Speciali}. 
	 * @param slot Casella di tipo {@link Speciali}
	 */
    private void drawSpeciali(Casella slot) {
        setLayout(new GridLayout(3, 1));
        JPanel panel_top = new JPanel(new GridLayout(1, 1));
        JPanel panel_mid = new JPanel(new GridLayout(1, 1));
        JPanel panel_dwn = new JPanel(new GridLayout(1, 1));
        panel_top.setBackground(colori.getGrpColor(0));
        panel_mid.setBackground(colori.getGrpColor(0));
        panel_dwn.setBackground(colori.getGrpColor(0));
        JLabel label_top, label_mid, label_dwn;
        add(panel_top);
        add(panel_mid);
        if (slot instanceof Prob_Imp) {
            setLayout(new BorderLayout());
            label_top = new JLabel(slot.getNome());
            java.net.URL imgURL;
            if (slot.getNome().contains("Imprevisti")) imgURL = DrawCasella.class.getResource("images/imprevisti.gif"); else imgURL = DrawCasella.class.getResource("images/communitychest.gif");
            ImageIcon icon = new ImageIcon(imgURL);
            label_mid = new JLabel(icon);
            label_dwn = new JLabel("");
            panel_top.add(label_top);
            panel_mid.add(label_mid);
            add(BorderLayout.NORTH, panel_top);
            add(BorderLayout.CENTER, panel_mid);
        } else if (slot instanceof Tasse_Crediti) {
            label_top = new JLabel(slot.getNome());
            java.net.URL imgURL;
            add(panel_dwn);
            if (slot.getNome().toLowerCase().contains("via")) imgURL = DrawCasella.class.getResource("images/start.gif"); else if (slot.getNome().toLowerCase().contains("lusso")) imgURL = DrawCasella.class.getResource("images/lusso.gif"); else if (slot.getNome().toLowerCase().contains("gratuito")) {
                imgURL = DrawCasella.class.getResource("images/parking.gif");
                setLayout(new BorderLayout());
                add(BorderLayout.NORTH, panel_top);
                add(BorderLayout.CENTER, panel_mid);
            } else imgURL = null;
            try {
                ImageIcon icon = new ImageIcon(imgURL);
                label_mid = new JLabel(icon);
            } catch (Exception Ex) {
                label_mid = new JLabel();
            }
            int valore = ((Tasse_Crediti) slot).getCredito();
            if (valore > 0) label_dwn = new JLabel("<html><p align=center>Ritirate al passaggio<br>�" + valore + "</p></html>"); else if (valore < 0) {
                valore = -valore;
                label_dwn = new JLabel("<html><p align=center>Pagare<br>�" + valore + "</p></html>");
            } else label_dwn = new JLabel("");
            panel_top.add(label_top);
            panel_mid.add(label_mid);
            panel_dwn.add(label_dwn);
        } else if (slot instanceof Prigione) {
            if (((Prigione) slot).isVaiPrigione()) {
                setLayout(new BorderLayout());
                label_top = new JLabel("In Prigione!");
                java.net.URL imgURL;
                imgURL = DrawCasella.class.getResource("images/carabiniere.gif");
                ImageIcon icon = new ImageIcon(imgURL);
                label_mid = new JLabel(icon);
                label_dwn = new JLabel("");
                add(BorderLayout.NORTH, panel_top);
                add(BorderLayout.CENTER, panel_mid);
            } else {
                label_top = new JLabel("Prigione");
                java.net.URL imgURL;
                imgURL = DrawCasella.class.getResource("images/prigione.gif");
                ImageIcon icon = new ImageIcon(imgURL);
                label_mid = new JLabel(icon);
                label_dwn = new JLabel("Transito");
                panel_top.setBackground(Color.red);
                panel_mid.setBackground(Color.red);
                panel_dwn.setBorder(new LineBorder(Color.black, 1));
                add(panel_dwn);
            }
            panel_top.add(label_top);
            panel_mid.add(label_mid);
            panel_dwn.add(label_dwn);
        } else {
            label_top = new JLabel("ERRORE");
            label_mid = new JLabel("ERRORE");
            label_dwn = new JLabel("ERRORE");
            panel_top.add(label_top);
            panel_mid.add(label_mid);
            panel_dwn.add(label_dwn);
        }
        setBorder(new LineBorder(Color.black, 2));
        label_top.setHorizontalAlignment(SwingConstants.CENTER);
        label_mid.setHorizontalAlignment(SwingConstants.CENTER);
        label_dwn.setHorizontalAlignment(SwingConstants.CENTER);
    }

    /**
	 * Disegna un pannello contenente la casella di tipo {@link Servizi}. 
	 * @param slot Casella di tipo {@link Servizi}
	 */
    private void drawServizi(Casella slot) {
        setLayout(new GridLayout(3, 1));
        JPanel panel_top = new JPanel(new GridLayout(1, 1));
        JPanel panel_mid = new JPanel(new GridLayout(1, 1));
        JPanel panel_dwn = new JPanel(new GridLayout(1, 1));
        panel_top.setBackground(colori.getGrpColor(0));
        panel_mid.setBackground(colori.getGrpColor(0));
        panel_dwn.setBackground(colori.getGrpColor(0));
        JLabel label_top, label_mid, label_dwn;
        label_top = new JLabel("<html><p align=center>" + slot.getNome() + "<br>" + slot.getValore() + "�</p></html>");
        java.net.URL imgURL;
        if (slot instanceof Stazione) imgURL = DrawCasella.class.getResource("images/stazione_small.gif"); else if (slot.getNome().contains("Elettrica")) imgURL = DrawCasella.class.getResource("images/luce.gif"); else imgURL = DrawCasella.class.getResource("images/acqua.gif");
        ImageIcon icon = new ImageIcon(imgURL);
        label_mid = new JLabel(icon);
        if (slot.getProprietario() == 0) label_dwn = new JLabel("Propriet� della banca"); else label_dwn = new JLabel("Propriet� di " + gameTable.getGiocatore(slot.getProprietario()).getNome());
        setBorder(new LineBorder(Color.black, 2));
        panel_top.add(label_top);
        panel_mid.add(label_mid);
        panel_dwn.add(label_dwn);
        label_top.setHorizontalAlignment(SwingConstants.CENTER);
        label_mid.setHorizontalAlignment(SwingConstants.CENTER);
        label_dwn.setHorizontalAlignment(SwingConstants.CENTER);
        add(panel_top);
        add(panel_mid);
        add(panel_dwn);
    }

    /**
	 * Disegna un pannello contenente la casella di tipo {@link Terreno}. 
	 * @param slot Casella di tipo {@link Terreno}
	 */
    private void drawTerreno(Casella slot) {
        setLayout(new GridLayout(3, 1));
        JPanel panel_top = new JPanel(new GridLayout(1, 1));
        JPanel panel_mid = new JPanel(new GridLayout(1, 1));
        JPanel panel_dwn = new JPanel(new GridLayout(1, 1));
        panel_top.setBackground(colori.getGrpColor(0));
        panel_mid.setBackground(colori.getGrpColor(0));
        panel_dwn.setBackground(colori.getGrpColor(0));
        JLabel label_mid, label_dwn;
        JPanel panel_case = new JPanel(new GridLayout(1, 5));
        ;
        int n_case = ((Terreno) slot).getCase();
        switch(n_case) {
            case 1:
                panel_case.add(new JPanel());
                panel_case.add(new JPanel());
                panel_case.add(new DrawHouse());
                panel_case.add(new JPanel());
                panel_case.add(new JPanel());
                break;
            case 2:
                panel_case.add(new JPanel());
                panel_case.add(new DrawHouse());
                panel_case.add(new JPanel());
                panel_case.add(new DrawHouse());
                panel_case.add(new JPanel());
                break;
            case 3:
                panel_case = new JPanel(new GridLayout(1, 6));
                panel_case.add(new JPanel());
                panel_case.add(new DrawHouse());
                panel_case.add(new DrawHouse());
                panel_case.add(new DrawHouse());
                panel_case.add(new JPanel());
                break;
            case 4:
                panel_case.add(new DrawHouse());
                panel_case.add(new DrawHouse());
                panel_case.add(new JPanel());
                panel_case.add(new DrawHouse());
                panel_case.add(new DrawHouse());
                break;
            case 5:
                panel_case = new JPanel(new GridLayout(1, 3));
                panel_case.add(new JPanel());
                panel_case.add(new DrawHouse(true));
                panel_case.add(new JPanel());
                break;
            default:
                panel_case = new JPanel();
                panel_case.add(new JPanel());
        }
        panel_case.setBackground(colori.getGrpColor(slot.getPosizione()));
        panel_case.setBorder(new LineBorder(new java.awt.Color(0, 0, 0), 1));
        for (int i = 0; i < panel_case.getComponentCount(); i++) panel_case.getComponent(i).setBackground(colori.getGrpColor(slot.getPosizione()));
        label_mid = new JLabel("<html><p align=center><b>" + slot.getNome() + "</b><br>" + slot.getValore() + "�</html>");
        if (slot.getProprietario() == 0) label_dwn = new JLabel("Propriet� della banca"); else label_dwn = new JLabel("Propriet� di " + gameTable.getGiocatore(slot.getProprietario()).getNome());
        setBorder(new LineBorder(Color.black, 2));
        panel_top.add(panel_case);
        panel_mid.add(label_mid);
        panel_dwn.add(label_dwn);
        label_mid.setHorizontalAlignment(SwingConstants.CENTER);
        label_dwn.setHorizontalAlignment(SwingConstants.CENTER);
        add(panel_top);
        add(panel_mid);
        add(panel_dwn);
    }
}
