package com.vss.client;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import com.vss.core.Client;
import com.vss.core.IOperate;
import com.vss.core.IParcel;
import com.vss.core.IRequest;
import com.vss.core.IResponse;
import com.vss.core.impl.GetParcel;
import com.vss.core.impl.OS;
import com.vss.core.impl.RequestImpl;
import com.vss.core.impl.ResponseImpl;

public class VSSCopyFrom implements IOperate {

    public IResponse operate(IRequest request) {
        File root = new File(VSSEnv.getLocalDir());
        IResponse response = new ResponseImpl(IResponse.ERROR_FILE_FAULT, "File  not find");
        if (!root.exists()) return response;
        String url = (String) request.getOptions().get(OS.FILE);
        File file = new File(root, url);
        IParcel parcel = new GetParcel(0);
        OutputStream os = null;
        try {
            os = new FileOutputStream(file);
            while (true) {
                response = new Client().operate(new RequestImpl("Patcel", request.getOptions(), parcel));
                if (!response.isOK()) {
                    return response;
                }
                parcel = (IParcel) response.getResult();
                if (parcel.isLast()) {
                    if (response.isOK()) {
                        response = new ResponseImpl(IResponse.ERROR_SUCCESS, file.getAbsolutePath());
                    }
                    break;
                }
                os.write(parcel.getData());
            }
        } catch (IOException ex) {
            response = new ResponseImpl(IResponse.ERROR_FILE_FAULT, file.getAbsolutePath());
        } finally {
            try {
                if (os != null) os.close();
            } catch (IOException ex) {
            }
        }
        return response;
    }
}
