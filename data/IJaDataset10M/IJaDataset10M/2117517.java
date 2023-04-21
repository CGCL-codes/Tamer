package org.equanda.tapestry.components.userAdmin.configItem;

import org.equanda.tapestry.components.EquandaBaseComponent;
import org.apache.tapestry.IAsset;
import org.apache.tapestry.annotations.Asset;
import org.apache.tapestry.annotations.ComponentClass;
import org.apache.tapestry.annotations.Parameter;

/**
 * Description!!!
 *
 * @author <a href="mailto:florin@paragon-software.ro">Florin</a>
 */
@ComponentClass(allowInformalParameters = true, allowBody = true)
public abstract class ConfigItem extends EquandaBaseComponent {

    private org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(getClass());

    /**
     * ********************************* Parameter definitions: ***********************************
     */
    @Parameter(name = "key", required = true)
    public abstract String getKey();

    @Parameter(name = "internationalizedLabel")
    public abstract String getInternationalizedLabel();

    @Parameter(name = "value", required = true)
    public abstract Object getValue();

    public abstract void setValue(Object value);

    public String getItemId() {
        return "ID_" + getKey();
    }

    /**
     * ********************************** Assets definitions: *************************************
     */
    @Asset(value = "/org/equanda/tapestry/components/userAdmin/configItem/ConfigItem.html")
    public abstract IAsset get$template();
}
