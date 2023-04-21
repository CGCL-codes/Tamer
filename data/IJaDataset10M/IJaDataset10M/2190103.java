package org.jowidgets.api.test.blueprint;

import org.jowidgets.api.test.blueprint.builder.IHierarchy2nd3SetupBuilder;
import org.jowidgets.api.test.blueprint.defaults.Hierarchy2nd3AnnotationBasedDefaults;
import org.jowidgets.api.test.blueprint.descriptor.IHierarchy2nd3Descriptor;
import org.jowidgets.api.widgets.blueprint.defaults.anotations.DefaultsInitializer;

@DefaultsInitializer(Hierarchy2nd3AnnotationBasedDefaults.class)
public interface IHierarchy2nd3BluePrint extends IHierarchy2nd3SetupBuilder<IHierarchy2nd3BluePrint>, IHierarchy2nd3Descriptor {
}
