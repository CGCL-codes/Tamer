package com.acv.dao.catalog.categories.common;

import java.util.List;
import com.acv.dao.catalog.Browsable;
import com.acv.dao.catalog.categories.common.model.Category;
import com.acv.dao.catalog.categories.common.model.DestinationGroup;
import com.acv.dao.catalog.categories.destinations.model.Destination;
import com.acv.dao.catalog.categories.facilities.model.Facility;
import com.acv.dao.catalog.categories.interests.model.Interest;
import com.acv.dao.catalog.categories.promotions.model.Promotion;
import com.acv.dao.catalog.categories.ratings.model.Rating;
import com.acv.dao.catalog.categories.top5.model.Top5;
import com.acv.dao.common.Dao;

/**
 * Interface for the hibernate object in charge of retrieving categories related content from the database.
 *
 * @author Mickael Guesnon
 */
public interface CategoryDao extends Dao {

    /**
	 * Fetch a specific {@link Category} from the database.
	 *
	 * @param id The id of the {@link Category} to fetch.
	 *
	 * @return The requested {@link Category}.
	 * <code>null</code> If no {@link Category} can be found or if the display dates of fetched {@link Category} doesn't match with the current browsing date.
	 */
    public Category getCategory(Long id);

    /**
	 * Fetch a specific {@link Category} from the database.
	 *
	 * @param code The code of the {@link Category} to fetch.
	 *
	 * @return The requested {@link Category}.
	 * <code>null</code> If no {@link Category} can be found or if the display dates of fetched {@link Category} doesn't match with the current browsing date.
	 */
    public Category getCategoryByCode(String code);

    /**
	 * Retrieve the whole {@link List} of {@link Category} objects from the CATEGORIES table.
	 *
	 * @return The {@link List} of {@link Category} objects matching the current browsing date.
	 */
    public List<Category> getCategories();

    /**
	 * Fetch a {@link Browsable} from the CATEGORIES table.
	 *
	 * @param id The id of the {@link Browsable} to fetch.
	 *
	 * @return The requested {@link Browsable}.
	 * <code>null</code> If no {@link Category} can be found, if the display dates of fetched {@link Category} doesn't match with the current browsing date or if the {@link Category} found isn't an instance
	 * of {@link Browsable}.
	 */
    public Browsable getBrowsableById(Long id);

    /**
	 * Fetch the {@link List} of leaves {@link Destination} objects from the DESTINATIONS table.
	 *
	 * @return The {@link List} of leaves {@link Destination} objects matching the current browsing date.
	 */
    public List<Destination> getLeavesDestinations();

    /**
	 * Fetch a {@link List} of {@link Browsable} objects from the DESTINATION_GROUPS table.
	 *
	 * @return The whole {@link List} of {@link DestinationGroup} objects matching the current browsing date as a {@link List} of {@link Browsable} objects.
	 */
    public List<Browsable> getDestinationGroupList();

    /**
	 * Fetch a {@link List} of {@link Browsable} object from the INTERESTS table.
	 *
	 * @return The whole {@link List} of {@link Interest} objects as {@link List} of {@link Browsable} objects.
	 */
    public List<Browsable> getInterests();

    /**
	 * Fetch the {@link List} of {@link Facility} objects from the FACILITIES table.
	 *
	 * @return The {@link List} of {@link Facility} objects.
	 */
    public List<Facility> getFacilities();

    /**
	 * Fetch the {@link List} of {@link Top5} objects from the TOP5 table.
	 *
	 * @return The {@link List} of {@link Top5} objects sorted by replicant order.
	 */
    public List<Top5> getTop5();

    /**
	 * Fetch the {@link List} of {@link Browsable} objects from the RATINGS table.
	 *
	 * @return The {@link List} of {@link Rating} objects as a {@link List} of {@link Browsable} objects.
	 */
    public List<Browsable> getRatings();

    /**
	 * Fetch the {@link List} of {@link Promotion} objects from the PROMOTIONS table filtered by marketing spot code.
	 *
	 * @param marketingSpotCode The marketing spot code to filter the {@link Promotion} result {@link List}.
	 *
	 * @return The {@link List} of filtered {@link Promotion} objects matching the current browsing date.
	 */
    public List<Promotion> getPromotionsByMarketingSpotCode(String marketingSpotCode);
}
