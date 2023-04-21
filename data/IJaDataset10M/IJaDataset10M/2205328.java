package com.ibm.tuningfork.infra.stream;

import com.ibm.tuningfork.infra.data.Sample;
import com.ibm.tuningfork.infra.sharing.ISharingConvertibleCallback;
import com.ibm.tuningfork.infra.stream.core.SampleStream;
import com.ibm.tuningfork.infra.stream.expression.Operand;
import com.ibm.tuningfork.infra.stream.precise.SampleStreamCursor;
import com.ibm.tuningfork.infra.units.Unit;

/**
 * A stream of samples obtained by applying a reduction operator to the input samples, in order.
 */
public class ReductionOfManySampleStream extends SampleStream {

    protected final Sample defaultValue = new Sample(Long.MAX_VALUE, 0.0);

    protected final SampleStream[] inputs;

    protected final SampleReductionOperator[] operators;

    protected Sample lastInputSample;

    protected Sample lastGeneratedSample = defaultValue;

    public ReductionOfManySampleStream(String name, SampleReductionOperator operator, SampleStream input1, SampleStream input2, Unit unit) {
        this(name, new SampleReductionOperator[] { operator, operator }, new SampleStream[] { input1, input2 }, unit);
    }

    public ReductionOfManySampleStream(String name, SampleReductionOperator operator, SampleStream[] inputs, Unit unit) {
        this(name, makeOperatorArray(operator, inputs.length), inputs, unit);
    }

    public ReductionOfManySampleStream(String name, SampleReductionOperator[] operators, SampleStream[] inputs, Unit unit) {
        super(name, makeOperatorName(operators), Operand.makeOperandList(inputs), unit);
        this.inputs = inputs.clone();
        this.operators = operators.clone();
    }

    private static SampleReductionOperator[] makeOperatorArray(SampleReductionOperator operator, int n) {
        SampleReductionOperator[] array = new SampleReductionOperator[n];
        for (int i = 0; i < n; i++) {
            array[i] = operator;
        }
        return array;
    }

    private static String makeOperatorName(SampleReductionOperator operators[]) {
        String name = "";
        String sep = "";
        String uniqueName = operators[0].getName();
        for (SampleReductionOperator operator : operators) {
            name += sep + operator.getName();
            sep = ",";
            if (!name.equals(uniqueName)) {
                uniqueName = null;
            }
        }
        if (uniqueName != null) {
            return uniqueName;
        } else {
            return name;
        }
    }

    @Override
    public void collectSpecificReconstructionArguments(ISharingConvertibleCallback cb) throws Exception {
        cb.convert(getName());
        cb.convert(inputs);
        cb.convert(operators);
    }

    private void process(Sample sample, int index) {
        Sample newSample = operators[index].reduce(sample, lastGeneratedSample, lastInputSample);
        if (newSample != null) {
            addSample(newSample);
            lastGeneratedSample = newSample;
        }
        lastInputSample = sample;
    }

    public void derivedRun() {
        final int N = inputs.length;
        SampleStreamCursor[] cursors = new SampleStreamCursor[N];
        Sample[] current = new Sample[N];
        for (int i = 0; i < N; i++) {
            inputs[i].start();
            cursors[i] = inputs[i].newCursor(0, Long.MAX_VALUE);
        }
        int eofCount = 0;
        while (true) {
            for (int i = 0; i < N; i++) {
                if (current[i] == null) {
                    current[i] = cursors[i].getNext();
                    while (current[i] == null) {
                        if (cursors[i].eof()) {
                            current[i] = defaultValue;
                            eofCount++;
                            break;
                        }
                        modifyCheck();
                        cursors[i].blockForMore();
                        current[i] = cursors[i].getNext();
                    }
                }
            }
            if (eofCount == N) {
                return;
            }
            int oldestIndex = 0;
            for (int i = 1; i < N; i++) {
                if (current[i].getTime() <= current[oldestIndex].getTime()) {
                    oldestIndex = i;
                }
            }
            Sample oldest = current[oldestIndex];
            process(oldest, oldestIndex);
            current[oldestIndex] = null;
        }
    }
}
