package org.milligan.eccles;

/**
 * A tag that supports writing to the report file
 * @author Ian Tomey
 *
 */
public abstract class ReportingTag extends Tag {

    public ReportingTag() {
    }

    /**
	 * Indicate this tag supports writing to the report
	 * @return
	 */
    public boolean isReportingTag() {
        return true;
    }
}
