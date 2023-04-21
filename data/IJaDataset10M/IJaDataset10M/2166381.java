package com.stromberglabs.jopensurf;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import javax.imageio.ImageIO;

/**
 * A class to calculate the upright or free oriented interest points of an image lazily (will not calculate until you ask for them)
 * @author astromberg
 *
 */
public class Surf implements Serializable {

    private static final long serialVersionUID = 1L;

    private static final int HESSIAN_OCTAVES = 5;

    private static final int HESSIAN_INIT_SAMPLE = 2;

    private static final float HESSIAN_THRESHOLD = 0.0004F;

    private static final float HESSIAN_BALANCE_VALUE = 0.81F;

    private transient BufferedImage mOriginalImage;

    private FastHessian mHessian;

    private List<SURFInterestPoint> mFreeOrientedPoints;

    private List<SURFInterestPoint> mUprightPoints;

    private List<SURFInterestPoint> mDescriptorFreeInterestPoints;

    private int mNumOctaves = HESSIAN_OCTAVES;

    private float mThreshold = HESSIAN_THRESHOLD;

    private float mBalanceValue = HESSIAN_BALANCE_VALUE;

    private IntegralImage mIntegralImage;

    public Surf(BufferedImage image) {
        this(image, HESSIAN_BALANCE_VALUE, HESSIAN_THRESHOLD, HESSIAN_OCTAVES);
    }

    public Surf(BufferedImage image, float balanceValue, float threshold, int octaves) {
        mOriginalImage = image;
        mNumOctaves = octaves;
        mBalanceValue = balanceValue;
        mThreshold = threshold;
        mIntegralImage = new IntegralImage(mOriginalImage);
        mHessian = new FastHessian(mIntegralImage, mNumOctaves, HESSIAN_INIT_SAMPLE, mThreshold, mBalanceValue);
        mDescriptorFreeInterestPoints = mHessian.getIPoints();
    }

    public List<SURFInterestPoint> getUprightInterestPoints() {
        return getPoints(true);
    }

    public List<SURFInterestPoint> getFreeOrientedInterestPoints() {
        return getPoints(false);
    }

    private List<SURFInterestPoint> getPoints(boolean upright) {
        List<SURFInterestPoint> points = upright ? mUprightPoints : mFreeOrientedPoints;
        if (points == null) {
            points = getDescriptorFreeInterestPoints();
            if (upright) {
                mUprightPoints = points;
            } else {
                mFreeOrientedPoints = points;
            }
            for (SURFInterestPoint point : points) {
                getOrientation(point);
                getMDescriptor(point, upright);
            }
        }
        return points;
    }

    private List<SURFInterestPoint> getDescriptorFreeInterestPoints() {
        List<SURFInterestPoint> points = new ArrayList<SURFInterestPoint>(mDescriptorFreeInterestPoints.size());
        for (SURFInterestPoint point : mDescriptorFreeInterestPoints) {
            try {
                points.add((SURFInterestPoint) point.clone());
            } catch (CloneNotSupportedException e) {
                e.printStackTrace();
            }
        }
        return points;
    }

    private float haarX(int row, int column, int s) {
        return ImageTransformUtils.BoxIntegral(mIntegralImage, row - s / 2, column, s, s / 2) - 1 * ImageTransformUtils.BoxIntegral(mIntegralImage, row - s / 2, column - s / 2, s, s / 2);
    }

    private float haarY(int row, int column, int s) {
        return ImageTransformUtils.BoxIntegral(mIntegralImage, row, column - s / 2, s / 2, s) - 1 * ImageTransformUtils.BoxIntegral(mIntegralImage, row - s / 2, column - s / 2, s / 2, s);
    }

