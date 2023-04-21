package xcordion.impl;

import xcordion.api.*;
import xcordion.api.events.BeginEvent;
import xcordion.api.events.EndEvent;
import xcordion.impl.command.NoOpCommand;

public class XcordionImpl<T extends TestElement<T>> implements Xcordion<T> {

    private CommandRepository commandRepository;

    private XcordionEventListener<T> broadcaster;

    public XcordionImpl(CommandRepository commandRepository, XcordionEventListener<T> broadcaster) {
        this.commandRepository = commandRepository;
        this.broadcaster = broadcaster;
    }

    public <C extends EvaluationContext<C>> void run(TestDocument<T> doc, C rootContext) {
        T bodyElement = doc.getRootElement().getFirstChildNamed("body");
        if (bodyElement == null) {
            bodyElement = doc.getRootElement();
        }
        for (ItemAndExpression<Pragma> pragmaAndExpression : commandRepository.pragmasForElement(bodyElement)) {
            rootContext = pragmaAndExpression.getItem().evaluate(this, bodyElement, rootContext, pragmaAndExpression.getExpression());
        }
        broadcaster.handleEvent(new BeginEvent<T>(bodyElement, rootContext.getIgnoreState()));
        ItemAndExpression<Command> commandAndExpression = commandRepository.commandForElement(bodyElement, rootContext.getIgnoreState());
        if (commandAndExpression == null) {
            new NoOpCommand().runElementAndChildren(this, bodyElement, rootContext, null);
        } else {
            commandAndExpression.getItem().runElementAndChildren(this, bodyElement, rootContext, commandAndExpression.getExpression());
        }
        broadcaster.handleEvent(new EndEvent<T>(bodyElement, rootContext.getIgnoreState()));
    }

    public CommandRepository getCommandRepository() {
        return commandRepository;
    }

    public XcordionEventListener<T> getBroadcaster() {
        return broadcaster;
    }
}
