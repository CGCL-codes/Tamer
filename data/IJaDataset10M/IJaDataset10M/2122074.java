package gladiatormanager;

public class PuettavaTavara {

    public static final int ASE = 1;

    public static final int KILPI = 2;

    public static final int KYPARA = 3;

    public static final int HAARNISKA = 4;

    public static final int JALKAHAARNISKA = 5;

    private int paino;

    private String nimi;

    private int[] varatutKohdat;

    private int voima, kantomatka, lyontikustannus;

    private int suojaus;

    public void setNimi(String nimi) {
        this.nimi = nimi;
    }

    public String getNimi() {
        return nimi;
    }

    public void setLyontikustannus(int lyontikustannus) {
        this.lyontikustannus = lyontikustannus;
    }

    public int getLyontikustannus() {
        return lyontikustannus;
    }

    public void setVoima(int vahinko) {
        this.voima = vahinko;
    }

    public int getVoima() {
        return voima;
    }

    public void setKantomatka(int kantomatka) {
        this.kantomatka = kantomatka;
    }

    public int getKantomatka() {
        return kantomatka;
    }

    public void setSuojaus(int suojaus) {
        this.suojaus = suojaus;
    }

    public int getSuojaus() {
        return suojaus;
    }

    public void setPaino(int paino) {
        this.paino = paino;
    }

    public int getPaino() {
        return paino;
    }

    public void setVaratutKohdat(int[] varatutKohdat) {
        this.varatutKohdat = varatutKohdat;
    }

    public void setVarattuKohta(int varattuKohta) {
        varatutKohdat = new int[1];
        varatutKohdat[0] = varattuKohta;
    }

    public int[] getVaratutKohdat() {
        return varatutKohdat;
    }

    public boolean varaakoKohdan(int kohta) {
        for (int i = 0; i < varatutKohdat.length; i++) {
            if (varatutKohdat[i] == kohta) return true;
        }
        return false;
    }
}
