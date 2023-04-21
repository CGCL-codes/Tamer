package com.kenstevens.stratdom.site.command;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import com.kenstevens.stratdom.site.Command;
import com.kenstevens.stratdom.site.CommandProcessor;
import com.kenstevens.stratdom.site.StratSite;
import com.meterware.httpunit.WebForm;
import com.meterware.httpunit.WebResponse;

@Scope("prototype")
@Component
public class PlayersOnCommand extends Command {

    @Autowired
    StratSite stratSite;

    @Autowired
    CommandProcessor commandProcessor;

    @Override
    public WebResponse execute() throws Exception {
        WebForm mainform = stratSite.getMainform();
        setParameter(mainform, FORM_COMMAND, FORM_COMMAND_VALUE_PLAYERS_ON);
        mainform.submit();
        return stratSite.getCurrentPage();
    }

    @Override
    public String getDescription() {
        return "Get players on";
    }
}
