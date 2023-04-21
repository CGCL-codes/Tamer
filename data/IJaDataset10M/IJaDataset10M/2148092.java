package org.ujac.ui.editor.action;

import java.awt.event.ActionEvent;
import org.ujac.ui.editor.TextArea;

/**
 * Name: GotoLineKeyAction<br>
 * Description: A class handling the 'goto line' key action (CTRL+L).
 * <br>Log: $Log: GotoLineKeyAction.java,v $
 * <br>Log: Revision 1.1  2006/11/05 01:22:38  haustein
 * <br>Log: 0.20
 * <br>Log:
 * <br>Log: Revision 1.2  2004/07/03 08:43:33  lauerc
 * <br>Log: Removed unnecessary imports.
 * <br>Log:
 * <br>Log: Revision 1.1  2004/07/03 00:58:27  lauerc
 * <br>Log: Initial revision.
 * <br>Log:
 * @author $Author: haustein $
 * @version $Revision: 1.1 $
 */
public class GotoLineKeyAction extends KeyAction {

    /**
   * Constructs a GotoLineKeyAction instance with specific arguments.
   * @param textArea The text area.
   */
    public GotoLineKeyAction(TextArea textArea) {
        super(textArea);
    }

    /**
   * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
   */
    public void actionPerformed(ActionEvent e) {
        textArea.showGotoLineDialog();
    }
}
