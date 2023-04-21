package net.sf.xclh;

import java.util.List;
import org.jdom.Content;

public class JDomTransformationOrder {

    List<Content> content = null;

    TransformAction action = TransformAction.CLONE;

    String locationInformation = null;

    public JDomTransformationOrder(String locationInformation) {
        super();
        this.locationInformation = locationInformation;
    }

    public JDomTransformationOrder(List<Content> content, String locationInformation) {
        super();
        this.content = content;
        this.action = TransformAction.REPLACE;
        this.locationInformation = locationInformation;
    }

    public JDomTransformationOrder(TransformAction action, String locationInformation) {
        super();
        this.action = action;
        this.locationInformation = locationInformation;
    }

    public JDomTransformationOrder() {
        super();
    }

    public JDomTransformationOrder(List<Content> content) {
        super();
        this.content = content;
        this.action = TransformAction.REPLACE;
    }

    public JDomTransformationOrder(TransformAction action) {
        super();
        this.action = action;
    }

    public List<Content> getContent() {
        return content;
    }

    public TransformAction getAction() {
        return action;
    }

    public String getLocationInformation() {
        return locationInformation;
    }

    public void setLocationInformation(String locationInformation) {
        this.locationInformation = locationInformation;
    }
}
