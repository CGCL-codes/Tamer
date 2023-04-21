package it.jnrpe.server.plugins.xml;

import it.jnrpe.plugins.PluginOption;
import it.jnrpe.server.xml.XMLOption;

public class XMLPluginOption extends XMLOption {

    public PluginOption toPluginOption() {
        return new PluginOption().setArgName(getArgName()).setArgsCount(getArgsCount()).setArgsOptional(getArgsOptional()).setDescription(getDescription()).setHasArgs(hasArgs()).setLongOpt(getLongOpt()).setOption(getOption()).setRequired(getRequired().equalsIgnoreCase("true")).setType(getType()).setValueSeparator(getValueSeparator());
    }
}
