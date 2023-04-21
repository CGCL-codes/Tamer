package playground.anhorni.locationchoice.cs;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;
import org.apache.log4j.Logger;
import org.matsim.core.api.population.Population;
import org.matsim.core.controler.Controler;
import org.matsim.core.gbl.Gbl;
import org.matsim.core.mobsim.cppdeqsim.DEQSimControler;
import org.matsim.core.network.MatsimNetworkReader;
import org.matsim.core.network.NetworkLayer;
import org.matsim.core.population.PopulationImpl;
import org.matsim.core.population.PopulationWriter;
import playground.anhorni.locationchoice.cs.choicesetextractors.ExtractChoiceSetsRouting;
import playground.anhorni.locationchoice.cs.depr.filters.ActTypeAndAreaTripFilter;
import playground.anhorni.locationchoice.cs.depr.filters.SampleDrawer;
import playground.anhorni.locationchoice.cs.depr.filters.SampleDrawerFixedSizeRandom;
import playground.anhorni.locationchoice.cs.depr.filters.SampleDrawerFixedSizeTravelCosts;
import playground.anhorni.locationchoice.cs.depr.filters.TripFilter;
import playground.anhorni.locationchoice.cs.helper.ChoiceSets;
import playground.anhorni.locationchoice.cs.helper.ZHFacilities;
import playground.anhorni.locationchoice.cs.io.CSShapeFileWriter;
import playground.anhorni.locationchoice.cs.io.CSWriter;
import playground.anhorni.locationchoice.cs.io.ChoiceSetWriterSimple;
import playground.anhorni.locationchoice.cs.io.CompareTrips;
import playground.anhorni.locationchoice.cs.io.NelsonTripReader;
import playground.anhorni.locationchoice.cs.io.NelsonTripWriter;
import playground.anhorni.locationchoice.cs.io.TripStats;
import playground.anhorni.locationchoice.cs.io.ZHFacilitiesReader;
import playground.anhorni.locationchoice.cs.io.ZHFacilitiesWriter;
import playground.balmermi.mz.PlansCreateFromMZ;

public class GenerateChoiceSets {

    private Population choiceSetPopulation = new PopulationImpl();

    private final NetworkLayer network = new NetworkLayer();

    private ZHFacilities zhFacilities;

    private final ChoiceSets choiceSets = new ChoiceSets();

    private final List<CSWriter> writers = new Vector<CSWriter>();

    private TripFilter filter;

    private SampleDrawer sampleDrawer = null;

    private boolean isSetup = false;

    Controler controler = null;

    private int choiceSetSize = 20;

    private String choiceSetPopulationFile = null;

    private String matsimRunConfigFile = null;

    private String zhFacilitiesFile = null;

    private String shapeFile = null;

    private double walkingSpeed = 5.0;

    private String outdir = null;

    private String sampling;

    private String readNelson;

    private String DEQSim;

    private String mode;

    private String reported;

    private static final Logger log = Logger.getLogger(GenerateChoiceSets.class);

    public static void main(final String[] args) {
        String inputFile = "./input/input.txt";
        Gbl.startMeasurement();
        GenerateChoiceSets generator = new GenerateChoiceSets();
        generator.readInputFile(inputFile);
        if (!generator.isSetup()) {
            generator.setup();
        }
        generator.run();
        Gbl.printElapsedTime();
    }

