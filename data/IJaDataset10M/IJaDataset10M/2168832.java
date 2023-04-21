package org.jcvi.glk.elvira.plate;

import org.jcvi.glk.SequenceDirection;
import org.jcvi.glk.TrimSequenceAttributeType;
import org.jcvi.glk.Well;
import org.jcvi.glk.ctm.CTMUtil;
import org.jcvi.glk.helpers.GLKHelper;

/**
 * {@code CtmName} is a {@link PrimerNameStrategy}
 * that uses the naming conventions used
 * as part of primer naming in the CTM
 * to tell the Closure Group enough information
 * to generate TIGR Sequence names for the primer
 * pairs.  The CTM Name will be something like
 * '04T48A01HA_1184F_1775R', which includes
 * the region number, the plate name, the Well location,
 * the region name, and the forward and reverse coordinates
 * of the primer pair.
 * @author dkatzel
 *
 *
 */
public class CtmName implements PrimerNameStrategy {

    private final TrimSequenceAttributeType coordinateType;

    private final TrimSequenceAttributeType regionNumberType;

    private final TrimSequenceAttributeType regionType;

    public CtmName(GLKHelper helper) {
        this.coordinateType = helper.getTrimSequenceAttributeType("coordinate");
        this.regionNumberType = helper.getTrimSequenceAttributeType("region_number");
        this.regionType = helper.getTrimSequenceAttributeType("region");
    }

    @Override
    public String getPrimerNameFor(Well well, SequenceDirection dir) {
        return CTMUtil.createCtmTaskNameFrom(well, coordinateType, regionNumberType, regionType);
    }
}
