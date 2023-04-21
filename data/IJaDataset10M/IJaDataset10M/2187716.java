package in.raster.mayam.model;

/**
 *
 * @author  BabuHussain
 * @version 0.5
 *
 */
public class PresetModel {

    private int pk;

    private String presetName;

    private String windowWidth;

    private String windowLevel;

    private int modalityFk;

    public PresetModel() {
    }

    public int getPk() {
        return pk;
    }

    public void setPk(int pk) {
        this.pk = pk;
    }

    public String getPresetName() {
        return presetName;
    }

    public void setPresetName(String presetName) {
        this.presetName = presetName;
    }

    public String getWindowLevel() {
        return windowLevel;
    }

    public void setWindowLevel(String windowLevel) {
        this.windowLevel = windowLevel;
    }

    public String getWindowWidth() {
        return windowWidth;
    }

    public void setWindowWidth(String windowWidth) {
        this.windowWidth = windowWidth;
    }

    public int getModalityFk() {
        return modalityFk;
    }

    public void setModalityFk(int modalityFk) {
        this.modalityFk = modalityFk;
    }

    @Override
    public String toString() {
        return this.presetName;
    }
}
