package core;

import java.util.*;

/** @pdOid 61dc51e2-ed43-4706-9d6e-00ae4240aaa9 */
public class Serigala extends Enemy {

    /** @pdOid 22ed855f-e3e4-454f-936b-b6f94cce6fb4 */
    public Serigala() {
        this.setID(6);
        this.setHp(30);
        this.setVelocity(3);
        this.setMoneykill(6);
    }

    public static void main(String args[]) {
        Serigala S1 = new Serigala();
        S1.Display();
    }
}
