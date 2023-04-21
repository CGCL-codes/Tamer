package com.volantis.mcs.dom2theme.impl.generator.rule.type;

import com.volantis.mcs.dom2theme.impl.model.OutputStyledElement;
import com.volantis.mcs.dom2theme.impl.model.OutputStyledElementIteratee;
import com.volantis.mcs.dom2theme.impl.model.OutputStyledElementList;
import com.volantis.mcs.dom2theme.impl.model.OutputStyles;
import com.volantis.mcs.dom2theme.impl.model.PseudoStylePath;
import com.volantis.mcs.dom2theme.impl.model.PseudoStylePathIteratee;
import com.volantis.mcs.localization.LocalizationFactory;
import com.volantis.shared.iteration.IterationAction;
import com.volantis.styling.PseudoElement;
import com.volantis.styling.PseudoStyleEntityVisitor;
import com.volantis.styling.StatefulPseudoClassSet;
import com.volantis.synergetics.log.LogDispatcher;
import java.util.HashSet;
import java.util.Set;

/**
 * Extracts the list of type selectors we will use from the list of elements.
 */
public class TypeSelectorSequenceExtractor {

    private static final LogDispatcher logger = LocalizationFactory.createLogger(TypeSelectorSequenceExtractor.class);

    private final TypeSelectorSequenceList selectorList = new TypeSelectorSequenceList();

    private final Set typeSet = new HashSet();

    private final Set pseudoElementSet = new HashSet();

    private final Set pseudoClassSets = new HashSet();

    private final Set combinedSet = new HashSet();

    public TypeSelectorSequenceList extractSequences(OutputStyledElementList elementList) {
        elementList.iterate(new OutputStyledElementIteratee() {

            public IterationAction next(OutputStyledElement element) {
                extractElementSelectors(element);
                return IterationAction.CONTINUE;
            }
        });
        if (logger.isDebugEnabled()) {
            logger.debug("Sorted type selectors: " + selectorList.toString());
        }
        return selectorList;
    }

    private void extractElementSelectors(final OutputStyledElement outputElement) {
        OutputStyles styles = outputElement.getStyles();
        if (styles == null) {
            return;
        }
        styles.iterate(new PseudoStylePathIteratee() {

            public void next(PseudoStylePath pseudoPath) {
                String type = outputElement.getName();
                if (pseudoPath.isEmpty()) {
                    if (!typeSet.contains(type)) {
                        typeSet.add(type);
                        TypeSelectorSequence sequence = new TypeSelectorSequence();
                        sequence.setType(type);
                        selectorList.add(sequence);
                    }
                } else {
                    final TypeSelectorSequence combined = new TypeSelectorSequence();
                    if (type != null) {
                        combined.setType(type);
                    }
                    pseudoPath.accept(new PseudoStyleEntityVisitor() {

                        public void visit(StatefulPseudoClassSet pseudoClassSet) {
                            if (!pseudoClassSets.contains(pseudoClassSet)) {
                                pseudoClassSets.add(pseudoClassSet);
                                TypeSelectorSequence sequence = new TypeSelectorSequence();
                                sequence.addPseudoClassSet(pseudoClassSet);
                                selectorList.add(sequence);
                            }
                            combined.addPseudoClassSet(pseudoClassSet);
                        }

                        public void visit(PseudoElement pseudoElement) {
                            if (!pseudoElementSet.contains(pseudoElement)) {
                                pseudoElementSet.add(pseudoElement);
                                TypeSelectorSequence sequence = new TypeSelectorSequence();
                                sequence.addPseudoElement(pseudoElement);
                                selectorList.add(sequence);
                            }
                            combined.addPseudoElement(pseudoElement);
                        }
                    });
                    if (combined.isComposite() && !combinedSet.contains(combined)) {
                        combinedSet.add(combined);
                        final TypeSelectorSequence sequence = new TypeSelectorSequence();
                        if (combined.getType() != null) {
                            sequence.setType(combined.getType());
                        }
                        pseudoPath.accept(new PseudoStyleEntityVisitor() {

                            public void visit(StatefulPseudoClassSet pseudoClassSet) {
                                sequence.addPseudoClassSet(pseudoClassSet);
                            }

                            public void visit(PseudoElement pseudoElement) {
                                sequence.addPseudoElement(pseudoElement);
                            }
                        });
                        selectorList.add(sequence);
                    }
                }
            }
        });
    }
}
