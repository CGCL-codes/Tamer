package org.jfonia.notation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.jfonia.connect5.basics.AbstractObserver;
import org.jfonia.connect5.intervals.BasicIntInterval;
import org.jfonia.connect5.intervals.LinkedIntIntervalList;
import org.jfonia.connect5.relations.Equal;
import org.jfonia.constants.SymbolConstants;
import org.jfonia.model.ISequence;
import org.jfonia.model.ISequenceObserver;
import org.jfonia.model.Sequence;
import org.jfonia.model.ToneSequence;
import org.jfonia.model.elements.Beat;
import org.jfonia.model.elements.Clef;
import org.jfonia.model.elements.IStaffElement;
import org.jfonia.model.elements.MusicalElement;
import org.jfonia.model.elements.Tone;
import percussiongenerator.model.Pair;

/**
 *
 * @author Rik Bauwens
 */
public class StaffElementSequence implements ISequence, ISequenceObserver {

    private Set<IStaffElementSequenceObserver> observers = new LinkedHashSet<IStaffElementSequenceObserver>();

    private ToneSequence toneSequence;

    private Sequence<Beat> beatSequence;

    private Sequence<Clef> clefSequence;

    private Map<MusicalElement, Set<IStaffElement>> musicalElements;

    private List<IStaffElement> staffElements;

    private Map<MusicalElement, Pair<Equal<Integer>, AbstractObserver>> staffElementRelations;

    private boolean adding;

    private boolean locked;

    public StaffElementSequence() {
        musicalElements = new HashMap<MusicalElement, Set<IStaffElement>>();
        staffElements = new ArrayList<IStaffElement>();
        staffElementRelations = new HashMap<MusicalElement, Pair<Equal<Integer>, AbstractObserver>>();
        locked = false;
    }

    public MusicalElement getMusicalElement(int tick) {
        MusicalElement musicalElement = toneSequence.getMusicalElement(tick);
        for (IStaffElement staffElement : musicalElements.get(musicalElement)) {
            if (staffElement.getBegin() == tick) return (MusicalElement) staffElement;
        }
        return null;
    }

    public MusicalElement addMusicalElement(MusicalElement musicalElement) {
        System.out.println("add: " + musicalElement.getClass());
        if (musicalElement instanceof Tone && toneSequence != null) toneSequence.addMusicalElement(musicalElement); else if (musicalElement instanceof Beat && beatSequence != null) beatSequence.addMusicalElement(musicalElement); else if (musicalElement instanceof Clef && clefSequence != null) clefSequence.addMusicalElement(musicalElement); else if (musicalElement instanceof ObservableNoteOrRest) {
            Tone tone = new Tone(musicalElement.getDifference());
            if (musicalElement instanceof ObservableNote) tone.setBase40Rank(((ObservableNote) musicalElement).getBase40Rank());
            toneSequence.addMusicalElement(tone);
            return tone;
        }
        return musicalElement;
    }

    public MusicalElement insertMusicalElement(int beginTick, MusicalElement musicalElement) {
        System.out.println("begintick: " + beginTick);
        if (musicalElement instanceof Tone && toneSequence != null) toneSequence.insertMusicalElement(beginTick, musicalElement); else if (musicalElement instanceof Beat && beatSequence != null) beatSequence.insertMusicalElement(beginTick, musicalElement); else if (musicalElement instanceof Clef && clefSequence != null) clefSequence.insertMusicalElement(beginTick, musicalElement); else if (musicalElement instanceof ObservableNoteOrRest) {
            Tone tone = new Tone(musicalElement.getDifference());
            if (musicalElement instanceof ObservableNote) tone.setBase40Rank(((ObservableNote) musicalElement).getBase40Rank());
            toneSequence.insertMusicalElement(beginTick, tone);
            return tone;
        }
        return musicalElement;
    }

    public void addMusicalElements(List<MusicalElement> musicalElements) {
        switchOffObservers();
        MusicalElement lastFromList = toneSequence.getMusicalElementFromIndex(toneSequence.size() - 1);
        for (MusicalElement musicalElement : musicalElements) addMusicalElement(musicalElement);
        updateFrom(lastFromList);
        switchOnObservers();
    }

