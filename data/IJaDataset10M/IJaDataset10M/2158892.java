package myAI;

import java.util.Vector;

public class Plateau implements Cloneable {

    public static final boolean PRINTLOG = true;

    public static final short VIDE = 0;

    public static final short BLANC = 1;

    public static final short NOIR = 2;

    public static final short ROI = 3;

    private static final short CASEINTERDITE = 5;

    private static final short CASETRONE = 6;

    private static final short CASESORTIE = 7;

    private short[][] mPlateau;

    private short[][] mTypeCases;

    private short mTaille;

    private short mRoiCol;

    private short mRoiLig;

    private boolean mbFinal = false;

    private boolean mbRoiCapture = false;

    private boolean mbRoiEnfui = false;

    private short mNbBlanc = 0;

    private short mNbNoir = 0;

    private short mNbCoupsPermisRoi = 0;

    private short mNbPionsNoirAutourRoi = 0;

    private short mNbCoinsAutourRoi = 0;

    /**
	 * Getter
	 * Doit �tre utilis� apr�s un appel � CalculStatistiquesRoi()
	 * 0 < NbCoupsPermisRoi < 16
	 * @return
	 */
    public short getNbCoupsPermisRoi() {
        return this.mNbCoupsPermisRoi;
    }

    /**
	 * Getter
	 * Doit �tre utilis� apr�s un appel � CalculStatistiquesRoi()
	 * 0 < NbPionsNoirAutourRoi < 4
	 * @return
	 */
    public short getNbPionsNoirAutourRoi() {
        return this.mNbPionsNoirAutourRoi;
    }

    /**
	 * Getter
	 * Doit �tre utilis� apr�s un appel � CalculStatistiquesRoi()
	 * 0 < NbCoinsAutourRoi < 2
	 * @return
	 */
    public short getNbCoinsAutourRoi() {
        return this.mNbCoinsAutourRoi;
    }

    /**
	 * Getter
	 * @return
	 */
    public short getNbBlanc() {
        return this.mNbBlanc;
    }

    /**
	 * Getter
	 * @return
	 */
    public short getNbNoir() {
        return this.mNbNoir;
    }

    /**
	 * Getter
	 * @return
	 */
    public boolean estFinal() {
        return this.mbFinal;
    }

    /**
	 * Getter
	 * @return
	 */
    public boolean estRoiCapture() {
        return this.mbRoiCapture;
    }

    /**
	 * Getter
	 * @return
	 */
    public boolean estRoiEnfui() {
        return this.mbRoiEnfui;
    }

    /**
	 * NbBlanc - NbNoir
	 * 0 < NbBlanc < 9
	 * 0 < NbNoir < 16
	 * -16 < NbBlanc - NbNoir < 9
	 * @return
	 */
    public short getDiffPions() {
        return (short) (this.getNbBlanc() - this.getNbNoir());
    }

