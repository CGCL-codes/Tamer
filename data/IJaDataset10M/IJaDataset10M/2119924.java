package com.mindquarry.teamspace.manager;

import org.apache.avalon.framework.service.ServiceException;
import com.mindquarry.teamspace.Teamspace;
import com.mindquarry.teamspace.TeamspaceAdmin;
import com.mindquarry.teamspace.TeamspaceAlreadyExistsException;
import com.mindquarry.teamspace.TeamspaceException;
import com.mindquarry.teamspace.TeamspaceTestBase;
import com.mindquarry.user.User;
import com.mindquarry.user.UserAdmin;
import com.mindquarry.user.UserRO;

public class TeamspaceManagerTest extends TeamspaceTestBase {

    private User queryUserById(String userId) throws ServiceException {
        UserAdmin userAdmin = lookupUserAdmin();
        return userAdmin.userById(userId);
    }

    public void testCreateAndRemoveTeamspace() throws ServiceException, TeamspaceAlreadyExistsException, TeamspaceException {
        TeamspaceAdmin teamsAdmin = lookupTeamspaceAdmin();
        UserAdmin userAdmin = lookupUserAdmin();
        String userId = "mindquarry-user";
        userAdmin.createUser(userId, "aSecretPassword", "Mindquarry User", "surname", "an email", "the skills");
        String teamspaceId = "mindquarry-teamspace";
        Teamspace teamspace = teamsAdmin.createTeamspace(teamspaceId, "Mindquarry Teamspace", "a greate description", queryUserById(userId));
        assertEquals(1, teamspace.getUsers().size());
        teamsAdmin.deleteTeamspace(teamspace);
        userAdmin.deleteUser(queryUserById(userId));
    }

    public void testCreateAndRemoveTeamspaceAsAdmin() throws ServiceException, TeamspaceAlreadyExistsException, TeamspaceException {
        TeamspaceAdmin teamsAdmin = lookupTeamspaceAdmin();
        UserAdmin userAdmin = lookupUserAdmin();
        String userId = "admin";
        UserRO creator = userAdmin.userById(userId);
        String teamspaceId = "mindquarry-teamspace";
        Teamspace team = teamsAdmin.createTeamspace(teamspaceId, "Mindquarry Teamspace", "a greate description", creator);
        assertEquals(0, team.getUsers().size());
        teamsAdmin.deleteTeamspace(team);
    }
}
