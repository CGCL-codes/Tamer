package net.sourceforge.taverna.scuflworkers.biojava;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import net.sourceforge.taverna.baclava.DataThingAdapter;
import org.biojava.bio.BioException;
import org.biojava.bio.seq.Sequence;
import org.biojava.bio.seq.SequenceIterator;
import org.biojava.bio.seq.io.SeqIOTools;
import org.biojava.bio.seq.io.agave.AgaveWriter;
import org.embl.ebi.escience.scuflworkers.java.LocalWorker;
import uk.ac.soton.itinnovation.taverna.enactor.entities.TaskExecutionException;

/**
 * This class parses genbank files and outputs the results in Agave XML format.
 * 
 * Last edited by $Author: wassinki $
 * 
 * @author Mark
 * @version $Revision: 1.1 $
 */
public class GenBankParserWorker implements LocalWorker {

    public GenBankParserWorker() {
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
        DataThingAdapter inAdapter = new DataThingAdapter(inputMap);
        String fileUrl = inAdapter.getString("fileUrl");
        BufferedReader br = null;
        HashMap outputMap = new HashMap();
        DataThingAdapter outAdapter = new DataThingAdapter(outputMap);
        try {
            br = getReader(fileUrl);
            SequenceIterator sequences = SeqIOTools.readGenbank(br);
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            StringBuffer sb = new StringBuffer();
            AgaveWriter writer = new AgaveWriter();
            while (sequences.hasNext()) {
                Sequence seq = sequences.nextSequence();
                writer.writeSequence(seq, new PrintStream(os));
                sb.append(os.toString());
            }
            outAdapter.putString("genbankdata", sb.toString());
        } catch (FileNotFoundException ex) {
            throw new TaskExecutionException(ex);
        } catch (BioException ex) {
            throw new TaskExecutionException(ex);
        } catch (NoSuchElementException ex) {
            throw new TaskExecutionException(ex);
        } catch (IOException io) {
            throw new TaskExecutionException(io);
        }
        return outputMap;
    }

    /**
	 * @see org.embl.ebi.escience.scuflworkers.java.LocalWorker#inputNames()
	 */
    public String[] inputNames() {
        return new String[] { "fileUrl" };
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
        return new String[] { "genbankdata" };
    }

    /**
	 * @see org.embl.ebi.escience.scuflworkers.java.LocalWorker#outputTypes()
	 */
    public String[] outputTypes() {
        return new String[] { "'text/xml'" };
    }
}
