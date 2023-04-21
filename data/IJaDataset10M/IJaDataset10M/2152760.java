package widget;

import dscript.connect.*;
import java.awt.*;

public class DTextArea extends Dustyable {

    private TextArea ta;

    public DTextArea() {
        ta = new TextArea("", 5, 5);
    }

    public boolean isComponent() {
        return true;
    }

    public Component getAsComponent() {
        return ta;
    }

    public boolean processCommand(String command) {
        if (command.equalsIgnoreCase("sendtext")) {
            getJavaConnector().sendActionMessage(ta.getText());
            return true;
        }
        if (command.equalsIgnoreCase("clear")) {
            ta.setText("");
            return true;
        }
        if (command.equalsIgnoreCase("is_editable")) {
            ta.setEditable(true);
            return true;
        }
        if (command.equalsIgnoreCase("not_editable")) {
            ta.setEditable(false);
            return true;
        }
        if (command.equalsIgnoreCase("both_scrollbars")) {
            ta = new TextArea(ta.getText(), ta.getRows(), ta.getColumns(), 0);
            return true;
        }
        if (command.equalsIgnoreCase("no_scrollbars")) {
            ta = new TextArea(ta.getText(), ta.getRows(), ta.getColumns(), 3);
            return true;
        }
        if (command.equalsIgnoreCase("vertical_scrollbars_only")) {
            ta = new TextArea(ta.getText(), ta.getRows(), ta.getColumns(), 1);
            return true;
        }
        if (command.equalsIgnoreCase("horizontal_scrollbars_only")) {
            ta = new TextArea(ta.getText(), ta.getRows(), ta.getColumns(), 2);
            return true;
        }
        return false;
    }

    public boolean processCommand(String command, String[] args) {
        if (args.length < 1) {
            return false;
        }
        if (command.equalsIgnoreCase("settext")) {
            ta.setText(args[0]);
            return true;
        }
        if (command.equalsIgnoreCase("append")) {
            ta.append(args[0]);
            return true;
        }
        if (command.equalsIgnoreCase("setpointer")) {
            try {
                int i = Integer.parseInt(args[0]);
                if (i > (ta.getText().length() - 1)) {
                    throw new Exception();
                }
                ta.setCaretPosition(i);
                return true;
            } catch (Exception e) {
            }
            return false;
        }
        if (args.length < 2) {
            return false;
        }
        if (command.equalsIgnoreCase("insert")) {
            try {
                int i = Integer.parseInt(args[1]);
                ta.insert(args[0], i);
                return true;
            } catch (Exception e) {
                return false;
            }
        }
        if (command.equalsIgnoreCase("setsize")) {
            try {
                int i = Integer.parseInt(args[0]);
                int j = Integer.parseInt(args[1]);
                ta = new TextArea(ta.getText(), i, j, ta.getScrollbarVisibility());
                return true;
            } catch (Exception e) {
                return false;
            }
        }
        return false;
    }
}
