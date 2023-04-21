package backend;

import java.math.BigDecimal;
import java.math.MathContext;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Container for indexed Images which hold the statistical information of the 
 * Image.
 * 
 * @author Rajmund Witt
 * @version 0.5
 * 
 * @see LogWriter
 * @see SystemInformation
 */
public class CategorizedImage implements Comparable {

    private static LogWriter log_ = LogWriter.getInstance();

    private static Case case_ = Case.getInstance();

    private String canonicalPath_;

    private String canonicalDirectory_;

    private String name_;

    private int width_;

    private int height_;

    private long lastModified_;

    private boolean hasSkinTone_;

    private boolean isProcessedCorrectly_;

    private int numberOfPixels_;

    private int numberOfRgbSkinToneHits_;

    private int numberOfYCbCrSkinToneHits_;

    private double preciseRgbPercentage_;

    private double preciseYCbCrPercentage_;

    private double preciseAveragePercentage_;

    private int readableRgbPercentage_;

    private int readableYCbCrPercentage_;

    private int readableAveragePercentage_;

    private String md5_;

    private String sha1_;

    private CategorizedImage() {
    }

    public CategorizedImage(String canonicalPath, String name, int width, int height, long lastModified) {
        canonicalPath_ = canonicalPath;
        name_ = name;
        lastModified_ = lastModified;
        if ((width > 0 && width < 100000) && (height > 0 && height < 100000)) {
            width_ = width;
            height_ = height;
            numberOfPixels_ = width_ * height_;
        } else {
            isProcessedCorrectly_ = false;
            width_ = 100;
            height_ = 100;
            numberOfPixels_ = 10000;
        }
        hasSkinTone_ = false;
        isProcessedCorrectly_ = true;
        numberOfRgbSkinToneHits_ = 0;
        numberOfYCbCrSkinToneHits_ = 0;
        preciseRgbPercentage_ = 0.0;
        preciseYCbCrPercentage_ = 0.0;
        preciseAveragePercentage_ = 0.0;
        readableRgbPercentage_ = 0;
        readableYCbCrPercentage_ = 0;
        readableAveragePercentage_ = 0;
        md5_ = null;
        sha1_ = null;
    }

    public String getCanonicalPath() {
        return canonicalPath_;
    }

    public String getCanonicalDirectory() {
        return canonicalDirectory_;
    }

    public String getName() {
        return name_;
    }

    public int getWidth() {
        return width_;
    }

    public int getHeight() {
        return height_;
    }

    public long getLastModified() {
        return lastModified_;
    }

    public boolean getHasSkinTone() {
        return hasSkinTone_;
    }

    public boolean getIsProcessedCorrectly() {
        return isProcessedCorrectly_;
    }

    public int getNumberOfPixels() {
        return numberOfPixels_;
    }

    public double getPreciseRgbPercentage() {
        return preciseRgbPercentage_;
    }

    public double getPreciseYCbCrPercentage() {
        return preciseYCbCrPercentage_;
    }

    public double getPreciseAveragePercentage() {
        return preciseAveragePercentage_;
    }

    public int getReadableRgbPercentage() {
        return readableRgbPercentage_;
    }

    public int getReadableYCbCrPercentage() {
        return readableYCbCrPercentage_;
    }

    public int getReadableAveragePercentage() {
        return readableAveragePercentage_;
    }

    public String getMd5() {
        return md5_;
    }

    public String getSha1() {
        return sha1_;
    }

    public void setCanonicalPath(String path) {
        canonicalPath_ = path;
    }

    public void setCanonicalDirectory(String dir) {
        canonicalDirectory_ = dir;
    }

    public void setName(String name) {
        name_ = name;
    }

    public void setHasSkinTone(boolean hasSkinTone) {
        hasSkinTone_ = hasSkinTone;
    }

    public void setMd5(String md5) {
        md5_ = md5;
    }

    public void setSha1(String sha1) {
        sha1_ = sha1;
    }

    public void increasNumberOfRgbSkinToneHits() {
        numberOfRgbSkinToneHits_++;
    }

    public void increasNumberOfYCbCrSkinToneHits() {
        numberOfYCbCrSkinToneHits_++;
    }

