package org.neuroph.netbeans.ide.imageexplorer.imagevalidator;

import org.openide.filesystems.FileObject;

/**
 *
 * @author Djordje
 */
public interface FileObjectValidator {

    public boolean validateFileObjects(FileObject f);
}
