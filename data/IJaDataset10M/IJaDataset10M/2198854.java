package viewer.config.edit.tileconfig.action;

import java.awt.event.ActionEvent;
import viewer.config.TileConfigUrlDisk;
import viewer.config.edit.tileconfig.TileConfigEditorList;

/**
 * @author Sebastian Kuerten (sebastian.kuerten@fu-berlin.de)
 * 
 */
public class AddTileConfigAction extends TileConfigAction {

    private static final long serialVersionUID = 8063537304808331253L;

    /**
	 * Create a new action
	 * 
	 * @param editorList
	 *            the editor list this action is about.
	 */
    public AddTileConfigAction(TileConfigEditorList editorList) {
        super(editorList);
        setName("Add");
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        TileConfigUrlDisk config = new TileConfigUrlDisk(-1, "foo", "url", "path");
        editorList.add(config);
        editorList.revalidate();
    }
}
