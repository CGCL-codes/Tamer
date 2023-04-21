package jsesh.editor;

import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Collections;
import java.util.List;
import java.util.Observable;
import jsesh.editor.caret.MDCCaret;
import jsesh.editor.command.CommandFactory;
import jsesh.editor.command.MDCCommand;
import jsesh.mdc.MDCParserModelGenerator;
import jsesh.mdc.MDCSyntaxError;
import jsesh.mdc.constants.Dialect;
import jsesh.mdc.model.AlphabeticText;
import jsesh.mdc.model.MDCPosition;
import jsesh.mdc.model.ModelElementObserver;
import jsesh.mdc.model.TopItem;
import jsesh.mdc.model.TopItemList;
import jsesh.mdc.model.operations.ModelOperation;
import jsesh.mdc.output.MdCModelWriter;

/**
 * The edition model of a hieroglyphic text.
 * 
 * This model represents a edited text and all operations which can be made on
 * it. (including changing the text completely).
 * 
 * As such, it handles undo/redo operations and the like.
 * 
 * The advantages of using this class is that the text can be easily loaded,
 * cleared, and so on.
 * 
 * TODO : express this class in terms of patterns, and modify it accordingly.
 * 
 * FIXME : currently, the Observable pattern used is misleading.
 * <p>
 * For instance, the workflow and the Carets are all understood as Observers.
 * Now, the workflow uses the carets, and hence should be warned <em>after</em>
 * them, because some of them may be out of date at the time they are used.
 * 
 * <p>
 * In a few cases, these caused a problem with the code of getCurrentMDCLine,
 * but it's now fixed.
 * 
 * <p>
 * The carets would probably deserve a better treatment (and we might use a more
 * precise class than observer). This file is free Software (c) Serge Rosmorduc
 * 
 * @author rosmord
 */
public class HieroglyphicTextModel extends Observable implements ModelElementObserver {

    private TopItemList model;

    private boolean philologyIsSign;

    private boolean debug;

    private UndoManager undoManager;

    public HieroglyphicTextModel() {
        undoManager = new UndoManager();
        setTopItemList(new TopItemList());
        philologyIsSign = true;
        debug = false;
    }

    /**
	 * Returns the model for READONLY PURPORSES ONLY. Currently, nothing
	 * enforces this rule. We might either decide to hide the model behind some
	 * kind of specific interface, or send a copy.
	 * 
	 * @return TopItemList
	 */
    public TopItemList getModel() {
        return model;
    }

    /**
	 * Sets the model.
	 * 
	 * @param model
	 *            The model to set
	 * 
	 */
    public void setTopItemList(TopItemList model) {
        undoManager.clear();
        if (this.model != null) {
            this.model.deleteObserver(this);
        }
        this.model = model;
        if (model != null) {
            model.addObserver(this);
        }
        setChanged();
        notifyObservers();
    }

    public void clear() {
        setTopItemList(new TopItemList());
    }

    public void readTopItemList(Reader in) throws MDCSyntaxError {
        TopItemList l = createGenerator(Dialect.OTHER).parse(in);
        setTopItemList(l);
    }

    /**
	 * read data into the model.
	 * 
	 * @param in
	 * @param dialect
	 *            a MDC dialect identifier from Dialect
	 * @throws MDCSyntaxError
	 * @see Dialect
	 */
    public void readTopItemList(Reader in, Dialect dialect) throws MDCSyntaxError {
        if (Dialect.TKSESH.equals(dialect)) {
            this.philologyIsSign = false;
        } else {
            this.philologyIsSign = true;
        }
        TopItemList l = createGenerator(dialect).parse(in);
        setTopItemList(l);
    }

    public void setMDCCode(String text) throws MDCSyntaxError {
        StringReader r = new StringReader(text);
        readTopItemList(r);
    }

    private MDCParserModelGenerator createGenerator(Dialect dialect) {
        MDCParserModelGenerator generator = new MDCParserModelGenerator(dialect);
        generator.setPhilologyAsSigns(philologyIsSign);
        generator.setDebug(debug);
        return generator;
    }

    /**
	 * Returns the debug.
	 * 
	 * @return boolean
	 * 
	 */
    public boolean isDebug() {
        return debug;
    }

    /**
	 * Returns the philologyIsSign.
	 * 
	 * @return boolean
	 * 
	 */
    public boolean isPhilologyIsSign() {
        return philologyIsSign;
    }

    /**
	 * Sets the debug.
	 * 
	 * @param debug
	 *            The debug to set
	 * 
	 */
    public void setDebug(boolean debug) {
        this.debug = debug;
    }

    /**
	 * Sets the philologyIsSign.
	 * 
	 * @param philologyIsSign
	 *            The philologyIsSign to set
	 * 
	 */
    public void setPhilologyIsSign(boolean philologyIsSign) {
        this.philologyIsSign = philologyIsSign;
    }

    public void observedElementChanged(ModelOperation operation) {
        setChanged();
        notifyObservers(operation);
    }

    /**
	 * Build a list of topitems from a String.
	 * 
	 * @param text
	 * @return the corresponding list of items.
	 * @throws MDCSyntaxError
	 */
    public List buildItems(String text) throws MDCSyntaxError {
        MDCParserModelGenerator generator = createGenerator(Dialect.OTHER);
        StringReader r = new StringReader(text);
        TopItemList t = generator.parse(r);
        return t.removeTopItems(0, t.getNumberOfChildren());
    }

