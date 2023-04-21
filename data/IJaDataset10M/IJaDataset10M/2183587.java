package org.expasy.jpl.core.ms.spectrum.peak;

import org.apache.commons.collections15.Transformer;
import org.expasy.jpl.commons.base.builder.BuilderException;
import org.expasy.jpl.commons.base.builder.InstanceBuilder;
import org.expasy.jpl.commons.base.math.DoubleComparator;
import org.expasy.jpl.core.mol.chem.MassCalculator;
import org.expasy.jpl.core.mol.polymer.pept.Peptide;
import org.expasy.jpl.core.ms.lc.RetentionTime;

/**
 * A MS peak is a molecular ion obtained from a mass spectrometry experiment
 * detected in a specific mass/charge.
 * 
 * @author nikitin
 * 
 * @version 1.0
 * 
 */
public class PeakImpl implements Peak {

    public static final DoubleComparator COMPARATOR = DoubleComparator.newInstance(0.0000000001);

    private static final long serialVersionUID = 8626703305620811095L;

    public static final int UNKNOWN_CHARGE_STATE = 0;

    public static final int DEFAULT_CHARGE_STATE = 1;

    /** id of the peak (persistence use) */
    protected long id;

    /** mz of the peak */
    protected double mz;

    /** number of electrical charges (0 if unknown) */
    protected int charge;

    /** intensity of signal */
    private double intensity;

    /** the sequence peptide */
    private Peptide peptide;

    /** retention time */
    private RetentionTime rt;

    /** the Mass-Spectrometry level */
    protected int msLevel = 1;

    public static class Builder implements InstanceBuilder<PeakImpl> {

        /** mandatory */
        private double mz;

        /** default */
        private Peptide peptide = null;

        private String sequence = null;

        Transformer<String, String> converter = null;

        private MassCalculator massCalc = MassCalculator.getMonoAccuracyInstance();

        private double intensity;

        private int charge = DEFAULT_CHARGE_STATE;

        private int msLevel = 1;

        private RetentionTime rt = null;

        public Builder(double mz) {
            this.mz = mz;
        }

        public Builder(String sequence) {
            this.sequence = sequence;
        }

        public Builder(String sequence, double mz) {
            this.sequence = sequence;
            this.mz = mz;
        }

        public Builder(Peptide peptide) {
            this.peptide = peptide;
            this.charge = peptide.getNumberOfProtons();
        }

        public Builder(String seq, Transformer<String, String> converter) {
            this.converter = converter;
            this.sequence = converter.transform(seq);
        }

        public Builder massCalc(MassCalculator massCalc) {
            this.massCalc = massCalc;
            return this;
        }

        public Builder intensity(double intensity) {
            this.intensity = intensity;
            return this;
        }

        public Builder charge(int charge) {
            this.charge = charge;
            return this;
        }

        public Builder msLevel(int msLevel) {
            this.msLevel = msLevel;
            return this;
        }

        public Builder rt(RetentionTime rt) {
            this.rt = rt;
            return this;
        }

        public PeakImpl build() throws BuilderException {
            if (sequence == null && peptide == null) {
                try {
                    PeakChecker.checkMz(mz);
                } catch (InvalidPeakException e) {
                    throw new BuilderException("cannot build MS-peak" + e);
                }
            } else {
                if (sequence != null) {
                    peptide = new Peptide.Builder(sequence).ambiguityEnabled().protons(charge).build();
                }
                if (!peptide.isAmbiguous()) {
                    mz = massCalc.getMz(peptide);
                }
            }
            return new PeakImpl(this);
        }
    }

    public PeakImpl() {
    }

    public PeakImpl(Builder builder) {
        mz = builder.mz;
        charge = builder.charge;
        intensity = builder.intensity;
        msLevel = builder.msLevel;
        peptide = builder.peptide;
        rt = builder.rt;
    }

    public static PeakImpl emptyInstance() {
        return new PeakImpl();
    }

    @Override
    public boolean equals(final Object obj) {
        if (obj instanceof PeakImpl) {
            final PeakImpl peak = (PeakImpl) obj;
            if (compareTo(peak) == 0 && (peak.charge == charge)) {
                return true;
            }
        }
        return false;
    }

    /**
	 * Return a copy of {@code this} instance.
	 */
    @Override
    public PeakImpl clone() {
        try {
            PeakImpl instance = (PeakImpl) super.clone();
            instance.id = id;
            instance.mz = mz;
            instance.charge = charge;
            instance.intensity = intensity;
            instance.peptide = peptide;
            if (rt != null) {
                instance.rt = rt.clone();
            }
            instance.msLevel = msLevel;
            return instance;
        } catch (CloneNotSupportedException e) {
            throw new IllegalStateException("cannot make a clone from " + this, e);
        }
    }

    public void setMz(final double mz) throws InvalidPeakException {
        PeakChecker.checkMz(mz);
        this.mz = mz;
    }

    public final double getMz() {
        return mz;
    }

    /**
	 * Set a valid charge state peak value.
	 * 
	 * @param chargeNumber the charge value to set.
	 * @throws InvalidPeakException
	 * 
	 * @throws JPLInvalidChargeException if charge is negative or null.
	 */
    public void setCharge(final int chargeNumber) throws InvalidPeakException {
        PeakChecker.checkCharge(chargeNumber);
        charge = chargeNumber;
    }

    /**
	 * Get the charge of peak.
	 * 
	 * @return peak charge state.
	 */
    public int getCharge() {
        return charge;
    }

    public double getIntensity() {
        return intensity;
    }

    public long getId() {
        return id;
    }

    public void setId(final long id) {
        this.id = id;
    }

    public int getMSLevel() {
        return msLevel;
    }

    public void setMSLevel(final int newLevel) {
        msLevel = newLevel;
    }

    public Peptide getPeptide() {
        return peptide;
    }

    public RetentionTime getRT() {
        return rt;
    }

    public int compareTo(Peak o) {
        return COMPARATOR.compare(mz, o.getMz());
    }

    /**
	 * The string representation of a peak.
	 * 
	 * @return a string brief description.
	 */
    @Override
    public String toString() {
        final StringBuffer buffer = new StringBuffer();
        if (mz > 0) {
            buffer.append(mz);
            buffer.append(" Da");
            if (intensity > 0) {
                buffer.append(" (" + intensity + ")");
            }
            buffer.append(", charge = ");
            if (charge == UNKNOWN_CHARGE_STATE) {
                buffer.append("NA");
            } else {
                buffer.append(charge);
            }
        } else {
            buffer.append("null");
        }
        return buffer.toString();
    }
}
