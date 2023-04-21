package com.izforge.izpack.sample;

import com.izforge.izpack.panels.*;

public class IPValidator implements Validator {

    public boolean validate(ProcessingClient client) {
        if (client.getNumFields() != 4) {
            return (false);
        }
        boolean isIP = true;
        for (int i = 0; i < 4; i++) {
            int value;
            try {
                value = Integer.parseInt(client.getFieldContents(i));
                if ((value < 0) || (value > 255)) {
                    isIP = false;
                }
            } catch (Throwable exception) {
                isIP = false;
            }
        }
        return (isIP);
    }
}
