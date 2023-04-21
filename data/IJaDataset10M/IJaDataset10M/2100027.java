package org.exist.debuggee.dbgp.packets;

import java.util.HashMap;
import java.util.Map;
import org.apache.mina.core.session.IoSession;
import org.exist.dom.QName;
import org.exist.xquery.Variable;
import org.exist.xquery.XQueryContext;

/**
 * @author <a href="mailto:shabanovd@gmail.com">Dmitriy Shabanov</a>
 *
 */
public class ContextGet extends Command {

    /**
	 * stack depth (optional)
	 */
    private Integer stackDepth;

    /**
	 * context id (optional, retrieved by context-names)
	 */
    private String contextID;

    private Map<QName, Variable> variables;

    public ContextGet(IoSession session, String args) {
        super(session, args);
    }

    protected void init() {
        stackDepth = null;
        contextID = null;
    }

    protected void setArgument(String arg, String val) {
        if (arg.equals("d")) {
            stackDepth = Integer.parseInt(val);
        } else if (arg.equals("c")) {
            contextID = val;
        } else {
            super.setArgument(arg, val);
        }
    }

    @Override
    public void exec() {
        if (contextID == null || contextID.equals("")) variables = getJoint().getVariables(); else if (contextID.equals(ContextNames.LOCAL)) variables = getJoint().getLocalVariables(); else if (contextID.equals(ContextNames.GLOBAL)) variables = getJoint().getGlobalVariables(); else variables = new HashMap<QName, Variable>();
    }

    public byte[] responseBytes() {
        String response = xml_declaration + "<response " + namespaces + "command=\"context_get\" " + "context=\"" + contextID + "\" " + "transaction_id=\"" + transactionID + "\"> " + getPropertiesString() + "</response>";
        return response.getBytes();
    }

    private String getPropertiesString() {
        if (variables == null) return "";
        StringBuilder properties = new StringBuilder();
        XQueryContext ctx = getJoint().getContext();
        for (Variable variable : variables.values()) {
            properties.append(PropertyGet.getPropertyString(variable, ctx));
        }
        return properties.toString();
    }

    public byte[] commandBytes() {
        String command = "context_get -i " + transactionID;
        if (stackDepth != null) command += " -d " + String.valueOf(stackDepth);
        if (contextID != null && !contextID.equals("")) command += " -c " + contextID;
        return command.getBytes();
    }

    public void setContextID(String contextID) {
        this.contextID = contextID;
    }
}
