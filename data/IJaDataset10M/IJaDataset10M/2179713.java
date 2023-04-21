package edu.upmc.opi.caBIG.caTIES.client.vr.admin;

import java.util.List;
import ca.odell.glazedlists.TextFilterator;
import edu.upmc.opi.caBIG.caTIES.middletier.CaTIES_DistributionProtocolImpl;
import edu.upmc.opi.caBIG.caTIES.middletier.CaTIES_UserImpl;

public class UserTextFilterator implements TextFilterator<CaTIES_UserImpl> {

    @Override
    public void getFilterStrings(List<String> inList, CaTIES_UserImpl user) {
        inList.add(user.obj.getFirstName());
        inList.add(user.obj.getLastName());
        inList.add(user.obj.getUsername());
    }
}
