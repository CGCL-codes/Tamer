package org.openremote.modeler.client.rpc;

import java.util.List;
import org.openremote.modeler.domain.Device;
import org.openremote.modeler.domain.DeviceCommand;
import org.openremote.modeler.irfileparser.BrandInfo;
import org.openremote.modeler.irfileparser.CodeSetInfo;
import org.openremote.modeler.irfileparser.DeviceInfo;
import org.openremote.modeler.irfileparser.GlobalCache;
import org.openremote.modeler.irfileparser.IRCommandInfo;
import org.openremote.modeler.irfileparser.IRTrans;
import com.extjs.gxt.ui.client.data.BeanModel;
import com.google.gwt.user.client.rpc.AsyncCallback;

public interface IRFileParserRPCServiceAsync {

    void getBrands(AsyncCallback<List<BrandInfo>> callback);

    void getDevices(BrandInfo brand, AsyncCallback<List<DeviceInfo>> callback);

    void getCodeSets(DeviceInfo device, AsyncCallback<List<CodeSetInfo>> callback);

    void getIRCommands(CodeSetInfo codeset, AsyncCallback<List<IRCommandInfo>> callback);

    void saveCommands(Device device, GlobalCache globalCache, IRTrans irTrans, List<IRCommandInfo> selectedFunctions, AsyncCallback<List<DeviceCommand>> callback);
}
