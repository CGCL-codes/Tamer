package playground.yu.newPlan;

import playground.marcel.MyRuns;

public class FilterPlansWithRouteInArea {

    /**
	 * @param args
	 */
    public static void main(String[] args) {
        MyRuns.filterPlansWithRouteInArea(new String[] { "../data/ivtch/configMake10pctZrhPlans.xml" }, 683518.0, 246836.0, 30000.0);
    }
}
