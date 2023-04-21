package unbbayes.simulation.montecarlo.resources;

import java.util.ListResourceBundle;

/**
 * Resources for the monte carlo package.
 * @author Rommel Carvalho (rommel.carvalho@gmail.com)
 */
public class MCResources extends ListResourceBundle {

    /**
	 *  Override getContents and provide an array, where each item in the array is a pair
	 *	of objects. The first element of each pair is a String key,
	 *	and the second is the value associated with that key.
	 *
	 * @return The resources' contents
	 */
    public Object[][] getContents() {
        return contents;
    }

    /**
	 * The resources
	 */
    static final Object[][] contents = { { "netFileFilter", "Net (.net), XMLBIF (.xml)" }, { "textFileFilter", "Text (.txt)" }, { "mcTitle", "Sampling / Simulation" }, { "sampleSizeLbl", "Sample size :" }, { "ok", "OK" }, { "success", "Success" }, { "error", "Error" }, { "loadNetException", "Error to load net file" }, { "sampleSizeException", "The sample size must be an integer greater than zero" }, { "saveException", "Error while saving file with sampled states" }, { "saveSuccess", "File with sampled states saved successfully" }, { "selectFile", "You must select a file to start sampling. " }, { "openFile", "Open network file for sampling" } };
}
