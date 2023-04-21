package net.bioclipse.xws.component.adhoc.function.testfunctions;

import net.bioclipse.xws.component.adhoc.function.FunctionInformation;
import net.bioclipse.xws.component.adhoc.function.IFunction;
import net.bioclipse.xws.component.xmpp.process.IProcessStatus;
import org.w3c.dom.Element;

public class TestFunctionWithError implements IFunction {

    public FunctionInformation getFunctionInformation() {
        String functionname = "testFunctionWithError";
        String functiondescription = "This is a test function that will fail with an error.";
        String description_details = "This is a test function that will fail with an error.";
        FunctionInformation info = new FunctionInformation(functionname, functiondescription, description_details, "<xs:schema xmlns:xs='http://www.w3.org/2001/XMLSchema' targetNamespace='urn:xws:testFunctionWithError:input' xmlns='urn:xws:testFunctionWithError:input' elementFormDefault='qualified'/>", "<xs:schema xmlns:xs='http://www.w3.org/2001/XMLSchema' targetNamespace='urn:xws:testFunctionWithError:output' xmlns='urn:xws:testFunctionWithError:output' elementFormDefault='qualified'/>", false);
        return info;
    }

    public void run(IProcessStatus ps, Element input) {
        ps.setError("The test function failed!");
    }
}
