package com.xenoage.zong.commands;

import static com.xenoage.util.error.Err.err;
import static com.xenoage.zong.viewer.App.app;
import java.util.List;
import com.xenoage.util.error.ErrorLevel;
import com.xenoage.util.logging.Log;
import com.xenoage.util.logging.LogLevel;
import com.xenoage.zong.documents.ScoreDoc;
import com.xenoage.zong.documents.VScoreDoc;
import com.xenoage.zong.util.exceptions.CancelledException;
import com.xenoage.zong.util.exceptions.PropertyAlreadySetException;
import com.xenoage.zong.util.exceptions.UselessException;
import com.xenoage.zong.viewer.Voc;

/**
 * This class executes or undoes commands and
 * saves a history of them, for exactly one document.
 *
 * @author Andreas Wenger
 */
public class CommandPerformer {

    private VScoreDoc document;

    private DocumentHistory history;

    /**
   * Creates a new {@link CommandPerformer} for the given {@link VScoreDoc}.
   */
    public CommandPerformer(VScoreDoc document) {
        this.document = document;
        this.history = new DocumentHistory(document);
    }

    /**
   * Performs the given Command.
   * If possible, the command is added to
   * the history, and the following commands
   * in the history are deleted.
   */
    public void execute(Command command) {
        Log.log(LogLevel.Command, command.getClass().getName() + " is executed...");
        try {
            command.execute(this);
            if (command.isInHistory()) {
                history.removeFollowingCommands();
                if (command instanceof UndoableCommand) {
                    history.addCommand((UndoableCommand) command);
                    history.forward();
                }
                document.repaint();
                notifyListeners();
            }
        } catch (CancelledException ex) {
            Log.log(LogLevel.Command, "Cancelled.");
        } catch (UselessException ex) {
            Log.log(LogLevel.Command, "No effect.");
        } catch (PropertyAlreadySetException ex) {
            Log.log(LogLevel.Command, "Property was already set.");
        }
    }

    /**
   * Performs the Command given by its ID.
   * If possible, the command is added to the history.
   */
    public void execute(String commandID) {
        Log.log(LogLevel.Message, this, "Trying to instantiate command \"" + commandID + "\"...");
        try {
            Class<?> cmdClass = Class.forName("com.xenoage.zong.commands." + commandID);
            Command cmd = (Command) cmdClass.newInstance();
            Log.log(LogLevel.Message, this, "OK. Command created.");
            execute(cmd);
            notifyListeners();
        } catch (Exception ex) {
            err().report(ErrorLevel.Warning, Voc.Error_CommandFailed, ex);
        }
    }

    /**
   * Undoes the last command in the history, if possible.
   * The current tool is deactivated, but a tool corresponding to the updated
   * selection may be (re)activated.
   */
    public void undo() {
        if (isUndoPossible()) {
            UndoableCommand cmd = history.getLastCommand();
            Log.log(LogLevel.Command, cmd.getClass().getName() + " is undone...");
            cmd.undo(this);
            document.deOrReactivateTool();
            history.back();
            document.repaint();
            notifyListeners();
        }
    }

    /**
   * Redoes the next command in the history, if possible.
   */
    public void redo() {
        if (isRedoPossible()) {
            UndoableCommand cmd = history.getLastUndoneCommand();
            cmd.redo(this);
            history.forward();
            document.repaint();
            notifyListeners();
        }
    }

    /**
   * Update the menu items for undo and redo.
   * OBSOLETE /
  public void updateMenuItems()
  {
  	Command lastCommand = history.getLastCommand();
  	Command lastUndoneCommand = history.getLastUndoneCommand();
  	//undo menu item
		if (mnuUndo != null)
		{
			Command cmd = lastCommand;
			if (cmd != null && cmd.isUndoable())
			{
				mnuUndo.setText(Lang.get(Voc.Menu_Edit_Undo) + ": " + cmd.getName());
				mnuUndo.setEnabled(true);
			}
			else
			{
				mnuUndo.setText(Lang.get(Voc.Menu_Edit_Undo) + ": "
					+ Lang.get(com.xenoage.zong.core.Voc.General_Impossible));
				mnuUndo.setEnabled(false);
			}
		}
		//redo menu item
		if (mnuRedo != null)
		{
			Command cmd = lastUndoneCommand;
			if (cmd != null)
			{
				mnuRedo.setText(Lang.get(Voc.Menu_Edit_Redo) + ": " + cmd.getName());
				mnuRedo.setEnabled(true);
			}
			else
			{
				mnuRedo.setText(Lang.get(Voc.Menu_Edit_Redo) + ": "
					+ Lang.get(com.xenoage.zong.core.Voc.General_Impossible));
				mnuRedo.setEnabled(false);
			}
		}
  } */
    private void notifyListeners() {
        for (CommandListener l : app().getCommandListeners()) l.commandPerformed(this);
    }

    /**
   * Returns the {@link ScoreDoc} the commands are performed on.
   */
    public VScoreDoc getVScoreDoc() {
        return document;
    }

    /**
   * Returns true, if at least one command can be undone.
   */
    public boolean isUndoPossible() {
        return (history.getLastCommand() != null);
    }

    /**
   * Returns true, if at least one command can be redone.
   */
    public boolean isRedoPossible() {
        return (history.getLastUndoneCommand() != null);
    }

    /**
   * Gets a list of the undoable commands. The first one is the command,
   * that must be undone first.
   */
    public List<UndoableCommand> getUndoableCommands() {
        return history.getUndoableCommands();
    }

    /**
   * Undoes the given number of commands in the history, if possible.
   */
    public void undoMultipleSteps(int steps) {
        Log.log(LogLevel.Command, "Multiple undo (" + steps + " steps)...");
        for (int i = 0; i < steps && isUndoPossible(); i++) {
            UndoableCommand cmd = history.getLastCommand();
            Log.log(LogLevel.Command, cmd.getClass().getName() + " is undone...");
            cmd.undo(this);
            history.back();
        }
        document.repaint();
        notifyListeners();
    }
}
