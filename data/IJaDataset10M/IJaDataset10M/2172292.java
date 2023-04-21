package photocard.modeles;

import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.awt.image.renderable.ParameterBlock;
import javax.media.jai.JAI;
import javax.media.jai.LookupTableJAI;
import javax.media.jai.PlanarImage;
import javax.media.jai.ROI;
import javax.media.jai.RenderedOp;
import photocard.vues.VuePrincipale;

/**
 * Class OutilsManager
 */
public class OutilsManager {

    private VuePrincipale vuePrincipale;

    private BufferedImage originalImage, resultImage;

    private int height, width;

    public OutilsManager(VuePrincipale vu) {
        this.vuePrincipale = vu;
    }

    public void setResultImage(BufferedImage resultImage) {
        this.resultImage = resultImage;
    }

    public BufferedImage getResultImage() {
        return resultImage;
    }

    public void rotation(String sens, EmplacementPhoto emp) {
        int h = emp.getContenuModifie().getHeight();
        int w = emp.getContenuModifie().getWidth();
        int RotDeg = 0;
        if (sens == "+") RotDeg = 10; else if (sens == "-") RotDeg = -10;
        float rotateAngle = (float) Math.toRadians(RotDeg);
        float centerX = (float) width / 2;
        float centerY = (float) height / 2;
        ParameterBlock pb = new ParameterBlock();
        pb.addSource(emp.getContenuModifie());
        pb.add(centerX);
        pb.add(centerY);
        pb.add(rotateAngle);
        pb.add(new javax.media.jai.InterpolationBicubic(8));
        PlanarImage rotatedImage = JAI.create("rotate", pb);
        emp.setContenuModifie(rotatedImage.getAsBufferedImage());
        int h1 = emp.getContenuModifie().getHeight();
        int w1 = emp.getContenuModifie().getWidth();
        emp.setHauteurAffichage(emp.getHauteurAffichage() * (h1 / h));
        emp.setLargeurAffichage(emp.getLargeurAffichage() * (w1 / w));
        this.vuePrincipale.updateCarte();
    }

    public void sepia(EmplacementPhoto emp) {
        if (emp == null || emp.getContenuModifie() == null) return;
        int w = emp.getContenuModifie().getWidth();
        int h = emp.getContenuModifie().getHeight();
        WritableRaster raster = emp.getContenuModifie().getRaster();
        int[] pixels = new int[w * h * 3];
        raster.getPixels(0, 0, w, h, pixels);
        for (int i = 0; i < pixels.length; i += 3) {
            int r = pixels[i];
            int g = pixels[i + 1];
            int b = pixels[i + 2];
            int newR = ((r * 100) >> 8) + ((g * 196) >> 8) + ((b * 48) >> 8);
            newR = newR > 255 ? 255 : newR;
            int newG = ((r * 89) >> 8) + ((g * 175) >> 8) + ((b * 43) >> 8);
            newG = newG > 255 ? 255 : newG;
            int newB = ((r * 69) >> 8) + ((g * 136) >> 8) + ((b * 33) >> 8);
            newB = newB > 255 ? 255 : newB;
            pixels[i] = newR;
            pixels[i + 1] = newG;
            pixels[i + 2] = newB;
        }
        raster.setPixels(0, 0, w, h, pixels);
        this.vuePrincipale.updateCarte();
    }

    public void noirBlanc(EmplacementPhoto emp) {
        BufferedImage tmp;
        tmp = new BufferedImage(emp.getContenuModifie().getWidth(), emp.getContenuModifie().getHeight(), BufferedImage.TYPE_BYTE_GRAY);
        tmp.getGraphics().drawImage(emp.getContenuModifie(), 0, 0, null);
        emp.setContenuModifie(tmp);
        this.vuePrincipale.updateCarte();
    }

    public void luminosite(int val, EmplacementPhoto emp) {
        ParameterBlock pb = new ParameterBlock();
        pb.addSource(emp.getContenuModifie());
        byte lutData[];
        lutData = new byte[256];
        for (int j = 0; j < 256; j++) lutData[j] = clampByte(j + val);
        LookupTableJAI mLookup = new LookupTableJAI(lutData);
        pb.add(mLookup);
        PlanarImage brightImage = JAI.create("lookup", pb, null);
        emp.setContenuModifie(brightImage.getAsBufferedImage());
        this.vuePrincipale.updateCarte();
    }

    public void contraste(int val, EmplacementPhoto emp) {
        ParameterBlock pb = new ParameterBlock();
        pb.addSource(emp.getContenuModifie());
        byte lutData[];
        lutData = new byte[256];
        for (int j = 0; j < 256; j++) lutData[j] = clampByte(j + val);
        LookupTableJAI mLookup = new LookupTableJAI(lutData);
        pb.add(mLookup);
        PlanarImage brightImage = JAI.create("lookup", pb, null);
        emp.setContenuModifie(brightImage.getAsBufferedImage());
        this.vuePrincipale.updateCarte();
    }

    public void contraste2(int intensite, EmplacementPhoto emp) {
        intensite = 100;
        ParameterBlock pb = new ParameterBlock();
        ROI roi = null;
        pb.addSource(emp.getContenuModifie());
        pb.add(roi);
        pb.add(50);
        pb.add(50);
        RenderedOp op = JAI.create("extrema", pb);
        double[][] extrema = (double[][]) op.getProperty("extrema");
        int bands = extrema[0].length;
        double[] map = new double[bands];
        for (int i = 0; i < bands; i++) {
            map[i] = (double) intensite;
        }
        pb = new ParameterBlock();
        pb.addSource(emp.getContenuModifie());
        pb.add(extrema[1]);
        pb.add(extrema[0]);
        pb.add(map);
        PlanarImage pi = JAI.create("threshold", pb);
        emp.setContenuModifie(pi.getAsBufferedImage());
    }

    public void annuler(EmplacementPhoto emp) {
        emp.setContenuModifie(emp.getContenuOriginal());
        this.vuePrincipale.updateCarte();
    }

    public void deplace(String depl) {
        int valX = 0, valY = 0, X = 0, Y = 0;
        height = resultImage.getHeight();
        width = resultImage.getWidth();
        if (depl == "bas") {
            Y = Y + 10;
            valY = 10;
        }
        if (depl == "haut") {
            Y = Y - 10;
            valY = -10;
        }
        if (depl == "gauche") {
            X = X - 10;
            valX = -10;
        }
        if (depl == "droite") {
            X = X + 10;
            valX = 10;
        }
        BufferedImage tmp;
        tmp = new BufferedImage(width + X, height + Y, resultImage.TRANSLUCENT);
        tmp.getGraphics().drawImage(resultImage, X, Y, resultImage.getWidth(), resultImage.getHeight(), null);
        resultImage = tmp;
        this.vuePrincipale.updateCarte();
    }

    private final byte clampByte(int i) {
        if (i > 255) return -1;
        if (i < 0) return 0; else return (byte) i;
    }
}
