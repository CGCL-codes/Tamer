package org.corrib.jonto.udc;

import java.io.IOException;
import java.io.InputStream;
import org.corrib.jonto.TaxonomyContext;
import org.corrib.jonto.TaxonomyEntry;
import org.corrib.jonto.UnsupportedAdditionalPropertyException;
import org.corrib.jonto.beans.SmartProperties;
import org.corrib.jonto.beans.impl.RdfTaxonomyContext;
import org.corrib.jonto.db.rdf.RdfTaxonomyFactory;
import org.corrib.jonto.db.rdf.TaxonomyStorage;

/**
 * Taxonomy Context for DMoz [Singleton implementation]
 * 
 * @author skruk
 *
 */
public class UdcContext extends RdfTaxonomyContext {

    protected static TaxonomyContext<TaxonomyEntry> context = null;

    /**
	 * @param _label
	 * @param _description
	 * @param _namespaceURI
	 * @param _namespaceAbbr
	 * @param _isDecimal
	 */
    protected UdcContext(String _label, String _description, String _namespaceURI, String _namespaceAbbr) {
        super(_label, _description, _namespaceURI, _namespaceAbbr, TaxonomyStorage.getInstance("jonto-dmoz"));
    }

    /**
	 * Returns singleton object of DDC Context
	 * 
	 * @return
	 * @throws IOException 
	 */
    public static TaxonomyContext<TaxonomyEntry> getContext() throws IOException {
        synchronized (UdcContext.class) {
            if (context == null) {
                InputStream is = UdcContext.class.getClassLoader().getResourceAsStream("udc.properties");
                SmartProperties prop = new SmartProperties();
                prop.load(is);
                is.close();
                context = RdfTaxonomyFactory.getInstance().readTaxonomy(prop, UdcContext.class.getClassLoader(), "jonto-dmoz");
            }
        }
        return context;
    }

    /**
	 * testing ... 
	 * 
	 * @param args
	 * @throws UnsupportedAdditionalPropertyException 
	 */
    public static void main(String[] args) throws UnsupportedAdditionalPropertyException {
        try {
            TaxonomyContext<TaxonomyEntry> cntx = UdcContext.getContext();
            for (TaxonomyEntry entry : cntx.listRoots()) {
                System.out.println("[ROOT]\t" + entry.getLabel());
                entry.getSubEntries();
                for (TaxonomyEntry child : entry.getSubEntries()) {
                    System.out.println("[CHILD]\t" + child.getLabel());
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
