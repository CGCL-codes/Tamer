package net.sourceforge.hlm.gui.math.semantic.placeholder;

import net.sourceforge.hlm.gui.managers.*;
import net.sourceforge.hlm.gui.math.*;
import net.sourceforge.hlm.gui.math.semantic.placeholder.content.*;
import net.sourceforge.hlm.library.contexts.*;
import net.sourceforge.hlm.library.terms.element.*;
import net.sourceforge.hlm.visual.templates.*;

public class ElementTermPlaceholderView extends PlaceholderView<ElementTerm> {

    public ElementTermPlaceholderView(ObjectFrame frame, LibraryObserver libraryManager, ContextPlaceholder<ElementTerm> placeholder, ParenthesesStyle parentheses) {
        super(frame, libraryManager, placeholder);
        this.parentheses = parentheses;
    }

    @Override
    protected PlaceholderContentView<ElementTerm> createContentView(ObjectFrame frame, ElementTerm content) {
        return new ElementTermView(frame, this.libraryManager, content, this, this.parentheses);
    }

    private ParenthesesStyle parentheses;
}
