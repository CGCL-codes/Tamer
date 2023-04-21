package ch.intertec.storybook.action;

import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import ch.intertec.storybook.toolkit.I18N;
import ch.intertec.storybook.toolkit.net.NetTools;

@SuppressWarnings("serial")
public class OpenBrowserSbPageAction extends AbstractAction {

    public static final String ACTION_KEY_PAGE = "page";

    public OpenBrowserSbPageAction() {
        super(I18N.getMsg("msg.pro.version.button"));
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String page = (String) getValue(ACTION_KEY_PAGE);
        NetTools.openBrowserSBPage(page);
    }
}
