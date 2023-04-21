package com.rapidminer.gui.actions;

import java.awt.event.ActionEvent;
import javax.swing.Icon;
import com.rapidminer.Process;
import com.rapidminer.gui.MainFrame;
import com.rapidminer.gui.tools.ResourceAction;
import com.rapidminer.gui.tools.SwingTools;

/**
 * Start the corresponding action.
 * 
 * @author Ingo Mierswa
 */
public class RunAction extends ResourceAction {

    private static final long serialVersionUID = 1;

    private static final Icon ICON_PLAY_SMALL = SwingTools.createIcon("16/media_play.png");

    private static final Icon ICON_PLAY_LARGE = SwingTools.createIcon("24/media_play.png");

    private static final Icon ICON_RESUME_SMALL = SwingTools.createIcon("16/media_play_green.png");

    private static final Icon ICON_RESUME_LARGE = SwingTools.createIcon("24/media_play_green.png");

    private final MainFrame mainFrame;

    public RunAction(MainFrame mainFrame) {
        super("run");
        this.mainFrame = mainFrame;
        setCondition(PROCESS_RUNNING, DISALLOWED);
        setCondition(EDIT_IN_PROGRESS, DISALLOWED);
    }

    public void actionPerformed(ActionEvent e) {
        mainFrame.runProcess();
    }

    public void setState(int processState) {
        switch(processState) {
            case Process.PROCESS_STATE_PAUSED:
                putValue(LARGE_ICON_KEY, ICON_RESUME_LARGE);
                putValue(SMALL_ICON, ICON_RESUME_SMALL);
                break;
            default:
                putValue(LARGE_ICON_KEY, ICON_PLAY_LARGE);
                putValue(SMALL_ICON, ICON_PLAY_SMALL);
        }
    }
}
