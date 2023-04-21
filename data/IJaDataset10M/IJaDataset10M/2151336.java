package player;

import java.util.Vector;
import de.fhl.oop.tictactoe.engine.T3Konstanten;
import de.fhl.oop.tictactoe.engine.T3Routinen;
import de.fhl.oop.tictactoe.engine.T3Spiel;
import de.fhl.oop.tictactoe.player.T3FeldPos;
import de.fhl.oop.tictactoe.player.T3VersierterSpieler;

public class EliteJules extends T3VersierterSpieler {

    public EliteJules(String name) {
        super(name);
    }

    public int zugnummer(char[][] feld) {
        return T3Konstanten.BREITE * T3Konstanten.BREITE - this.leere_felder(feld).size() + 1;
    }

    @Override
    public void am_zug(char v, T3Spiel s) throws Exception {
        char[][] feld = s.lese_feld();
        Vector<T3FeldPos> gewinnfelder = this.gewinnfelder(v, feld);
        if (!gewinnfelder.isEmpty()) {
            T3FeldPos pos = gewinnfelder.firstElement();
            s.setze_auf_feld(this, v, pos.getX(), pos.getY());
            return;
        }
        char gegner = v == T3Konstanten.X ? T3Konstanten.O : T3Konstanten.X;
        gewinnfelder = this.gewinnfelder(gegner, feld);
        if (!gewinnfelder.isEmpty()) {
            T3FeldPos pos = (T3FeldPos) gewinnfelder.firstElement();
            s.setze_auf_feld(this, v, pos.getX(), pos.getY());
            return;
        }
        if (v == T3Konstanten.X) {
            if (this.zugnummer(feld) == 1) {
                s.setze_auf_feld(this, v, 1, 1);
                return;
            }
        } else {
            if (this.zugnummer(feld) == 2 && feld[1][1] == T3Konstanten.LEER) {
                s.setze_auf_feld(this, v, 1, 1);
                return;
            }
            if (this.zugnummer(feld) == 2) {
                s.setze_auf_feld(this, v, 2, 2);
                return;
            }
        }
        int max = 0;
        T3FeldPos bestpos = new T3FeldPos();
        for (T3FeldPos pos : this.leere_felder(feld)) {
            char[][] lookahead = T3Routinen.deepclone(feld);
            lookahead[pos.getX()][pos.getY()] = v;
            int chancen = this.gewinnfelder(v, lookahead).size();
            max = chancen >= max ? chancen : max;
            if (max == chancen) bestpos = pos;
        }
        if (max > 0) {
            s.setze_auf_feld(this, v, bestpos.getX(), bestpos.getY());
            return;
        }
        bestpos = (T3FeldPos) this.leere_felder(feld).firstElement();
        s.setze_auf_feld(this, v, bestpos.getX(), bestpos.getY());
    }
}
