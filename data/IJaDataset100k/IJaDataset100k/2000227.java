package com.google.gwt.activity.shared;

import com.google.gwt.place.shared.Place;

/**
 * Wraps an activity mapper and applies a filter to the place objects that it
 * sees.
 */
public class FilteredActivityMapper implements ActivityMapper {

    /**
   * Implemented by objects that want to interpret one place as another.
   */
    public interface Filter {

        /**
     * Returns the filtered interpretation of the given {@link Place}.
     *
     * @param place the input {@link Place}.
     * @return the output {@link Place}.
     */
        Place filter(Place place);
    }

    private final Filter filter;

    private final ActivityMapper wrapped;

    /**
   * Constructs a FilteredActivityMapper object.
   *
   * @param filter a Filter object
   * @param wrapped an ActivityMapper object
   */
    public FilteredActivityMapper(Filter filter, ActivityMapper wrapped) {
        this.filter = filter;
        this.wrapped = wrapped;
    }

    public Activity getActivity(Place place) {
        return wrapped.getActivity(filter.filter(place));
    }
}
