package net.sf.nxtassembler.library.lang;

import java.util.Hashtable;
import java.util.List;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import net.sf.nxtassembler.exception.AssemblerException;
import net.sf.nxtassembler.parser.typedef.ClusterDef;
import net.sf.nxtassembler.parser.typedef.RecordDef;
import net.sf.nxtassembler.types.DataType;
import net.sf.nxtassembler.util.Logging;
import net.sf.nxtassembler.util.RandomString;

/**
 * 
 * @author Matteo Valdina, zanfire@users.sourceforge.net
 * @version 1.0
 *
 */
public abstract class FunctionMerger extends Function implements Cloneable {

    protected static final Hashtable<String, FunctionMerger> singletonsFunctionInstance = new Hashtable<String, FunctionMerger>();

    protected Document dom = null;

    protected Element dataspaceElement = null;

    protected Element callFunctionElement = null;

    protected String currentRandomString = RandomString.generate(24);

    public FunctionMerger(Function f, Element callFunction) throws AssemblerException {
        super(f.functionName);
        f = (Function) ((Function) f).clone();
        this.code = f.code;
        this.params = f.params;
        this.dataspace = f.dataspace;
        this.dom = callFunction.getOwnerDocument();
        this.callFunctionElement = callFunction;
        NodeList nl = dom.getElementsByTagName("Dataspace");
        if (nl.getLength() == 1) {
            this.dataspaceElement = (Element) nl.item(0);
        } else {
            Logging.fatal("Duplicated dataspace?");
        }
    }

    public abstract List<Code> getInlineCode();

    public abstract void mergeParam(RecordDef param, Param value);

    public abstract void firstInitialization();

    public void merge(List<Param> invocationParam) throws AssemblerException {
        if (!singletonsFunctionInstance.containsKey(getSignature())) {
            mergeDataspace();
            firstInitialization();
            singletonsFunctionInstance.put(getSignature(), (FunctionMerger) this.clone());
        } else {
            dataspace.clear();
            code.clear();
            params.clear();
            FunctionMerger f = singletonsFunctionInstance.get(getSignature());
            for (int i = 0; i < params.size(); i++) {
                params.add((RecordDef) f.params.get(i).clone());
            }
            for (int i = 0; i < code.size(); i++) {
                code.add((Code) f.code.get(i).clone());
            }
        }
        mergeParam(invocationParam);
        List<Code> inline = getInlineCode();
        if (inline.size() == 0) throw new RuntimeException("I hope a programming error.");
        Node parent = callFunctionElement.getParentNode();
        Node previus = null;
        for (int i = inline.size() - 1; i >= 0; i--) {
            Code code = inline.get(i);
            Node current = code.getNodePresenter(dom);
            if (i == inline.size() - 1) {
                parent.replaceChild(current, callFunctionElement);
            } else {
                parent.insertBefore(current, previus);
            }
            previus = current;
        }
    }

    /**
	 * This method merge params into the invocation of this function.
	 * 
	 * @param invocationParam
	 * @throws AssemblerException
	 */
    private void mergeParam(List<Param> invocationParam) throws AssemblerException {
        if (invocationParam.size() != params.size()) {
            Logging.fatal("Expected arguments are " + Integer.toString(params.size()) + " but in the function " + "arguments are " + Integer.toString(invocationParam.size()));
        }
        for (int i = 0; i < invocationParam.size(); i++) {
            RecordDef param = params.get(i);
            Param instance = invocationParam.get(i);
            if (param.compareTo(instance.ref) == 0) {
                mergeParam(param, instance);
            } else {
                Logging.fatal("Expected arguemnt has type " + param.type.toString() + " but the passed argument is " + instance.ref.type.toString());
            }
        }
    }

