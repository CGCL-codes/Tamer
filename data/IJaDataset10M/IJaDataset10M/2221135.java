package br.cin.ufpe.corba.namingservice;

import br.cin.ufpe.corba.SystemException;

public class NotFound extends SystemException {

    public enum NotFoundReason {

        missing_node, not_object, not_context
    }

    private static final long serialVersionUID = 1787137289311045426L;

    private NotFoundReason why;

    private Name rest_of_name;

    public NotFound(Name name, NotFoundReason reason) {
        this.rest_of_name = name;
        this.why = reason;
    }

    public NotFoundReason getWhy() {
        return why;
    }

    public void setWhy(NotFoundReason why) {
        this.why = why;
    }

    public Name getRestOfName() {
        return rest_of_name;
    }

    public void setRestOfName(Name rest_of_name) {
        this.rest_of_name = rest_of_name;
    }
}
