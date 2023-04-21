package com.cirnoworks.fisce.graphic.nh;

import com.cirnoworks.fisce.graphic.UIToolkit;
import com.cirnoworks.fisce.intf.IThread;
import com.cirnoworks.fisce.intf.NativeHandlerTemplate;
import com.cirnoworks.fisce.intf.VMCriticalException;
import com.cirnoworks.fisce.intf.VMException;

/**
 * @author Cloudee
 * 
 */
public class BaseHALSetBackgroundColor extends NativeHandlerTemplate {

    private final UIToolkit ui;

    public BaseHALSetBackgroundColor(UIToolkit ui) {
        this.ui = ui;
    }

    public void dealNative(int[] args, IThread thread) throws VMException, VMCriticalException {
        int color = args[0];
        ui.setBackgroundColor(color);
    }

    public String getUniqueName() {
        return "com/cirnoworks/fisce/game/BaseHAL.setBackgroundColor.(I)V";
    }
}
