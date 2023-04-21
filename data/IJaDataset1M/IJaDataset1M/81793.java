package org.rubypeople.rdt.core.tests.core;

import junit.framework.TestCase;
import org.eclipse.core.runtime.Path;
import org.rubypeople.eclipse.shams.resources.ShamProject;
import org.rubypeople.rdt.internal.core.LoadPathEntry;

public class TC_LoadPathEntry extends TestCase {

    public TC_LoadPathEntry(String name) {
        super(name);
    }

    public void testToXml() {
        ShamProject project = new ShamProject(new Path("myLocation"), "MyProject");
        LoadPathEntry entry = new LoadPathEntry(project);
        String expected = "<pathentry type=\"project\" path=\"myLocation\"/>";
        assertEquals(expected, entry.toXML());
    }
}
