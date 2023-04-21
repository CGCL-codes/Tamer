package com.mindquarry.teamspace.manager;

import org.apache.avalon.framework.service.ServiceException;
import com.mindquarry.teamspace.TeamspaceAdmin;
import com.mindquarry.teamspace.TeamspaceTestBase;

public class TeamspaceQueryApiTest extends TeamspaceTestBase {

    public void testTeamspacesForUser() throws ServiceException {
        TeamspaceAdmin admin = lookupTeamspaceAdmin();
        assertEquals(0, admin.teamspacesForUser("foo").size());
        admin.teamspacesForUser("");
    }
}
