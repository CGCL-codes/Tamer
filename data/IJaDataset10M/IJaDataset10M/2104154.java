package com.googlecode.bdoc.mojo;

import static org.junit.Assert.*;
import org.junit.Test;

public class TestMojoVersion {

    @Test
    public void shouldReadMojoVersionFromGeneratedMojoFile() {
        assertNotNull(MojoVersion.versionNumber());
    }
}
