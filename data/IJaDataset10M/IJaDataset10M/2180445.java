package org.renjin.primitives.io.serialization;

import org.junit.Test;
import org.renjin.primitives.io.serialization.Version;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

public class VersionTest {

    @Test
    public void pack() {
        Version version = new Version(2, 10, 1);
        assertThat(version.asPacked(), equalTo(133633));
    }
}
