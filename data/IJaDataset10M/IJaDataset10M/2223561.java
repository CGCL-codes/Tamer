package net.sourceforge.taverna.scuflworkers.xml;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.SourceLocator;
import javax.xml.transform.Templates;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import net.sourceforge.taverna.baclava.DataThingAdapter;
import net.sourceforge.taverna.io.FileNameUtil;
import org.embl.ebi.escience.scuflworkers.java.LocalWorker;
import uk.ac.soton.itinnovation.taverna.enactor.entities.TaskExecutionException;

/**
 * This processor transforms an input XML document into an output document. If
 * an inFileURL is supplied, it will use the document located at the URL as
 * input. If the xml-text is supplied, it will this in-memory XML document as
 * input. If an outputFile url is supplied, the results will be written to the
 * output document.
 * 
 * @author mfortner
 * @version $Revision: 1.1 $
 * 
 * @tavinput xslFileURL The complete path to XSL file.
 * @tavinput outFileURL The complete path to the output file. (optional)
 * @tavinput inFileURL The complete path to the input file.
 * @tavinput xml-text The XML text to be processed. (optional)
 * @tavinput outputExt The output file extension. Use this only if you want to
 *           add the extension to the input filename and use it as the output
 *           file name.
 * @tavoutput outputStr A string containing the output text. This is useful, if
 *            you want to connect this processor to another and pass the results
 *            to it.
 * 
 */
public class XSLTWorker implements LocalWorker {

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

    private BufferedWriter getWriter(final String fileUrl) throws IOException {
        Writer writer;
        try {
            writer = new FileWriter(fileUrl);
        } catch (FileNotFoundException e) {
            URL url = new URL(fileUrl);
            writer = new OutputStreamWriter(url.openConnection().getOutputStream());
        }
        return new BufferedWriter(writer);
    }

    public Map execute(Map inputMap) throws TaskExecutionException {
        DataThingAdapter inAdapter = new DataThingAdapter(inputMap);
        Map outputMap = new HashMap();
        DataThingAdapter outAdapter = new DataThingAdapter(outputMap);
        String xslFilename = inAdapter.getString("xslFileURL");
        String outFilename = inAdapter.getString("outFileURL");
        String inFilename = inAdapter.getString("inFileURL");
        String ext = inAdapter.getString("outputExt");
        if ((outFilename == null || outFilename.equals("")) && ext != null) {
            outFilename = FileNameUtil.replacePathExtension(inFilename, ext);
        }
        try {
            TransformerFactory factory = TransformerFactory.newInstance();
            Templates template = factory.newTemplates(new StreamSource(getReader(xslFilename)));
            Transformer xformer = template.newTransformer();
            Source source = new StreamSource(getReader(inFilename));
            StringWriter resultStr = new StringWriter();
            Result result = new StreamResult(resultStr);
            xformer.transform(source, result);
            String outText = resultStr.toString();
            outAdapter.putString("outputStr", outText);
            if (outFilename != null && !outFilename.equals("")) {
                try {
                    BufferedWriter out = getWriter(outFilename);
                    out.write(outText);
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (TransformerConfigurationException e) {
            e.printStackTrace();
        } catch (TransformerException e) {
            SourceLocator locator = e.getLocator();
            int col = locator.getColumnNumber();
            int line = locator.getLineNumber();
            String publicId = locator.getPublicId();
            String systemId = locator.getSystemId();
        }
        return outputMap;
    }

    public String[] inputNames() {
        return new String[] { "xslFileURL", "outFileURL", "inFileURL", "outputExt" };
    }

    public String[] inputTypes() {
        return new String[] { "'text/plain'", "'text/plain'", "'text/plain'", "'text/plain'" };
    }

    public String[] outputNames() {
        return new String[] { "outputStr" };
    }

    public String[] outputTypes() {
        return new String[] { "'text/xml'" };
    }
}
