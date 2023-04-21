package ca.llsutherland.squash.persistence;

import ca.llsutherland.squash.TestFacadeFactory;
import ca.llsutherland.squash.utils.Specification;

public class DeletionSpecification implements Specification<String> {

    @Override
    public boolean isSatisfiedBy(String sql) {
        return TestFacadeFactory.getTestFacade().deleteDomainObjects(sql);
    }
}
