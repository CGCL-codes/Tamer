package com.privilege.displayable.dwr;

import com.privilege.displayable.ComponentType;
import com.privilege.displayable.Image;
import com.privilege.model.DefaultImageModel;
import com.privilege.model.ImageModel;
import com.privilege.model.Model;

public class DWRImage extends AbstractDWRComponent implements Image {

    private ComponentType type = ComponentType.IMAGE;

    public DWRImage(ImageModel model) {
        super(model);
    }

    public DWRImage(String id, String imagePath) {
        super(new DefaultImageModel(id, null, imagePath));
    }

    public String getValue() {
        return "<img src=\"" + getPath() + "\" name=\"" + getId() + "\" id=\"" + getId() + "\" />";
    }

    @Override
    public ComponentType getComponentType() {
        return type;
    }

    @Override
    public void setComponentType(ComponentType type) {
        this.type = type;
    }

    public String getPath() {
        return ((ImageModel) getModel()).getPath();
    }

    public void setPath(String path) {
        ((ImageModel) getModel()).setPath(path);
    }
}
