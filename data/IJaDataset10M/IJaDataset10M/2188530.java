package core;

import java.util.*;

/** @pdOid a66becc9-5316-4c55-9026-5da1e5ad8ee2 */
public class Perampok extends Enemy {

    /** @pdOid a6a46f60-b101-49e3-ac2a-bba4d03b3d21 */
    public Perampok() {
        this.setID(7);
        this.setHp(40);
        this.setVelocity(3);
        this.setMoneykill(8);
    }

    public static void main(String args[]) {
        Perampok P1 = new Perampok();
        P1.Display();
    }
}
