package org.argeproje.resim.proc.input;

import java.awt.image.renderable.ParameterBlock;
import javax.media.jai.JAI;
import javax.media.jai.PlanarImage;
import org.argeproje.resim.proc.data.Data;
import org.argeproje.resim.proc.data.ImageDA;

public class SubImagePR extends InputPR {

    protected float _upLeftX;

    protected float _upLeftY;

    protected float _width;

    protected float _height;

    public void setParameters(int upLeftX, int upLeftY, int width, int height) {
        setParamUpLeftX(upLeftX);
        setParamUpLeftY(upLeftY);
        setParamWidth(width);
        setParamHeight(height);
    }

    public void setParamUpLeftX(float upLeftX) {
        _upLeftX = upLeftX;
    }

    public float getParamUpLeftX() {
        return _upLeftX;
    }

    public void setParamUpLeftY(float upLeftY) {
        _upLeftY = upLeftY;
    }

    public float getParamUpLeftY() {
        return _upLeftY;
    }

    public void setParamWidth(float width) {
        _width = width;
    }

    public float getParamWidth() {
        return _width;
    }

    public void setParamHeight(int height) {
        _height = height;
    }

    public float getParamHeight() {
        return _height;
    }

    public Data process() {
        PlanarImage im = (PlanarImage) getInput().getData();
        ParameterBlock pb = new ParameterBlock();
        pb.addSource(im);
        pb.add(getParamUpLeftX());
        pb.add(getParamUpLeftY());
        pb.add(getParamWidth());
        pb.add(getParamHeight());
        PlanarImage out = JAI.create("crop", pb);
        ParameterBlock pb2 = new ParameterBlock();
        pb2.addSource(out);
        pb2.add(-getParamUpLeftX());
        pb2.add(-getParamUpLeftY());
        out = JAI.create("translate", pb2);
        setOutput(new ImageDA(out));
        return getOutput();
    }
}
