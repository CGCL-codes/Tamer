package org.jjazz.ui.editor.cl_editor;

import org.openide.util.Lookup;
import org.openide.util.lookup.AbstractLookup;
import org.openide.util.lookup.InstanceContent;

public class BugTest {

    private InstanceContent lookupContent;

    /** Our local lookup. */
    private Lookup lookup;

    public BugTest() {
        lookupContent = new InstanceContent();
        lookup = new AbstractLookup(lookupContent);
    }

    public void selectItem() {
    }
}
