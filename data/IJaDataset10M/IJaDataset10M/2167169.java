package it.gashale.jacolib.main.configurator.scripts;

import it.gashale.jacolib.core.JacolibError;
import it.gashale.jacolib.main.Jacolib;
import it.gashale.jacolib.main.configurator.Configurator;
import it.gashale.jacolib.main.configurator.Environment;
import java.util.StringTokenizer;

public class JacolibConfigurator extends Configurator {

    public JacolibConfigurator(Environment env) {
        super(env);
    }

    public void configure(Jacolib jacolib) throws JacolibError {
        configure(jacolib, null);
    }

    public void configure(Jacolib jacolib, String conf) throws JacolibError {
        String confs = getValue(new String[] { "InitialConfigurators" }, null);
        StringTokenizer st = new StringTokenizer(confs);
        while (st.hasMoreTokens()) {
            String full_prefix = st.nextToken();
            String[] prefixes = full_prefix.split("\\.");
            if (conf == null) {
                configure_p(jacolib, prefixes, true);
            } else if (conf.equals(full_prefix)) {
                configure_p(jacolib, prefixes, false);
            }
        }
    }

    protected void configure_p(Jacolib jacolib, String[] prefixes, boolean auto) throws JacolibError {
        setPrefixes(prefixes);
        String configurator_class = getValue(new String[] { "Configurator" }, null);
        String autoload = getValue(new String[] { "Autoload" }, "false");
        if (auto && autoload.equals("false")) return;
        if (configurator_class != null) {
            Configurator configurator = newConfigurator(configurator_class, getEnvironment());
            configurator.setPrefixes(getPrefixes());
            configurator.configure(jacolib);
        } else {
            throw new JacolibError("Undefine Configurator.");
        }
    }
}
