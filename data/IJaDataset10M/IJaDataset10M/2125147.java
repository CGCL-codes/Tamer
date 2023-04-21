package policy;

import flit.FlitArray;

public abstract class Policy {

    public abstract int[] route(FlitArray inputChannels, boolean[] outputChannelStatus);

    public abstract int getPolicyCode();

    public abstract String getPolicyName();

    public abstract String getPolicyDescription();
}
