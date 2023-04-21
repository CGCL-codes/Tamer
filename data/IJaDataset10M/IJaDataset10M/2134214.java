package be.vds.test;

import java.util.Date;
import be.vds.wizard.WizardPanelDescriptor;

public class TestPanel1Descriptor extends WizardPanelDescriptor {

    public static final String IDENTIFIER = "INTRODUCTION_PANEL";

    public TestPanel1Descriptor() {
        super(IDENTIFIER, new TestPanel1());
    }

    public Object getNextPanelDescriptor() {
        return TestPanel2Descriptor.IDENTIFIER;
    }

    public Object getBackPanelDescriptor() {
        return null;
    }

    public void doBeforeNext() {
        getWizard().getModel().getDataMap().put("test1", new Date());
    }
}
