package pl.edu.pw.DVDManiac.cronek.console;

import java.util.List;
import org.apache.commons.mail.EmailException;
import org.apache.log4j.Logger;
import pl.edu.pw.DVDManiac.cronek.CronekManager;
import pl.edu.pw.DVDManiac.cronek.mailing.NewsMailing;
import pl.edu.pw.DVDManiac.hibernate.model.pojo.Account;
import pl.edu.pw.DVDManiac.AccountsManager.AccountsManager;
import pl.edu.pw.DVDManiac.AccountsManager.iAccountsInterface;

public class PenaltyMailingSender {

    /**
	 * @param args
	 */
    public static void main(String[] args) {
        CronekManager.sendPenaltyMailing();
    }
}
