package org.impalaframework.config;

import java.io.File;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.util.FileCopyUtils;
import junit.framework.TestCase;

public class LocationModificationStateHolderTest extends TestCase {

    private LocationModificationStateHolder holder;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        holder = new LocationModificationStateHolder();
    }

    public void testNoResource() {
        holder.setLocations(new Resource[0]);
        assertFalse(holder.isModifiedSinceLastCheck());
        assertFalse(holder.isModifiedSinceLastCheck());
    }

    public void testIsModified() throws Exception {
        FileSystemResource resource = new FileSystemResource("../impala-core/files/reload/tomodify.txt");
        holder.setLocation(resource);
        assertFalse(holder.isModifiedSinceLastCheck());
        assertFalse(holder.isModifiedSinceLastCheck());
        File file = resource.getFile();
        Thread.sleep(20);
        FileCopyUtils.copy(("some text").getBytes(), file);
        assertTrue(holder.isModifiedSinceLastCheck());
        assertFalse(holder.isModifiedSinceLastCheck());
    }

    public void testNotOnFirstCheck() throws Exception {
        FileSystemResource resource = new FileSystemResource("../impala-core/files/reload/tomodify.txt");
        holder.setLocation(resource);
        holder.setReturnOnFirstCheck(true);
        assertTrue(holder.isModifiedSinceLastCheck());
        assertFalse(holder.isModifiedSinceLastCheck());
    }
}
