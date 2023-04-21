package org.simbrain.console;

import java.awt.BorderLayout;
import java.awt.Dimension;
import org.simbrain.workspace.Workspace;
import org.simbrain.workspace.gui.GenericFrame;
import org.simbrain.workspace.gui.GuiComponent;
import bsh.Interpreter;
import bsh.util.JConsole;

/**
 * Component corresponding to a beanshell window.
 */
public class ConsoleDesktopComponent extends GuiComponent<ConsoleComponent> {

    private static final long serialVersionUID = 1L;

    /**
     * Constructor.
     */
    public ConsoleDesktopComponent(GenericFrame frame, ConsoleComponent component) {
        super(frame, component);
        setPreferredSize(new Dimension(500, 400));
    }

    @Override
    public void postAddInit() {
        setLayout(new BorderLayout());
        JConsole console = new JConsole();
        Interpreter interprerter = getSimbrainInterpreter(console, super.getWorkspaceComponent().getWorkspace());
        add("Center", console);
        new Thread(interprerter).start();
    }

    /**
     * Returns a Simbrain interpreter.
     *
     * @param console console for interpreter
     * @param workspace workspace references
     * @return simbrain interpreter
     */
    public static Interpreter getSimbrainInterpreter(final JConsole console, final Workspace workspace) {
        Interpreter interpreter = new Interpreter(console);
        interpreter.getNameSpace().importPackage("org.simbrain.network.neurons");
        interpreter.getNameSpace().importPackage("org.simbrain.network.connections");
        interpreter.getNameSpace().importPackage("org.simbrain.network.layouts");
        interpreter.getNameSpace().importPackage("org.simbrain.network.networks");
        interpreter.getNameSpace().importPackage("org.simbrain.network.interfaces");
        interpreter.getNameSpace().importPackage("org.simbrain.network.groups");
        interpreter.getNameSpace().importPackage("org.simbrain.network.synapses");
        interpreter.getNameSpace().importPackage("org.simbrain.workspace");
        interpreter.getNameSpace().importCommands(".");
        interpreter.getNameSpace().importCommands("org.simbrain.console.commands");
        interpreter.getOut();
        interpreter.getErr();
        try {
            String FS = System.getProperty("file.separator");
            interpreter.set("workspace", workspace);
            interpreter.set("bsh.prompt", ">");
            interpreter.eval("addClassPath(\"scripts/console\");");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return interpreter;
    }

    @Override
    public void closing() {
    }

    @Override
    protected void update() {
    }
}
