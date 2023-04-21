package ch.sahits.codegen.generator;

import java.io.File;
import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import ch.sahits.codegen.core.util.WorkspaceFragmentProvider;
import ch.sahits.model.IGeneratedObject;
import ch.sahits.model.IOutputFileModel;
import ch.sahits.model.IOutputFileModelBuilder;
import ch.sahits.model.ModelBuilderFactory;

/**
 * Generate an ServiceNameMessageReceiverInOut.java file in the appropriate
 * package. The output location should be the project root.
 * @author Andi Hotz, Sahits GmbH
 * @since 1.2.0
 */
public class Axis2MessageInOutReciever extends BasicAxis2ModelGenerator implements IGeneralFileGenerator {

    /** Wrapper generator */
    private AxisWSDL2JavaWrapper wsdl2java;

    /**
	 * Initialize the Registry after the initialisation of the model
	 * @see ch.sahits.codegen.generator.BasicAxis2ModelGenerator#init(IOutputFileModel, IGeneratedObject)
	 */
    @Override
    public void init(IOutputFileModel outputModel, IGeneratedObject model) {
        super.init(outputModel, model);
        wsdl2java = new AxisWSDL2JavaWrapper(ws, this.model);
        String outputPath = getAbsoluteOutputLoacation();
        IOutputFileModelBuilder b = (IOutputFileModelBuilder) ModelBuilderFactory.newBuilder(IOutputFileModel.class);
        outputModel = b.outputFileModel(outputModel).fileName(outputPath).build();
    }

    /**
	 * Generate the file reference and delegate the creation of the file to {@link AxisWSDL2JavaWrapper}
	 * @return File instance referencing the file to be created
	 */
    public IFile generate() {
        String outputPath = getAbsoluteOutputLoacation();
        wsdl2java.calculateOption(outputPath.substring(outputPath.lastIndexOf(File.separator)));
        wsdl2java.generate();
        wsdl2java.cleanup();
        IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
        IResource resource = root.findMember(new Path(model.getStoreLocation()));
        IContainer container = (IContainer) resource;
        String fileName = model.getFileName();
        String prefix = WorkspaceFragmentProvider.getAbsolutWorkspacePath();
        fileName = fileName.substring(prefix.length() + 1);
        fileName = fileName.substring(fileName.indexOf(File.separator) + 1);
        IPath path = new Path(fileName);
        IFile file = container.getFile(path);
        return file;
    }

    /**
	 * Retrieve the absolute path to the file output location based on the
	 * initialized model. The file is always service.xml regardless of the specified
	 * name in the model.
	 * @return Absolute path to the output location
	 */
    public String getAbsoluteOutputLoacation() {
        return wsdl2java.getCorretedPath(ws.getServiceName() + AxisWSDL2JavaWrapper.MESSAGE_RECEIVER_IN_OUT_FRAGMENT).get(0);
    }
}
