package de.jmda.mproc;

import static org.junit.Assert.assertNotNull;
import org.junit.Test;
import de.jmda.MarkerAnnotationType;

@MarkerAnnotationType
public class JUTTypeElementFactory {

    @Test
    public void testTypeElementFactory() {
        assertNotNull(TypeElementFactory.get("jmda.core", getClass()));
    }
}
