package uk.co.ordnancesurvey.rabbitgui.parserproblem.highlighter.impl;

import uk.ac.leeds.comp.ui.base.impl.UIModelImpl;
import uk.co.ordnancesurvey.rabbitgui.document.RbtDocumentModel;
import uk.co.ordnancesurvey.rabbitgui.parserproblem.ErrorAndWarningParsedResultVisitor;
import uk.co.ordnancesurvey.rabbitgui.parserproblem.highlighter.RbtParserProblemHighlightModel;
import uk.co.ordnancesurvey.rabbitparser.parsedsentencepart.IParsedPart;
import uk.co.ordnancesurvey.rabbitparser.util.IParsedSpan;

/**
 * Base implementation of a {@link RbtParserProblemHighlightModel}. It monitors
 * a {@link #sourceDocModel} for changes and extracts errors and warnings,
 * feeding them to methods: {@link #removeProblemHighlights()} and
 * {@link #addProblemHighlight(IParsedSpan, uk.co.ordnancesurvey.rabbitgui.parserproblem.highlighter.RbtParserProblemHighlightModel.RbtParserProblemHighlightType)}
 * .
 * 
 * Subclasses need to implement those methods and provide a way to store the
 * problem highlights.
 * 
 * @author rdenaux
 * 
 */
public abstract class BaseProblemHighlightModelFromRbtDocumentImpl extends UIModelImpl implements RbtParserProblemHighlightModel {

    private static final long serialVersionUID = -5886748264966738708L;

    protected final RbtDocumentModel sourceDocModel;

    /**
	 * Use this class to create {@link RbtParserProblemHighlight}s.
	 * 
	 * @author rdenaux
	 * 
	 */
    protected final class HighlightImpl implements RbtParserProblemHighlight {

        private final IParsedSpan span;

        private final RbtParserProblemHighlightType probType;

        public HighlightImpl(IParsedSpan aSpan, RbtParserProblemHighlightType aProbType) {
            assert (aSpan != null);
            span = aSpan;
            assert (aProbType != null);
            probType = aProbType;
        }

        public final IParsedSpan getSpan() {
            return span;
        }

        public final RbtParserProblemHighlightType getProblemType() {
            return probType;
        }
    }

    private class ProblemChangeMonitor extends ErrorAndWarningParsedResultVisitor {

        private static final long serialVersionUID = -4270453717477879110L;

        public ProblemChangeMonitor(RbtDocumentModel aDocModel) {
            super(aDocModel);
        }

        @Override
        protected void handleErrors(IParsedPart aPart) {
            assert (aPart != null);
            addProblemHighlight(aPart.getSpan(), RbtParserProblemHighlightType.ERROR);
        }

        @Override
        protected void handleWarnings(IParsedPart aPart) {
            assert (aPart != null);
            addProblemHighlight(aPart.getSpan(), RbtParserProblemHighlightType.WARNING);
        }

        @Override
        protected void doBeforeTransversing() {
            removeProblemHighlights();
        }

        @Override
        protected void doAfterTransversing() {
            notifyUpdate(RbtParserProblemHighlightModel.PROBLEM_HIGHLIGHTS_CHANGED);
        }
    }

    private final ProblemChangeMonitor problemChangeMonitor;

    public BaseProblemHighlightModelFromRbtDocumentImpl(RbtDocumentModel aDocModel) {
        sourceDocModel = aDocModel;
        problemChangeMonitor = new ProblemChangeMonitor(sourceDocModel);
    }

    protected final void refreshModel() {
        problemChangeMonitor.handleMirroredParsedResultUpdated(sourceDocModel);
    }

    @Override
    public void dispose() {
        problemChangeMonitor.dispose();
        super.dispose();
    }

    /**
	 * Remove all highlights from this model
	 */
    protected abstract void removeProblemHighlights();

    /**
	 * Add a new highlight at the specified span, with the specified problem
	 * type.
	 * 
	 * @param aSpan
	 * @param aProbType
	 */
    protected abstract void addProblemHighlight(IParsedSpan aSpan, RbtParserProblemHighlightType aProbType);
}
