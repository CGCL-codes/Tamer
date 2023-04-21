package org.ztemplates.test.actions.urlhandler.repository.nested;

import org.ztemplates.actions.ZAfter;
import org.ztemplates.actions.ZBefore;
import org.ztemplates.actions.ZMatch;

/**
 */
@ZMatch("nested/${value}")
public class NestedHandler {

    private String value;

    private int beforeCalled = 0;

    private int afterCalled = 0;

    private int afterValueCalled = 0;

    private int beforeValueCalled = 0;

    @ZBefore
    public void before() {
        beforeCalled++;
    }

    @ZAfter
    public void after() {
        afterCalled++;
    }

    public int getAfterCalled() {
        return afterCalled;
    }

    public int getBeforeCalled() {
        return beforeCalled;
    }

    @ZBefore("value")
    public void beforeValue() {
        beforeValueCalled++;
    }

    @ZAfter("value")
    public void afterValue() {
        afterValueCalled++;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public int getAfterValueCalled() {
        return afterValueCalled;
    }

    public int getBeforeValueCalled() {
        return beforeValueCalled;
    }
}
