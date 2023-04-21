package org.kalypso.nofdpidss.ui.view.projectsetup.navigation.timeseriesmanager.commands;

import org.eclipse.core.expressions.PropertyTester;
import org.kalypso.ogc.sensor.timeseries.TimeserieConstants;

/**
 * @author Dirk Kuch
 */
public class TSMCommandPropertyTesterW extends PropertyTester {

    /**
   * check if selected observation has an w/q relationship, if yes -> enable view command (return true)
   * 
   * @see org.eclipse.core.expressions.IPropertyTester#test(java.lang.Object, java.lang.String, java.lang.Object[],
   *      java.lang.Object)
   */
    public boolean test(final Object receiver, final String property, final Object[] args, final Object expectedValue) {
        return TSMCommandHelper.hasAxisOfType(TimeserieConstants.TYPE_WATERLEVEL);
    }
}
