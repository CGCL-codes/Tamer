package com.ohua.real.time.operators;

import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import com.ohua.checkpoint.framework.operatorcheckpoints.AbstractCheckPoint;
import com.ohua.checkpoint.framework.operatorcheckpoints.EmptyCheckpoint;
import com.ohua.engine.data.model.daapi.InputPortControl;
import com.ohua.engine.data.model.daapi.OutputPortControl;
import com.ohua.engine.exceptions.Assertion;
import com.ohua.engine.flowgraph.elements.operator.UserOperator;

public class NormalizerOperator extends UserOperator {

    public static class Normalization {

        public String sourceFormat = null;

        public String targetFormat = null;

        public boolean recursive = true;
    }

    public static class NormalizerProperties {

        public Map<String, List<Normalization>> normalization = null;
    }

    public NormalizerProperties _properties = null;

    private InputPortControl _inportControl = null;

    private OutputPortControl _outportControl = null;

    @Override
    public void cleanup() {
    }

    @Override
    public void prepare() {
        _inportControl = getDataLayer().getInputPortController("input");
        _outportControl = getDataLayer().getOutputPortController("output");
    }

    @Override
    public void runProcessRoutine() {
        boolean backOff = false;
        while (_inportControl.next() && !backOff) {
            getDataLayer().transferInputToOutput(_inportControl.getPortName(), _outportControl.getPortName());
            for (Map.Entry<String, List<Normalization>> entry : _properties.normalization.entrySet()) {
                for (Normalization normalization : entry.getValue()) {
                    performNormalization(entry, normalization);
                }
            }
            backOff = _outportControl.send();
        }
    }

    private void performNormalization(Map.Entry<String, List<Normalization>> entry, Normalization normalization) {
        List<Object> sourceValues = _outportControl.getData(entry.getKey());
        Assertion.invariant(sourceValues.size() == 1);
        String sourceValue = sourceValues.get(0).toString();
        Pattern sourcePattern = Pattern.compile(normalization.sourceFormat);
        String targetValue = normalization.targetFormat;
        Matcher matcher = sourcePattern.matcher(sourceValue);
        while (matcher.find()) {
            String result = targetValue;
            do {
                for (int i = 1; i <= matcher.groupCount(); i++) {
                    String group = matcher.group(i);
                    result = result.replace("$" + i, group);
                }
                _outportControl.setData(entry.getKey(), result);
            } while (matcher.find());
            if (!normalization.recursive) {
                break;
            } else {
                sourceValue = result;
                matcher = sourcePattern.matcher(sourceValue);
            }
        }
    }

    public AbstractCheckPoint checkpoint() {
        return new EmptyCheckpoint();
    }

    public void restart(AbstractCheckPoint checkpoint) {
        prepare();
    }
}
