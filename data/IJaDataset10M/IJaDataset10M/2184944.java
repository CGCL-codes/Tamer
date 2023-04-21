package fr.ens.transcriptome.teolenn.measurement;

import fr.ens.transcriptome.teolenn.TeolennException;
import fr.ens.transcriptome.teolenn.sequence.Sequence;

public abstract class BooleanMeasurement extends SimpleMeasurement {

    /**
   * Calc the measurement of a sequence.
   * @param sequence the sequence to use for the measurement
   * @return an int value
   */
    protected abstract boolean calcBooleanMeasurement(final Sequence sequence);

    /**
   * Calc the measurement of a sequence.
   * @param sequence the sequence to use for the measurement
   * @return an object as result
   */
    public Object calcMesurement(final Sequence sequence) {
        return calcBooleanMeasurement(sequence);
    }

    /**
   * Get the type of the result of calcMeasurement.
   * @return the type of the measurement
   */
    public Object getType() {
        return Boolean.class;
    }

    /**
   * Parse a string to an object return as calcMeasurement.
   * @param s String to parse
   * @return an object
   */
    public Object parse(final String s) {
        if (s == null) return null;
        return Boolean.parseBoolean(s);
    }

    /**
   * Set a parameter for the filter.
   * @param key key for the parameter
   * @param value value of the parameter
   */
    public void setInitParameter(final String key, final String value) {
    }

    /**
   * Run the initialization phase of the parameter.
   * @throws TeolennException if an error occurs while the initialization phase
   */
    public void init() throws TeolennException {
    }
}
