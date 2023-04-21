package com.nepxion.swing.framework.ribbon;

import java.awt.Color;
import javax.swing.UIManager;

public class RibbonContextInitializer {

    public void initialize() {
        UIManager.put("control", new Color(212, 226, 242));
    }
}