    public void insertMusicalElements(int beginTick, List<MusicalElement> musicalElements) {
        switchOffObservers();
        MusicalElement elementFromList = toneSequence.getMusicalElement(beginTick);
        int tickPosition = beginTick;
        for (MusicalElement musicalElement : musicalElements) {
            insertMusicalElement(tickPosition, musicalElement);
            tickPosition += musicalElement.getDifference();
        }
        updateFrom(elementFromList);
        switchOnObservers();
    }

    public void removeMusicalElement(MusicalElement musicalElement) {
        System.out.println("remove " + musicalElement.getClass());
        if (musicalElement instanceof IStaffElement) removeStaffElement((IStaffElement) musicalElement);
        if (musicalElement instanceof Tone && toneSequence != null) toneSequence.removeMusicalElement(musicalElement); else if (musicalElement instanceof Beat && beatSequence != null) beatSequence.removeMusicalElement(musicalElement); else if (musicalElement instanceof Clef && clefSequence != null) clefSequence.removeMusicalElement(musicalElement);
    }

    public int indexOf(MusicalElement musicalElement) {
        if (musicalElement instanceof ObservableNoteOrRest) return toneSequence.indexOf(((ObservableNoteOrRest) musicalElement).getTone());
        if (musicalElement instanceof Tone && toneSequence != null) return toneSequence.indexOf(musicalElement); else if (musicalElement instanceof Beat && beatSequence != null) return beatSequence.indexOf(musicalElement); else if (musicalElement instanceof Clef && clefSequence != null) return clefSequence.indexOf(musicalElement);
        return -1;
    }

    public int getMusicalLength() {
        if (toneSequence != null) return toneSequence.getMusicalLength();
        if (beatSequence != null) return beatSequence.getMusicalLength();
        if (clefSequence != null) return clefSequence.getMusicalLength();
        return 0;
    }

    private void switchOffObservers() {
        if (toneSequence != null) toneSequence.removeObserver(this);
        if (beatSequence != null) beatSequence.removeObserver(this);
        if (clefSequence != null) clefSequence.removeObserver(this);
    }

    private void switchOnObservers() {
        if (toneSequence != null) toneSequence.addObserver(this);
        if (beatSequence != null) beatSequence.addObserver(this);
        if (clefSequence != null) clefSequence.addObserver(this);
    }

    public ToneSequence getToneSequence() {
        return toneSequence;
    }

    public Sequence<Beat> getBeatSequence() {
        return beatSequence;
    }

    public Sequence<Clef> getClefSequence() {
        return clefSequence;
    }

    public void setToneSequence(ToneSequence toneSequence) {
        if (this.toneSequence != null) this.toneSequence.removeObserver(this);
        this.toneSequence = toneSequence;
        if (toneSequence != null) {
            toneSequence.addObserver(this);
            createNotes();
        }
    }

    public void setBeatSequence(Sequence<Beat> beatSequence) {
        if (this.beatSequence != null) this.beatSequence.removeObserver(this);
        this.beatSequence = beatSequence;
        if (beatSequence != null) {
            beatSequence.addObserver(this);
            createNotes();
        }
    }

    public void setClefSequence(Sequence<Clef> clefSequence) {
        if (this.clefSequence != null) this.clefSequence.removeObserver(this);
        this.clefSequence = clefSequence;
        if (clefSequence != null) {
            clefSequence.addObserver(this);
            createNotes();
        }
    }

    public void addStaffElement(int beginTick, IStaffElement staffElement) {
        if (staffElement instanceof Tone) {
            adding = true;
            Tone tone = (Tone) staffElement;
            if (beginTick == toneSequence.getMusicalLength()) toneSequence.add(tone); else {
                MusicalElement nextMusicalElement = toneSequence.getMusicalElement(beginTick);
                toneSequence.insert(toneSequence.indexOf(nextMusicalElement), tone);
            }
        }
    }

    public void removeStaffElement(IStaffElement staffElement) {
        System.out.println("removestaffelement");
        if (staffElement instanceof ObservableNoteOrRest) ((ObservableNoteOrRest) staffElement).remove();
    }

    private void createNotes() {
        clear();
        createNotesFrom(0);
    }

