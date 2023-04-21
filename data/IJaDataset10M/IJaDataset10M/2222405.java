package net.entropysoft.dashboard.plugin.dashboard.gef.command;

import net.entropysoft.dashboard.plugin.dashboard.model.DashboardGuide;
import net.entropysoft.dashboard.plugin.dashboard.model.DashboardRuler;
import org.eclipse.gef.commands.Command;

/**
 * Command that adds a new guide to a ruler
 * 
 */
public class CreateGuideCommand extends Command {

    private DashboardGuide guide;

    private DashboardRuler parent;

    private int position;

    /**
	 * 
	 * @param parent
	 *            the vertical or horizontal ruler
	 * @param position
	 *            The pixel position where the new guide is to be created
	 */
    public CreateGuideCommand(DashboardRuler parent, int position) {
        super("Create Guide");
        this.parent = parent;
        this.position = position;
    }

    public boolean canUndo() {
        return true;
    }

    public void execute() {
        if (guide == null) guide = new DashboardGuide(!parent.isHorizontal());
        guide.setPosition(position);
        parent.addGuide(guide);
    }

    public void undo() {
        parent.removeGuide(guide);
    }
}
