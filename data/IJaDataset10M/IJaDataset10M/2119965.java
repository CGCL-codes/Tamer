package be.khleuven.kevinvranken;

import be.khleuven.kevinvranken.entiteiten.Korrel;
import be.khleuven.kevinvranken.entiteiten.Muur;
import be.khleuven.kevinvranken.entiteiten.Onbeweegbaar;
import be.khleuven.kevinvranken.entiteiten.PacMan;
import be.khleuven.kevinvranken.entiteiten.Personage;
import be.khleuven.kevinvranken.entiteiten.SuperKorrel;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.image.BufferStrategy;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JPanel;

/**
 *
 * @author kevin
 */
public class SpelPaneel extends JPanel {

    private boolean gepauseerd = false;

    private boolean links, rechts, boven, onder = false;

    private List<Personage> personages = new ArrayList<Personage>();

    private List<Onbeweegbaar> onbeweegbaarheden = new ArrayList<Onbeweegbaar>();

    private List<Personage> personagesTeVerwijderen = new ArrayList<Personage>();

    private List<Onbeweegbaar> onbeweegbaarhedenTeVerwijderen = new ArrayList<Onbeweegbaar>();

    private PacMan pacMan;

    private String laatsteRichting = "right";

    private long laatsteAnimatieUpdate;

    int animatieTeller = 3;

    public SpelPaneel() {
        setBackground(Color.BLACK);
        setPreferredSize(new Dimension(650, 500));
        setSize(new Dimension(650, 500));
        setBounds(0, 0, 650, 500);
        setIgnoreRepaint(true);
        setVisible(true);
        prepareerEntiteiten();
        addKeyListener(new invoerAfhandeling(this));
    }

    public void prepareerEntiteiten() {
        pacMan = new PacMan(this, "afbeeldingen/PacMan/PacMan3right.gif", 300, 300);
        personages.add(pacMan);
        onbeweegbaarheden.add(new Korrel(this, 200, 200));
        onbeweegbaarheden.add(new SuperKorrel(this, 10, 15));
        onbeweegbaarheden.add(new Muur(this, 10, 0));
        onbeweegbaarheden.add(new Muur(this, 30, 0));
        onbeweegbaarheden.add(new Muur(this, 280, 200));
        onbeweegbaarheden.add(new Muur(this, 280, 230));
        onbeweegbaarheden.add(new Muur(this, 350, 220));
    }

    public void spelLus() {
        requestFocus();
        long laatsteLus = System.currentTimeMillis();
        while (!gepauseerd) {
            allesVerwijderen();
            long delta = System.currentTimeMillis() - laatsteLus;
            laatsteLus = System.currentTimeMillis();
            Graphics2D g = (Graphics2D) getGraphics();
            g.setColor(Color.black);
            pacMan.setHorizontaleSnelheid(0);
            pacMan.setVerticaleSnelheid(0);
            if (isLinks() && !isRechts()) {
                pacMan.setHorizontaleSnelheid(-90);
            } else {
                if (isRechts() && !isLinks()) {
                    pacMan.setHorizontaleSnelheid(90);
                }
            }
            doeAnimatie(g);
            for (Onbeweegbaar onbeweegbaar : onbeweegbaarheden) {
                onbeweegbaar.teken(g);
            }
            for (Onbeweegbaar onbeweegbaar : onbeweegbaarheden) {
                if (onbeweegbaar.collidesWith(pacMan)) {
                    onbeweegbaar.collidedWith(pacMan);
                    if (onbeweegbaar instanceof Muur) pacMan.collidedWith(onbeweegbaar);
                }
            }
            for (Personage personage : personages) {
                if (isLinks() || isRechts() || isBoven() || isOnder()) {
                    personage.beweeg(delta);
                }
                personage.teken(g);
            }
            g.dispose();
            try {
                Thread.sleep(1);
            } catch (Exception e) {
            }
        }
    }

    public void verwijderPersonage(Personage personage) {
        personagesTeVerwijderen.add(personage);
    }

    public void verwijderKorrel(Korrel korrel) {
        onbeweegbaarhedenTeVerwijderen.add(korrel);
    }

    public void allesVerwijderen() {
        personages.removeAll(personagesTeVerwijderen);
        onbeweegbaarheden.removeAll(onbeweegbaarhedenTeVerwijderen);
        onbeweegbaarhedenTeVerwijderen.clear();
        personagesTeVerwijderen.clear();
    }

    public void doeAnimatie(Graphics2D g) {
        if (System.currentTimeMillis() - laatsteAnimatieUpdate >= 80) {
            String referentie = "afbeeldingen/PacMan/PacMan" + animatieTeller + laatsteRichting + ".gif";
            if (animatieTeller++ >= 4) {
                animatieTeller = 2;
            }
            laatsteAnimatieUpdate = System.currentTimeMillis();
            g.setColor(Color.black);
            g.fill(new Rectangle((int) pacMan.x, (int) pacMan.y - 2, 35, 35));
            pacMan.setSprite(referentie);
        }
    }

    public void setPacmanRichting(char richting) {
        links = rechts = boven = onder = false;
        switch(richting) {
            case 'l':
                links = true;
                laatsteRichting = "left";
                break;
            case 'r':
                rechts = true;
                laatsteRichting = "right";
                break;
            case 'b':
                boven = true;
                laatsteRichting = "up";
                break;
            case 'o':
                onder = true;
                laatsteRichting = "down";
                break;
        }
    }

    /**
     * @return the links
     */
    public boolean isLinks() {
        return links;
    }

    /**
     * @return the rechts
     */
    public boolean isRechts() {
        return rechts;
    }

    /**
     * @return the boven
     */
    public boolean isBoven() {
        return boven;
    }

    /**
     * @return the onder
     */
    public boolean isOnder() {
        return onder;
    }
}
