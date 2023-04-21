package com.skype.diceroller.controller.commands;

import java.util.List;
import com.skype.diceroller.controller.resources.Resource;

public abstract class Command {

    public abstract Object execute(List<Resource> resources);
}
