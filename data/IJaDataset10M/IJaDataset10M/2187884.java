package org.thechiselgroup.choosel.protovis.client;

import com.google.gwt.user.client.Element;

/**
 * 
 * @author Lars Grammel
 */
public abstract class PVAbstractPanel<T extends PVAbstractPanel<T>> extends PVAbstractBar<T> {

    protected PVAbstractPanel() {
    }

    public final native T canvas(Element element);

    public final native T canvas(String elementId);

    public final native String overflow();

    public final native T overflow(String overflow);

    public final native PVTransform transform();

    public final native T transform(PVTransform transform);
}
