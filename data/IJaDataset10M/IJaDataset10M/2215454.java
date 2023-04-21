package bio.xml.echofunctions;

import net.bioclipse.xws.component.adhoc.function.FunctionInformation;
import net.bioclipse.xws.component.adhoc.function.IFunction;
import net.bioclipse.xws.component.xmpp.process.IProcessStatus;
import org.w3c.dom.Element;
import bio.xml.schemata.SchemaReader;

public class SBML implements IFunction {

    public FunctionInformation getFunctionInformation() {
        String functionname = "echoSBML";
        String description = "Echos a valid Systems Biology Markub Language (SBML) document.";
        String descriptiondetails = "Echos a valid Systems Biology Markub Language (SBML) document. See http://sbml.org to acquire further details about this XML Schema.";
        String schema = SchemaReader.readFile("sbml.xsd");
        FunctionInformation info = new FunctionInformation(functionname, description, descriptiondetails, schema, schema, false);
        return info;
    }

    public void run(IProcessStatus ps, Element input) {
        ps.setResult(input, "Echo result");
    }
}
