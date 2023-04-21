package com.hy.mydesktop.client.rpc.reader;

import java.util.ArrayList;
import java.util.List;
import com.extjs.gxt.ui.client.data.DataReader;
import com.extjs.gxt.ui.client.data.ListLoadResult;
import com.extjs.gxt.ui.client.data.ModelData;
import com.extjs.gxt.ui.client.data.TreeModelReader;
import com.hy.mydesktop.shared.rpc.meta.GxtToSeamServiceResult;
import com.hy.mydesktop.shared.rpc.meta.GxtToSeamServiceTreeNode;

/**
 * 
 * <ul>
 * <li>开发作者：花宏宇</li>
 * <li>设计日期：2010-10-9；时间：下午05:33:08</li>
 * <li>类型名称：GxtToSeamServiceResultTreeReader</li>
 * <li>设计目的：用于解析GxtToSeamServiceResult的DataReader</li>
 * </ul>
 * <ul>
 * <b>修订编号：</b>
 * <li>修订日期：</li>
 * <li>修订作者：</li>
 * <li>修订原因：</li>
 * <li>修订内容：</li>
 * </ul>
 */
public class GxtToSeamServiceResultTreeReader<D> implements DataReader<D> {

    public D read(Object loadConfig, Object data) {
        System.err.println("####GxtToSeamServiceResultTreeReader.read() is running...........................");
        if (data instanceof GxtToSeamServiceResult) {
            GxtToSeamServiceResult gxtToSeamServiceResult = (GxtToSeamServiceResult) data;
            ModelData modelData = gxtToSeamServiceResult.getResult();
            for (ModelData subModelData : (List<ModelData>) modelData.get("listOfModelData")) {
                System.out.println("gxtToSeamServiceResult.getResult()========modelData.get(listOfModelData)" + subModelData);
                if (subModelData instanceof GxtToSeamServiceTreeNode) {
                    System.out.println("diaplayText " + ((GxtToSeamServiceTreeNode) subModelData).getDisplayText());
                    ;
                    System.out.println("getServiceMethodName " + ((GxtToSeamServiceTreeNode) subModelData).getServiceMethodName());
                    ;
                }
            }
            System.out.println("GxtToSeamServiceResultTreeReader.read().loadConfig is ######" + loadConfig);
            System.out.println(modelData.get("listOfModelData"));
            System.err.println("####GxtToSeamServiceResultTreeReader.read() is over...........................");
            return (D) (gxtToSeamServiceResult.getResult().get("listOfModelData"));
        }
        assert false : "Error converting data";
        return null;
    }
}
