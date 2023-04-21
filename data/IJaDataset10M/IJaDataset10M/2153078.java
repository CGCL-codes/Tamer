package ru.nsu.ccfit.pm.econ.modules;

import ru.nsu.ccfit.pm.econ.view.shared.BuySellGateway;
import ru.nsu.ccfit.pm.econ.view.shared.ChatGateway;
import ru.nsu.ccfit.pm.econ.view.shared.CompanyRosterGateway;
import ru.nsu.ccfit.pm.econ.view.shared.EconomicActivityGateway;
import ru.nsu.ccfit.pm.econ.view.shared.GameTimeGateway;
import ru.nsu.ccfit.pm.econ.view.shared.MineStatsGateway;
import ru.nsu.ccfit.pm.econ.view.shared.PlayerRosterGateway;
import com.google.inject.AbstractModule;
import com.google.inject.Scopes;

/**
 * Binds shared views to their's gateway objects.
 * @author dragonfly
 */
class SharedGatewaysModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(EconomicActivityGateway.class).in(Scopes.SINGLETON);
        bind(GameTimeGateway.class).in(Scopes.SINGLETON);
        bind(MineStatsGateway.class).in(Scopes.SINGLETON);
        bind(PlayerRosterGateway.class).in(Scopes.SINGLETON);
        bind(ChatGateway.class).in(Scopes.SINGLETON);
        bind(CompanyRosterGateway.class).in(Scopes.SINGLETON);
        bind(BuySellGateway.class).in(Scopes.SINGLETON);
    }
}
