package com.example.manyparameters;

import com.seitenbau.testing.service.Instrumentation;
import com.seitenbau.testing.shared.config.MethodConfiguration;
import com.seitenbau.testing.shared.config.RecorderConfiguration;
import com.seitenbau.testing.shared.tracer.RmiTracer;

public class ManyParametertes_instrument {

    public static void main(String[] args) {
        RecorderConfiguration configuration = new RecorderConfiguration();
        MethodConfiguration methodConfiguration = new MethodConfiguration(ManyParaemters.class, "call");
        methodConfiguration.setTracer(RmiTracer.class);
        configuration.addMethodConfiguration(methodConfiguration);
        Instrumentation.createArtifacts(configuration, "src/test/aspect", "src/test/resources");
    }
}
