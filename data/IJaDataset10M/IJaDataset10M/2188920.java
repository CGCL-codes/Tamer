package xGraphique.xFenetrage.Command;

import xGraphique.xCarte.XCarte;
import xGraphique.xFenetrage.XFenetrePrincipale;

public class CommandeExecAffichageElementsSimplifie extends CommandeExec {

    @Override
    public void execute() {
        XCarte c = XFenetrePrincipale.getInstance().carte;
        c.affichageReduit = true;
        XFenetrePrincipale.getInstance().conteneurCarte.gatc.dessin.repaintAll();
    }
}
