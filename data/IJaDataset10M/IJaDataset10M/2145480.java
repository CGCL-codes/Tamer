package net.sf.ideoreport.common.config.alerters;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

/**
 * Contient une d�finition de plage au sein d'un alerteur.
 * 
 * @author jbeausseron
 */
public class RangeDefinition implements IRangeDefinition {

    /**
	 * Logger for this class
	 */
    private static final Logger LOGGER = Logger.getLogger(RangeDefinition.class);

    /**
	 * identifiant de l'alerteur
	 */
    private String id;

    /**
	 * Alerteur qui contient cette plage
	 */
    private IAlerterDefinition alerterConfig;

    /**
	 * Valeur (pour les alerteurs de type STRING)
	 */
    private String value;

    /**
	 * valeur minimale (exclue)
	 */
    private Double minExcluded;

    /**
	 * valeur minimale (incluse)
	 */
    private Double minIncluded;

    /**
	 * valeur maximale (exclue)
	 */
    private Double maxExcluded;

    /**
	 * valeur maximale (incluse)
	 */
    private Double maxIncluded;

    /**
	 * constructeur par d�faut
	 */
    public RangeDefinition() {
        super();
    }

    /**
	 * @see net.sf.ideoreport.common.config.alerters.IRangeDefinition#matchValue(java.lang.Object)
	 */
    public boolean matchValue(Object pValue) {
        boolean vRetour = false;
        if (!StringUtils.isEmpty(this.value)) {
            vRetour = this.value.equals(pValue);
        } else {
            boolean vRangeMatch = true;
            try {
                double vValueAsDouble = Double.parseDouble(String.valueOf(pValue));
                if (this.minExcluded != null) {
                    vRangeMatch = vRangeMatch && (vValueAsDouble > this.minExcluded.doubleValue());
                } else if (this.minIncluded != null) {
                    vRangeMatch = vRangeMatch && (vValueAsDouble >= this.minIncluded.doubleValue());
                }
                if (this.maxExcluded != null) {
                    vRangeMatch = vRangeMatch && (vValueAsDouble < this.maxExcluded.doubleValue());
                } else if (this.maxIncluded != null) {
                    vRangeMatch = vRangeMatch && (vValueAsDouble <= this.maxIncluded.doubleValue());
                }
                vRetour = vRangeMatch;
            } catch (Exception e) {
                LOGGER.warn("unable to convert " + pValue + " to a number value");
            }
        }
        return vRetour;
    }

    /**
	 * @see net.sf.ideoreport.common.config.alerters.IRangeDefinition#getAlerterConfig()
	 */
    public IAlerterDefinition getAlerterConfig() {
        return alerterConfig;
    }

    /**
	 * @see net.sf.ideoreport.common.config.alerters.IRangeDefinition#setAlerterConfig(net.sf.ideoreport.common.config.alerters.IAlerterDefinition)
	 */
    public void setAlerterConfig(IAlerterDefinition pAlerterConfig) {
        alerterConfig = pAlerterConfig;
    }

    /**
	 * @see net.sf.ideoreport.common.config.alerters.IRangeDefinition#getId()
	 */
    public String getId() {
        return id;
    }

    /**
	 * @see net.sf.ideoreport.common.config.alerters.IRangeDefinition#setId(java.lang.String)
	 */
    public void setId(String pId) {
        id = pId;
    }

    /**
	 * @see net.sf.ideoreport.common.config.alerters.IRangeDefinition#getMaxExcluded()
	 */
    public Double getMaxExcluded() {
        return maxExcluded;
    }

    /**
	 * @see net.sf.ideoreport.common.config.alerters.IRangeDefinition#setMaxExcluded(java.lang.Double)
	 */
    public void setMaxExcluded(Double pMaxExcluded) {
        maxExcluded = pMaxExcluded;
    }

    /**
	 * @see net.sf.ideoreport.common.config.alerters.IRangeDefinition#getMaxIncluded()
	 */
    public Double getMaxIncluded() {
        return maxIncluded;
    }

    /**
	 * @see net.sf.ideoreport.common.config.alerters.IRangeDefinition#setMaxIncluded(java.lang.Double)
	 */
    public void setMaxIncluded(Double pMaxIncluded) {
        maxIncluded = pMaxIncluded;
    }

    /**
	 * @see net.sf.ideoreport.common.config.alerters.IRangeDefinition#getMinExcluded()
	 */
    public Double getMinExcluded() {
        return minExcluded;
    }

    /**
	 * @see net.sf.ideoreport.common.config.alerters.IRangeDefinition#setMinExcluded(java.lang.Double)
	 */
    public void setMinExcluded(Double pMinExcluded) {
        minExcluded = pMinExcluded;
    }

    /**
	 * @see net.sf.ideoreport.common.config.alerters.IRangeDefinition#getMinIncluded()
	 */
    public Double getMinIncluded() {
        return minIncluded;
    }

    /**
	 * @see net.sf.ideoreport.common.config.alerters.IRangeDefinition#setMinIncluded(java.lang.Double)
	 */
    public void setMinIncluded(Double pMinIncluded) {
        minIncluded = pMinIncluded;
    }

    /**
	 * @see net.sf.ideoreport.common.config.alerters.IRangeDefinition#getValue()
	 */
    public String getValue() {
        return value;
    }

    /**
	 * @see net.sf.ideoreport.common.config.alerters.IRangeDefinition#setValue(java.lang.String)
	 */
    public void setValue(String pValue) {
        value = pValue;
    }
}
