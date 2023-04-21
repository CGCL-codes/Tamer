package com.nusino.dql.query;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Copy Right DynamicQL&copy; Nusino Technologies Inc.
 * If you are authorized to use this code. then you can modify code. However, the copy right marker does not allow to be removed. 
 * @author daping huang, dhuang05@gmail.com
 */
public class JPQLDefinition {

    private List<JPQLCause> causeList = new ArrayList<JPQLCause>();

    public void addCause(JPQLCause cause) {
        getCauseList().add(cause);
    }

    public List<JPQLCause> getCauseList() {
        return causeList;
    }

    public void setCauseList(List<JPQLCause> causeList) {
        this.causeList = causeList;
    }
}
