package com.volantis.mcs.protocols;

import com.volantis.mcs.devices.InternalDevice;
import com.volantis.devrep.repository.api.devices.DefaultDevice;
import com.volantis.mcs.devices.InternalDeviceFactory;
import com.volantis.mcs.protocols.capability.DeviceCapabilityConstants;
import com.volantis.mcs.protocols.capability.DeviceCapabilityManagerBuilder;
import com.volantis.mcs.protocols.hr.HorizontalRuleEmulator;
import com.volantis.mcs.protocols.hr.HorizontalRuleEmulatorFactory;
import com.volantis.mcs.protocols.hr.HorizontalRuleEmulatorWithBorderStylingOnDIV;
import com.volantis.mcs.protocols.hr.HorizontalRuleEmulatorWithBorderStylingOnHorizontalRule;
import com.volantis.mcs.protocols.hr.HorizontalRuleEmulatorWithBorderStylingOnMultipleDivs;
import com.volantis.mcs.protocols.hr.TableAttrHREmulator;
import com.volantis.styling.Styles;
import com.volantis.styling.StylesBuilder;
import com.volantis.synergetics.testtools.TestCaseAbstract;
import java.util.HashMap;

/**
 * Test class for the HrEmulationFactory
 */
public class HrEmulationFactoryTestCase extends TestCaseAbstract {

    private static final InternalDeviceFactory INTERNAL_DEVICE_FACTORY = InternalDeviceFactory.getDefaultInstance();

    /**
     * Test the HREmulationFactory doesn't return an emulator when it is not
     * required.
     */
    public void testEmulatorFullySupportedHR() {
        HorizontalRuleEmulatorFactory factory = getHREmulatorFactory();
        ProtocolConfigurationImpl config = new ProtocolConfigurationImpl();
        HashMap policies = new HashMap();
        policies.put(DeviceCapabilityConstants.HR_SUPPORTED, "full");
        InternalDevice device = INTERNAL_DEVICE_FACTORY.createInternalDevice(new DefaultDevice("device", policies, null));
        device.setProtocolConfiguration(config);
        DeviceCapabilityManagerBuilder builder = new DeviceCapabilityManagerBuilder(device);
        config.setDeviceCapabilityManager(builder.build());
        HorizontalRuleAttributes attrs = getHRAttributes();
        HorizontalRuleEmulator emulator = factory.getEmulator(device, attrs, true);
        assertNull("No emulator should have been specified", emulator);
    }

    /**
     * Test no emulator is returned when the hr is paritally supported but
     * no attributes are actually used
     */
    public void testEmulatorPartialSupportNoAttributes() {
        HorizontalRuleEmulatorFactory factory = getHREmulatorFactory();
        ProtocolConfigurationImpl config = new ProtocolConfigurationImpl();
        HashMap policies = new HashMap();
        policies.put(DeviceCapabilityConstants.HR_SUPPORTED, "partial");
        InternalDevice device = INTERNAL_DEVICE_FACTORY.createInternalDevice(new DefaultDevice("device", policies, null));
        device.setProtocolConfiguration(config);
        DeviceCapabilityManagerBuilder builder = new DeviceCapabilityManagerBuilder(device);
        config.setDeviceCapabilityManager(builder.build());
        HorizontalRuleAttributes attrs = new HorizontalRuleAttributes();
        HorizontalRuleEmulator emulator = factory.getEmulator(device, attrs, true);
        assertNull("No emulator should have been specified", emulator);
    }

