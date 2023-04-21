package com.jpmorrsn.fbp.test.networks;

import com.jpmorrsn.fbp.components.Discard;
import com.jpmorrsn.fbp.components.Generate;
import com.jpmorrsn.fbp.components.ReplString;
import com.jpmorrsn.fbp.engine.Network;

/** This network is intended for timing runs */
public class VolumeTest extends Network {

    static final String copyright = "Copyright 2007, 2008, 2012, J. Paul Morrison.  At your option, you may copy, " + "distribute, or make derivative works under the terms of the Clarified Artistic License, " + "based on the Everything Development Company's Artistic License.  A document describing " + "this License may be found at http://www.jpaulmorrison.com/fbp/artistic2.htm. " + "THERE IS NO WARRANTY; USE THIS PRODUCT AT YOUR OWN RISK.";

    @Override
    protected void define() {
        connect(component("Generate", Generate.class), port("OUT"), component("ReplString ", ReplString.class), port("IN"));
        connect(component("ReplString "), port("OUT", 0), component("ReplString 2", ReplString.class), port("IN"));
        connect(component("ReplString 2"), port("OUT", 0), component("ReplString 3", ReplString.class), port("IN"));
        connect(component("ReplString 3"), port("OUT", 0), component("ReplString 4", ReplString.class), port("IN"));
        connect(component("ReplString 4"), port("OUT", 0), component("ReplString 5", ReplString.class), port("IN"));
        connect(component("ReplString 5"), port("OUT", 0), component("ReplString 6", ReplString.class), port("IN"));
        connect(component("ReplString 6"), port("OUT", 0), component("ReplString 7", ReplString.class), port("IN"));
        connect(component("ReplString 7"), port("OUT", 0), component("ReplString 8", ReplString.class), port("IN"));
        connect(component("ReplString 8"), port("OUT", 0), component("Discard", Discard.class), port("IN"));
        initialize("20000", component("Generate"), port("COUNT"));
    }

    public static void main(final String[] argv) throws Exception {
        new VolumeTest().go();
    }
}
