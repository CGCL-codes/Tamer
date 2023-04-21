package com.gencom.fun.ogame.simulator.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import com.gencom.fun.ogame.model.Player;
import com.gencom.fun.ogame.model.Universe;
import com.gencom.fun.ogame.simulator.SimulationEngine;

public class Main {

    private static final Object QUIT_COMMAND = "quit";

    private static BufferedReader reader;

    protected static Map<String, CommandExecutor> commandExecutors = new HashMap<String, CommandExecutor>();

    static {
        commandExecutors.put("printPlanets", new PrintPlanetsExecutor());
        commandExecutors.put("help", new HelpExecutor());
    }

    public static final void main(String[] args) throws IOException {
        SimulationEngine engine = new SimulationEngine(1, new Universe(10, 100, 16));
        Player player = engine.createNewPlayer("Test player");
        String command = "";
        while (!command.equals(QUIT_COMMAND)) {
            if (command.trim().length() > 0) {
                executeCommand(engine, player, command);
            }
            command = getNextCommand();
        }
    }

    private static void executeCommand(SimulationEngine engine, Player player, String command) {
        String[] commandParts = command.split(" ");
        if (commandParts.length > 0) {
            CommandExecutor executor = commandExecutors.get(commandParts[0]);
            if (executor != null) {
                executor.executeCommand(engine, player, command);
            } else {
                System.out.println("Unknown command");
            }
        }
    }

    private static String getNextCommand() throws IOException {
        System.out.print("Command -> ");
        if (reader == null) {
            Main.reader = new BufferedReader(new InputStreamReader(System.in));
        }
        String command = reader.readLine();
        return command;
    }
}
