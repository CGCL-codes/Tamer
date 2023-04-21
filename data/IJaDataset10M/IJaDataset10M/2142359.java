package net.sourceforge.hlm.gui.math.semantic.placeholder;

import net.sourceforge.hlm.gui.managers.*;
import net.sourceforge.hlm.gui.math.*;
import net.sourceforge.hlm.library.contexts.*;
import net.sourceforge.hlm.library.objects.constructions.*;
import net.sourceforge.hlm.library.terms.element.*;
import net.sourceforge.hlm.visual.templates.*;

public class StructuralSamplePlaceholderView extends ElementTermPlaceholderView {

    public StructuralSamplePlaceholderView(ObjectFrame frame, LibraryObserver libraryManager, ContextPlaceholder<ElementTerm> placeholder, ParenthesesStyle parentheses, StructuralCaseList<?> cases) {
        super(frame, libraryManager, placeholder, parentheses);
        this.cases = cases;
    }

    public StructuralCaseList<?> getCases() {
        return this.cases;
    }

    private StructuralCaseList<?> cases;
}
