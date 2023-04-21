package com.ruanko.client;

import com.jacob.activeX.ActiveXComponent;
import com.jacob.com.Dispatch;
import com.jacob.com.Variant;

public class MicControl {

    public static void MicUp() {
        try {
            ActiveXComponent dotnetCom = new ActiveXComponent("MicControl.MicControl");
            Variant var = Dispatch.call(dotnetCom, "MicSet", 1000);
            String str = var.toString();
            System.out.println("+" + str);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static void MicDown() {
        try {
            ActiveXComponent dotnetCom = new ActiveXComponent("MicControl.MicControl");
            Variant var = Dispatch.call(dotnetCom, "MicSet", -1000);
            String str = var.toString();
            System.out.println("-" + str);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static void main(String[] args) {
        MicUp();
    }
}
