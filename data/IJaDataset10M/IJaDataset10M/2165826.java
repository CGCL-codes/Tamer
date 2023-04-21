package zeus_proto;

import java.io.*;
import java.util.*;

/**
 * Ez az objektum szolg�l t�rol�k�nt a p�lya objektumoknak mint Stone, Indian. Ezen k�v�l a szomsz�d mez�ket is t�rolja, ezeknek referenci�it lek�rdezhet�v� teszi, objektumok mozg�s�t kezeli.
 */
public class Field {

    /**
     * A fieldnek a rajzolo objektuma
     */
    public explodeView myView;

    /**
     * Field neve amit ki lehet iratni debug c�lokra. A konstruktora ezt �ll�tja be.
     */
    public String myName;

    /**
     * kell az ]tvonalkereseshez, kifejtettuk mar ezt a csomopontot ?
     */
    public boolean visited;

    /**
     * kell az ]tvonalkereseshez, ez az aktu�lis t�vols�g a sourcet�l
     */
    public int dist;

    /**
     * kell az ]tvonalkereseshez, az optim�lis �tban az el?z? csom�pont.
     */
    public Field prev;

    /**
     * A szomsz�dok t�mbjei , nem mindig van minden szomsz�d megadva.
     */
    public Field[] neighborList;

    /**
     * A tartalmazott p�lyaobjektum referenci�ja.
     */
    public Collidible myObject;

    /**
     * Field konstrukora. L�trehozza szomsz�dokat t�rol� t�mb�t.
     * @param name Ezt a nevet �ll�tja be a Fieldnek.
     */
    public Field(String name) {
        myName = name;
        neighborList = new Field[6];
        myView = new explodeView();
        myView.constructDraw();
    }

    public void setViewCoords(int x, int y) {
    }

    /**
     * Be�ll�tja a Field tartalm�t az �j objektumra, amit param�terk�nt kap.
     * @param n Az �j tartalom.
     */
    public void setField(Collidible n) {
        if (Debug.verbosity > 2) System.out.println("->" + myName + " setField(" + n.myName + ")");
        myObject = n;
    }

    /**
     * Visszaadja azt a szomsz�dot ami a dir ir�nyban tal�lhat�. 
     * Az ir�nyoknak megfelel egy egyedi sz�m amivel meg lehet c�mezni a 
     * neighborList t�mb�t.
     * @param dir Ebben az ir�nyban lev� szomsz�dot k�rdezi le.
     * @return Dir ir�nybeli szomsz�d.
     */
    public Field getField(zeus_proto.Direction dir) {
        if (Debug.verbosity > 1) System.out.println("->" + myName + " getField(" + dir.myName + ")");
        Field next;
        if (neighborList[dir.myInt] != null) {
            next = neighborList[dir.myInt];
            if (Debug.verbosity > 1) System.out.println("<-" + next.myName);
            return next;
        }
        return null;
    }

    /**
     * Visszaadja azt az objektumot ami rajta van.
     * @return Tartalmazott objektum.
     */
    public Collidible getObject() {
        return myObject;
    }

    /**
     * Indian mozg�s�nak kezel�se, ez �tk�zik 
     * ha van valami a mez�n, vagy csak helyet v�ltoztat.
     * @param A Indian aki mozog
     * @param dir Ebbe az ir�nyba mozog, Stone tol�skor hasznos
     * @return true ha nem volt ott semmi �s helyet cser�lt.
     */
    public boolean move(Indian A, Direction dir) {
        if (Debug.verbosity > 0) System.out.println("->" + myName + " move(" + A.myName + "," + dir.myName + ")/Field");
        if (Debug.verbosity > 0) System.out.print(myName);
        if (myObject != null) return (myObject.collide(A, dir)); else {
            shift(A);
        }
        return true;
    }

    /**
     * Stone mozg�s�nak kezel�se, ez �tk�zik 
     * ha van valami a mez�n, ekkor ha lefele mozdult akkor
     * m�r nem esik (setFalling), vagy csak helyet v�ltoztat,
     * ha lefele akkor es�sben van(setFalling).
     * @param A Stone aki mozog
     * @param dir Ebbe az ir�nyba mozog
     * @return true ha nem volt ott semmi �s helyet cser�lt.
     */
    public boolean move(Stone A, zeus_proto.Direction dir) {
        if (Debug.verbosity > 0) System.out.println("->Stone:" + myName + " move(" + A.myName + "," + dir.myName + ")/Field");
        if (myObject != null) {
            if (dir.myInt == 1 || dir.myInt == 2) {
                if (Debug.verbosity > 0) System.out.print(myName);
                myObject.collide(A, dir);
                if (Debug.verbosity > 0) System.out.print(myName);
                A.setFalling(false);
            }
            return (false);
        } else {
            if (Debug.verbosity > 0) System.out.print(myName);
            shift(A);
            if (dir.myInt == 1) {
                if (Debug.verbosity > 0) System.out.print(myName);
                A.setFalling(true);
            }
        }
        return true;
    }

    /**
     * Wumpus mozg�s�nak kezel�se, ez �tk�zik 
     * ha van valami a mez�n, vagy csak helyet v�ltoztat.
     * @param A aki mozog
     * @param dir Ebbe az ir�nyba mozog
     * @return true ha nem volt ott semmi �s helyet cser�lt.
     */
    public boolean move(Wumpus A, zeus_proto.Direction dir) {
        if (Debug.verbosity > 0) System.out.println("->" + myName + " move(" + A.myName + "," + dir.myName + ")/Wumpus");
        if (Debug.verbosity > 0) System.out.print(myName);
        if (myObject != null) return (myObject.collide(A, dir));
        return true;
    }

