package org.jfonia.notation;

import java.util.ArrayList;
import java.util.List;
import org.jfonia.connect5.basics.BasicValueNode;
import org.jfonia.connect5.basics.MutableValueNode;
import org.jfonia.connect5.relations.Equal;
import org.jfonia.constants.ViewConstants;
import org.jfonia.model.elements.Beat;
import org.jfonia.model.elements.Clef;
import org.jfonia.model.elements.KeySignature;
import org.jfonia.model.elements.TimeSignature;
import org.jfonia.pitch.ClefNode;
import org.jfonia.view.elements.IElement;
import org.jfonia.view.elements.StaffPart;
import org.jfonia.view.main.LeadSheet;

/**
 *
 * @author Rik Bauwens
 */
public class Notation {

    private static Notation instance;

    private List<IElement> selectedElements;

    private IElement lastSelectedElement;

    private MutableValueNode<Integer> noteHeadNode;

    private MutableValueNode<Integer> accidentalNode;

    private MutableValueNode<Integer> flagsNode;

    private MutableValueNode<Integer> noteDotsNode;

    private Equal<Integer> noteHeadRelation;

    private Equal<Integer> accidentalRelation;

    private Equal<Integer> flagsRelation;

    private Equal<Integer> dotsRelation;

    private MutableValueNode<Integer> restNode;

    private MutableValueNode<Integer> restDotsNode;

    private Equal<Integer> restRelation;

    private Equal<Integer> restDotsRelation;

    private Equal<Integer> noteRelation;

    private MutableValueNode<Integer> clefNode;

    private Equal<Integer> clefRelation;

    private MutableValueNode<Integer> keySignatureNode;

    private Equal<Integer> keySignatureRelation;

    private MutableValueNode<Integer> nominatorNode;

    private MutableValueNode<Integer> denominatorNode;

    private Equal<Integer> nominatorRelation;

    private Equal<Integer> denominatorRelation;

    private MutableValueNode<Integer> barNode;

    private Equal<Integer> barRelation;

    private MutableValueNode<Integer> bpmNode;

    private Equal<Integer> bpmRelation;

    private MutableValueNode<String> instrumentNode;

    private Equal<String> instrumentRelation;

    private Notation() {
        selectedElements = new ArrayList<IElement>();
        noteHeadNode = new BasicValueNode<Integer>(ViewConstants.INITIAL_NOTEHEAD);
        accidentalNode = new BasicValueNode<Integer>(ViewConstants.INITIAL_ACCIDENTAL);
        flagsNode = new BasicValueNode<Integer>(ViewConstants.INITIAL_FLAGS);
        noteDotsNode = new BasicValueNode<Integer>(ViewConstants.INITIAL_DOTS);
        restNode = new BasicValueNode<Integer>(ViewConstants.INITIAL_REST);
        restDotsNode = new BasicValueNode<Integer>(ViewConstants.INITIAL_DOTS);
        clefNode = new BasicValueNode<Integer>(ViewConstants.INITIAL_CLEF);
        keySignatureNode = new BasicValueNode<Integer>(ViewConstants.INITIAL_KEY_SIGNATURE);
        nominatorNode = new BasicValueNode<Integer>(ViewConstants.INITIAL_NOMINATOR);
        denominatorNode = new BasicValueNode<Integer>(ViewConstants.INITIAL_DENOMINATOR);
        barNode = new BasicValueNode<Integer>(ViewConstants.INITIAL_BAR);
        bpmNode = new BasicValueNode<Integer>(ViewConstants.INITIAL_BPM);
        instrumentNode = new BasicValueNode<String>(ViewConstants.INITIAL_INSTRUMENT);
    }

    public static synchronized Notation getInstance() {
        if (instance == null) instance = new Notation();
        return instance;
    }

    public int getNoteHead() {
        return noteHeadNode.getValue();
    }

    public void setNoteHead(int noteHead) {
        noteHeadNode.setValue(noteHead);
        setFlags(0);
    }

    public int getAccidental() {
        return accidentalNode.getValue();
    }

