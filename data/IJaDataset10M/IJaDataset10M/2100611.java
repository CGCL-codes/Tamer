package com.volantis.mcs.protocols.html;

import com.volantis.mcs.protocols.ProtocolConfiguration;
import com.volantis.mcs.protocols.ProtocolSupportFactory;

/**
 * This is the Netscape 4 specialisation of the HTMLVersion4_0. It only needs to
 * set the CSS emulation properties up correctly.
 */
public class HTMLVersion4_0_NS4 extends HTMLVersion4_0 {

    public HTMLVersion4_0_NS4(ProtocolSupportFactory supportFactory, ProtocolConfiguration configuration) {
        super(supportFactory, configuration);
    }
}
