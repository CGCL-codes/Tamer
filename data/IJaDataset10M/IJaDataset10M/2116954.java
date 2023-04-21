package net.sf.karatasi.desktop;

import java.util.logging.Logger;
import net.sf.karatasi.databasecheck.ProgressInformationHandlerInterface;
import org.jetbrains.annotations.NotNull;

public class GuiCheckHandler implements ProgressInformationHandlerInterface {

    /** The logger. */
    private static final Logger LOG = Logger.getLogger("net.sf.japi.net.rest");

    /** The target step counter. */
    private int targetSteps = 0;

    /** The completed steps counter. */
    private int completedSteps = 0;

    /** The name of active the section. */
    @SuppressWarnings("unused")
    private String sectionName = "";

    /** The name of the database. */
    private String databaseName = "";

    /** The constructor. */
    public GuiCheckHandler(@NotNull final String databaseName) {
        this.databaseName = databaseName;
    }

    /** @see net.sf.karatasi.databasecheck.ProgressInformationHandlerInterface#incrementStepCount(int) */
    public void incrementStepCount(final int increment) {
        targetSteps += increment;
    }

    /** @see net.sf.karatasi.databasecheck.ProgressInformationHandlerInterface#notifyError(java.lang.String, java.lang.String) */
    public void notifyError(final String elementDescriptor, final String errorDescriptor) {
        LOG.info("Checking " + databaseName + " [" + elementDescriptor + "] failed: " + errorDescriptor);
    }

    /** @see net.sf.karatasi.databasecheck.ProgressInformationHandlerInterface#notifyFinished() */
    public void notifyFinished() {
        LOG.info("Check of " + databaseName + " finished with success (" + completedSteps + "/" + targetSteps + ")");
    }

    /** @see net.sf.karatasi.databasecheck.ProgressInformationHandlerInterface#setActivityDescription(java.lang.String) */
    public void setActivityDescription(final String description) {
    }

    /** @see net.sf.karatasi.databasecheck.ProgressInformationHandlerInterface#setStepCount(int) */
    public void setStepCount(final int count) {
        targetSteps = count;
    }

    /** @see net.sf.karatasi.databasecheck.ProgressInformationHandlerInterface#startSection(java.lang.String) */
    public void startSection(final String sectionName) {
        this.sectionName = sectionName;
    }

    /** @see net.sf.karatasi.databasecheck.ProgressInformationHandlerInterface#stepForwardByOne() */
    public void stepForwardByOne() {
        completedSteps++;
    }
}
