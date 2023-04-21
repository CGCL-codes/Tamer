package com.plus.subtrack.admin;

import com.plus.subtrack.dataobjects.Genre;
import com.plus.subtrack.dataobjects.Size;
import com.plus.subtrack.dataobjects.MarketType;
import com.plus.subtrack.model.Model;
import com.plus.subtrack.session.SessionAction;
import com.plus.subtrack.session.SubTrackSession;
import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Collection;

/**
 * User: Mark Elam
 * Date: 12-Oct-2005
 * Time: 18:47:59
 */
public class MarketTypeSaveAction extends SessionAction {

    protected static Logger _logger = Logger.getLogger(MarketTypeSaveAction.class.getName());

    public ActionForward execute(ActionMapping actionMapping, ActionForm actionForm, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Model model, SubTrackSession subTrackSession) {
        MarketTypeActionForm marketTypeActionForm = (MarketTypeActionForm) actionForm;
        model.saveMarketType(marketTypeActionForm);
        return null;
    }
}
