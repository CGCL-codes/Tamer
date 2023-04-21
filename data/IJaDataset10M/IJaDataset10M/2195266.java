package org.zmpp.blorb;

import org.zmpp.media.Resolution;
import org.zmpp.media.ZmppImage;

/**
 * This class contains informations related to Blorb images and their
 * scaling. Scaling information is optional and probably only relevant
 * to V6 games. BlorbImage also calculates the correct image size,
 * according to the specification made in the Blorb standard specification.
 * 
 * @author Wei-ju Wu
 * @version 1.5
 */
public class BlorbImage implements ZmppImage {

    /**
   * This class represents a ratio.
   */
    protected static class Ratio {

        private int numerator;

        private int denominator;

        public Ratio(int numerator, int denominator) {
            this.numerator = numerator;
            this.denominator = denominator;
        }

        public int getNumerator() {
            return numerator;
        }

        public int getDenominator() {
            return denominator;
        }

        public float getValue() {
            return (float) numerator / denominator;
        }

        public boolean isDefined() {
            return !(numerator == 0 && denominator == 0);
        }

        @Override
        public String toString() {
            return numerator + "/" + denominator;
        }
    }

    /**
   * This class represents resolution information.
   */
    protected static class ResolutionInfo {

        private Resolution standard;

        private Resolution minimum;

        private Resolution maximum;

        public ResolutionInfo(Resolution std, Resolution min, Resolution max) {
            standard = std;
            minimum = min;
            maximum = max;
        }

        public Resolution getStandard() {
            return standard;
        }

        public Resolution getMinimum() {
            return minimum;
        }

        public Resolution getMaximum() {
            return maximum;
        }

        public float computeERF(int screenwidth, int screenheight) {
            return Math.min(screenwidth / standard.getWidth(), screenheight / standard.getHeight());
        }

        @Override
        public String toString() {
            return "Std: " + standard.toString() + " Min: " + minimum.toString() + " Max: " + maximum.toString();
        }
    }

    protected static class ScaleInfo {

        private ResolutionInfo resolutionInfo;

        private Ratio standard;

        private Ratio minimum;

        private Ratio maximum;

        public ScaleInfo(ResolutionInfo resinfo, Ratio std, Ratio min, Ratio max) {
            this.resolutionInfo = resinfo;
            this.standard = std;
            this.minimum = min;
            this.maximum = max;
        }

        public ResolutionInfo getResolutionInfo() {
            return resolutionInfo;
        }

        public Ratio getStdRatio() {
            return standard;
        }

        public Ratio getMinRatio() {
            return minimum;
        }

        public Ratio getMaxRatio() {
            return maximum;
        }

        public float computeScaleRatio(int screenwidth, int screenheight) {
            float value = resolutionInfo.computeERF(screenwidth, screenheight) * standard.getValue();
            if (minimum.isDefined() && value < minimum.getValue()) {
                value = minimum.getValue();
            }
            if (maximum.isDefined() && value > maximum.getValue()) {
                value = maximum.getValue();
            }
            return value;
        }

        @Override
        public String toString() {
            return String.format("std: %s, min: %s, max: %s\n", standard.toString(), minimum.toString(), maximum.toString());
        }
    }

    private NativeImage image;

    private Resolution resolution;

    private ScaleInfo scaleinfo;

    public BlorbImage(NativeImage image) {
        this.image = image;
    }

    public BlorbImage(int width, int height) {
        resolution = new Resolution(width, height);
    }

    public NativeImage getImage() {
        return image;
    }

    public ScaleInfo getScaleInfo() {
        return scaleinfo;
    }

    /**
   * Returns the size of the image, scaled to the specified screen
   * dimensions
   * @param screenwidth screen width
   * @param screenheight screen height
   * @return the scaled size
   */
    public Resolution getSize(int screenwidth, int screenheight) {
        if (scaleinfo != null) {
            float ratio = scaleinfo.computeScaleRatio(screenwidth, screenheight);
            if (image != null) {
                return new Resolution((int) (image.getWidth() * ratio), (int) (image.getHeight() * ratio));
            } else {
                return new Resolution((int) (resolution.getWidth() * ratio), (int) (resolution.getHeight() * ratio));
            }
        } else {
            if (image != null) {
                return new Resolution(image.getWidth(), image.getHeight());
            } else {
                return new Resolution(resolution.getWidth(), resolution.getHeight());
            }
        }
    }

    protected void setScaleInfo(ScaleInfo scaleinfo) {
        this.scaleinfo = scaleinfo;
    }
}
