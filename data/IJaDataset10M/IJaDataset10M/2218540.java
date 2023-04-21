package org.t2framework.commons.exception;

import static org.t2framework.commons.Constants.EMPTY_ARRAY;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Member;

public class InvocationTargetRuntimeException extends BaseRuntimeException {

    private static final long serialVersionUID = 1L;

    protected Member member;

    public InvocationTargetRuntimeException(Member member, InvocationTargetException e) {
        this(member, e, EMPTY_ARRAY);
    }

    public InvocationTargetRuntimeException(Member member, InvocationTargetException cause, Object... args) {
        super(cause.getTargetException(), "ECMN0004", member, cause.getTargetException(), args);
        this.member = member;
    }

    public Member getMember() {
        return member;
    }
}