    /**
     * test for a HorizontalRuleEmulatorWithBorderStylingOnHorizontalRule
     * emulator when the hr is partially supported
     */
    public void testFactoryHRBorderStyling() {
        HorizontalRuleEmulatorFactory factory = getHREmulatorFactory();
        ProtocolConfigurationImpl config = new ProtocolConfigurationImpl();
        HashMap policies = new HashMap();
        policies.put(DeviceCapabilityConstants.HR_SUPPORTED, "partial");
        policies.put(DeviceCapabilityConstants.HR_COLOR, "none");
        policies.put(DeviceCapabilityConstants.HR_BORDER_TOP_COLOR, "none");
        policies.put(DeviceCapabilityConstants.HR_BORDER_TOP_WIDTH, "none");
        policies.put(DeviceCapabilityConstants.HR_BORDER_BOTTOM_COLOR, "full");
        policies.put(DeviceCapabilityConstants.HR_BORDER_BOTTOM_WIDTH, "full");
        InternalDevice device = INTERNAL_DEVICE_FACTORY.createInternalDevice(new DefaultDevice("device", policies, null));
        device.setProtocolConfiguration(config);
        DeviceCapabilityManagerBuilder builder = new DeviceCapabilityManagerBuilder(device);
        config.setDeviceCapabilityManager(builder.build());
        HorizontalRuleAttributes attrs = getHRAttributes();
        HorizontalRuleEmulator emulator = factory.getEmulator(device, attrs, true);
        assertNotNull("emulator should not be null", emulator);
        assertTrue("emulator was instnace of" + emulator.getClass().getName() + "and should be an instance of " + "HorizontalRuleEmulatorWithBorderStylingOnHorizontalRule", emulator instanceof HorizontalRuleEmulatorWithBorderStylingOnHorizontalRule);
    }

    /**
     * Test for a HorizontalRuleEmulatorWithBorderStylingOnDIV when the
     * hr is not supported but the div is fully supported
     */
    public void testFactoryHRDivStylingBottomDivFullSupport() {
        HorizontalRuleEmulatorFactory factory = getHREmulatorFactory();
        ProtocolConfigurationImpl config = new ProtocolConfigurationImpl();
        HashMap policies = new HashMap();
        policies.put(DeviceCapabilityConstants.HR_SUPPORTED, "none");
        policies.put(DeviceCapabilityConstants.DIV_SUPPORTED, "full");
        InternalDevice device = INTERNAL_DEVICE_FACTORY.createInternalDevice(new DefaultDevice("device", policies, null));
        device.setProtocolConfiguration(config);
        DeviceCapabilityManagerBuilder builder = new DeviceCapabilityManagerBuilder(device);
        config.setDeviceCapabilityManager(builder.build());
        HorizontalRuleAttributes attrs = getHRAttributes();
        HorizontalRuleEmulator emulator = factory.getEmulator(device, attrs, true);
        assertNotNull("emulator should not be null", emulator);
        assertTrue("emulator was instnace of" + emulator.getClass().getName() + "and should be an instance of " + "HorizontalRuleEmulatorWithBorderStylingOnDIV", emulator instanceof HorizontalRuleEmulatorWithBorderStylingOnDIV);
        assertTrue(((HorizontalRuleEmulatorWithBorderStylingOnDIV) emulator).getBorderProperty().equals(HorizontalRuleEmulatorWithBorderStylingOnDIV.BORDER_BOTTOM_PROPERTY));
    }

