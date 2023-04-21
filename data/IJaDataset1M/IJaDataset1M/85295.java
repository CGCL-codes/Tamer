package org.ztemplates.test.actions.urlhandler.match.collision2;

import org.ztemplates.actions.ZMatch;

/**
 */
@ZMatch("/basic[/#{nested}]")
public class Handler {

    private NestedHandler nested;

    public NestedHandler getNested() {
        return nested;
    }

    public void setNested(NestedHandler nested) {
        this.nested = nested;
    }
}
