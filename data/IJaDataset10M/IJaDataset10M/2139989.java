package edu.usc.genomix.snaploview;

import java.util.*;

public class ScoreRecord {

    private String mSnpName;

    private String mAlias;

    private String mChr;

    private int mCoordinate;

    private float mSnpScore;

    private String mOriginalName;

    private int mCodingStatus;

    private int mLocus;

    private boolean mForceInclude;

    private boolean mForceCapture;

    private boolean mPotentialTagNoCapture;

    public static final String CODINGSTATUS_UNKNOWN_STRING = "-99";

    public static final String CODINGSTATUS_NONSYN_STRING = "NONSYN";

    public static final String CODINGSTATUS_SYNON_STRING = "SYNON";

    public static final int CODINGSTATUS_UNKNOWN = 0;

    public static final int CODINGSTATUS_NONSYN = 1;

    public static final int CODINGSTATUS_SYNON = 2;

    public static final String LOCUS_3UTR_STRING = "3UTR";

    public static final String LOCUS_5UTR_STRING = ("5UTR");

    public static final String LOCUS_UTR_STRING = "UTR";

    public static final String LOCUS_CODING_STRING = "coding";

    public static final String LOCUS_FLANKING_3UTR_STRING = "flanking_3UTR";

    public static final String LOCUS_FLANKING_5UTR_STRING = "flanking_5UTR";

    public static final String LOCUS_INTRON_STRING = "intron";

    public static final String LOCUS_UNKNOWN_STRING = "-99";

    public static final int LOCUS_3UTR = 0;

    public static final int LOCUS_5UTR = 1;

    public static final int LOCUS_UTR = 2;

    public static final int LOCUS_CODING = 3;

    public static final int LOCUS_FLANKING_3UTR = 4;

    public static final int LOCUS_FLANKING_5UTR = 5;

    public static final int LOCUS_INTRON = 6;

    public static final int LOCUS_UNKNOWN = 7;

