package jp.hpl.data;

import javax.swing.JFrame;
import jp.hpl.map.MapCanvas;
import src.backend.MapData;
import src.gui.FlagsFrame;
import src.gui.Model;

public class DefaultMarathonDataModel extends Model {

    private IEditorOneFrame frame;

    private static final String MAP_FILE_READING_ERROR_MESSAGE = "Problem reading map file";

    private DefaultMarathonDataModel() {
        super();
    }

    public DefaultMarathonDataModel(IEditorOneFrame frame) {
        super(null);
        this.frame = frame;
    }

    public int getSelectedLevelIndex() {
        try {
            return this.frame.getLevelIndex();
        } catch (Exception e) {
            return 0;
        }
    }

    public void setLevelSelectorNames() {
        try {
            super.setLevelSelectorNames();
        } catch (Exception e) {
            return;
        }
    }

    public void removeChildFrame(JFrame arg0) {
        super.removeChildFrame(arg0);
    }

    public void initLoad() {
        try {
            this.frame.initLoad();
        } catch (Exception e) {
            return;
        }
    }

    public void turnProgOff() {
        this.frame.turnProgOff();
    }
}
