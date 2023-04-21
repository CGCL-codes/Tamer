package org.jnetpcap.protocol.tcpip;

import org.jnetpcap.packet.analysis.JProtocolHandler;

/**
 * Callback method from Http protocol analyzer.
 * 
 * @author Mark Bednarczyk
 * @author Sly Technologies, Inc.
 */
public interface HttpHandler extends JProtocolHandler {

    public void processHttp(Http http);
}
