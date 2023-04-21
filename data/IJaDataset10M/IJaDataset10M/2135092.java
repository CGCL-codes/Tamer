package cn.myapps.core.validate.repository.action;

import java.util.Collection;
import java.util.Map;
import junit.framework.TestCase;
import cn.myapps.core.deploy.module.ejb.ModuleProcess;
import cn.myapps.core.deploy.module.ejb.ModuleVO;
import cn.myapps.core.validate.repository.ejb.ValidateRepositoryProcess;
import cn.myapps.core.validate.repository.ejb.ValidateRepositoryVO;
import cn.myapps.util.ProcessFactory;
import cn.myapps.util.sequence.Sequence;

public class ValidateRepositoryHelperTest extends TestCase {

    ValidateRepositoryHelper helper = null;

    protected void setUp() throws Exception {
        super.setUp();
        helper = new ValidateRepositoryHelper();
    }

    protected void tearDown() throws Exception {
        super.tearDown();
    }

    public void testGet_validate() throws Exception {
        String moduleid = null;
        ModuleProcess dp = (ModuleProcess) ProcessFactory.createProcess(ModuleProcess.class);
        ValidateRepositoryProcess vp = (ValidateRepositoryProcess) ProcessFactory.createProcess(ValidateRepositoryProcess.class);
        Collection coll = dp.doSimpleQuery(null, null);
        ModuleVO mod = (ModuleVO) coll.iterator().next();
        Map map = new ValidateRepositoryHelper().get_validate("11de-96ab-1cd52bcd-979f-7d180a5b557b");
        if (map == null) assertEquals(1, map.size()); else System.out.println(map);
        ValidateRepositoryVO vo = new ValidateRepositoryVO();
        vo.setName("name");
        vp.doCreate(vo);
        dp.doRemove(vo.getId());
    }
}
