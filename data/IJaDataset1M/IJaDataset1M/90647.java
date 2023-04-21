package herkulez.interpreter;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.swing.JOptionPane;

/**
 * This class represents an interpreter for the programming language brainfuck.
 * brainfuck was designed by the Swiss Urban M�ller in 1993. It is the trial to have a
 * programming language, which is as easy as possible but turing-complete.
 * 
 * I added some more functionality, so that this is not a real brainfuck interpreter.
 * Both, real brainfuck code and my modified code, can be executed with this interpreter.
 * 
 * brainfuck has only the eight following commands:
 *  '-' decrement
 *  '+' increment (add 1)
 *  '>' increment pointer address
 *  '<' decrement pointer address
 *  '[' begin loop
 *  ']' end loop 
 *  ',' input
 *  '.' output
 *
 * What I added is:
 *  'V' add 5
 *  'X' add 10
 *  'L' add 50
 *  'C' add 100
 *  'M' add 1000
 *  '#' duplicate
 *  '0' set to 0
 *  'U' use value as character set multiplier (128*x)
 *
 * 
 * @author johannes
 */
public class ExtendedBFInterpreter implements IInterpreter {

    /**
	 * Code which should be executed.
	 */
    private StringBuffer code = new StringBuffer("");

    /**
	 * Representation of the memory of runtime environment.
	 */
    private List<Integer> memory = new ArrayList<Integer>();

    /**
	 * Actual pointer position in memory.
	 */
    private int aktPos = 0;

    /**
	 * Stack implementation.
	 */
    private String stack = "";

    /**
	 * Max value in memory.
	 */
    private int maxCode = 128;

    /**
	 * OutputStream where result is written.
	 */
    private OutputStream console;

    /**
	 * Should the extended possibilities be used?
	 */
    private boolean useExtendedBrainfuck = false;

    /**
	 * ACSII charset.
	 */
    public static final int ASCII = 1;

    /**
	 * LATIN_1 charset.
	 */
    public static final int LATIN_1 = 2;

    /**
	 * Index to stop at in debug mode.
	 */
    private int stopIndex = -1;

    /**
	 * Contructor.
	 */
    public ExtendedBFInterpreter() {
        maxCode = 128;
    }

    /**
	 * Apply another charset.
	 * @param charset : See constant values.
	 */
    public void setCharSet(int charset) {
        maxCode = 128 * charset;
    }

    /**
	 * sets the environment variables for the interpreter
	 * @param code
	 * @param console
	 * @param settings
	 */
    public void init(String inputcode, OutputStream console, Map<String, Object> settings) {
        memory.add(new Integer(0));
        code = new StringBuffer(inputcode);
        this.console = console;
        if (((Boolean) settings.get("interpreter.debug.on")).booleanValue()) {
            this.stopIndex = ((Integer) settings.get("ide.caret.pos")).intValue();
        }
        this.useExtendedBrainfuck = ((Boolean) settings.get("interpreter.run.extended")).booleanValue();
    }

    /**
	 * pointer is set to next address in memory
	 */
    public void incrementPointer() {
        aktPos++;
        if (aktPos >= memory.size()) {
            memory.add(aktPos, new Integer(0));
        }
    }

    /**
	 * interpreter goes back to last bracket
	 * @param i
	 * @return
	 */
    public int goBackToBracket(int i) {
        Integer z = new Integer(this.getValue());
        if (z != 0) {
            i = Integer.parseInt(stack.substring(stack.lastIndexOf(";") + 1)) - 1;
        }
        stack = stack.substring(0, stack.lastIndexOf(";"));
        return i;
    }

    /**
	 * increments value in memory at current pointer position
	 */
    public void increment(int count) {
        this.setValue((this.getValue() + count) % maxCode);
    }

    /**
	 * sets value in memory at current pointer position to zero
	 */
    public void zero() {
        this.setValue(0);
    }

    /**
	 * duplicates value in memory at current pointer position
	 */
    public void doublicate() {
        this.setValue((this.getValue() * 2) % maxCode);
    }

    /**
	 * pointer is set to previous address in memory
	 */
    public void decrementPonter() {
        if (aktPos > 0) aktPos--;
    }

    /**
	 * decrements value in memory at current pointer position
	 */
    public void decrement() {
        this.setValue((int) ((128 + this.getValue() - 1) % maxCode));
    }

    /**
	 * asks the user for an input
	 */
    public void input() {
        String in = (String) JOptionPane.showInputDialog("Zeichen eingeben:", "");
        if (in == null || in.trim().length() == 0) {
            this.zero();
        } else {
            this.setValue((int) in.charAt(0));
        }
    }

    /**
	 * writes the value at the current address in memory 
	 * to the console
	 * @param console
	 */
    public void output(OutputStream console) {
        try {
            console.write(this.getValue());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
	 * gets the value at the current pointer address from memory
	 * @return
	 */
    public int getValue() {
        return memory.get(aktPos).shortValue();
    }

    /**
	 * Return console output.
	 * @return
	 */
    public OutputStream getConsole() {
        return console;
    }

    /**
	 * sets the value at the current pointer address in memory
	 * @param z
	 */
    public void setValue(int z) {
        memory.set(aktPos, new Integer(z));
    }

    public void run() {
        boolean running = true;
        int i = 0;
        if (code.length() == 0) {
            running = false;
        }
        while (running) {
            char c = code.charAt(i);
            if (useExtendedBrainfuck) {
                switch(c) {
                    case 'U':
                        this.setCharSet(this.getValue());
                        break;
                    case '0':
                        this.zero();
                        break;
                    case '#':
                        this.doublicate();
                        break;
                    case 'V':
                        this.increment(5);
                        break;
                    case 'X':
                        this.increment(10);
                        break;
                    case 'L':
                        this.increment(50);
                        break;
                    case 'C':
                        this.increment(100);
                        break;
                    case 'M':
                        this.increment(1000);
                        break;
                }
            }
            switch(c) {
                case '>':
                    this.incrementPointer();
                    break;
                case '<':
                    this.decrementPonter();
                    break;
                case '-':
                    this.decrement();
                    break;
                case '+':
                    this.increment(1);
                    break;
                case '.':
                    this.output(console);
                    break;
                case '[':
                    stack += ";" + i;
                    break;
                case ']':
                    i = goBackToBracket(i);
                    break;
                case ',':
                    this.input();
                    break;
            }
            if (i < code.length() - 1 && i + 1 != stopIndex) {
                i++;
            } else {
                running = false;
            }
        }
    }
}
