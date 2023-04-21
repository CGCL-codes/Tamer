package com.volantis.mcs.runtime;

import com.volantis.mcs.integration.TestURLRewriter;
import com.volantis.mcs.integration.URLRewriter;
import com.volantis.mcs.management.tracking.PageTrackerFactory;
import com.volantis.mcs.protocols.DefaultStyleSheetHandler;
import com.volantis.mcs.runtime.configuration.ProtocolsConfiguration;
import com.volantis.mcs.runtime.configuration.WMLOutputPreference;
import com.volantis.mcs.runtime.policies.PolicyReferenceFactory;
import com.volantis.mcs.runtime.selection.VariantSelectionPolicy;
import junitx.util.PrivateAccessor;
import java.io.InputStream;
import java.lang.reflect.UndeclaredThrowableException;

/**
 * Test Volantis object.
 */
public class TestVolantis extends Volantis {

    private VariantSelectionPolicy variantSelectionPolicy;

    private ProtocolsConfiguration protocolsConfiguration;

    private PolicyReferenceFactory policyReferenceFactory;

    private RuntimeProject defaultProject;

    public TestVolantis() {
        super();
        try {
            PrivateAccessor.setField(this, "pageGenerationCache", new PageGenerationCache());
            InputStream cssInputStream = Volantis.class.getResourceAsStream("default.css");
            defaultStyleSheet = DefaultStyleSheetHandler.compileStyleSheet(cssInputStream);
        } catch (NoSuchFieldException e) {
            throw new UndeclaredThrowableException(e);
        }
    }

    public URLRewriter getURLRewriter() {
        return new TestURLRewriter();
    }

    /**
     * Test method to set the header message directly.
     *
     * @param message
     */
    public void setPageHeadingMsg(String message) {
        try {
            PrivateAccessor.setField(this, "vtPageHeadingMsg", message);
        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        }
    }

    public VariantSelectionPolicy getVariantSelectionPolicy() {
        return variantSelectionPolicy;
    }

    public void setVariantSelectionPolicy(VariantSelectionPolicy policy) {
        variantSelectionPolicy = policy;
    }

    public PageTrackerFactory getPageTrackerFactory() {
        return null;
    }

    public ProtocolsConfiguration getProtocolsConfiguration() {
        if (protocolsConfiguration == null) {
            protocolsConfiguration = new ProtocolsConfiguration();
            protocolsConfiguration.setPreferredOutputFormat(WMLOutputPreference.WMLC);
        }
        return protocolsConfiguration;
    }

    public void setPolicyReferenceFactory(PolicyReferenceFactory policyReferenceFactory) {
        this.policyReferenceFactory = policyReferenceFactory;
    }

    public PolicyReferenceFactory getPolicyReferenceFactory() {
        return policyReferenceFactory;
    }

    public RuntimeProject getDefaultProject() {
        return defaultProject;
    }

    public void setDefaultProject(RuntimeProject defaultProject) {
        this.defaultProject = defaultProject;
    }
}
