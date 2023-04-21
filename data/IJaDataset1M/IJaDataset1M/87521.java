package net.sf.julie.grammar;

import net.sf.julie.Interpretable;

public interface QQElement extends Interpretable {

    Interpretable interpret(int depth);
}
