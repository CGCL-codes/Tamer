package org.remus.infomngmnt.mindmap.extension;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.List;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.emf.common.util.URI;
import org.eclipse.remus.BinaryReference;
import org.eclipse.remus.common.core.streams.StreamCloser;
import org.eclipse.remus.core.extension.AbstractInformationRepresentation;
import org.eclipse.remus.core.model.InformationStructureRead;
import org.eclipse.remus.util.InformationUtil;
import org.eclipse.remus.util.StatusCreator;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.ImageLoader;
import org.eclipse.swt.widgets.Display;
import org.remus.infomngmnt.mindmap.MindmapActivator;
import org.xmind.core.IWorkbook;
import org.xmind.core.io.IStorage;
import org.xmind.ui.internal.editor.WorkbookRef;
import org.xmind.ui.internal.editor.WorkbookRefManager;
import org.xmind.ui.mindmap.MindMapImageExtractor;

/**
 * @author Tom Seidel <tom.seidel@remus-software.org>
 */
public class MindMapInformationRepresentation extends AbstractInformationRepresentation {

    private WorkbookRefManager manager;

    private WorkbookRef workbookRef;

    private IStorage storage;

    private String imageHref;

    @Override
    public void handlePreBuild(final IProgressMonitor monitor) {
        InformationStructureRead read = InformationStructureRead.newSession(getValue());
        List<BinaryReference> binaryReferences = read.getBinaryReferences();
        try {
            if (binaryReferences.size() > 0) {
                IFile binaryReferenceToFile = InformationUtil.binaryReferenceToFile(binaryReferences.get(0), getValue());
                manager = WorkbookRefManager.getInstance();
                workbookRef = manager.addReferrer(binaryReferenceToFile, null);
                storage = workbookRef.createStorage();
                workbookRef.loadWorkbook(storage, null, monitor);
            }
        } catch (CoreException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (org.xmind.core.CoreException e) {
            e.printStackTrace();
        }
        super.handlePreBuild(monitor);
    }

    @Override
    public void handlePostBuild(final IFile derivedFile, final IProgressMonitor monitor) throws CoreException {
        workbookRef.dispose(true);
        super.handlePostBuild(derivedFile, monitor);
    }

    /**
	 * 
	 */
    public MindMapInformationRepresentation() {
    }

    @SuppressWarnings("restriction")
    @Override
    public InputStream handleHtmlGeneration(final IProgressMonitor monitor) throws CoreException {
        try {
            IWorkbook workbook = workbookRef.getWorkbook();
            MindMapImageExtractor mindMapImageExtractor = new MindMapImageExtractor(Display.getDefault(), workbook.getPrimarySheet(), workbook.getPrimarySheet().getRootTopic());
            Image image = mindMapImageExtractor.getImage();
            ImageLoader loader = new ImageLoader();
            loader.data = new ImageData[] { image.getImageData() };
            IFile file = getBuildFolder().getFile("out.png");
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            loader.save(baos, SWT.IMAGE_PNG);
            mindMapImageExtractor.dispose();
            imageHref = file.getLocation().toOSString();
            byte[] bytesFromFile = baos.toByteArray();
            baos.close();
            if (!file.exists()) {
                file.create(new ByteArrayInputStream(bytesFromFile), true, monitor);
            } else {
                file.setContents(new ByteArrayInputStream(bytesFromFile), true, false, monitor);
            }
            InformationStructureRead read = InformationStructureRead.newSession(getValue());
            ByteArrayOutputStream returnValue = new ByteArrayOutputStream();
            InputStream templateIs = null;
            try {
                templateIs = FileLocator.openStream(Platform.getBundle(MindmapActivator.PLUGIN_ID), new Path("$nl$/template/htmlserialization.flt"), true);
                org.eclipse.remus.js.rendering.FreemarkerRenderer.getInstance().process(MindmapActivator.PLUGIN_ID, templateIs, returnValue, Collections.singletonMap("imageHref", URI.createFileURI(file.getLocation().toOSString())), read.getContentsAsStrucuturedMap(), read.getDynamicContentAsStructuredMap());
            } catch (IOException e) {
                throw new CoreException(StatusCreator.newStatus("Error reading locations", e));
            } finally {
                StreamCloser.closeStreams(templateIs);
            }
            return new ByteArrayInputStream(returnValue.toByteArray());
        } catch (IOException e) {
            throw new CoreException(StatusCreator.newStatus("Error generating html-output", e));
        }
    }

    @Override
    public boolean createFolderOnBuild() {
        return true;
    }
}
