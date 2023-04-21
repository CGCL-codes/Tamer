package com.realtime.crossfire.jxclient.gui.commands;

import com.realtime.crossfire.jxclient.settings.Filenames;
import java.io.File;
import java.io.IOException;
import org.jetbrains.annotations.NotNull;

/**
 * Helper class for creating file names for screenshot files.
 * @author Andreas Kirschbaum
 */
public class ScreenshotFiles {

    /**
     * The number of auto-created screenshot filenames. If more than this number
     * of screenshots are created, old files will be recycled.
     */
    private static final int SCREENSHOT_FILENAMES = 10;

    /**
     * A number for creating screenshot file names. It is incremented for each
     * screenshot.
     */
    private int screenshotId = 0;

    /**
     * Returns a {@link File} for the next screenshot file.
     * @return the file
     * @throws IOException if the file cannot be determined
     */
    @NotNull
    public File getFile() throws IOException {
        final File file = Filenames.getSettingsFile("screenshot" + screenshotId + ".png");
        screenshotId = (screenshotId + 1) % SCREENSHOT_FILENAMES;
        return file;
    }
}
