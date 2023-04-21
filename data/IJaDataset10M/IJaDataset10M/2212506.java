package joelib.desc.types;

import org.apache.log4j.Category;
import joelib.desc.DescriptorHelper;
import joelib.desc.DescriptorInfo;
import joelib.desc.AbstractSMARTSCounter;

/**
 * Number of aromatic hydroxy groups.
 *
 * @author     wegnerj
 * @license GPL
 * @cvsversion    $Revision: 1.11 $, $Date: 2004/09/24 15:06:15 $
 */
public class AromaticOHGroups extends AbstractSMARTSCounter {

    /**
     *  Obtain a suitable logger.
     */
    private static Category logger = Category.getInstance("joelib.desc.types.AromaticOHGroups");

    public static final String DEFAULT = "[OX2;H1;$(O-a)]";

    public static final String DESC_KEY = "Number_of_aromatic_OH_groups";

    public AromaticOHGroups() {
        if (logger.isDebugEnabled()) {
            logger.debug("Initialize " + this.getClass().getName() + "with SMARTS pattern: " + DEFAULT);
        }
        descInfo = DescriptorHelper.generateDescInfo(DESC_KEY, this.getClass(), DescriptorInfo.TYPE_NO_COORDINATES, "joelib.desc.StringInit", "joelib.desc.result.IntResult");
    }

    public String getDefaultSMARTS() {
        return DEFAULT;
    }
}
