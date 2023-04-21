package net.allblog.leech.leechBot;

import java.io.IOException;
import java.net.MalformedURLException;
import net.allblog.hibernate.HibernateSessionFactory;
import org.apache.log4j.Logger;

public class LeechBotThread extends Thread {

    private static Logger logger = Logger.getLogger(LeechBotThread.class);

    private static LeechBot bot;

    private static int period = 1000 * 60 * 10;

    /**
	 * @see parameter, "ms" must be greater than 10,000
	 * @param ms milliseconds
	 */
    public void setPeriod(int ms) {
        if (ms > 10 * 1000) this.period = ms;
    }

    public void run() {
        while (true) {
            this.bot = new LeechBotImpl();
            try {
                this.bot.crawlling();
            } catch (MalformedURLException e) {
                this.logger.error("Site URL is wrong!", e);
            } catch (IOException e) {
                this.logger.error(e.getLocalizedMessage(), e);
            } catch (NullPointerException e) {
                this.logger.error(e.getLocalizedMessage(), e);
            }
            try {
                HibernateSessionFactory.commitTransaction();
                logger.info("@HIBERNATE FILTER COMMIT!!!! ");
            } finally {
                HibernateSessionFactory.closeSession();
                logger.info("@HIBERNATE FILTER CLOSE!!!! ");
            }
            try {
                this.sleep(this.period);
            } catch (InterruptedException e) {
                this.logger.error(e.getLocalizedMessage(), e);
            }
        }
    }
}