    /**
     * �ltal�nos move
     * @param A Aki mozog
     * @param dir Ebbe az ir�nyba mozog
     * @return true ha nem volt ott semmi �s helyet cser�lt.
     */
    public boolean move(Collidible A, zeus_proto.Direction dir) {
        if (Debug.verbosity > 0) System.out.println("->" + myName + " move(" + A.myName + "," + dir.myName + ")/Field");
        if (Debug.verbosity > 0) System.out.print(myName);
        if (myObject != null) return (myObject.collide(A, dir)); else {
            shift(A);
        }
        return true;
    }

    /**
     * Flood terjed�s�nek kezel�se speci�lis eset. Pr�b�l terjedni minden 
    ** ir�nyba �s ahol nincs semmi ott l�trehoz egy �jabb p�ld�nyt.
     * @param A Flood aki terjed
     * @param dir Ebbe az ir�nyba mozog
     * @return true ha tudott terjedni.
     */
    public boolean move(Flood A, zeus_proto.Direction dir) {
        if (myObject == null) {
            if (Debug.verbosity > 0) System.out.println("->Flood:" + myName + " move(" + A.myName + "," + dir.myName + ")/Field");
            Flood l2 = new Flood("F");
            Game.tempList.add(l2);
            if (Debug.verbosity > 1) System.out.println(myName + "->Flood:" + l2.myName + " <<CREATE>>");
            setField(l2);
            l2.setNewLocation(this);
            return true;
        } else {
            myObject.collide(A, dir);
            if (Debug.verbosity > 1) System.out.println("<-Don't step");
            return false;
        }
    }

    /**
     * A Field dir ir�nybeli szomsz�dj�nak be�ll�t�sa
     * @param f Ki van ott
     * @param dir Ebben az ir�nyban
     */
    public void setNeighbor(Field f, zeus_proto.Direction dir) {
        if (Debug.verbosity > 2) System.out.println("Game->" + myName + " setNeighbor(" + f.myName + "," + dir.myName + ")");
        neighborList[dir.myInt] = f;
    }

    /**
     * Helycser�k kezel�se. Be�ll�tja az objektum helyzet�t az �j mez�re,
     * elt�vol�tja a r�gir�l.
     * @param A Aki helyet akar cser�lni.
     */
    public void shift(Collidible A) {
        if (Debug.verbosity > 0) System.out.println("->" + myName + " shift(" + A.myName + ")");
        if (Debug.verbosity > 1) System.out.print(myName);
        setField(A);
        if (Debug.verbosity > 1) System.out.print(myName);
        A.parent.freeField();
        A.parent.myView.constructDraw();
        if (Debug.verbosity > 1) System.out.print(myName);
        A.setNewLocation(this);
        moveableView V = (moveableView) A.myView;
        V.moveDraw();
    }

    /**
     * Helyv�ltoztat�skor, a tartalmazott objektum referenci�j�nak t�rl�se.
     * Ezut�n �res lesz a mez�.
     */
    public void freeField() {
        if (Debug.verbosity > 1) System.out.println("->" + myName + " freeField()");
        myObject = null;
    }

    /**
     * �tem kezel� met�dus, annyit csin�l , hogy az utvonalkeres�shez sz�ks�ges v�ltoz�kat default �rt�kre �ll�tja
     * megh�vja ennek a tick met�dus�t.
     */
    public void tick() {
        visited = false;
        dist = 100;
    }

    /**
     * Bomba robban�skor h�v�dik meg, tov�bb�tja ezt a h�v�st a tartalmazott 
     * objektumnak.
     */
    public void explode() {
        if (Debug.verbosity > 1) System.out.println("->" + myName + " explode()");
        if (myObject != null) {
            myObject.explodeObject();
        } else myView.explodeDraw();
    }

    /**
     * Bomba lerak�sa/l�trehoz�sa. 
     * Egy �j bomb t�pus� objektumot hoz l�tre �s be�ll�tja a 
     * sz�ks�ges referenci�kat.
     */
    public void setBomb() {
        if (Debug.verbosity > 2) System.out.println(myName + "->B <<CREATE>>");
        Bomb b = new Bomb("B");
        b.setNewLocation(this);
        setField(b);
        Game.bombList.add(b);
    }

    /**
     * Utvonalkeres�s ut�n be�ll�tja a sz�ks�ege �rt�keket default �rt�kre.
     */
    public void resetList() {
        Iterator itr = Game.fieldList.iterator();
        Field A;
        while (itr.hasNext()) {
            A = (Field) itr.next();
            A.prev = null;
            A.visited = false;
            A.dist = 99;
        }
    }

    /**
     * Visszat�r annak az �regnek a fieldjeinek list�j�val, amiben van ez a field, 
     * �s csak �res mez?k meg indi�nok szerepelnek.
     * Rekurz�v met�dus, megh�vja mag�t azokra a fieldekre ahol van indi�n vagy �res �s
     * visszat�r az �j list�val.
     * @return B?vebb �reg lista
     * @param A Aktu�lis �reg lista
     */
    public LinkedList getList(LinkedList A) {
        this.visited = true;
        if ((myObject == null) || (myObject.myName.charAt(0) == 'J')) A.add(this);
        Field f;
        for (int i = 0; i < 6; i++) {
            f = this.neighborList[i];
            if (f != null) if (((f.myObject == null) || (f.myObject.myName.charAt(0) == 'J')) && (f.visited == false)) {
                A = f.getList(A);
            }
        }
        return A;
    }
}
