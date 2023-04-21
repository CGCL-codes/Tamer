package org.liris.schemerger.measurements;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.TreeMap;
import org.liris.schemerger.chronicle.IChronicleMinimalSet;
import org.liris.schemerger.chronicle.cstdb.CstDB;
import org.liris.schemerger.core.dataset.SimSequence;
import org.liris.schemerger.core.pattern.IChronicle;
import org.liris.schemerger.core.pattern.ItemType;
import org.liris.schemerger.utils.ItemTypeEntry;

/**
 * 
 * @author Damien Cram
 */
public class FIFOViewer extends ProcessLogger {

    private int nmax = 0;

    CstDB<?> cstDb = null;

    private int itCnt = 0;

    private EventHandler startIteration = new EventHandler() {

        public void handleEvent(Object... arr) {
            FIFOViewer.this.itCnt++;
            IChronicleMinimalSet<?, ?> frequents = (IChronicleMinimalSet<?, ?>) arr[1];
            if (FIFOViewer.this.itCnt % 10000 == 0) {
                System.out.println("----------------------- " + FIFOViewer.this.itCnt + " ----------------------------");
                for (IChronicle<?> f : frequents.getFrequents()) System.out.println(f);
            }
        }
    };

    private ItemType[] itemTypes;

    private EventHandler endMining = new EventHandler() {

        public void handleEvent(Object... arr) {
            IChronicleMinimalSet<?, ?> frequents = (IChronicleMinimalSet<?, ?>) arr[0];
            for (IChronicle<?> c : frequents.getFrequents()) {
                if (c.getEpisode().size() > FIFOViewer.this.nmax) FIFOViewer.this.nmax = c.getEpisode().size();
            }
            BigInteger totalNumber = new BigInteger("0");
            for (int k = 2; k <= FIFOViewer.this.nmax + 1; k++) {
                ChronicleNumberEstimator cstEst = new ChronicleNumberEstimator(k, FIFOViewer.this.itemTypes);
                while (cstEst.hasNext()) {
                    LinkedList<ItemType> episode = cstEst.getNext();
                    BigInteger nbEpisodeChronicles = new BigInteger("1");
                    boolean toutCoupleFrequent = true;
                    for (int i = 0; i < k; i++) {
                        for (int j = i + 1; j < k; j++) {
                            ItemTypeEntry entry = new ItemTypeEntry(episode.get(i), episode.get(j));
                            if (FIFOViewer.this.cstDb.getConstraintGraph(entry) == null) {
                                toutCoupleFrequent = false;
                                break;
                            }
                            int cstGraphSize = FIFOViewer.this.cstDb.getConstraintGraph(entry).size();
                            BigInteger cstGraphSizeBigInt = new BigInteger((new Integer(cstGraphSize)).toString());
                            nbEpisodeChronicles = nbEpisodeChronicles.multiply(cstGraphSizeBigInt);
                        }
                        if (!toutCoupleFrequent) break;
                    }
                    if (!toutCoupleFrequent) {
                    } else {
                        totalNumber = totalNumber.add(nbEpisodeChronicles);
                    }
                }
            }
            System.out.println("nombre total de chroniques: " + totalNumber);
            System.out.println("nombre effectivement traitées: " + FIFOViewer.this.itCnt);
        }
    };

    private EventHandler startMining = new EventHandler() {

        public void handleEvent(Object... arr) {
            FIFOViewer.this.itCnt++;
            SimSequence<?> s = (SimSequence<?>) arr[0];
            FIFOViewer.this.cstDb = (CstDB<?>) arr[1];
            FIFOViewer.this.itemTypes = s.getItemTypes().toArray(new ItemType[s.getItemTypes().size()]);
            List<ItemType> list = new LinkedList<ItemType>(Arrays.asList(FIFOViewer.this.itemTypes));
            Collections.sort(list);
            FIFOViewer.this.itemTypes = list.toArray(new ItemType[list.size()]);
        }
    };

    @Override
    protected void fillHandlerMap(TreeMap<Integer, EventHandler> map) {
        map.put(CHOOSE_FIRST, this.startIteration);
        map.put(START_MINING, this.startMining);
        map.put(END_MINING, this.endMining);
    }
}
