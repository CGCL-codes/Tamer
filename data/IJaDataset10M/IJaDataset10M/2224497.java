package org.ensembl.compara.test;

import java.util.logging.Logger;
import org.ensembl.compara.driver.ComparaDriver;
import org.ensembl.test.Base;

/**
 * Base for compara tests configures the logging system and loads the
 * registry driver in it's constructor.
 * 
 * The registry is configured from a config file.
 * The system uses $HOME/.ensembl/compara_unit_test.pl if it exists otherwise
 * it uses the default config file resource/data/compara_unit_test.pl.
 */
public abstract class ComparaBase extends Base {

    protected ComparaDriver comparaDriver = null;

    private static Logger logger = Logger.getLogger(ComparaBase.class.getName());

    public ComparaBase(String name) throws Exception {
        super(name);
    }

    protected void setUp() throws Exception {
        super.setUp();
        comparaDriver = registry.getGroup("compara").getComparaDriver();
    }
}
