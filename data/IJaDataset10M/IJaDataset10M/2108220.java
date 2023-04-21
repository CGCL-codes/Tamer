package org.radrails.server.core;

import java.io.File;
import java.io.FilenameFilter;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.update.core.BaseInstallHandler;

/**
 * Install handler for RadRails. Clears out Ruby scripts and metadata from
 * previous installations.
 * 
 * @author mkent
 * 
 */
public class ServerCoreInstallHandler extends BaseInstallHandler {

    public void installCompleted(boolean success) throws CoreException {
        if (success) {
            deleteRubyFiles(ServerPlugin.getInstance().getStateLocation().toFile());
        }
    }

    /**
	 * Deletes all files ending in .rb from the given directory.
	 * 
	 * @param dir
	 *            the directory to search
	 */
    private void deleteRubyFiles(File dir) {
        File[] contents = dir.listFiles(new FilenameFilter() {

            public boolean accept(File dir, String name) {
                return name.endsWith(".rb");
            }
        });
        for (int i = 0; i < contents.length; i++) {
            contents[i].delete();
        }
    }
}
