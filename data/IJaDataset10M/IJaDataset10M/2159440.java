package org.dozer.vo.deep;

import org.dozer.vo.BaseTestObject;
import org.dozer.vo.SimpleObj;

/**
 * @author garsombke.franz
 * @author sullins.ben
 * @author tierney.matt
 */
public class SrcNestedDeepObj2 extends BaseTestObject {

    private String src5;

    private SimpleObj[] simpleObjects;

    public String getSrc5() {
        return src5;
    }

    public void setSrc5(String src5) {
        this.src5 = src5;
    }

    public SimpleObj[] getSimpleObjects() {
        return simpleObjects;
    }

    public void setSimpleObjects(SimpleObj[] simpleObjects) {
        this.simpleObjects = simpleObjects;
    }
}
