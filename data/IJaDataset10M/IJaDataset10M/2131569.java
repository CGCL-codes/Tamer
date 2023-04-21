package org.zkoss.canvas;

import org.zkoss.zul.Image;

/**
 *
 * @author simonpai
 */
public class ImageSnapshot extends Snapshot {

    private Image _image;

    public ImageSnapshot(Image image, double dx, double dy) {
        super(dx, dy);
        _image = image;
    }

    public ImageSnapshot(Image image, double dx, double dy, double dw, double dh) {
        super(dx, dy, dw, dh);
        _image = image;
    }

    public ImageSnapshot(Image image, double dx, double dy, double dw, double dh, double sx, double sy, double sw, double sh) {
        super(dx, dy, dw, dh, sx, sy, sw, sh);
        _image = image;
    }

    public Image getImage() {
        return _image;
    }

    public ImageSnapshot setImage(Image image) {
        _image = image;
        return this;
    }

    @Override
    public String getSnapshotCntRef() {
        return _image.getUuid();
    }

    @Override
    public String getType() {
        return "img";
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return new ImageSnapshot(_image, _dx, _dy, _dw, _dh, _sx, _sy, _sw, _sh);
    }
}
