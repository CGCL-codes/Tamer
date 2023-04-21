package org.limmen.crs.model;

import org.junit.Test;

public class SectionTest {

    @Test(expected = IllegalArgumentException.class)
    public void addDuplicateSetting() {
        Configuration configuration = new Configuration("test");
        configuration.addSetting("section1", new Setting("setting1"));
        configuration.addSetting("section1", new Setting("setting1"));
    }
}
