package org.jarp.gui.jhotdraw.tools.format;

import java.awt.Component;
import javax.swing.JOptionPane;
import org.jarp.core.PetriNet;
import org.jarp.core.PetriNetEditor;
import org.jarp.tools.AbstractTool;
import org.jarp.tools.Tool;
import org.jarp.tools.ToolFactory;
import CH.ifa.draw.framework.FigureAttributeConstant;
import CH.ifa.draw.standard.ChangeAttributeCommand;
import CH.ifa.draw.util.Command;
import CH.ifa.draw.util.UndoableCommand;

/**
 * Format tool. Does most of common format commands.
 * Colors, order, etc.
 * 
 * @version $Revision: 1.1 $
 * @author <a href="mailto:ricardo_padilha@users.sourceforge.net">Ricardo 
 * Sangoi Padilha</a>
 */
public class AttributeTool extends AbstractTool {

    private FigureAttributeConstant attribute;

    /**
	 * Constructor for <code>AttributeTool</code>.
	 * @param factory
	 * @param editor
	 */
    public AttributeTool(ToolFactory factory, PetriNetEditor editor, FigureAttributeConstant attr) {
        super(factory, editor);
        this.attribute = attr;
    }

    /**
	 * @see org.jarp.gui.jhotdraw.tools.AbstractJARPTool#execute()
	 */
    public void execute() throws Exception {
        String newName = JOptionPane.showInputDialog((Component) getEditor(), "TODO: fix this", "TODO: fix this too", JOptionPane.INFORMATION_MESSAGE);
        if (newName != null && !newName.equals("")) {
            Command command = new UndoableCommand(new ChangeAttributeCommand("ChangeAttributeTool", attribute, newName, getEditor()));
            command.execute();
        }
    }

    public static class Factory1 implements ToolFactory {

        public Tool createTool(PetriNetEditor editor, PetriNet net) {
            return new AttributeTool(this, editor, FigureAttributeConstant.getConstant("Name"));
        }
    }

    public static class Factory2 implements ToolFactory {

        public Tool createTool(PetriNetEditor editor, PetriNet net) {
            return new AttributeTool(this, editor, FigureAttributeConstant.getConstant("Tokens"));
        }
    }

    public static class Factory3 implements ToolFactory {

        public Tool createTool(PetriNetEditor editor, PetriNet net) {
            return new AttributeTool(this, editor, FigureAttributeConstant.getConstant("Weight"));
        }
    }
}
