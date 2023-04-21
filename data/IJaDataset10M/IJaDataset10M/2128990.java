package jp.dodododo.aop.instrument;

import java.lang.instrument.Instrumentation;

public class Premain {

    public static void premain(String agentArgs, Instrumentation instrumentation) {
        instrumentation.addTransformer(new Transformer());
    }
}
