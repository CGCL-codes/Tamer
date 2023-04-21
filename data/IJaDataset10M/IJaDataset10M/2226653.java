package playground.dgrether;

/**
 * @author dgrether
 *
 */
public interface DgPaths {

    final String DATA = "/media/data/";

    final String WORKBASE = DATA + "work/";

    final String REPOS = WORKBASE + "repos/";

    final String WSBASE = WORKBASE + "matsimHeadWorkspace/";

    final String VSPCVSBASE = WORKBASE + "repos/vsp-cvs/";

    final String SHAREDSVN = REPOS + "shared-svn/";

    final String RUNBASE = REPOS + "runs-svn/";

    final String IVTCHNET = SHAREDSVN + "studies/schweiz-ivtch/baseCase/network/ivtch-osm.xml";

    final String IVTCHROADPRICING = SHAREDSVN + "studies/schweiz-ivtch/baseCase/roadpricing/zurichCityArea/zurichCityAreaWithoutHighwaysPricingScheme.xml";

    final String IVTCHCOUNTS = SHAREDSVN + "studies/schweiz-ivtch/baseCase/counts/countsIVTCH.xml";

    final String IVTCHBASE = SHAREDSVN + "studies/schweiz-ivtch/";

    final String STUDIESDG = SHAREDSVN + "studies/dgrether/";

    final String EXAMPLEBASE = STUDIESDG + "examples/";

    final String OUTDIR = WORKBASE + "matsimOutput/";

    final String GERSHENSONINPUT = STUDIESDG + "gershenson/input/";

    final String GERSHENSONOUTPUT = STUDIESDG + "gershenson/output/";

    final String RUNSSVN = RUNBASE;

    final String CLUSTERBASE = "/homes/extern/grether/netils/";

    final String CLUSTERSVN = CLUSTERBASE;

    final String CLUSTER_MATSIM_OUTPUT = CLUSTERBASE + "matsimOutput/";

    final String BERLIN_SCENARIO = SHAREDSVN + "studies/countries/de/berlin-bvg09/";

    final String BERLIN_NET = BERLIN_SCENARIO + "net/miv_big/m44_344_big.xml.gz";
}
