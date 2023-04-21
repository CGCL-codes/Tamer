package hu.cubussapiens.modembed.pic.device.tranformation;

import hu.cubussapiens.modembed.ITransformation;
import hu.cubussapiens.modembed.ITransformationContext;
import hu.cubussapiens.modembed.MODembedCore;
import java.io.IOException;
import java.net.URL;
import java.util.Collections;
import java.util.List;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.emf.common.util.Diagnostic;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.m2m.qvt.oml.BasicModelExtent;
import org.eclipse.m2m.qvt.oml.ExecutionContextImpl;
import org.eclipse.m2m.qvt.oml.ExecutionDiagnostic;
import org.eclipse.m2m.qvt.oml.ModelExtent;
import org.eclipse.m2m.qvt.oml.TransformationExecutor;
import org.eclipse.m2m.qvt.oml.util.Log;
import pic.PicCPUType;

/**
 * @author balazs.grill
 *
 */
public class DeviceTransformation implements ITransformation {

    private static final String TRANSFORM_DEVICE = "/transforms/deviceTransformation.qvto";

    @Override
    public void transform(Resource source, IProgressMonitor monitor, ITransformationContext tcontext) {
        URL transformation = DeviceTranformationPlugin.getDefault().getBundle().getEntry(TRANSFORM_DEVICE);
        try {
            transformation = FileLocator.resolve(transformation);
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        URI transformationURI = URI.createURI(transformation.toString());
        URI srcuri = source.getURI();
        String name = srcuri.lastSegment().replaceAll("\\.dev", "");
        URI target = MODembedCore.resolveStringReference(srcuri, name + ".xmi");
        TransformationExecutor executor = new TransformationExecutor(transformationURI);
        EList<EObject> inObjects = source.getContents();
        ModelExtent input = new BasicModelExtent(inObjects);
        ModelExtent output = new BasicModelExtent();
        ExecutionContextImpl context = new ExecutionContextImpl();
        context.setLog(new Log() {

            @Override
            public void log(int level, String message, Object param) {
                System.out.println(message);
            }

            @Override
            public void log(String message, Object param) {
                System.out.println(message);
            }

            @Override
            public void log(int level, String message) {
                System.out.println(message);
            }

            @Override
            public void log(String message) {
                System.out.println(message);
            }
        });
        context.setConfigProperty("keepModeling", true);
        ExecutionDiagnostic result = executor.execute(context, input, output);
        if (result.getSeverity() == Diagnostic.OK) {
            List<EObject> outObjects = output.getContents();
            for (EObject eo : outObjects) {
                if (eo instanceof PicCPUType) {
                    PicCPUType cpu = ((PicCPUType) eo);
                    cpu.setName(name);
                    cpu.getConfiguration().getScheme().setName(name);
                }
            }
            Resource outResource = tcontext.createTargetResource(target);
            outResource.getContents().addAll(outObjects);
            try {
                outResource.save(Collections.emptyMap());
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            System.err.println(result);
            for (Diagnostic d : result.getChildren()) {
                System.err.println(d);
            }
        }
    }
}
