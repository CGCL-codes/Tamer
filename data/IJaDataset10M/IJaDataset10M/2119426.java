package ch.isbiel.oois.infospectator.action;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import ch.isbiel.oois.infocore.dto.RaceMetaDO;
import ch.isbiel.oois.infospectator.Constants;
import ch.isbiel.oois.infospectator.service.InfoManagerService;
import ch.isbiel.oois.infospectator.service.InfoManagerServiceException;

/** * This Action gets the details of a race * * @author $Author$ * @version $Revision$ $Date$ */
public class GetRaceDetailsAction extends OOISBaseAction {

    /**   * Process the specified HTTP request, and create the corresponding HTTP   * response (or forward to another web component that will create it).   * Return an <code>ActionForward</code> instance describing where and how   * control should be forwarded, or <code>null</code> if the response has   * already been completed.   *   * @param mapping The ActionMapping used to select this instance   * @param actionForm The optional ActionForm bean for this request (if any)   * @param request The HTTP request we are processing   * @param response The HTTP response we are creating   *   * @exception IOException if an input/output error occurs   * @exception ServletException if a servlet exception occurs   */
    public ActionForward perform(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        ActionErrors errors = new ActionErrors();
        InfoManagerService im = getInfoManagerService();
        if (im == null) {
            errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("error.infomanager.missing"));
            saveErrors(request, errors);
            return (new ActionForward(mapping.getInput()));
        }
        RaceDetailForm rdform = (RaceDetailForm) form;
        Integer race_id = rdform.getRaceId();
        RaceMetaDO race = null;
        try {
            race = im.getRaceMetaInfo(race_id);
        } catch (InfoManagerServiceException iex) {
            errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("error.infomanager.serviceexception"));
            saveErrors(request, errors);
            return (new ActionForward(mapping.getInput()));
        }
        int cat_count = race.getCategoriesAndLegs().size();
        List cats = new ArrayList(cat_count);
        Iterator it = race.getCategoriesAndLegs().keySet().iterator();
        while (it.hasNext()) {
            String cat = (String) it.next();
            cats.add(cat);
        }
        Collections.sort(cats);
        List cat_leg_list = new ArrayList(cats.size());
        for (int i = 0, n = cats.size(); i < n; i++) {
            Integer leg_count = (Integer) race.getCategoriesAndLegs().get(cats.get(i));
            List leg_list = new ArrayList(leg_count.intValue());
            leg_list.add(cats.get(i));
            for (int j = 1; j <= leg_count.intValue(); j++) {
                leg_list.add(new Integer(j));
            }
            cat_leg_list.add(leg_list);
        }
        if (servlet.getDebug() >= 1) {
            servlet.log("GetRaceDetailsAction: cat_leg_list=" + cat_leg_list);
        }
        request.setAttribute(Constants.RACE, race);
        if (race.getType() == RaceMetaDO.RELAY) {
            request.setAttribute(Constants.IS_RELAY, new Boolean(true));
        }
        request.setAttribute(Constants.CATEGORY_LEG_LIST, cat_leg_list);
        if (servlet.getDebug() >= 1) {
            servlet.log("GetRaceDetailsAction: Got race. id=" + race.getId() + " state=" + race.getState() + " type=" + race.getType());
        }
        return (mapping.findForward(Constants.SUCCESS_KEY));
    }
}
