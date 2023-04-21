package uk.ac.manchester.cs.snee.client;

import java.io.IOException;
import org.apache.log4j.PropertyConfigurator;
import uk.ac.manchester.cs.snee.MetadataException;
import uk.ac.manchester.cs.snee.SNEEException;
import uk.ac.manchester.cs.snee.common.SNEEConfigurationException;
import uk.ac.manchester.cs.snee.data.generator.ConstantRatePushStreamGenerator;
import uk.ac.manchester.cs.snee.metadata.schema.SchemaMetadataException;

public class SNEEClientUsingTupleGeneratorSource extends SNEEClient {

    private static ConstantRatePushStreamGenerator _myDataSource;

    public SNEEClientUsingTupleGeneratorSource(String query, double duration, String csvFile) throws SNEEException, IOException, SNEEConfigurationException, MetadataException, SchemaMetadataException {
        super(query, duration, csvFile);
        if (logger.isDebugEnabled()) logger.debug("ENTER SNEEClietnUsingTupleGeneratorSource()");
        displayAllExtents();
        if (logger.isDebugEnabled()) logger.debug("RETURN SNEEClietnUsingTupleGeneratorSource()");
    }

    /**
	 * The main entry point for the SNEE controller
	 * @param args
	 * @throws IOException
	 * @throws InterruptedException 
	 */
    public static void main(String[] args) {
        PropertyConfigurator.configure(SNEEClientUsingTupleGeneratorSource.class.getClassLoader().getResource("etc/log4j.properties"));
        Long duration;
        String query;
        String csvFile = null;
        if (args.length != 2 && args.length != 3) {
            System.out.println("Usage: \n" + "\t\"query statement\"\n" + "\t\"query duration in seconds\"\n" + "\t[\"csv file to log results\"]\n");
            query = "SELECT * FROM PushStream;";
            duration = Long.valueOf("20");
            csvFile = null;
        } else {
            query = args[0];
            duration = Long.valueOf(args[1]);
            if (args.length == 3) {
                csvFile = args[2];
            }
        }
        try {
            SNEEClientUsingTupleGeneratorSource client = new SNEEClientUsingTupleGeneratorSource(query, duration, csvFile);
            _myDataSource = new ConstantRatePushStreamGenerator();
            _myDataSource.startTransmission();
            client.run();
            _myDataSource.stopTransmission();
        } catch (Exception e) {
            System.out.println("Execution failed. See logs for detail.");
            logger.fatal("Execution failed", e);
            System.exit(1);
        }
        System.out.println("Success!");
        System.exit(0);
    }
}
