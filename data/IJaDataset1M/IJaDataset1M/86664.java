package mtn.sevenuplive.max.mxj;

public class SevenUp4LiveControllerClient extends SevenUp4LiveMidiClient {

    public SevenUp4LiveControllerClient(SevenUp4Live app, int instanceNum, int ch) {
        super(app, instanceNum, ch);
    }

    protected int getOutletOrdinal() {
        return SevenUp4Live.eOutlets.ControllerMidiOutlet.ordinal();
    }
}
