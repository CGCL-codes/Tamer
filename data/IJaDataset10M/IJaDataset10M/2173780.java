package polyester.programming;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lights.interfaces.ITuple;
import lights.interfaces.ITupleSpace;
import lights.interfaces.IValuedField;
import lights.interfaces.TupleSpaceException;
import polyester.AbstractWorker;

/**
 * This class is an agent that executes a behavior defined by rules.
 * 
 * @author adavoust
 *
 */
public class ProgramWorker extends AbstractWorker {

    protected Map<ITuple, List<TupleInstruction>> instructions;

    /**
	 * store the variables for the execution
	 */
    protected Map<String, String> variables;

    public ProgramWorker() {
        super(null);
    }

    public ProgramWorker(ITupleSpace ts) {
        super(ts);
        instructions = new HashMap<ITuple, List<TupleInstruction>>();
        variables = new HashMap<String, String>();
    }

    @Override
    protected List<ITuple> answerQuery(ITuple template, ITuple query) {
        LOG.debug(name + " : got something-------------====-------------------   ---- : " + query.toString());
        extractVariables(template, query);
        List<TupleInstruction> toExecute = instructions.get(template);
        for (TupleInstruction instruction : toExecute) {
            LOG.debug(name + " : next intruction: " + instruction.toString());
            int verb = instruction.getInstruction();
            ITuple intuple;
            try {
                if (verb == TupleInstruction.FUNCTION) {
                    processFunction(instruction.getTuple());
                } else {
                    ITuple newtuple = populateTuple(instruction.getTuple());
                    switch(verb) {
                        case TupleInstruction.OUT:
                            space.out(newtuple);
                            break;
                        case TupleInstruction.READ:
                            intuple = space.rd(newtuple);
                            extractVariables(newtuple, intuple);
                            break;
                        case TupleInstruction.IN:
                            intuple = space.in(newtuple);
                            extractVariables(newtuple, intuple);
                            break;
                        default:
                            LOG.error(name + "error: unknown tuple instruction verb");
                    }
                }
            } catch (TupleSpaceException e) {
                LOG.error(e);
            }
        }
        LOG.info(name + " has completed a cycle of instructions");
        return null;
    }

    /**
	 * Start a new rule 
	 * @param head the head of the new rule
	 * @throws IllegalArgumentException if the new rule's head isn't a 'read' or 'in' instruction
	 */
    public void newRule(TupleInstruction head) throws IllegalArgumentException {
        if (head.getInstruction() == TupleInstruction.READ || head.getInstruction() == TupleInstruction.IN) {
            List<TupleInstruction> tail = new ArrayList<TupleInstruction>();
            if (head.getInstruction() == TupleInstruction.IN) tail.add((TupleInstruction) head.clone());
            instructions.put(head.getTuple(), tail);
            addQueryTemplate(head.getTuple());
        } else {
            LOG.error("Error: agent " + name + "'s first instruction is not 'READ' or 'IN'");
            throw (new IllegalArgumentException("Error: Illegal rule head: " + head.toString()));
        }
    }

    public void addInstruction(ITuple head, TupleInstruction tuplinst) throws IllegalArgumentException {
        List<TupleInstruction> sequence = instructions.get(head);
        if (sequence == null) throw (new IllegalArgumentException("Error: Rule head not found: " + head.toString())); else sequence.add(tuplinst);
    }

    public void work() {
        variables.clear();
        super.work();
    }

    /**
	 * Populate a tuple with variable values from the "stack"
	 * a field that needs populating is a NameValueField of type litteral with a non-null varName
	 * @param tuple the tuple to populate
	 */
    private ITuple populateTuple(ITuple tuple) {
        ITuple newtuple = (ITuple) tuple.clone();
        NameValueField outfield;
        for (int i = 0; i < newtuple.getFields().length; i++) {
            if (newtuple.get(i) instanceof NameValueField) {
                outfield = (NameValueField) newtuple.get(i);
                if (!outfield.isFormal() && outfield.getVarName() != null) {
                    String value = variables.get(outfield.getVarName());
                    if (value != null) {
                        outfield.setValue(value);
                        outfield.setVarName(null);
                    }
                }
            }
        }
        return newtuple;
    }

    /**
	 *  read variables from a read tuple
	 * @param template the template used to read the input tuple
	 * @param intuple the input tuple
	 */
    private void extractVariables(ITuple template, ITuple intuple) {
        NameValueField templatefield;
        IValuedField inputfield;
        for (int i = 0; i < template.getFields().length; i++) {
            if (template.get(i) instanceof NameValueField && intuple.get(i) instanceof IValuedField) {
                templatefield = (NameValueField) template.get(i);
                inputfield = (IValuedField) intuple.get(i);
                if (templatefield.isFormal() && templatefield.getVarName() != null) {
                    if (inputfield.getType() == String.class) variables.put(templatefield.getVarName(), (String) (inputfield.getValue()));
                }
            }
        }
    }

    private void processFunction(ITuple tuple) {
    }

    public String listInstructions() {
        String res = "Instruction list:\n";
        for (ITuple head : instructions.keySet()) {
            res = res + "head: (read)" + head.toString() + "\n tail:";
            for (TupleInstruction inst : instructions.get(head)) {
                res = res + inst.toString() + "\n";
            }
        }
        return res;
    }

    public static String getResourceIdFromURI(String uri) {
        return uri.substring(uri.lastIndexOf('/' + 1));
    }

    public static String getCommunityIdFromURI(String uri) {
        return uri.substring(5, uri.lastIndexOf('/'));
    }
}
