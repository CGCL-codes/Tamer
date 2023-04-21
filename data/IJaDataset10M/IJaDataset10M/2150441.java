package ru.onlytime.tomcat.deploy.spi;

import javax.enterprise.deploy.shared.ActionType;
import javax.enterprise.deploy.shared.CommandType;
import javax.enterprise.deploy.shared.StateType;

public class DeploymentStatusImpl implements javax.enterprise.deploy.spi.status.DeploymentStatus {

    private CommandType type;

    private String message;

    private boolean isFailed = false;

    public DeploymentStatusImpl(String message, CommandType type, boolean isFailed) {
        this.message = message;
        this.type = type;
        this.isFailed = isFailed;
    }

    public ActionType getAction() {
        return ActionType.EXECUTE;
    }

    public CommandType getCommand() {
        return type;
    }

    public String getMessage() {
        return message;
    }

    public StateType getState() {
        return StateType.COMPLETED;
    }

    public boolean isCompleted() {
        return !isFailed;
    }

    public boolean isFailed() {
        return isFailed;
    }

    public boolean isRunning() {
        return false;
    }
}
