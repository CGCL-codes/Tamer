package spamwatch.filter;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import spamwatch.base.config.FilterConfig;
import spamwatch.base.store.EMailStore;

public class FilterManager {

    private List<Filter> filters;

    private FilterConfig config;

    public FilterManager(FilterConfig config) {
        this.config = config;
        filters = new LinkedList<Filter>();
    }

    public FilterResult rateMessage(EMailStore store) {
        FilterResult result = new FilterResult(config);
        for (Filter filter : filters) {
            FilterRating rating = filter.rate(store);
            FilterConfig.FilterData data = filter.getFilterData();
            double weight = data.getWeight();
            switch(rating.getClassification()) {
                case HAM:
                    weight += data.calcHamWeight();
                    break;
                case SPAM:
                    weight += data.calcSpamWeight();
                    break;
            }
            rating.setWeight(weight);
            result.addRating(rating);
        }
        return result;
    }

    public Iterator<Filter> getFilters() {
        return filters.iterator();
    }

    /**
	 * Trains the filters and maintains the statistics 
	 * @param messages
	 * @param statusUpdater called after each learned message
	 */
    public void trainMessages(EMailStore[] messages, Runnable statusUpdater) {
        for (EMailStore store : messages) {
            for (Filter filter : filters) {
                Classification userClassification = store.getFlagClassification();
                Classification filterClassification = filter.rate(store).getClassification();
                if (filterClassification != Classification.UNKNOWN) {
                    if (filterClassification == Classification.SPAM) {
                        filter.getFilterData().setSpamCount(filter.getFilterData().getSpamCount() + 1);
                        filter.getFilterData().setSpamRight(filter.getFilterData().getSpamRight() + ((userClassification == filterClassification) ? 1 : 0));
                    } else {
                        filter.getFilterData().setHamCount(filter.getFilterData().getHamCount() + 1);
                        filter.getFilterData().setHamRight(filter.getFilterData().getHamRight() + ((userClassification == filterClassification) ? 1 : 0));
                    }
                }
                filter.learn(store);
            }
            if (statusUpdater != null) {
                statusUpdater.run();
            }
        }
        for (Filter filter : filters) {
            filter.save();
        }
        try {
            config.save();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public synchronized void readConfig() {
        FilterUtilities filterUtilities = new FilterUtilities();
        filters.clear();
        int nof = config.getNumberOfFilters();
        for (int i = 0; i < nof; i++) {
            FilterConfig.FilterData data = config.getFilterData(i);
            Filter filter = null;
            try {
                Class<?> filterClass = Class.forName(data.getClassName());
                Constructor constructor = filterClass.getConstructor(new Class[] { FilterUtilities.class });
                filter = (Filter) constructor.newInstance(new Object[] { filterUtilities });
            } catch (Exception e) {
            }
            if (filter != null) {
                filters.add(filter);
                filter.setFilterData(data);
            }
        }
    }

    public FilterConfig getFilterConfig() {
        return config;
    }
}