    private void readInputFile(final String inputFile) {
        try {
            FileReader fileReader = new FileReader(inputFile);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            this.matsimRunConfigFile = bufferedReader.readLine();
            this.choiceSetPopulationFile = bufferedReader.readLine();
            this.outdir = bufferedReader.readLine();
            this.zhFacilitiesFile = bufferedReader.readLine();
            this.shapeFile = bufferedReader.readLine();
            this.walkingSpeed = Double.parseDouble(bufferedReader.readLine().trim()) / 3.6;
            this.choiceSetSize = Integer.parseInt(bufferedReader.readLine().trim());
            this.sampling = bufferedReader.readLine();
            this.readNelson = bufferedReader.readLine();
            this.DEQSim = bufferedReader.readLine();
            this.mode = bufferedReader.readLine();
            this.reported = bufferedReader.readLine();
            log.info("MATSim config file: " + this.matsimRunConfigFile);
            log.info("choice set population file: " + this.choiceSetPopulationFile);
            log.info("out dir: " + this.outdir);
            log.info("zhFacilitiesFile" + this.zhFacilitiesFile);
            log.info("shape file:" + this.shapeFile);
            log.info("walkingSpeed = " + this.walkingSpeed + " m/s");
            log.info("choice set size: " + this.choiceSetSize);
            log.info("Sampling :" + this.sampling);
            log.info("readNelson: " + this.readNelson);
            log.info("DEQSim : " + this.DEQSim);
            log.info("mode : " + this.mode);
            log.info("reported : " + this.reported);
            bufferedReader.close();
            fileReader.close();
        } catch (IOException e) {
            Gbl.errorMsg(e);
        }
    }

    public void setup() {
        this.zhFacilities = new ZHFacilities();
        ActTypeAndAreaTripFilter filterAreaAndType = new ActTypeAndAreaTripFilter(this.shapeFile, "s");
        this.filter = filterAreaAndType;
        boolean folderGenerated = new File(this.outdir + "shapefiles").mkdir();
        folderGenerated = folderGenerated && new File(this.outdir + "shapefiles/singletrips").mkdir();
        folderGenerated = folderGenerated && new File(this.outdir + "shapefiles/singlechoicesets").mkdir();
        folderGenerated = folderGenerated && new File(this.outdir + "choicesets").mkdir();
        if (!folderGenerated) {
            log.info("Problem while generating output folders");
        }
        ChoiceSetWriterSimple writer = new ChoiceSetWriterSimple(this.zhFacilities);
        this.writers.add(writer);
        CSShapeFileWriter shpWriter = new CSShapeFileWriter();
        this.writers.add(shpWriter);
        TripStats tripStats = new TripStats(this.mode);
        this.writers.add(tripStats);
        SampleDrawer sampleDrawer;
        if (this.sampling.equals("random")) {
            sampleDrawer = new SampleDrawerFixedSizeRandom(this.choiceSetSize);
        } else {
            sampleDrawer = new SampleDrawerFixedSizeTravelCosts(this.choiceSetSize, true);
        }
        this.setSampleDrawer(sampleDrawer);
    }

    public void run() {
        String configArgs[] = { this.matsimRunConfigFile };
        Gbl.createConfig(configArgs);
        this.createChoiceSetFacilities();
        if (this.readNelson.equals("true")) {
            this.choiceSets.setCarChoiceSets(new NelsonTripReader(this.network, this.zhFacilities).readFiles("input/MZ2005_Wege.dat", "input/810Trips.dat", "car"));
            this.choiceSets.setWalkChoiceSets(new NelsonTripReader(this.network, this.zhFacilities).readFiles("input/MZ2005_Wege.dat", "input/810Trips.dat", "walk"));
        } else {
            this.choiceSetPopulation = this.createChoiceSetPopulationFromMZ();
            new PopulationWriter(this.choiceSetPopulation, this.outdir + "/MZPopulation.txt", "v4").write();
            this.choiceSets.setCarChoiceSets(this.filter.apply(this.choiceSetPopulation, "car"));
            this.choiceSets.setWalkChoiceSets(this.filter.apply(this.choiceSetPopulation, "walk"));
        }
        if (this.DEQSim.equals("true")) {
            String[] args = { this.matsimRunConfigFile };
            this.controler = new DEQSimControler(args);
        } else {
            this.controler = new Controler(this.matsimRunConfigFile);
        }
        int tt;
        if (this.reported.equals("reported")) {
            tt = 1;
        } else {
            tt = 2;
        }
        ExtractChoiceSetsRouting listenerCar = new ExtractChoiceSetsRouting(this.controler, this.zhFacilities, this.choiceSets.getCarChoiceSets(), "car", tt);
        ExtractChoiceSetsRouting listenerWalk = new ExtractChoiceSetsRouting(this.controler, this.zhFacilities, this.choiceSets.getWalkChoiceSets(), "walk", tt);
        if (this.mode.equals("car")) {
            this.controler.addControlerListener(listenerCar);
        } else if (this.mode.equals("walk")) {
            this.controler.addControlerListener(listenerWalk);
        } else {
            log.error("No mode chosen");
        }
        log.info("Running controler: ...");
        this.controler.run();
        log.info("Output: ...");
        this.output();
    }

