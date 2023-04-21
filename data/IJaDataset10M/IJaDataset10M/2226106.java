package fr.ircf.echecs.metier;

/**
 * Classe MCheval
 * R�presente un Cheval de jeu d'�checs m�tier
 */
public class MCheval extends MPiece {

    /**
	 * Constructeur
	 */
    public MCheval(MPlateau mPlateau, MJoueur mJoueur, boolean couleur, int x, int y) {
        super(mPlateau, mJoueur, couleur, x, y);
        strImage = "images/pieces/cheval";
        strPiece = "C";
    }

    /**
	 * M�thode calculer(), permet de mettre � jour le vecteur mcasesAccessibles
	 * qui repr�sente les cases sur lesquelles la pi�ce peut se rendre, d�pend du type de pi�ce
	 */
    public void calculer() {
        MCase mCaseTest = null;
        mCasesAccessibles.removeAllElements();
        int[] d = { -1, -2, -1, +2, +1, -2, +1, +2, -2, -1, -2, +1, +2, -1, +2, +1 };
        for (int i = 0; i < 16; i += 2) {
            mCaseTest = getMCase().testerCase(d[i], d[i + 1]);
            if (mCaseTest != null && (mCaseTest.getMPiece() == null || mCaseTest.getMPiece().getCouleur() == !getCouleur())) mCasesAccessibles.add(mCaseTest);
        }
    }
}
