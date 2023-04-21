package no.eirikb.bomberman.applet.game;

/**
 *
 * @author eirikb
 */
public class Fire {

    private int x;

    private int y;

    private int anim;

    private int anim2;

    private Way way;

    private boolean end;

    public Fire(int x, int y, Way way, boolean end) {
        this.x = x;
        this.y = y;
        this.way = way;
        this.end = end;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public Way getWay() {
        return way;
    }

    public boolean isEnd() {
        return end;
    }

    public int anim() {
        anim2++;
        if (anim2 == 2) {
            anim++;
            anim2 = 0;
        }
        return anim;
    }
}
