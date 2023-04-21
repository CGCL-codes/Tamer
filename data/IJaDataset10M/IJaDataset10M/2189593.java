package uk.ac.lkl.migen.system.ai.um;

import uk.ac.lkl.common.util.reflect.StaticInstanceObject;

public abstract class AbstractLearnerModelAttribute extends StaticInstanceObject {

    protected AbstractLearnerModelAttribute(int id, String name, String description) {
        super(id, name, description);
    }

    protected AbstractLearnerModelAttribute(int id, String description) {
        super(id, description);
    }
}
