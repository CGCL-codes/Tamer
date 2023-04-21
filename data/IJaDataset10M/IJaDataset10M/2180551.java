package it.ilz.hostingjava.scheduler;

import it.ilz.hostingjava.util.*;
import it.ilz.hostingjava.listener.Context;
import java.io.File;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dom4j.Document;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

/**
 * <p>
 * This is a <i>job</i> that collects all applications names inside
 * a desired folder (hopefully: webapps) and creates a simple
 * robot.txt file google compliant.
 * </p>
 *
 * @author Andrea Pagliari
 * @since giugno 2007
 * @version 1.0
 */
public class NewslettersJob implements Job {

    public void execute(JobExecutionContext context) throws JobExecutionException {
        if (flagInizio) {
            inizio();
            flagInizio = false;
        }
        controlli();
    }

    public NewslettersJob() {
        inizio();
    }

    public void controlli() {
        Newsletters newsletter = new Newsletters(periodo, gg, Context.pathapplicazione + File.separatorChar + "newsletters" + File.separatorChar, Context.smtp, from, oggetto);
        newsletter.setHtmemail();
        try {
            newsletter.inserisciCandidati();
            newsletter.inviaNewsletter();
            newsletter.eliminaCandidatiEliminazione();
            newsletter.setUtentiDaEliminare();
            newsletter.eliminaUser();
        } catch (Exception e) {
            logger.error("Invio newsletters " + e.getMessage(), e);
        }
        newsletter = null;
    }

    public void inizio() {
        try {
            SAXReader reader = new SAXReader();
            Document document = reader.read(new File(Context.pathapplicazione + File.separatorChar + "WEB-INF" + File.separatorChar + "classes" + File.separator + "newsletter.xml"));
            Node node = null;
            node = document.selectSingleNode("//root/oggetto");
            oggetto = node.getText();
            node = document.selectSingleNode("//root/from");
            from = node.getText();
            node = document.selectSingleNode("//root/periodo");
            periodo = Integer.parseInt(node.getText());
            node = document.selectSingleNode("//root/gg");
            gg = Integer.parseInt(node.getText());
        } catch (Exception e) {
            logger.error("\n lettura file configurazione " + e.getMessage(), e);
        }
        if (Context.debug > 2) {
            logger.info("\n\n from: " + from + "\noggetto: " + oggetto + "\nperiodo: " + periodo + "\ngg: " + gg);
        }
    }

    private String oggetto;

    private String from;

    private int periodo;

    private int gg;

    private boolean flagInizio = true;

    private static Log logger = LogFactory.getLog(NewslettersJob.class);
}
