package edu.ucsd.cse135.gas.action.applicant;

import java.util.ArrayList;
import java.util.HashMap;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import edu.ucsd.cse135.gas.logic.support.Location;
import edu.ucsd.cse135.gas.logic.support.University;
import edu.ucsd.cse135.gas.resources.Constants;

public class ProvideDegreeAction extends Action {

    public ProvideDegreeAction() {
        super();
    }

    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        University university = new University();
        ArrayList<Location> locationsList = new ArrayList<Location>();
        locationsList = university.getLocations();
        ArrayList<HashMap<String, String>> locationsCol1 = new ArrayList<HashMap<String, String>>();
        ArrayList<HashMap<String, String>> locationsCol2 = new ArrayList<HashMap<String, String>>();
        ArrayList<HashMap<String, String>> locationsCol3 = new ArrayList<HashMap<String, String>>();
        int col = locationsList.size() / 3;
        for (int i = 0; i < col; i++) {
            HashMap<String, String> location = new HashMap<String, String>();
            location.put("name", locationsList.get(i).getName());
            location.put("id", "" + locationsList.get(i).getId());
            locationsCol1.add(location);
        }
        for (int i = col; i < (col + col); i++) {
            HashMap<String, String> location = new HashMap<String, String>();
            location.put("name", locationsList.get(i).getName());
            location.put("id", "" + locationsList.get(i).getId());
            locationsCol2.add(location);
        }
        for (int i = (col + col); i < locationsList.size(); i++) {
            HashMap<String, String> location = new HashMap<String, String>();
            location.put("name", locationsList.get(i).getName());
            location.put("id", "" + locationsList.get(i).getId());
            locationsCol3.add(location);
        }
        request.setAttribute("locationsCol1", locationsCol1);
        request.setAttribute("locationsCol2", locationsCol2);
        request.setAttribute("locationsCol3", locationsCol3);
        return mapping.findForward(Constants.SUCCESS);
    }
}
