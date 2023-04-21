package auo.cms.prefercolor;

import shu.cms.colorspace.independ.*;
import shu.cms.colorspace.depend.*;
import shu.cms.Illuminant;
import shu.cms.hvs.cam.ChromaticAdaptation;
import shu.cms.hvs.cam.CAMConst;
import shu.cms.*;
import java.util.*;
import shu.cms.plot.*;
import shu.math.array.*;

/**
 * <p>Title: Colour Management System</p>
 *
 * <p>Description: a Colour Management System by Java</p>
 *
 * <p>Copyright: Copyright (c) 2009</p>
 *
 * <p>Company: skygroup</p>
 *
 * @author skyforce
 * @version 1.0
 */
public class MemoryColorPatches {

    public static final class CMYMemoryColor {

        public static final RGB getRGB(double[] cmyValeus) {
            double r = 100 - cmyValeus[0];
            double g = 100 - cmyValeus[1];
            double b = 100 - cmyValeus[2];
            RGB rgb = new RGB(RGB.ColorSpace.sRGB, new double[] { r, g, b }, RGB.MaxValue.Double100);
            return rgb;
        }

        public static double[] Gold = new double[] { 5, 15, 65 };

        public static double[] Silver = new double[] { 20, 15, 14 };

        public static double[] Half = new double[] { 5, 15, 15 };

        public static double[] Highlight = new double[] { 5, 3, 3 };

        public static double[] Ocean = new double[] { 60, 0, 25 };

        public static double[] Green = new double[] { 100, 0, 100 };

        public static double[] Lemon = new double[] { 5, 18, 75 };
    }

    public static final MemoryColorInterface getOrientalInstance() {
        return new MemoryColorInterface() {

            public CIEXYZ getReferenceWhiteXYZ() {
                return ReferenceWhiteXYZ;
            }

            ;

            public CIELab getSkin() {
                return OrientalSkin;
            }

            ;

            public CIELab getSky() {
                return BlueSky_;
            }

            ;

            public CIELab getGrass() {
                return GreenGrass;
            }

            ;

            public CIELab getFoliage() {
                return DeciduousFoliage;
            }

            ;

            public CIELab getOrange() {
                return Orange;
            }

            ;

            public CIELab getBanana() {
                return Banana;
            }
        };
    }

    public static final MemoryColorInterface getCaucasianInstance() {
        return new MemoryColorInterface() {

            public CIEXYZ getReferenceWhiteXYZ() {
                return ReferenceWhiteXYZ;
            }

            ;

            public CIELab getSkin() {
                return CaucasianSkin;
            }

            ;

            public CIELab getSky() {
                return BlueSky_;
            }

            ;

            public CIELab getGrass() {
                return GreenGrass;
            }

            ;

            public CIELab getFoliage() {
                return DeciduousFoliage;
            }

            ;

            public CIELab getOrange() {
                return Orange;
            }

            ;

            public CIELab getBanana() {
                return Banana;
            }
        };
    }

    private static final CIEXYZ ReferenceWhiteXYZ = Illuminant.C.getNormalizeXYZ();

    public static final CIELab CaucasianSkin = new CIELab(79.5, 16.1, 10.4, ReferenceWhiteXYZ);

    public static final CIELab BlueSky = new CIELab(54, -17, -28.1, ReferenceWhiteXYZ);

    public static final CIELab BlueSky_ = new CIELab(54, -16.798023, -27.766144, ReferenceWhiteXYZ);

    public static final CIELab GreenGrass = new CIELab(50., -33.7, 29.8, ReferenceWhiteXYZ);

    public static final CIELab OrientalSkin = new CIELab(63.9, 14, 16.1, ReferenceWhiteXYZ);

    public static final CIELab DeciduousFoliage = new CIELab(33.6, -18.5, 12.8, ReferenceWhiteXYZ);

    public static final CIELab Orange = new CIELab(71.6, 26.7, 75.72, ReferenceWhiteXYZ);

    public static final CIELab Banana = new CIELab(70, 0, 65, ReferenceWhiteXYZ);

    public static class Hungarian implements MemoryColorInterface {

        private static Hungarian hungarian;

        public static MemoryColorInterface getInstance() {
            if (null == hungarian) {
                hungarian = new Hungarian();
            }
            return hungarian;
        }

