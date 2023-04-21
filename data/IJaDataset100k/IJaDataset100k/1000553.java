package de.searchworkorange.indexcrawler.crawler.statistic.statusserver;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import org.apache.log4j.Level;
import de.searchworkorange.indexcrawler.NetworkException;
import de.searchworkorange.indexcrawler.SocketAction;
import de.searchworkorange.indexcrawler.SocketActionTimer;
import de.searchworkorange.indexcrawler.configuration.ConfigurationCollection;
import de.searchworkorange.indexcrawler.crawler.statistic.Report;
import de.searchworkorange.indexcrawler.crawler.statistic.StatisticModel;
import de.searchworkorange.lib.logger.LoggerCollection;
import de.searchworkorange.lib.timer.WrongConfigurationException;

/**
 * 
 * @author Sascha Kriegesmann kriegesmann at vaxnet.de
 */
public class StatusServerListener extends SocketAction implements IStatusServerListener {

    private static final boolean CLASSDEBUG = false;

    public StatusServerListener(LoggerCollection loggerCol, ConfigurationCollection config, String name, ServerSocket servSocket, StatisticModel statsModel) throws NetworkException, WrongConfigurationException {
        super(loggerCol, config, name, servSocket, statsModel);
        timer.pause();
    }

    /**
     * 
     * @return Socket
     */
    @Override
    public Socket getClientSocket() {
        return super.clientSocket;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public SocketActionTimer getTimer() {
        return timer;
    }

    @Override
    public void statusServerFired(Report report) {
        try {
            timer.goOn();
            send(report.toXMLString(), true);
            timer.pause();
        } catch (NetworkException ex) {
            loggerCol.logException(CLASSDEBUG, this.getClass().getName(), Level.FATAL, ex);
        }
    }

    @Override
    public void offline() {
        offline = true;
        if (in != null) {
            try {
                in.close();
            } catch (IOException ex) {
                loggerCol.logException(CLASSDEBUG, this.getClass().getName(), Level.FATAL, ex);
            }
        }
        if (out != null) {
            out.close();
        }
        if (clientSocket != null) {
            try {
                clientSocket.close();
            } catch (IOException ex) {
                loggerCol.logException(CLASSDEBUG, this.getClass().getName(), Level.FATAL, ex);
            }
        }
    }
}
