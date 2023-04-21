package gui.macros;

import gui.EuclideGui;
import java.util.ArrayList;
import dynamic.DynamicObject2D;

/**
 * A basic implementation of a macro, containing a set of instructions, that
 * can construct only one new dynamic object.
 * @author dlegland
 *
 */
public class SimpleMacro {

    /**
	 * name of the macro, that will appear in menus
	 */
    String name;

    /**
	 * Expected classes of inputs
	 */
    ArrayList<Class<?>> classes;

    /**
	 * The set of instructions used to create the different
	 */
    ArrayList<SimpleMacroInstruction> instructions;

    ArrayList<String> userInstructions = new ArrayList<String>();

    public SimpleMacro(String name, ArrayList<Class<?>> classes, ArrayList<SimpleMacroInstruction> instructions) {
        this.name = name;
        this.classes = classes;
        this.instructions = instructions;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUserInstruction(int step) {
        if (step < userInstructions.size()) return userInstructions.get(step);
        return new String("Choose input shape " + (step + 1));
    }

    public int getInstructionNumber() {
        return instructions.size();
    }

    public int getInputSize() {
        return classes.size();
    }

    public Class<?> getInputClass(int step) {
        return classes.get(step);
    }

    public void run(EuclideGui gui, ArrayList<DynamicObject2D> inputs) {
        ArrayList<DynamicObject2D> parents = new ArrayList<DynamicObject2D>(inputs.size() + instructions.size());
        for (int i = 0; i < inputs.size(); i++) parents.add(inputs.get(i));
        for (SimpleMacroInstruction instr : instructions) instr.execute(gui, parents);
        DynamicObject2D dynamic = parents.get(parents.size() - 1);
        gui.addNewObject(gui.getCurrentDoc(), dynamic);
    }
}
