package persist;

import org.decisiondeck.xmcda_oo.structure.Criterion.PreferenceDirection;
import org.decisiondeck.xmcda_oo.structure.IOrderedInterval;

public class PersistVarious {

    public org.decisiondeck.xmcda_2_0_0.ProjectReference writeComment(String comment) {
        final org.decisiondeck.xmcda_2_0_0.ProjectReference xmlPrRef = org.decisiondeck.xmcda_2_0_0.ProjectReference.Factory.newInstance();
        xmlPrRef.addComment(comment);
        return xmlPrRef;
    }

    public org.decisiondeck.xmcda_2_0_0.PreferenceDirection.Enum getXmlPreferenceDirection(IOrderedInterval interval) {
        final PreferenceDirection preferenceDirection = interval.getPreferenceDirection();
        return getXmlPreferenceDirection(preferenceDirection);
    }

    /**
     * @param preferenceDirection
     *            may be <code>null</code>.
     * @return <code>null</code> iff <code>null</code> or unknown.
     */
    public org.decisiondeck.xmcda_2_0_0.PreferenceDirection.Enum getXmlPreferenceDirection(final PreferenceDirection preferenceDirection) {
        if (preferenceDirection == null) {
            return null;
        }
        switch(preferenceDirection) {
            case MAXIMIZE:
                return org.decisiondeck.xmcda_2_0_0.PreferenceDirection.MAX;
            case MINIMIZE:
                return org.decisiondeck.xmcda_2_0_0.PreferenceDirection.MIN;
            default:
                return null;
        }
    }
}