    /**
	 * Computes the Percentages -  Example: 0.666677 and sets the according
	 * variables of the Categorized Image. Should only performed once the 
	 * whole image has been processed.
	 */
    public void computePercentages(boolean usedRGB, boolean usedYCbCr) {
        if ((numberOfRgbSkinToneHits_ <= numberOfPixels_) && (numberOfYCbCrSkinToneHits_ <= numberOfPixels_)) {
            MathContext precision = MathContext.DECIMAL32;
            BigDecimal a = new BigDecimal(numberOfRgbSkinToneHits_);
            BigDecimal a2 = new BigDecimal(numberOfYCbCrSkinToneHits_);
            BigDecimal b = new BigDecimal(numberOfPixels_);
            try {
                a = a.divide(b, precision);
                a2 = a2.divide(b, precision);
            } catch (ArithmeticException exc) {
                exc.printStackTrace();
                log_.appendString("Error while computing skintone Percentage" + "of image: " + canonicalPath_);
                case_.setIsValidRun(false);
            }
            preciseRgbPercentage_ = a.doubleValue();
            preciseYCbCrPercentage_ = a2.doubleValue();
            readableRgbPercentage_ = formatPercentage(preciseRgbPercentage_);
            readableYCbCrPercentage_ = formatPercentage(preciseYCbCrPercentage_);
            if (usedRGB && usedYCbCr) {
                BigDecimal b_avg = new BigDecimal(2);
                BigDecimal avg = (a.add(a2)).divide(b_avg, precision);
                preciseAveragePercentage_ = avg.doubleValue();
            } else if (usedYCbCr) {
                preciseAveragePercentage_ = preciseYCbCrPercentage_;
            } else {
                preciseAveragePercentage_ = preciseRgbPercentage_;
            }
            readableAveragePercentage_ = formatPercentage(preciseAveragePercentage_);
        } else {
            isProcessedCorrectly_ = false;
            preciseRgbPercentage_ = 1.0;
            preciseYCbCrPercentage_ = 1.0;
            preciseAveragePercentage_ = 1.0;
            readableRgbPercentage_ = 100;
            readableYCbCrPercentage_ = 100;
            readableAveragePercentage_ = 100;
        }
    }

    /**
	 * @return readablePercentage Returns the percentage as a readable/integer
	 *  form from 0 to a 100
	 */
    public int formatPercentage(double percentage) {
        int readablePercentage = (int) (percentage * 100);
        return readablePercentage;
    }

    /**
	 * Compares CategorizedImages and sorts by the following hierarchy: 
	 * skinTone% - LastModified
	 * 
	 * @param CategorizedImage
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
    public int compareTo(Object image2) {
        double image2Percentage = 0;
        long image2LastMod = 0;
        try {
            image2Percentage = ((CategorizedImage) image2).getPreciseAveragePercentage();
            image2LastMod = ((CategorizedImage) image2).getLastModified();
        } catch (ClassCastException exc) {
            exc.printStackTrace();
            log_.appendString("Problem when sorting Images - ClassCastError");
        }
        if (preciseAveragePercentage_ > image2Percentage) {
            return 1;
        } else if (preciseAveragePercentage_ < image2Percentage) {
            return -1;
        } else {
            if (lastModified_ > image2LastMod) {
                return 1;
            } else if (lastModified_ < image2LastMod) {
                return -1;
            } else {
                return 0;
            }
        }
    }

    /**
	 * @return the CategorizedImage as a textual representation to ease 
	 * 			report export
	 */
    public String toString() {
        Date lM = new Date(lastModified_);
        SimpleDateFormat timeStampFormat = new SimpleDateFormat("yyyy-MM-dd|HH:mm:ss|Z");
        StringBuilder theString = new StringBuilder();
        SystemInformation currentSystem_ = SystemInformation.getInstance();
        theString.append(canonicalPath_);
        theString.append(currentSystem_.getLineSeparator());
        theString.append(readableAveragePercentage_ + "%\t");
        theString.append(width_ + "x" + height_ + " = " + numberOfPixels_);
        theString.append("px");
        theString.append("\tRGB DetectorValue: " + preciseRgbPercentage_);
        theString.append("\tYCbCr DetectorValue: " + preciseYCbCrPercentage_);
        theString.append(currentSystem_.getLineSeparator());
        theString.append("Last Modified: ");
        theString.append(timeStampFormat.format(lM));
        theString.append("\t\tProcessed correctly: ");
        theString.append(isProcessedCorrectly_);
        if (md5_ != null) {
            theString.append(currentSystem_.getLineSeparator());
            theString.append("MD5: " + md5_);
        }
        if (sha1_ != null) {
            theString.append(currentSystem_.getLineSeparator());
            theString.append("SHA1: " + sha1_);
        }
        theString.append(currentSystem_.getLineSeparator());
        theString.append(currentSystem_.getLineSeparator());
        return theString.toString();
    }
}
