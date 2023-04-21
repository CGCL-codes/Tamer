package org.jowidgets.api.layout.miglayout;

import org.jowidgets.api.layout.ILayoutFactory;
import org.jowidgets.common.widgets.layout.MigLayoutDescriptor;

public interface IMigLayoutFactoryBuilder {

    IMigLayoutFactoryBuilder descriptor(MigLayoutDescriptor descriptor);

    IMigLayoutFactoryBuilder rowConstraints(String constraints);

    IMigLayoutFactoryBuilder columnConstraints(String constraints);

    IMigLayoutFactoryBuilder constraints(String constraints);

    IMigLayoutFactoryBuilder rowConstraints(IAC constraints);

    IMigLayoutFactoryBuilder columnConstraints(IAC constraints);

    IMigLayoutFactoryBuilder constraints(ILC constraints);

    ILayoutFactory<IMigLayout> build();
}
