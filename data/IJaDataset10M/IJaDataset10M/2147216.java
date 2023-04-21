package de.knowwe.core.taghandler;

import de.knowwe.core.kdom.AbstractType;
import de.knowwe.core.kdom.sectionFinder.MultiSectionFinder;
import de.knowwe.core.kdom.sectionFinder.RegexSectionFinder;
import de.knowwe.core.kdom.sectionFinder.SectionFinder;

public class TagHandlerTypeStartSymbol extends AbstractType {

    public TagHandlerTypeStartSymbol() {
        MultiSectionFinder multi = new MultiSectionFinder();
        SectionFinder f1 = new RegexSectionFinder("\\[\\{KnowWEPlugin ");
        SectionFinder f2 = new RegexSectionFinder("\\%\\%KnowWEPlugin ");
        multi.addSectionFinder(f1);
        multi.addSectionFinder(f2);
        this.sectionFinder = multi;
    }
}
