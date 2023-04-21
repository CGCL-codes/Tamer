package net.sf.groofy.match.ui.listeners;

import net.sf.groofy.GroofyApp;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.widgets.Text;

public class SearchFieldModifyListener implements ModifyListener {

    @Override
    public void modifyText(ModifyEvent e) {
        String oldString = GroofyApp.getInstance().getMatchWindow().getSearchString();
        String newString = ((Text) e.getSource()).getText();
        if (!newString.equals(oldString)) GroofyApp.getInstance().getMatchWindow().setSearchString(newString);
    }
}
