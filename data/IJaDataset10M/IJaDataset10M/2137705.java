package net.sourceforge.hlm.visual;

import net.sourceforge.hlm.library.parameters.arguments.*;

public interface LanguageSpecificTemplate<T> extends LanguageSpecificList<T> {

    T findBestMatch(Language language, ArgumentList arguments);
}
