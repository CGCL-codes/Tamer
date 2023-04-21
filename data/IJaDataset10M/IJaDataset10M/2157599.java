package org.tripcom.eai.scalability.loader;

import java.net.InetAddress;
import org.tripcom.api.ws.client.TSClient;
import org.tripcom.integration.entry.SpaceURI;

/**
 * Executable class that prepares the data loading in a flat subspace structure.
 * @author davidfr
 * @see DataLoader
 */
public class WriteFlatSubspaces {

    /**
	 * Load data into a given number of flat subspaces to be created in a kernel. Flat in this context means to create N subspaces
	 * that are subspaces of the root space directly (in contrast to nested subspaces). 
	 * @param args This method expects 4 arguments:
	 * 	1. The kernel name (to create the ts Client).
	 *  2. The port number (to create the ts Client).
	 *  3. The number of subspaces to be created (a long number).
	 *  4. The number of times that the canonical file (100 Assets) will be stored IN EACH subspace.
	 * @throws Exception If any error occurs.
	 */
    public static void main(String[] args) throws Exception {
        if (args.length < 4) throw new RuntimeException("4 arguments are expected: {kernel name} {port} {number of subspaces to create in the kernel} {number of times the canonical file will be stored in each subspace} ");
        DataLoader dataLoader = new DataLoader();
        TSClient tsClient = new TSClient(DataLoader.PROTOCOL, InetAddress.getByName(args[0]), Integer.parseInt(args[1]));
        dataLoader.setTsClient(tsClient);
        long counter = 1;
        int subspaces = Integer.parseInt(args[2]);
        int repetitions = Integer.parseInt(args[3]);
        tsClient.create(new SpaceURI(DataLoader.DEFAULT_ROOT_SPACE_URI));
        for (long i = 0; i < subspaces; i++) {
            String subspaceUri = DataLoader.DEFAULT_ROOT_SPACE_URI + "/subspace_" + dataLoader.subspaceIdFormatter.format(i);
            tsClient.create(new SpaceURI(subspaceUri));
            for (long j = 0; j < repetitions; j++) {
                String data = dataLoader.generateDataPack(args[0], subspaceUri, counter);
                dataLoader.writeRdfString(data, new SpaceURI(subspaceUri));
                counter += 100;
            }
            counter = 1;
        }
    }
}
