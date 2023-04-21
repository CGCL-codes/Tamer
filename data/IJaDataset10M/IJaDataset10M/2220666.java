package net.sf.cplab.core.util;

/**
 * Bean for holding trial time and trial parameters. Used by ImageTrialTimeFactory
 * 
 * @author jtse
 *
 */
public class TrialTime {

    private String trialParameters;

    private Double trialTime;

    public TrialTime() {
    }

    /**
	 * @param trialParameters
	 * @param trialTime
	 */
    public TrialTime(String trialParameters, Double trialTime) {
        this.trialParameters = trialParameters;
        this.trialTime = trialTime;
    }

    public String getTrialParameters() {
        return trialParameters;
    }

    public void setTrialParameters(String trialParameters) {
        this.trialParameters = trialParameters;
    }

    public Double getTrialTime() {
        return trialTime;
    }

    public void setTrialTime(Double trialTime) {
        this.trialTime = trialTime;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((trialParameters == null) ? 0 : trialParameters.hashCode());
        result = prime * result + ((trialTime == null) ? 0 : trialTime.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        final TrialTime other = (TrialTime) obj;
        if (trialParameters == null) {
            if (other.trialParameters != null) return false;
        } else if (!trialParameters.equals(other.trialParameters)) return false;
        if (trialTime == null) {
            if (other.trialTime != null) return false;
        } else if (!trialTime.equals(other.trialTime)) return false;
        return true;
    }
}
