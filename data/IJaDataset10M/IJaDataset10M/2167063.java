package com.example.staticclass;

import com.seitenbau.testing.service.Instrumentation;
import com.seitenbau.testing.shared.config.MethodConfiguration;
import com.seitenbau.testing.shared.config.RecorderConfiguration;

public class StaticClass_simplePlusMethod_instrument {

    public static void main(String[] args) {
        RecorderConfiguration configuration = new RecorderConfiguration();
        MethodConfiguration methodConfiguration = new MethodConfiguration("com.example.staticclass", "StaticClass", "int", "simplePlusMethod", "int,int");
        configuration.addMethodConfiguration(methodConfiguration);
        Instrumentation.createArtifacts(configuration, "src/test/java", "src/test/resources");
    }
}
