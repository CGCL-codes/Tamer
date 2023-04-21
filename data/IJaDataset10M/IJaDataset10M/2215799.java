package ch.skyguide.tools.requirement.hmi.action;

import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.net.URL;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ImageIcon;
import javax.swing.KeyStroke;
import ch.skyguide.tools.requirement.hmi.RequirementTool;
import ch.skyguide.tools.requirement.hmi.model.BeanManagerAndTableModelFactory;
import ch.skyguide.tools.requirement.hmi.search.NodeTextSeeker;

@SuppressWarnings("serial")
public class SearchAction extends AbstractAction {

    private final NodeTextSeeker seeker;

    public SearchAction(final NodeTextSeeker nodeSeeker) {
        super(BeanManagerAndTableModelFactory.getInstance().getTranslatedText("menu.SEARCH"));
        putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_F, InputEvent.CTRL_MASK));
        putValue(SHORT_DESCRIPTION, BeanManagerAndTableModelFactory.getInstance().getTranslatedText("hint.SEARCH"));
        URL find16url = RequirementTool.class.getClassLoader().getResource("toolbarButtonGraphics/general/Find16.gif");
        putValue(Action.SMALL_ICON, new ImageIcon(find16url));
        seeker = nodeSeeker;
    }

    public void actionPerformed(ActionEvent _e) {
        seeker.retrieveSearchString();
        if (seeker.isEnabled()) {
            seeker.searchFirstNode();
        }
    }
}
