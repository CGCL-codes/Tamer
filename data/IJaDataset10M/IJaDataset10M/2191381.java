package playground.kai.bvwp;

public class IllustrationFictiveExamplePV {

    private static Values economicValues;

    private static ScenarioForEval nullfall;

    private static ScenarioForEval planfall;

    public static void main(String[] args) {
        economicValues = IllustrationFictiveExamplePVEconomicValues.createEconomicValues1();
        nullfall = IllustrationFictiveExamplePVScenario.createNullfall1();
        planfall = IllustrationFictiveExamplePVScenario.createPlanfall1(nullfall);
        runRoH();
        runBVWP2003();
        runBVWP2010();
        runBVWP2015();
    }

    private static void runRoH() {
        System.out.println("\n==================================================================================================================================\n" + "Folgende Rechnung entspricht exakt ``rechnungen>javaRechnungen>RoH_PV.xlsx'' (EconomicValues, Scenario und Methodik) ");
        UtilityChanges utilityChanges = new UtilityChangesRuleOfHalf();
        utilityChanges.utilityChange(economicValues, nullfall, planfall);
    }

    private static void runBVWP2003() {
        System.out.println("\n==================================================================================================================================\n" + "Folgende Rechnung entspricht exakt ``rechnungen>javaRechnungen>BVWP2003.xlsx'' (EconomicValues, Scenario und Methodik) ");
        UtilityChanges utilityChanges = new UtilityChangesBVWP2003();
        utilityChanges.utilityChange(economicValues, nullfall, planfall);
    }

    private static void runBVWP2010() {
        System.out.println("\n==================================================================================================================================\n" + "Folgende Rechnung versucht die Methodik der Bedarfsplanüberprüfung Schiene 2010 zu nachzuvollziehen. ");
        UtilityChanges utilityChanges = new UtilityChangesBVWP2010();
        utilityChanges.utilityChange(economicValues, nullfall, planfall);
    }

    private static void runBVWP2015() {
        System.out.println("\n==================================================================================================================================\n" + "Folgende Rechnung ergibt das gleiche Ergebnis wie RoH.");
        UtilityChanges utilityChanges = new UtilityChangesBVWP2015();
        utilityChanges.utilityChange(economicValues, nullfall, planfall);
    }
}
