package oxygen.tool.wlfacade.plugins;

import java.util.Map;
import org.python.core.PyDictionary;
import oxygen.jython.OxyJythonConvenienceCommands;
import oxygen.tool.facade.FacadePluginAdapter;
import oxygen.tool.wlfacade.WLFacadeContextHelper;
import oxygen.tool.wlfacade.helpers.WLServerStart;

public class StartServerProcess extends FacadePluginAdapter {

    public void execute(String outerrfile) throws Exception {
        WLFacadeContextHelper wlctx = new WLFacadeContextHelper(ctx);
        WLServerStart.startserverprocess(wlctx, outerrfile);
    }

    public void execute(String startdir, String outerrfile) throws Exception {
        WLFacadeContextHelper wlctx = new WLFacadeContextHelper(ctx);
        WLServerStart.startserverprocess(wlctx, null, startdir, null, outerrfile);
    }

    public void execute(Map sysprops, String startdir, String outerrfile) throws Exception {
        WLFacadeContextHelper wlctx = new WLFacadeContextHelper(ctx);
        WLServerStart.startserverprocess(wlctx, sysprops, startdir, null, outerrfile);
    }

    public void execute(PyDictionary sysprops, String startdir, String outerrfile) throws Exception {
        execute(OxyJythonConvenienceCommands.py2jmap(sysprops), startdir, outerrfile);
    }
}
