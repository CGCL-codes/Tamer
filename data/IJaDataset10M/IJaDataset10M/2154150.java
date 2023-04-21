package cn.ac.ntarl.umt.actions.profile;

import java.util.ArrayList;
import cn.ac.ntarl.umt.CLBException;
import cn.ac.ntarl.umt.database.DAOFactory;
import cn.ac.ntarl.umt.database.ExecuteHelper;
import cn.ac.ntarl.umt.profile.ProfileValueDAO;
import cn.ac.ntarl.umt.profile.data.Attribute;
import cn.ac.ntarl.umt.profile.data.UserProfile;

public class DBUpdateAttribute extends ExecuteHelper {

    public DBUpdateAttribute(int userid, ArrayList<Attribute> attrs) {
        profile = new UserProfile(userid);
        profile.setAttributes(attrs);
    }

    /**
	 * 
	 */
    private static final long serialVersionUID = 1L;

    @Override
    public void checkPermission() throws CLBException {
    }

    @Override
    public void perform() throws CLBException {
        ProfileValueDAO pd = DAOFactory.getInstance().getProfileValueDAO();
        pd.update(profile);
    }

    private UserProfile profile;
}
