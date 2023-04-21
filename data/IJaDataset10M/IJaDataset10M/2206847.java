package stack.era.domain.job;

import stack.era.domain.DomainObject;

public class Job extends DomainObject {

    private static final long serialVersionUID = -1203233651688539457L;

    public Job(String newName) {
        super(newName);
    }

    @Override
    public void update() {
    }
}
