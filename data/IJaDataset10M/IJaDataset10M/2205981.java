package net.raymanoz.command;

import net.raymanoz.migrate.Script;
import net.raymanoz.migrate.ScriptList;

public class ListCommand implements Command {

    private static final String COMMAND_STRING = "list";

    private CommandAssembler assembler;

    public ListCommand(CommandAssembler assembler) {
        this.assembler = assembler;
    }

    public void execute(String[] args) {
        execute();
    }

    public void execute() {
        ScriptList scriptList = getScriptList();
        if (scriptList.size() == 0) {
            System.out.println("No migrate actions found");
        } else {
            for (Script script : scriptList) {
                System.out.println(script.getPatch() + " - " + script.getFileName());
            }
        }
    }

    protected ScriptList getScriptList() {
        return assembler.buildScriptList(1);
    }

    public String getCommandString() {
        return COMMAND_STRING;
    }

    @Override
    public String[] helpMessage() {
        return null;
    }
}
