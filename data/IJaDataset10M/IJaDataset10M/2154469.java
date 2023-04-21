package org.tripcom.eai.scalability.loader;

import java.net.InetAddress;
import org.tripcom.api.ws.client.TSClient;
import org.tripcom.integration.entry.SpaceURI;

public class WriteCustomFile {

    /**
	 * Load a custom rdf file in a given space. The path of the root space is defined as an argument of the operation.
	 * @param args This method expects 4 arguments:
	 * 	1. The kernel name (to create the ts Client).
	 *  2. The port number (to create the ts Client).
	 *  3. The space to which the data file is to be stored.
	 *  4. The file name to be stored in the given space.
	 * @throws Exception If any error occurs.
	 */
    public static void main(String[] args) throws Exception {
        if (args.length < 4) throw new RuntimeException("4 arguments are expected: {kernel name} {port} {space name} {file name to be stored}");
        DataLoader dataLoader = new DataLoader();
        TSClient tsClient = new TSClient(DataLoader.PROTOCOL, InetAddress.getByName(args[0]), Integer.parseInt(args[1]));
        dataLoader.setTsClient(tsClient);
        SpaceURI space = new SpaceURI(args[2]);
        tsClient.create(space);
        dataLoader.writeRdfFile(tsClient, args[3], space);
    }
}
