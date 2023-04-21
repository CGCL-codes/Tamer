package org.dmdf.dmt.externalizers;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

/**
 * @author Steven Wang
 * 
 * <p>
 * A super class for all ObjectExternalizer implementation. All Externalizer
 * handles non-primitive type shouold extend this class.
 * 
 * <p>
 * Since you are using DMTFramework, you don't need implement any of the
 * externalizer for your Domain Model Object.
 * <p>
 * However, some case you might use this to make your Domain Model
 * transportation more efficient - especially for those reference data, to
 * client it is read only, and client have a cache to hold on it. For example:
 * 
 * <pre>
 *      public class MyObjectExternalizer extends ObjectExternalizer {
 *          	public Object doRead(ObjectInput objectInput) throws IOException, ClassNotFoundException {
 *   				Object id = objectInput.readObject();
 *   				//get MyObject from local cache
 *   				return getMyObjectFromLocalCache(id);
 *     		}
 *    
 *     		public void doWrite(Object obj, ObjectOutput objectOutput) throws IOException {
 *     			//we can only write the id for this object, and the client side can get it back from it's own cache
 *     			objectOutput.writeObject(((MyObject)obj).getId());
 *     		}
 *       }
 * </pre>
 * 
 * <p>
 * Another usage is if you can condense the data need to be streamlized. for
 * Example:
 * 
 * <pre>
 * public class ColorExternalizer extends ObjectExternalizer {
 * 
 * 	public Object doRead(ObjectInput objectInput) throws IOException {
 * 		return new Color(objectInput.readInt());
 * 	}
 * 
 * 	public void doWrite(Object obj, ObjectOutput objectOutput) throws IOException {
 * 		objectOutput.writeInt(((Color) obj).getRGB());
 * 	}
 * 
 * }
 * </pre>
 * 
 * <p>
 * In the required method <code>doRead()</code> and <code>doWrite()</code>,
 * the client object guarenteed not null.
 * 
 */
public abstract class ObjectExternalizer implements Externalizer {

    public final Object read(ObjectInput objectInput) throws IOException, ClassNotFoundException {
        return NullFlag.notNull(objectInput) ? doRead(objectInput) : null;
    }

    /**
	 * The subclass implementation responsible read the object back
	 * 
	 * @param objectInput
	 * @return Object
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
    protected abstract Object doRead(ObjectInput objectInput) throws IOException, ClassNotFoundException;

    public void write(Object obj, ObjectOutput objectOutput) throws IOException {
        NullFlag.flagNotNull(objectOutput, obj != null);
        if (obj != null) {
            doWrite(obj, objectOutput);
        }
    }

    /**
	 * The subclass is responsible write the obj to objectOutput, the obj is
	 * guarenteed not null, subclass do not need do any null check on that
	 * 
	 * @param obj
	 * @param objectOutput
	 * @throws IOException
	 */
    protected abstract void doWrite(Object obj, ObjectOutput objectOutput) throws IOException;
}
