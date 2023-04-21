package oxygen.tool.wlfacade.plugins;

import java.util.Map;
import org.python.core.PyDictionary;
import oxygen.jython.OxyJythonConvenienceCommands;
import oxygen.tool.facade.FacadePluginAdapter;
import oxygen.tool.wlfacade.WLFacadeContextHelper;
import oxygen.tool.wlfacade.WLMBSTree;
import oxygen.tool.wlfacade.WLScriptHelper;
import oxygen.util.OxyTable;

public class ListTypes extends FacadePluginAdapter {

    public OxyTable execute() throws Exception {
        return execute((Map) null);
    }

    public OxyTable execute(Map matchMap) throws Exception {
        WLFacadeContextHelper wlctx = new WLFacadeContextHelper(ctx);
        WLScriptHelper.ensureMBSTree(wlctx);
        WLMBSTree wltree = (WLMBSTree) wlctx.getCurrentTree();
        return wltree.listtypes(matchMap);
    }

    public OxyTable execute(PyDictionary dict) throws Exception {
        return execute(OxyJythonConvenienceCommands.py2jmap(dict));
    }
}
