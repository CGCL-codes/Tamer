package playground.christoph.evacuation.controler;

import java.util.Arrays;
import org.matsim.api.core.v01.TransportMode;
import org.matsim.api.core.v01.population.Person;
import org.matsim.core.controler.Controler;
import org.matsim.core.controler.events.IterationStartsEvent;
import org.matsim.core.controler.events.ReplanningEvent;
import org.matsim.core.controler.events.StartupEvent;
import org.matsim.core.controler.listener.IterationStartsListener;
import org.matsim.core.controler.listener.ReplanningListener;
import org.matsim.core.controler.listener.StartupListener;
import org.matsim.core.mobsim.framework.MobsimFactory;
import org.matsim.core.population.PersonImpl;
import org.matsim.core.scenario.ScenarioImpl;
import org.matsim.households.Household;
import org.matsim.vehicles.VehicleWriterV1;
import playground.christoph.controler.KTIEnergyFlowsController;
import playground.christoph.evacuation.mobsim.EvacuationQSimFactory;
import playground.christoph.evacuation.mobsim.LegModeChecker;
import playground.christoph.evacuation.vehicles.AssignVehiclesToPlans;
import playground.christoph.evacuation.vehicles.CreateVehiclesForHouseholds;
import playground.christoph.evacuation.vehicles.HouseholdVehicleAssignmentReader;

/**
 * Prepares a scenario to be run as evacuation simulation.
 * 
 * <ul>
 * 	<li>Creates vehicles on household level based on model from bjaeggi (writes vehicles file and add vehicles to households).</li>
 * 	<li>Adapts plans to have consistent leg mode chains - it is ensured, that for each car leg a vehicles is available.</li>
 * 	<li>Ensures that within a household with n vehicles only n persons have car legs in their plans.</li>
 * </ul>
 * 
 * @author cdobler
 */
public class PrepareScenarioControler extends KTIEnergyFlowsController implements StartupListener, IterationStartsListener, ReplanningListener {

    public static final String FILENAME_VEHICLES = "output_vehicles.xml.gz";

    protected String[] householdVehicleFiles = new String[] { "Fahrzeugtypen_Kanton_AG.txt", "Fahrzeugtypen_Kanton_AI.txt", "Fahrzeugtypen_Kanton_AR.txt", "Fahrzeugtypen_Kanton_BE.txt", "Fahrzeugtypen_Kanton_BL.txt", "Fahrzeugtypen_Kanton_BS.txt", "Fahrzeugtypen_Kanton_FR.txt", "Fahrzeugtypen_Kanton_GE.txt", "Fahrzeugtypen_Kanton_GL.txt", "Fahrzeugtypen_Kanton_GR.txt", "Fahrzeugtypen_Kanton_JU.txt", "Fahrzeugtypen_Kanton_LU.txt", "Fahrzeugtypen_Kanton_NE.txt", "Fahrzeugtypen_Kanton_NW.txt", "Fahrzeugtypen_Kanton_OW.txt", "Fahrzeugtypen_Kanton_SG.txt", "Fahrzeugtypen_Kanton_SH.txt", "Fahrzeugtypen_Kanton_SO.txt", "Fahrzeugtypen_Kanton_SZ.txt", "Fahrzeugtypen_Kanton_TG.txt", "Fahrzeugtypen_Kanton_TI.txt", "Fahrzeugtypen_Kanton_UR.txt", "Fahrzeugtypen_Kanton_VD.txt", "Fahrzeugtypen_Kanton_VS.txt", "Fahrzeugtypen_Kanton_ZG.txt", "Fahrzeugtypen_Kanton_ZH.txt" };

    protected LegModeChecker legModeChecker;

    protected CreateVehiclesForHouseholds createVehiclesForHouseholds;

    protected AssignVehiclesToPlans assignVehiclesToPlans;

    protected HouseholdVehicleAssignmentReader householdVehicleAssignmentReader;

    public PrepareScenarioControler(String[] args) {
        super(Arrays.copyOfRange(args, 0, 2));
        this.addCoreControlerListener(this);
        String vehiclesFilesPath = args[2];
        if (!vehiclesFilesPath.endsWith("/")) vehiclesFilesPath = vehiclesFilesPath + "/";
        for (int i = 0; i < householdVehicleFiles.length; i++) householdVehicleFiles[i] = vehiclesFilesPath + householdVehicleFiles[i];
    }

    @Override
    public void notifyStartup(StartupEvent event) {
        for (Person person : this.scenarioData.getPopulation().getPersons().values()) {
            ((PersonImpl) person).removeUnselectedPlans();
        }
        legModeChecker = new LegModeChecker(this.createRoutingAlgorithm());
        legModeChecker.setValidNonCarModes(new String[] { TransportMode.walk, TransportMode.bike, TransportMode.pt });
        legModeChecker.setToCarProbability(0.5);
        legModeChecker.run(this.scenarioData.getPopulation());
        legModeChecker.printStatistics();
        this.householdVehicleAssignmentReader = new HouseholdVehicleAssignmentReader(this.scenarioData);
        for (String file : this.householdVehicleFiles) this.householdVehicleAssignmentReader.parseFile(file);
        this.householdVehicleAssignmentReader.createVehiclesForCrossboarderHouseholds();
        this.config.scenario().setUseVehicles(true);
        createVehiclesForHouseholds = new CreateVehiclesForHouseholds(this.scenarioData, this.householdVehicleAssignmentReader.getAssignedVehicles());
        createVehiclesForHouseholds.run();
        new VehicleWriterV1(scenarioData.getVehicles()).writeFile(this.getControlerIO().getOutputFilename(FILENAME_VEHICLES));
        this.assignVehiclesToPlans = new AssignVehiclesToPlans(this.scenarioData, this.createRoutingAlgorithm());
        for (Household household : ((ScenarioImpl) scenarioData).getHouseholds().getHouseholds().values()) {
            this.assignVehiclesToPlans.run(household);
        }
        this.assignVehiclesToPlans.printStatistics();
        MobsimFactory mobsimFactory = new EvacuationQSimFactory();
        this.setMobsimFactory(mobsimFactory);
    }

    @Override
    public void notifyIterationStarts(IterationStartsEvent event) {
        if (event.getIteration() == this.config.controler().getFirstIteration()) {
            this.assignVehiclesToPlans.reassignVehicles();
        }
    }

    @Override
    public void notifyReplanning(ReplanningEvent event) {
        this.assignVehiclesToPlans.reassignVehicles();
    }

    public static void main(final String[] args) {
        if ((args == null) || (args.length == 0)) {
            System.out.println("No argument given!");
            System.out.println("Usage: Controler config-file [dtd-file]");
            System.out.println();
        } else {
            final Controler controler = new PrepareScenarioControler(args);
            controler.setOverwriteFiles(true);
            controler.run();
        }
        System.exit(0);
    }
}
