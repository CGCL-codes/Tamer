package org.starobjects.wicket.app;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import org.junit.Test;
import org.starobjects.wicket.viewer.components.ComponentType;

public class ViewConstantsTest {

    @Test
    public void testToString() {
        assertThat(ComponentType.ENTITY_COLLECTION.toString(), is("entityCollection"));
    }
}
