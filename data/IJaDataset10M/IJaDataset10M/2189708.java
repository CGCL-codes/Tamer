package net.sf.gratepic.gui;

import flickr.response.Activity;
import flickr.response.Event;
import flickr.response.Item;
import flickr.response.Items;
import flickr.service.Flickr;
import flickr.service.FlickrResponseHandler;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import org.netbeans.api.progress.ProgressHandle;
import org.netbeans.api.progress.ProgressHandleFactory;
import org.openide.util.NbBundle;

/**
 * @author leon
 */
public class RecentActivityNavigation implements PagedNavigation {

    public static final String PROP_ITEMS = "items";

    private String period = "2d";

    private Items items;

    private PropertyChangeSupport propertyChangeSupport = new PropertyChangeSupport(this);

    public int getPage() {
        int page = items.getPage();
        int pages = items.getPages();
        return page > pages ? pages : page;
    }

    public void setPeriod(String period) {
        this.period = period;
    }

    public int getTotalPages() {
        return items.getPages();
    }

    public void next() {
    }

    public void nextPage() {
        loadPage(items.getPage() + 1);
    }

    public boolean hasNextPage() {
        return items.hasNextPage();
    }

    public boolean hasNext() {
        return false;
    }

    public boolean hasPrevious() {
        return false;
    }

    public boolean hasPreviousPage() {
        return items.hasPreviousPage();
    }

    public void previous() {
    }

    public void previousPage() {
        loadPage(getPage() - 1);
    }

    public void loadPage(int page) {
        final ProgressHandle progressHandle = ProgressHandleFactory.createHandle(NbBundle.getMessage(RecentActivityNavigation.class, "loadingActivity"));
        Flickr.get().getRecentActivityService().loadRecentActivityItems(period, 5, page, new FlickrResponseHandler<Items>() {

            public void beforeStart() {
                progressHandle.start();
            }

            public void callFinished(Items items) {
                Items oldItems = RecentActivityNavigation.this.items;
                RecentActivityNavigation.this.items = items;
                propertyChangeSupport.firePropertyChange(PROP_ITEMS, oldItems, items);
            }

            public void callFailed(Exception exception) {
                FlickrErrorHandler.handle(NbBundle.getMessage(RecentActivityNavigation.class, "couldNotLoadRecentActivity"), exception);
            }

            public void afterFinish() {
                progressHandle.finish();
            }
        });
    }

    public synchronized void removePropertyChangeListener(String propertyName, PropertyChangeListener listener) {
        propertyChangeSupport.removePropertyChangeListener(propertyName, listener);
    }

    public synchronized void addPropertyChangeListener(String propertyName, PropertyChangeListener listener) {
        propertyChangeSupport.addPropertyChangeListener(propertyName, listener);
    }

    public List<ActivityItem> getActivityItems() {
        List<ActivityItem> l = new ArrayList<ActivityItem>();
        for (Item item : items.getItems()) {
            Activity activity = item.getActivity();
            List<Event> events = activity.getEvents();
            Collections.sort(events, new Comparator<Event>() {

                public int compare(Event o1, Event o2) {
                    long l = o2.getDateAdded() - o1.getDateAdded();
                    if (l < 0) {
                        return -1;
                    } else if (l > 0) {
                        return 1;
                    } else {
                        return 0;
                    }
                }
            });
            for (Event event : events) {
                l.add(new ActivityItem(item, activity, event));
            }
        }
        return l;
    }

    public static class ActivityItem {

        private Activity activity;

        private Item item;

        private Event event;

        public ActivityItem(Item item, Activity activity, Event event) {
            this.item = item;
            this.event = event;
            this.activity = activity;
        }

        public Event getEvent() {
            return event;
        }

        public Item getItem() {
            return item;
        }

        public Activity getActivity() {
            return activity;
        }
    }
}
