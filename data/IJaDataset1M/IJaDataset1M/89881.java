package jset.sootwrapper;

import java.util.Map;
import jset.model.jaxb.CallGraphWriter;
import org.apache.log4j.Logger;
import soot.PhaseOptions;
import soot.Scene;
import soot.SceneTransformer;
import soot.jimple.spark.SparkTransformer;
import soot.jimple.toolkits.callgraph.CHATransformer;
import soot.jimple.toolkits.callgraph.CallGraph;
import soot.options.Options;

public class SootRunner implements Runnable {

    private SootOptions sootOptions;

    private CallGraph callGraph;

    public SootRunner(SootOptions sootOptions) {
        this.sootOptions = sootOptions;
    }

    public void run() {
        setDefaultPhaseOptions();
        setCustomPhaseOptions();
        Logger.getLogger(SootWrapper.class).info("Starting Soot with options:\n" + sootOptions.toString());
        Logger.getLogger(SootWrapper.class).info("Loading basic classes...");
        Scene.v().setSootClassPath(sootOptions.getSootClassPath());
        Scene.v().setMainClass(Scene.v().loadClassAndSupport(sootOptions.getMainClass()));
        Logger.getLogger(SootWrapper.class).info("Loading dynamic classes...");
        Scene.v().loadNecessaryClasses();
        Map<String, String> transformOptions = sootOptions.getTransformerOptions();
        transformOptions.put("enabled", "true");
        SceneTransformer xFormer = null;
        if (SootConstants.CHA_TRANSFORMER.equals(sootOptions.getTransformer())) {
            Logger.getLogger(SootWrapper.class).info("Starting CHATransformer...");
            xFormer = CHATransformer.v();
            xFormer.transform("cg.cha", transformOptions);
        }
        if (SootConstants.SPARK_TRANSFORMER.equals(sootOptions.getTransformer())) {
            Logger.getLogger(SootWrapper.class).info("Starting SPARKTransformer...");
            xFormer = SparkTransformer.v();
            setDefaultSPARKPhaseOptions(transformOptions);
            xFormer.transform("cg.spark", transformOptions);
        }
        callGraph = Scene.v().getCallGraph();
        Logger.getLogger(SootWrapper.class).info("Writing call graph (" + callGraph.size() + ") edges...");
        try {
            CallGraphWriter.write(callGraph, sootOptions);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public CallGraph getCallGraph() {
        return callGraph;
    }

    private void setDefaultSPARKPhaseOptions(Map<String, String> optionsMap) {
        optionsMap.putAll(PhaseOptions.v().getPhaseOptions("cg.spark"));
        optionsMap.put("cg.spark", "enabled:true");
        optionsMap.put("enabled", "true");
        optionsMap.put("on-fly-cg", "true");
        optionsMap.put("verbose", "false");
    }

    private void setCustomPhaseOptions() {
        for (String key : sootOptions.getPhaseOptions().keySet()) {
            PhaseOptions.v().setPhaseOption(key, sootOptions.getPhaseOptions().get(key));
        }
    }

    private void setDefaultPhaseOptions() {
        Options.v().set_whole_program(true);
        PhaseOptions.v().setPhaseOption("jb.ne", "enabled:false");
        PhaseOptions.v().setPhaseOption("jb.uce", "enabled:false");
        PhaseOptions.v().setPhaseOption("jb.dae", "enabled:false");
        PhaseOptions.v().setPhaseOption("jb.ule", "enabled:false");
        PhaseOptions.v().setPhaseOption("jb.cp-ule", "enabled:false");
        PhaseOptions.v().setPhaseOption("jj", "enabled:false");
        PhaseOptions.v().setPhaseOption("wstp", "enabled:false");
        PhaseOptions.v().setPhaseOption("wsop", "enabled:false");
        PhaseOptions.v().setPhaseOption("wjtp", "enabled:false");
        PhaseOptions.v().setPhaseOption("wjop", "enabled:false");
        PhaseOptions.v().setPhaseOption("wjap", "enabled:false");
        PhaseOptions.v().setPhaseOption("shimple", "enabled:false");
        PhaseOptions.v().setPhaseOption("stp", "enabled:false");
        PhaseOptions.v().setPhaseOption("sop", "enabled:false");
        PhaseOptions.v().setPhaseOption("jtp", "enabled:false");
        PhaseOptions.v().setPhaseOption("jop", "enabled:false");
        PhaseOptions.v().setPhaseOption("jap", "enabled:false");
        PhaseOptions.v().setPhaseOption("gb", "enabled:false");
        PhaseOptions.v().setPhaseOption("gop", "enabled:false");
        PhaseOptions.v().setPhaseOption("bb", "enabled:false");
        PhaseOptions.v().setPhaseOption("bop", "enabled:false");
        PhaseOptions.v().setPhaseOption("tag", "enabled:false");
        PhaseOptions.v().setPhaseOption("db", "enabled:false");
        PhaseOptions.v().setPhaseOption("cg", "implicit-entry:true");
        PhaseOptions.v().setPhaseOption("cg", "verbose:false");
        PhaseOptions.v().setPhaseOption("cg", "all-reachable:true");
    }
}
