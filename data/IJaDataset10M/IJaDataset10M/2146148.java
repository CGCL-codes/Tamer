package es.gavab.jmh.tool;

public class SimpleToolTest {

    public static void main(String[] args) {
        simpleTool("mhAnalysis/best_values_osc/cph/phub_100_10", "min");
        simpleTool("mhAnalysis/best_values_osc/cph/phub_50_5", "min");
        simpleTool("mhAnalysis/best_values_osc/mmdp/GKD-Ia", "max");
        simpleTool("mhAnalysis/best_values_osc/mmdp/GKD-Ic", "max");
        simpleTool("mhAnalysis/best_values_osc/cwp", "min");
        simpleTool("mhAnalysis/best_values_osc/ab/hb", "max");
        simpleTool("mhAnalysis/best_values_osc/ab/mesh", "max");
    }

    public static void simpleTool(String folder, String mode) {
        System.out.println(folder);
        SimpleTool.main(new String[] { folder, mode });
    }
}
