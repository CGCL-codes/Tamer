package com.google.code.jqwicket.ui.droppable;

import com.google.code.jqwicket.ui.JQUIComponentBehavior;

/**
 * @author mkalina
 *
 */
public class DroppableBehavior extends JQUIComponentBehavior<DroppableOptions> implements IDroppable {

    private static final long serialVersionUID = 1L;

    public DroppableBehavior() {
        this(new DroppableOptions());
    }

    public DroppableBehavior(DroppableOptions options) {
        super(options);
    }

    /**
	 * {@inheritDoc}
	 * 
	 * @see com.google.code.jqwicket.IJQUIWidget#getName()
	 */
    public CharSequence getName() {
        return JQ_COMPONENT_NAME;
    }
}
