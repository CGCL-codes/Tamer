package de.bea.services.vidya.client.datasource.types;

public class WSHotspotElement {

    protected java.lang.String hotspotText;

    protected long imageUid;

    protected java.lang.String imageUrl;

    protected java.lang.String type;

    protected int x1;

    protected int x2;

    protected int y1;

    protected int y2;

    public WSHotspotElement() {
    }

    public WSHotspotElement(java.lang.String hotspotText, long imageUid, java.lang.String imageUrl, java.lang.String type, int x1, int x2, int y1, int y2) {
        this.hotspotText = hotspotText;
        this.imageUid = imageUid;
        this.imageUrl = imageUrl;
        this.type = type;
        this.x1 = x1;
        this.x2 = x2;
        this.y1 = y1;
        this.y2 = y2;
    }

    public java.lang.String getHotspotText() {
        return hotspotText;
    }

    public void setHotspotText(java.lang.String hotspotText) {
        this.hotspotText = hotspotText;
    }

    public long getImageUid() {
        return imageUid;
    }

    public void setImageUid(long imageUid) {
        this.imageUid = imageUid;
    }

    public java.lang.String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(java.lang.String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public java.lang.String getType() {
        return type;
    }

    public void setType(java.lang.String type) {
        this.type = type;
    }

    public int getX1() {
        return x1;
    }

    public void setX1(int x1) {
        this.x1 = x1;
    }

    public int getX2() {
        return x2;
    }

    public void setX2(int x2) {
        this.x2 = x2;
    }

    public int getY1() {
        return y1;
    }

    public void setY1(int y1) {
        this.y1 = y1;
    }

    public int getY2() {
        return y2;
    }

    public void setY2(int y2) {
        this.y2 = y2;
    }
}
