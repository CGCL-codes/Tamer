package net.sourceforge.taverna.scuflworkers.io;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import net.sourceforge.taverna.baclava.DataThingAdapter;
import org.embl.ebi.escience.scuflworkers.java.LocalWorker;
import uk.ac.soton.itinnovation.taverna.enactor.entities.TaskExecutionException;

/**
 * This processor reads text from a file specified by the "fileurl" attribute.
 * and returns the results in the "filecontents" item in the outputMap.
 * 
 * 
 * 
 * @author Mark
 * @version $Revision: 1.1 $
 * 
 * @tavinput fileurl The complete path to the text file to be read.
 * @tavoutput filecontents The contents of the text file.
 */
public class TextFileReader implements LocalWorker {

    public TextFileReader() {
    }

    private BufferedReader getReader(final String fileUrl) throws IOException {
        InputStreamReader reader;
        try {
            reader = new FileReader(fileUrl);
        } catch (FileNotFoundException e) {
            URL url = new URL(fileUrl);
            reader = new InputStreamReader(url.openStream());
        }
        return new BufferedReader(reader);
    }

    /**
	 * @see org.embl.ebi.escience.scuflworkers.java.LocalWorker#execute(java.util.Map)
	 */
    public Map execute(Map inputMap) throws TaskExecutionException {
        DataThingAdapter inputAdapter = new DataThingAdapter(inputMap);
        String fileurl = inputAdapter.getString("fileurl");
        Map outputMap = new HashMap();
        DataThingAdapter outputAdapter = new DataThingAdapter(outputMap);
        StringBuffer sb = new StringBuffer(4000);
        try {
            BufferedReader in = getReader(fileurl);
            String str;
            String lineEnding = System.getProperty("line.separator");
            while ((str = in.readLine()) != null) {
                sb.append(str);
                sb.append(lineEnding);
            }
            in.close();
        } catch (IOException e) {
            throw new TaskExecutionException(e);
        }
        outputAdapter.putString("filecontents", sb.toString());
        return outputMap;
    }

    /**
	 * @see org.embl.ebi.escience.scuflworkers.java.LocalWorker#inputNames()
	 */
    public String[] inputNames() {
        return new String[] { "fileurl" };
    }

    /**
	 * @see org.embl.ebi.escience.scuflworkers.java.LocalWorker#inputTypes()
	 */
    public String[] inputTypes() {
        return new String[] { "'text/plain'" };
    }

    /**
	 * @see org.embl.ebi.escience.scuflworkers.java.LocalWorker#outputNames()
	 */
    public String[] outputNames() {
        return new String[] { "filecontents" };
    }

    /**
	 * @see org.embl.ebi.escience.scuflworkers.java.LocalWorker#outputTypes()
	 */
    public String[] outputTypes() {
        return new String[] { "'text/plain'" };
    }
}
