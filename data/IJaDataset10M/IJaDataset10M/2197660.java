package net.sourceforge.obexftpfrontend.command;

import java.io.File;
import java.util.List;
import net.sourceforge.obexftpfrontend.model.ConfigurationHolder;
import net.sourceforge.obexftpfrontend.model.OBEXElement;
import net.sourceforge.obexftpfrontend.obexftp.OBEXException;
import net.sourceforge.obexftpfrontend.obexftp.OBEXFTPException;
import org.apache.log4j.Logger;

/**
 * The application creates this command and adds it to the command queue when
 * the user wants to send files to the device.
 * @author Daniel F. Martins
 */
public class SendFilesCommand extends AbstractOBEXFTPCommand<Object> {

    /** Logger. */
    private static final Logger log = Logger.getLogger(SendFilesCommand.class);

    /** Folder to put the new files. */
    private OBEXElement folder;

    /** Files to send. */
    private List<File> files;

    /**
     * Create a new instance of SendFilesCommand.
     * @param configHolder Component that manages the configuration.
     * @param folder Folder that will receive the files.
     * @param files Files to send.
     * @throws IllegalArgumentException if some argument is invalid.
     */
    public SendFilesCommand(ConfigurationHolder configHolder, OBEXElement folder, List<File> files) {
        super(configHolder, "Sending files" + ((folder != null) ? " to the " + folder.getName() + " folder" : ""));
        if (files == null || files.size() == 0) {
            throw new IllegalArgumentException("The files argument cannot be null or empty");
        }
        this.folder = folder;
        this.files = files;
    }

    @Override
    public Object execute() throws OBEXFTPException, OBEXException, InterruptedException {
        boolean hasFiles = false;
        List<String> args = convertPath(folder);
        for (File file : files) {
            Thread.sleep(1);
            if (!file.exists()) {
                continue;
            }
            args.add("--put");
            args.add(file.getAbsolutePath());
            hasFiles = true;
        }
        Thread.sleep(1);
        if (hasFiles) {
            log.info("Trying to upload files to the device");
            runCommand(null, args, true);
        } else {
            log.info("There\'s no valid files to upload to the device");
        }
        return null;
    }
}
