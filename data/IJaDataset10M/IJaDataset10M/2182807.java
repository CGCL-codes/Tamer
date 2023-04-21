package org.dozer.vo.deep;

import java.util.List;
import org.dozer.vo.BaseTestObject;

/**
 * @author garsombke.franz
 * @author sullins.ben
 * @author tierney.matt
 */
public class SrcDeepObj extends BaseTestObject {

    private SrcNestedDeepObj srcNestedObj;

    private String sameNameField;

    private List someList;

    public SrcNestedDeepObj getSrcNestedObj() {
        return srcNestedObj;
    }

    public void setSrcNestedObj(SrcNestedDeepObj srcNestedObj) {
        this.srcNestedObj = srcNestedObj;
    }

    public String getSameNameField() {
        return sameNameField;
    }

    public void setSameNameField(String sameNameField) {
        this.sameNameField = sameNameField;
    }

    public List getSomeList() {
        return someList;
    }

    public void setSomeList(List someList) {
        this.someList = someList;
    }
}
