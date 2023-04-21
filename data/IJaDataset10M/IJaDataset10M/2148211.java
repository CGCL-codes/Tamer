package net.sf.nazgaroth.game.cfg;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import org.apache.commons.digester.Digester;
import org.apache.log4j.Logger;
import org.xml.sax.SAXException;

public abstract class AbstractConfiguration<T> {

    private Logger log = Logger.getLogger(this.getClass());

    private static final String CONF_DIR = "cfg/";

    private Digester digester;

    private Class<T> clazz;

    public AbstractConfiguration(Class<T> clazz) {
        digester = new Digester();
        this.clazz = clazz;
    }

    protected abstract void registerRules(Digester digester);

    public T read(String file) {
        registerRules(digester);
        try {
            return clazz.cast(digester.parse(new FileInputStream(CONF_DIR + file)));
        } catch (FileNotFoundException e) {
            log.error(e.getLocalizedMessage());
        } catch (IOException e) {
            log.error(e.getLocalizedMessage());
        } catch (SAXException e) {
            log.error(e.getLocalizedMessage());
        }
        try {
            return clazz.newInstance();
        } catch (InstantiationException e) {
            log.error(e.getLocalizedMessage());
        } catch (IllegalAccessException e) {
            log.error(e.getLocalizedMessage());
        }
        return null;
    }
}
