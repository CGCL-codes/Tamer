package org.ztemplates.test.actions.urlhandler.callbacks.test2;

import org.ztemplates.actions.ZAfter;
import org.ztemplates.actions.ZBefore;
import org.ztemplates.actions.ZInit;
import org.ztemplates.actions.ZMatch;

/**
 */
@ZMatch("/base/#{nested}[/#{tree}]")
public class Handler {

    private NestedHandlerInterface nested;

    private TreeHandler tree;

    private int beforeCalled = 0;

    private int afterCalled = 0;

    private int beforeNestedCalled = 0;

    private int afterNestedCalled = 0;

    private int beforeTreeCalled = 0;

    private int afterTreeCalled = 0;

    private int initNestedCalled = 0;

    private boolean beforeTreeCalled_After_AfterNestedCalled;

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

    public void beforeNested() {
        beforeNestedCalled++;
    }

    @ZInit("nested")
    public void initNested(NestedHandlerInterface n) {
        initNestedCalled++;
    }

    @ZAfter("nested")
    public void afterNested() {
        afterNestedCalled++;
    }

    public NestedHandlerInterface getNested() {
        return nested;
    }

    public void setNested(NestedHandlerInterface nested) {
        this.nested = nested;
    }

    public TreeHandler getTree() {
        return tree;
    }

    public void setTree(TreeHandler tree) {
        this.tree = tree;
    }

    public int getAfterNestedCalled() {
        return afterNestedCalled;
    }

    public int getBeforeNestedCalled() {
        return beforeNestedCalled;
    }

    public void beforeTree() {
        beforeTreeCalled_After_AfterNestedCalled = (afterNestedCalled == 1);
        beforeTreeCalled++;
    }

    @ZAfter("tree")
    public void afterTree() {
        afterTreeCalled++;
    }

    public int getAfterTreeCalled() {
        return afterTreeCalled;
    }

    public int getBeforeTreeCalled() {
        return beforeTreeCalled;
    }

    public boolean getBeforeTreeCalled_After_AfterNestedCalled() {
        return beforeTreeCalled_After_AfterNestedCalled;
    }

    public int getInitNestedCalled() {
        return initNestedCalled;
    }

    public void setInitNestedCalled(int initNestedCalled) {
        this.initNestedCalled = initNestedCalled;
    }
}
