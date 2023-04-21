package org.nomicron.suber.model.dao;

import org.nomicron.suber.model.bean.PointType;
import org.nomicron.suber.model.object.Player;
import org.nomicron.suber.model.object.PointEvent;
import com.dreamlizard.miles.interfaces.Dao;
import java.util.List;

/**
 * Data access object interface for PointEvent.
 */
public interface PointEventDao extends Dao {

    /**
     * Get a new instance of a PointEvent.
     *
     * @return new PointEvent object
     */
    PointEvent create();

    /**
     * Store the specified PointEvent to the persistence layer.
     *
     * @param object PointEvent to store
     */
    void store(PointEvent object);

    /**
     * Find the PointEvent with the specified id.
     *
     * @param id id to look for
     * @return matching PointEvent
     */
    PointEvent findById(Integer id);

    /**
     * Find the list of valid (excluding DELETED) point events for the specified player and point type.
     *
     * @param player    Player to get points for
     * @param pointType PointType
     * @return List of PointEvents
     */
    List<PointEvent> findValidByPlayerAndPointType(Player player, PointType pointType);

    /**
     * Find the list of point events for the specified player, point type and status.
     *
     * @param player           Player to get points for
     * @param pointType        PointType
     * @param pointEventStatus PointEventStatus
     * @return List of PointEvents
     */
    List<PointEvent> findByPlayerPointTypeAndStatus(Player player, PointType pointType, PointEvent.PointEventStatus pointEventStatus);

    /**
     * Find the list of PointEvents for the specified Player and PointEventStatus.
     *
     * @param player           Player to get points for
     * @param pointEventStatus PointEventStatus
     * @return List of PointEvents
     */
    List<PointEvent> findByPlayerAndStatus(Player player, PointEvent.PointEventStatus pointEventStatus);
}
