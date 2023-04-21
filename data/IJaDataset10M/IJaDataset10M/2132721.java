package com.kenstevens.stratdom.site.action;

import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import com.kenstevens.stratdom.main.Spring;
import com.kenstevens.stratdom.site.Command;
import com.kenstevens.stratdom.site.command.PlayersOnCommand;
import com.kenstevens.stratdom.site.parser.PlayersOnPageParser;

@Scope("prototype")
@Component
public class PlayersOnAction extends Action {

    @Autowired
    private PlayersOnPageParser playersOnPageParser;

    @Autowired
    Spring spring;

    private PlayersOnCommand playersOnCommand;

    @SuppressWarnings("unused")
    @PostConstruct
    private void initialize() {
        playersOnCommand = spring.getBean(PlayersOnCommand.class);
        addParser(playersOnPageParser);
    }

    @Override
    public Command getCommand() {
        return playersOnCommand;
    }
}
