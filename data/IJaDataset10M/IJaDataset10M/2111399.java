package egu.plugin.util.interfaces;

import org.eclipse.core.runtime.IProgressMonitor;

public interface IFileBuilder {

    boolean open();

    boolean create();

    boolean isExist();

    boolean write(String dataToWrite, IProgressMonitor monitor);

    boolean write(IWritable writable, IProgressMonitor monitor);

    boolean close();
}
