package edu.ucdavis.genomics.metabolomics.binbase.tools;

import java.io.OutputStream;
import java.util.Collection;
import java.util.Vector;
import edu.ucdavis.genomics.metabolomics.binbase.bdi.math.BinSimilarityMatrix;
import edu.ucdavis.genomics.metabolomics.util.config.XMLConfigurator;
import edu.ucdavis.genomics.metabolomics.util.database.ConnectionFactory;
import edu.ucdavis.genomics.metabolomics.util.database.DriverUtilities;
import edu.ucdavis.genomics.metabolomics.util.io.dest.FileDestination;
import edu.ucdavis.genomics.metabolomics.util.io.source.ResourceSource;
import edu.ucdavis.genomics.metabolomics.util.statistics.data.DataFile;

public class CalculateSimilarityMatrix {

    /**
	 * @param args
	 */
    public static void main(String args[]) throws Exception {
        if (args.length <= 6) {
            System.out.println("USAGE: ");
            System.out.println("args[0]:     database server");
            System.out.println("args[1]:     username");
            System.out.println("args[2]:     password");
            System.out.println("args[3]:     database name");
            System.out.println("args[4]:     directory");
            System.out.println("args[5...n]: bin ids to calculate similarities");
            System.out.println("found amount of attributes: " + args.length);
            return;
        }
        int ids[] = new int[args.length - 5];
        for (int i = 5; i < args.length; i++) {
            System.out.println("adding: " + args[i]);
            ids[i - 5] = Integer.parseInt(args[i]);
        }
        System.setProperty(ConnectionFactory.KEY_USERNAME_PROPERTIE, args[1]);
        System.setProperty(ConnectionFactory.KEY_HOST_PROPERTIE, args[0]);
        System.setProperty(ConnectionFactory.KEY_PASSWORD_PROPERTIE, args[2]);
        System.setProperty(ConnectionFactory.KEY_DATABASE_PROPERTIE, args[3]);
        if (System.getProperty(ConnectionFactory.KEY_TYPE_PROPERTIE) == null) {
            System.setProperty(ConnectionFactory.KEY_TYPE_PROPERTIE, String.valueOf(DriverUtilities.POSTGRES));
        }
        XMLConfigurator.getInstance().addConfiguration(new ResourceSource("/config/hibernate.xml"));
        BinSimilarityMatrix matrix = new BinSimilarityMatrix();
        DataFile data = matrix.createListVsListMatrix(ids, ids);
        FileDestination dest = new FileDestination(args[4]);
        dest.setIdentifier("result-matrix.txt");
        OutputStream out = dest.getOutputStream();
        data.write(out);
        out.flush();
        out.close();
    }
}
