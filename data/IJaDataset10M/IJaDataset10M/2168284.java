package uncertain.testcase.proc;

import junit.framework.TestCase;
import uncertain.composite.CompositeLoader;
import uncertain.composite.CompositeMap;
import uncertain.ocm.OCManager;
import uncertain.ocm.PackageMapping;
import uncertain.proc.ProcedureRegistry;

public class ProcedureRegistryTest extends TestCase {

    OCManager ocm = OCManager.getInstance();

    CompositeLoader loader = CompositeLoader.createInstanceForOCM();

    protected void setUp() throws Exception {
        ocm.getClassRegistry().addPackageMapping(new PackageMapping("uncertain.proc", "uncertain.proc"));
    }

    public ProcedureRegistryTest(String name) {
        super(name);
    }

    public void testLoad() throws Exception {
        CompositeMap config = loader.loadFromClassPath("uncertain.testcase.proc.ProcedureRegistryTest");
        assertNotNull(config);
        ProcedureRegistry reg = (ProcedureRegistry) ocm.createObject(config);
        assertNotNull(reg);
        String proc = reg.getMappedProcedure("screen");
        assertEquals("aurora.service.controller.RunScreen", proc);
        CompositeMap pre_service = reg.getProcedureConfig("pre-service");
        assertNotNull(pre_service);
        assertEquals(pre_service.getChilds().size(), 2);
    }
}
