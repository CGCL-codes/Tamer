package cn.myapps.core.domain.action;

import java.util.Collection;
import java.util.HashMap;
import junit.framework.TestCase;
import cn.myapps.core.department.ejb.DepartmentVO;
import cn.myapps.core.domain.ejb.DomainVO;
import cn.myapps.util.sequence.Sequence;

/**
 * @author   chris
 */
public class DomainActionTest extends TestCase {

    private DomainAction action;

    String domainName = null;

    DomainVO domain = new DomainVO();

    protected void setUp() throws Exception {
        super.setUp();
        action = new DomainAction();
    }

    protected void tearDown() throws Exception {
        super.tearDown();
    }

    public void testDoSave() throws Exception {
        domainName = "teemlink";
        domain.setName(domainName);
        domain.setApplicationid(Sequence.getSequence());
        domain.setDescription("域");
        action.setContent(domain);
        action.doSave();
        doList();
        doEdit();
        doDelete();
    }

    public void doView() throws Exception {
        String id = action.getContent().getId();
        HashMap mp = new HashMap();
        mp.put("id", new String[] { id });
        action.getContext().setParameters(mp);
        action.doView();
        String afterViewId = action.getContent().getId();
        assertEquals(afterViewId, id);
    }

    public void doList() throws Exception {
        HashMap mp = new HashMap();
        mp.put("s_name", domainName);
        action.getContext().setParameters(mp);
        action.doList();
        Collection data = action.getDatas().datas;
        assertNotNull(data);
        DepartmentVO dep = (DepartmentVO) data.iterator().next();
        assertEquals(dep.getName(), domainName);
    }

    public void doEdit() throws Exception {
        String id = action.getContent().getId();
        HashMap mp = new HashMap();
        mp.put("id", new String[] { id });
        action.getContext().setParameters(mp);
        action.doEdit();
    }

    public void doDelete() throws Exception {
        String id = action.getContent().getId();
        action.set_selects(new String[] { id });
        action.doDelete();
        DomainAction actions = new DomainAction();
        HashMap mp = new HashMap();
        mp.put("s_name", domainName);
        action.getContext().setParameters(mp);
        action.doList();
        Collection data = action.getDatas().datas;
        assertFalse(data.size() > 0);
    }
}