    /**
     * Test for a HorizontalRuleEmulatorWithBorderStylingOnDIV when the
     * hr is not supported and the div is partially supported
     */
    public void testFactoryHRDivStylingBottom() {
        HorizontalRuleEmulatorFactory factory = getHREmulatorFactory();
        ProtocolConfigurationImpl config = new ProtocolConfigurationImpl();
        HashMap policies = new HashMap();
        policies.put(DeviceCapabilityConstants.HR_SUPPORTED, "none");
        policies.put(DeviceCapabilityConstants.DIV_SUPPORTED, "partial");
        policies.put(DeviceCapabilityConstants.DIV_BORDER_TOP_COLOR, "none");
        policies.put(DeviceCapabilityConstants.DIV_BORDER_TOP_WIDTH, "none");
        policies.put(DeviceCapabilityConstants.DIV_BORDER_BOTTOM_COLOR, "full");
        policies.put(DeviceCapabilityConstants.DIV_BORDER_BOTTOM_WIDTH, "full");
        InternalDevice device = INTERNAL_DEVICE_FACTORY.createInternalDevice(new DefaultDevice("device", policies, null));
        device.setProtocolConfiguration(config);
        DeviceCapabilityManagerBuilder builder = new DeviceCapabilityManagerBuilder(device);
        config.setDeviceCapabilityManager(builder.build());
        HorizontalRuleAttributes attrs = getHRAttributes();
        HorizontalRuleEmulator emulator = factory.getEmulator(device, attrs, true);
        assertNotNull("emulator should not be null", emulator);
        assertTrue("emulator was instnace of" + emulator.getClass().getName() + "and should be an instance of " + "HorizontalRuleEmulatorWithBorderStylingOnDIV", emulator instanceof HorizontalRuleEmulatorWithBorderStylingOnDIV);
        assertTrue(((HorizontalRuleEmulatorWithBorderStylingOnDIV) emulator).getBorderProperty().equals(HorizontalRuleEmulatorWithBorderStylingOnDIV.BORDER_BOTTOM_PROPERTY));
    }

    /**
     * Test for a HorizontalRuleEmulatorWithBorderStylingOnDIV when the
     * hr is not supported but the div is partially supported
     */
    public void testFactoryHRDivStylingTop() {
        HorizontalRuleEmulatorFactory factory = getHREmulatorFactory();
        ProtocolConfigurationImpl config = new ProtocolConfigurationImpl();
        HashMap policies = new HashMap();
        policies.put(DeviceCapabilityConstants.HR_SUPPORTED, "none");
        policies.put(DeviceCapabilityConstants.DIV_SUPPORTED, "partial");
        policies.put(DeviceCapabilityConstants.DIV_BORDER_TOP_COLOR, "full");
        policies.put(DeviceCapabilityConstants.DIV_BORDER_TOP_WIDTH, "full");
        policies.put(DeviceCapabilityConstants.DIV_BORDER_BOTTOM_COLOR, "none");
        policies.put(DeviceCapabilityConstants.DIV_BORDER_BOTTOM_WIDTH, "none");
        InternalDevice device = INTERNAL_DEVICE_FACTORY.createInternalDevice(new DefaultDevice("device", policies, null));
        device.setProtocolConfiguration(config);
        DeviceCapabilityManagerBuilder builder = new DeviceCapabilityManagerBuilder(device);
        config.setDeviceCapabilityManager(builder.build());
        HorizontalRuleAttributes attrs = getHRAttributes();
        HorizontalRuleEmulator emulator = factory.getEmulator(device, attrs, true);
        assertNotNull("emulator should not be null", emulator);
        assertTrue("emulator was instnace of" + emulator.getClass().getName() + "and should be an instance of " + "HorizontalRuleEmulatorWithBorderStylingOnDIV", emulator instanceof HorizontalRuleEmulatorWithBorderStylingOnDIV);
        assertTrue(((HorizontalRuleEmulatorWithBorderStylingOnDIV) emulator).getBorderProperty().equals(HorizontalRuleEmulatorWithBorderStylingOnDIV.BORDER_TOP_PROPERTY));
    }

