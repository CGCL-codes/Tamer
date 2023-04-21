package org.posterita.beans;

public class AvailableProductSizeBean extends UDIBean {

    private Boolean existsInSmall = Boolean.valueOf(false);

    private Boolean existsInMedium = Boolean.valueOf(false);

    private Boolean existsInLarge = Boolean.valueOf(false);

    private Boolean existsInXLarge = Boolean.valueOf(false);

    public Boolean getExistsInLarge() {
        return existsInLarge;
    }

    public void setExistsInLarge(Boolean existsInLarge) {
        this.existsInLarge = existsInLarge;
    }

    public Boolean getExistsInMedium() {
        return existsInMedium;
    }

    public void setExistsInMedium(Boolean existsInMedium) {
        this.existsInMedium = existsInMedium;
    }

    public Boolean getExistsInSmall() {
        return existsInSmall;
    }

    public void setExistsInSmall(Boolean existsInSmall) {
        this.existsInSmall = existsInSmall;
    }

    public Boolean getExistsInXLarge() {
        return existsInXLarge;
    }

    public void setExistsInXLarge(Boolean existsInXLarge) {
        this.existsInXLarge = existsInXLarge;
    }
}
