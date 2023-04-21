package nl.uva.science.luna.intermediate.interpreter;

import java.io.InputStream;
import java.util.Stack;
import java.util.Hashtable;
import nl.uva.science.luna.intermediate.language.Factory;
import nl.uva.science.luna.intermediate.language.types.Instr;
import nl.uva.science.luna.intermediate.language.types.InstrList;
import nl.uva.science.luna.intermediate.language.types.LabelInstr;
import aterm.ATermList;
import aterm.pure.PureFactory;

/**
 * @author Robert Baarda
 *
 * Class needed to execute the intermediate stack language.
 */
public final class Interpreter {

    private enum NatOperator {

        Add, Multiply, Division
    }

    private enum CompareOperator {

        IfEqual, IfLessOrEqual, IfLess
    }

    private Stack<Instr> m_stack;

    private Hashtable<Integer, Instr> m_register;

    private ATermList m_instructionATermList;

    private Factory m_factory;

    private static Interpreter m_interpreter;

    /**
	 * Declare private constructor.
	 * An object of this class can be retrieved through getInstance().
	 */
    private Interpreter() {
        m_stack = new Stack<Instr>();
        m_register = new Hashtable<Integer, Instr>();
        m_factory = Factory.getInstance(new PureFactory());
    }

    /**
	 * There can only be one interpreter active at a time,
	 * so we use the singleton pattern to retrieve
	 * an object of this class.
	 * 
	 * @return Object of Interpreter class.
	 */
    public static Interpreter getInstance() {
        if (m_interpreter == null) {
            m_interpreter = new Interpreter();
        }
        return m_interpreter;
    }

    /**
	 * Tries to execute a serie of stack statements
	 * given through an InputStream.
	 * 
	 * @param inputStream
	 */
    public void execute(InputStream inputStream) {
        try {
            InstrList instructionList = m_factory.InstrListFromFile(inputStream);
            m_instructionATermList = instructionList.getList();
            LabelInstr labelInstruction = null;
            Instr instruction = null;
            for (int i = 0; i < m_instructionATermList.getLength(); i++) {
                labelInstruction = (LabelInstr) m_instructionATermList.elementAt(i);
                instruction = labelInstruction.getInstr();
                if (instruction.isNat() || instruction.isStr()) {
                    m_stack.add(instruction);
                }
                if (instruction.isStop()) {
                    this.processStop(0);
                }
                if (instruction.isMinus()) {
                    this.processNegation();
                }
                if (instruction.isAdd()) {
                    this.processNatOperator(NatOperator.Add);
                }
                if (instruction.isMult()) {
                    this.processNatOperator(NatOperator.Multiply);
                }
                if (instruction.isDiv()) {
                    this.processNatOperator(NatOperator.Division);
                }
                if (instruction.isConcat()) {
                    this.processStringConcatenation();
                }
                if (instruction.isIfequal()) {
                    int location = this.processCompareOperator(CompareOperator.IfEqual);
                    if (location != -1) i = location;
                    continue;
                }
                if (instruction.isIflessequal()) {
                    int location = this.processCompareOperator(CompareOperator.IfLessOrEqual);
                    if (location != -1) i = location;
                    continue;
                }
                if (instruction.isIfless()) {
                    int location = this.processCompareOperator(CompareOperator.IfLess);
                    if (location != -1) i = location;
                    continue;
                }
                if (instruction.isGoto()) {
                    i = this.processGoto();
                    continue;
                }
                if (instruction.isPop()) {
                    if (m_stack.size() >= 1) {
                        m_stack.pop();
                    }
                }
                if (instruction.isCount()) {
                    m_stack.push(m_factory.makeInstr_Nat(m_stack.size()));
                }
                if (instruction.isLoad()) {
                    this.processLoad();
                }
                if (instruction.isSkip()) {
                }
                if (instruction.isStore()) {
                    this.processStore();
                }
            }
            this.processStop(1, "No more instructions left to execute, missing stop instruction!");
        } catch (Exception e) {
            this.processStop(1, e.getMessage());
        }
    }

    /**
	 * Stops the execution of the program,
	 * a status can be given indicating under what circumstances
	 * the program stopped executing (0 = normal, 1 = error).
	 * A message can be given to specify the nature of the programstop.
	 * 
	 * @param status
	 * @param message
	 */
    private void processStop(int status, String message) {
        if (status == 0) {
            System.out.println(message);
        } else {
            System.err.println(message);
        }
        System.out.println("Stop.");
        this.processStop(status);
    }

    /**
	 * Stops the execution of the program,
	 * a status can be given indicating under what circumstances
	 * the program stopped executing (0 = normal, 1 = error).
	 * 
	 * @param status
	 */
    private void processStop(int status) {
        System.out.println(m_stack.toString());
        System.exit(status);
    }

    /**
	 * Performs a mathematical operation on two natural constants (integers)
	 * operands.
	 * 
	 * @param operator
	 * The operator specifies what kind of mathematical operation should be performed.
	 * 
	 * @throws Exception
	 */
    private void processNatOperator(NatOperator operator) throws Exception {
        if (m_stack.size() >= 2) {
            if (m_stack.peek().isNat()) {
                int a = m_stack.pop().getNatCon();
                if (m_stack.peek().isNat()) {
                    int b = m_stack.pop().getNatCon();
                    int result = 0;
                    switch(operator) {
                        case Add:
                            result = b + a;
                            System.out.println("Add: " + b + " + " + a);
                            break;
                        case Division:
                            result = b / a;
                            System.out.println("Division: " + b + " / " + a);
                            break;
                        case Multiply:
                            result = b * a;
                            System.out.println("Multiply: " + b + " * " + a);
                            break;
                        default:
                            throw new Exception("Operator not supported.");
                    }
                    m_stack.push(m_factory.makeInstr_Nat(result));
                    return;
                }
            }
            throw new Exception("One or more arguments are not of type natural.");
        }
        throw new Exception("To few arguments given, 2 arguments are needed to perform mathematical operation on naturals");
    }

