package com.google.devtools.depan.filesystem.graph;

import com.google.devtools.depan.model.ElementVisitor;

/**
 * Lists the requirements of any {@link ElementVisitor} that operates on file
 * system elements.
 *
 * @author tugrul@google.com (Tugrul Ince)
 */
public interface FileSystemElementVisitor extends ElementVisitor {

    /**
   * Performs the tasks of this visitor on the given {@link DirectoryElement}.
   *
   * @param element {@link DirectoryElement} on which the tasks of this visitor
   * will be performed.
   */
    public void visitDirectoryElement(DirectoryElement element);

    /**
   * Performs the tasks of this visitor on the given {@link FileElement}.
   *
   * @param element {@link FileElement} on which the tasks of this visitor
   * will be performed.
   */
    public void visitFileElement(FileElement element);
}
