package org.laboratory.investment.service;

import java.io.Serializable;
import org.apache.log4j.Logger;
import org.laboratory.investment.dataUniverse.ContractType;
import org.laboratory.investment.dataUniverse.InputFeederData;
import org.laboratory.investment.dataUniverse.Ticker;
import org.laboratory.investment.locations.IURLFeeder;
import org.laboratory.investment.locations.sites.BolsaMadrid;
import org.laboratory.investment.locations.sites.Google;
import org.laboratory.investment.locations.sites.Yahoo;
import org.laboratory.investment.service.impl.RetrieveCommand;
import org.laboratory.investment.service.impl.UPDATECommand;

/**
 * Clase proveedora de objetos para funcionar. Por ahora sabe crear feeaders y
 * tb commands
 * 
 * @author Juan Miguel Albisu Frieyro
 * 
 */
public class ServiceFactory implements Serializable {

    private static final long serialVersionUID = 1L;

    private static Logger logger = Logger.getLogger(ServiceFactory.class);

    /**
	 * Proveedor de feeaders Permite indicar el sistema de persistencia
	 * 
	 * @param feederString
	 * @param tickerOrStudy
	 * @param tickerName
	 * @return
	 */
    public IURLFeeder getIURLFeeder(Ticker ticker) {
        String provider = ticker.getProvider();
        IURLFeeder feeder = null;
        if (provider.equalsIgnoreCase("BOLSAMADRID")) {
            feeder = new BolsaMadrid(ticker);
        } else if (provider.equalsIgnoreCase("YAHOO")) {
            feeder = new Yahoo(ticker);
        } else if (provider.equalsIgnoreCase("GOOGLE")) {
            feeder = new Google(ticker);
        } else {
            logger.error("FEEDER no encontrado para " + provider + "en ticker " + ticker.getSymbol() + "-" + ticker.getISIN());
        }
        return feeder;
    }

    /**
	 * Proveedor de Commands Por ahora solo conoce en forma de UPDATE y RETRIEVE
	 * 
	 */
    public Command commandFactory(String line) throws Exception {
        return commandFactory(line.split(" "));
    }

    /**
	 * Proveedor de Commands Por ahora solo conoce en forma de UPDATE y RETRIEVE
	 * 
	 */
    public Command commandFactory(String[] line) throws Exception {
        Command c;
        if (line[0].equalsIgnoreCase("UPDATE")) {
            c = new UPDATECommand(line);
        } else if (line[0].equalsIgnoreCase("RETRIEVE")) {
            c = new RetrieveCommand(line);
        } else {
            Exception ex = new IllegalArgumentException("COMANDO no conocido " + line[0]);
            logger.error("Comando no interpretado: " + line[0], ex);
            throw ex;
        }
        return c;
    }

    public Ticker getStockTicker(InputFeederData inf) {
        Ticker t = new Ticker();
        t.setProvider(inf.getProvider());
        t.setSubyacentName(inf.getSubyacentName());
        t.setSymbol(inf.getSymbol());
        t.setISIN(t.getSymbol());
        t.setType(ContractType.STOCK);
        return t;
    }
}