    /**
	 * Concatenates two string constants.
	 * 
	 * @throws Exception
	 */
    private void processStringConcatenation() throws Exception {
        if (m_stack.size() >= 2) {
            if (m_stack.peek().isStr()) {
                String a = m_stack.pop().getStrCon();
                if (m_stack.peek().isStr()) {
                    String b = m_stack.pop().getStrCon();
                    String result = b.concat(a);
                    m_stack.push(m_factory.makeInstr_Str(result));
                    System.out.println("Concat: " + b + " + " + a);
                    return;
                }
            }
            throw new Exception("One or more arguments are not of type string.");
        }
        throw new Exception("To few arguments given, 2 arguments are needed to perform concatenation.");
    }

    /**
	 * Negates a natural constant (integer).
	 * 
	 * @throws Exception
	 */
    private void processNegation() throws Exception {
        if (m_stack.size() >= 1) {
            if (m_stack.peek().isNat()) {
                int a = 0 - m_stack.pop().getNatCon();
                m_stack.push(m_factory.makeInstr_Nat(a));
                System.out.println("Negate: " + a);
                return;
            }
            throw new Exception("Argument given is not of type natural.");
        }
        throw new Exception("No argument (natural) was given to negate.");
    }

    /**
	 * Compares two constants, two strings constants of two natural constants (integers).
	 * 
	 * @param operator
	 * Operator specifies what kind of compare operation should be performed.
	 * 
	 * @throws Exception
	 */
    private int processCompareOperator(CompareOperator operator) throws Exception {
        if (m_stack.size() >= 3) {
            boolean result = false;
            if (m_stack.peek().isNat()) {
                int a = m_stack.pop().getNatCon();
                if (m_stack.peek().isNat()) {
                    int b = m_stack.pop().getNatCon();
                    switch(operator) {
                        case IfEqual:
                            result = b == a;
                            System.out.println("IfEqual: " + b + " == " + a);
                            break;
                        case IfLessOrEqual:
                            result = b <= a;
                            System.out.println("IfLessOrEqual: " + b + " <= " + a);
                            break;
                        case IfLess:
                            result = b < a;
                            System.out.println("IfLess: " + b + " < " + a);
                            break;
                        default:
                            throw new Exception("Operator not supported.");
                    }
                }
            } else if (m_stack.peek().isStr()) {
                String a = m_stack.pop().getStrCon();
                if (m_stack.peek().isStr()) {
                    String b = m_stack.pop().getStrCon();
                    switch(operator) {
                        case IfEqual:
                            result = b.equals(a);
                            System.out.println("IfEqual: " + b + " == " + a);
                            break;
                        case IfLessOrEqual:
                            result = b.length() <= a.length();
                            System.out.println("IfLessOrEqual: " + b + " <= " + a);
                            break;
                        case IfLess:
                            result = b.length() < a.length();
                            System.out.println("IfLess: " + b + " < " + a);
                            break;
                        default:
                            throw new Exception("Operator not supported.");
                    }
                }
            }
            if (result) {
                return this.processGoto();
            } else {
                m_stack.pop();
            }
            return -1;
        }
        throw new Exception("Compare operator needs 3 values.");
    }

    /**
	 * Lets the execution of stack statements jump to a specified stack statement location.
	 * 
	 * @return
	 * An index number (counting from 0) from where statement executing
	 * should continue.
	 * 
	 * @throws Exception
	 */
    private int processGoto() throws Exception {
        if (m_stack.size() >= 1) {
            if (m_stack.peek().isNat()) {
                int location = m_stack.pop().getNatCon() - 1;
                for (int i = 0; i < m_instructionATermList.getLength(); i++) {
                    int currentLocation = Integer.parseInt(m_instructionATermList.elementAt(i).getChildAt(0).toString());
                    if (location == currentLocation) {
                        System.out.println("Goto: " + (i + 1));
                        return i;
                    }
                }
                throw new Exception("Specified jump location does not exist.");
            } else {
                throw new Exception("Invalid jump location, natural expected.");
            }
        } else {
            throw new Exception("No location is given to jump to.");
        }
    }

    /**
	 * Loads a value from the register and pushes it on the execution stack.
	 * 
	 * @throws Exception
	 */
    private void processLoad() throws Exception {
        if (m_stack.size() >= 1) {
            if (m_stack.peek().isNat()) {
                int registerLocation = m_stack.pop().getNatCon();
                System.out.println("Load: " + registerLocation);
                m_stack.push(m_register.get(new Integer(registerLocation)));
                return;
            }
            throw new Exception("Invalid register location given, natural expected.");
        }
        throw new Exception("Expected register location (natural), none was given.");
    }

    /**
	 * Stores a value in the register at a specified location.
	 * 
	 * @throws Exception
	 */
    private void processStore() throws Exception {
        if (m_stack.size() >= 2) {
            if (m_stack.peek().isNat() || m_stack.peek().isStr()) {
                Instr value = m_stack.pop();
                if (m_stack.peek().isNat()) {
                    int registerLocation = m_stack.pop().getNatCon();
                    m_register.put(new Integer(registerLocation), value);
                    System.out.println("Store: " + registerLocation + " value: " + value);
                    return;
                }
                throw new Exception("Invalid register location, natural expected");
            }
            throw new Exception("Invalid value to store in register given, natural or string expected");
        }
        throw new Exception("To few arguments given, 2 arguments are needed to perform store, a value and register location.");
    }
}
