package com.sbook.canyonjam.optimize;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import edu.poly.bxmc.betaville.jme.map.ILocation;
import edu.poly.bxmc.betaville.net.UnexpectedServerResponse;
import edu.poly.bxmc.betaville.xml.USGSResponse;

/**
 * @author Skye Book
 *
 */
public class OptimizablePool {

    private volatile int counter = 0;

    private ExecutorService threadPool;

    float[] heights = new float[1000];

    /**
	 * 
	 */
    public OptimizablePool(int numThreads) {
        threadPool = Executors.newFixedThreadPool(numThreads);
    }

    public void submit(final int index, final ILocation gps) {
        new Runnable() {

            @Override
            public void run() {
                USGSResponse elevationResponse = new USGSResponse(gps.getGPS().getLatitude(), gps.getGPS().getLongitude());
                try {
                    elevationResponse.parse();
                } catch (UnexpectedServerResponse e) {
                    System.out.println(e.getMessage());
                    return;
                }
                heights[index] = (float) elevationResponse.getElevation();
            }
        };
    }
}
