package apollo.gui.drawable;

import java.util.Vector;
import java.awt.Rectangle;
import java.awt.Color;
import apollo.datamodel.SeqFeatureI;
import apollo.datamodel.FeatureSetI;
import apollo.datamodel.Exon;
import apollo.datamodel.Transcript;
import apollo.datamodel.AnnotatedFeatureI;
import apollo.config.Config;
import apollo.gui.Transformer;
import apollo.gui.TierManagerI;
import apollo.config.FeatureProperty;
import org.apache.log4j.*;

public class DrawableUtil {

    protected static final Logger logger = LogManager.getLogger(DrawableUtil.class);

    public static Color unconventionalPurple = new Color(160, 32, 240);

    public static void setBoxBounds(Drawable dsf, Transformer transformer, TierManagerI manager) {
        if (dsf instanceof DrawableSetI) {
            int setSize = ((DrawableSetI) dsf).size();
            if (setSize == 0) {
                dsf.setBoxBounds(new Rectangle(0, 0, 0, 0));
                return;
            }
        }
        dsf.setBoxBounds(getPixelBounds(dsf, transformer, manager));
    }

    /**
     This is for creating drag boxes. All the dimensions matter.
     Returns a union of all the drawables bounds in pixels. */
    public static Rectangle getBoxBounds(Vector drawables, Transformer transformer, TierManagerI manager) {
        Rectangle boxBounds = null;
        int drawable_count = drawables.size();
        for (int i = 0; i < drawable_count; i++) {
            Drawable dsf = (Drawable) drawables.elementAt(i);
            Rectangle dsfPixels = getPixelBounds(dsf, transformer, manager);
            if (boxBounds == null) {
                boxBounds = dsfPixels;
            } else {
                boxBounds = boxBounds.union(dsfPixels);
            }
        }
        if (boxBounds == null) {
            return new Rectangle(0, 0, 0, 0);
        }
        return boxBounds;
    }

    private static Rectangle getPixelBounds(Drawable dsf, Transformer transformer, TierManagerI manager) {
        SeqFeatureI sf = dsf.getFeature();
        FeatureProperty fp = dsf.getFeatureProperty();
        Transformer.PixelRange pixRng = transformer.basepairRangeToPixelRange(sf);
        pixRng.ensureMinimumWidth(fp.getMinWidth());
        int pixelHeight = dsf.getSize(transformer, manager);
        int pixelY = getPixelY(dsf, transformer, manager, pixelHeight);
        return new Rectangle(pixRng.low, pixelY, pixRng.getWidth(), pixelHeight);
    }

    /** Return y value for middle of tier, not feature. 
      This may not be the middle of the feature. There is one midpoint 
      for the tier, but features of different widths have different middles.
      Need to use tier midpoint to ensure intron lines
      collapse to the same line when tier is collapsed */
    public static int getTierYCentrePixel(int tierNum, Transformer transformer, TierManagerI manager) {
        int tierCentre = manager.getTier(tierNum).getDrawCentre();
        return transformer.toPixelY(tierCentre);
    }

    private static int getPixelY(Drawable dsf, Transformer transformer, TierManagerI manager, int pixelHeight) {
        int pixelY = getTierYCentrePixel(dsf.getTierIndex(manager), transformer, manager);
        pixelY -= (int) ((double) pixelHeight * 0.5);
        return pixelY;
    }

    /** Creates and returns a DrawableFeatureSet from a FeatureSetI.
      Changed return type from FeatureSetI to DrawableFeatureSet */
    public static Drawable createDrawable(SeqFeatureI sf) {
        FeatureProperty fp = Config.getPropertyScheme().getFeatureProperty(sf.getTopLevelType());
        Drawable glyph = null;
        if (fp != null) {
            String glyph_name = fp.getStyle();
            if (glyph_name != null) {
                Drawable check_glyph = createGlyph(glyph_name);
                if (isOneLevel(fp) && fp.isAnnotationType()) check_glyph = setToOneLevelGlyph(check_glyph);
                int feature_level = sf.numberOfGenerations();
                if (check_glyph != null) {
                    if (feature_level == check_glyph.getDrawLevel()) {
                        glyph = check_glyph;
                    } else {
                        logger.debug("Can't create glyph, feat level " + feature_level + " !=" + " draw level " + check_glyph.getDrawLevel() + " " + glyph_name);
                    }
                } else {
                    logger.error("Unable to create glyph for " + glyph_name);
                }
            }
        }
        if (glyph == null) {
            if (sf.canHaveChildren()) {
                glyph = new DrawableFeatureSet(false);
            } else {
                if (sf.getFeatureType().contains("primer")) {
                    glyph = new DrawableHalfArrow(false);
                } else {
                    glyph = new DrawableSeqFeature(false);
                }
            }
        }
        glyph.setFeature(sf);
        return (glyph);
    }

    private static boolean isOneLevel(FeatureProperty fp) {
        return fp.getNumberOfLevels() == 1;
    }

    private static Drawable setToOneLevelGlyph(Drawable glyph) {
        if (glyph.getDrawLevel() == 1) return glyph;
        boolean drawn = true;
        return new DrawableSeqFeature(drawn);
    }

    /** We may want to config this for developers who want to put shapes 
    in a separate directory (may want list of paths) but for now this 
    is fine */
    private static final String drawablePath = "apollo.gui.drawable.";

    /** Glyph name may be whole path (apollo.gui.drawable.Zigzag),
	just the class name(Zigzag) */
    public static Drawable createGlyph(String glyph_name) {
        Drawable glyph = null;
        int tries = 0;
        boolean no_class = true;
        String prefix = drawablePath;
        String class_name = glyph_name;
        while (glyph == null && tries < 2) {
            try {
                if (prefix != "" && !(glyph_name.startsWith(prefix))) class_name = prefix + glyph_name;
                Class glyph_class = Class.forName(class_name);
                no_class = (glyph_class == null);
                glyph = (Drawable) glyph_class.newInstance();
            } catch (Exception e) {
                logger.error("createGlyph: couldn't create glyph " + class_name + "; no class = " + no_class, e);
                tries++;
                if (tries < 2) prefix = "";
            }
        }
        return glyph;
    }

    public static Color getStartCodonColor(FeatureSetI fs) {
        return ((fs.unConventionalStart() || fs.getStartCodon() == null) ? unconventionalPurple : Color.green);
    }

    public static Color getFeatureColor(SeqFeatureI feature, FeatureProperty fp) {
        Color color;
        if (feature != null) {
            boolean colorAllAnnotsByOwner = Config.getStyle().colorAllAnnotsByOwner();
            if ((colorAllAnnotsByOwner && feature instanceof AnnotatedFeatureI) || (((feature instanceof Transcript) || (feature instanceof Exon)) && (feature.getTopLevelType().equalsIgnoreCase("gene")))) {
                AnnotatedFeatureI annot;
                if (feature instanceof Exon) annot = (AnnotatedFeatureI) feature.getRefFeature(); else annot = (AnnotatedFeatureI) feature;
                String owner = Config.getProjectName(annot.getOwner());
                if (Config.getStyle().showIsProbCheckbox() && annot.isProblematic() && fp.getProblematicColor() != null) color = fp.getProblematicColor(); else if (Config.getStyle().showFinishedCheckbox() && annot.isFinished() && fp.getFinishedColor() != null) color = fp.getFinishedColor(); else color = Config.getAnnotationColor(owner, fp);
            } else color = fp.getColour();
        } else color = FeatureProperty.DEFAULT_COLOR;
        return color;
    }
}