    private void getOrientation(SURFInterestPoint input) {
        double gauss;
        float scale = input.getScale();
        int s = (int) Math.round(scale);
        int r = (int) Math.round(input.getY());
        int c = (int) Math.round(input.getX());
        List<Double> xHaarResponses = new ArrayList<Double>();
        List<Double> yHaarResponses = new ArrayList<Double>();
        List<Double> angles = new ArrayList<Double>();
        for (int i = -6; i <= 6; ++i) {
            for (int j = -6; j <= 6; ++j) {
                if (i * i + j * j < 36) {
                    gauss = GaussianConstants.Gauss25[Math.abs(i)][Math.abs(j)];
                    double xHaarResponse = gauss * haarX(r + j * s, c + i * s, 4 * s);
                    double yHaarResponse = gauss * haarY(r + j * s, c + i * s, 4 * s);
                    xHaarResponses.add(xHaarResponse);
                    yHaarResponses.add(yHaarResponse);
                    angles.add(getAngle(xHaarResponse, yHaarResponse));
                }
            }
        }
        float sumX = 0, sumY = 0;
        float ang1, ang2, ang;
        float max = 0;
        float orientation = 0;
        for (ang1 = 0; ang1 < 2 * Math.PI; ang1 += 0.15f) {
            ang2 = (float) (ang1 + Math.PI / 3.0f > 2 * Math.PI ? ang1 - 5.0f * Math.PI / 3.0f : ang1 + Math.PI / 3.0f);
            sumX = sumY = 0;
            for (int k = 0; k < angles.size(); k++) {
                ang = angles.get(k).floatValue();
                if (ang1 < ang2 && ang1 < ang && ang < ang2) {
                    sumX += xHaarResponses.get(k).floatValue();
                    sumY += yHaarResponses.get(k).floatValue();
                } else if (ang2 < ang1 && ((ang > 0 && ang < ang2) || (ang > ang1 && ang < 2 * Math.PI))) {
                    sumX += xHaarResponses.get(k).floatValue();
                    sumY += yHaarResponses.get(k).floatValue();
                }
            }
            if (sumX * sumX + sumY * sumY > max) {
                max = sumX * sumX + sumY * sumY;
                orientation = (float) getAngle(sumX, sumY);
            }
        }
        input.setOrientation(orientation);
    }

    private void getMDescriptor(SURFInterestPoint point, boolean upright) {
        int y, x, count = 0;
        int sample_x, sample_y;
        double scale, dx, dy, mdx, mdy, co = 1F, si = 0F;
        float desc[] = new float[64];
        double gauss_s1 = 0.0D, gauss_s2 = 0.0D, xs = 0.0D, ys = 0.0D;
        double rx = 0.0D, ry = 0.0D, rrx = 0.0D, rry = 0.0D, len = 0.0D;
        int i = 0, ix = 0, j = 0, jx = 0;
        float cx = -0.5f, cy = 0.0f;
        scale = point.getScale();
        x = Math.round(point.getX());
        y = Math.round(point.getY());
        if (!upright) {
            co = Math.cos(point.getOrientation());
            si = Math.sin(point.getOrientation());
        }
        i = -8;
        while (i < 12) {
            j = -8;
            i = i - 4;
            cx += 1.0F;
            cy = -0.5F;
            while (j < 12) {
                dx = dy = mdx = mdy = 0.0F;
                cy += 1.0F;
                j = j - 4;
                ix = i + 5;
                jx = j + 5;
                xs = Math.round(x + (-jx * scale * si + ix * scale * co));
                ys = Math.round(y + (jx * scale * co + ix * scale * si));
                for (int k = i; k < i + 9; ++k) {
                    for (int l = j; l < j + 9; ++l) {
                        sample_x = (int) Math.round(x + (-1D * l * scale * si + k * scale * co));
                        sample_y = (int) Math.round(y + (l * scale * co + k * scale * si));
                        gauss_s1 = gaussian(xs - sample_x, ys - sample_y, 2.5F * scale);
                        rx = haarX(sample_y, sample_x, (int) (2 * Math.round(scale)));
                        ry = haarY(sample_y, sample_x, (int) (2 * Math.round(scale)));
                        rrx = gauss_s1 * (-rx * si + ry * co);
                        rry = gauss_s1 * (rx * co + ry * si);
                        dx += rrx;
                        dy += rry;
                        mdx += Math.abs(rrx);
                        mdy += Math.abs(rry);
                    }
                }
                gauss_s2 = gaussian(cx - 2.0f, cy - 2.0f, 1.5f);
                desc[count++] = (float) (dx * gauss_s2);
                desc[count++] = (float) (dy * gauss_s2);
                desc[count++] = (float) (mdx * gauss_s2);
                desc[count++] = (float) (mdy * gauss_s2);
                len += (dx * dx + dy * dy + mdx * mdx + mdy * mdy) * (gauss_s2 * gauss_s2);
                j += 9;
            }
            i += 9;
        }
        len = Math.sqrt(len);
        for (i = 0; i < 64; i++) desc[i] /= len;
        point.setDescriptor(desc);
    }

