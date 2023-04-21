package org.pct4g.data.script;

import org.pct4g.data.BaseProperties;

public class ScriptProperties extends BaseProperties {

    /**
	 * Constructor for DownloadProperties.
	 * @param name
	 */
    public ScriptProperties(String name, String hostname) {
        super(name + "-" + hostname + "-script");
    }
}
