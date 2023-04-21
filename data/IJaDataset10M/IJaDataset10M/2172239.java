package neissmodel.scripts;

import java.util.ArrayList;
import java.util.List;
import neissmodel.BHPSLinker;
import neissmodel.BirkinPRM;
import neissmodel.NeissModel;
import neissmodel.NeissModelParameter;

/**
 * Used to do a PRM batch run. I have hard-coded each region in London, it will
 * run the model for each.
 * @param args

 * @author Nick Malleson
 */
public class PRMBatchRun {

    public static void main(String args[]) {
        NeissModel m = new BirkinPRM();
        NeissModelParameter<Integer> sarRecords = new NeissModelParameter<Integer>("No. SAR records", 10000);
        NeissModelParameter<Integer> iterPerArea = new NeissModelParameter<Integer>("Iterations per area", 10);
        NeissModelParameter<Integer> testRecords = new NeissModelParameter<Integer>("No. test records", 50);
        String[] regions = new String[] { "00BK" };
        regions: for (int i = 0; i < regions.length; i++) {
            String reg = regions[i];
            NeissModel linker = new BHPSLinker();
            List<NeissModelParameter<?>> linker_pars = new ArrayList<NeissModelParameter<?>>();
            linker_pars.add(new NeissModelParameter<String>("Region name", reg));
            linker_pars.add(new NeissModelParameter<String>("Aggregate field", "XXXX"));
            linker_pars.add(new NeissModelParameter<Integer>("Population year", 1));
            boolean success = linker.runModel(linker_pars);
            if (success) {
                System.out.println("\n\n\t** FINISHED LINKING REGION " + reg + " ** (" + (i + 1) + "/" + regions.length + ")\n\n");
            } else {
                System.out.println("\n\n\t** ERROR LINKING WITH REGION " + reg + " ** (" + (i + 1) + "/" + regions.length + ")\n\n");
                break regions;
            }
        }
    }
}
