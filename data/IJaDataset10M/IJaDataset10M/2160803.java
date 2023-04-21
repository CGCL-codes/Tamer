package root;

import domain.Medlem;
import java.io.Serializable;

public class HoldHaandtering implements Serializable {

    DataHandler dh;

    public HoldHaandtering() {
        dh = new DataHandler();
    }

    public void tilfoejTilSpringRytme(Medlem m) {
        dh.tilfoejTilSpringRytme(m);
    }

    public void tilfoejTilGrandprix(Medlem m) {
        dh.tilfoejTilGrandprix(m);
    }

    public void tilfoejTilSpringgymnastik(Medlem m) {
        dh.tilfoejTilSpringgymnastik(m);
    }

    public void tilfoejTilRytme(Medlem m) {
        dh.tilfoejTilRytme(m);
    }

    public void tilfoejTilMotionFitness(Medlem m) {
        dh.tilfoejTilMotionFitness(m);
    }

    public void fjernFraSpringRytme(Medlem m) {
        dh.FjernFraSpringRytme(m);
    }

    public void fjernFraGrandprix(Medlem m) {
        dh.FjernFraGranprix(m);
    }

    public void fjernFraSpringgymnastik(Medlem m) {
        dh.FjernFraSpringgymnastik(m);
    }

    public void fjernFraRytme(Medlem m) {
        dh.FjernFraRytme(m);
    }

    public void fjernFraMotionFitness(Medlem m) {
        dh.FjernFraMotionFitness(m);
    }
}
