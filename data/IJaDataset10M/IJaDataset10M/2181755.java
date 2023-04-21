package net.sf.stump.form;

import org.apache.wicket.markup.ComponentTag;

/**
 * @author Joni Freeman
 */
public class PropertyModelStrategy implements IModelStrategy {

    public CharSequence model(ComponentTag tag) {
        return ", new PropertyModel(model, \"" + tag.getId() + "\")";
    }
}
