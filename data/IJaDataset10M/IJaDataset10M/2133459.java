package core;

import java.util.*;

/** @pdOid e98738ae-4301-452b-8586-3bb4a14f1b0c */
public class Kentang extends Plant {

    private int penurunan_tingkat_panen;

    /** @pdOid f73f21c1-bd30-43ce-b860-1553747262fc */
    public Kentang(int x, int y) {
        super(6, 3, 3, false, 0, 12, x, y);
        penurunan_tingkat_panen = 2;
    }

    public void watered() {
        if (!getStatusWatered() && !isDead()) {
            setStatusWatered(true);
        }
    }

    public void harvested() {
        if (isHarvest()) {
            if (isRepanenable()) {
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
