package ar.com.rab.beancipher.impl.configuration.model.beanmapping;

import java.util.List;

/**
 * Part of configuration model mapped on Castor XML
 * 
 * @author Bajales Raul
 *
 */
public class CFGBeanMapping {

    private String defaultPropertyCipher;

    private List mappings;

    /**
     * Constructor
     */
    public CFGBeanMapping() {
        super();
    }

    /**
     * @return Returns the defaultPropertyCipher.
     */
    public String getDefaultPropertyCipher() {
        return defaultPropertyCipher;
    }

    /**
     * @param defaultPropertyCipher The defaultPropertyCipher to set.
     */
    public void setDefaultPropertyCipher(String defaultPropertyCipher) {
        this.defaultPropertyCipher = defaultPropertyCipher;
    }

    /**
     * @return Returns the mappings.
     */
    public List getMappings() {
        return mappings;
    }

    /**
     * @param mappings The mappings to set.
     */
    public void setMappings(List mappings) {
        this.mappings = mappings;
    }
}