    private Population createChoiceSetPopulationFromMZ() {
        Population temporaryPopulation = new PopulationImpl();
        try {
            new PlansCreateFromMZ(this.choiceSetPopulationFile, this.outdir + "/output_wegeketten.dat", 1, 7).run(temporaryPopulation);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return temporaryPopulation;
    }

    private void createChoiceSetFacilities() {
        MatsimNetworkReader networkReader = new MatsimNetworkReader(this.network);
        networkReader.readFile(Gbl.getConfig().network().getInputFile());
        ZHFacilitiesReader zhFacilitiesReader = new ZHFacilitiesReader(this.network);
        zhFacilitiesReader.readFile(this.zhFacilitiesFile, this.zhFacilities);
        new ZHFacilitiesWriter().write(this.outdir, this.zhFacilities);
    }

    private void output() {
        this.choiceSets.finish();
        this.zhFacilities.finish();
        Iterator<CSWriter> writer_it = this.writers.iterator();
        while (writer_it.hasNext()) {
            CSWriter writer = writer_it.next();
            if (this.mode.equals("car")) {
                writer.write(this.outdir, "car", this.choiceSets.getCarChoiceSets());
            } else if (this.mode.equals("walk")) {
                writer.write(this.outdir, "walk", this.choiceSets.getWalkChoiceSets());
            }
        }
        NelsonTripWriter nelsonWriter = new NelsonTripWriter();
        nelsonWriter.write(this.outdir, "car", this.choiceSets.getCarChoiceSets());
        nelsonWriter.write(this.outdir, "walk", this.choiceSets.getWalkChoiceSets());
        CompareTrips compareTripsCar = new CompareTrips(this.outdir, "car");
        compareTripsCar.compare("input/ttbcar.dat", this.choiceSets.getCarChoiceSets());
        CompareTrips compareTripsWalk = new CompareTrips(this.outdir, "walk");
        compareTripsWalk.compare("input/ttbwalk.dat", this.choiceSets.getWalkChoiceSets());
    }

    public String getSpssfile() {
        return this.choiceSetPopulationFile;
    }

    public void setSpssfile(final String spssfile) {
        this.choiceSetPopulationFile = spssfile;
    }

    public String getOutdir() {
        return this.outdir;
    }

    public void setOutdir(final String outdir) {
        this.outdir = outdir;
    }

    public String getConfigFile() {
        return this.matsimRunConfigFile;
    }

    public void setConfigFile(final String configfile) {
        this.matsimRunConfigFile = configfile;
    }

    public String getZhFacilitiesFile() {
        return this.zhFacilitiesFile;
    }

    public void setZhFacilitiesFile(final String zhFacilitiesFile) {
        this.zhFacilitiesFile = zhFacilitiesFile;
    }

    public TripFilter getFilter() {
        return this.filter;
    }

    public void setFilter(final TripFilter filter) {
        this.filter = filter;
    }

    public String getShapeFile() {
        return this.shapeFile;
    }

    public void setShapeFile(final String shapeFile) {
        this.shapeFile = shapeFile;
    }

    public SampleDrawer getSampleDrawer() {
        return this.sampleDrawer;
    }

    public void setSampleDrawer(final SampleDrawer sampleDrawer) {
        this.sampleDrawer = sampleDrawer;
    }

    public boolean isSetup() {
        return this.isSetup;
    }

    public void setSetup(final boolean isSetup) {
        this.isSetup = isSetup;
    }
}
