package ch.skyguide.tools.requirement.hmi.action;

import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ImageIcon;
import javax.swing.KeyStroke;
import ch.skyguide.tools.requirement.hmi.RequirementTool;
import ch.skyguide.tools.requirement.hmi.model.BeanManagerAndTableModelFactory;
import ch.skyguide.tools.requirement.hmi.search.NodeTextSeeker;

@SuppressWarnings("serial")
public class SearchPreviousAction extends AbstractAction {

    private final NodeTextSeeker seeker;

    public SearchPreviousAction(final NodeTextSeeker nodeSeeker) {
        super(BeanManagerAndTableModelFactory.getInstance().getTranslatedText("menu.SEARCH_PREVIOUS"));
        putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_F3, InputEvent.CTRL_MASK));
        putValue(SHORT_DESCRIPTION, BeanManagerAndTableModelFactory.getInstance().getTranslatedText("hint.SEARCH_PREVIOUS"));
        putValue(Action.SMALL_ICON, new ImageIcon(RequirementTool.class.getResource("icons/FindPrevious.gif")));
        seeker = nodeSeeker;
        seeker.setPreviousAction(this);
    }

    public void actionPerformed(ActionEvent _e) {
        if (seeker.isEnabled()) {
            seeker.searchPreviousNode();
        }
    }
}
