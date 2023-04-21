package rapidmot.dico;

/**
 * @author Monteiro & Aubert
 *	Contient le mot ainsi que le mot trier
 */
public class Mot {

    private String mot, motTrier, motAccent;

    /**
	 * 
	 */
    public Mot(String mot) {
        this.motAccent = mot.toLowerCase();
        this.mot = sansAccent(motAccent);
        trier();
    }

    public Mot(Mot mot) {
        this.mot = mot.getMot();
        this.motAccent = mot.getMotAccent();
        this.motTrier = mot.getMotTrier();
    }

    private void trier() {
        int length = mot.length();
        char tab[] = mot.toCharArray();
        for (int i = 0; i < length; ++i) for (int j = i; j < length; ++j) if (tab[i] > tab[j]) {
            char tmp = tab[i];
            tab[i] = tab[j];
            tab[j] = tmp;
        }
        motTrier = new String(tab);
    }

    /**
	 * cree le mot sans caract�re accentu� (�,�,�,�...)
	 * @param mot mot avec accent
	 * @return mot sans accent
	 */
    private String sansAccent(String mot) {
        String s = "";
        char c;
        for (int i = 0; i < mot.length(); i++) {
            c = mot.charAt(i);
            if (c < 'a' || c > 'z') {
                switch(c) {
                    case '�':
                        s += 'e';
                        break;
                    case '�':
                        s += 'e';
                        break;
                    case '�':
                        s += 'e';
                        break;
                    case '�':
                        s += 'e';
                        break;
                    case '�':
                        s += 'a';
                        break;
                    case '�':
                        s += 'a';
                        break;
                    case '�':
                        s += 'a';
                        break;
                    case '�':
                        s += 'i';
                        break;
                    case '�':
                        s += 'i';
                        break;
                    case '�':
                        s += 'o';
                        break;
                    case '�':
                        s += 'o';
                        break;
                    case '�':
                        s += 'u';
                        break;
                    case '�':
                        s += 'u';
                        break;
                    case '�':
                        s += 'u';
                        break;
                    case '�':
                        s += 'c';
                        break;
                }
            } else s += c;
        }
        return s;
    }

    /**
	 * Renvoi le mot d'origine
	 * @return mot
	 */
    public String getMot() {
        return mot;
    }

    /**
	 * @return le mot avec les caractere accentuer
	 */
    public String getMotAccent() {
        return motAccent;
    }

    /**
	 * Renvoi le mot trier 
	 * exemple pour chien renvoi ceihn
	 * @return motTrier
	 */
    public String getMotTrier() {
        return motTrier;
    }

    /**
	 * Renvoi true si le mot et contenu dans le mot de 6lettres
	 * @param mot6lettres qui doit etre trier
	 * @return true or false
	 */
    public boolean isContains(String mot6lettres) {
        int cpt = 0;
        int tailleSortie = motTrier.length();
        for (int i = 0; i < mot6lettres.length(); ++i) {
            if (motTrier.charAt(cpt) == mot6lettres.charAt(i)) {
                ++cpt;
                if (cpt == tailleSortie) return true;
            }
        }
        return cpt == tailleSortie;
    }

    /**
	 * renvoi vrai si le mot est plus petit ou egal (alphabetiquement parlant) que l'autre
	 */
    public boolean plusPetit(Mot m) {
        if (getSize() <= m.getSize()) {
            if (getSize() < m.getSize()) return true;
            String motAutre = m.getMot();
            for (int i = 0; i < mot.length(); ++i) {
                if (mot.charAt(i) == motAutre.charAt(i)) continue;
                if (mot.charAt(i) > motAutre.charAt(i)) return false; else return true;
            }
            return true;
        } else return false;
    }

    /**
	 * Renvoi la taille du mot
	 * @return
	 */
    public int getSize() {
        return mot.length();
    }

    public String toString() {
        return mot;
    }
}
