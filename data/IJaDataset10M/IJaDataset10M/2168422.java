package uk.ac.lkl.migen.system.server.handler.User;

import uk.ac.lkl.common.util.restlet.EntityMapper;
import uk.ac.lkl.common.util.restlet.server.EntityHandler;
import uk.ac.lkl.common.util.restlet.server.EntityId;
import uk.ac.lkl.migen.system.server.Gender;
import uk.ac.lkl.migen.system.server.User;
import uk.ac.lkl.migen.system.server.manipulator.UserTableManipulator;

public class GenderHandler extends EntityHandler<UserTableManipulator> {

    public GenderHandler(UserTableManipulator manipulator) {
        super(manipulator);
    }

    public Gender getGender(EntityId<User> userId, EntityMapper mapper) {
        return null;
    }

    public void setGender(EntityId<User> userId, Gender gender, EntityMapper mapper) {
    }
}
