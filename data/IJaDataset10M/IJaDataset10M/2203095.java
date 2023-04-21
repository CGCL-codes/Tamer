package net.sourceforge.taverna.io;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import net.sourceforge.taverna.baclava.DataThingAdapter;

/**
 * This class is used to process file streams.
 * 
 * Last edited by $Author: wassinki $
 * 
 * @author Mark
 * @version $Revision: 1.1 $
 */
public class FileStreamProcessor extends AbstractStreamProcessor implements StreamProcessor {

    /**
	 * @see net.sourceforge.taverna.io.StreamProcessor#processStream(java.io.InputStream)
	 */
    public Map processStream(InputStream stream) throws IOException {
        HashMap outputMap = new HashMap();
        DataThingAdapter outAdapter = new DataThingAdapter(outputMap);
        StringBuffer sb = new StringBuffer(2000);
        try {
            BufferedReader rd = new BufferedReader(new InputStreamReader(stream));
            String str;
            while ((str = rd.readLine()) != null) {
                sb.append(str);
                sb.append(NEWLINE);
            }
            rd.close();
        } catch (IOException e) {
        }
        outAdapter.putString("outputText", sb.toString());
        return outputMap;
    }
}
