package Model;

public class Reponse {

    private String popos;

    private boolean juste;

    public Reponse() {
        this.popos = "";
        this.juste = false;
    }

    public Reponse(String popos, boolean juste) {
        this.popos = popos;
        this.juste = juste;
    }

    public String getPopos() {
        return popos;
    }

    public void setPopos(String popos) {
        this.popos = popos;
    }

    public boolean isJuste() {
        return juste;
    }

    public void setJuste(boolean juste) {
        this.juste = juste;
    }
}
