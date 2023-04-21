package net.sf.xsnapshot.converters;

import net.sf.xsnapshot.TransformContext;
import net.sf.xsnapshot.Transformer;
import net.sf.xsnapshot.TransformerArgs;
import net.sf.xsnapshot.XSnapshotTransformException;

/**
 * This converter converts anything to a string by invoking toString on it
 * @author dkokotov@vecna.com
 * @version $Revision: 38 $
 */
public class ToStringTransformer implements Transformer {

    /**
   * returns the String representation of objectToConvert by invoking toString on it
   * if it is null, returns a null string 
   */
    public Object transform(Class returnClass, Object objectToTransform, TransformerArgs args, TransformContext context) throws XSnapshotTransformException {
        if (!returnClass.equals(String.class)) {
            throw new XSnapshotTransformException("ToStringTransformer can only convert to String");
        }
        if (objectToTransform == null) {
            return null;
        } else {
            return objectToTransform.toString();
        }
    }
}
