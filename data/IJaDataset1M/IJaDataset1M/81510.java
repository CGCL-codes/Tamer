package kgarten.gui;

/** A pr�ri egy mez�j�t reprezent�l� oszt�ly. Ez az oszt�ly felel�s a
 * j�t�k t�rreprezent�ci�j�nak megval�s�t�s��rt. Tartalmazza a mez�ben
 * l�v� objektumot �s a t�rreprezent�ci�nak megfelel� koordin�t�kat.
 * @inst. 0..*
 * @comp. <tt>object: KObject</tt>
 * @resp. a mozgat�sok elv�gz�se.
 */
public class Field implements KConstants {

    /** A pr�ri sz�less�ge mez�kben. */
    public static final int WIDTH = 25;

    /** A pr�ri magass�ga mez�kben. */
    public static final int HEIGHT = 25;

    /** Aktu�lis poz�ci� koordin�t�i. */
    private Coord coord;

    /** A mez�ben l�v� objektum. */
    private KObject object;

    /** L�trehoz egy <tt>obj</tt> objektumot tartalmaz� (<tt>x</tt>, <tt>y</tt>) poz�ci�j� mez�t. */
    public Field(KObject object, int x, int y) {
        this.object = object;
        coord = new Coord(x, y);
    }

    /** Visszaadja a mez�ben t�rolt objektumot. */
    public KObject getObject() {
        return object;
    }

    /** Visszaadja az <tt>x</tt> koordin�t�t. */
    public int getX() {
        return coord.x;
    }

    /** Visszaadja az <tt>y</tt> koordin�t�t. */
    public int getY() {
        return coord.y;
    }

    /** Visszaadja a <tt>dir</tt> ir�nyhoz k�pest jobbra fordul�s
	 * ut�n kapott ir�nyt. */
    public static int turnRight(int dir) {
        switch(dir) {
            case UP:
                return UPRIGHT;
            case DOWN:
                return DOWNLEFT;
            case UPRIGHT:
                return DOWNRIGHT;
            case UPLEFT:
                return UP;
            case DOWNRIGHT:
                return DOWN;
            case DOWNLEFT:
                return UPLEFT;
            case STAY:
                return UP;
            default:
                return -1;
        }
    }

    /** Visszaadja a <tt>dir</tt> ir�nyhoz k�pest balra fordul�s
	 * ut�n kapott ir�nyt. */
    public static int turnLeft(int dir) {
        switch(dir) {
            case UP:
                return UPLEFT;
            case DOWN:
                return DOWNRIGHT;
            case UPRIGHT:
                return UP;
            case UPLEFT:
                return DOWNLEFT;
            case DOWNRIGHT:
                return UPRIGHT;
            case DOWNLEFT:
                return DOWN;
            case STAY:
                return DOWN;
            default:
                return -1;
        }
    }

    /** Megvizsg�lja, hogy az <tt>f</tt> mez�t�l a megadott
	 * ir�nyban a <tt>g</tt> szomsz�dos mez� tal�lhat�-e.
	 * @param f a viszony�t�s alapj�t k�pez� mez�.
	 * @param g a viszony�tott mez�.
	 * @param dir a viszony�t�s ir�nya.
	 * @return <tt>true</tt> ha <tt>f</tt>-t�l <tt>dir</tt>
	 * ir�nyban a <tt>g</tt> mez� tal�lhat�.
	 * @return <tt>false</tt> egy�bk�nt.
	 */
    public static boolean isNext(Field f, Field g, int dir) {
        return g.coord.equals(f.coord.getDir(dir));
    }

    /** �thelyezi a mez�t a megadott ir�nyban szomsz�dos
	 * poz�ci�ra. */
    public void move(int dir) {
        coord = coord.getDir(dir);
    }

    /** Az <tt>f</tt> mez�t �tmozgatja a <tt>g</tt> mez�
	 * poz�ci�j�ba. */
    public static void moveTo(Field f, Field g) {
        f.coord = g.coord.getDir(STAY);
    }

    /** Felcser�li az <tt>f</tt> �s <tt>g</tt> mez�t. */
    public static void swap(Field f, Field g) {
        Coord pf = f.coord.getDir(STAY);
        Coord pg = g.coord.getDir(STAY);
        f.coord = pg;
        g.coord = pf;
    }

    private class Coord {

        private int x;

        private int y;

        public Coord(int x, int y) {
            this.x = x;
            this.y = y;
        }

        /** Visszaadja a <tt>dir</tt> ir�nyban l�v� szomsz�dos mez�
		 * <tt>x</tt>, <tt>y</tt> koordin�t�j�t reprezent�l� oszt�lyt. */
        public Coord getDir(int dir) {
            int newx = -1;
            int newy = -1;
            switch(dir) {
                case UP:
                    newx = x;
                    newy = y - 2;
                    break;
                case DOWN:
                    newx = x;
                    newy = y + 2;
                    break;
                case UPRIGHT:
                    newx = x + 1;
                    newy = y - 1;
                    break;
                case UPLEFT:
                    newx = x - 1;
                    newy = y - 1;
                    break;
                case DOWNRIGHT:
                    newx = x + 1;
                    newy = y + 1;
                    break;
                case DOWNLEFT:
                    newx = x - 1;
                    newy = y + 1;
                    break;
                case STAY:
                    newx = x;
                    newy = y;
            }
            if (newx < 0 || newx >= WIDTH || newy < 0 || newy >= HEIGHT) {
                newx = x;
                newy = y;
            }
            return new Coord(newx, newy);
        }

        public boolean equals(Coord p) {
            return x == p.x && y == p.y;
        }
    }
}
