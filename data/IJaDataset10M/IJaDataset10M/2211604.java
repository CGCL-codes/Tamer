package org.seenoo.service.common;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.seenoo.framework.CommonAction;
import org.seenoo.framework.exception.DBException;
import org.seenoo.framework.exception.SeenooException;
import org.seenoo.framework.exception.SysException;

/**
 * 
 * @author Seenoo
 */
public abstract class PassportAction extends CommonAction {

    @Override
    protected void afterTreatment(String forwardName, ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws SysException, DBException, SeenooException {
    }

    @Override
    protected boolean serviceInitial(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws SysException, DBException, SeenooException {
        return true;
    }
}
