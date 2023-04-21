package com.tonbeller.jpivot.table.navi;

import orcajo.azada.core.model.Mode;
import orcajo.azada.core.model.ModelException;
import org.olap4j.metadata.Hierarchy;
import org.olap4j.metadata.Level;
import org.olap4j.metadata.Member;
import com.tonbeller.jpivot.olap.model.VisitorSupportSloppy;
import com.tonbeller.jpivot.table.Icons;
import com.tonbeller.jpivot.table.span.Span;

public class DrillReplaceUI extends DrillExpandUI {

    public String getId() {
        return Mode.Drill.DRILL_REPLACE.name();
    }

    class CanCollapse extends VisitorSupportSloppy {

        boolean result = false;

        public void visitHierarchy(Hierarchy v) {
            result = table.getQueryManager().canCollapse(v);
        }

        public void visitLevel(Level v) {
            result = table.getQueryManager().canCollapse(v.getHierarchy());
        }

        public void visitMember(Member v) {
            result = table.getQueryManager().canCollapse(v);
        }
    }

    protected boolean canCollapse(Span span) {
        CanCollapse cc = new CanCollapse();
        span.getObject().accept(cc);
        return cc.result;
    }

    class DoCollapse extends VisitorSupportSloppy {

        public void visitHierarchy(Hierarchy v) {
            try {
                table.getQueryManager().collapse(v);
            } catch (ModelException e) {
            }
        }

        public void visitLevel(Level v) {
            try {
                table.getQueryManager().collapse(v.getHierarchy());
            } catch (ModelException e) {
            }
        }

        public void visitMember(Member v) {
            try {
                table.getQueryManager().collapse(v);
            } catch (ModelException e) {
            }
        }
    }

    protected void collapse(Span span) throws ModelException {
        DoCollapse dc = new DoCollapse();
        span.getObject().accept(dc);
    }

    protected boolean canExpand(Span span) {
        if (positionContainsMember(span)) {
            return table.getQueryManager().canExpand(span.getMember());
        }
        return false;
    }

    protected void expand(Span span) throws ModelException {
        table.getQueryManager().expand(span.getMember());
    }

    protected boolean initializeExtension() {
        return table.getOlapModel().getDrill().equals(Mode.Drill.DRILL_REPLACE);
    }

    protected String getCollapseImage() {
        return Icons.DRILL_REPLACE_COLLAPSE;
    }

    protected String getExpandImage() {
        return Icons.DRILL_REPLACE_EXPAND;
    }

    protected String getOtherImage() {
        return Icons.DRILL_POSITION_OTHER;
    }
}
