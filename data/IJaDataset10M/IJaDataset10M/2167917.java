package com.epicsagaonline.bukkit.EpicZones.CommandHandlers;

import org.bukkit.command.CommandSender;

public interface CommandHandler {

    /**
         *
         * @param command
         * @param sender
         * @param args
         * @return true if arguments are not invalid, false if usage should be printed
         */
    boolean onCommand(String command, CommandSender sender, String[] args);
}
