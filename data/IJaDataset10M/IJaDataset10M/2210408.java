package org.dicom4jserver.beans;

import org.dicom4j.toolkit.beans.StudyBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * a Study
 * @author <a href="mailto:straahd@users.sourceforge.net">Laurent Lecomte 
 *
 */
public class Study extends StudyBean {

    /**
	 * the logger
	 */
    private static Logger fLogger = LoggerFactory.getLogger(Study.class);

    public Study() {
        super();
    }
}
