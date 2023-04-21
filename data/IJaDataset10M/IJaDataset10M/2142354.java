package org.argeproje.resim.proc.data;

import javax.media.jai.TiledImage;

public class ImageSegmentDA extends Data {

    private int _width = 1;

    private int _height = 1;

    private int _posX = 0;

    private int _posY = 0;

    private int _band = 0;

    private ImageDA _refImage;

    private float[][] _segmentData;

    public int getPosX() {
        return _posX;
    }

    public int getPosY() {
        return _posY;
    }

    public float[][] getSegmentData() {
        return _segmentData;
    }

    public void writePixel(int xPos, int yPos) {
        TiledImage im = (TiledImage) _refImage.getData();
        im.setSample(_posX + xPos, _posY + yPos, _band, _segmentData[xPos][yPos]);
    }

    public ImageSegmentDA(ImageDA refImage, int band, int width, int height, int posX, int posY) {
        _refImage = refImage;
        _segmentData = _refImage.getSegmentByFirstPixelPos(band, width, height, posX, posY);
        _band = band;
        _width = width;
        _height = height;
        _posX = posX;
        _posY = posY;
        setDataType(Data.IMAGE_SEGMENT);
        setData(_segmentData);
    }

    protected ImageSegmentDA(ImageSegmentDA segment, int offsetX, int offsetY) {
        _refImage = segment._refImage;
        _band = segment._band;
        _width = segment._width;
        _height = segment._height;
        _posX = segment._posX + offsetX * _width;
        _posY = segment._posY + offsetY * _height;
        _segmentData = _refImage.getSegmentByFirstPixelPos(_band, _width, _height, _posX, _posY);
        setDataType(Data.IMAGE_SEGMENT);
        setData(_segmentData);
    }

    public void write() {
        _refImage.setSegmentByFirstPixelPos(_band, _posX, _posY, _segmentData);
    }

    public ImageSegmentDA getNextSegment() {
        ImageSegmentDA result = null;
        result = getRightSegment();
        if (result == null) {
            int posX = 0;
            int posY = this._posY + _height;
            if (posY < _refImage.getHeight() - _height) {
                result = new ImageSegmentDA(this._refImage, this._band, this._width, this._height, posX, posY);
            }
        }
        return result;
    }

    public ImageSegmentDA get1PixelNextSegment() {
        ImageSegmentDA result = null;
        int posX = _posX + 1;
        int posY = _posY;
        if (posX > this._refImage.getWidth() - _width) {
            posX = 0;
            posY = posY + 1;
        }
        if (posY <= _refImage.getHeight() - _height) {
            result = new ImageSegmentDA(this._refImage, this._band, this._width, this._height, posX, posY);
        }
        return result;
    }

    public ImageSegmentDA getRightSegment() {
        if (!isValidSegment(1, 0)) return null;
        return new ImageSegmentDA(this, 1, 0);
    }

    public ImageSegmentDA getLeftSegment() {
        if (!isValidSegment(-1, 0)) return null;
        return new ImageSegmentDA(this, -1, 0);
    }

    public ImageSegmentDA getUpSegment() {
        if (!isValidSegment(0, -1)) return null;
        return new ImageSegmentDA(this, 0, -1);
    }

    public ImageSegmentDA getDownSegment() {
        if (!isValidSegment(0, 1)) return null;
        return new ImageSegmentDA(this, 0, 1);
    }

    public ImageSegmentDA getUpRightSegment() {
        if (!isValidSegment(1, -1)) return null;
        return new ImageSegmentDA(this, 1, -1);
    }

    public ImageSegmentDA getUpLeftSegment() {
        if (!isValidSegment(-1, -1)) return null;
        return new ImageSegmentDA(this, -1, -1);
    }

    public ImageSegmentDA getDownRightSegment() {
        if (!isValidSegment(1, 1)) return null;
        return new ImageSegmentDA(this, 1, 1);
    }

    public ImageSegmentDA getDownLeftSegment() {
        if (!isValidSegment(-1, 1)) return null;
        return new ImageSegmentDA(this, -1, 1);
    }

    private boolean isValidSegment(int offsetX, int offsetY) {
        int posX = _posX + offsetX * _width;
        int posY = _posY + offsetY * _height;
        boolean result = true;
        if (posX > _refImage.getWidth() - _width) {
            result = false;
        }
        if (posY > _refImage.getHeight() - _height) {
            result = false;
        }
        return result;
    }
}
