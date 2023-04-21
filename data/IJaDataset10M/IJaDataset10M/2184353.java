package net.sf.dozer.util.mapping.vo.inheritance;

import net.sf.dozer.util.mapping.vo.BaseTestObject;

public class SuperSpecificObject extends BaseTestObject {

    private String superAttr1;

    public String getSuperAttr1() {
        return superAttr1;
    }

    public void setSuperAttr1(String superAttr1) {
        this.superAttr1 = superAttr1;
    }
}
