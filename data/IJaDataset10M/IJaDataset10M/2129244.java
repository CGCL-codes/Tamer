package org.debellor.core.cell;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import org.debellor.core.Cell;
import org.debellor.core.Sample;
import org.debellor.core.Sample.SampleType;

/**
 * @author Marcin Wojnarski
 *
 */
public class BatchOfSamples extends Cell {

    private SampleType type;

    private ArrayList<Sample> samples = new ArrayList<Sample>();

    private int pos;

    /** Constructs a BatchOfSamples with the initial content passed as an array of samples
	 * or as a variable-length parameter list. */
    public BatchOfSamples(Sample... s) {
        super(false);
        samples.addAll(Arrays.asList(s));
    }

    public BatchOfSamples(Collection<? extends Sample> s) {
        super(false);
        samples.addAll(s);
    }

    public BatchOfSamples(SampleType t, Sample... s) {
        super(false);
        setType(t);
        samples.addAll(Arrays.asList(s));
    }

    /** Adds an array of samples or a variable-length parameter list of samples. */
    public void add(Sample... s) {
        samples.addAll(Arrays.asList(s));
    }

    public void add(Collection<? extends Sample> s) {
        samples.addAll(s);
    }

    @Override
    protected SampleType onOpen() throws Exception {
        pos = 0;
        return type;
    }

    @Override
    protected Sample onNext() throws Exception {
        if (pos >= samples.size()) return null;
        return samples.get(pos++);
    }

    @Override
    protected void onClose() throws Exception {
    }

    public void setType(SampleType type) {
        this.type = type;
    }
}
