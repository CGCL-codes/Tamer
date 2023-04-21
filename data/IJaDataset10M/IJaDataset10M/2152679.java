package org.eclipse.emf.mapping.command;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.edit.command.CommandParameter;
import org.eclipse.emf.mapping.Mapping;
import org.eclipse.emf.mapping.domain.MappingDomain;

public class NameMatchMappingCommand extends MatchMappingCommand {

    /**
   * This creates a command that creates a new child mappings for the given mapping 
   * by attempting to match by name input children with output children.
   */
    public static Command create(MappingDomain domain, Mapping mapping) {
        return domain.createCommand(NameMatchMappingCommand.class, new CommandParameter(mapping));
    }

    public NameMatchMappingCommand(MappingDomain domain, Mapping mapping) {
        super(domain, mapping);
    }

    @Override
    protected boolean match(Object inputObject, Object outputObject, Collection<Object> mappedObjects) {
        String inputName = domain.getName(inputObject);
        String outputName = domain.getName(outputObject);
        if (inputName != null && outputName != null) {
            List<String> parsedInputName = domain.parseInputName(inputName);
            List<String> parsedOutputName = domain.parseOutputName(outputName);
            if (concatName(parsedInputName).equalsIgnoreCase(concatName(parsedOutputName))) {
                mappedObjects.add(inputObject);
            }
        }
        boolean multipleMatchesAllowed = (domain.getMappingEnablementFlags() & MappingDomain.ENABLE_MULTIPLE_INPUTS) != 0;
        return !multipleMatchesAllowed;
    }

    protected String concatName(List<String> parsedName) {
        StringBuilder result = new StringBuilder();
        for (String nameComponent : parsedName) {
            result.append(nameComponent);
        }
        return result.toString();
    }

    @Override
    public void execute() {
        super.execute();
        for (Command command : new ArrayList<Command>(commandList)) {
            appendAndExecute(NameMatchMappingCommand.create(domain, (Mapping) command.getResult().iterator().next()));
        }
    }
}
