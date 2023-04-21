package org.encog.app.analyst.csv;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.encog.app.analyst.AnalystError;
import org.encog.app.analyst.EncogAnalyst;
import org.encog.app.analyst.script.normalize.AnalystField;
import org.encog.util.EngineArray;

/**
 * A utility used to breat data into time-series lead and lag.
 */
public class TimeSeriesUtil {

    /**
	 * The lag depth.
	 */
    private final int lagDepth;

    /**
	 * The lead depth.
	 */
    private final int leadDepth;

    /**
	 * The total depth.
	 */
    private final int totalDepth;

    /**
	 * The analyst to use.
	 */
    private final EncogAnalyst analyst;

    /**
	 * The input size.
	 */
    private final int inputSize;

    /**
	 * The output size.
	 */
    private final int outputSize;

    /**
	 * The buffer to hold the time-series data.
	 */
    private final List<double[]> buffer = new ArrayList<double[]>();

    /**
	 * The heading map.
	 */
    private final Map<String, Integer> headingMap = new HashMap<String, Integer>();

    /**
	 * Construct the time-series utility.
	 * @param theAnalyst The analyst to use.
	 * @param headings The column headings.
	 */
    public TimeSeriesUtil(final EncogAnalyst theAnalyst, final List<String> headings) {
        this.analyst = theAnalyst;
        this.lagDepth = analyst.getLagDepth();
        this.leadDepth = analyst.getLeadDepth();
        this.totalDepth = this.lagDepth + this.leadDepth + 1;
        this.inputSize = analyst.determineUniqueColumns();
        this.outputSize = analyst.determineInputCount() + analyst.determineOutputCount();
        int headingIndex = 0;
        for (final String column : headings) {
            this.headingMap.put(column, headingIndex++);
        }
    }

    /**
	 * @return the analyst
	 */
    public final EncogAnalyst getAnalyst() {
        return this.analyst;
    }

    /**
	 * @return the buffer
	 */
    public final List<double[]> getBuffer() {
        return this.buffer;
    }

    /**
	 * @return the headingMap
	 */
    public final Map<String, Integer> getHeadingMap() {
        return this.headingMap;
    }

    /**
	 * @return the inputSize
	 */
    public final int getInputSize() {
        return this.inputSize;
    }

    /**
	 * @return the lagDepth
	 */
    public final int getLagDepth() {
        return this.lagDepth;
    }

    /**
	 * @return the leadDepth
	 */
    public final int getLeadDepth() {
        return this.leadDepth;
    }

    /**
	 * @return the outputSize
	 */
    public final int getOutputSize() {
        return this.outputSize;
    }

    /**
	 * @return the totalDepth
	 */
    public final int getTotalDepth() {
        return this.totalDepth;
    }

    /**
	 * Process a row.
	 * @param input The input.
	 * @return The output.
	 */
    public final double[] process(final double[] input) {
        if (input.length != this.inputSize) {
            throw new AnalystError("Invalid input size: " + input.length + ", should be " + this.inputSize);
        }
        this.buffer.add(0, EngineArray.arrayCopy(input));
        if (this.buffer.size() < this.totalDepth) {
            return null;
        }
        final double[] output = new double[this.outputSize];
        int outputIndex = 0;
        for (final AnalystField field : this.analyst.getScript().getNormalize().getNormalizedFields()) {
            if (!field.isIgnored()) {
                if (!this.headingMap.containsKey(field.getName())) {
                    throw new AnalystError("Undefined field: " + field.getName());
                }
                final int headingIndex = this.headingMap.get(field.getName());
                final int timeslice = translateTimeSlice(field.getTimeSlice());
                final double[] row = this.buffer.get(timeslice);
                final double d = row[headingIndex];
                output[outputIndex++] = d;
            }
        }
        while (this.buffer.size() > this.totalDepth) {
            this.buffer.remove(this.buffer.size() - 1);
        }
        return output;
    }

    /**
	 * Translate a timeslice from a pos/neg number to a displacement 
	 * into the buffer.
	 * @param index The index.
	 * @return The translated displacement.
	 */
    private int translateTimeSlice(final int index) {
        return Math.abs(index - this.leadDepth);
    }
}
