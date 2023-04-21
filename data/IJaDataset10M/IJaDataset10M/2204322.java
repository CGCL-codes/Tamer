package com.novocode.naf.model;

/**
 * Default implementation of the ICLabelModel interface.
 * 
 * @author Stefan Zeiger (szeiger@novocode.com)
 * @since Jan 5, 2004
 */
public class DefaultCLabelModel extends DefaultModel implements ICLabelModel {

    public static final PType<ICLabelModel> PTYPE = new PType<ICLabelModel>(ICLabelModel.class, DefaultCLabelModel.class);

    private volatile String text, imageName;

    public DefaultCLabelModel() {
    }

    public DefaultCLabelModel(String text) {
        this.text = text;
    }

    public DefaultCLabelModel(String text, String imageName) {
        this.text = text;
        this.imageName = imageName;
    }

    public String getText() {
        return text;
    }

    public String getImageID() {
        return imageName;
    }

    public synchronized void setText(String text) {
        if (this.text != text) {
            this.text = text;
            notifyListeners();
        }
    }

    public synchronized void setImageID(String imageName) {
        if (this.imageName != imageName) {
            this.imageName = imageName;
            notifyListeners();
        }
    }

    public synchronized void setTextAndImageID(String text, String imageName) {
        if (this.text != text || this.imageName != imageName) {
            this.text = text;
            this.imageName = imageName;
            notifyListeners();
        }
    }
}
