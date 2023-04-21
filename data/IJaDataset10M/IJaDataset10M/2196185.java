package com.agentfactory.josf.util;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;
import com.agentfactory.josf.action.CreateRobot;
import com.agentfactory.josf.communications.ActionSender;

public class JOSFLoader {

    public static List<String> load(ActionSender sender, String props) {
        List<String> list = new LinkedList<String>();
        Properties properties = new Properties();
        try {
            properties.load(new FileInputStream(props));
            String[] robots = properties.getProperty("robots").split(",");
            for (String robot : robots) {
                String[] rbt = robot.split(":");
                sender.queue(new CreateRobot(rbt[0], rbt[1]));
                list.add(rbt[0] + "_" + rbt[1]);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return list;
    }
}
