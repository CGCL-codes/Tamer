package com.mysolution.core.scenario.commands.impl;

import com.mysolution.core.scenario.commands.CommandType;
import com.mysolution.core.scenario.commands.AbstractCommand;
import com.mysolution.core.scenario.commands.CommandException;
import com.mysolution.core.scenario.ScenarioWorkflow;
import com.mysolution.core.resources.resourcesImpl.ActionResource;
import com.mysolution.persistence.domain.Action;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.FrameWindow;
import com.gargoylesoftware.htmlunit.ElementNotFoundException;
import java.util.Stack;
import java.util.List;

/**
 * dku
 */
@CommandType(type = Action.Type.VALIDATE, command = Action.Command.WHILE_FRAME_EXIST)
public class WhileFrameExistCommand extends AbstractCommand {

    public static final Action.Type type = Action.Type.VALIDATE;

    public static final Action.Command command = Action.Command.WHILE_FRAME_EXIST;

    public Boolean execute(ScenarioWorkflow workflow) throws CommandException {
        Boolean rez = true;
        try {
            ActionResource actionResource = workflow.getActionResource();
            Stack<HtmlPage> pageStack = workflow.getHistory();
            if (actionResource != null && !pageStack.isEmpty()) {
                HtmlPage activePage = pageStack.peek();
                Action action = actionResource.getDomain();
                try {
                    activePage.getFrameByName(action.getValue());
                    logger.info(action.getName() + ": frame found == true");
                } catch (ElementNotFoundException e) {
                    rez = false;
                    logger.info(action.getName() + ": frame found == false");
                }
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
