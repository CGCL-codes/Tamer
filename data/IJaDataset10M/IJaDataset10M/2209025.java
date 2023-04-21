package org.openi.util;

/**
 * Folder visitor interface
 * 
 * @author Dipendra Pokhrel
 *
 */
public interface FolderVisitor {

    public void visit(Folder folder);

    public void visit(FileItem file);
}
