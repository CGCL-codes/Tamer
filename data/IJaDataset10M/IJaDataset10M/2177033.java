package com.mysolution.core.scenario.commands.impl;

import com.mysolution.core.scenario.commands.AbstractCommand;
import com.mysolution.core.scenario.commands.CommandException;
import com.mysolution.core.scenario.commands.CommandType;
import com.mysolution.core.scenario.ScenarioWorkflow;
import com.mysolution.core.resources.resourcesImpl.ActionResource;
import com.mysolution.core.exprinterpretator.Interpretator;
import com.mysolution.persistence.domain.Action;
import com.mysolution.utils.ImageUtils;
import com.mysolution.Constants;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlImage;
import com.gargoylesoftware.htmlunit.html.FrameWindow;
import javax.imageio.ImageReader;
import java.util.Stack;
import java.util.List;
import org.apache.commons.lang.StringUtils;

/**
 * dku
 */
@CommandType(type = Action.Type.ACTION, command = Action.Command.COMPUTE_EXPRESSION)
public class ComputeValueCommand extends AbstractCommand {

    public static final Action.Type type = Action.Type.ACTION;

    public static final Action.Command command = Action.Command.COMPUTE_EXPRESSION;

    public Boolean execute(ScenarioWorkflow workflow) throws CommandException {
        try {
            ActionResource actionResource = workflow.getActionResource();
            Stack<HtmlPage> pageStack = workflow.getHistory();
            if (actionResource != null && pageStack != null && !pageStack.isEmpty()) {
                HtmlPage activePage = pageStack.peek();
                Action action = actionResource.getDomain();
                HtmlPage resultPage = null;
                String value = action.getValue();
                List<HtmlElement> elList = (List<HtmlElement>) activePage.getByXPath(action.getXpath());
                if (elList != null && elList.size() > 0) {
                    HtmlElement element = elList.get(0);
                    String expr = element.getTextContent();
                    long computedValue = new Interpretator().compute(expr);
                    logger.info("Bot protection: " + expr + " " + computedValue);
                    workflow.setPropertyValue(Constants.RECOGNIZED_VALUE_PROPERTY, computedValue);
                }
            }
        } catch (Exception e) {
            throw new CommandException(this, e);
        }
        return true;
    }

    public Action.Type getType() {
        return type;
    }

    public Action.Command getCommand() {
        return command;
    }
}