        public CIEXYZ getReferenceWhiteXYZ() {
            return ReferenceWhiteXYZ;
        }

        ;

        public CIELab getSkin() {
            return Skin;
        }

        ;

        public CIELab getSky() {
            return Sky;
        }

        ;

        public CIELab getGrass() {
            return Grass;
        }

        ;

        public CIELab getFoliage() {
            return Foliage;
        }

        ;

        public CIELab getOrange() {
            return Orange;
        }

        ;

        public CIELab getBanana() {
            return Banana;
        }

        private static final CIEXYZ ReferenceWhiteXYZ = Illuminant.D65WhitePoint;

        public static final CIELab Skin = new CIELab(81.8, 10.5, 19.3, ReferenceWhiteXYZ);

        public static final CIELab Sky = new CIELab(65.7, -10.4, -28.9, ReferenceWhiteXYZ);

        public static final CIELab Grass = new CIELab(45.5, -40.4, 32.2, ReferenceWhiteXYZ);

        public static final CIELab Foliage = new CIELab(43.2, -39.1, 30.0, ReferenceWhiteXYZ);

        public static final CIELab Orange = new CIELab(68.2, 27.7, 62.9, ReferenceWhiteXYZ);

        public static final CIELab Banana = new CIELab(84.1, -10.6, 66.7, ReferenceWhiteXYZ);
    }

    public static class Korean implements MemoryColorInterface {

        private static Korean korean;

        public static MemoryColorInterface getInstance() {
            if (null == korean) {
                korean = new Korean();
            }
            return korean;
        }

        public CIEXYZ getReferenceWhiteXYZ() {
            return ReferenceWhiteXYZ;
        }

        ;

        public CIELab getSkin() {
            return Skin;
        }

        ;

        public CIELab getSky() {
            return Sky;
        }

        ;

        public CIELab getGrass() {
            return Grass;
        }

        ;

        public CIELab getFoliage() {
            return Foliage;
        }

        ;

        public CIELab getOrange() {
            return Orange;
        }

        ;

        public CIELab getBanana() {
            return Banana;
        }

        private static final CIEXYZ ReferenceWhiteXYZ = Illuminant.D65WhitePoint;

        public static final CIELab Skin = new CIELab(73.1, 8.6, 20.3, ReferenceWhiteXYZ);

        public static final CIELab Sky = new CIELab(62.6, -10, -35.4, ReferenceWhiteXYZ);

        public static final CIELab Grass = new CIELab(55.6, -45.7, 35.1, ReferenceWhiteXYZ);

        public static final CIELab Foliage = new CIELab(40.5, -33.9, 21.4, ReferenceWhiteXYZ);

        public static final CIELab Orange = new CIELab(72.6, 24.5, 69.1, ReferenceWhiteXYZ);

        public static final CIELab Banana = new CIELab(86.6, -10.5, 69.1, ReferenceWhiteXYZ);
    }

    public static final RGB getsRGB(CIELab memoryColor) {
        return getPatch("", memoryColor).getRGB();
    }

    public static final List<Patch> getAllsRGBPatchList() {
        List<Patch> patchList = new ArrayList<Patch>();
        patchList.add(getPatch("BlueSky_", BlueSky_));
        patchList.add(getPatch("CaucasianSkin", CaucasianSkin));
        patchList.add(getPatch("DeciduousFoliage", DeciduousFoliage));
        patchList.add(getPatch("GreenGrass", GreenGrass));
        patchList.add(getPatch("Orange", Orange));
        patchList.add(getPatch("OrientalSkin", OrientalSkin));
        return patchList;
    }

    private static final Patch getPatch(String name, CIELab memoryColor) {
        CIEXYZ XYZ = memoryColor.toXYZ();
        ChromaticAdaptation ca = new ChromaticAdaptation(memoryColor.getWhite(), RGB.ColorSpace.sRGB.getReferenceWhiteXYZ(), CAMConst.CATType.Bradford);
        CIEXYZ sRGBXYZ = ca.getDestinationColor(XYZ);
        RGB sRGB = new RGB(RGB.ColorSpace.sRGB, sRGBXYZ);
        sRGB.rationalize();
        Patch patch = new Patch(name, sRGBXYZ, memoryColor, sRGB);
        return patch;
    }

