package com.isdinvestments.cam.infrastructure.repository;

import java.util.List;
import java.util.Map;
import com.isdinvestments.cam.domain.model.Account;
import com.isdinvestments.cam.domain.model.Asset;
import com.isdinvestments.cam.domain.model.AssetClassification;
import com.isdinvestments.cam.domain.model.AssetSubClassification;
import com.isdinvestments.cam.domain.model.Customer;
import com.isdinvestments.cam.domain.model.Event;
import com.isdinvestments.cam.domain.model.EventSubType;
import com.isdinvestments.cam.domain.model.EventType;
import com.isdinvestments.cam.domain.model.Portfolio;
import com.isdinvestments.cam.domain.model.Position;
import com.isdinvestments.cam.domain.model.PositionSnapshot;
import com.isdinvestments.cam.domain.model.PositionSnapshotPK;
import com.isdinvestments.cam.domain.model.PositionTransaction;
import com.isdinvestments.cam.domain.model.SubAccount;
import com.isdinvestments.cam.domain.model.SubAccountPK;

/**
 * Portfolio Repository interface.
 */
public interface PortfolioRepository {

    /**
	 * Find an entity by its id (primary key).
	 * @return The found entity instance or null if the entity does not exist.
	 */
    public Portfolio findPortfolioById(long id);

    /**
	 * Return all persistent instances of the <code>Portfolio</code> entity.
	 */
    public List<Portfolio> findAllPortfolios();

    /**
	 * Make the given instance managed and persistent.
	 */
    public void persistPortfolio(Portfolio portfolio);

    /**
	 * Remove the given persistent instance.
	 */
    public void removePortfolio(Portfolio portfolio);

    /**
	 * Find an entity by its id (primary key).
	 * @return The found entity instance or null if the entity does not exist.
	 */
    public Position findPositionById(long id);

    /**
	 * Return all persistent instances of the <code>Position</code> entity.
	 */
    public List<Position> findAllPositions();

    /**
	 * Make the given instance managed and persistent.
	 */
    public void persistPosition(Position position);

    /**
	 * Remove the given persistent instance.
	 */
    public void removePosition(Position position);

    /**
	 * Find an entity by its id (primary key).
	 * @return The found entity instance or null if the entity does not exist.
	 */
    public PositionSnapshot findPositionSnapshotById(com.isdinvestments.cam.domain.model.PositionSnapshotPK id);

    /**
	 * Return all persistent instances of the <code>PositionSnapshot</code> entity.
	 */
    public List<PositionSnapshot> findAllPositionSnapshots();

    /**
	 * Make the given instance managed and persistent.
	 */
    public void persistPositionSnapshot(PositionSnapshot positionSnapshot);

    /**
	 * Remove the given persistent instance.
	 */
    public void removePositionSnapshot(PositionSnapshot positionSnapshot);

    /**
	 * Find an entity by its id (primary key).
	 * @return The found entity instance or null if the entity does not exist.
	 */
    public PositionSnapshotPK findPositionSnapshotPKById(long id);

    /**
	 * Return all persistent instances of the <code>PositionSnapshotPK</code> entity.
	 */
    public List<PositionSnapshotPK> findAllPositionSnapshotPKs();

    /**
	 * Make the given instance managed and persistent.
	 */
    public void persistPositionSnapshotPK(PositionSnapshotPK positionSnapshotPK);

    /**
	 * Remove the given persistent instance.
	 */
    public void removePositionSnapshotPK(PositionSnapshotPK positionSnapshotPK);

    /**
	 * Find an entity by its id (primary key).
	 * @return The found entity instance or null if the entity does not exist.
	 */
    public PositionTransaction findPositionTransactionById(long id);

    /**
	 * Return all persistent instances of the <code>PositionTransaction</code> entity.
	 */
    public List<PositionTransaction> findAllPositionTransactions();

    /**
	 * Make the given instance managed and persistent.
	 */
    public void persistPositionTransaction(PositionTransaction positionTransaction);

    /**
	 * Remove the given persistent instance.
	 */
    public void removePositionTransaction(PositionTransaction positionTransaction);
}
