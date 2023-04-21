package net.sf.xsnapshot.transformers;

import net.sf.xsnapshot.TransformContext;
import net.sf.xsnapshot.Transformer;
import net.sf.xsnapshot.TransformerArgs;
import net.sf.xsnapshot.XSnapshotTransformException;
import net.sf.xsnapshot.XSnapshotUtils;

/**
 * Transformer that assumes the value it is passed is a snapshot, converts
 * it to the corresponding model, and returns the model instance.
 * There should be no need to specify this transformer explicitly in the xdoclet 
 * markup as it is invoked behind the scenes. However, you can customize 
 * snapshot-to-model transformation by subclassing this transformer and 
 * registering it as the "snapshot-to-model" transformer with the XSnapshotRegistry.
 * 
 * @author Daniel Kokotov
 */
public class SnapshotToModelTransformer implements Transformer {

    /**
   * Assumes objectToTransform is a snapshot, converts it to its corresponding
   * model, and returns the converted model instance.
   * @see net.sf.xsnapshot.Transformer#transform(java.lang.Class, java.lang.Object, net.sf.xsnapshot.TransformerArgs, net.sf.xsnapshot.TransformContext)
   */
    public Object transform(Class returnClass, Object objectToTransform, TransformerArgs args, TransformContext context) throws XSnapshotTransformException {
        Object model = XSnapshotUtils.createModel(objectToTransform, context);
        return model;
    }
}
