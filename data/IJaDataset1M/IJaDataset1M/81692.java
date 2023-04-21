package prove;

public class Giocatore {

    private String nome;

    private Carta[] carte = new Carta[10];

    public boolean setNome(String pNome) {
        if (!pNome.equals("")) {
            this.nome = pNome;
            return true;
        }
        return false;
    }

    public boolean setCarte(Carta[] pCarte) {
        int idb = 0;
        int idc = 0;
        int idd = 0;
        int ids = 0;
        if (pCarte.length == 10) {
            for (int i = 0; i < 10; i++) {
                String seme = pCarte[i].getSeme();
                if (seme.equals("Bastoni")) {
                    idb++;
                } else if (seme.equals("Denari")) {
                    idd++;
                } else if (seme.equals("Coppe")) {
                    idc++;
                } else {
                    ids++;
                }
            }
            int[] bastoni = new int[idb];
            int[] coppe = new int[idc];
            int[] denari = new int[idd];
            int[] spade = new int[ids];
            idb = 0;
            idc = 0;
            idd = 0;
            ids = 0;
            for (int i = 0; i < 10; i++) {
                String seme = pCarte[i].getSeme();
                if (seme.equals("Bastoni")) {
                    bastoni[idb] = i;
                    idb++;
                } else if (seme.equals("Denari")) {
                    denari[idd] = i;
                    idd++;
                } else if (seme.equals("Coppe")) {
                    coppe[idc] = i;
                    idc++;
                } else {
                    spade[ids] = i;
                    ids++;
                }
            }
            int i = 0;
            for (int id = 0; id < bastoni.length; id++) {
                this.carte[i] = pCarte[bastoni[id]];
                i++;
            }
            for (int id = 0; id < coppe.length; id++) {
                this.carte[i] = pCarte[coppe[id]];
                i++;
            }
            for (int id = 0; id < denari.length; id++) {
                this.carte[i] = pCarte[denari[id]];
                i++;
            }
            for (int id = 0; id < spade.length; id++) {
                this.carte[i] = pCarte[bastoni[id]];
                i++;
            }
            return true;
        }
        return false;
    }

    public String getNome() {
        return this.nome;
    }

    public Carta[] getCarte() {
        return this.carte;
    }

    public static void main(String[] args) {
        int idb = 0;
        int idc = 0;
        int idd = 0;
        int ids = 0;
        Mazzo mazzo = new Mazzo();
        mazzo.shuffle();
        Carta[] pCarte = mazzo.getGiocatore(1);
        Carta[] carte = new Carta[10];
        for (int i = 0; i < 10; i++) {
            String seme = pCarte[i].getSeme();
            if (seme.equals("Bastoni")) {
                idb++;
            } else if (seme.equals("Denari")) {
                idd++;
            } else if (seme.equals("Coppe")) {
                idc++;
            } else {
                ids++;
            }
        }
        int[] bastoni = new int[idb];
        int[] coppe = new int[idc];
        int[] denari = new int[idd];
        int[] spade = new int[ids];
        idb = 0;
        idc = 0;
        idd = 0;
        ids = 0;
        for (int i = 0; i < 10; i++) {
            String seme = pCarte[i].getSeme();
            if (seme.equals("Bastoni")) {
                bastoni[idb] = i;
                idb++;
            } else if (seme.equals("Denari")) {
                denari[idd] = i;
                idd++;
            } else if (seme.equals("Coppe")) {
                coppe[idc] = i;
                idc++;
            } else {
                spade[ids] = i;
                ids++;
            }
        }
        int i = 0;
        for (int id = 0; id < bastoni.length; id++) {
            carte[i] = pCarte[bastoni[id]];
            i++;
        }
        for (int id = 0; id < coppe.length; id++) {
            carte[i] = pCarte[coppe[id]];
            i++;
        }
        for (int id = 0; id < denari.length; id++) {
            carte[i] = pCarte[denari[id]];
            i++;
        }
        for (int id = 0; id < spade.length; id++) {
            carte[i] = pCarte[bastoni[id]];
            i++;
        }
    }
}
