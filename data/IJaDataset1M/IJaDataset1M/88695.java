package com.volantis.mcs.eclipse.ab.editors.devices;

import com.volantis.mcs.eclipse.core.DeviceRepositoryAccessorManager;
import com.volantis.devrep.repository.impl.testtools.device.TestDeviceRepositoryCreator;
import com.volantis.synergetics.cornerstone.utilities.xml.jaxp.TestTransformerMetaFactory;
import com.volantis.synergetics.testtools.TestCaseAbstract;
import com.volantis.synergetics.testtools.io.TemporaryFileExecutor;
import com.volantis.synergetics.testtools.io.TemporaryFileManager;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.jdom.input.DefaultJDOMFactory;
import java.io.File;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Test case for PolicyValueModifierFactory.
 */
public class PolicyValueModifierFactoryTestCase extends TestCaseAbstract {

    /**
     * A map of policy name to the PolicyValueModifier subclass that is used
     * to modify the values of the policy.
     */
    private static final Map policyNameToPVMClass = new HashMap(7);

    /**
     * Populate the map with test data.
     */
    static {
        policyNameToPVMClass.put("beep", BooleanPolicyValueModifier.class);
        policyNameToPVMClass.put("pixelsx", TextPolicyValueModifier.class);
        policyNameToPVMClass.put("ssversion", ListPolicyValueModifier.class);
        policyNameToPVMClass.put("localsec", TextPolicyValueModifier.class);
        policyNameToPVMClass.put("java", ComboPolicyValueModifier.class);
        policyNameToPVMClass.put("UAProf.MexeSpec", TextPolicyValueModifier.class);
        policyNameToPVMClass.put("UAProf.MexeClassmarks", ListPolicyValueModifier.class);
    }

    /**
     * Tests the class of the factory-created PolicyValueModifier is correct
     * for the policy name.
     */
    public void testPolicyValueModifierFactory() throws Exception {
        TemporaryFileManager manager = new TemporaryFileManager(new TestDeviceRepositoryCreator());
        manager.executeWith(new TemporaryFileExecutor() {

            public void execute(File repository) throws Exception {
                DeviceRepositoryAccessorManager deviceRAM = new DeviceRepositoryAccessorManager(repository.getPath(), new TestTransformerMetaFactory(), new DefaultJDOMFactory(), false);
                PolicyValueModifierFactory pvmodFactory = new PolicyValueModifierFactory(deviceRAM);
                Shell shell = new Shell(Display.getDefault(), SWT.SHELL_TRIM);
                Iterator it = policyNameToPVMClass.keySet().iterator();
                while (it.hasNext()) {
                    String policyName = (String) it.next();
                    PolicyValueModifier pvMod = pvmodFactory.createPolicyValueModifier(shell, SWT.NONE, policyName);
                    assertEquals(policyNameToPVMClass.get(policyName), pvMod.getClass());
                }
            }
        });
    }
}
