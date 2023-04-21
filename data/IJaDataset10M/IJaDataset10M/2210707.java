package de.admedia.util;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import de.admedia.pdfartikelverwaltung.PDFArtikelverwaltung;
import de.admedia.realeartikelverwaltung.RealeArtikelverwaltung;
import de.admedia.userverwaltung.Userverwaltung;
import de.admedia.virtuellartikelverwaltung.VirtuellArtikelverwaltung;

/**
 * 
 * @author Peter Fast
 *
 */
public class EjbUtil {

    private static final Log LOG = LogFactory.getLog(EjbUtil.class);

    private static final String REALEARTIKELVERWALTUNG_LOCAL = "Admedia/RealeArtikelverwaltungBean/local";

    private static final String VIRTUELLARTIKELVERWALTUNG_LOCAL = "Admedia/VirtuellArtikelverwaltungBean/local";

    private static final String USERVERWALTUNG_LOCAL = "Admedia/UserverwaltungBean/local";

    private static final String PDFARTIKELVERWALTUNG_LOCAL = "Admedia/PDFVerwaltungBean/local";

    public static Userverwaltung getUserverwaltungLocal() throws EjbNotFoundException {
        Userverwaltung uv = null;
        try {
            final Context ctx = new InitialContext();
            uv = (Userverwaltung) ctx.lookup(USERVERWALTUNG_LOCAL);
            ctx.close();
        } catch (NamingException e) {
            LOG.error("Kein SessionBean mit Namen " + USERVERWALTUNG_LOCAL);
            throw new EjbNotFoundException(USERVERWALTUNG_LOCAL);
        }
        return uv;
    }

    public static RealeArtikelverwaltung getRealeArtikelverwaltungLocal() throws EjbNotFoundException {
        RealeArtikelverwaltung rv = null;
        Context ctx;
        try {
            ctx = new InitialContext();
            rv = (RealeArtikelverwaltung) ctx.lookup(REALEARTIKELVERWALTUNG_LOCAL);
            ctx.close();
        } catch (NamingException e) {
            LOG.error("Kein SessionBean mit Namen " + REALEARTIKELVERWALTUNG_LOCAL);
            throw new EjbNotFoundException(REALEARTIKELVERWALTUNG_LOCAL);
        }
        return rv;
    }

    public static VirtuellArtikelverwaltung getVirtuellArtikelverwaltungLocal() throws EjbNotFoundException {
        VirtuellArtikelverwaltung vv = null;
        Context ctx;
        try {
            ctx = new InitialContext();
            vv = (VirtuellArtikelverwaltung) ctx.lookup(VIRTUELLARTIKELVERWALTUNG_LOCAL);
            ctx.close();
        } catch (NamingException e) {
            LOG.error("Kein SessionBean mit Namen " + VIRTUELLARTIKELVERWALTUNG_LOCAL);
            throw new EjbNotFoundException(VIRTUELLARTIKELVERWALTUNG_LOCAL);
        }
        return vv;
    }

    public static PDFArtikelverwaltung getPDFArtikelverwaltungLocal() throws EjbNotFoundException {
        PDFArtikelverwaltung pv = null;
        Context ctx;
        try {
            ctx = new InitialContext();
            pv = (PDFArtikelverwaltung) ctx.lookup(PDFARTIKELVERWALTUNG_LOCAL);
            ctx.close();
        } catch (NamingException e) {
            LOG.error("Kein SessionBean mit Namen " + PDFARTIKELVERWALTUNG_LOCAL);
            throw new EjbNotFoundException(PDFARTIKELVERWALTUNG_LOCAL);
        }
        return pv;
    }
}
