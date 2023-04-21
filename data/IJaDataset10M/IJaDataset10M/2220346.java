package hu.takacsot.validator.bean;

import hu.takacsot.validator.ErrorItem;

public class NeverErrorChecker extends Checker {

    @Override
    public ErrorItem check(Object o, String property, Object value) {
        return null;
    }
}
