package v2k.parser.tree;

import java.util.*;

/**
 *
 * @author karl
 */
public class EventControl {

    private enum EType {

        eHierEvent, eStar, eEventExpr
    }

    ;

    public EventControl(boolean isStar) {
        m_item = new AStar(isStar);
    }

    public EventControl(HierEventIdent hei) {
        m_item = new AHier(hei);
    }

    public EventControl(EventExpression ee) {
        m_item = new AEvent(ee);
    }

    private A m_item;

    private abstract static class A {

        protected A(EType e) {
            m_type = e;
        }

        final EType m_type;
    }

    private static class AStar extends A {

        private AStar(boolean isStar) {
            super(EType.eStar);
            m_isStar = isStar;
        }

        private boolean m_isStar;
    }

    private static class AHier extends A {

        private AHier(HierEventIdent hei) {
            super(EType.eHierEvent);
            m_event = hei;
        }

        private HierEventIdent m_event;
    }

    private static class AEvent extends A {

        private AEvent(EventExpression ee) {
            super(EType.eEventExpr);
            m_event = ee;
        }

        private EventExpression m_event;
    }
}
