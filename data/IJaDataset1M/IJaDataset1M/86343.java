package com.openbravo.pos.pda.struts.actions;

import com.openbravo.pos.pda.bo.RestaurantManager;
import com.openbravo.pos.ticket.Floor;
import com.openbravo.pos.ticket.Place;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.DynaActionForm;

/**
 *
 * @author jaroslawwozniak
 */
public class FloorAction extends org.apache.struts.action.Action {

    private static final String SUCCESS = "success";

    private static final String FAILURE = "failure";

    /**
     * This is the action called from the Struts framework.
     * @param mapping The ActionMapping used to select this instance.
     * @param form The optional ActionForm bean for this request.
     * @param request The HTTP Request we are processing.
     * @param response The HTTP Response we are processing.
     * @throws java.lang.Exception
     * @return
     */
    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        DynaActionForm inputFormPlace = (DynaActionForm) form;
        RestaurantManager manager = new RestaurantManager();
        ArrayList<Floor> floors = new ArrayList<Floor>();
        floors = (ArrayList<Floor>) manager.findAllFloors();
        List busyTables = new ArrayList<Floor>();
        String floorId = (String) inputFormPlace.get("floorId");
        List places = new ArrayList<Place>();
        if (floorId.equals("") || floorId.equals("undefined")) {
            places = manager.findAllPlaces(floors.get(0).getId());
            floorId = floors.get(0).getId();
            busyTables = manager.findAllBusyTable(floorId);
        } else {
            places = manager.findAllPlaces(floorId);
            busyTables = manager.findAllBusyTable(floorId);
        }
        request.setAttribute("i", 0);
        request.setAttribute("busy", busyTables);
        request.getSession().setAttribute("places", places);
        request.setAttribute("floorId", floorId);
        request.setAttribute("floors", floors);
        return mapping.findForward(SUCCESS);
    }
}
