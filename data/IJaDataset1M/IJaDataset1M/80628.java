package org.jowidgets.examples.common;

import org.jowidgets.api.toolkit.Toolkit;
import org.jowidgets.api.widgets.IContainer;
import org.jowidgets.api.widgets.IFrame;
import org.jowidgets.api.widgets.IInputField;
import org.jowidgets.api.widgets.blueprint.IFrameBluePrint;
import org.jowidgets.common.application.IApplication;
import org.jowidgets.common.application.IApplicationLifecycle;
import org.jowidgets.common.types.Dimension;
import org.jowidgets.common.widgets.layout.MigLayoutDescriptor;
import org.jowidgets.examples.common.icons.DemoIconsInitializer;
import org.jowidgets.tools.layout.MigLayoutFactory;
import org.jowidgets.tools.widgets.blueprint.BPF;

public class TempDemoApplication implements IApplication {

    public void start() {
        Toolkit.getApplicationRunner().run(this);
    }

    @Override
    public void start(final IApplicationLifecycle lifecycle) {
        DemoIconsInitializer.initialize();
        final IFrameBluePrint frameBp = BPF.frame().setTitle("Temp");
        frameBp.setSize(new Dimension(800, 600));
        final IFrame rootFrame = Toolkit.createRootFrame(frameBp, lifecycle);
        rootFrame.setLayout(MigLayoutFactory.growingInnerCellLayout());
        final IContainer composite = rootFrame.add(BPF.scrollComposite(), MigLayoutFactory.GROWING_CELL_CONSTRAINTS);
        composite.setLayout(new MigLayoutDescriptor("[][grow, 0::][]", "[]"));
        for (int i = 0; i < 20; i++) {
            composite.add(BPF.textLabel("Testlabel of the control " + i));
            final IInputField<Short> inputField = composite.add(BPF.inputFieldShortNumber(), "growx, w 0::");
            composite.add(BPF.inputComponentValidationLabel(inputField).setShowValidationMessage(false), "w 20!, wrap");
        }
        rootFrame.setVisible(true);
    }
}
