package jsesh.graphics.export;

import jsesh.editor.caret.MDCCaret;

/**
 * Interface for classes that can provide a caret.
 * @author S. Rosmorduc
 */
public interface CaretBroker {

    MDCCaret getCaret();
}
