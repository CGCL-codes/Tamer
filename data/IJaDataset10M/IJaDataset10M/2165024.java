package com.mattgarner.jaddas.node.command;

import com.mattgarner.jaddas.node.RemoteClient;
import com.mattgarner.jaddas.node.command.ddl.CmdAlterDatasetAddIndex;
import com.mattgarner.jaddas.node.command.ddl.CmdCreateDataset;
import com.mattgarner.jaddas.node.command.ddl.CmdDropDataset;
import com.mattgarner.jaddas.node.command.dml.CmdBulkLoader;
import com.mattgarner.jaddas.node.command.dml.CmdInsertInto;
import com.mattgarner.jaddas.node.command.dml.CmdSelect;
import com.mattgarner.jaddas.node.command.state.CmdSetConnection;
import com.mattgarner.jaddas.node.command.state.CmdShow;
import com.mattgarner.jaddas.node.net.Message;
import com.mattgarner.jaddas.node.net.Protocol;
import com.mattgarner.jaddas.node.net.Worker;

public class CommandParserClient extends CommandParser {

    public CommandParserClient(Worker serverWorker, RemoteClient client) {
        super(serverWorker, client);
    }

    public final boolean processCommand(Message message) {
        Worker worker = getServerWorker();
        RemoteClient clientConfig = getRemoteClient();
        boolean cmdMatched = false;
        if (message.getMessageFlag() == Protocol.MSG_COMMAND) {
            String command = preprocessCommandString(message.getMessageString());
            if (command.toUpperCase().matches("^SET\\s*CONNECTION\\s.*")) {
                CmdSetConnection cmd = new CmdSetConnection(worker, clientConfig);
                cmdMatched = cmd.parseCommand(command);
                if (cmdMatched) {
                    return true;
                }
            }
            if (command.toUpperCase().matches("^SHOW\\s.*")) {
                CmdShow cmd = new CmdShow(worker, clientConfig);
                cmdMatched = cmd.parseCommand(command);
                if (cmdMatched) {
                    return true;
                }
            }
            if (command.toUpperCase().matches("^CREATE\\s*DATASET\\s.*")) {
                CmdCreateDataset cmd = new CmdCreateDataset(worker, clientConfig);
                cmdMatched = cmd.parseCommand(command);
                if (cmdMatched) {
                    return true;
                }
            }
            if (command.toUpperCase().matches("^DROP\\s+DATASET\\s.*")) {
                CmdDropDataset cmd = new CmdDropDataset(worker, clientConfig);
                cmdMatched = cmd.parseCommand(command);
                if (cmdMatched) {
                    return true;
                }
            }
            if (command.toUpperCase().matches("^INSERT\\s+INTO\\s.*")) {
                CmdInsertInto cmd = new CmdInsertInto(worker, clientConfig);
                cmdMatched = cmd.parseCommand(command);
                if (cmdMatched) {
                    return true;
                }
            }
            if (command.toUpperCase().matches("^ALTER\\s+DATASET\\s+(.*?)\\s+ADD\\s+INDEX\\s+ON\\s+.*")) {
                CmdAlterDatasetAddIndex cmd = new CmdAlterDatasetAddIndex(worker, clientConfig);
                cmdMatched = cmd.parseCommand(command);
                if (cmdMatched) {
                    return true;
                }
            }
            if (command.toUpperCase().matches("^SELECT\\s.*")) {
                CmdSelect cmd = new CmdSelect(worker, clientConfig);
                cmdMatched = cmd.parseCommand(command);
                if (cmdMatched) {
                    return true;
                }
            }
        }
        if (message.getMessageFlag() == Protocol.MSG_BULKLOAD_ROWS) {
            CmdBulkLoader cmd = new CmdBulkLoader(worker, clientConfig);
            cmdMatched = cmd.processCommand(message);
            if (cmdMatched) {
                return true;
            }
        }
        worker.sendMessage(Protocol.MSG_NAK, "Command not recognized.");
        return false;
    }
}
