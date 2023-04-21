package com.bening.smsapp.test;

import java.util.Date;
import com.bening.smsapp.bootstrap.BootConfigParameters;
import com.bening.smsapp.constant.SmsAppConstant;
import com.bening.smsapp.digester.DigesterEngine;

public class RunDigesterTest {

    public static void main(String[] args) {
        Date a = new Date();
        System.out.println(a);
        String fileConfigPath = System.getProperty(SmsAppConstant.CORE_CONFIGPATH);
        BootConfigParameters params = BootConfigParameters.getInstance(fileConfigPath);
        DigesterEngine.process(params);
        a = new Date();
        System.out.println(a);
    }
}
