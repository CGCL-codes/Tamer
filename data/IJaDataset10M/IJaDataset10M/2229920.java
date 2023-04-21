package de.jlab.test;

import java.util.Map;
import javax.swing.JComponent;
import de.jlab.external.measurement.model.ExternalModel;
import de.jlab.ui.external.UIExternalModule;

public class TestModule implements UIExternalModule {

    ExternalModel model = null;

    public JComponent createUIComponent() {
        return new TestUI(model);
    }

    public String getModuleId() {
        return "Multimeter";
    }

    public String getMenuPath() {
        return "MultiTest";
    }

    public void init(ExternalModel model, Map<String, String> params) {
        this.model = model;
    }

    public String getModelId() {
        return model.getIdentity();
    }
}
