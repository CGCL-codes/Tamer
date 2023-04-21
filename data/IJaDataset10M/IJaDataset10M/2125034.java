package org.karticks.mapreduce;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * A simple harness to run Map-Reduce implementations. This harness starts a <code>Thread</code> for
 * every Map phase (and hence not very scalable for large number of concurrent Map jobs),
 * and adds the result of each Map phase to a list (of <code>Maps</code>), and passes this <code>List</code> to
 * the Reduce phase.
 *  
 * @author Kartick Suriamoorthy
 *
 */
public class MapReduceWorker {

    private List<Map<String, Integer>> mapList = null;

    private Object mutex = new Object();

    private List<Mapper> mappers = null;

    private List<InputStream> sources = null;

    private int counter = 0;

    public MapReduceWorker() {
        mapList = new ArrayList<Map<String, Integer>>();
        mappers = new ArrayList<Mapper>();
        sources = new ArrayList<InputStream>();
    }

    /**
	 * Adds this <code>Mapper</code> and the <code>InputStream</code> from which the <code>Mapper</code> is going
	 * to read its data to an internal collection. All the <code>Mappers</code> (that
	 * were added) will be run when <code>doWork()</code> is called.
	 * 
	 * @param mapper The <code>Mapper</code> object responsible for the Map phase
	 * @param is The <code>InputStream</code> from which the <code>Mapper</code> object is going to read data
	 */
    public void addMapper(final Mapper mapper, final InputStream is) {
        mappers.add(mapper);
        sources.add(is);
    }

    /**
	 * Runs the Map and Reduce phase. First runs the Map phase for every <code>Mapper</code>
	 * that was created by executing each <code>Mapper.doMap()</code> in a separate thread. A
	 * thread for each Map phase is created, and hence this implementation is not
	 * scalable for large number of concurrent Map jobs. After all the Map jobs
	 * are finished, the Reduce phase is called by passing all the intermediate
	 * results (<code>Map</code> objects) from the Map phase. The final <code>Map</code> object from the
	 * Reduce phase is returned.
	 * 
	 * @return The <code>Map</code> object from the Reduce phase
	 */
    public Map<String, Integer> doWork() {
        counter = mappers.size();
        for (int i = 0; i < mappers.size(); i++) {
            final Mapper mapper = mappers.get(i);
            final InputStream is = sources.get(i);
            Runnable r = new Runnable() {

                public void run() {
                    Map<String, Integer> map = mapper.doMap(is);
                    synchronized (mutex) {
                        mapList.add(map);
                        counter--;
                    }
                }
            };
            Thread t = new Thread(r);
            t.setName("Mapper Thread - " + i);
            t.start();
        }
        while (true) {
            if (counter == 0) {
                break;
            } else {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException ie) {
                    throw new RuntimeException("Interruped while waiting for Mapper threads to finish.", ie);
                }
            }
        }
        Reducer reducer = new Reducer();
        Map<String, Integer> result = reducer.doReduce(mapList);
        return result;
    }
}