    /**
	 * This method check if the values passed to this function are correct 
	 * and if it is the case and the <code>merge</code> argument is 
	 * <code>true</code> merge these values in the InlineFunction.
	 *  
	 * @param values
	 * @param merge
	 * @throws AssemblerException
	 */
    protected void checkParams(List<Param> values, boolean merge) throws AssemblerException {
        if (values.size() != params.size()) {
            Logging.fatal("Expected arguments are " + Integer.toString(params.size()) + " but in the function " + "arguments are " + Integer.toString(values.size()));
        }
        for (int i = 0; i < values.size(); i++) {
            RecordDef param = params.get(i);
            Param value = values.get(i);
            if (param.compareTo(value.ref) == 0) {
                if (merge) {
                    mergeParam(param, value);
                }
            } else {
                Logging.fatal("Expected arguemnt has type " + param.type.toString() + " but the passed argument is " + value.ref.type.toString());
            }
        }
    }

    protected static void newLabel(Function f, String oldLabel, String newLabel) {
        Logging.debug("Rename Label from " + oldLabel + " to " + newLabel);
        for (int i = 0; i < f.code.size(); i++) {
            Code c = f.code.get(i);
            replaceArgument(c, "to", oldLabel, newLabel);
        }
    }

    protected void updateDataspaceName(String oldName, String newName) {
        Logging.debug("Rename Record from " + oldName + " to " + newName);
        for (int i = 0; i < code.size(); i++) {
            Code c = code.get(i);
            replaceArgument(c, "source", oldName, newName);
            replaceArgument(c, "source1", oldName, newName);
            replaceArgument(c, "source2", oldName, newName);
            replaceArgument(c, "destination", oldName, newName);
            replaceArgument(c, "index", oldName, newName);
            replaceArgument(c, "count", oldName, newName);
            replaceArgument(c, "newVal", oldName, newName);
            replaceArgument(c, "error", oldName, newName);
            replaceArgument(c, "default", oldName, newName);
            replaceArgument(c, "indexPast", oldName, newName);
            replaceArgument(c, "confirm", oldName, newName);
            replaceArgument(c, "mutexID", oldName, newName);
            replaceArgument(c, "clumpID", oldName, newName);
            replaceArgument(c, "callerID", oldName, newName);
            replaceArgument(c, "parmCluster", oldName, newName);
            replaceArgument(c, "port", oldName, newName);
            for (int x = 0; x < c.extendedArgs.size(); x++) {
                ExtendedArg a = c.extendedArgs.get(x);
                replaceExtendedArgument(a, "source", oldName, newName);
            }
        }
    }

    protected static void replaceArgument(Code c, String argument, String oldValue, String newValue) {
        String value = c.getArgumentValue(argument);
        if (value != null) {
            if (oldValue.compareTo(value) == 0) {
                c.setArgumentValue(argument, newValue);
            } else {
                int dot = value.indexOf('.');
                if (dot >= 0 && dot <= value.length()) {
                    String s = value.substring(0, dot);
                    if (oldValue.compareTo(s) == 0) {
                        c.setArgumentValue(argument, newValue + "." + value.substring(dot + 1));
                    }
                }
            }
        }
    }

    protected static void replaceExtendedArgument(ExtendedArg c, String argument, String oldValue, String newValue) {
        String value = c.getArgumentValue(argument);
        if (value != null) {
            if (oldValue.compareTo(value) == 0) {
                c.setArgumentValue(argument, newValue);
            } else {
                int dot = value.indexOf('.');
                if (dot >= 0 && dot <= value.length()) {
                    String s = value.substring(0, dot);
                    if (oldValue.compareTo(s) == 0) {
                        c.setArgumentValue(argument, newValue + "." + value.substring(dot + 2));
                    }
                }
            }
        }
    }

    public void mergeDataspace() {
        for (int i = 0; i < dataspace.size(); i++) {
            RecordDef def = dataspace.get(i);
            String oldName = def.name;
            String newName = def.name + "_" + currentRandomString;
            def.name = newName;
            updateDataspaceName(oldName, newName);
            if (def.type == DataType.CLUSTER) {
                List<Code> list = ((ClusterDef) def).getCodeSets();
                for (Code c : list) {
                    code.add(0, c);
                }
            } else if (def.type == DataType.ARRAY) {
                Logging.error("Case unhandled");
            } else if (def.type == DataType.MUTEX) {
                Logging.error("Case unhandled");
            } else {
                Code current = def.getCodeSet();
                code.add(0, current);
            }
            dataspaceElement.appendChild(def.getXmlPresenter(dom));
        }
    }

    @Override
    public abstract Object clone();
}
