package chsec.gui.pers_ed;

import java.util.ArrayList;
import chsec.domain.Role;
import chsec.domain.User;
import chsec.service.PersonDataService;
import chsec.service.UserPerson;
import com.agical.rmock.extension.junit.RMockTestCase;

public class UserSrchWinCtrlImpTest extends RMockTestCase {

    private UserEditWinCtrl mockEditCtrl;

    private UserSrchWin mockGui;

    private PersonDataService mockDataSvc;

    private UserSrchWinCtrlImp controller;

    public void setUp() {
        controller = new UserSrchWinCtrlImp();
        mockEditCtrl = (UserEditWinCtrl) mock(UserEditWinCtrl.class, "editCtrl");
        controller.setEditCtrl(mockEditCtrl);
        mockGui = (UserSrchWin) mock(UserSrchWin.class, "gui");
        controller.setGui(mockGui);
        mockDataSvc = (PersonDataService) mock(PersonDataService.class, "dataSvc");
        controller.setSvc(mockDataSvc);
    }

    public void testClear() {
        mockGui.clearCrit();
        startVerification();
        controller.clearCrit();
    }

    public void testAddUser() {
        User mockUser = new User();
        mockEditCtrl.setUp(null, null);
        mockEditCtrl.getResult();
        modify().returnValue(mockUser);
        mockGui.addToResult(null);
        modify().args(is.ANYTHING);
        startVerification();
        controller.addUser();
    }

    public void testAddUserCancelled() {
        mockEditCtrl.setUp(null, null);
        mockEditCtrl.getResult();
        startVerification();
        controller.addUser();
    }

    public void testDoSearch() {
        MockPersonDataSvc mpds = new MockPersonDataSvc();
        ArrayList<UserPerson> upL = new ArrayList<UserPerson>();
        UserPerson up = new UserPerson();
        upL.add(up);
        controller.setSvc(mpds);
        mpds.setUpUserPersonResult(upL);
        mockGui.getUnameCrit();
        mockGui.getUroleCrit();
        mockGui.setSearchResult(upL);
        startVerification();
        controller.doSearch();
    }

    public void testDoSearchNoResult() {
        String unameCrit = "uname";
        Role roleCrit = new Role();
        roleCrit.setId(1L);
        mockGui.getUnameCrit();
        modify().returnValue(unameCrit);
        mockGui.getUroleCrit();
        modify().returnValue(roleCrit);
        mockDataSvc.findUsers(unameCrit, roleCrit, new ArrayList<UserPerson>());
        modify().returnValue(false);
        mockGui.setSearchResult(new ArrayList<UserPerson>());
        startVerification();
        controller.doSearch();
    }

    public void testEditUser() {
        ArrayList<Role> allRoles = new ArrayList<Role>();
        controller.setAllRoles(allRoles);
        UserPerson up = new UserPerson();
        up.usrId = 1L;
        User usr = new User();
        User editUsr = new User();
        editUsr.setName("uname");
        UserPerson editUp = new UserPerson();
        editUp.usrNm = editUsr.getName();
        int selIdx = 2;
        mockGui.getSelRow();
        modify().returnValue(selIdx);
        mockGui.getUser(selIdx);
        modify().returnValue(up);
        mockDataSvc.fetchUser(up.usrId);
        modify().returnValue(usr);
        mockEditCtrl.setUp(usr, new ArrayList<Role>());
        mockEditCtrl.getResult();
        modify().returnValue(editUsr);
        mockGui.updResult(selIdx, editUp);
        startVerification();
        controller.editUser();
    }

    public void testEditUserCancelled() {
        ArrayList<Role> allRoles = new ArrayList<Role>();
        controller.setAllRoles(allRoles);
        UserPerson up = new UserPerson();
        up.usrId = 1L;
        User usr = new User();
        User editUsr = new User();
        editUsr.setName("uname");
        int selIdx = 2;
        mockGui.getSelRow();
        modify().returnValue(selIdx);
        mockGui.getUser(selIdx);
        modify().returnValue(up);
        mockDataSvc.fetchUser(up.usrId);
        modify().returnValue(usr);
        mockEditCtrl.setUp(usr, new ArrayList<Role>());
        mockEditCtrl.getResult();
        startVerification();
        controller.editUser();
    }

    public void testRemUser() {
        UserPerson up = new UserPerson();
        up.usrId = 1L;
        int selIdx = 2;
        mockGui.getSelRow();
        modify().returnValue(selIdx);
        mockGui.getUser(selIdx);
        modify().returnValue(up);
        mockDataSvc.delUser(up.usrId);
        mockGui.remFromResult(selIdx);
        startVerification();
        controller.remUser();
    }
}