    public void setAccidental(int accidental) {
        accidentalNode.setValue(accidental);
    }

    public int getFlags() {
        return flagsNode.getValue();
    }

    public void setFlags(int flags) {
        flagsNode.setValue(flags);
    }

    public int getNoteDots() {
        return noteDotsNode.getValue();
    }

    public void setNoteDots(int dots) {
        noteDotsNode.setValue(dots);
    }

    public int getNoteMusicalTime() {
        return MusicalTime.toMusicalTime(new Note(getNoteHead(), 0, getFlags(), getNoteDots()));
    }

    public int getRest() {
        return restNode.getValue();
    }

    public void setRest(int rest) {
        restNode.setValue(rest);
    }

    public int getRestDots() {
        return restDotsNode.getValue();
    }

    public void setRestDots(int dots) {
        restDotsNode.setValue(dots);
    }

    public int getRestMusicalTime() {
        return MusicalTime.toMusicalTime(new Rest(getRest(), getRestDots()));
    }

    public Integer getClef() {
        return clefNode.getValue();
    }

    public void setClef(Integer clef) {
        clefNode.setValue(clef);
    }

    public int getKeySignature() {
        return keySignatureNode.getValue();
    }

    public void setKeySignature(int keySignature) {
        keySignatureNode.setValue(keySignature);
    }

    public int getKeySignatureDirection() {
        return getKeySignature() > 0 ? 1 : (getKeySignature() < 0 ? -1 : 0);
    }

    public void setKeySignatureDirection(int direction) {
        if (getKeySignature() == 0) setKeySignature(Math.abs(getKeySignature() + 1) * direction); else setKeySignature(Math.abs(getKeySignature()) * direction);
    }

    public int getAbsoluteKeySignature() {
        return Math.abs(getKeySignature());
    }

    public void setAbsoluteKeySignature(int absoluteKeySignature) {
        setKeySignature(absoluteKeySignature * getKeySignatureDirection());
    }

    public int getNominator() {
        return nominatorNode.getValue();
    }

    public void setNominator(int nominator) {
        nominatorNode.setValue(nominator);
    }

    public int getDenominator() {
        return denominatorNode.getValue();
    }

    public void setDenominator(int denominator) {
        denominatorNode.setValue(denominator);
    }

    public int getBar() {
        return barNode.getValue();
    }

    public void setBar(int bar) {
        barNode.setValue(bar);
    }

    public int getBPM() {
        return bpmNode.getValue();
    }

    public void setBPM(int bpm) {
        bpmNode.setValue(bpm);
    }

    public String getInstrument() {
        return instrumentNode.getValue();
    }

    public void setInstrument(String instrument) {
        instrumentNode.setValue(instrument);
    }

    public MutableValueNode<Integer> getNoteHeadNode() {
        return noteHeadNode;
    }

    public MutableValueNode<Integer> getAccidentalNode() {
        return accidentalNode;
    }

    public MutableValueNode<Integer> getNoteDotsNode() {
        return noteDotsNode;
    }

    public MutableValueNode<Integer> getFlagsNode() {
        return flagsNode;
    }

    public MutableValueNode<Integer> getRestNode() {
        return restNode;
    }

    public MutableValueNode<Integer> getRestDotsNode() {
        return restDotsNode;
    }

    public MutableValueNode<Integer> getClefNode() {
        return clefNode;
    }

    public MutableValueNode<Integer> getKeySignatureNode() {
        return keySignatureNode;
    }

    public MutableValueNode<Integer> getNominatorNode() {
        return nominatorNode;
    }

    public MutableValueNode<Integer> getDenominatorNode() {
        return denominatorNode;
    }

    public MutableValueNode<Integer> getBarNode() {
        return barNode;
    }

    public MutableValueNode<Integer> getBPMNode() {
        return bpmNode;
    }

    public MutableValueNode<String> getInstrumentNode() {
        return instrumentNode;
    }

