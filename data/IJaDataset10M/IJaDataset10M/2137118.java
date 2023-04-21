package org.dmd.dmv.server.generated.dmw;

import java.util.Iterator;
import org.dmd.dmw.DmwObjectIterator;
import org.dmd.dmv.shared.generated.dmo.MayRuleDMO;
import org.dmd.dmv.server.extended.MayRule;

/**
 * The MayRuleIterableDMW wraps an Iterator for a particular type and makes 
 * it Iterable.
 * <P>
 * This code was auto-generated and shouldn't be altered manually!
 * Generated from: org.dmd.dms.util.GenUtility.dumpObjectIterable(GenUtility.java:1672)
 *    Called from: org.dmd.dmg.generators.DMWGenerator.createTypeIterables(DMWGenerator.java:78)
 */
public class MayRuleIterableDMW extends DmwObjectIterator<MayRule, MayRuleDMO> {

    public static final MayRuleIterableDMW emptyList = new MayRuleIterableDMW();

    protected MayRuleIterableDMW() {
        super();
    }

    public MayRuleIterableDMW(Iterator<MayRuleDMO> it) {
        super(it);
    }
}
