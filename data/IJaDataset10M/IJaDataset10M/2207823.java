package com.pms.palo.modules.hello.client;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.google.gwt.user.client.rpc.IsSerializable;
import com.tensegrity.palowebviewer.modules.paloclient.client.XView;
import com.tensegrity.palowebviewer.modules.paloclient.client.misc.TestSer;
import com.tensegrity.palowebviewer.modules.paloclient.client.misc.XPath;

public class SerializedClassA implements IsSerializable {

    private XPath databasePath;

    private XPath viewPath;

    private XPath cubePath;

    private Map dimensions = new HashMap();

    private Map subsets = new HashMap();

    private Map selectedElements = new HashMap();

    private String description;

    private String viewName;

    private List list = new ArrayList();

    public SerializedClassA() {
        list.add(new TestSer());
    }
}