    private void createNotesFrom(int index) {
        if (index < 0) return;
        boolean withBeats = (beatSequence != null);
        if (toneSequence != null) {
            int beatIndex = 0;
            for (int i = index; i < toneSequence.size(); i++) {
                final Tone tone = (Tone) toneSequence.getMusicalElementFromIndex(i);
                final LinkedIntIntervalList toneLength = new LinkedIntIntervalList();
                Equal<Integer> beginRelation = new Equal<Integer>(tone.getBeginNode(), toneLength.getBeginNode());
                musicalElements.put(tone, new HashSet<IStaffElement>());
                if (withBeats) {
                    Tie tie = null;
                    int begin = tone.getBegin();
                    int duration = tone.getDifference();
                    Beat beat = null;
                    while (beatIndex < beatSequence.size() && ((Beat) beatSequence.getMusicalElementFromIndex(beatIndex)).getEnd() <= tone.getEnd()) {
                        beat = (Beat) beatSequence.getMusicalElementFromIndex(beatIndex);
                        duration = Math.min(beat.getDifference(), tone.getEnd() - begin);
                        tie = split(tone, toneLength, duration, tie);
                        begin += duration;
                        beatIndex++;
                    }
                    if (beat != null && beat.isMeasureBeat() && beat.getBegin().compareTo(begin) >= 0) add(new Bar(beat.getBegin(), beat.getDifference()));
                    if (beatIndex < beatSequence.size() && ((Beat) beatSequence.getMusicalElementFromIndex(beatIndex)).getBegin() < tone.getEnd()) split(tone, toneLength, duration, tie);
                } else {
                    System.out.println("start split: ");
                    split(tone, toneLength);
                    System.out.println("end split");
                }
                AbstractObserver observer = new AbstractObserver(toneLength.getEndNode()) {

                    public void onNotify(Object source) {
                        tone.setDifference(toneLength.getDifference());
                    }
                };
                staffElementRelations.put(tone, new Pair<Equal<Integer>, AbstractObserver>(beginRelation, observer));
            }
        }
    }

    private void updateAfter(MusicalElement musicalElement) {
        MusicalElement nextMusicalElement = toneSequence.getMusicalElement(musicalElement.getBegin());
        if (nextMusicalElement != null) new UpdateFrom(nextMusicalElement).start();
    }

    private void updateFrom(MusicalElement musicalElement) {
        new UpdateFrom(musicalElement).start();
    }

    private class UpdateFrom extends Thread {

        private MusicalElement musicalElement;

        public UpdateFrom(MusicalElement musicalElement) {
            this.musicalElement = musicalElement;
        }

        @Override
        public void run() {
            while (!requestLock()) {
            }
            removeFrom(((Tone) musicalElement).getBegin());
            createNotesFrom(toneSequence.indexOf(musicalElement));
            removeLock();
        }
    }

    private void split(Tone tone, LinkedIntIntervalList toneLength) {
        split(tone, toneLength, tone.getDifference(), null);
    }

    private Tie split(Tone tone, LinkedIntIntervalList toneLength, int duration, Tie globalTie) {
        System.out.println(duration);
        int begin = toneLength.getEnd();
        int noteCount = 0;
        Tie tie = null;
        int musicalLength = duration;
        int divisor = SymbolConstants.DIVISOR;
        while (divisor > 0) {
            System.out.println("divisor: " + divisor);
            int toneCount = musicalLength / divisor;
            for (int j = 0; j < toneCount; j++) {
                BasicIntInterval noteLength = new BasicIntInterval();
                noteLength.setDifference(divisor);
                toneLength.add(noteLength);
                ObservableNoteOrRest noteOrRest = null;
                if (tone.getBase40Rank() == null) noteOrRest = new ObservableRest(this, tone, noteLength); else {
                    noteOrRest = new ObservableNote(this, tone, noteLength).setClefSequence(clefSequence);
                }
                add(tone, noteOrRest);
                if (noteCount == 0 && begin != tone.getBegin()) {
                    globalTie.setRight(noteOrRest);
                    add(globalTie);
                }
                if (j == toneCount - 1 && musicalLength % divisor == 0 && begin + duration < tone.getEnd()) globalTie = new Tie().setLeft(noteOrRest);
                if (noteCount % 2 == 0) {
                    tie = new Tie().setLeft(noteOrRest);
                } else {
                    tie.setRight(noteOrRest);
                    add(tie);
                }
                noteCount++;
                begin += divisor;
            }
            musicalLength %= divisor;
            divisor /= 2;
        }
        return globalTie;
    }

