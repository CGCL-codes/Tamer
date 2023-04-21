package benchmark1.benchmarkws;

import java.io.*;
import java.util.List;
import java.util.Stack;
import java.util.ArrayList;
import com.dilanperera.rapidws.core.TypeHelper;
import com.dilanperera.rapidws.handlers.operation.ParameterExtractionResult;
import com.dilanperera.rapidws.handlers.operation.BaseOperationHandler;

/**
 * Handles the 'ReceiveBase64' Web Service operation.
 */
public class ReceiveBase64OperationHandler extends BaseOperationHandler {

    /**
	* The output of the operation.
	*/
    private String output = "";

    /**
	* Represents the stack describing the depth to which a parameter nests.
	*/
    private Stack nestingDepthStack = new Stack();

    private String input = null;

    private Integer __rws__returnValue;

    private boolean __rws__isInputNull = false;

    /**
	 * Handles the operation.
	 *
	 * @param input the input to the operation.
	 * @return String the output of the operation.
	 * @throws Exception when an error occurs during the operation.
	 */
    protected final String handleOperation(String input) throws Exception {
        this.setInput(input);
        this.deserializeInput();
        this.executeOperation();
        this.serializeOutput();
        return this.output;
    }

    /**
	 * Gets the return action.
	 *
	 * @return the return action.
	 */
    public String getReturnAction() {
        return "receiveBase64Response";
    }

    /**
	 * Deserializes the input.
	 *
	 * @throws Exception when an error occurs during the operation.
	 */
    protected void deserializeInput() throws Exception {
        this.resetCurrentPosition();
        if (this.readToFirstParameter()) {
            ParameterExtractionResult result = new ParameterExtractionResult();
            result.setExpectedParameterFound(true);
            result = this.readPrimitiveParameterData("input", result);
            if (result.wasExpectedParameterFound()) {
                if (result.isNil()) {
                    this.__rws__isInputNull = true;
                } else {
                    this.input = String.valueOf(result.getParameterValue());
                }
            }
        }
        this.resetCurrentPosition();
    }

    /**
	 * Executes the service operation.
	 *
	 * @throws Exception when an error occurs during the operation.
	 */
    protected void executeOperation() throws Exception {
        this.__rws__returnValue = new benchmark1.Benchmark1Impl().receiveBase64((this.__rws__isInputNull) ? null : TypeHelper.getBytes(input));
    }

    /**
	 * Serializes the output.
	 *
	 * @throws Exception when an error occurs during the operation.
	 */
    protected void serializeOutput() throws Exception {
        this.output += "<?xml version='1.0' encoding='UTF-8'?>";
        this.output += "<soapenv:Envelope xmlns:soapenv=\"http://www.w3.org/2003/05/soap-envelope\">";
        this.output += "<soapenv:Body>";
        this.output += "<ns:receiveBase64Response xmlns:ns=\"http://benchmark1\">";
        this.output += "<ns:return>" + this.__rws__returnValue.toString() + "</ns:return>";
        this.output += "</ns:receiveBase64Response>";
        this.output += "</soapenv:Body>";
        this.output += "</soapenv:Envelope>";
    }
}
