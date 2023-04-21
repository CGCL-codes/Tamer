package hu.cubussapiens.modembed;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.emf.ecore.resource.Resource;

/**
 * @author balazs.grill
 *
 */
public interface ITransformation {

    public void transform(Resource source, IProgressMonitor monitor, ITransformationContext context);
}
