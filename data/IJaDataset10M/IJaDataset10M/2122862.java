package dp;

import java.util.Random;
import mil.army.usace.ehlschlaeger.digitalpopulations.censusgen.Params;
import mil.army.usace.ehlschlaeger.digitalpopulations.censusgen.Phase_InitialPlacement;
import mil.army.usace.ehlschlaeger.digitalpopulations.censusgen.RegionData;
import mil.army.usace.ehlschlaeger.digitalpopulations.censusgen.Solution;
import oms3.annotations.Author;
import oms3.annotations.Description;
import oms3.annotations.Execute;
import oms3.annotations.In;
import oms3.annotations.Out;
import oms3.annotations.VersionInfo;

@Description("CensusGen phase 2:" + " Generate a complete set of households, giving them a plausible initial placement.")
@Author(name = "Chuck Ehlschlaeger, William R. Zwicky", org = "CERL")
@VersionInfo("$Date: 2011-07-19 03:49:55 -0500 (Tue, 19 Jul 2011) $")
public class CensusGenP2 {

    @Description("Our run-time configuration.")
    @In
    public Params params;

    @Description("Our source of random numbers.")
    @In
    public Random random;

    @Description("Main useful region map and its table.")
    @In
    public RegionData primaryRegion;

    @Description("Optimized expansion factors")
    @In
    public int[] numRealizations2Make;

    @Description("Wrapper for all data representing one solution:" + " archtypes, locations of realizations, and statistical quality of same.")
    @In
    @Out
    public Solution soln;

    @Execute
    public void execute() throws Exception {
        Phase_InitialPlacement p2 = new Phase_InitialPlacement(soln, primaryRegion.map, numRealizations2Make);
        p2.setParams(params);
        p2.setRandomSource(random);
        p2.go();
    }
}
