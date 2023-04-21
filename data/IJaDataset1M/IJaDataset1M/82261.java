package org.xith3d.behaviors.impl;

import org.xith3d.behaviors.WakeupCriterion;

/*********************************************************************
 * A WakeupCondition extension with a test() method.
 *
 * @version
 *   $Date: 2007-01-31 22:02:10 -0500 (Wed, 31 Jan 2007) $
 * @since
 *   2005-07-14
 * @author
 *   <a href="http://www.croftsoft.com/">David Wallace Croft</a>
 *********************************************************************/
public abstract class WakeupCriterionImpl extends org.xith3d.scenegraph.behavior.WakeupCriterion implements WakeupCriterion {

    private boolean triggered;

    public abstract void test();

    @Override
    public boolean hasTriggered() {
        return triggered;
    }

    public void setTriggered() {
        triggered = true;
    }

    public void reset() {
        triggered = false;
    }
}
