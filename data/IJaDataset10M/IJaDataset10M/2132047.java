package org.usca.workshift.gwt.workshiftapp.client.usermanagement;

import com.google.gwt.user.client.rpc.ServiceDefTarget;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.core.client.GWT;
import org.usca.workshift.database.model.Member;
import org.usca.workshift.database.model.House;
import org.usca.workshift.gwt.workshiftapp.client.usermanagement.ManageUserAsync;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: danny
 * Date: Mar 25, 2008
 * Time: 8:32:27 PM
 * To change this template use File | Settings | File Templates.
 */
public interface ManageUser extends RemoteService {

    public Member saveMember(Member m, House h);

    public List<Member> getUsers();

    public List<House> getHouses();

    public Member modifyUser(Member m, String currentPassword);

    public Member modifyUser(Member m);

    public Boolean deleteUser(Member m);

    Boolean setMemberPassword(Member m, String currentPassword, String newPassword);

    /**
     * Utility/Convenience class.
     * Use ManageUser.App.getInstance() to access static instance of ManageUserAsync
     */
    public static class App {

        private static ManageUserAsync ourInstance = null;

        public static synchronized ManageUserAsync getInstance() {
            if (ourInstance == null) {
                ourInstance = (ManageUserAsync) GWT.create(ManageUser.class);
                ((ServiceDefTarget) ourInstance).setServiceEntryPoint(GWT.getModuleBaseURL() + "org.usca.workshift.gwt.workshiftapp.WorkshiftApp/ManageUser");
            }
            return ourInstance;
        }
    }
}
