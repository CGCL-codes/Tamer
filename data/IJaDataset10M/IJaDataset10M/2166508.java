package net.sf.joafip.heapfile.record.service;

import java.io.File;
import net.sf.joafip.heapfile.record.entity.IFileStorable;
import net.sf.joafip.heapfile.service.HeapException;

/**
 * to write only class implementing {@link IFileStorable} to file.<br>
 * {@link #open()} and {@link #close()}<br>
 * 
 * @author luc peuvrier
 * 
 */
public class FileForStorableWriteOnly extends FileForStorable {

    public FileForStorableWriteOnly(File file) throws HeapException {
        super(file);
    }

    public FileForStorableWriteOnly(String fileName) throws HeapException {
        super(fileName);
    }

    @Override
    public void readStorable(IFileStorable storable) throws HeapException {
        throw new UnsupportedOperationException();
    }

    @Override
    public void read(byte[] data) throws HeapException {
        throw new UnsupportedOperationException();
    }
}
