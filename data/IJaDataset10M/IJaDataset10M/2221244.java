package nl.hupa.besturing;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import nl.hupa.hulpmiddelen.MeeTeGevenObject;
import org.apache.log4j.Logger;

public class ToetsenAfhandelaar implements KeyListener {

    Logger log = Logger.getLogger("ToetsenAfhandelaar.class");

    public char toetsWaarde;

    public boolean isVooruit;

    public boolean isAchteruit;

    public boolean isLinks;

    public boolean isRechts;

    public boolean isEinde;

    public boolean isPaginaOmhoog;

    public boolean isPaginaOmlaag;

    public boolean schakelaarLijnMode;

    public boolean schakelaarRotate;

    public boolean toetsAktie = true;

    public ToetsenAfhandelaar() {
    }

    /**
     * Verplichte methode
     */
    public void keyPressed(KeyEvent toets) {
        toetsAktie = true;
        switch(toets.getKeyCode()) {
            case KeyEvent.VK_ESCAPE:
                isEinde = true;
                System.exit(0);
                break;
            case KeyEvent.VK_LEFT:
                isLinks = true;
                break;
            case KeyEvent.VK_RIGHT:
                isRechts = true;
                break;
            case KeyEvent.VK_UP:
                isVooruit = true;
                break;
            case KeyEvent.VK_DOWN:
                isAchteruit = true;
                break;
            case KeyEvent.VK_PAGE_DOWN:
                isPaginaOmlaag = true;
                break;
            case KeyEvent.VK_PAGE_UP:
                isPaginaOmhoog = true;
                break;
            default:
                toetsWaarde = toets.getKeyChar();
                overigeToetsen();
                break;
        }
    }

    public void keyReleased(KeyEvent toetslos) {
        toetsUit(toetslos.getKeyChar());
    }

    public void keyTyped(KeyEvent toets) {
        toetsWaarde = toets.getKeyChar();
    }

    /**
     * Zet de volgende functies uit indien
     * de toets niet meer is ingedrukt
     */
    private void toetsUit(char toetsWaardeLos) {
        isVooruit = false;
        isAchteruit = false;
        isLinks = false;
        isRechts = false;
        isEinde = false;
        isPaginaOmhoog = false;
        isPaginaOmlaag = false;
        log.debug("Toets los : " + toetsWaardeLos);
        log.debug("ToetsWaarde : " + toetsWaarde);
        if (toetsWaarde == toetsWaardeLos) {
            toetsWaarde = '\0';
        }
    }

    private void omschakelenLijnMode() {
        log.debug(" Omschakelijn : " + schakelaarLijnMode);
        if (schakelaarLijnMode) schakelaarLijnMode = false; else schakelaarLijnMode = true;
    }

    private void overigeToetsen() {
        switch(toetsWaarde) {
            case 'l':
                omschakelenLijnMode();
                break;
            case 'a':
                break;
            default:
                log.debug("Niet afgehandelde toets ingedrukt");
                break;
        }
    }
}
