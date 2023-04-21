package com.amazon.merchants.timer;

import java.io.*;
import java.util.Properties;
import javax.management.timer.Timer;
import org.apache.log4j.PropertyConfigurator;
import com.amazon.merchants.jmx.MBeanServerManager;
import com.amazon.merchants.monitor.Heartbeat;

public class TestTimerAndNotifier {

    public static void main(String[] args) throws Exception {
        Properties props = new Properties();
        props.load(new FileInputStream(args[0]));
        PropertyConfigurator.configure(props.getProperty("log4j.configuration"));
        TimerService.instance(MBeanServerManager.server()).start();
        NotificationRegistry.initialize(MBeanServerManager.server(), TimerService.instance().timerName());
        NotificationRegistry.register(props);
        Heartbeat heartbeat = new Heartbeat(props);
        new Thread(heartbeat).start();
        Thread.sleep(Timer.ONE_SECOND * 30);
        heartbeat.stopBeating();
    }
}
