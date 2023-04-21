package net.claribole.zvtm.lens;

import java.awt.image.SinglePixelPackedSampleModel;
import java.awt.image.WritableRaster;

/**Translucent lens.*/
public abstract class TLens extends FixedSizeLens {

    int lensProjectedWidth = 0;

    int lensProjectedHeight = 0;

    double d = 0;

    protected float MMTc = 0.0f;

    protected float MMTf = 1.0f;

    float[] gainT = new float[1];

    int[] BMl, BMm;

    int[] BOl, BOm;

    int Pl, Pm;

    int Rl, Gl, Bl;

    int Rm, Gm, Bm, Am;

    int Rr, Gr, Br;

    public void gf(float x, float y, float[] g) {
        d = Math.max(Math.abs(x - sw - lx), Math.abs(y - sh - ly));
        if (d <= LR2) g[0] = g[1] = MM; else g[0] = g[1] = 1;
    }

    public abstract void gfT(float x, float y, float[] g);

    synchronized void transformI(WritableRaster iwr, WritableRaster ewr) {
        synchronized (this) {
            if (BMl == null) {
                SinglePixelPackedSampleModel SMl = (SinglePixelPackedSampleModel) ewr.getSampleModel();
                SinglePixelPackedSampleModel SMm = (SinglePixelPackedSampleModel) iwr.getSampleModel();
                BMl = SMl.getBitMasks();
                BMm = SMm.getBitMasks();
                BOl = SMl.getBitOffsets();
                BOm = SMm.getBitOffsets();
            }
            iwr.getDataElements(lurd[0], lurd[1], lensWidth, lensHeight, oPixelsI);
            ewr.getDataElements(0, 0, mbw, mbh, mPixelsI);
            if (BMl.length == 4) {
                for (int x = lurd[0]; x < lurd[2]; x++) {
                    for (int y = lurd[1]; y < lurd[3]; y++) {
                        Pl = mPixelsI[Math.round(((y - lurd[1]) * MM - mbh / 2.0f) / MM + mbh / 2.0f) * mbw + Math.round(((x - lurd[0]) * MM - mbw / 2.0f) / MM + mbw / 2.0f)];
                        Rl = (Pl & BMl[0]) >>> BOl[0];
                        Gl = (Pl & BMl[1]) >>> BOl[1];
                        Bl = (Pl & BMl[2]) >>> BOl[2];
                        Pm = oPixelsI[(y - lurd[1]) * (lensWidth) + (x - lurd[0])];
                        Rm = (Pm & BMm[0]) >>> BOm[0];
                        Gm = (Pm & BMm[1]) >>> BOm[1];
                        Bm = (Pm & BMm[2]) >>> BOm[2];
                        Am = (Pm & BMm[3]) >>> BOm[3];
                        this.gfT(x, y, gainT);
                        Rr = Math.round(Rl * gainT[0] + Rm * (1 - gainT[0]));
                        Gr = Math.round(Gl * gainT[0] + Gm * (1 - gainT[0]));
                        Br = Math.round(Bl * gainT[0] + Bm * (1 - gainT[0]));
                        tPixelsI[(y - lurd[1]) * (lensWidth) + (x - lurd[0])] = (Rr << BOm[0]) | (Gr << BOl[1]) | (Br << BOl[2]) | (Am << BOl[3]);
                    }
                }
            } else {
                for (int x = lurd[0]; x < lurd[2]; x++) {
                    for (int y = lurd[1]; y < lurd[3]; y++) {
                        Pl = mPixelsI[Math.round(((y - lurd[1]) * MM - mbh / 2.0f) / MM + mbh / 2.0f) * mbw + Math.round(((x - lurd[0]) * MM - mbw / 2.0f) / MM + mbw / 2.0f)];
                        Rl = (Pl & BMl[0]) >>> BOl[0];
                        Gl = (Pl & BMl[1]) >>> BOl[1];
                        Bl = (Pl & BMl[2]) >>> BOl[2];
                        Pm = oPixelsI[(y - lurd[1]) * (lensWidth) + (x - lurd[0])];
                        Rm = (Pm & BMm[0]) >>> BOm[0];
                        Gm = (Pm & BMm[1]) >>> BOm[1];
                        Bm = (Pm & BMm[2]) >>> BOm[2];
                        this.gfT(x, y, gainT);
                        Rr = Math.round(Rl * gainT[0] + Rm * (1 - gainT[0]));
                        Gr = Math.round(Gl * gainT[0] + Gm * (1 - gainT[0]));
                        Br = Math.round(Bl * gainT[0] + Bm * (1 - gainT[0]));
                        tPixelsI[(y - lurd[1]) * (lensWidth) + (x - lurd[0])] = (Rr << BOm[0]) | (Gr << BOl[1]) | (Br << BOl[2]);
                    }
                }
            }
            iwr.setDataElements(lurd[0], lurd[1], lensWidth, lensHeight, tPixelsI);
        }
    }

    synchronized void transformS(WritableRaster iwr, WritableRaster ewr) {
        synchronized (this) {
            if (BMl == null) {
                SinglePixelPackedSampleModel SMl = (SinglePixelPackedSampleModel) ewr.getSampleModel();
                SinglePixelPackedSampleModel SMm = (SinglePixelPackedSampleModel) iwr.getSampleModel();
                BMl = SMl.getBitMasks();
                BMm = SMm.getBitMasks();
                BOl = SMl.getBitOffsets();
                BOm = SMm.getBitOffsets();
            }
            iwr.getDataElements(lurd[0], lurd[1], lensWidth, lensHeight, oPixelsS);
            ewr.getDataElements(0, 0, mbw, mbh, mPixelsS);
            for (int x = lurd[0]; x < lurd[2]; x++) {
                for (int y = lurd[1]; y < lurd[3]; y++) {
                    Pl = mPixelsS[Math.round(((y - lurd[1]) * MM - mbh / 2.0f) / MM + mbh / 2.0f) * mbw + Math.round(((x - lurd[0]) * MM - mbw / 2.0f) / MM + mbw / 2.0f)];
                    Rl = (Pl & BMl[0]) >>> BOl[0];
                    Gl = (Pl & BMl[1]) >>> BOl[1];
                    Bl = (Pl & BMl[2]) >>> BOl[2];
                    Pm = oPixelsS[(y - lurd[1]) * (lensWidth) + (x - lurd[0])];
                    Rm = (Pm & BMm[0]) >>> BOm[0];
                    Gm = (Pm & BMm[1]) >>> BOm[1];
                    Bm = (Pm & BMm[2]) >>> BOm[2];
                    this.gfT(x, y, gainT);
                    Rr = Math.round(Rl * gainT[0] + Rm * (1 - gainT[0]));
                    Gr = Math.round(Gl * gainT[0] + Gm * (1 - gainT[0]));
                    Br = Math.round(Bl * gainT[0] + Bm * (1 - gainT[0]));
                    tPixelsS[(y - lurd[1]) * (lensWidth) + (x - lurd[0])] = (short) ((Rr << BOm[0]) | (Gr << BOl[1]) | (Br << BOl[2]));
                }
            }
            iwr.setDataElements(lurd[0], lurd[1], lensWidth, lensHeight, tPixelsS);
        }
    }

    synchronized void transformB(WritableRaster iwr, WritableRaster ewr) {
        System.err.println("Error: translucent lens: Sample model not supported yet");
    }

    public float getFocusTranslucencyValue() {
        return MMTf;
    }

    public float getContextTranslucencyValue() {
        return MMTc;
    }
}
