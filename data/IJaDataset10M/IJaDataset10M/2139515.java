package com.simpleftp.ftp.server.controller;

import org.apache.log4j.Logger;
import com.simpleftp.ftp.server.FtpStateMachine;
import com.simpleftp.ftp.server.IState;
import com.simpleftp.ftp.server.command.AbstractCommand;
import com.simpleftp.ftp.server.command.ICommand;
import com.simpleftp.ftp.server.objects.FtpReply;
import com.simpleftp.ftp.server.objects.FtpUserSession;
import com.simpleftp.ftp.server.utils.FtpLogger;

/**
 * This class will have the state machine logic. The 'next_stae' of the user session
 * is a factor of 'current_stae', command executed and success status of the command.
 * Each current status and command executed will have a pair of next_stae value. ie one
 * for success of the command and one for failure. The class will use the <code>FtpStateMachine</code>
 * for determining the next state of the user session.
 * @author sajil
 * */
public class FtpCommandProcessor {

    private static Logger logger = FtpLogger.getLogger();

    public synchronized void handleCommand(ICommand command, FtpUserSession session) {
        if (command.requiresLogin()) {
            if (!session.isComplete()) {
                session.getControlConnection().scheduleSend(FtpReply.getFtpReply(530));
                if (FtpLogger.debug) {
                    logger.debug("User not logged in. Command needs a user login ");
                }
                return;
            }
        }
        IState nextState[] = null;
        try {
            if (FtpLogger.debug) {
                logger.debug("Getting the next state for the session. ");
                logger.debug("Session current state is " + session.getCurrentState().getState() + " Command is " + command);
            }
            nextState = FtpStateMachine.getInstance().getNextStates(session.getCurrentState(), ((AbstractCommand) command));
        } catch (Exception e) {
            logger.error("Error while getting the next state..." + e);
            session.getControlConnection().scheduleSend(FtpReply.getFtpReply(451));
            return;
        }
        if (nextState == null) {
            command.setCommandInSequence(false);
        }
        boolean success = command.execute(session);
        if (FtpLogger.debug) {
            logger.debug("Command executed with return value " + success);
        }
        if (!command.isCommandInSequence()) {
            if (FtpLogger.debug) {
                logger.debug("Command is not in sequence, returning without changing the session state");
            }
            return;
        }
        if (success) {
            if (FtpLogger.debug) {
                logger.debug("Command executed successfullly setting next state to " + nextState[0].getState());
            }
            session.setCurrentState(nextState[0]);
        } else {
            if (FtpLogger.debug) {
                logger.debug("execute command return false, setting next state to " + nextState[1].getState());
            }
            session.setCurrentState(nextState[1]);
        }
        session.setPreviousCommand(command);
    }
}