    public ScoreRecord(String[] split, Hashtable columnNameToIndexHash) {
        String coordinateString;
        this.mAlias = null;
        try {
            this.mSnpName = split[((Integer) (columnNameToIndexHash.get(ScoreFile.SNPNAME))).intValue()];
        } catch (ArrayIndexOutOfBoundsException ex) {
            throw ex;
        }
        try {
            if (columnNameToIndexHash.containsKey(ScoreFile.ORIGINAL_NAME)) {
                this.mOriginalName = split[((Integer) (columnNameToIndexHash.get(ScoreFile.ORIGINAL_NAME))).intValue()];
            } else {
                this.mOriginalName = this.mSnpName;
            }
        } catch (ArrayIndexOutOfBoundsException ex) {
            throw ex;
        }
        try {
            mChr = split[((Integer) (columnNameToIndexHash.get(ScoreFile.CHR))).intValue()];
        } catch (ArrayIndexOutOfBoundsException ex) {
            throw ex;
        }
        if (mChr.equals("")) {
            throw new ArrayIndexOutOfBoundsException();
        }
        try {
            coordinateString = split[((Integer) (columnNameToIndexHash.get(ScoreFile.COORDINATE))).intValue()];
            mCoordinate = Integer.parseInt(coordinateString);
            if (mCoordinate < 0) {
                throw new ArrayIndexOutOfBoundsException();
            }
        } catch (ArrayIndexOutOfBoundsException ex2) {
            throw ex2;
        } catch (NumberFormatException ex3) {
            throw ex3;
        }
        try {
            String scoreString = split[((Integer) (columnNameToIndexHash.get(ScoreFile.SNP_SCORE))).intValue()];
            if (scoreString.equals("")) {
                this.mSnpScore = 0;
            } else {
                this.mSnpScore = Float.parseFloat(scoreString);
            }
        } catch (ArrayIndexOutOfBoundsException ex2) {
            throw ex2;
        } catch (NumberFormatException ex3) {
            throw ex3;
        }
        try {
            String text;
            if (columnNameToIndexHash.containsKey(ScoreFile.CODING_STATUS)) {
                text = split[((Integer) (columnNameToIndexHash.get(ScoreFile.CODING_STATUS))).intValue()];
                if (text.equals(CODINGSTATUS_NONSYN_STRING)) {
                    this.mCodingStatus = CODINGSTATUS_NONSYN;
                } else if (text.equals(CODINGSTATUS_SYNON_STRING)) {
                    this.mCodingStatus = CODINGSTATUS_SYNON;
                } else {
                    this.mCodingStatus = CODINGSTATUS_UNKNOWN;
                }
            }
        } catch (ArrayIndexOutOfBoundsException ex) {
            throw ex;
        }
        try {
            String text;
            if (columnNameToIndexHash.containsKey(ScoreFile.LOCATION)) {
                text = split[((Integer) (columnNameToIndexHash.get(ScoreFile.LOCATION))).intValue()];
                if (text.equals(LOCUS_3UTR_STRING)) {
                    this.mLocus = LOCUS_3UTR;
                } else if (text.equals(LOCUS_5UTR_STRING)) {
                    this.mLocus = LOCUS_5UTR;
                } else if (text.equals(LOCUS_UTR_STRING)) {
                    this.mLocus = LOCUS_UTR;
                } else if (text.equals(LOCUS_CODING_STRING)) {
                    this.mLocus = LOCUS_CODING;
                } else if (text.equals(LOCUS_FLANKING_3UTR_STRING)) {
                    this.mLocus = LOCUS_FLANKING_3UTR;
                } else if (text.equals(LOCUS_FLANKING_5UTR_STRING)) {
                    this.mLocus = LOCUS_FLANKING_5UTR;
                } else if (text.equals(LOCUS_INTRON_STRING)) {
                    this.mLocus = LOCUS_INTRON;
                } else {
                    this.mLocus = LOCUS_UNKNOWN;
                }
            }
        } catch (ArrayIndexOutOfBoundsException ex) {
            throw ex;
        }
        try {
            String text;
            this.mForceCapture = false;
            if (columnNameToIndexHash.containsKey(ScoreFile.FORCE_CAPTURE)) {
                text = split[((Integer) (columnNameToIndexHash.get(ScoreFile.FORCE_CAPTURE))).intValue()];
                if (text.equals("1")) {
                    this.mForceCapture = true;
                }
            }
        } catch (ArrayIndexOutOfBoundsException ex) {
            throw ex;
        }
        try {
            String text;
            this.mForceInclude = false;
            if (columnNameToIndexHash.containsKey(ScoreFile.FORCE_INCLUDE)) {
                text = split[((Integer) (columnNameToIndexHash.get(ScoreFile.FORCE_INCLUDE))).intValue()];
                if (text.equals("1")) {
                    this.mForceInclude = true;
                }
            }
        } catch (ArrayIndexOutOfBoundsException ex) {
            throw ex;
        }
        try {
            String text;
            this.mPotentialTagNoCapture = false;
            if (columnNameToIndexHash.containsKey(ScoreFile.POTENTIAL_TAG_NO_CAPTURE)) {
                text = split[((Integer) (columnNameToIndexHash.get(ScoreFile.POTENTIAL_TAG_NO_CAPTURE))).intValue()];
                if (text.equals("1")) {
                    this.mPotentialTagNoCapture = true;
                }
            }
        } catch (ArrayIndexOutOfBoundsException ex) {
            throw ex;
        }
    }

    public String getSnpName() {
        return this.mSnpName;
    }

    public String getOriginalName() {
        return this.mOriginalName;
    }

    public String getChr() {
        return this.mChr;
    }

    public int getCoordinate() {
        return this.mCoordinate;
    }

    public float getSnpScore() {
        return this.mSnpScore;
    }

    public void setAlias(String alias) {
        this.mAlias = alias;
    }

    public String getAlias() {
        return this.mAlias;
    }

    public int getLocus() {
        return this.mLocus;
    }

    public int getCodingStatus() {
        return this.mCodingStatus;
    }

    public boolean getForceCapture() {
        return this.mForceCapture;
    }

    public boolean getForceInclude() {
        return this.mForceInclude;
    }

    public boolean getPotentialTagNoCapture() {
        return this.mPotentialTagNoCapture;
    }
}
