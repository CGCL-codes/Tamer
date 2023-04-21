package ncsa.d2k.core.modules;

public class FanInModule extends ComputeModule {

    int availableInputIndex;

    int lastInputIndexChecked;

    public boolean isReady() {
        for (int i = 0; i < this.numInputs; i++) {
            if (inputWriteIndices[lastInputIndexChecked] != inputReadIndices[lastInputIndexChecked]) {
                availableInputIndex = lastInputIndexChecked;
                return true;
            }
            if (++lastInputIndexChecked == this.numInputs) lastInputIndexChecked = 0;
        }
        return false;
    }

    public void doit() throws Exception {
        this.pushOutput(this.pullInput(availableInputIndex), 0);
    }
}
