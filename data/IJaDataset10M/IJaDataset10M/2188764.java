package bank.cnaps2.tmp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import gneit.topbase.api.core.param.ParamSet;
import gneit.topbase.api.core.serivce.callback.Callback;
import gneit.topbase.api.core.spring.SpringContextUtils;
import gneit.topbase.api.data.page.IPage;
import gneit.topbase.core.serivce.callback.CallbackImpl;
import gneit.topbase.data.provider.OnceDataProvider;

public class tmpMenuProvide extends OnceDataProvider {

    protected Callback doProvide(int i, int j, ParamSet paramset) {
        Callback provideCallback = new CallbackImpl();
        List rsList = new ArrayList();
        Map map1 = new HashMap();
        map1.put("ID", "1");
        map1.put("PID", "");
        map1.put("CAPTION", "父菜单1");
        map1.put("COMMAND", "");
        map1.put("ICON", "");
        map1.put("CODE", "");
        rsList.add(map1);
        Map map2 = new HashMap();
        map2.put("ID", "10");
        map2.put("PID", "1");
        map2.put("CAPTION", "子菜单1");
        map2.put("COMMAND", "");
        map2.put("ICON", "");
        map2.put("CODE", "");
        rsList.add(map2);
        Map map3 = new HashMap();
        map3.put("ID", "11");
        map3.put("PID", "1");
        map3.put("CAPTION", "子菜单2");
        map3.put("COMMAND", "");
        map3.put("ICON", "");
        map3.put("CODE", "");
        rsList.add(map3);
        Map map4 = new HashMap();
        map4.put("ID", "112");
        map4.put("PID", "11");
        map4.put("CAPTION", "子菜单11");
        map4.put("COMMAND", "");
        map4.put("ICON", "");
        map4.put("CODE", "");
        rsList.add(map4);
        provideCallback.setCallbackData(rsList);
        provideCallback.setCallbackCount(rsList.size());
        provideCallback.setCallbackFinish(true);
        provideCallback.setTotalCount(rsList.size());
        return provideCallback;
    }
}
