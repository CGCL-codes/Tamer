package net.sf.litprog4j.editors.docbookeditor;

import java.util.EventListener;

/**
 * @author klin
 *
 */
public interface ICaretListener extends EventListener {

    public void caretPositionChanged(CaretChangeEvent e);
}
