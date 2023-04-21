package playground.anhorni.crossborder;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Hashtable;
import org.matsim.network.NetworkLayer;
import org.matsim.gbl.Gbl;
import playground.anhorni.crossborder.verification.Verification;

public class FMAParser extends Parser {

    private Hashtable<Integer, Zone> zones = new Hashtable<Integer, Zone>();

    private Verification verification;

    public FMAParser(NetworkLayer network, String file, Hashtable<Integer, Zone> zones) {
        super(network, file);
        this.zones = zones;
    }

    public Verification getVerification() {
        return verification;
    }

    public void setVerification(Verification verification) {
        this.verification = verification;
    }

    public int parse(String type, int startTime, int actPersonNumber) {
        int nrPlans = this.preParse(type, startTime);
        int recentlyAddedNumberOfPersons = 0;
        try {
            FileReader file_reader = new FileReader(this.file);
            BufferedReader buffered_reader = new BufferedReader(file_reader);
            String curr_line = buffered_reader.readLine();
            for (int i = 0; i < 7; i++) {
                curr_line = buffered_reader.readLine();
            }
            OnlineWriter onlineWriter = new OnlineWriter();
            onlineWriter.setFileName("output/" + type + startTime);
            onlineWriter.setNetwork(this.network);
            onlineWriter.initWriter();
            while ((curr_line = buffered_reader.readLine()) != null) {
                String[] entries = curr_line.split("\t", -1);
                Relation rel = new Relation();
                Zone fromZone = this.zones.get(Integer.parseInt(entries[0].trim()));
                Zone toZone = this.zones.get(Integer.parseInt(entries[1].trim()));
                if (fromZone.getId() > Config.chNumbers) {
                    rel.setFromZone(fromZone);
                    rel.setToZone(toZone);
                    if (!Config.lookAtTransit && rel.checkTransit()) {
                        continue;
                    }
                    double vol = Config.calibration[startTime] * (double) Double.parseDouble(entries[2].trim());
                    rel.setVolume(vol);
                    this.verification.addToAggregatedVolume(type, startTime, vol);
                    rel.setType(type);
                    rel.setStartTime(startTime);
                    rel.assignPlansToRelations(super.network);
                    rel.assignStartingTime(nrPlans, recentlyAddedNumberOfPersons);
                    ArrayList<Plan> plansRel = rel.getPlans();
                    if (rel.checkTransit()) {
                        this.verification.addTransitTripsPerHour(startTime, plansRel.size());
                    }
                    onlineWriter.setPlans(plansRel);
                    onlineWriter.write(actPersonNumber + recentlyAddedNumberOfPersons);
                    recentlyAddedNumberOfPersons += plansRel.size();
                }
            }
            onlineWriter.endWrite();
            buffered_reader.close();
        } catch (IOException e) {
            Gbl.errorMsg(e);
        }
        this.verification.setXTripsPerHour(type, startTime, recentlyAddedNumberOfPersons);
        return recentlyAddedNumberOfPersons;
    }

    public int preParse(String actType, int h) {
        int recentlyAddedNumberOfPersons = 0;
        double difference = 0.0;
        try {
            FileReader file_reader = new FileReader(this.file);
            BufferedReader buffered_reader = new BufferedReader(file_reader);
            String curr_line = buffered_reader.readLine();
            for (int i = 0; i < 7; i++) {
                curr_line = buffered_reader.readLine();
            }
            while ((curr_line = buffered_reader.readLine()) != null) {
                String[] entries = curr_line.split("\t", -1);
                Relation rel = new Relation();
                Zone fromZone = this.zones.get(Integer.parseInt(entries[0].trim()));
                Zone toZone = this.zones.get(Integer.parseInt(entries[1].trim()));
                if (fromZone.getId() > Config.chNumbers) {
                    rel.setFromZone(fromZone);
                    rel.setToZone(toZone);
                    if (!Config.lookAtTransit && rel.checkTransit()) {
                        continue;
                    }
                    double vol = Config.calibration[h] * (double) Double.parseDouble(entries[2].trim());
                    rel.setVolume(vol);
                    rel.setType(actType);
                    rel.assignPlansToRelations(super.network);
                    ArrayList<Plan> plansRel = rel.getPlans();
                    recentlyAddedNumberOfPersons += plansRel.size();
                    difference += vol - plansRel.size();
                }
            }
            buffered_reader.close();
        } catch (IOException e) {
            Gbl.errorMsg(e);
        }
        this.verification.addXDifference(actType, difference);
        return recentlyAddedNumberOfPersons;
    }
}