    public void setLeadSheet(LeadSheet leadSheet) {
        if (bpmRelation != null) bpmRelation.switchOff();
        if (instrumentRelation != null) instrumentRelation.switchOff();
        if (leadSheet == null) return;
        bpmRelation = new Equal<Integer>(leadSheet.getBPMNode(), bpmNode);
        instrumentRelation = new Equal<String>(leadSheet.getInstrumentNode(), instrumentNode);
    }

    public void setNoteOrRest(ObservableNoteOrRest noteOrRest) {
        if (noteHeadRelation != null) noteHeadRelation.switchOff();
        if (accidentalRelation != null) accidentalRelation.switchOff();
        if (flagsRelation != null) flagsRelation.switchOff();
        if (dotsRelation != null) dotsRelation.switchOff();
        if (restRelation != null) restRelation.switchOff();
        if (restDotsRelation != null) restDotsRelation.switchOff();
        if (noteRelation != null) noteRelation.switchOff();
        if (noteOrRest == null) return;
        if (noteOrRest instanceof ObservableRest) {
            ObservableRest rest = (ObservableRest) noteOrRest;
            restRelation = new Equal<Integer>(rest.getRestNode(), restNode);
            restDotsRelation = new Equal<Integer>(rest.getDotsNode(), restDotsNode);
        } else {
            ObservableNote note = (ObservableNote) noteOrRest;
            noteHeadRelation = new Equal<Integer>(note.getNoteHeadNode(), noteHeadNode);
            accidentalRelation = new Equal<Integer>(note.getAccidentalNode(), accidentalNode);
            flagsRelation = new Equal<Integer>(note.getFlagsNode(), flagsNode);
            dotsRelation = new Equal<Integer>(note.getDotsNode(), noteDotsNode);
        }
    }

    public void setClef(Clef clef) {
        if (clefRelation != null) clefRelation.switchOff();
        if (clef == null) return;
        clefRelation = new Equal<Integer>(new ClefNode(clef.getTypeNode()), clefNode);
    }

    public void setKeySignature(KeySignature keySignature) {
        if (keySignatureRelation != null) keySignatureRelation.switchOff();
        if (keySignature == null) return;
        keySignatureRelation = new Equal<Integer>(keySignature.getTypeNode(), keySignatureNode);
    }

    public void setTimeSignature(TimeSignature timeSignature) {
        if (nominatorRelation != null) nominatorRelation.switchOff();
        if (denominatorRelation != null) denominatorRelation.switchOff();
        if (timeSignature == null) return;
        nominatorRelation = new Equal<Integer>(timeSignature.getNominatorNode(), nominatorNode);
        denominatorRelation = new Equal<Integer>(timeSignature.getDenominatorNode(), denominatorNode);
    }

    public void setBar(Beat beat) {
        if (barRelation != null) barRelation.switchOff();
        if (beat == null) return;
    }

    public int getSelectedElementCount() {
        return selectedElements.size();
    }

    public List<IElement> getSelectedElements() {
        return selectedElements;
    }

    public boolean containsSelectedElement(IElement element) {
        return selectedElements.contains(element);
    }

    public void addSelectedElement(IElement element) {
        element.setSelected(true);
        lastSelectedElement = element;
        if (!selectedElements.contains(element)) selectedElements.add(element);
    }

    public void removeSelectedElement(IElement element) {
        element.setSelected(false);
        if (selectedElements.contains(element)) selectedElements.remove(element);
        if (element == lastSelectedElement) lastSelectedElement = null;
    }

    public void setSelectedElement(IElement element) {
        clearSelectedElements();
        addSelectedElement(element);
        if (element instanceof StaffPart) {
            setClef(((StaffPart) element).getClef());
            setKeySignature(((StaffPart) element).getKeySignature());
            setTimeSignature(((StaffPart) element).getTimeSignature());
            setNoteOrRest(((StaffPart) element).getNoteOrRest());
        }
    }

    public void clearSelectedElements() {
        lastSelectedElement = null;
        for (IElement element : selectedElements) element.setSelected(false);
        selectedElements.clear();
    }

    public int getTickPosition() {
        if (lastSelectedElement != null) {
            return lastSelectedElement.getTickPosition();
        }
        return -1;
    }
}
