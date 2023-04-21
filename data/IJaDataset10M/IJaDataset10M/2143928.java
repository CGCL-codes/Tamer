package org.bissa.weatherMonitor.collector;

import org.bissa.Bissa;
import org.bissa.BissaFactory;
import org.bissa.NodeInfo;
import org.bissa.tuple.Tuple;
import java.util.List;

public class TestSenser {

    public static void main(String[] args) throws InterruptedException {
        NodeInfo info = NodeInfo.getInstance();
        info.setLocalAddress("127.0.1.1");
        info.setLocalPort(9002);
        info.setBootAddress("127.0.1.1");
        info.setBootPort(9001);
        info.setConfigurationFile("/home/charith/software/final_year_project/Project/bissa/bissa-p2p" + "/trunk/bissa/config/freepastry");
        Bissa bissa = BissaFactory.createBissa(info);
        for (int i = 0; i < 1000; i++) {
            List<Tuple> list = bissa.read(new Tuple("0", null, null));
            System.out.println("new set");
            for (Tuple t : list) {
                System.out.println("" + t);
            }
            Thread.sleep(3000);
        }
    }
}
