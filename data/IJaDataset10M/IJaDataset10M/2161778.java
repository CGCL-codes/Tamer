package playground.yu.integration.cadyts.utils;

import java.util.Map;
import java.util.TreeMap;
import org.matsim.analysis.VolumesAnalyzer;
import org.matsim.api.core.v01.Id;
import org.matsim.core.controler.Controler;
import org.matsim.core.controler.events.AfterMobsimEvent;
import org.matsim.core.controler.events.ShutdownEvent;
import org.matsim.core.controler.events.StartupEvent;
import org.matsim.core.controler.listener.AfterMobsimListener;
import org.matsim.core.controler.listener.ShutdownListener;
import org.matsim.core.controler.listener.StartupListener;
import org.matsim.counts.Count;
import playground.yu.utils.io.SimpleWriter;
import utilities.math.BasicStatistics;

/**
 * calculates the sample variance of simulated traffic volumes on some places,
 * where the count station are installed
 *
 * @author Y. Chen
 *
 */
public class SampleVariance4simVolumes implements StartupListener, AfterMobsimListener, ShutdownListener {

    private final Map<Id, Map<Integer, BasicStatistics>> sampleVariances = new TreeMap<Id, Map<Integer, BasicStatistics>>();

    private double countsScaleFactor;

    @Override
    public void notifyAfterMobsim(AfterMobsimEvent event) {
        Controler ctl = event.getControler();
        VolumesAnalyzer va = ctl.getVolumes();
        for (Id countId : sampleVariances.keySet()) {
            int[] volumes = va.getVolumesForLink(countId);
            Map<Integer, BasicStatistics> localStatistics = sampleVariances.get(countId);
            for (Integer timeStep : localStatistics.keySet()) {
                double simTrafVol = volumes != null ? countsScaleFactor * volumes[timeStep - 1] : 0d;
                localStatistics.get(timeStep).add(simTrafVol);
                System.out.println(">>>>>\t" + this.getClass().getName() + "\tIteration\t" + event.getIteration() + "\tcount Id\t" + countId.toString() + "\ttimeStep\t" + timeStep + "\tsimulated traffic volume\t" + simTrafVol + " [veh/h]");
            }
        }
    }

    /**
	 * @param args
	 */
    public static void main(String[] args) {
        Controler ctl = new Controler(args);
        ctl.addControlerListener(new SampleVariance4simVolumes());
        ctl.setOverwriteFiles(true);
        ctl.run();
    }

    @Override
    public void notifyStartup(StartupEvent event) {
        Controler ctl = event.getControler();
        Map<Id, Count> countsMap = ctl.getCounts().getCounts();
        for (Id countId : countsMap.keySet()) {
            Map<Integer, BasicStatistics> localStatistics = new TreeMap<Integer, BasicStatistics>();
            sampleVariances.put(countId, localStatistics);
            for (Integer timeStep : countsMap.get(countId).getVolumes().keySet()) {
                localStatistics.put(timeStep, new BasicStatistics());
            }
        }
        countsScaleFactor = ctl.getConfig().counts().getCountsScaleFactor();
    }

    public void write(String filename) {
        SimpleWriter writer = new SimpleWriter(filename);
        writer.writeln("countId\ttimeStep (H)\tsample variance");
        writer.flush();
        for (Id countId : sampleVariances.keySet()) {
            Map<Integer, BasicStatistics> localStatistics = sampleVariances.get(countId);
            for (Integer timeStep : localStatistics.keySet()) {
                writer.writeln(countId.toString() + "\t" + timeStep + "\t" + localStatistics.get(timeStep).getVar());
                writer.flush();
            }
        }
        writer.close();
    }

    @Override
    public void notifyShutdown(ShutdownEvent event) {
        write(event.getControler().getControlerIO().getOutputFilename("simpleVariance.log"));
    }
}
