package org.liris.schemerger.winepi;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import org.liris.schemerger.core.dataset.SimSequence;
import org.liris.schemerger.core.event.ISimEvent;
import org.liris.schemerger.core.pattern.IEpisode;
import org.liris.schemerger.core.pattern.ITypeDec;
import org.liris.schemerger.core.pattern.ItemType;
import org.liris.schemerger.utils.Counter;
import org.liris.schemerger.utils.ItemTypeCounter;

/**
 * The WINEPI counter for candidates episodes.
 * 
 * @author Damien Cram
 * 
 */
public class WinepiCounter<E extends ISimEvent, T extends ITypeDec> {

    public class WindowSlider {

        private EventInListener<E> eventInListener = new EventInListener<E>() {

            public void eventIn(E event) {
                ItemType type = event.getItem().getType();
                Counter cnt = WindowSlider.this.currentEvents.get(type);
                cnt.increment();
                ItemTypeCounter eventEntry = new ItemTypeCounter(type, cnt.getCnt());
                if (WinepiCounter.this.map.get(eventEntry) == null) return;
                for (WinepiCounterEntry<T> entry : WinepiCounter.this.map.get(eventEntry)) {
                    entry.addEventCounts(cnt.getCnt());
                    if (entry.getEpisode().size() == entry.getEventCount()) entry.setInWindow(event.getDate().previous(WindowSlider.this.win));
                }
            }

            ;
        };

        private EventOutListener<E> eventOutListener = new EventOutListener<E>() {

            public void eventOut(E event) {
                ItemType type = event.getItem().getType();
                Counter cnt = WindowSlider.this.currentEvents.get(type);
                ItemTypeCounter eventEntry = new ItemTypeCounter(event.getItem().getType(), cnt.getCnt());
                if (WinepiCounter.this.map.get(eventEntry) == null) return;
                for (WinepiCounterEntry<T> entry : WinepiCounter.this.map.get(eventEntry)) {
                    if (entry.getEpisode().size() == entry.getEventCount()) entry.updateFrequentCount(event.getDate().next());
                    entry.substractEventCounts(cnt.getCnt());
                }
                cnt.decrement();
            }
        };

        private SimSequence<E> s;

        private long win;

        private Map<ItemType, Counter> currentEvents = new TreeMap<ItemType, Counter>();

        public WindowSlider(SimSequence<E> s, long win, Collection<ItemType> itemTypes) {
            super();
            this.s = s;
            this.win = win;
            for (ItemType type : itemTypes) this.currentEvents.put(type, new Counter());
        }

        public void setEventInListener(EventInListener<E> eventInListener) {
            this.eventInListener = eventInListener;
        }

        public void setEventOutListener(EventOutListener<E> eventOutListener) {
            this.eventOutListener = eventOutListener;
        }

        public void slide() {
            LinkedList<E> windowEvents = new LinkedList<E>();
            for (E e : this.s) {
                while (windowEvents.size() != 0 && (windowEvents.getFirst().getDate().diffAsLong(e.getDate())) > this.win) this.eventOutListener.eventOut(windowEvents.removeFirst());
                this.eventInListener.eventIn(e);
                windowEvents.addLast(e);
            }
            while (windowEvents.size() != 0) this.eventOutListener.eventOut(windowEvents.removeFirst());
        }
    }

    private int episodeSize;

    private Map<ItemTypeCounter, Set<WinepiCounterEntry<T>>> map = new TreeMap<ItemTypeCounter, Set<WinepiCounterEntry<T>>>();

    private ArrayList<WinepiCounterEntry<T>> candidates = new ArrayList<WinepiCounterEntry<T>>();

    public WinepiCounter(int episodeSize) {
        super();
        this.episodeSize = episodeSize;
    }

    public void addEpisode(IEpisode<T> episode) {
        if (episode.size() != this.episodeSize) throw new IllegalStateException("The number of events contained in the episode must be " + this.episodeSize);
        WinepiCounterEntry<T> candidateEntry = new WinepiCounterEntry<T>(episode);
        this.candidates.add(candidateEntry);
        for (ItemTypeCounter eventEntry : episode.getItemTypeEntries()) {
            if (this.map.get(eventEntry) == null) this.map.put(eventEntry, new TreeSet<WinepiCounterEntry<T>>());
            this.map.get(eventEntry).add(candidateEntry);
        }
    }

    private Map<IEpisode<T>, Float> counts(SimSequence<E> sequence, Collection<ItemType> eventTypes, float frequence, long win, boolean serial) {
        if (serial) throw new UnsupportedOperationException("WINEPI is not implemented for serial episodes yet");
        WindowSlider slider = new WindowSlider(sequence, win, eventTypes);
        slider.slide();
        long nbWins = sequence.getEnd().asLong() - sequence.getStart().asLong() - win + 1;
        long support = (long) ((nbWins) * frequence) + 1;
        Map<IEpisode<T>, Float> frequents = new TreeMap<IEpisode<T>, Float>();
        Iterator<WinepiCounterEntry<T>> it = this.candidates.iterator();
        while (it.hasNext()) {
            WinepiCounterEntry<T> winepiCounterEntry = it.next();
            if (winepiCounterEntry.getFreqCount() < support) it.remove(); else frequents.put(winepiCounterEntry.getEpisode(), ((float) winepiCounterEntry.getFreqCount()) / ((float) nbWins));
        }
        return frequents;
    }

    public Map<IEpisode<T>, Float> countsParallels(SimSequence<E> sequence, Collection<ItemType> eventTypes, float frequence, long win) {
        return counts(sequence, eventTypes, frequence, win, false);
    }

    public Map<IEpisode<T>, Float> countsSerial(SimSequence<E> sequence, Collection<ItemType> eventTypes, float frequence, long win) {
        return counts(sequence, eventTypes, frequence, win, true);
    }

    public Collection<IEpisode<T>> getCandidates() {
        TreeSet<IEpisode<T>> c = new TreeSet<IEpisode<T>>();
        for (WinepiCounterEntry<T> entry : this.candidates) c.add(entry.getEpisode());
        return c;
    }

    public Set<WinepiCounterEntry<T>> getCandidatesEntries(ItemTypeCounter eventEntry) {
        return this.map.get(eventEntry);
    }

    public int getEpisodeSize() {
        return this.episodeSize;
    }

    @Override
    public String toString() {
        String s = "*** Winepi Counter ***\n- episode size: " + this.episodeSize;
        List<ItemTypeCounter> entries = new ArrayList<ItemTypeCounter>(this.map.keySet());
        Collections.sort(entries);
        for (ItemTypeCounter entry : entries) {
            s += "\n" + entry + " -->  " + this.map.get(entry);
        }
        return s;
    }
}
