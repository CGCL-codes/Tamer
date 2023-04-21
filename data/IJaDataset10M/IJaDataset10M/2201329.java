package zeus_proto;

/**
 * Robban�szert reprezent�l� oszt�ly.
 */
public class Bomb extends Collidible {

    /**
     * Ennyi tick ut�n fog robbanni
     */
    private int countDown;

    /**
     * konstruktor
     * L�trehoz�skor be�lljt�dik 1-re a h�tramarad� id� robban�sig
     * vagyis a k�vetkez� �temben robban.
     * @param name N�v
     */
    public Bomb(String name) {
        myName = name;
        rolls = false;
        countDown = 1;
        myView = new bombView();
        myView.constructDraw();
    }

    /**
     * �temez�s, ha van m�g id� akkor cs�kkenti ezt, ha nincs akkor
     * megh�vja az �sszes ir�nyban lev� szomsz�d robban�s kezel�j�t.
     * El�tte le kell k�rdezze ezeket e szomsz�dokat.
     */
    public void tick() {
        if (Debug.verbosity > 1) System.out.println("->Bomb:" + myName + " tick()");
        if (countDown > 0) minusTimer(); else {
            Field next;
            next = parent.getField(Game.E);
            if (next != null) next.explode();
            next = parent.getField(Game.SE);
            if (next != null) next.explode();
            next = parent.getField(Game.SW);
            if (next != null) next.explode();
            next = parent.getField(Game.W);
            if (next != null) next.explode();
            next = parent.getField(Game.NW);
            if (next != null) next.explode();
            next = parent.getField(Game.NE);
            if (next != null) next.explode();
            parent.setField(null);
            Game.tempDelList.add(this);
        }
    }

    /**
     * A robban�sig h�tramarad� �temek sz�m�nak cs�kkent�se.
     */
    public void minusTimer() {
        if (Debug.verbosity > 1) System.out.println("->Bomb:" + myName + " minusTimer()");
        countDown--;
        bombView V = (bombView) myView;
        V.tickDraw(countDown);
    }

    /**
     * Bomba id?z�t?j�nek be�ll�t�sa
     * @param i Be�ll�tand� tick sz�m robban�sig
     */
    public void setTimer(int i) {
        if (Debug.verbosity > 1) System.out.println("->Bomb:" + myName + " minusTimer()");
        countDown = i;
    }

    /**
     * �ltal�nos <B>explodeObject</B> override, a bomb�t ha felrobbantj�k akkor 
     * ? is felrobban, nem csak elt?nik, �gy a k�r�l�tte lev? mez?knek is megh�vja
     * az expldeObject met�dus�t.
     */
    public void explodeObject() {
        if (Debug.verbosity > 1) System.out.println("->Bomb:" + myName + " explodeObject()");
        parent.setField(null);
        countDown = 0;
        this.tick();
        Game.tempDelList.add(this);
    }
}
