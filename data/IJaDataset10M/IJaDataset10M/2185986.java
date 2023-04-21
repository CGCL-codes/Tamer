package org.gamegineer.table.internal.net.node;

import java.util.Collection;
import java.util.Collections;
import java.util.concurrent.Future;
import net.jcip.annotations.Immutable;
import org.gamegineer.common.core.util.concurrent.SynchronousFuture;
import org.gamegineer.table.net.IPlayer;
import org.gamegineer.table.net.ITableNetworkConfiguration;

/**
 * Fake implementation of {@link INodeController}.
 */
@Immutable
public final class FakeNodeController implements INodeController {

    /**
     * Initializes a new instance of the {@code FakeNodeController} class.
     */
    public FakeNodeController() {
    }

    @Override
    public Future<Void> beginConnect(@SuppressWarnings("unused") final ITableNetworkConfiguration configuration) {
        return new SynchronousFuture<Void>();
    }

    @Override
    public Future<Void> beginDisconnect() {
        return new SynchronousFuture<Void>();
    }

    @Override
    public void cancelControlRequest() {
    }

    @Override
    public void endConnect(@SuppressWarnings("unused") final Future<Void> future) {
    }

    @Override
    public void endDisconnect(@SuppressWarnings("unused") final Future<Void> future) {
    }

    @Override
    public IPlayer getPlayer() {
        return null;
    }

    @Override
    public Collection<IPlayer> getPlayers() {
        return Collections.emptyList();
    }

    @Override
    public void giveControl(@SuppressWarnings("unused") final String playerName) {
    }

    @Override
    public void requestControl() {
    }
}
