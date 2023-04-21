package org.jdmp.sigmen.client.carte;

import java.awt.event.MouseEvent;
import org.jdmp.sigmen.client.Main;
import org.jdmp.sigmen.messages.Constantes;
import org.jdmp.sigmen.messages.Constantes.Direction;

public abstract class OnCase implements OnMap {

    public static final int DEPLACEMENT = 1;

    public static final int CLIC = 2;

    protected Case position;

    protected int direction = Constantes.Direction.BAS;

    protected LocateBufferedImage img;

    protected int decalageX = 0;

    protected int decalageY = 0;

    public abstract void clicSouris(MouseEvent e);

    public void deplacementSouris(MouseEvent e) {
        getCarte().setCursor(getCursor());
    }

    public Carte getCarte() {
        return position.getCarte();
    }

    public Case getPosition() {
        return position;
    }

    public void effacer() {
        position.remove(this);
    }

    public void goTo(int direction) {
        final int dest = direction;
        new Thread() {

            @Override
            public void run() {
                move(dest);
            }
        }.start();
    }

    private void move(int direction) {
        int modifX = 0;
        int modifY = 0;
        this.direction = direction;
        int nbEtapes = Constantes.Affichage.DEPLACEMENT_CARTE_ETAPES;
        switch(direction) {
            case Direction.HAUT:
                modifY = -1;
                break;
            case Direction.BAS:
                modifY = 1;
                break;
            case Direction.GAUCHE:
                modifX = -1;
                break;
            case Direction.DROITE:
                modifX = 1;
                break;
            case Direction.HAUT_GAUCHE:
                modifX = -1;
                modifY = -1;
                nbEtapes = Constantes.Affichage.DEPLACEMENT_DIAGONALE_CARTE_ETAPES;
                break;
            case Direction.HAUT_DROITE:
                modifX = 1;
                modifY = -1;
                nbEtapes = Constantes.Affichage.DEPLACEMENT_DIAGONALE_CARTE_ETAPES;
                break;
            case Direction.BAS_GAUCHE:
                modifX = -1;
                modifY = 1;
                nbEtapes = Constantes.Affichage.DEPLACEMENT_DIAGONALE_CARTE_ETAPES;
                break;
            case Direction.BAS_DROITE:
                modifX = 1;
                modifY = 1;
                nbEtapes = Constantes.Affichage.DEPLACEMENT_DIAGONALE_CARTE_ETAPES;
                break;
        }
        try {
            for (int i = 0; i < nbEtapes; i++) {
                decalageX += modifX * 100 / nbEtapes;
                decalageY += modifY * 100 / nbEtapes;
                Thread.sleep(Constantes.Affichage.DEPLACEMENT_CARTE_DELAI);
            }
            Case newPosition = position.voisin(direction);
            int dest;
            if (direction == Direction.GAUCHE || direction == Direction.BAS || direction == Direction.BAS_GAUCHE || direction == Direction.BAS_DROITE) {
                dest = Case.DESSOUS;
            } else {
                dest = Case.DESSUS;
            }
            synchronized (getCarte()) {
                effacer();
                position = newPosition;
                try {
                    position.add(this, dest);
                    decalageX = 0;
                    decalageY = 0;
                } catch (NullPointerException e) {
                }
            }
        } catch (InterruptedException e) {
            Main.erreur("Déplacement de personnage interrompu.", e);
            decalageX = 0;
            decalageY = 0;
        }
    }

    public boolean isHere(int x, int y) {
        try {
            return (((img.getImg().getRGB(x * img.getImg().getWidth() / Carte.getCaseSize(), y * img.getImg().getHeight() / Carte.getCaseSize()) >> 24) & 0xFF) != 0x00);
        } catch (IndexOutOfBoundsException e) {
            return false;
        }
    }

    protected void passeDessous(int type, MouseEvent e) {
        OnMap next = getCarte().getNext(this, e.getX(), e.getY());
        switch(type) {
            case DEPLACEMENT:
                next.deplacementSouris(e);
                break;
            case CLIC:
                next.clicSouris(e);
                break;
        }
    }
}
