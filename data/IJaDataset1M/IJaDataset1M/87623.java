package com.atosorigin.nl.jspring2008.mule;

import java.io.File;
import java.io.IOException;
import org.mule.MuleException;
import org.mule.providers.file.FileMessageReceiver;
import org.mule.providers.file.i18n.FileMessages;
import org.mule.umo.UMOComponent;
import org.mule.umo.endpoint.UMOEndpoint;
import org.mule.umo.lifecycle.InitialisationException;
import org.mule.umo.provider.UMOConnector;
import org.mule.util.FileUtils;

/**
 * @author Jos Dirksen (jos.dirksen@gmail.com)
 *
 */
public class SingleMessageReceiver extends FileMessageReceiver {

    private File readDirectory;

    public SingleMessageReceiver(UMOConnector connector, UMOComponent component, UMOEndpoint endpoint, String readDir, String moveDir, String moveToPattern, long frequency) throws InitialisationException {
        super(connector, component, endpoint, readDir, moveDir, moveToPattern, frequency);
        try {
            readDirectory = FileUtils.openDirectory(readDir);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void poll() {
        try {
            File file = this.listSingleFile();
            if (file != null) {
                this.processFile(file);
            }
        } catch (Exception e) {
            this.handleException(e);
        }
    }

    private File listSingleFile() throws MuleException {
        try {
            File[] todoFiles = readDirectory.listFiles();
            File oldestFile = null;
            for (int i = 0; i < todoFiles.length; i++) {
                if (oldestFile == null) {
                    oldestFile = todoFiles[i];
                }
                if (todoFiles[i].lastModified() < oldestFile.lastModified()) {
                    oldestFile = todoFiles[i];
                }
            }
            return oldestFile;
        } catch (Exception e) {
            throw new MuleException(FileMessages.errorWhileListingFiles(), e);
        }
    }
}
