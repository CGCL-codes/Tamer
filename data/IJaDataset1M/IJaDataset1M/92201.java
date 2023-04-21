package org.remus.infomngmnt.core.commands;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.common.command.CompoundCommand;
import org.remus.infomngmnt.BinaryReference;
import org.remus.infomngmnt.InformationUnit;
import org.remus.infomngmnt.InformationUnitListItem;
import org.remus.infomngmnt.core.model.InformationStructureRead;
import org.remus.infomngmnt.resources.util.ResourceUtil;

/**
 * @author Tom Seidel <tom.seidel@remus-software.org>
 */
public class MoveInformationUnitCommand implements Command {

    public MoveInformationUnitCommand(final InformationUnitListItem affectedObject, final String targetPath) {
        this.targetPath = targetPath;
        this.affectedFile = ResourcesPlugin.getWorkspace().getRoot().getFile(new Path(affectedObject.getWorkspacePath()));
        this.sourcePath = affectedObject.getWorkspacePath();
        this.targetFile = ResourcesPlugin.getWorkspace().getRoot().getFile(new Path(IPath.SEPARATOR + targetPath));
        InformationUnit fullObject = (InformationUnit) affectedObject.getAdapter(InformationUnit.class);
        InformationStructureRead read = InformationStructureRead.newSession(fullObject);
        List<BinaryReference> binaryReferences = read.getBinaryReferences();
        this.sourceBinaryReferences = new ArrayList<IFile>();
        this.targetBinaryReferences = new ArrayList<IFile>();
        for (BinaryReference binaryReference : binaryReferences) {
            binaryReference.getProjectRelativePath();
            this.sourceBinaryReferences.add(ResourcesPlugin.getWorkspace().getRoot().getProject(new Path(this.sourcePath).segment(0)).getFolder(ResourceUtil.BINARY_FOLDER).getFile(binaryReference.getProjectRelativePath()));
            this.targetBinaryReferences.add(ResourcesPlugin.getWorkspace().getRoot().getProject(new Path(this.targetPath).segment(0)).getFolder(ResourceUtil.BINARY_FOLDER).getFile(binaryReference.getProjectRelativePath()));
        }
    }

    private final IFile affectedFile;

    private final IFile targetFile;

    private final String targetPath;

    private final String sourcePath;

    private final List<IFile> sourceBinaryReferences;

    private final List<IFile> targetBinaryReferences;

    public boolean canExecute() {
        return this.affectedFile.exists() && !this.targetFile.exists() && ResourcesPlugin.getWorkspace().getRoot().getProject(new Path(this.targetPath).segment(0)).exists();
    }

    public boolean canUndo() {
        return true;
    }

    public Command chain(final Command command) {
        CompoundCommand compoundCommand = new CompoundCommand();
        compoundCommand.append(this);
        compoundCommand.append(command);
        return compoundCommand;
    }

    public void dispose() {
    }

    public void execute() {
        try {
            NullProgressMonitor monitor = new NullProgressMonitor();
            if (this.affectedFile.getLocationURI().getScheme().equals(this.targetFile.getLocationURI().getScheme())) {
                this.affectedFile.move(new Path(IPath.SEPARATOR + this.targetPath), true, monitor);
                for (int i = 0, n = this.sourceBinaryReferences.size(); i < n; i++) {
                    this.sourceBinaryReferences.get(i).move(this.targetBinaryReferences.get(i).getFullPath(), true, monitor);
                }
            } else {
                this.targetFile.create(this.affectedFile.getContents(), true, monitor);
                this.affectedFile.delete(true, monitor);
                for (int i = 0, n = this.sourceBinaryReferences.size(); i < n; i++) {
                    this.targetBinaryReferences.get(i).create(this.sourceBinaryReferences.get(i).getContents(), true, monitor);
                    this.sourceBinaryReferences.get(i).delete(true, monitor);
                }
            }
        } catch (CoreException e) {
            e.printStackTrace();
        }
    }

    public Collection<?> getAffectedObjects() {
        return Collections.EMPTY_LIST;
    }

    public String getDescription() {
        return null;
    }

    public String getLabel() {
        return null;
    }

    public Collection<?> getResult() {
        return Collections.EMPTY_LIST;
    }

    public void redo() {
        execute();
    }

    public void undo() {
        try {
            NullProgressMonitor monitor = new NullProgressMonitor();
            if (this.affectedFile.getLocationURI().getScheme().equals(this.targetFile.getLocationURI().getScheme())) {
                this.targetFile.move(new Path(this.sourcePath), true, monitor);
                for (int i = 0, n = this.targetBinaryReferences.size(); i < n; i++) {
                    this.targetBinaryReferences.get(i).move(this.sourceBinaryReferences.get(i).getFullPath(), true, monitor);
                }
            } else {
                this.affectedFile.create(this.targetFile.getContents(), true, monitor);
                this.targetFile.delete(true, monitor);
                for (int i = 0, n = this.targetBinaryReferences.size(); i < n; i++) {
                    this.sourceBinaryReferences.get(i).create(this.targetBinaryReferences.get(i).getContents(), true, monitor);
                    this.targetBinaryReferences.get(i).delete(true, monitor);
                }
            }
        } catch (CoreException e) {
            e.printStackTrace();
        }
    }
}
