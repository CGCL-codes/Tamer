package com.vss.client;

import java.io.File;
import com.vss.core.IOperate;
import com.vss.core.IRequest;
import com.vss.core.IResponse;
import com.vss.core.impl.OS;
import com.vss.core.impl.ResponseImpl;

public class VSSLocalDir implements IOperate {

    public IResponse operate(IRequest request) {
        String file = (String) request.getOptions().get(OS.FILE);
        if (file == null) {
            return new ResponseImpl(IResponse.ERROR_SUCCESS, VSSEnv.getLocalDir());
        } else if (!new File(file).exists()) {
            return new ResponseImpl(IResponse.ERROR_FILE_FAULT, "The inputted directory doesn't exists!");
        } else {
            VSSEnv.setLocalDir(file);
            return new ResponseImpl();
        }
    }
}