    private static final void drawVector(Plot3D plot3D, java.awt.Color color, CIELab cie, CIELab prefered, boolean chromaticAdaptation) {
        double[] original = null, modified = null;
        if (chromaticAdaptation) {
            ChromaticAdaptation ca = new ChromaticAdaptation(cie.getWhite(), prefered.getWhite(), CAMConst.CATType.Bradford);
            ca.getDestinationColor(cie).getabLValues();
            original = ca.getDestinationColor(cie).getabLValues();
            modified = ca.getDestinationColor(prefered).getabLValues();
        } else {
            original = cie.getabLValues();
            modified = prefered.getabLValues();
        }
        double[] vector = DoubleArray.minus(modified, original);
        plot3D.addVectortoPlot("", color, original, vector);
        plot3D.setAxisLabels("a*", "b*", "L*");
        plot3D.setFixedBounds(0, -128, 128);
        plot3D.setFixedBounds(1, -128, 128);
        plot3D.setFixedBounds(2, 0, 100);
    }

    public static void main(String[] args) {
        cmyTest(args);
    }

    public static void cmyTest(String[] args) {
        RGB gold = CMYMemoryColor.getRGB(CMYMemoryColor.Gold);
        RGB green = CMYMemoryColor.getRGB(CMYMemoryColor.Green);
        RGB half = CMYMemoryColor.getRGB(CMYMemoryColor.Half);
        RGB highlight = CMYMemoryColor.getRGB(CMYMemoryColor.Highlight);
        RGB lemon = CMYMemoryColor.getRGB(CMYMemoryColor.Lemon);
        RGB ocean = CMYMemoryColor.getRGB(CMYMemoryColor.Ocean);
        RGB silver = CMYMemoryColor.getRGB(CMYMemoryColor.Silver);
        Plot2D plot = Plot2D.getInstance();
        CIEXYZ whiteXYZ = RGB.toXYZ(RGB.White, RGB.ColorSpace.sRGB);
        plot.addScatterPlot("gold", gold.getColor(), new CIELab(gold.toXYZ(), whiteXYZ).getabValues());
        plot.addScatterPlot("green", green.getColor(), new CIELab(green.toXYZ(), whiteXYZ).getabValues());
        plot.addScatterPlot("half", half.getColor(), new CIELab(half.toXYZ(), whiteXYZ).getabValues());
        plot.addScatterPlot("highlight", highlight.getColor(), new CIELab(highlight.toXYZ(), whiteXYZ).getabValues());
        plot.addScatterPlot("lemon", lemon.getColor(), new CIELab(lemon.toXYZ(), whiteXYZ).getabValues());
        plot.addScatterPlot("ocean", ocean.getColor(), new CIELab(ocean.toXYZ(), whiteXYZ).getabValues());
        plot.addScatterPlot("silver", silver.getColor(), new CIELab(silver.toXYZ(), whiteXYZ).getabValues());
        plot.setVisible();
        for (int x = 0; x < plot.getPlotSize(); x++) {
            plot.setDotRadius(x, 4);
        }
    }

    public static void memoryTest(String[] args) {
        MemoryColorInterface ciestandard = MemoryColorPatches.getOrientalInstance();
        MemoryColorInterface prefered = MemoryColorPatches.Korean.getInstance();
        Plot3D plot = Plot3D.getInstance();
        plot.setVisible();
        boolean chromaticAdaptation = true;
        drawVector(plot, getsRGB(ciestandard.getFoliage()).getColor(), ciestandard.getFoliage(), prefered.getFoliage(), chromaticAdaptation);
        drawVector(plot, getsRGB(ciestandard.getGrass()).getColor(), ciestandard.getGrass(), prefered.getGrass(), chromaticAdaptation);
        drawVector(plot, getsRGB(ciestandard.getOrange()).getColor(), ciestandard.getOrange(), prefered.getOrange(), chromaticAdaptation);
        drawVector(plot, getsRGB(ciestandard.getSkin()).getColor(), ciestandard.getSkin(), prefered.getSkin(), chromaticAdaptation);
        drawVector(plot, getsRGB(ciestandard.getSky()).getColor(), ciestandard.getSky(), prefered.getSky(), chromaticAdaptation);
        plot.rotateToAxis(2);
    }
}
