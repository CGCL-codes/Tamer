package edu.psu.citeseerx.loaders;

import java.io.IOException;
import org.springframework.beans.factory.ListableBeanFactory;
import edu.psu.citeseerx.updates.HomePageStatisticsGenerator;

/**
 * Loads the HomePageStatisticsGenerator bean and runs genStats, using the 
 * supplied command line argument if there is one.
 * @author Juan Pablo Fernandez Ramirez
 * @version $Rev: 810 $ $Date: 2008-12-02 14:05:57 -0500 (Tue, 02 Dec 2008) $
 */
public class HomePageStatisticsGeneratorLoader {

    /**
	 * @param args
	 */
    public static void main(String[] args) throws IOException {
        ListableBeanFactory factory = ContextReader.loadContext();
        HomePageStatisticsGenerator generator = (HomePageStatisticsGenerator) factory.getBean("homePageStatisticsGenerator");
        if (args.length > 0) {
            generator.genStats(args[0]);
        } else {
            generator.genStats();
        }
    }
}
