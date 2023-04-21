package testifie.ui.commands.input;

import java.util.Vector;
import testifie.ui.commands.ICommand;
import testifie.ui.common.beans.Dialog;

/**
 * @author slips
 */
public interface IInputCommand extends ICommand {

    void setEnterValue(String value);

    String getEnterValue();

    void setDialog(Dialog handler);

    Dialog getDialog();

    Vector getDialogs();

    void fireEvent(String event);
}
