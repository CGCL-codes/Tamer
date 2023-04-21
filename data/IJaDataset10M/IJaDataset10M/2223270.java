package org.references.method;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.NoSuchElementException;
import org.dml.tools.Initer;
import org.dml.tools.RunTime;
import org.dml.tracking.Factory;
import org.references.Reference;

/**
 * 
 * a list of all parameters passed to a method<br>
 * supposedly formed of objects which are references to real instances like
 * String<br>
 * you may have the same object twice, acting as two different parameters(different ParamName), but
 * this object is in fact a referent to the real instance which is ie. String,
 * 
 * same ParamName cannot have two objects in the same MethodParams list<br>
 * so there's a one to one mapping between a ParamName and it's value<br>
 * although the same ParamName can be used with another value in a different MethodParams<br>
 * 
 * TODO: maybe somehow do type checking or something when get/write and store the type in ParamID ? but also allow like
 * subclass of , or superclass of , to be specified as type
 */
public class MethodParams {

    private final HashMap<ParamID, Reference<Object>> listOfParamsWithValues = new HashMap<ParamID, Reference<Object>>();

    /**
	 * this will never be deInit-ed or similar<br>
	 * 
	 * @return new instance of MethodParams
	 */
    public static MethodParams getNew() {
        MethodParams one = new MethodParams();
        return one;
    }

    /**
	 * private constructor
	 */
    private MethodParams() {
        super();
    }

    public int size() {
        return listOfParamsWithValues.size();
    }

    /**
	 * explicitly get value instead of ref to value<br>
	 * 
	 * @param paramName
	 * @return get the value object not the reference to it
	 * @throws NoSuchElementException
	 *             if there is no mapping between paramName and a value<br>
	 *             the exception is wrapped! use {@link RunTime#isThisWrappedException_of_thisType(Throwable, Class)}
	 */
    public Object getEx(ParamID paramName) {
        Reference<Object> ref = this.get(paramName);
        if (null == ref) {
            RunTime.thro(new NoSuchElementException("a certain parameter was expected but was not specified by caller"));
        }
        return ref.getObject();
    }

    /**
	 * this method will search for paramName and return a reference to it's
	 * value Object<br>
	 * 
	 * @param paramName
	 * @return reference; null if not found, or the ref pointing to the value,
	 *         the value can be null tho;<br>
	 *         that's why we need a reference because the value pointed to by ref can be null AND also there can be no
	 *         such ref indicated by returning null<br>
	 *         return null if not found; use .getObject() to get the value
	 */
    public Reference<Object> get(ParamID paramName) {
        RunTime.assumedNotNull(paramName);
        return listOfParamsWithValues.get(paramName);
    }

    /**
	 * @param paramName
	 * @param value
	 *            can be null or an object that was already used as a parameter
	 *            one or more times
	 * @return reference to previous value(aka object) or null if none
	 */
    public Reference<Object> set(ParamID paramName, Object value) {
        RunTime.assumedNotNull(paramName);
        Reference<Object> ref = this.get(paramName);
        Reference<Object> prevValue = ref;
        if (null == ref) {
            ref = new Reference<Object>();
            int bug = listOfParamsWithValues.size();
            Object oldValue = listOfParamsWithValues.put(paramName, ref);
            RunTime.assumedNull(oldValue);
            RunTime.assumedTrue(listOfParamsWithValues.size() == 1 + bug);
        }
        ref.setObject(value);
        return prevValue;
    }

    /**
	 * easy cast wrapper
	 * 
	 * @param paramName
	 * @return get the value object as a String object
	 */
    public String getExString(ParamID paramName) {
        RunTime.assumedNotNull(paramName);
        Object o = this.getEx(paramName);
        if (!(o instanceof String)) {
            RunTime.badCall("that parameter didn't have a String in it - cast fail");
        }
        return (String) o;
    }

    /**
	 * @param paramName
	 * @return false if didn't exist; true if it did exist, but it doesn't now
	 *         after call
	 */
    public boolean remove(ParamID paramName) {
        RunTime.assumedNotNull(paramName);
        return null != listOfParamsWithValues.remove(paramName);
    }

