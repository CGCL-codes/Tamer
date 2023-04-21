package HarvestMarsTanamantest;

import harvestmars.*;

/**
 *
 * @author Ryan
 */
public class Macaronni extends TanamanMultiTime {

    Macaronni(int X, int Y) {
        ID = 5;
        TitikDewasa = 10;
        TitikPanen = 13;
        TitikMati = 26;
        Umur = 0;
        HappyMeter = 0;
        this.X = X;
        this.Y = Y;
        Dewasa = false;
        Status = true;
        Harvest = false;
        Visual = 't';
    }

    Macaronni(int NewUmur, int NewHappyMeter, char NewVisual) {
        ID = 5;
        TitikDewasa = 10;
        TitikPanen = 13;
        TitikMati = 26;
        Umur = NewUmur;
        HappyMeter = NewHappyMeter;
        Dewasa = CheckDewasa();
        Status = CheckAlive();
        Harvest = CheckHarvest();
        Visual = NewVisual;
    }

    public void SetVisual(boolean VisualCode) {
        if (VisualCode == false) {
            Visual = '5';
        } else {
            Visual = 'e';
        }
    }

    @Override
    public void Grow(Lahan P) {
        if (P.w.getMusim() == 2) {
            if (GetStatus() == true) {
                SetUmur(GetUmur() + 1);
                CheckAlive();
                if (P.s[X][Y].getWatered() == 1) {
                    HappyMeterplusplus();
                    CheckDewasa();
                    CheckVisual();
                    CheckHarvest();
                }
            }
        } else {
            SetStatus(false);
        }
    }
}
