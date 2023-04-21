package org.goet;

import org.goet.gui.*;
import org.goet.datamodel.*;
import org.goet.datamodel.impl.*;
import org.goet.dataadapter.*;
import javax.swing.*;
import java.awt.*;
import java.net.*;
import java.lang.reflect.*;

public class GOET {

    public static void main(String[] args) {
        org.goet.datamodel.reflect.Initializer.initialize();
        Controller controller = new Controller();
        GOETFrame frame = new GOETFrame(controller);
        frame.show();
    }

    public static String getVersion() {
        return "0.501 (alpha version)";
    }
}