    private double getAngle(double xHaarResponse, double yHaarResponse) {
        if (xHaarResponse >= 0 && yHaarResponse >= 0) return Math.atan(yHaarResponse / xHaarResponse);
        if (xHaarResponse < 0 && yHaarResponse >= 0) return Math.PI - Math.atan(-yHaarResponse / xHaarResponse);
        if (xHaarResponse < 0 && yHaarResponse < 0) return Math.PI + Math.atan(yHaarResponse / xHaarResponse);
        if (xHaarResponse >= 0 && yHaarResponse < 0) return 2 * Math.PI - Math.atan(-yHaarResponse / xHaarResponse);
        return 0;
    }

    public Map<SURFInterestPoint, SURFInterestPoint> getMatchingPoints(Surf descriptor, boolean upright) {
        System.out.println("Finding matching points..");
        Map<SURFInterestPoint, SURFInterestPoint> matchingPoints = new HashMap<SURFInterestPoint, SURFInterestPoint>();
        List<SURFInterestPoint> points = upright ? getUprightInterestPoints() : getFreeOrientedInterestPoints();
        for (SURFInterestPoint a : points) {
            double smallestDistance = Float.MAX_VALUE;
            double nextSmallestDistance = Float.MAX_VALUE;
            SURFInterestPoint possibleMatch = null;
            for (SURFInterestPoint b : upright ? descriptor.getUprightInterestPoints() : descriptor.getFreeOrientedInterestPoints()) {
                double distance = a.getDistance(b);
                if (distance < smallestDistance) {
                    nextSmallestDistance = smallestDistance;
                    smallestDistance = distance;
                    possibleMatch = b;
                } else if (distance < nextSmallestDistance) {
                    nextSmallestDistance = distance;
                }
            }
            if (smallestDistance / nextSmallestDistance < 0.75d) {
                matchingPoints.put(a, possibleMatch);
            }
        }
        System.out.println("Done finding matching points");
        return matchingPoints;
    }

    public String getStringRepresentation(boolean freeOriented) {
        StringBuffer buffer = new StringBuffer();
        for (SURFInterestPoint point : freeOriented ? getFreeOrientedInterestPoints() : getUprightInterestPoints()) {
            for (double val : point.getDescriptor()) {
                buffer.append(Double.doubleToLongBits(val) + ",");
            }
        }
        buffer.substring(0, buffer.length() - 1);
        return buffer.toString();
    }

    public void setStringRepresentation(String str) {
    }

    private double gaussian(double x, double y, double sig) {
        return (1.0f / (2.0f * Math.PI * sig * sig)) * Math.exp(-(x * x + y * y) / (2.0f * sig * sig));
    }

    public boolean isEquivalentTo(Surf surf) {
        List<SURFInterestPoint> pointsA = surf.getFreeOrientedInterestPoints();
        List<SURFInterestPoint> pointsB = getFreeOrientedInterestPoints();
        if (pointsA.size() != pointsB.size()) return false;
        for (int i = 0; i < pointsA.size(); i++) {
            SURFInterestPoint pointA = pointsA.get(i);
            SURFInterestPoint pointB = pointsB.get(i);
            if (!pointA.isEquivalentTo(pointB)) return false;
        }
        return true;
    }

    public static void saveToFile(Surf surf, String file) {
        try {
            ObjectOutputStream stream = new ObjectOutputStream(new FileOutputStream(file));
            stream.writeObject(surf);
            stream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Surf readFromFile(String location) {
        File file = new File(location);
        if (file != null && file.exists()) {
            try {
                ObjectInputStream str = new ObjectInputStream(new FileInputStream(file));
                return (Surf) str.readObject();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    private void writeObject(ObjectOutputStream out) throws IOException {
        if (mFreeOrientedPoints == null) getFreeOrientedInterestPoints();
        if (mUprightPoints == null) getUprightInterestPoints();
        out.defaultWriteObject();
    }

    public static void main(String args[]) {
        try {
            BufferedImage image = ImageIO.read(new File("H:\\workspace\\javaopensurf\\example\\lenna.png"));
            Surf board = new Surf(image);
            saveToFile(board, "C:\\surf_test.bin");
            Surf boarder = readFromFile("C:\\surf_test.bin");
            List<SURFInterestPoint> points = boarder.getFreeOrientedInterestPoints();
            System.out.println("Found " + points.size() + " interest points");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
