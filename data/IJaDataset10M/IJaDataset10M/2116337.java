package ch.unibe.a3ubAdmin.util.dbadmin;

import java.util.Iterator;
import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import ch.unibe.a3ubAdmin.util.EmailSender;

public class SendLdapData {

    /** Knows where to put the produced scripts. */
    private String basePath = "";

    /** The Log of this class */
    private Log log = LogFactory.getLog(getClass());

    public SendLdapData(String basePath) {
        this.setBasePath(basePath);
    }

    /**
	 * @return Returns the basePath.
	 */
    private String getBasePath() {
        return this.basePath;
    }

    /**
	 * @param basePath
	 *            The basePath to set.
	 */
    private void setBasePath(String basePath) {
        this.basePath = basePath;
    }

    public void sendData() throws Exception {
        DbToLdap exporter = new DbToLdap(this.getBasePath());
        String fileName = "ExportToLdap.dat";
        exporter.exportToLdap(fileName);
        List appIdList = exporter.getAllApplications();
        EmailSender emailSender = EmailSender.getInstance();
        String sender = "petersen@id.unibe.ch";
        String recipient = "geiser@id.unibe.ch";
        String subject = "Neue Daten aus a3ubadmin";
        String text = "Diese Email enthält die neuen Autorisierungsattribute, " + "geändert mit a3ubadmin (automatisch erstellt). " + "Sie sollen in den LDAP importiert werden." + "Bitte zuerst alle Einträge zu folgenden Applikationen löschen:\n";
        Iterator appIdIterator = appIdList.iterator();
        while (appIdIterator.hasNext()) {
            String currentAppId = (String) appIdIterator.next();
            text += "'" + currentAppId + "'\n";
        }
        text += "\nDie SE-Gruppe.";
        emailSender.sendMailWithAttachement(sender, recipient, subject, text, this.getBasePath() + fileName);
        this.log.info("Sent ExportToLdap-file successfully.");
    }
}