    /**
	 * this will merge the two, such as <code>this</code> will have both<br>
	 * 
	 * @param withThisNewOnes
	 *            the contents of this parameter list will be copied to <code>this</code>
	 * @param overwrite
	 *            if true then overwrite the params that already exist in <code>this</code> with those that exist in
	 *            <code>withThisNewOnes</code><br>
	 *            clearly this means that those that don't exist in <code>this</code> will be added from
	 *            withThisNewOnes, but those that do
	 *            exist in <code>this</code> will be lost
	 */
    public void mergeWith(MethodParams withThisNewOnes, boolean overwrite) {
        RunTime.assumedNotNull(withThisNewOnes, overwrite);
        if (this == withThisNewOnes) {
            RunTime.badCall("attempted to merge with self");
        }
        Iterator<Entry<ParamID, Reference<Object>>> iter = withThisNewOnes.getIter();
        while (iter.hasNext()) {
            Entry<ParamID, Reference<Object>> current = iter.next();
            ParamID paramName = current.getKey();
            Reference<Object> refToValue = current.getValue();
            boolean alreadyExists = this.get(paramName) != null;
            if ((alreadyExists && overwrite) || (!alreadyExists)) {
                this.set(paramName, refToValue.getObject());
                RunTime.assumedTrue(this.getEx(paramName) == withThisNewOnes.getEx(paramName));
                RunTime.assumedTrue(this.get(paramName) != withThisNewOnes.get(paramName));
            }
        }
    }

    /**
	 * @return
	 * 
	 */
    private Iterator<Entry<ParamID, Reference<Object>>> getIter() {
        return listOfParamsWithValues.entrySet().iterator();
    }

    public void clear() {
        listOfParamsWithValues.clear();
        RunTime.assumedTrue(this.size() == 0);
    }

    /**
	 * after calling this, you will need to use deInit() on the returned,
	 * because the returned did an init()
	 * 
	 * @return clone of this
	 */
    public MethodParams getClone() {
        MethodParams clone = getNew();
        RunTime.assumedTrue(clone.size() == 0);
        clone.mergeWith(this, false);
        RunTime.assumedTrue(clone.size() == this.size());
        RunTime.assumedTrue(clone.listOfParamsWithValues.size() == listOfParamsWithValues.size());
        RunTime.assumedTrue(clone.listOfParamsWithValues != listOfParamsWithValues);
        RunTime.assumedTrue(clone != this);
        return clone;
    }

    @Override
    public String toString() {
        return super.toString() + listOfParamsWithValues.toString();
    }

    /**
	 * @param outputs
	 * @param params
	 */
    @Deprecated
    public static void expectedOutputs(MethodParams outputs, ParamID... params) {
        RunTime.assumedNotNull(outputs, params);
        RunTime.assumedTrue(params.length > 0);
        RunTime.assumedTrue(outputs.size() > 0);
        for (ParamID param : params) {
            RunTime.assumedNotNull(param);
            Reference<Object> ref2RVar = outputs.get(param);
            if (null == ref2RVar) {
                RunTime.badCall("the expected param(" + param + ") was not in the outputs list");
            } else {
                Object rVar = ref2RVar.getObject();
                if (!(rVar instanceof Reference)) {
                    RunTime.badCall("you passed a non Reference variable(rVar) for param " + param);
                }
                RunTime.assumedNotNull(rVar);
            }
        }
    }

    /**
	 * @param paramID
	 * @param value
	 */
    @Deprecated
    public void setRVarValue(ParamID paramID, Object value) {
        RunTime.assumedNotNull(paramID);
        Reference<Object> ref2RVar = this.get(paramID);
        if (null == ref2RVar) {
            RunTime.badCall("the param(" + paramID + ") must already exist before setting it's value. why? because it's a reference to the value; so that ref must exist");
        } else {
            Object rVar = ref2RVar.getObject();
            if (!(rVar instanceof Reference)) {
                RunTime.bug("should not be that the already existing paramID has a non reference associated with it");
            } else {
                RunTime.assumedNotNull(rVar);
                @SuppressWarnings("unchecked") Reference<Object> rVarCast = (Reference<Object>) rVar;
                rVarCast.setObject(value);
            }
        }
    }

    /**
	 * @param paramID
	 * @param rVar
	 */
    @Deprecated
    public void associate(ParamID paramID, Reference<?> rVar) {
        RunTime.assumedNotNull(paramID, rVar);
        if (null != this.get(paramID)) {
            RunTime.badCall("the param(" + paramID + ") must not already be set");
        } else {
            this.set(paramID, rVar);
        }
    }
}
