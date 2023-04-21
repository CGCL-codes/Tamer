package org.remus.infomngmnt.mindmap.extension;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.emf.common.command.Command;
import org.eclipse.remus.InformationUnit;
import org.eclipse.remus.core.create.PostCreationHandler;

/**
 * @author Tom Seidel <tom.seidel@remus-software.org>
 */
@SuppressWarnings("restriction")
public class MindmapPostCreationHandler extends PostCreationHandler {

    @Override
    public Command handlePreSaving(final InformationUnit unit, final IProgressMonitor monitor) {
        return null;
    }
}
