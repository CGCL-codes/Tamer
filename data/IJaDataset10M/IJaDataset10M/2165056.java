package org.tripcom.dam.test;

import java.net.InetAddress;
import java.net.URI;
import java.net.UnknownHostException;
import org.tripcom.api.ws.client.TSClient;
import org.tripcom.dam.utils.Literals;

public class OutTest {

    /**
	 * @param args
	 * @throws UnknownHostException 
	 */
    public static void main(String[] args) throws Exception {
        TSClient tsClient = new TSClient(Literals.protocol, InetAddress.getByName(Literals.host), Literals.port);
        URI spaceAsURI = new URI(Literals.space);
        System.out.println(tsClient.outmultiple(args[0], spaceAsURI));
    }
}
