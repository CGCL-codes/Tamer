package org.jowidgets.impl.widgets.composed.blueprint.defaults.registry;

import org.jowidgets.api.widgets.blueprint.builder.ICollectionInputControlSetupBuilder;
import org.jowidgets.api.widgets.blueprint.builder.ICollectionInputDialogSetupBuilder;
import org.jowidgets.api.widgets.blueprint.builder.ICollectionInputFieldSetupBuilder;
import org.jowidgets.api.widgets.blueprint.builder.IInputComponentValidationLabelSetupBuilder;
import org.jowidgets.api.widgets.blueprint.builder.IInputCompositeSetupBuilder;
import org.jowidgets.api.widgets.blueprint.builder.IInputDialogSetupBuilder;
import org.jowidgets.api.widgets.blueprint.builder.IInputFieldSetupBuilder;
import org.jowidgets.api.widgets.blueprint.builder.ILoginDialogSetupBuilder;
import org.jowidgets.api.widgets.blueprint.builder.IMessageDialogSetupBuilder;
import org.jowidgets.api.widgets.blueprint.builder.IProgressBarSetupBuilder;
import org.jowidgets.api.widgets.blueprint.builder.IQuestionDialogSetupBuilder;
import org.jowidgets.api.widgets.blueprint.builder.IValidationLabelSetupBuilder;
import org.jowidgets.impl.widgets.basic.blueprint.defaults.registry.BasicDefaultsInitializerRegistry;
import org.jowidgets.impl.widgets.composed.blueprint.defaults.CollectionInputControlDefaults;
import org.jowidgets.impl.widgets.composed.blueprint.defaults.CollectionInputDialogDefaults;
import org.jowidgets.impl.widgets.composed.blueprint.defaults.CollectionInputFieldDefaults;
import org.jowidgets.impl.widgets.composed.blueprint.defaults.InputComponentValidationLabelDefaults;
import org.jowidgets.impl.widgets.composed.blueprint.defaults.InputCompositeDefaults;
import org.jowidgets.impl.widgets.composed.blueprint.defaults.InputDialogDefaults;
import org.jowidgets.impl.widgets.composed.blueprint.defaults.InputFieldDefaults;
import org.jowidgets.impl.widgets.composed.blueprint.defaults.LoginDialogDefaults;
import org.jowidgets.impl.widgets.composed.blueprint.defaults.MessageDialogDefaults;
import org.jowidgets.impl.widgets.composed.blueprint.defaults.ProgressBarDefaults;
import org.jowidgets.impl.widgets.composed.blueprint.defaults.QuestionDialogDefaults;
import org.jowidgets.impl.widgets.composed.blueprint.defaults.ValidationLabelDefaults;

public class ComposedDefaultsInitializerRegistry extends BasicDefaultsInitializerRegistry {

    public ComposedDefaultsInitializerRegistry() {
        super();
        register(IInputCompositeSetupBuilder.class, new InputCompositeDefaults());
        register(ICollectionInputControlSetupBuilder.class, new CollectionInputControlDefaults());
        register(IMessageDialogSetupBuilder.class, new MessageDialogDefaults());
        register(IQuestionDialogSetupBuilder.class, new QuestionDialogDefaults());
        register(IProgressBarSetupBuilder.class, new ProgressBarDefaults());
        register(IInputDialogSetupBuilder.class, new InputDialogDefaults());
        register(ILoginDialogSetupBuilder.class, new LoginDialogDefaults());
        register(IInputFieldSetupBuilder.class, new InputFieldDefaults());
        register(IValidationLabelSetupBuilder.class, new ValidationLabelDefaults());
        register(IInputComponentValidationLabelSetupBuilder.class, new InputComponentValidationLabelDefaults());
        register(ICollectionInputDialogSetupBuilder.class, new CollectionInputDialogDefaults());
        register(ICollectionInputFieldSetupBuilder.class, new CollectionInputFieldDefaults());
    }
}
