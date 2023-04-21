package com.coldcore.coloradoftp.command.impl.ftp;

import com.coldcore.coloradoftp.factory.ObjectFactory;
import com.coldcore.coloradoftp.factory.ObjectName;
import com.coldcore.coloradoftp.filesystem.FileSystem;
import com.coldcore.coloradoftp.filesystem.ListingFile;
import java.util.Set;

public class NlstCommand extends ListCommand {

    protected String prepareList(Set<ListingFile> list) {
        FileSystem fileSystem = (FileSystem) ObjectFactory.getObject(ObjectName.FILESYSTEM);
        String fileSeparator = fileSystem.getFileSeparator();
        StringBuffer sb = new StringBuffer();
        for (ListingFile f : list) {
            sb.append(f.getName());
            if (f.isDirectory()) sb.append(fileSeparator);
            sb.append("\r\n");
        }
        return sb.toString();
    }
}