    /**
     * Test for a HorizontalRuleEmulatorWithBorderStylingOnDIV when the
     * hr is not supported but the div is fully supported
     */
    public void testFactoryHRMultipleDivStyling() {
        HorizontalRuleEmulatorFactory factory = getHREmulatorFactory();
        ProtocolConfigurationImpl config = new ProtocolConfigurationImpl();
        HashMap policies = new HashMap();
        policies.put(DeviceCapabilityConstants.HR_SUPPORTED, "none");
        policies.put(DeviceCapabilityConstants.DIV_SUPPORTED, "partial");
        policies.put(DeviceCapabilityConstants.DIV_BORDER_TOP_COLOR, "full");
        policies.put(DeviceCapabilityConstants.DIV_BORDER_TOP_WIDTH, "full");
        policies.put(DeviceCapabilityConstants.DIV_BORDER_BOTTOM_COLOR, "none");
        policies.put(DeviceCapabilityConstants.DIV_BORDER_BOTTOM_WIDTH, "none");
        policies.put(DeviceCapabilityConstants.DIV_MARGIN_BOTTOM, "partial");
        policies.put(DeviceCapabilityConstants.DIV_MARGIN_TOP, "partial");
        InternalDevice device = INTERNAL_DEVICE_FACTORY.createInternalDevice(new DefaultDevice("device", policies, null));
        device.setProtocolConfiguration(config);
        DeviceCapabilityManagerBuilder builder = new DeviceCapabilityManagerBuilder(device);
        config.setDeviceCapabilityManager(builder.build());
        HorizontalRuleAttributes attrs = getHRAttributes("margin-bottom: 5px; margin-top: 5px; text-align: left");
        HorizontalRuleEmulator emulator = factory.getEmulator(device, attrs, true);
        assertNotNull("emulator should not be null", emulator);
        assertTrue("emulator was instnace of" + emulator.getClass().getName() + "and should be an instance of " + "HorizontalRuleEmulatorWithBorderStylingOnMultipleDivs", emulator instanceof HorizontalRuleEmulatorWithBorderStylingOnMultipleDivs);
    }

    /**
     * Test for a TableAttrHrEmulator when the
     * supports CSS flag is false
     */
    public void testFactoryHRTableStyling() {
        HorizontalRuleEmulatorFactory factory = getHREmulatorFactory();
        ProtocolConfigurationImpl config = new ProtocolConfigurationImpl();
        HashMap policies = new HashMap();
        policies.put(DeviceCapabilityConstants.HR_SUPPORTED, "none");
        policies.put(DeviceCapabilityConstants.DIV_SUPPORTED, "partial");
        policies.put(DeviceCapabilityConstants.DIV_BORDER_TOP_COLOR, "full");
        policies.put(DeviceCapabilityConstants.DIV_BORDER_TOP_WIDTH, "full");
        policies.put(DeviceCapabilityConstants.DIV_BORDER_BOTTOM_COLOR, "none");
        policies.put(DeviceCapabilityConstants.DIV_BORDER_BOTTOM_WIDTH, "none");
        policies.put(DeviceCapabilityConstants.DIV_MARGIN_BOTTOM, "partial");
        policies.put(DeviceCapabilityConstants.DIV_MARGIN_TOP, "partial");
        InternalDevice device = INTERNAL_DEVICE_FACTORY.createInternalDevice(new DefaultDevice("device", policies, null));
        device.setProtocolConfiguration(config);
        DeviceCapabilityManagerBuilder builder = new DeviceCapabilityManagerBuilder(device);
        config.setDeviceCapabilityManager(builder.build());
        HorizontalRuleAttributes attrs = getHRAttributes("margin-bottom: 5px; margin-top: 5px; text-align: left");
        HorizontalRuleEmulator emulator = factory.getEmulator(device, attrs, false);
        assertNotNull("emulator should not be null", emulator);
        assertTrue("emulator was instnace of" + emulator.getClass().getName() + "and should be an instance of " + "HorizontalRuleEmulatorWithBorderStylingOnMultipleDivs", emulator instanceof TableAttrHREmulator);
    }

    private HorizontalRuleEmulatorFactory getHREmulatorFactory() {
        HorizontalRuleEmulatorFactory factory = HorizontalRuleEmulatorFactory.getInstance();
        assertNotNull(factory);
        return factory;
    }

    private HorizontalRuleAttributes getHRAttributes() {
        return getHRAttributes("");
    }

    private HorizontalRuleAttributes getHRAttributes(String additionalCSS) {
        HorizontalRuleAttributes attrs = new HorizontalRuleAttributes();
        Styles styles = StylesBuilder.getCompleteStyles("color: red; width: 50%; height: 5px; " + additionalCSS);
        attrs.setStyles(styles);
        return attrs;
    }
}
