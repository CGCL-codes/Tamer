package bfpl.io;

import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import base.exception.TechnicalException;
import bfpl.bean.Bildfahrplan;

public class BfplLoader {

    public BfplLoader(final File f) {
        setProjekt(f);
        configurers.add(new ZuglaufstellenConfigurer());
        configurers.add(new ZuglaufConfigurer());
    }

    public void load() throws TechnicalException {
        setResult(new Bildfahrplan());
        setProperties(new Properties());
        try {
            properties.load(new FileReader(getProjekt()));
        } catch (Exception e) {
            throw new TechnicalException("Fehler beim einlesen der Konfiguration. " + getProjekt().getAbsolutePath(), e);
        }
        for (Configurer conf : configurers) {
            conf.configure(result, properties);
        }
    }

    protected File getProjekt() {
        return projekt;
    }

    protected void setProjekt(final File projekt) {
        this.projekt = projekt;
    }

    public Bildfahrplan getResult() {
        return result;
    }

    protected void setResult(final Bildfahrplan result) {
        this.result = result;
    }

    public Properties getProperties() {
        return properties;
    }

    protected void setProperties(final Properties properties) {
        this.properties = properties;
    }

    private File projekt;

    private List<Configurer> configurers = new ArrayList<Configurer>();

    private Bildfahrplan result;

    private Properties properties;
}
