package org.laboratory.investment.service.impl;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.Collection;
import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import org.laboratory.investment.config.LaboratoryContext;
import org.laboratory.investment.config.LaboratoryContext.BEANS;
import org.laboratory.investment.dataUniverse.Quote;
import org.laboratory.investment.dataUniverse.Ticker;
import org.laboratory.investment.dataUniverse.Universe;
import org.laboratory.investment.dataUniverse.UniverseProvider;
import org.laboratory.investment.service.QuotesPersistanceService;

/**
 * 
 * Busca todos los universos de tipo Quote y los exporta o importa
 * Directorio->JDO JDO->Directorio En el directorio espera tener ficheros
 * nombredelticker.data
 * 
 * @author Juan Miguel Albisu Frieyro
 * 
 */
public class ExportImportQuotesFiles {

    File directory;

    public ExportImportQuotesFiles(File directory) {
        this.directory = directory;
        if (!directory.isDirectory()) this.directory = directory.getParentFile();
    }

    public void importFromFiles() throws IOException {
        FilenameFilter ff = new FilenameFilter() {

            @Override
            public boolean accept(File dir, String name) {
                if (name.endsWith(".data")) return true;
                return false;
            }
        };
        File[] files = directory.listFiles(ff);
        for (File f : files) {
            String tickername = f.getName().split("\\.")[0];
            QuotesPersistanceService fqps = new FileQuotesPersistanceImpl(f, tickername);
            Ticker t = new Ticker();
            t.setSymbol(tickername);
            JDOQuotesPersistanceService jdoqps = new JDOQuotesPersistanceService(t);
            Collection<Quote> q = fqps.getQuotes();
            jdoqps.saveQuotesDirty(q);
        }
    }

    /**
	 * Exporta los objetos Quote de los universos de tipo Quotes a ticker.data
	 * Cuidado: pisa el fichero en caso de existir
	 * 
	 * @throws IOException
	 */
    @SuppressWarnings("unchecked")
    public void exportToFiles() throws IOException {
        UniverseProvider up = LaboratoryContext.getBean(BEANS.UNIVERSEPROVIDER);
        PersistenceManager pm = up.getUniversePersistenceManager();
        Query q = pm.newQuery(Universe.class);
        q.setFilter("type==\"" + Quote.class.getName() + "\"");
        Collection<Universe> universes = (Collection<Universe>) q.execute();
        for (Universe u : universes) {
            QuotesPersistanceService jdoqps = new JDOQuotesPersistanceService(u);
            QuotesPersistanceService fqps = new FileQuotesPersistanceImpl(new File(directory, u.getName() + ".data"), u.getName());
            Collection<Quote> quotes = jdoqps.getQuotes();
            fqps.saveQuotes(quotes);
        }
    }
}
