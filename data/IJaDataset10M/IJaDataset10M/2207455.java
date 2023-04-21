package periman;

public class ScreeningProperties {

    private String imgPath;

    private boolean showThreat;

    public ScreeningProperties(String imagePath, boolean threat) {
        imgPath = imagePath;
        showThreat = threat;
    }

    public String getImgPath() {
        return imgPath;
    }

    public boolean isShowThreat() {
        return showThreat;
    }

    public void setShowThreat(boolean b) {
        showThreat = b;
    }

    public boolean isThreatRealyInImage() {
        if (imgPath.contains(ConfStateData.IMAGES_W_O_THREAT_DIRECTORY)) return false;
        return true;
    }
}
