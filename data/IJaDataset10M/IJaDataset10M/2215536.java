package com.googlecode.lemyriapode.prgechecs.univers;

import com.googlecode.lemyriapode.prgechecs.univers.Domaine.Obstacle.TypeObstacle;
import static com.googlecode.lemyriapode.prgechecs.univers.Domaine.*;

public class Collision {

    Avatar A;

    boolean bord;

    boolean obstacle;

    public Collision(Avatar A) {
        this.A = A;
        this.bord = false;
        this.obstacle = false;
    }

    public boolean testObstacle() {
        if (horsDomaine()) {
            return true;
        }
        return Obstacle();
    }

    private boolean horsDomaine() {
        return (A.position.X < minX) | (A.position.X > maxX) | (A.position.Y < minY) | (A.position.Y > maxY);
    }

    private boolean Obstacle() {
        TypeObstacle[][] liste = A.domaine.obstacle.getListe();
        TypeObstacle E;
        if (((E = liste[A.position.X][A.position.Y]) != null)) {
            if (E.estBlanc()) {
                return true;
            }
        }
        return false;
    }
}
