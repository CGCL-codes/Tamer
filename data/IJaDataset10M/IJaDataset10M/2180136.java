package org.spantus.extractor.impl;

import java.util.Iterator;
import java.util.LinkedList;
import org.spantus.core.FrameValues;
import org.spantus.core.FrameVectorValues;
import org.spantus.extractor.AbstractExtractor;
import org.spantus.extractor.AbstractExtractorVector;
import org.spantus.math.MatrixUtils;

/**
 *  Linear predictive coding residual energy
 * 
 * @author Mindaugas Greibus
 *
 * @since 0.0.1
 * 
 * Created 2008.02.29
 *
 */
public class LPCResidualExtractor extends AbstractExtractor {

    private AbstractExtractorVector extractorVector = new LPCExtractor();

    private LinkedList<Double> buffer;

    protected FrameVectorValues calculateExtr3D(FrameValues window) {
        syncLPCParams();
        return extractorVector.calculateWindow(window);
    }

    private void syncLPCParams() {
        extractorVector.setConfig(getConfig());
    }

    public FrameValues calculateWindow(FrameValues window) {
        FrameVectorValues extrValues = calculateExtr3D(window);
        FrameValues calculatedValues = newFrameValues(window);
        int order = extrValues.get(0).size();
        LinkedList<Double> bufferValues = getBuffer(order);
        Double valueSum = 0D;
        Double predictedSum = 0D;
        for (Double value : window) {
            bufferValues.poll();
            bufferValues.add(value);
            Double predicted = 0D;
            Iterator<Double> coefIter = extrValues.get(0).iterator();
            for (Double bufferedVal : getBuffer(order)) {
                predicted += bufferedVal * coefIter.next();
            }
            valueSum += value;
            predictedSum += predicted;
        }
        calculatedValues.add(Math.abs(valueSum - predictedSum));
        return calculatedValues;
    }

    LinkedList<Double> getBuffer(int order) {
        if (buffer == null) {
            buffer = new LinkedList<Double>();
            buffer.addAll(MatrixUtils.zeros(order));
        }
        return buffer;
    }

    public String getName() {
        return ExtractorEnum.LPC_RESIDUAL_EXTRACTOR.name();
    }
}
