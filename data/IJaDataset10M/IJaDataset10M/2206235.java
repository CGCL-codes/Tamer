package net.sourceforge.ondex.ws_tester.mapping;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import net.sourceforge.ondex.exception.type.PluginException;
import net.sourceforge.ondex.init.Initialisation;
import net.sourceforge.ondex.webservice.client.WebserviceException_Exception;
import net.sourceforge.ondex.webservice.client.mapping.ArrayOfString;
import net.sourceforge.ondex.ws_tester.inputs.MappingArrayOfString;
import net.sourceforge.ondex.ws_tester.parser.MedlineParserTester;
import org.xml.sax.SAXException;

/**
 * Program for creating plugin wrappers.
 *
 * Designed only to run when Christian Server is running.
 * 
 * @author BrennincC based on stuff by taubertj
 */
public class TmbasedTester extends WS_Tester_Mapping {

    public TmbasedTester() throws MalformedURLException, net.sourceforge.ondex.webservice.client.parser.WebserviceException_Exception, WebserviceException_Exception, IOException {
        super("OXL tests");
    }

    public long mapFile(long graphid) throws net.sourceforge.ondex.webservice.client.mapping.WebserviceException_Exception, WebserviceException_Exception {
        String[] cc = { "Comp", "Reaction" };
        ArrayOfString ConceptClass = new MappingArrayOfString(cc);
        String result;
        result = mapping_service.tmbasedMapping(ConceptClass, null, null, "exact", null, graphid);
        System.out.println(result);
        graphInfo(graphid, false);
        return graphid;
    }
}
