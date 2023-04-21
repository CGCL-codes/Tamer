package com.mysolution.core.scenario.commands.impl;

import com.mysolution.persistence.domain.Action;
import com.mysolution.core.scenario.ScenarioWorkflow;
import com.mysolution.core.scenario.commands.AbstractCommand;
import com.mysolution.core.scenario.commands.CommandException;
import com.mysolution.core.scenario.commands.CommandType;
import com.mysolution.core.resources.resourcesImpl.ActionResource;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import java.util.Stack;
import java.util.List;

/**
 * dku
 */
@CommandType(type = Action.Type.VALIDATE, command = Action.Command.WHILE_IS_TRUE)
public class WhileIsTrueCommand extends AbstractCommand {

    public static final Action.Type type = Action.Type.VALIDATE;

    public static final Action.Command command = Action.Command.WHILE_IS_TRUE;

    public Boolean execute(ScenarioWorkflow workflow) throws CommandException {
        Boolean rez = false;
        try {
            ActionResource actionResource = workflow.getActionResource();
            Stack<HtmlPage> pageStack = workflow.getHistory();
            if (actionResource != null && !pageStack.isEmpty()) {
                HtmlPage activePage = pageStack.peek();
                Action action = actionResource.getDomain();
                Object data = activePage.getFirstByXPath(action.getXpath());
                if (data instanceof Boolean) rez = (Boolean) data;
                logger.info(action.getName() + ": found data = " + data);
            }
        } catch (Exception e) {
            throw new CommandException(this, e);
        }
        return rez;
    }

    public Action.Type getType() {
        return type;
    }

    public Action.Command getCommand() {
        return command;
    }
}