    /**
	 * On sauvegarde le plateau actuel car il va y avoir une prise dessus
	 * non utilis� � priori par soucis de performance
	 */
    private Plateau clonePlateau() {
        Plateau plateau = new Plateau(this.mTaille);
        try {
            plateau = (Plateau) this.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return plateau;
    }

    /**
	 * d�place une pi�ce sur le plateau sans ce soucier si ce mouvement est valide ou non
	 * @param coup
	 */
    public void bougePiece(Coup coup) {
        if (coup.departLigne == coup.arriveeLigne && coup.departColonne == coup.arriveeColonne) {
            return;
        }
        this.mPlateau[coup.arriveeLigne][coup.arriveeColonne] = this.mPlateau[coup.departLigne][coup.departColonne];
        this.mPlateau[coup.departLigne][coup.departColonne] = VIDE;
    }

    /**
	 * Joue un coup sur le plateau actuel, on suppose que le coup est valide
	 * d�termine dans cette m�thode si le plateau sera final, i.e. si l'un des deux joueurs ont gagn�
	 * @param coup
	 */
    public Plateau jouerCoup(Coup coup, boolean clonePlateauAvantPrise) {
        if ((coup.departLigne == 0) && (coup.departColonne == 0) && (coup.arriveeLigne == 0) && (coup.arriveeColonne == 0)) return null;
        Plateau nouveauPlateau = this;
        this.bougePiece(coup);
        if (coup.couleur == NOIR) {
            if (((mRoiLig - 1 >= 0 && (nouveauPlateau.mPlateau[mRoiLig - 1][mRoiCol] == NOIR || nouveauPlateau.mTypeCases[mRoiLig - 1][mRoiCol] == CASETRONE)) || mRoiLig - 1 < 0) && ((mRoiLig + 1 < nouveauPlateau.mTaille && (nouveauPlateau.mPlateau[mRoiLig + 1][mRoiCol] == NOIR || nouveauPlateau.mTypeCases[mRoiLig + 1][mRoiCol] == CASETRONE)) || mRoiLig + 1 >= nouveauPlateau.mTaille) && ((mRoiCol - 1 >= 0 && (nouveauPlateau.mPlateau[mRoiLig][mRoiCol - 1] == NOIR || nouveauPlateau.mTypeCases[mRoiLig][mRoiCol - 1] == CASETRONE)) || mRoiCol - 1 < 0) && ((mRoiCol + 1 < nouveauPlateau.mTaille && (nouveauPlateau.mPlateau[mRoiLig][mRoiCol + 1] == NOIR || nouveauPlateau.mTypeCases[mRoiLig][mRoiCol + 1] == CASETRONE)) || mRoiCol + 1 >= nouveauPlateau.mTaille)) {
                if (PRINTLOG) {
                    System.out.println("Le roi etait en " + mRoiLig + "," + mRoiCol + " et s'est fait capture.");
                }
                nouveauPlateau.mbFinal = true;
                nouveauPlateau.mbRoiCapture = true;
                nouveauPlateau.mNbBlanc--;
            }
        }
        if (nouveauPlateau.mPlateau[coup.arriveeLigne][coup.arriveeColonne] == ROI) {
            mRoiCol = coup.arriveeColonne;
            mRoiLig = coup.arriveeLigne;
            if (nouveauPlateau.mTypeCases[mRoiLig][mRoiCol] == CASESORTIE) {
                if (PRINTLOG) {
                    System.out.println("Le roi etait en " + mRoiLig + "," + mRoiCol + " et s'est enfuit.");
                }
                nouveauPlateau.mbFinal = true;
                nouveauPlateau.mbRoiEnfui = true;
            }
        }
        if (coup.couleur == BLANC) {
            if ((coup.arriveeLigne - 2 >= 0 && nouveauPlateau.mPlateau[coup.arriveeLigne - 1][coup.arriveeColonne] == NOIR) && (nouveauPlateau.mPlateau[coup.arriveeLigne - 2][coup.arriveeColonne] == BLANC || nouveauPlateau.mTypeCases[coup.arriveeLigne - 2][coup.arriveeColonne] == CASETRONE || nouveauPlateau.mPlateau[coup.arriveeLigne - 2][coup.arriveeColonne] == ROI)) {
                if (clonePlateauAvantPrise) {
                    nouveauPlateau = this.clonePlateau();
                    clonePlateauAvantPrise = false;
                }
                nouveauPlateau.mPlateau[coup.arriveeLigne - 1][coup.arriveeColonne] = VIDE;
                nouveauPlateau.mNbNoir--;
            }
            if ((coup.arriveeLigne + 2 < nouveauPlateau.mTaille && nouveauPlateau.mPlateau[coup.arriveeLigne + 1][coup.arriveeColonne] == NOIR) && (nouveauPlateau.mPlateau[coup.arriveeLigne + 2][coup.arriveeColonne] == BLANC || nouveauPlateau.mTypeCases[coup.arriveeLigne + 2][coup.arriveeColonne] == CASETRONE || nouveauPlateau.mPlateau[coup.arriveeLigne + 2][coup.arriveeColonne] == ROI)) {
                if (clonePlateauAvantPrise) {
                    nouveauPlateau = this.clonePlateau();
                    clonePlateauAvantPrise = false;
                }
                nouveauPlateau.mPlateau[coup.arriveeLigne + 1][coup.arriveeColonne] = VIDE;
                nouveauPlateau.mNbNoir--;
            }
            if ((coup.arriveeColonne - 2 >= 0 && nouveauPlateau.mPlateau[coup.arriveeLigne][coup.arriveeColonne - 1] == NOIR) && (nouveauPlateau.mPlateau[coup.arriveeLigne][coup.arriveeColonne - 2] == BLANC || nouveauPlateau.mTypeCases[coup.arriveeLigne][coup.arriveeColonne - 2] == CASETRONE || nouveauPlateau.mPlateau[coup.arriveeLigne][coup.arriveeColonne - 2] == ROI)) {
                if (clonePlateauAvantPrise) {
                    nouveauPlateau = this.clonePlateau();
                    clonePlateauAvantPrise = false;
                }
                nouveauPlateau.mPlateau[coup.arriveeLigne][coup.arriveeColonne - 1] = VIDE;
                nouveauPlateau.mNbNoir--;
            }
            if ((coup.arriveeColonne + 2 < nouveauPlateau.mTaille && nouveauPlateau.mPlateau[coup.arriveeLigne][coup.arriveeColonne + 1] == NOIR) && (nouveauPlateau.mPlateau[coup.arriveeLigne][coup.arriveeColonne + 2] == BLANC || nouveauPlateau.mTypeCases[coup.arriveeLigne][coup.arriveeColonne + 2] == CASETRONE || nouveauPlateau.mPlateau[coup.arriveeLigne][coup.arriveeColonne + 2] == ROI)) {
                if (clonePlateauAvantPrise) {
                    nouveauPlateau = this.clonePlateau();
                    clonePlateauAvantPrise = false;
                }
                nouveauPlateau.mPlateau[coup.arriveeLigne][coup.arriveeColonne + 1] = VIDE;
                nouveauPlateau.mNbNoir--;
            }
        } else {
            if ((coup.arriveeLigne - 2 >= 0 && nouveauPlateau.mPlateau[coup.arriveeLigne - 1][coup.arriveeColonne] == BLANC) && (nouveauPlateau.mPlateau[coup.arriveeLigne - 2][coup.arriveeColonne] == NOIR || nouveauPlateau.mTypeCases[coup.arriveeLigne - 2][coup.arriveeColonne] == CASETRONE)) {
                if (clonePlateauAvantPrise) {
                    nouveauPlateau = this.clonePlateau();
                    clonePlateauAvantPrise = false;
                }
                nouveauPlateau.mPlateau[coup.arriveeLigne - 1][coup.arriveeColonne] = VIDE;
                nouveauPlateau.mNbBlanc--;
            }
            if ((coup.arriveeLigne + 2 < nouveauPlateau.mTaille && nouveauPlateau.mPlateau[coup.arriveeLigne + 1][coup.arriveeColonne] == BLANC) && (nouveauPlateau.mPlateau[coup.arriveeLigne + 2][coup.arriveeColonne] == NOIR || nouveauPlateau.mTypeCases[coup.arriveeLigne + 2][coup.arriveeColonne] == CASETRONE)) {
                if (clonePlateauAvantPrise) {
                    nouveauPlateau = this.clonePlateau();
                    clonePlateauAvantPrise = false;
                }
                nouveauPlateau.mPlateau[coup.arriveeLigne + 1][coup.arriveeColonne] = VIDE;
                nouveauPlateau.mNbBlanc--;
            }
            if ((coup.arriveeColonne - 2 >= 0 && nouveauPlateau.mPlateau[coup.arriveeLigne][coup.arriveeColonne - 1] == BLANC) && (nouveauPlateau.mPlateau[coup.arriveeLigne][coup.arriveeColonne - 2] == NOIR || nouveauPlateau.mTypeCases[coup.arriveeLigne][coup.arriveeColonne - 2] == CASETRONE)) {
                if (clonePlateauAvantPrise) {
                    nouveauPlateau = this.clonePlateau();
                    clonePlateauAvantPrise = false;
                }
                nouveauPlateau.mPlateau[coup.arriveeLigne][coup.arriveeColonne - 1] = VIDE;
                nouveauPlateau.mNbBlanc--;
            }
            if ((coup.arriveeColonne + 2 < nouveauPlateau.mTaille && nouveauPlateau.mPlateau[coup.arriveeLigne][coup.arriveeColonne + 1] == BLANC) && (nouveauPlateau.mPlateau[coup.arriveeLigne][coup.arriveeColonne + 2] == NOIR || nouveauPlateau.mTypeCases[coup.arriveeLigne][coup.arriveeColonne + 2] == CASETRONE)) {
                if (clonePlateauAvantPrise) {
                    nouveauPlateau = this.clonePlateau();
                    clonePlateauAvantPrise = false;
                }
                nouveauPlateau.mPlateau[coup.arriveeLigne][coup.arriveeColonne + 1] = VIDE;
                nouveauPlateau.mNbBlanc--;
            }
        }
        return nouveauPlateau;
    }

    /**
	 * D�fait un coup : joue l'inverse d'un coup et r�initialise les flags du plateau
	 * @param coup
	 */
    public void defaireCoup(Coup coup) {
        this.bougePiece(coup.coupInverse());
        this.mbFinal = false;
        this.mbRoiCapture = false;
        this.mbRoiEnfui = false;
    }

    /**
	 * Indique si une case est occup�e
	 * Les coordonn�es doivent �tre valide
	 * @param x
	 * @param y
	 * @return
	 */
    public boolean estOccupee(short x, short y) {
        return (this.mPlateau[x][y] != VIDE);
    }

    public short getCouleur(short x, short y) {
        return this.mPlateau[x][y];
    }

    /**
	 * Indique si une case n'est pas autoris�e pour un d�placement
	 * @param x
	 * @param y
	 * @return
	 */
    public boolean estInterdite(short x, short y) {
        return (this.mTypeCases[x][y] == CASEINTERDITE || this.mTypeCases[x][y] == CASETRONE);
    }

    /**
	 * Calcul quelques statistiques sur le roi et ces entourages
	 *
	 */
    public void calculStatistiquesRoi() {
        this.mNbPionsNoirAutourRoi = 0;
        this.mNbCoinsAutourRoi = 0;
        this.mNbCoupsPermisRoi = 0;
        short iMin = (short) (this.mRoiLig + 1);
        for (short i = iMin; i < this.mTaille; i++) {
            if (this.estOccupee(i, this.mRoiCol)) {
                if (i == iMin && this.getCouleur(i, this.mRoiCol) == Plateau.NOIR) {
                    this.mNbPionsNoirAutourRoi++;
                }
                break;
            }
            if (this.estInterdite(i, this.mRoiCol)) {
                if (i == iMin) {
                    this.mNbCoinsAutourRoi++;
                }
                continue;
            }
            this.mNbCoupsPermisRoi++;
        }
        iMin = (short) (this.mRoiLig - 1);
        for (short i = iMin; i >= 0; i--) {
            if (this.estOccupee(i, this.mRoiCol)) {
                if (i == iMin && this.getCouleur(i, this.mRoiCol) == Plateau.NOIR) {
                    this.mNbPionsNoirAutourRoi++;
                }
                break;
            }
            if (this.estInterdite(i, this.mRoiCol)) {
                if (i == iMin) {
                    this.mNbCoinsAutourRoi++;
                }
                continue;
            }
            this.mNbCoupsPermisRoi++;
        }
        iMin = (short) (this.mRoiCol + 1);
        for (short i = iMin; i < this.mTaille; i++) {
            if (this.estOccupee(this.mRoiLig, i)) {
                if (i == iMin && this.getCouleur(this.mRoiLig, i) == Plateau.NOIR) {
                    this.mNbPionsNoirAutourRoi++;
                }
                break;
            }
            if (this.estInterdite(this.mRoiLig, i)) {
                if (i == iMin) {
                    this.mNbCoinsAutourRoi++;
                }
                continue;
            }
            this.mNbCoupsPermisRoi++;
        }
        iMin = (short) (this.mRoiCol - 1);
        for (short i = iMin; i >= 0; i--) {
            if (this.estOccupee(this.mRoiLig, i)) {
                if (i == iMin && this.getCouleur(this.mRoiLig, i) == Plateau.NOIR) {
                    this.mNbPionsNoirAutourRoi++;
                }
                break;
            }
            if (this.estInterdite(this.mRoiLig, i)) {
                if (i == iMin) {
                    this.mNbCoinsAutourRoi++;
                }
                continue;
            }
            this.mNbCoupsPermisRoi++;
        }
    }

    public String Statistiques() {
        return "diff pions (blancs - noirs) 	: " + this.getDiffPions() + "\n" + "Nb pions noir autour du roi 	: " + this.mNbPionsNoirAutourRoi + "\n" + "Nb pions coins autour du roi 	: " + this.mNbCoinsAutourRoi + "\n" + "Nb coups permis pour du roi 	: " + this.mNbCoupsPermisRoi + "\n";
    }

    /**
	 * indique les coups permis pour une pi�ce au coordonn�e (x,y)
	 * Il faut passer un vector de coups non null
	 * @param x
	 * @param y
	 * @param coups
	 */
    public void coupsPermis(short x, short y, Vector<Coup> coups) {
        short couleur = this.getCouleur(x, y);
        for (short i = (short) (x + 1); i < this.mTaille; i++) {
            if (this.estOccupee(i, y)) {
                break;
            }
            if (this.estInterdite(i, y)) {
                continue;
            }
            coups.add(new Coup(x, y, i, y, couleur));
        }
        for (short i = (short) (x - 1); i >= 0; i--) {
            if (this.estOccupee(i, y)) {
                break;
            }
            if (this.estInterdite(i, y)) {
                continue;
            }
            coups.add(new Coup(x, y, i, y, couleur));
        }
        for (short i = (short) (y + 1); i < this.mTaille; i++) {
            if (this.estOccupee(x, i)) {
                break;
            }
            if (this.estInterdite(x, i)) {
                continue;
            }
            coups.add(new Coup(x, y, x, i, couleur));
        }
        for (short i = (short) (y - 1); i >= 0; i--) {
            if (this.estOccupee(x, i)) {
                break;
            }
            if (this.estInterdite(x, i)) {
                continue;
            }
            coups.add(new Coup(x, y, x, i, couleur));
        }
    }

    /**
	 * Liste les coups possibles pour un joueur
	 * Il faut passer un vector de coups non null
	 * @param couleur
	 * @return
	 */
    public void coupsPermis(int couleur, Vector<Coup> coups) {
        if (couleur == Plateau.BLANC) {
            this.coupsPermisBlanc(coups);
            return;
        }
        for (short x = 0; x < this.mTaille; x++) {
            for (short y = 0; y < this.mTaille; y++) {
                if (this.mPlateau[x][y] != couleur) {
                    continue;
                }
                this.coupsPermis(x, y, coups);
            }
        }
    }

    /**
	 * Les blancs ont le roi � g�rer dans les coups possibles
	 * alors on fait une m�thode rien que pour eux
	 * @param coups
	 */
    protected void coupsPermisBlanc(Vector<Coup> coups) {
        for (short x = 0; x < this.mTaille; x++) {
            for (short y = 0; y < this.mTaille; y++) {
                if (this.mPlateau[x][y] != Plateau.BLANC && this.mPlateau[x][y] != Plateau.ROI) {
                    continue;
                }
                this.coupsPermis(x, y, coups);
            }
        }
    }

    /**
	 * Clone le plateau actuel, joue le coup sur le plateau clon� et le retourne
	 * non utilis� � priori par soucis de performance
	 * @param coup
	 * @return
	 */
    public Plateau cloneEtJoue(Coup coup) {
        Plateau plateau = new Plateau(this.mTaille);
        try {
            plateau = (Plateau) this.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        plateau.jouerCoup(coup, false);
        return plateau;
    }

    /**
	 * Cette m�thode sert � v�rifier la validit� d'un coup
	 * @param coup
	 * @return
	 */
    public boolean estLegal(Coup coup) {
        if ((coup.departLigne == 0) && (coup.departColonne == 0) && (coup.arriveeLigne == 0) && (coup.arriveeColonne == 0)) {
            for (int i = 0; i < this.mTaille; i++) for (int j = 0; j < this.mTaille; j++) if (this.mPlateau[i][j] == coup.couleur) if (((i > 0) && (this.mPlateau[i - 1][j] == VIDE) && (this.mTypeCases[i - 1][j] != CASETRONE) && (this.mTypeCases[i - 1][j] != CASEINTERDITE)) || ((i < this.mTaille - 1) && (this.mPlateau[i + 1][j] == VIDE) && (this.mTypeCases[i + 1][j] != CASETRONE) && (this.mTypeCases[i + 1][j] != CASEINTERDITE)) || ((j > 0) && (this.mPlateau[i][j - 1] == VIDE) && (this.mTypeCases[i][j - 1] != CASETRONE) && (this.mTypeCases[i][j - 1] != CASEINTERDITE)) || ((j < this.mTaille - 1) && (this.mPlateau[i][j + 1] == VIDE) && (this.mTypeCases[i][j + 1] != CASETRONE) && (this.mTypeCases[i][j + 1] != CASEINTERDITE))) {
                return false;
            }
            return true;
        }
        if ((coup.couleur == NOIR && this.mPlateau[coup.departLigne][coup.departColonne] != coup.couleur) || (coup.couleur == BLANC && (this.mPlateau[coup.departLigne][coup.departColonne] != coup.couleur && this.mPlateau[coup.departLigne][coup.departColonne] != ROI)) || (coup.departLigne == coup.arriveeLigne && coup.departColonne == coup.arriveeColonne)) {
            return false;
        }
        if ((this.mTypeCases[coup.arriveeLigne][coup.arriveeColonne] == CASEINTERDITE) || (this.mTypeCases[coup.arriveeLigne][coup.arriveeColonne] == CASETRONE)) return false;
        if (coup.departLigne == coup.arriveeLigne || coup.departColonne == coup.arriveeColonne) {
            if (coup.departLigne == coup.arriveeLigne) {
                int decal;
                if (coup.departColonne < coup.arriveeColonne) decal = 1; else decal = -1;
                for (int i = coup.departColonne + decal; i != (coup.arriveeColonne + decal); i += decal) {
                    if (this.mPlateau[coup.departLigne][i] != VIDE) return false;
                }
                return true;
            } else {
                int decal;
                if (coup.departLigne < coup.arriveeLigne) decal = 1; else decal = -1;
                for (int i = coup.departLigne + decal; i != (coup.arriveeLigne + decal); i += decal) {
                    if (this.mPlateau[i][coup.departColonne] != VIDE) return false;
                }
                return true;
            }
        } else {
            return false;
        }
    }

    public void initPlateau() {
        this.mPlateau = new short[this.mTaille][this.mTaille];
        this.mTypeCases = new short[this.mTaille][this.mTaille];
        for (int i = 0; i < this.mTaille; i++) for (int j = 0; j < this.mTaille; j++) {
            this.mPlateau[i][j] = VIDE;
            this.mTypeCases[i][j] = VIDE;
        }
        this.mTypeCases[0][0] = CASEINTERDITE;
        this.mTypeCases[0][this.mTaille - 1] = CASEINTERDITE;
        this.mTypeCases[this.mTaille - 1][0] = CASEINTERDITE;
        this.mTypeCases[this.mTaille - 1][this.mTaille - 1] = CASEINTERDITE;
        this.mTypeCases[4][4] = CASETRONE;
        this.mRoiCol = 4;
        this.mRoiLig = 4;
        for (int i = 3; i < 6; i++) {
            this.mTypeCases[i][0] = CASESORTIE;
            this.mTypeCases[0][i] = CASESORTIE;
            this.mTypeCases[i][8] = CASESORTIE;
            this.mTypeCases[8][i] = CASESORTIE;
        }
        this.mPlateau[4][4] = ROI;
        this.mNbBlanc += 1;
        for (int i = 3; i < 6; i++) {
            this.mPlateau[0][i] = NOIR;
            this.mPlateau[this.mTaille - 1][i] = NOIR;
            this.mPlateau[i][0] = NOIR;
            this.mPlateau[i][this.mTaille - 1] = NOIR;
            this.mNbNoir += 4;
        }
        this.mPlateau[4][1] = NOIR;
        this.mPlateau[1][4] = NOIR;
        this.mPlateau[this.mTaille - 2][4] = NOIR;
        this.mPlateau[4][this.mTaille - 2] = NOIR;
        this.mNbNoir += 4;
        this.mPlateau[3][4] = BLANC;
        this.mPlateau[2][4] = BLANC;
        this.mPlateau[4][3] = BLANC;
        this.mPlateau[4][2] = BLANC;
        this.mPlateau[5][4] = BLANC;
        this.mPlateau[6][4] = BLANC;
        this.mPlateau[4][5] = BLANC;
        this.mPlateau[4][6] = BLANC;
        this.mNbBlanc += 8;
    }

    @Override
    public String toString() {
        String msg = new String();
        for (int i = 0; i < this.mTaille; i++) {
            for (int j = 0; j < this.mTaille; j++) {
                if (this.mPlateau[i][j] == VIDE) {
                    msg += "-";
                } else {
                    msg += this.mPlateau[i][j];
                }
            }
            msg += "\n";
        }
        return msg;
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        Plateau plateau = (Plateau) super.clone();
        plateau.mPlateau = new short[this.mTaille][this.mTaille];
        plateau.mTypeCases = new short[this.mTaille][this.mTaille];
        for (int x = 0; x < this.mTaille; x++) {
            for (int y = 0; y < this.mTaille; y++) {
                plateau.mPlateau[x][y] = this.mPlateau[x][y];
                plateau.mTypeCases[x][y] = this.mTypeCases[x][y];
            }
        }
        return plateau;
    }

    /**
	 * Ctor
	 * @param taille
	 */
    public Plateau(short taille) {
        this.mTaille = taille;
    }
}