    /**
	 * A simple method for adding text at a given place. Note that this method
	 * is really not the most efficient one. It's quite error prone. Yet it
	 * allows a to write simple stuff fast.
	 * 
	 * @param index
	 * @param mdcText
	 * @throws MDCSyntaxError
	 */
    public void insertMDCText(int index, String mdcText) throws MDCSyntaxError {
        List items = buildItems(mdcText);
        insertElementsAt(buildPosition(index), items);
    }

    /**
	 * Is the model clean (i.e. has it been modified since last loaded or saved
	 * ?).
	 */
    public boolean isClean() {
        return undoManager.isClean();
    }

    public MDCCaret buildCaret() {
        return new MDCCaret(this.getModel());
    }

    public MDCPosition buildFirstPosition() {
        return new MDCPosition(getModel(), 0);
    }

    /**
	 * Replaces the element at a certain position by a new one.
	 * 
	 * @param position
	 *            : the cursor position <em>after</em> the element to replace.
	 * @param newElement
	 *            : the new element.
	 */
    public void replaceElementBefore(MDCPosition position, TopItem newElement) {
        replaceElementBefore(position, Collections.singletonList(newElement));
    }

    public void replaceElementBefore(MDCPosition position, List newElements) {
        MDCCommand command = new CommandFactory().buildReplaceCommand(model, newElements, position.getPreviousPosition(1), position, isFirstCommand());
        undoManager.doCommand(command);
    }

    /**
	 * Insert a list of elements at a certain point.
	 * 
	 * @param position
	 * @param elements
	 *            a list of TopItems
	 */
    public void insertElementsAt(MDCPosition position, List elements) {
        MDCCommand command = new CommandFactory().buildInsertCommand(model, elements, position, isFirstCommand());
        undoManager.doCommand(command);
    }

    /**
	 * 
	 * @param pos1
	 * @param pos2
	 * @param newElements
	 *            a list of TopItems
	 */
    public void replaceElement(MDCPosition pos1, MDCPosition pos2, List newElements) {
        MDCCommand command = new CommandFactory().buildReplaceCommand(getModel(), newElements, pos1, pos2, isClean());
        undoManager.doCommand(command);
    }

    public void replaceElement(MDCPosition pos1, MDCPosition pos2, TopItem element) {
        replaceElement(pos1, pos2, Collections.singletonList(element));
    }

    public void insertElementAt(MDCPosition position, TopItem item) {
        insertElementsAt(position, Collections.singletonList(item));
    }

    private boolean isFirstCommand() {
        return false;
    }

    /**
	 * Returns a <em>copy</em> of the items between two positions.
	 * 
	 * Precondition min < max.
	 * 
	 * @param min
	 * @param max
	 * @return
	 */
    public List getTopItemsBetween(int min, int max) {
        return getModel().getTopItemListBetween(min, max);
    }

    /**
	 * Returns a <em>copy</em> of the items between two positions. There are no
	 * conditions on pos1 and pos2.
	 * 
	 * @param pos1
	 * @param pos2
	 * @return
	 */
    public List getTopItemsBetween(MDCPosition pos1, MDCPosition pos2) {
        int min = Math.min(pos1.getIndex(), pos2.getIndex());
        int max = Math.max(pos1.getIndex(), pos2.getIndex());
        return getTopItemsBetween(min, max);
    }

    public void removeElements(MDCPosition minPosition, MDCPosition maxPosition) {
        MDCCommand command = new CommandFactory().buildRemoveCommand(model, minPosition, maxPosition, isClean());
        undoManager.doCommand(command);
    }

    /**
	 * Write a part of the text to a Writer.
	 * 
	 * @param sw
	 * @param minPos
	 * @param maxPos
	 */
    public void writeAsMDC(Writer sw, int minPos, int maxPos) {
        TopItemList t = getModel();
        MdCModelWriter w = new MdCModelWriter();
        w.write(sw, t, minPos, maxPos);
    }

    public MDCPosition getLastPosition() {
        return new MDCPosition(model, model.getNumberOfChildren());
    }

    public MDCPosition buildPosition(int index) {
        return new MDCPosition(model, index);
    }

    public void redo() {
        if (undoManager.canRedo()) undoManager.redo();
    }

    public void undo() {
        if (undoManager.canUndo()) undoManager.undoCommand();
    }

    /**
	 * Replace a part of the text with a text in MdC. Precondition: start < end,
	 * text is a valid MdC text.
	 * 
	 * @param start
	 * @param end
	 * @param text
	 * @throws MDCSyntaxError
	 */
    public void replaceWithMDCText(int start, int end, String text) throws MDCSyntaxError {
        List items = buildItems(text);
        MDCPosition startPos = buildPosition(start);
        MDCPosition endPos = buildPosition(end);
        replaceElement(startPos, endPos, items);
    }

    public boolean canUndo() {
        return undoManager.canUndo();
    }

    /**
	 * Return true if an operation can be redone.
	 * @return
	 * @see jsesh.editor.UndoManager#canRedo()
	 */
    public boolean canRedo() {
        return undoManager.canRedo();
    }

    public boolean mustSave() {
        return !undoManager.isClean();
    }

    public void setClean() {
        undoManager.clear();
    }
}
