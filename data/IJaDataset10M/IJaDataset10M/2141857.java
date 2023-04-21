package com.testingtech.java.tri;

import com.testingtech.ttcn.tri.ISAPlugin;
import com.testingtech.ttcn.tri.extension.PortPluginProvider;

public class JavaPortProvider implements PortPluginProvider {

    public ISAPlugin getPortPlugin() {
        return new JavaPortPlugin();
    }
}
