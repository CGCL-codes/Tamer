package dictutor;

import java.util.List;
import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListModel;
import javax.swing.ListModel;
import net.sf.dict4j.log.DictLogAppender;
import net.sf.dict4j.entity.Database;
import net.sf.dict4j.entity.Match;
import net.sf.dict4j.DictSession;
import net.sf.dict4j.entity.Definition;
import net.sf.dict4j.entity.Strategy;
import net.sf.dict4j.transport.DictTransport;
import net.sf.dict4j.transport.DictTransportDefault;
import net.sf.dict4j.cache.DictSessionWithCache;
import net.sf.dict4j.cache.MemoryCacheProvider;

public class DictutorViewBean {

    private DictLogAppender dictLogAppender;

    private DefaultListModel matchListModel = new DefaultListModel();

    private DefaultComboBoxModel databaseListModel = new DefaultComboBoxModel();

    private DefaultComboBoxModel strategyListModel = new DefaultComboBoxModel();

    private DictSession dictSession;

    public DictutorViewBean(DictLogAppender logAppender) {
        this.dictLogAppender = logAppender;
    }

    void connect(String server) {
        DictTransport transport = new DictTransportDefault(server);
        MemoryCacheProvider memoryCacheProvider = new MemoryCacheProvider();
        memoryCacheProvider.setExpirationInMilliSeconds(60000);
        memoryCacheProvider.setMaxCacheSize(100);
        dictSession = new DictSessionWithCache(transport, memoryCacheProvider);
        dictSession.setLogAppender(dictLogAppender);
        dictSession.open(getClass().getName());
    }

    void afterConnect() {
        databaseListModel.removeAllElements();
        for (final Database database : dictSession.showDatabases()) {
            databaseListModel.addElement(new DatabaseElement(database));
        }
        strategyListModel.removeAllElements();
        for (Strategy strategy : dictSession.showStrategies()) {
            strategyListModel.addElement(new StrategyElement(strategy));
        }
    }

    void disconnect() {
        dictSession.close();
        databaseListModel.removeAllElements();
        strategyListModel.removeAllElements();
    }

    List<Definition> getDefinitions(Match match) {
        return dictSession.define(match.getWord(), match.getDatabase().getName());
    }

    List<Match> loadMatches(String word, Strategy strategy, Database database) {
        matchListModel.removeAllElements();
        matchListModel.addElement("Matching ...");
        return dictSession.match(word, database, strategy);
    }

    void updateMatchListModel(List<Match> matches) {
        matchListModel.removeAllElements();
        for (Match match : matches) {
            matchListModel.addElement(match);
        }
    }

    class StrategyElement {

        Strategy strategy;

        public StrategyElement(Strategy strategy) {
            this.strategy = strategy;
        }

        @Override
        public String toString() {
            return strategy.getDescription();
        }
    }

    class DatabaseElement {

        Database database;

        public DatabaseElement(Database database) {
            this.database = database;
        }

        @Override
        public String toString() {
            return database.getDescription();
        }
    }

    DefaultListModel getMatchListModel() {
        return matchListModel;
    }

    ComboBoxModel getStrategyListModel() {
        return strategyListModel;
    }

    ComboBoxModel getDatabaseListModel() {
        return databaseListModel;
    }
}
