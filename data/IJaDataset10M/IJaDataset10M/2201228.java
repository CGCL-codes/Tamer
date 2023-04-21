package ch.gmtech.lab.gui;

import ch.gmtech.lab.domain.Command;

public interface CommandInvoker {

    void call(Command aCommand);
}
