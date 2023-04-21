package model.channel.set;

import model.channel.priority.*;
import model.channel.*;

public class NetworkValueSet extends AValueSet {

    public NetworkValueSet() {
        super((int) ChannelValues.NETWORK_SOURCE, new FaderDuckingPriority(10000, 100));
    }
}
