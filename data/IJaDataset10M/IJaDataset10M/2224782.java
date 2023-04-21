package com.codeinuse.roo.addon.spring.integration;

import org.springframework.roo.shell.CommandMarker;
import org.springframework.util.Assert;

public class SpringIntegrationCommands implements CommandMarker {

    private final SpringIntegrationOperations operations;

    public SpringIntegrationCommands(SpringIntegrationOperations operations) {
        Assert.notNull(operations, "SpringIntegrationOperations required");
        this.operations = operations;
    }
}
