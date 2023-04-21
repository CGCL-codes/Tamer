package org.soqqo.vannitator.vmodel;

public class VNameSimpleClass extends VName {

    private static final long serialVersionUID = 1L;

    public VNameSimpleClass(String name) {
        super(name);
    }

    @Override
    public String getAsNew() {
        if (getConfigBean() != null) {
            return getNewSimpleClassName();
        } else {
            return getName();
        }
    }
}
