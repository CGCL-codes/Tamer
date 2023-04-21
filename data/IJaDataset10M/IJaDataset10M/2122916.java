package de.banh.bibo.model.provider.postgresql;

import de.banh.bibo.model.Benutzerinfo;
import de.banh.bibo.model.Exemplar;
import de.banh.bibo.model.Medium;
import de.banh.bibo.util.AbstractBeanTest;

/**
 * 
 * @author Thomas
 */
public class PgVerleihinfoTest extends AbstractBeanTest {

    @Override
    protected Object getTestInstance() {
        PgVerleihinfo vinfo = new PgVerleihinfo();
        Medium medium = new PgMedium();
        Exemplar exemplar = new PgExemplar(medium);
        Benutzerinfo benutzer = new PgBenutzerinfo();
        vinfo.setExemplar(exemplar);
        vinfo.setAusleiher(benutzer);
        return vinfo;
    }
}
