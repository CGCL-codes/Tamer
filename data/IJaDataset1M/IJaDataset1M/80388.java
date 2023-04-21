package org.columba.mail.folder.mailboximport;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import org.columba.api.command.IWorkerStatusController;
import org.columba.mail.folder.IMailbox;
import org.columba.mail.util.MailResourceLoader;

/**
 * @version 1.0
 * @author
 */
public class PegasusMailImporter extends AbstractMailboxImporter {

    public PegasusMailImporter() {
        super();
    }

    public PegasusMailImporter(IMailbox destinationFolder, File[] sourceFiles) {
        super(destinationFolder, sourceFiles);
    }

    public int getType() {
        return TYPE_FILE;
    }

    public void importMailboxFile(File file, IWorkerStatusController worker, IMailbox destFolder) throws Exception {
        int count = 0;
        boolean sucess = false;
        StringBuffer strbuf = new StringBuffer();
        BufferedReader in = new BufferedReader(new FileReader(file));
        String str;
        while ((str = in.readLine()) != null) {
            if (worker.cancelled() == true) {
                return;
            }
            if ((str.startsWith("From ???@???") == false) || (str.length() == 0)) {
                strbuf.append(str + "\n");
            } else {
                if (strbuf.length() != 0) {
                    saveMessage(strbuf.toString(), worker, getDestinationFolder());
                    count++;
                    sucess = true;
                }
                strbuf = new StringBuffer();
            }
        }
        if ((sucess == true) && (strbuf.length() > 0)) {
            saveMessage(strbuf.toString(), worker, getDestinationFolder());
        }
        in.close();
    }

    public String getDescription() {
        return MailResourceLoader.getString("dialog", "mailboximport", "PegasusMail_description");
    }
}
