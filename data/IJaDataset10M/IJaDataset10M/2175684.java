package org.remus.infomngmnt.password;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.remus.common.core.streams.StreamCloser;
import org.eclipse.remus.js.rendering.FreemarkerRenderer;
import org.eclipse.remus.util.StatusCreator;

/**
 * @author Jan Hartwig <jhartwig@feb-radebeul.de>
 * 
 */
public class AbstractInformationRepresentation extends org.eclipse.remus.core.extension.AbstractInformationRepresentation {

    public AbstractInformationRepresentation() {
    }

    @Override
    public InputStream handleHtmlGeneration(final IProgressMonitor monitor) throws CoreException {
        ByteArrayOutputStream returnValue = new ByteArrayOutputStream();
        InputStream templateIs = null;
        InputStream contentsIs = getFile().getContents();
        try {
            templateIs = FileLocator.openStream(Platform.getBundle(PasswordPlugin.PLUGIN_ID), new Path("$nl$/template/htmlserialization.flt"), true);
            FreemarkerRenderer.getInstance().process(PasswordPlugin.PLUGIN_ID, templateIs, contentsIs, returnValue, null);
        } catch (IOException e) {
            throw new CoreException(StatusCreator.newStatus("Error reading locations", e));
        } finally {
            StreamCloser.closeStreams(templateIs, contentsIs);
        }
        return new ByteArrayInputStream(returnValue.toByteArray());
    }
}
