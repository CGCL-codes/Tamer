package cn.myapps.core.dynaform.activity.action;

import java.util.Collection;
import java.util.HashMap;
import junit.framework.TestCase;
import cn.myapps.base.dao.PersistenceUtils;
import cn.myapps.core.dynaform.activity.ejb.Activity;
import cn.myapps.core.dynaform.form.action.FormAction;
import cn.myapps.util.sequence.Sequence;

/**
 * @author   nicholas
 */
public class ActivityActionTest extends TestCase {

    private String iconid = null;

    private String viewid = null;

    private String name = null;

    ActivityAction action;

    FormAction formaction;

    Activity avty = new Activity();

    protected void setUp() throws Exception {
        super.setUp();
        action = new ActivityAction();
        formaction = new FormAction();
    }

    protected void tearDown() throws Exception {
        super.tearDown();
    }

    public void testDoSave() throws Exception {
        name = Sequence.getSequence() + "ActyName";
        avty.setName(name);
        iconid = Sequence.getSequence() + "iconid";
        viewid = Sequence.getSequence();
        action.set_iconid(iconid);
        action.setContent(avty);
        action.doSave();
        doView();
        doList();
        doDelete();
    }

    public void doView() throws Exception {
        String id = action.getContent().getId();
        HashMap mp = new HashMap();
        mp.put("id", new String[] { id });
        action.getContext().setParameters(mp);
        action.doView();
    }

    public void doList() throws Exception {
        action.doList();
        Collection data = action.getDatas().datas;
        if (data.size() >= 0) System.out.println(data.size()); else System.out.println(data.size());
    }

    public void doDelete() throws Exception {
        String id = action.getContent().getId();
        action.set_selects(new String[] { id });
        action.doDelete();
        PersistenceUtils.closeSession();
    }
}