    private void add(MusicalElement musicalElement, IStaffElement staffElement) {
        if (!musicalElements.containsKey(musicalElement)) musicalElements.put(musicalElement, new HashSet<IStaffElement>());
        musicalElements.get(musicalElement).add(staffElement);
        staffElements.add(staffElement);
        staffElementAdded(staffElement);
    }

    private void add(IStaffElement staffElement) {
        staffElements.add(staffElement);
        staffElementAdded(staffElement);
    }

    private void remove(MusicalElement musicalElement) {
        Pair<Equal<Integer>, AbstractObserver> pair = staffElementRelations.get(musicalElement);
        pair.first.switchOff();
        pair.second.switchOff();
        staffElementRelations.remove(musicalElement);
        for (IStaffElement staffElement : musicalElements.get(musicalElement)) {
            staffElements.remove(staffElement);
            staffElementRemoved(staffElement);
        }
        musicalElements.remove(musicalElement);
        if (musicalElement instanceof Tone) toneSequence.remove(toneSequence.indexOf((Tone) musicalElement));
    }

    protected void remove(IStaffElement staffElement) {
        staffElements.remove(staffElement);
        staffElementRemoved(staffElement);
    }

    protected void clear() {
        for (IStaffElement staffElement : staffElements) staffElementRemoved(staffElement);
        for (Pair<Equal<Integer>, AbstractObserver> pair : staffElementRelations.values()) {
            pair.first.switchOff();
            pair.second.switchOff();
        }
        staffElements.clear();
        staffElementRelations.clear();
    }

    private void removeFrom(int beginTick) {
        Iterator<MusicalElement> it = musicalElements.keySet().iterator();
        System.out.println("removefrom " + beginTick);
        System.out.println("musicalElements " + musicalElements.size());
        while (it.hasNext()) {
            MusicalElement musicalElement = it.next();
            System.out.println("found : " + musicalElement.getBegin() + " : " + musicalElement.getEnd());
            if (musicalElement.getBegin() >= beginTick) {
                Pair<Equal<Integer>, AbstractObserver> pair = staffElementRelations.get(musicalElement);
                pair.first.switchOff();
                pair.second.switchOff();
                staffElementRelations.remove(musicalElement);
                for (IStaffElement staffElement : musicalElements.get(musicalElement)) staffElementRemoved(staffElement);
                it.remove();
            }
        }
    }

    public void nodeAdded(Sequence sequence, MusicalElement musicalElement) {
        if (musicalElement instanceof Tone) {
            updateFrom(musicalElement);
            adding = false;
        }
    }

    public void nodeRemoved(Sequence sequence, MusicalElement musicalElement) {
        if (sequence == toneSequence) {
            if (!requestLock()) return;
            for (IStaffElement staffElement : musicalElements.get(musicalElement)) removeStaffElement(staffElement);
            updateAfter(musicalElement);
            removeLock();
        } else if (sequence == beatSequence) {
        }
    }

    public void nodeDifferenceChanged(Sequence sequence, final MusicalElement musicalElement) {
        if (sequence == toneSequence) {
            if (!adding) {
                if (musicalElement.getDifference() == 0) {
                    new Thread() {

                        @Override
                        public void run() {
                            while (!requestLock()) {
                            }
                            remove((MusicalElement) musicalElement);
                            removeLock();
                        }
                    }.start();
                }
            }
        } else if (sequence == beatSequence) {
        }
    }

    public void staffElementAdded(IStaffElement staffElement) {
        for (IStaffElementSequenceObserver observer : observers) observer.staffElementAdded(staffElement);
    }

    public void staffElementRemoved(IStaffElement staffElement) {
        for (IStaffElementSequenceObserver observer : observers) observer.staffElementRemoved(staffElement);
    }

    public void addObserver(IStaffElementSequenceObserver o) {
        if (!observers.contains(o)) observers.add(o);
    }

    public boolean containsObserver(IStaffElementSequenceObserver o) {
        return observers.contains(o);
    }

    public void removeObserver(IStaffElementSequenceObserver o) {
        observers.remove(o);
    }

    public boolean requestLock() {
        if (locked) return false; else {
            locked = true;
            return true;
        }
    }

    public void removeLock() {
        locked = false;
    }
}
