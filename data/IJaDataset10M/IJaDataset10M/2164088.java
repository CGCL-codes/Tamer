package org.skylight1.neny.server;

import static java.lang.String.format;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.logging.Logger;

import org.skylight1.neny.model.Restaurant;

public class SingleFileInspectionResultsFetcherTest {
	private static final Logger LOGGER = Logger.getLogger(SingleFileInspectionResultsFetcherTest.class.getName());

	/**
	 * @param args
	 * @throws IOException
	 * @throws ParseException
	 */
	public static void main(String[] args) throws IOException, ParseException {
		final SingleFileInspectionResultsFetcher fetcher = new SingleFileInspectionResultsFetcher();

		// use the fetcher to retrieve the restaurants according to the new file
		final Map<String, Restaurant> newRestaurants =
				fetcher.processFile("file:///C:\\Users\\Timothy\\Downloads\\dohmh_restaurant-inspections_002 (1).zip", new Date());
		// final Map<String, Restaurant> newRestaurants =
		// fetcher.processFile("http://nycopendata.socrata.com/download/4vkw-7nck/ZIP");

		LOGGER.info(format("new %d", newRestaurants.size()));

		final SortedSet<Restaurant> orderedRestaurants = new TreeSet<>(new Comparator<Restaurant>() {
			@Override
			public int compare(Restaurant aO1, Restaurant aO2) {
				final Date date1 = aO1.getInpectionDate();
				final Date date2 = aO2.getInpectionDate();
				final int dateComparison;
				if (date1 == null && date2 == null) {
					dateComparison = 0;
				} else if (date2 == null) {
					dateComparison = 1;
				} else {
					dateComparison = date1.compareTo(date2);
				}
				return dateComparison != 0 ? dateComparison : compareCamis(aO1, aO2);
			}

			private int compareCamis(Restaurant aO1, Restaurant aO2) {
				return aO1.getCamis().compareTo(aO2.getCamis());
			}
		});

		orderedRestaurants.addAll(newRestaurants.values());

		final Map<String, Integer> distribution = new HashMap<String, Integer>();
		for (final Restaurant r : orderedRestaurants) {
			final Integer currentCount;
			if (distribution.containsKey(r.getCuisineCode())) {
				currentCount = distribution.get(r.getCuisineCode());
			} else {
				currentCount = 0;
			}
			distribution.put(r.getCuisineCode(), currentCount + 1);
		}
		LOGGER.info(distribution.toString());

		for (final Restaurant r : new ArrayList<Restaurant>(orderedRestaurants).subList(orderedRestaurants.size() - 10, orderedRestaurants.size())) {
			LOGGER.info(r.toString());
		}
	}
}
