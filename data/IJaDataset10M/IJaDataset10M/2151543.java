package com.volantis.integration.mcs.protocols.voicexml;

import com.volantis.mcs.protocols.VolantisProtocol;
import com.volantis.mcs.protocols.builder.ProtocolBuilder;
import com.volantis.mcs.protocols.builder.ProtocolRegistry;

/**
 * This class provides the implement for the Abstract integration test case for
 * Voice XML.
 *
 * Note that the following tests:
 * <ul>
 * <li>testWriteOpenParagraph()</li>
 * <li>testWriteOpenRowIteratorPane()</li>
 * <li>testWriteOpenPane()</li>
 * <li>testWriteOpenLayout()</li>
 * </ul>
 *
 * did not complete successfully for the VoiceXML protocol (when it was a
 * StringProtocol.
 */
public class VoiceXMLVersion1_0IntegrationTestCase extends VoiceXMLRootIntegrationTestAbstract {

    protected VolantisProtocol getProtocol() {
        ProtocolBuilder protocolBuilder = new ProtocolBuilder();
        VolantisProtocol protocol = protocolBuilder.build(new ProtocolRegistry.VoiceXMLVersion1_0Factory(), internalDevice);
        return protocol;
    }

    public void testWriteOpenInclusion() throws Exception {
        super.testWriteOpenInclusion();
    }
}
