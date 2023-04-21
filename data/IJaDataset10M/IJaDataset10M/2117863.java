package at.rc.tacos.web.container;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;
import at.rc.tacos.platform.model.Location;

/**
 * 
 * @author Birgit
 * @version 1.0
 */
public class JournalContainerListContainer {

    private List<JournalContainer> journalContainerList;

    private SortedMap<Location, List<JournalContainer>> journalContainerMap;

    public JournalContainerListContainer(List<JournalContainer> journalContainerList) {
        this.journalContainerList = journalContainerList;
    }

    public List<JournalContainer> getJournalContainerList() {
        return journalContainerList;
    }

    public void setJournalList(List<JournalContainer> journalContainerList) {
        this.journalContainerList = journalContainerList;
    }

    public SortedMap<Location, List<JournalContainer>> getJournalContainerMap() {
        return journalContainerMap;
    }

    public void setJournalContainerMap(SortedMap<Location, List<JournalContainer>> journalContainerMap) {
        this.journalContainerMap = journalContainerMap;
    }

    public void groupJournalBy(final Comparator<Location> locationComparator) {
        SortedMap<Location, List<JournalContainer>> map = new TreeMap<Location, List<JournalContainer>>(locationComparator);
        for (JournalContainer journalContainer : journalContainerList) {
            final Location location = journalContainer.getRealLocation();
            List<JournalContainer> locationJournalContainerList = map.get(location);
            if (locationJournalContainerList == null) {
                locationJournalContainerList = new ArrayList<JournalContainer>();
                map.put(location, locationJournalContainerList);
            }
            locationJournalContainerList.add(journalContainer);
        }
        journalContainerMap = map;
    }

    public void sortJournal(final Comparator journalContainerComparator) {
        for (List<JournalContainer> journalContainerList : journalContainerMap.values()) {
            Collections.sort(journalContainerList, journalContainerComparator);
        }
    }
}
