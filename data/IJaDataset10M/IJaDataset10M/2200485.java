package org.minions.stigma.databases.server;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.MessageFormat;
import java.util.LinkedList;
import java.util.List;
import org.minions.stigma.databases.sql.SqlAsyncDB;
import org.minions.stigma.databases.sql.SqlReceiver;
import org.minions.stigma.game.actor.Actor;
import org.minions.stigma.server.managers.ActorManager;
import org.minions.utils.logger.Log;

/**
 * Class used for saving players' actors' in SQL database.
 * Uses global SQL connection.
 * @see SqlAsyncDB
 */
public class ActorSaveDB implements SqlReceiver {

    private PreparedStatement actorFKStatement;

    private PreparedStatement actorSavingStatement;

    private SaveReporter saveReporter = new SaveReporter();

    /**
     * Constructor. Global instance of {@link SqlAsyncDB}
     * must exists. Allocates some some resources (like
     * prepared statements).
     */
    public ActorSaveDB() {
        SqlAsyncDB db = SqlAsyncDB.globalInstance();
        assert db != null;
        actorSavingStatement = db.prepareStatement("REPLACE INTO `Avatars` (`Avatar_id`,`Account_id`, `Name`, `Level`, `Experience`, `Map_Id`, `X`, `Y`, `Strength`, `Willpower`, `Agility`, `Finesse`, `Actual_Health`, `Actual_Stamina`) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
        actorFKStatement = db.prepareStatement("SELECT Accout_id FROM Avatars WHERE Avatar_id=?");
    }

    /**
     * Frees resources allocated by this object. After
     * calling this function this object is no more usable.
     */
    public void freeResources() {
        try {
            actorSavingStatement.close();
        } catch (SQLException e) {
            Log.logger.error(e);
        }
    }

    /**
     * Saves in SQL database actor managed by given manager.
     * @param actor
     *            manager containing actor to be saved
     */
    public void saveActor(ActorManager actor) {
        SqlAsyncDB db = SqlAsyncDB.globalInstance();
        assert db != null;
        List<Object> list = new LinkedList<Object>();
        list.add(actor.getActor().getId());
        db.executeSQL(actorFKStatement, list, this, actor);
    }

    /** {@inheritDoc} */
    @Override
    public void processSqlResult(ResultSet resultSet, boolean success, Object requestSpecificObject) {
        assert requestSpecificObject instanceof ActorManager;
        ActorManager actorM = (ActorManager) requestSpecificObject;
        Actor actor = actorM.getActor();
        try {
            if (!success || resultSet == null || !resultSet.next()) {
                Log.logger.error(MessageFormat.format("Actor save failed for: {0} {1}. Possibly no account.", actor.getId(), actor.getName()));
                return;
            }
            int account_id = resultSet.getInt("Account_id");
            List<Object> list = new LinkedList<Object>();
            list.add(actor.getId());
            list.add(account_id);
            list.add(actor.getName());
            list.add(actor.getLevel());
            list.add(actor.getExperience());
            list.add(actor.getMapId());
            list.add(actor.getPosition().getX());
            list.add(actor.getPosition().getY());
            list.add(actor.getStrength());
            list.add(actor.getWillpower());
            list.add(actor.getAgility());
            list.add(actor.getFinesse());
            list.add(actor.getActualHealth());
            list.add(actor.getActualStamina());
            SqlAsyncDB db = SqlAsyncDB.globalInstance();
            assert db != null;
            db.executeSQL(actorSavingStatement, list, saveReporter, actorM);
        } catch (SQLException e) {
            Log.logger.error(e);
            return;
        }
    }

    private static class SaveReporter implements SqlReceiver {

        @Override
        public void processSqlResult(ResultSet resultSet, boolean success, Object requestSpecificObject) {
            assert requestSpecificObject instanceof ActorManager;
            ActorManager actor = (ActorManager) requestSpecificObject;
            if (!success) {
                Log.logger.error("Actor save failed for: " + actor.getActor().getId() + " " + actor.getActor().getName());
                return;
            }
        }
    }
}
