package com.android.sdklib.internal.repository;

import com.android.sdklib.internal.repository.Archive.Arch;
import com.android.sdklib.internal.repository.Archive.Os;

/**
 * A mock {@link ToolPackage} for testing.
 *
 * By design, this package contains one and only one archive.
 */
public class MockToolPackage extends ToolPackage {

    /**
     * Creates a {@link MockToolPackage} with the given revision and hardcoded defaults
     * for everything else.
     * <p/>
     * By design, this creates a package with one and only one archive.
     */
    public MockToolPackage(int revision) {
        super(null, null, revision, null, "desc", "url", Os.getCurrentOs(), Arch.getCurrentArch(), "foo");
    }
}
