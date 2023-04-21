package de.icehorsetools.iceoffice.service.print;

import java.io.File;
import org.apache.commons.lang.StringUtils;

/**
 * Abtrakte Basisklasse f�r alle Services die Objekte drucken
 *
 * @author csch
 * @version $Id: AObjectPrintSv.java 324 2009-04-20 21:39:34Z kruegertom $
 */
public abstract class AObjectPrintSv implements IObjectPrintSv {

    protected abstract String getDefaultReport();

    public boolean hasDefaultReport() {
        return StringUtils.isNotBlank(getDefaultReport());
    }

    public File getPdf(Object xAs) {
        return getPdf(xAs, getCheckedDefaultReport());
    }

    public abstract File getPdf(Object xAs, String xReport);

    public void print(Object xAs) {
        print(xAs, getCheckedDefaultReport());
    }

    /**
   * @return den DefaultReport der konkreten Implementierung
   * @throws IllegalArgumentException wenn {@link #hasDefaultReport()} <code>false</code> liefert
   */
    private String getCheckedDefaultReport() {
        String defaultReport = getDefaultReport();
        if (hasDefaultReport()) {
            return defaultReport;
        } else {
            throw new IllegalArgumentException("Concrete Service class " + this.getClass().getName() + " does not provide default report");
        }
    }

    public abstract void print(Object xAs, String xReport);
}
