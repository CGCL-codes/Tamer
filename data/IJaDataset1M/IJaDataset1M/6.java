package org.openexi.fujitsu.proc.grammars;

import java.util.ArrayList;
import org.openexi.fujitsu.proc.common.EventTypeList;
import org.openexi.fujitsu.schema.EXISchema;

final class SimpleContentGrammar extends ElementContentGrammar {

    private static final int POS_0 = 0;

    private static final int POS_1 = 1;

    private final int[] m_initials;

    private final ArrayEventTypeList m_eventTypes_0;

    private final ArrayEventTypeList m_eventTypes_1;

    private final EventCodeTuple m_eventCodes_0;

    private final EventCodeTuple m_eventCodes_1;

    SimpleContentGrammar(int tp, GrammarCache cache) {
        super(tp, cache);
        assert m_schema.getNodeType(tp) == EXISchema.SIMPLE_TYPE_NODE;
        m_initials = new int[] { m_nd };
        final EventTypeSchema[] eventTypeSchemas;
        m_eventTypes_0 = new ArrayEventTypeList();
        eventTypeSchemas = createEventType(tp, 0, 0, this, m_eventTypes_0);
        assert eventTypeSchemas.length == 1;
        final ArrayList<AbstractEventType> eventTypeList = new ArrayList<AbstractEventType>();
        eventTypeList.add(eventTypeSchemas[0]);
        final EventCodeTupleSink res = new EventCodeTupleSink();
        createEventCodeTuple(eventTypeList, cache.grammarOptions, res, m_eventTypes_0);
        m_eventCodes_0 = res.eventCodeTuple;
        m_eventTypes_0.setItems(res.eventTypes);
        m_eventTypes_1 = new ArrayEventTypeList();
        eventTypeList.clear();
        eventTypeList.add(new EventTypeSchemaEndElement(0, this, m_eventTypes_1));
        res.clear();
        createEventCodeTuple(eventTypeList, cache.grammarOptions, res, m_eventTypes_1);
        m_eventCodes_1 = res.eventCodeTuple;
        m_eventTypes_1.setItems(res.eventTypes);
    }

    @Override
    public final void init(GrammarState stateVariables) {
        super.init(stateVariables);
        stateVariables.cursor = 0;
        stateVariables.phase = ELEMENT_STATE_CONTENT_DEPLETE;
    }

    @Override
    final EventTypeList getNextEventTypes(GrammarState stateVariables) {
        return stateVariables.cursor != POS_0 ? m_eventTypes_1 : m_eventTypes_0;
    }

    @Override
    final EventCodeTuple getNextEventCodes(GrammarState stateVariables) {
        return stateVariables.cursor != POS_0 ? m_eventCodes_1 : m_eventCodes_0;
    }

    @Override
    final void element(int eventTypeIndex, String uri, String name, GrammarState stateVariables) {
        throw new IllegalStateException();
    }

    @Override
    final void schemaAttribute(int eventTypeIndex, String uri, String name, GrammarState stateVariables) {
        throw new IllegalStateException();
    }

    @Override
    final void xsitp(int tp, GrammarState stateVariables) {
        throw new IllegalStateException();
    }

    @Override
    final void nillify(GrammarState stateVariables) {
        throw new IllegalStateException();
    }

    @Override
    public final void chars(GrammarState stateVariables) {
        if (stateVariables.cursor != POS_1) {
            stateVariables.phase = ELEMENT_STATE_CONTENT_COMPLETE;
            stateVariables.cursor = POS_1;
        }
    }

    @Override
    public final void undeclaredChars(GrammarState stateVariables) {
    }

    @Override
    protected final void end(GrammarState stateVariables) {
        finish(stateVariables);
    }

    @Override
    final int[] getInitials() {
        return m_initials;
    }

    @Override
    final String getContentRegime() {
        return "simple";
    }
}
