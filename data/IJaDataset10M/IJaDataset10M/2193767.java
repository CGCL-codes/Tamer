package org.perfectjpattern.jee.integration.dao;

import java.lang.reflect.*;
import java.util.*;
import org.apache.commons.lang.*;
import org.eclipse.persistence.queries.*;
import org.eclipse.persistence.sessions.*;
import org.eclipse.persistence.sessions.server.*;
import org.perfectjpattern.core.structural.adapter.*;
import org.perfectjpattern.jee.api.integration.dao.*;

/**
 * Adapts EclipseLink {@link DatabaseQuery} to the JPA implementation-free 
 * PerfectJPattern's {@link IQuery} definition 
 * 
 * @author <a href="mailto:bravegag@hotmail.com">Giovanni Azua</a>
 * @version $Revision: 1.0 $Date: Jul 18, 2009 12:53:32 PM $
 */
public class ReadQueryAdapter extends Adapter<IQuery, ReadQuery> {

    @SuppressWarnings("unchecked")
    public <Element> List<Element> getResultList() {
        ReadQuery myReadQuery = getUnderlying();
        if (myReadQuery.getArgumentValues().size() > 0 && theNamedParameters.size() > 0) {
            throw new IllegalArgumentException("ReadQueryAdapter must be in " + "one mode only, either with positional or named parameters");
        }
        List<Object> myArguments = new ArrayList<Object>();
        if (myReadQuery.getArgumentValues().size() > 0) {
            myArguments.addAll(myReadQuery.getArgumentValues());
        } else {
            List<String> myArgumentNames = myReadQuery.getArguments();
            for (String myArgumentName : myArgumentNames) {
                Validate.isTrue(theNamedParameters.containsKey(myArgumentName), "'aNamedArguments' does not contain required parameter '" + myArgumentName + "'");
                myArguments.add(theNamedParameters.get(myArgumentName));
            }
        }
        List<Element> myElements = (List<Element>) theSession.executeQuery(myReadQuery.getName(), thePersistentClass, myArguments);
        return myElements;
    }

    public Object getSingleResult() {
        return getUnderlying().getFirstResult();
    }

    public IQuery setParameter(int aPosition, Object aValue) {
        getUnderlying().addArgumentValue(aValue);
        return getTarget();
    }

    public IQuery setParameter(String aName, Object aValue) {
        theNamedParameters.put(aName, aValue);
        return getTarget();
    }

    public Object setMaxResults(int aMaxRows) {
        getUnderlying().setMaxRows(aMaxRows);
        return getTarget();
    }

    public int executeUpdate() {
        throw new UnsupportedOperationException("'executeUpdate' not supported");
    }

    public String[] getNamedParameters() {
        DatabaseQuery myQuery = super.getUnderlying();
        final int myLength = myQuery.getArguments().size();
        String[] myNamedParameters = new String[myLength];
        for (int i = 0; i < myLength; i++) {
            myNamedParameters[i] = myQuery.getArguments().get(i).toString();
        }
        return myNamedParameters;
    }

    /**
     * Constructs a {@link QueryAdapter} from the Adaptee 
     * {@link ReadQuery} instance
     * 
     * @param anAdaptee Adaptee {@link ReadQuery} instance
     * @param aSession Holding {@link ServerSession} instance
     * @throws IllegalArgumentException
     */
    protected ReadQueryAdapter(ReadQuery anAdaptee, Session aSession, Class<?> aPersistentClass) throws IllegalArgumentException {
        super(IQuery.class, anAdaptee);
        Validate.notNull(aSession, "'aSession' must not be null");
        Validate.notNull(aSession, "'aPersistentClass' must not be null");
        theSession = aSession;
        thePersistentClass = aPersistentClass;
    }

    /** 
     * {@inheritDoc}
     */
    @Override
    protected Object invokeUnderlying(Method aMethod, Object[] anArguments) throws Throwable {
        try {
            return super.invokeUnderlying(aMethod, anArguments);
        } catch (InvocationTargetException anException) {
            throw new DaoException(anException.getCause());
        } catch (Throwable anException) {
            throw new DaoException(anException);
        }
    }

    private final Session theSession;

    private final Class<?> thePersistentClass;

    private final Map<String, Object> theNamedParameters = new HashMap<String, Object>();
}
