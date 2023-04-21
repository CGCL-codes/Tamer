package se.liu.ida.JessTab;

import jess.*;

/**
 * Visit some Jess classes. Lets you, for example, print out compelx
 * structures ithout putting the printing code in the structures
 * themselves.
 *
 * @author Henrik Eriksson
 */
public interface JessTabVisitor extends Visitor {

    public Object visitDefmessagehandler(Defmessagehandler dmh);

    public Object visitDefgeneric(Defgeneric dmh);

    public Object visitDefmethod(Defmethod dmh);
}
