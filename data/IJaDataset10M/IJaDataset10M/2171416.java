package com.shine.NetAnalisys.threadModel;

import java.util.List;
import com.shine.NetAnalisys.util.NetScanUtil;
import com.shine.framework.ThreadPoolUtil.model.MethodThreadModel;
import com.shine.framework.core.util.ReflectionUtil;

/**
 * 网络扫描线程
 * 
 * @author viruscodecn@gmail.com
 * 
 */
public class NetScanThreadModel extends MethodThreadModel {

    public NetScanThreadModel() {
        super();
        this.setType("netScanThreadModel");
    }

    @Override
    public void excute(Object... args) {
        try {
            if (args.length != 0) {
                List<String> list = NetScanUtil.netScan((String) args[0], (String) args[1]);
                ReflectionUtil.invokeMethod(object, methodName, list);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
