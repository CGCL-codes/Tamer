package ru.yermak.bookkeeping.operations;

import org.springframework.context.annotation.Scope;
import ru.yermak.bookkeeping.common.Command;
import java.awt.event.ActionEvent;

/**
 * User: harrier
 * Date: Apr 13, 2009
 */
@Scope("action")
@org.springframework.stereotype.Component
public class LossEditCommand implements Command {

    @Override
    public void execute(ActionEvent e) {
    }
}
