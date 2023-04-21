package core;

import java.util.*;

/** @pdOid 5f222858-8b7a-422d-83fb-6bc4438a026e */
public class Mawar extends Plant {

    private int penurunan_tingkat_panen;

    /** @pdOid 594e3d1d-74ec-4ca2-a933-9349891e0b4a */
    public Mawar(int x, int y) {
        super(1, 3, 5, true, 0, 14, x, y);
        penurunan_tingkat_panen = 1;
    }

    public void watered() {
        if (!getStatusWatered() && !isDead()) {
            setStatusWatered(true);
        }
    }

    public void harvested() {
        if (isHarvest()) {
            if (isRepanenable()) {
                System.out.println("happy meter : " + happyMeter);
                System.out.println("tingkat panen : " + tingkatPanen);
                System.out.println("tingkat dewasa : " + tingkatDewasa);
                System.out.println("penurunan tingkat panen : " + penurunan_tingkat_panen);
                if (tingkatPanen - penurunan_tingkat_panen >= tingkatDewasa) {
                    tingkatPanen -= penurunan_tingkat_panen;
                    happyMeter = tingkatDewasa;
                    if (tingkatPanen <= tingkatDewasa) {
                        setUmur(0);
                    }
                } else {
                    setUmur(0);
                }
            } else {
                setUmur(0);
            }
        }
    }
}
