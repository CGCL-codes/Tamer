package forza4;

import forza4.Impostazioni.Pedina;

public class Giocatore {

    protected Pedina pedina;

    private Integer mossaGUI;

    protected boolean turno = false;

    protected Board board;

    protected Statistica stat;

    protected int mossa;

    public Giocatore(Board board) {
        this.board = board;
    }

    public void setPedina(Pedina pedina) {
        this.pedina = pedina;
    }

    public Pedina getPedina() {
        return this.pedina;
    }

    public void setmossaGUI(int mossa) {
        mossaGUI = new Integer(mossa);
    }

    public void eseguiMossa() {
    }
}
