package net.findbugs.demo.analysis;

import edu.umd.cs.findbugs.classfile.IAnalysisCache;
import edu.umd.cs.findbugs.classfile.IAnalysisEngineRegistrar;

public class EngineRegistrar implements IAnalysisEngineRegistrar {

    public void registerAnalysisEngines(IAnalysisCache analysisCache) {
        new LockCountDataflowEngine().registerWith(analysisCache);
    }
}
