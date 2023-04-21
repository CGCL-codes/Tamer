package com.kenstevens.stratdom.site.action;

import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import com.kenstevens.stratdom.main.Spring;
import com.kenstevens.stratdom.model.City;
import com.kenstevens.stratdom.site.Command;
import com.kenstevens.stratdom.site.command.NextBuildUnitCommand;

@Scope("prototype")
@Component
public class NextBuildUnitAction extends Action {

    private final City city;

    private final String choice;

    private NextBuildUnitCommand nextBuildUnitCommand;

    @Autowired
    Spring spring;

    public NextBuildUnitAction(City city, String choice) {
        this.city = city;
        this.choice = choice;
    }

    @SuppressWarnings("unused")
    @PostConstruct
    private void initialize() {
        nextBuildUnitCommand = spring.getBean(NextBuildUnitCommand.class, new Object[] { city, choice });
    }

    @Override
    public Command getCommand() {
        return nextBuildUnitCommand;
    }
}
