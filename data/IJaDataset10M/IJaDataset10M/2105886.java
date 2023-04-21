package com.ilog.translator.java2cs.popup.actions;

public class AddOverrideModifierAction extends AbstractModifyModifierAction {

    /**
	 * Constructor for Action1.
	 */
    public AddOverrideModifierAction() {
        super();
    }

    @Override
    public String getTagValue() {
        return "override";
    }

    @Override
    public String getAction() {
        return "+";
    }
}
