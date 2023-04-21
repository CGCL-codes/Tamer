package com.modbus.main;

import com.modbus.quartz.ModbusQuertz;
import java.io.IOException;
import org.quartz.SchedulerException;

/**
 *
 * @author Administrator
 */
public class Test {

    public static void main(String[] args) throws InterruptedException, IOException, SchedulerException {
        ModbusQuertz qz = new ModbusQuertz("jobDetail", "jobDetailGroup", "cronTrigger", "triggerGroup", com.modbus.main.TestJob.class, "0/1 * * * * ?");
        qz.startTask();
    }
}
