package services.core.commands;

public abstract class SimpleCommand {

    public abstract SimpleCommandResponse execute(SimpleCommandRequest commandRequest);
}
