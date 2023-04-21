package tc;

import java.io.File;
import java.util.List;
import java.util.Locale;
import org.apache.log4j.xml.DOMConfigurator;
import tc.config.Configuration;
import tc.config.Language;
import tc.model.Tournament;
import tc.model.TournamentManager;

/**
 * @author bleras
 *
 */
public class Launcher {

    /**
	 * @param args
	 */
    public static void main(String[] args) {
        DOMConfigurator.configure("conf" + File.separator + "log.cfg.xml");
        Configuration config = Configuration.getInstance();
        Language lang = Language.getInstance(new Locale((String) config.getProperty("Language")));
        TournamentManager tournamentManager = TournamentManager.getInstance();
        List<Tournament> tournamentList = tournamentManager.listTournaments();
        for (Tournament tournament : tournamentList) {
            System.out.println(tournament.getTournamentName());
        }
    }
}
