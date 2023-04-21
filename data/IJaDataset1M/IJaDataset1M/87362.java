package com.dcivision.framework.taglib.channel;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import com.dcivision.framework.ApplicationException;
import com.dcivision.workflow.taglib.WorkflowStatusFlagTag;

public class AjaxStatusFlagTag extends WorkflowStatusFlagTag {

    private Log log = LogFactory.getLog(this.getClass());

    public String getAjaxContext() throws ApplicationException {
        try {
            this.init();
            return this.getContent();
        } catch (Exception ex) {
            log.error(ex.getMessage(), ex);
            throw new ApplicationException(ex);
        }
    }
}
