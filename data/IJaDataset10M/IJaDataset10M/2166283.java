package pl.vgtworld.games.statki.components;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.net.URL;
import javax.imageio.ImageIO;
import javax.swing.AbstractAction;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.Timer;

/**
 * Komponent wyswietlajacy informacje o trafieniach i zatopieniach statkow poszczegolnych graczy.
 * 
 * @author VGT
 * @version 1.0
 */
public class JComponentWydarzenia extends JComponent {

    /**
	 * Minimalna i preferowana szerokosc komponentu.
	 */
    private static final int SZEROKOSC = 40;

    /**
	 * Minimalna i preferowana wysokosc komponentu.
	 */
    private static final int WYSOKOSC = 40;

    /**
	 * Wielkosc czcionki wyswietlanych komunikatow.
	 */
    private static final int FONT_WIELKOSC = 30;

    /**
	 * Kolor tla komponentu.
	 */
    private Color oColorBackground;

    /**
	 * Kolor czcionki komunikatow komponentu.
	 */
    private Color oColorFont;

    /**
	 * Obiekt czcionki komunikatow.
	 */
    private Font oCzcionka;

    /**
	 * Obiekt komunikatow gracza lewego.
	 */
    private JLabel oGraczLewy;

    /**
	 * Obiekt komunikatow gracza prawego.
	 */
    private JLabel oGraczPrawy;

    /**
	 * Timer ukrywajacy komunikat gracza lewego.
	 */
    private Timer oTimerLewy;

    /**
	 * Timer ukrywajacy komunikat gracza prawego.
	 */
    private Timer oTimerPrawy;

    /**
	 * Tlo graficzne komponentu.
	 */
    private Image oImgTlo;

    /**
	 * Klasa prywatna obslugujaca akcje ukrywania komunikatu dla lewego gracza.
	 */
    private class ActionWyczyscLewy extends AbstractAction {

        public void actionPerformed(ActionEvent oEvent) {
            oGraczLewy.setText("");
            oGraczLewy.repaint();
        }
    }

    /**
	 * Klasa prywatna obslugujaca akcje ukrywania komunikatu dla prawego gracza.
	 */
    private class ActionWyczyscPrawy extends AbstractAction {

        public void actionPerformed(ActionEvent oEvent) {
            oGraczPrawy.setText("");
            oGraczPrawy.repaint();
        }
    }

    /**
	 * Konstruktor domyslny.
	 */
    public JComponentWydarzenia() {
        URL oImgUrl = getClass().getResource("/pl/vgtworld/games/statki/img/events-bg.png");
        if (oImgUrl != null) {
            try {
                oImgTlo = ImageIO.read(oImgUrl);
            } catch (IOException e) {
                oImgTlo = null;
            }
        } else oImgTlo = null;
        oColorBackground = new Color(0, 0, 0);
        oColorFont = new Color(255, 255, 255);
        oCzcionka = new Font("Serif", Font.BOLD, FONT_WIELKOSC);
        oGraczLewy = new JLabel("", JLabel.CENTER);
        oGraczLewy.setFont(oCzcionka);
        oGraczLewy.setVerticalAlignment(JLabel.CENTER);
        oGraczLewy.setForeground(oColorFont);
        JPanel oGraczLewyContainer = new JPanel();
        oGraczLewyContainer.setOpaque(false);
        oGraczLewyContainer.add(oGraczLewy);
        oGraczPrawy = new JLabel("", JLabel.CENTER);
        oGraczPrawy.setVerticalAlignment(JLabel.CENTER);
        oGraczPrawy.setFont(oCzcionka);
        oGraczPrawy.setForeground(oColorFont);
        JPanel oGraczPrawyContainer = new JPanel();
        oGraczPrawyContainer.setOpaque(false);
        oGraczPrawyContainer.add(oGraczPrawy);
        oTimerLewy = new Timer(1000, new ActionWyczyscLewy());
        oTimerLewy.setRepeats(false);
        oTimerPrawy = new Timer(1000, new ActionWyczyscPrawy());
        oTimerPrawy.setRepeats(false);
        setMinimumSize(new Dimension(SZEROKOSC, WYSOKOSC));
        setPreferredSize(new Dimension(SZEROKOSC, WYSOKOSC));
        setLayout(new GridLayout());
        add(oGraczLewyContainer);
        add(oGraczPrawyContainer);
    }

    /**
	 * Metoda wyswietla komunikat dla lewego gracza i wywoluje timer ukrywajacy komunikat po jednej sekundzie.
	 * 
	 * @param sTekst Tresc wyswietlanego komunikatu.
	 */
    public void ustawLewyKomunikat(String sTekst) {
        oGraczLewy.setText(sTekst);
        oTimerLewy.start();
    }

    /**
	 * Metoda wyswietla komunikat dla prawego gracza i wywoluje timer ukrywajacy komunikat po jednej sekundzie.
	 * 
	 * @param sTekst Tresc wyswietlanego komunikatu.
	 */
    public void ustawPrawyKomunikat(String sTekst) {
        oGraczPrawy.setText(sTekst);
        oTimerPrawy.start();
    }

    /**
	 * Przeciazona metoda rysujaca komponent.
	 */
    @Override
    public void paintComponent(Graphics g) {
        g.setColor(oColorBackground);
        g.fillRect(0, 0, getWidth(), getHeight());
        if (oImgTlo != null) {
            int iStartX = 0;
            while (iStartX < getWidth()) {
                g.drawImage(oImgTlo, iStartX, 0, null);
                iStartX += oImgTlo.getWidth(null);
            }
        }
    }
}
