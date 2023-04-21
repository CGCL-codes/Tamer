package playground.anhorni.locationchoice.analysis.mc;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import org.apache.log4j.Logger;
import org.matsim.core.gbl.Gbl;
import org.matsim.core.utils.io.IOUtils;
import playground.anhorni.locationchoice.analysis.mc.filters.GroceryFilter;
import playground.anhorni.locationchoice.analysis.mc.io.ActWriter;
import playground.anhorni.locationchoice.analysis.mc.io.TripWriter;
import playground.anhorni.locationchoice.preprocess.facilities.assembleFacilitiesVariousSources.BZReader;
import playground.anhorni.locationchoice.preprocess.facilities.assembleFacilitiesVariousSources.Hectare;

public class AnalyzeMZ {

    private static final Logger log = Logger.getLogger(AnalyzeMZ.class);

    private String outPath = "./output/analyzeMz/trips/";

    private MZ mz = new MZ();

    BinsHandling binsHandler = new BinsHandling();

    public static void main(String[] args) {
        AnalyzeMZ analyzer = new AnalyzeMZ();
        analyzer.run();
    }

    public void run() {
        Gbl.startMeasurement();
        Gbl.printElapsedTime();
        log.info("Reading MZ ...");
        mz.readFile();
        Gbl.printElapsedTime();
        log.info("Number of MZ trips: " + mz.getMzTripsAllTypes().size());
        this.analyzeTrips();
        this.analyzeDurations();
        this.analyzeActs();
        Gbl.printElapsedTime();
    }

    public void analyzeActs() {
        log.info("Starting analyze acts *************************");
        GroceryFilter groceryfilter = new GroceryFilter();
        List<MZTrip> mzTrips = groceryfilter.filterTrips(mz.getMzTripsAllTypes());
        log.info("Reading BZ ------------------------------------------------------------");
        BZReader bzReader = new BZReader();
        List<Hectare> hectares = bzReader.readBZGrocery("input/BZ/BZ01_UNT_P_DSVIEW.TXT");
        log.info("Number of hectares: " + hectares.size());
        log.info("Create hectare relations");
        CreateTripHectareRelation relationCreator = new CreateTripHectareRelation();
        List<MZTripHectare> relations = relationCreator.createRelations(mzTrips, hectares);
        log.info("Number of trip-hectare relations: " + relations.size());
        ActWriter writer = new ActWriter();
        String outpath = "output/analyzeMz/acts/";
        writer.write(relations, outpath);
        log.info("Finished analyzeActs ----------------------------------------------");
    }

    public void analyzeTrips() {
        log.info("Starting analyze trips *************************");
        this.writeSummary();
        this.writeTrips();
    }

    public void analyzeDurations() {
        this.binsHandler.createBinOutput(this.mz);
    }

    private void writeSummary() {
        int countShopTripsHinweg = 0;
        int countLeisureTripsHinweg = 0;
        int countShopTrips = 0;
        int countLeisureTrips = 0;
        Iterator<MZTrip> mzTrips_it = mz.getMzTripsAllTypes().iterator();
        while (mzTrips_it.hasNext()) {
            MZTrip mzTrip = mzTrips_it.next();
            if (mzTrip.getPurpose().equals("shop")) {
                if (mzTrip.getWzweck2().equals("1")) {
                    countShopTripsHinweg++;
                }
                countShopTrips++;
            }
            if (mzTrip.getPurpose().equals("leisure")) {
                if (mzTrip.getWzweck2().equals("1")) {
                    countLeisureTripsHinweg++;
                }
                countLeisureTrips++;
            }
        }
        try {
            BufferedWriter out = IOUtils.getBufferedWriter(outPath + "ch/summary.txt");
            out.write("Number of trips : \t" + mz.getMzTripsAllTypes().size() + "\n" + "Number of shop trips: \t" + countShopTrips + "\n" + "Number of leisure trips: \t" + countLeisureTrips + "\n" + "Number of shop trips HINWEG: \t" + countShopTripsHinweg + "\n" + "Number of leisure trips HINWEG: \t" + countLeisureTripsHinweg);
            out.flush();
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void writeTrips() {
        log.info("Writting trips file ...");
        TripWriter writer = new TripWriter();
        writer.write(mz, outPath);
    }

    public MZ getMz() {
        return mz;
    }

    public void setMz(MZ mz) {
        this.mz = mz;
    }
}
