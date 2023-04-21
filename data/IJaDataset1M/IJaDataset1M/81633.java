package org.openconcerto.sql.utils;

import org.openconcerto.sql.model.FieldRef;
import org.openconcerto.sql.model.SQLBase;
import org.openconcerto.sql.model.SQLField;
import org.openconcerto.sql.model.SQLSelect;
import org.openconcerto.sql.model.SQLSyntax;
import org.openconcerto.sql.model.SQLSystem;
import org.openconcerto.sql.model.SQLTable;
import org.openconcerto.sql.model.Where;
import org.openconcerto.sql.request.UpdateBuilder;
import org.openconcerto.sql.utils.SQLUtils.SQLFactory;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Collections;
import java.util.List;

/**
 * Reorder some or all rows of a table.
 * 
 * @author Sylvain
 */
public abstract class ReOrder {

    public static final BigDecimal MIN_ORDER = BigDecimal.ZERO;

    public static final BigDecimal DISTANCE = BigDecimal.ONE;

    public static ReOrder create(final SQLTable t) {
        return create(t, ALL);
    }

    public static ReOrder create(final SQLTable t, final int first, final int count) {
        return create(t, new Some(t, first, count));
    }

    private static ReOrder create(final SQLTable t, final Spec spec) {
        final SQLSystem system = t.getBase().getServer().getSQLSystem();
        if (system == SQLSystem.MYSQL) {
            return new ReOrderMySQL(t, spec);
        } else if (system == SQLSystem.POSTGRESQL) return new ReOrderPostgreSQL(t, spec); else if (system == SQLSystem.H2) return new ReOrderH2(t, spec); else throw new IllegalArgumentException(system + " not supported");
    }

    protected final SQLTable t;

    protected final Spec spec;

    protected ReOrder(final SQLTable t, final Spec spec) {
        this.t = t;
        if (!this.t.isOrdered()) throw new IllegalArgumentException(t + " is not ordered");
        this.spec = spec;
    }

    protected final boolean isAll() {
        return this.spec == ALL;
    }

    protected final BigDecimal getFirstToReorder() {
        return this.spec.getFirstToReorder();
    }

    protected final BigDecimal getFirstOrderValue() {
        return this.spec.getFirst();
    }

    protected final String getWhere() {
        final Where w = this.spec.getWhere(null);
        return w == null ? "" : " where " + w;
    }

    protected final Where getWhere(final FieldRef f) {
        return this.spec.getWhere(f);
    }

    protected final String getInc() {
        return this.spec.getInc();
    }

    public abstract List<String> getSQL(Connection conn) throws SQLException;

    public final void exec() throws SQLException {
        final Connection conn = this.t.getBase().getDataSource().getConnection();
        final UpdateBuilder updateUndef = new UpdateBuilder(this.t).set(this.t.getOrderField().getName(), MIN_ORDER.toPlainString());
        updateUndef.setWhere(new Where(this.t.getKey(), "=", this.t.getUndefinedID()));
        SQLUtils.executeAtomic(conn, new SQLFactory<Object>() {

            @Override
            public Object create() throws SQLException {
                final Statement stmt = conn.createStatement();
                if (isAll()) {
                    stmt.execute(updateUndef.asString());
                }
                for (final String s : getSQL(conn)) {
                    stmt.execute(s);
                }
                ReOrder.this.t.fireTableModified(-1, Collections.singletonList(ReOrder.this.t.getOrderField().getName()));
                return null;
            }
        });
    }

    private static class Some implements Spec {

        private final SQLTable t;

        private final BigDecimal firstToReorder;

        private final BigDecimal first;

        private final BigDecimal lastToReorder;

        public Some(final SQLTable t, final int first, final int count) {
            this.t = t;
            if (count <= 0) throw new IllegalArgumentException("Negative Count : " + count);
            if (BigDecimal.valueOf(first).compareTo(MIN_ORDER) <= 0) {
                this.firstToReorder = MIN_ORDER.add(t.getOrderULP());
                this.first = MIN_ORDER.add(DISTANCE);
            } else {
                this.firstToReorder = BigDecimal.valueOf(first);
                this.first = this.firstToReorder;
            }
            final BigDecimal simpleLastToReorder = this.firstToReorder.add(BigDecimal.valueOf(count));
            this.lastToReorder = simpleLastToReorder.compareTo(this.first) > 0 ? simpleLastToReorder : this.first.add(DISTANCE.movePointRight(1));
            assert this.getFirstToReorder().compareTo(this.getFirst()) <= 0 && this.getFirst().compareTo(this.getLast()) < 0 && this.getLast().compareTo(this.getLastToReorder()) <= 0;
        }

        @Override
        public final String getInc() {
            final SQLField oF = this.t.getOrderField();
            final SQLSyntax syntax = SQLSyntax.get(this.t.getServer().getSQLSystem());
            final SQLSelect selTableLast = new SQLSelect(this.t.getBase(), true);
            selTableLast.addSelect(oF, "MAX");
            final String avgDistance = " cast( " + getLast() + " - " + this.getFirst() + " as " + syntax.getOrderType() + " ) / ( count(*) -1)";
            final String res = "CASE WHEN max(" + SQLBase.quoteIdentifier(oF.getName()) + ") = (" + selTableLast.asString() + ") then " + ALL.getInc() + " else " + avgDistance + " end";
            return res + " FROM " + this.t.getSQLName().quote() + " where " + this.getWhere(null).getClause();
        }

        @Override
        public final Where getWhere(FieldRef order) {
            if (order == null) order = this.t.getOrderField(); else if (order.getField() != this.t.getOrderField()) throw new IllegalArgumentException();
            return new Where(order, this.getFirstToReorder(), this.getLastToReorder());
        }

        @Override
        public final BigDecimal getFirstToReorder() {
            return this.firstToReorder;
        }

        private final BigDecimal getLastToReorder() {
            return this.lastToReorder;
        }

        @Override
        public BigDecimal getFirst() {
            return this.first;
        }

        public final BigDecimal getLast() {
            return this.getLastToReorder();
        }
    }

    private static Spec ALL = new Spec() {

        @Override
        public String getInc() {
            return String.valueOf(DISTANCE);
        }

        @Override
        public final Where getWhere(final FieldRef order) {
            return null;
        }

        @Override
        public BigDecimal getFirstToReorder() {
            return MIN_ORDER;
        }

        @Override
        public BigDecimal getFirst() {
            return getFirstToReorder();
        }
    };

    static interface Spec {

        String getInc();

        Where getWhere(final FieldRef order);

        BigDecimal getFirstToReorder();

        BigDecimal getFirst();
    }
}
