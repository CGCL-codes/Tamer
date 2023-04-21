package com.peralex.utilities.ui.graphs.graphBase;

/**
 *
 * @author  Roy Emmerich
 */
public final class PixelUnitConverter {

    /** Not meant to be instantiated */
    private PixelUnitConverter() {
    }

    /**
   *  Convert a unit value to the equivalent pixel value
   *
   * @param proportional If the unit axis is inversely proportional to the pixel axis then
   *                       this must be false (Inversely proportional: units get smaller as pixels get bigger)
   * @param unitValue The unit value to be converted to a pixel equivalent
   * @param minPixelValue This should be 0 or above but less than maxPixelValue
   * @param maxPixelValue This should be either the current width or height of the area concerned
   * @param minUnitValue If directly proportional, the minUnitValue maps to the minPixelValue otherwise
   *                       it should map to the maxPixelValue
   * @return The required pixel value
   */
    public static int unitToPixel(boolean proportional, double unitValue, int minPixelValue, int maxPixelValue, double minUnitValue, double maxUnitValue) {
        final double yIntercept;
        final double slope;
        if (proportional) {
            slope = (maxPixelValue - minPixelValue) / (maxUnitValue - minUnitValue);
            yIntercept = minPixelValue - slope * (minUnitValue);
        } else {
            yIntercept = maxUnitValue * (maxPixelValue / (maxUnitValue - minUnitValue));
            slope = -1.0 * (maxPixelValue - minPixelValue) / (maxUnitValue - minUnitValue);
        }
        return (int) Math.round((slope * unitValue) + yIntercept);
    }

    /** 
   * Convert a pixel value to the equivalent unit value
   *
   * @param proportional If the unit axis is inversely proportional to the pixel axis then
   *                       this must be false (Inversely proportional: units get smaller as pixels get bigger)
   * @param pixelValue The pixel value to be converted to a unit equivalent
   * @param minPixelValue This should be 0 or above but less than maxPixelValue
   * @param maxPixelValue This should be either the current width or height of the area concerned
   * @param minUnitValue If directly proportional, the minUnitValue maps to the minPixelValue otherwise
   *                       it should map to the maxPixelValue
   * @return The required unit value
   */
    public static double pixelToUnit(boolean proportional, int pixelValue, int minPixelValue, int maxPixelValue, double minUnitValue, double maxUnitValue) {
        final double yIntercept;
        final double slope;
        if (proportional) {
            yIntercept = minUnitValue;
            slope = (maxUnitValue - minUnitValue) / (maxPixelValue - minPixelValue);
        } else {
            yIntercept = maxUnitValue;
            slope = -1.0 * (maxUnitValue - minUnitValue) / (maxPixelValue - minPixelValue);
        }
        return (slope * pixelValue) + yIntercept;
    }

    /**
   * Reduces the amount of computation required during the CPU-intensive part of drawing.
   * Uses double fields for computation.
   */
    public static final class UnitToPixel {

        private final double yIntercept;

        private final double slope;

        public UnitToPixel(boolean proportional, int minPixelValue, int maxPixelValue, double minUnitValue, double maxUnitValue) {
            if (proportional) {
                slope = (maxPixelValue - minPixelValue) / (maxUnitValue - minUnitValue);
                yIntercept = minPixelValue - slope * (minUnitValue);
            } else {
                yIntercept = maxUnitValue * (maxPixelValue / (maxUnitValue - minUnitValue));
                slope = -(maxPixelValue - minPixelValue) / (maxUnitValue - minUnitValue);
            }
        }

        public int compute(double unitValue) {
            return (int) ((slope * unitValue) + yIntercept);
        }
    }

    /**
   * Reduces the amount of computation required during the CPU-intensive part of drawing.
   * Uses float fields for computation.
   */
    public static final class UnitToPixel2 {

        private final float yIntercept;

        private final float slope;

        public UnitToPixel2(boolean proportional, int minPixelValue, int maxPixelValue, float minUnitValue, float maxUnitValue) {
            if (proportional) {
                slope = (maxPixelValue - minPixelValue) / (maxUnitValue - minUnitValue);
                yIntercept = minPixelValue - slope * (minUnitValue);
            } else {
                yIntercept = maxUnitValue * (maxPixelValue / (maxUnitValue - minUnitValue));
                slope = -(maxPixelValue - minPixelValue) / (maxUnitValue - minUnitValue);
            }
        }

        public int compute(float unitValue) {
            return (int) ((slope * unitValue) + yIntercept);
        }
    }
}
