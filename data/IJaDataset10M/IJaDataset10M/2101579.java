package org.freelords.network.server.fake;

import org.freelords.network.command.FLNetworkCommand;
import org.freelords.network.common.FLNetworkConnection;
import org.freelords.network.fake.FakeNetworkCommand;
import org.freelords.network.server.Updater;
import org.freelords.network.server.service.Service;

/** Fake service that only accepts FakeNetworkCommands. */
public class FakeService implements Service {

    public boolean accept = true;

    public boolean throwException = false;

    public boolean executed = false;

    @Override
    public boolean accept(FLNetworkCommand command) {
        if (!accept) {
            return false;
        }
        if (command instanceof FakeNetworkCommand) {
            return true;
        }
        return false;
    }

    @Override
    public void execute(FLNetworkCommand command, FLNetworkConnection connection, Updater updater) {
        if (throwException) {
            throw new IllegalArgumentException("Fake Service set to throwing an exception.");
        }
        executed = true;
    }
}

;
