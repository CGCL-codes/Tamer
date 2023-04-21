package org.jqc;

import org.jqc.comwrapper.ObjectWrapper;

/**
 * TODO Insert class description
 *
 * @author usf02000
 *
 */
public class QcCustomizationUsersGroups extends AbstractQcCollection<String, QcCustomizationUsersGroup, org.jqc.QcProjectConnectedSession> {

    QcCustomizationUsersGroups(final QcProjectConnectedSession context, final ObjectWrapper object) {
        super(context, object, "Groups", "Group");
    }

    @Override
    protected QcCustomizationUsersGroup createWrapper(final ObjectWrapper objectWrapper) {
        return new QcCustomizationUsersGroup(getSession(), objectWrapper);
    }
}
