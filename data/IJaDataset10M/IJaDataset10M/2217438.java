package edu.harvard.fas.rbrady.tpteam.tpmanager.http.user.update;

import edu.harvard.fas.rbrady.tpteam.tpmanager.http.UserServlet;
import edu.harvard.fas.rbrady.tpteam.tpmanager.http.admin.update.UpdateTest3;

public class UpdateTestByUser3 extends UpdateTest3 implements UserServlet {

    private static final long serialVersionUID = 1L;

    protected String mFormAction = "<input type=\"hidden\" name=\"formAction\" value=\"updateTestEntity\">\n";
}
