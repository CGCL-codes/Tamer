package lo.local.dreamrec.logic;

import com.webkitchen.eeg.acquisition.IRawSampleListener;
import com.webkitchen.eeg.acquisition.RawSample;

/**
 *
 */
public class InitialDataFilter extends AbstractFilter implements IRawSampleListener {

    long previousInputDataIndex;

    public InitialDataFilter() {
        super(1);
    }

    @Override
    protected Data calculateOutputData() {
        Data inputData = inputBuffer.get(0);
        long indexIncrement = 0;
        if (previousInputDataIndex != 0) {
            indexIncrement = inputData.getIndex() - previousInputDataIndex;
        }
        if (indexIncrement > 1) {
            throw new RuntimeException("Lost " + indexIncrement + " data Samples");
        }
        previousInputDataIndex = inputData.getIndex();
        inputBuffer.remove(0);
        return new Data(inputData.getValue(), inputDataCounter);
    }

    public void receiveSample(RawSample rawSample) {
        dataReceived(new Data(rawSample.getSamples()[0], rawSample.getPacketNumber()));
    }
}
