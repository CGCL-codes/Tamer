package uk.co.ordnancesurvey.rabbitparser.parsedsentencepart.impl.subject;

import uk.co.ordnancesurvey.rabbitparser.parsedsentencepart.IParsedPart;
import uk.co.ordnancesurvey.rabbitparser.parsedsentencepart.entity.IParsedConcept;
import uk.co.ordnancesurvey.rabbitparser.parsedsentencepart.impl.base.BaseCompositeParsedPart;
import uk.co.ordnancesurvey.rabbitparser.parsedsentencepart.subject.IParsedSubject;

/**
 * Default implementation of a {@link IParsedSubject}. It is abstract as only
 * specific subclass types can be instantiated.
 * 
 * @author rdenaux
 * 
 */
public abstract class ParsedSubject<T extends IParsedPart> extends BaseCompositeParsedPart<IParsedPart> implements IParsedSubject<T> {

    private static final long serialVersionUID = -6981506196197941123L;

    private final Class<T> scopeIndicatorClass;

    protected ParsedSubject(Class<T> aScopeIndicatorClass, T aScopeIndicator, IParsedConcept aConcept) {
        assert aConcept != null;
        scopeIndicatorClass = aScopeIndicatorClass;
        assert isSupportedScopeIndicator(aScopeIndicator) : "not supported " + aScopeIndicator;
        if (aScopeIndicator != null) {
            addSubPart(aScopeIndicator);
        }
        addSubPart(aConcept);
    }

    /**
     * Returns whether aKeyphrase can be accepted by this {@link ParsedSubject}
     * (not all keyphrase types are suitable for {@link IParsedSubject}s.
     * 
     * @param aScopeIndicator
     *            may be <code>null</code> as some subclasses may support
     *            <code>null</code> scope indicators.
     * @return
     */
    protected abstract boolean isSupportedScopeIndicator(T aScopeIndicator);

    public IParsedConcept getConcept() {
        return getUniqueSubPart(IParsedConcept.class);
    }

    public T getScopeIndicator() {
        return getUniqueOptionalSubPart(scopeIndicatorClass);
    }
}
