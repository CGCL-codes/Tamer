package org.telscenter.sail.webapp.domain.grading;

import org.telscenter.pas.emf.pas.ECurnitmap;

/**
 * An abstract transfer object for aggregating necessary objects to allow
 * WISE teachers to grade student work.
 * 
 * This object encapsulates the following objects:
 * 1) <code>AnnotationBundle</code>
 * 2) <code>ESessionBundle</code>
 * 3) <code>ECurnitmap</code>
 * Only the AnnotationBundle is modifiable and persist-able. 
 * 
 * @author Hiroki Terashima
 * @version $Id: GradeWorkAggregate.java 1188 2007-09-18 19:45:02Z laurel $
 */
public interface GradeWorkAggregate {

    /**
	 * Returns the runId that this object is for
	 * @return runId for this object
	 */
    public Long getRunId();

    /**
 	 * TODO HT comment me
	 */
    public void setRunId(Long runId);

    /**
	 * TODO HT comment me
	 * @return
	 */
    public ECurnitmap getCurnitmap();

    /**
	 * TODO HT comment me
	 */
    public void setCurnitmap(ECurnitmap curnitmap);
}
