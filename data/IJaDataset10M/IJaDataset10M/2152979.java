package backupit.core.io;

import backupit.core.dirbackup.DirBackup;
import backupit.core.dirbackup.io.DirBackupWriter;
import backupit.core.mailbackup.MailBackup;
import backupit.core.mailbackup.io.MailBackupWriter;
import java.io.IOException;

/**
 *
 * @author dbotelho
 */
public class BackupWriterFactory {

    public static DirBackupWriter getInstance(DirBackup backup) throws IOException {
        return new DirBackupWriter(backup);
    }

    public static MailBackupWriter getInstance(MailBackup backup) throws IOException {
        return new MailBackupWriter(backup);
    }
}
