package org.teragrid.gateways;

import java.util.ArrayList;
import java.util.Arrays;
import noNamespace.MachineType;
import noNamespace.MachinesType;
import noNamespace.QueueList;
import edu.ucsb.cs.QBETSClient;
import noNamespace.DeadlinePredictionDocument;
import noNamespace.BoundPredictionDocument;

public class _Test {

    /**
	 * @param args
	 */
    public static void main(String[] args) throws java.rmi.RemoteException {
        long timeStamp = 0;
        String[] hosts = new String[3];
        hosts[0] = "datastar";
        hosts[1] = "sdscteragrid";
        hosts[2] = "ncsateragrid";
        int nodes = 4;
        long wallTime = 381;
        float quantile = 0;
        float confidence = 0;
        long startDeadline = 600;
        try {
            if (hosts.length <= 0) {
                throw new java.rmi.RemoteException("You must specify at least one host in your input.");
            } else {
                HostQueueLookup lookup = new HostQueueLookup();
                MachinesType machines = QBETSClient.getMachines();
                MachineType[] machArr = machines.getMachineArray();
                for (int m = 0; m < machArr.length; m++) {
                    String[] hostList = machArr[m].getLoginHosts().getHostArray();
                    QueueList.Queue[] queues = machArr[m].getQueues().getQueueArray();
                    for (int q = 0; q < queues.length; q++) {
                        lookup.putHostQueue(machArr[m].getTag(), queues[q].getStringValue());
                    }
                }
                ArrayList results = new ArrayList();
                for (int i = 0; i < hosts.length; i++) {
                    String[] queues = lookup.getQueues(hosts[i]);
                    for (int j = 0; j < queues.length; j++) {
                        BoundPredictionDocument.BoundPrediction boundPrediction = QBETSClient.predictBound(0, hosts[i], queues[j], nodes, wallTime, quantile, confidence);
                        System.out.println(hosts[i] + " , " + queues[j] + " bound= " + boundPrediction.getPrediction());
                        DeadlinePredictionDocument.DeadlinePrediction prediction = QBETSClient.predictDeadline(timeStamp, hosts[i], queues[j], nodes, wallTime, confidence, startDeadline);
                        System.out.println(hosts[i] + " , " + queues[j] + " deadline= " + prediction.getPrediction());
                        System.out.println("\t" + prediction.getStatusLong());
                        results.add(new HostQueueTimes(hosts[i], queues[j], new Float(boundPrediction.getPrediction()), boundPrediction.getStatus(), boundPrediction.getStatusLong(), "not yet implemented. soon!"));
                        System.out.println("\n" + boundPrediction.toString() + "\n");
                    }
                }
                HostQueueTimes[] ret = (HostQueueTimes[]) results.toArray(new HostQueueTimes[results.size()]);
                Arrays.sort(ret);
                for (int a = 0; a < ret.length; a++) {
                    System.out.println(ret[a].getLabel() + "\t" + ret[a].getQueue() + "\t" + ret[a].getWait());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new java.rmi.RemoteException("There was an error:\n" + e.getMessage());
        }
    }
}
