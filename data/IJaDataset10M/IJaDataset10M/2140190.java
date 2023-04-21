package ca.qc.adinfo.rouge.command;

import java.util.HashMap;
import org.apache.log4j.Logger;
import ca.qc.adinfo.rouge.data.RougeObject;
import ca.qc.adinfo.rouge.room.RoomManager;
import ca.qc.adinfo.rouge.server.DBManager;
import ca.qc.adinfo.rouge.server.core.SessionContext;
import ca.qc.adinfo.rouge.user.User;
import ca.qc.adinfo.rouge.user.UserManager;

public class RougeCommandProcessor {

    private static Logger log = Logger.getLogger(RougeCommandProcessor.class);

    private HashMap<String, RougeCommand> commands;

    private HashMap<String, RougeCommand> anonymousCommands;

    private DBManager dbManager;

    private UserManager userManager;

    private RoomManager roomManager;

    public RougeCommandProcessor(DBManager dbManager, UserManager userManager, RoomManager roomManager) {
        this.commands = new HashMap<String, RougeCommand>();
        this.anonymousCommands = new HashMap<String, RougeCommand>();
        this.dbManager = dbManager;
        this.userManager = userManager;
        this.roomManager = roomManager;
    }

    public void processCommand(boolean anonymous, String key, RougeObject data, SessionContext session, User user) {
        RougeCommand command = null;
        if (anonymous) {
            synchronized (this.anonymousCommands) {
                command = this.anonymousCommands.get(key);
            }
        } else {
            synchronized (this.commands) {
                command = this.commands.get(key);
            }
        }
        if (command != null) {
            try {
                log.debug("Executing command: " + key);
                command.execute(data, session, user);
            } catch (Exception e) {
                log.error("Error processing command: " + e.getClass());
            }
        } else {
            log.debug("Unknown command: " + key);
        }
        return;
    }

    public void registerCommand(RougeCommand command, boolean anonymous) {
        if (anonymous) {
            synchronized (this.anonymousCommands) {
                this.anonymousCommands.put(command.getKey(), command);
            }
        } else {
            synchronized (this.commands) {
                this.commands.put(command.getKey(), command);
            }
        }
        command.setCommandProcessor(this);
        log.trace("Registered command: " + command.getKey());
    }

    public void unregisterCommand(RougeCommand command, boolean anonymous) {
        if (anonymous) {
            synchronized (this.anonymousCommands) {
                this.anonymousCommands.remove(command.getKey());
            }
        } else {
            synchronized (this.commands) {
                this.commands.remove(command.getKey());
            }
        }
        log.trace("Unregistered command: " + command.getKey());
    }

    public void send(String key, RougeObject object) {
        log.trace("Send method not implemented yet.");
    }

    public DBManager getDBManager() {
        return this.dbManager;
    }

    public UserManager userManager() {
        return this.userManager;
    }

    public RoomManager roomManager() {
        return this.roomManager;
    }
}
