package de.objectcode.time4u.migrator.server04.old.strategy;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import org.hibernate.cfg.reveng.DefaultReverseEngineeringStrategy;
import org.hibernate.cfg.reveng.OverrideRepository;
import org.hibernate.cfg.reveng.ReverseEngineeringRuntimeInfo;
import org.hibernate.cfg.reveng.ReverseEngineeringSettings;
import org.hibernate.cfg.reveng.ReverseEngineeringStrategy;
import org.hibernate.cfg.reveng.TableIdentifier;
import org.hibernate.mapping.ForeignKey;
import org.hibernate.mapping.Table;

public class Time4UReverseEngineeringStrategy implements ReverseEngineeringStrategy {

    ReverseEngineeringStrategy delegate;

    Map<Character, String> nameFixes = new HashMap<Character, String>();

    public Time4UReverseEngineeringStrategy() {
        final OverrideRepository or = new OverrideRepository();
        or.addResource("old-version.reveng.xml");
        delegate = or.getReverseEngineeringStrategy(new DefaultReverseEngineeringStrategy());
        nameFixes.put('�', "ae");
        nameFixes.put('�', "oe");
        nameFixes.put('�', "ue");
        nameFixes.put('�', "Ae");
        nameFixes.put('�', "Oe");
        nameFixes.put('�', "Ue");
        nameFixes.put('�', "ss");
        nameFixes.put('.', "_");
        nameFixes.put('(', "_");
        nameFixes.put(')', "_");
        nameFixes.put('/', "_");
        nameFixes.put('\'', "_");
        nameFixes.put('%', "_");
    }

    public String classNameToCompositeIdName(final String arg0) {
        return delegate.classNameToCompositeIdName(arg0);
    }

    public void close() {
        delegate.close();
    }

    public String columnToHibernateTypeName(final TableIdentifier arg0, final String arg1, final int arg2, final int arg3, final int arg4, final int arg5, final boolean arg6, final boolean arg7) {
        return delegate.columnToHibernateTypeName(arg0, arg1, arg2, arg3, arg4, arg5, arg6, arg7);
    }

    @SuppressWarnings("unchecked")
    public Map columnToMetaAttributes(final TableIdentifier arg0, final String arg1) {
        return delegate.columnToMetaAttributes(arg0, arg1);
    }

    public String columnToPropertyName(final TableIdentifier table, final String column) {
        return delegate.columnToPropertyName(table, fixName(column));
    }

    public void configure(final ReverseEngineeringRuntimeInfo arg0) {
        delegate.configure(arg0);
    }

    public boolean excludeColumn(final TableIdentifier table, final String column) {
        if (column.indexOf('?') >= 0 || column.indexOf('\'') >= 0) {
            System.err.println("Ignore: " + table.getName() + " " + column);
            return true;
        }
        return delegate.excludeColumn(table, column);
    }

    @SuppressWarnings("unchecked")
    public boolean excludeForeignKeyAsCollection(final String arg0, final TableIdentifier arg1, final List arg2, final TableIdentifier arg3, final List arg4) {
        return delegate.excludeForeignKeyAsCollection(arg0, arg1, arg2, arg3, arg4);
    }

    @SuppressWarnings("unchecked")
    public boolean excludeForeignKeyAsManytoOne(final String arg0, final TableIdentifier arg1, final List arg2, final TableIdentifier arg3, final List arg4) {
        return delegate.excludeForeignKeyAsManytoOne(arg0, arg1, arg2, arg3, arg4);
    }

    public boolean excludeTable(final TableIdentifier table) {
        return delegate.excludeTable(table);
    }

    @SuppressWarnings("unchecked")
    public String foreignKeyToCollectionName(final String arg0, final TableIdentifier arg1, final List arg2, final TableIdentifier arg3, final List arg4, final boolean arg5) {
        return delegate.foreignKeyToCollectionName(arg0, arg1, arg2, arg3, arg4, arg5);
    }

    @SuppressWarnings("unchecked")
    public String foreignKeyToEntityName(final String arg0, final TableIdentifier arg1, final List arg2, final TableIdentifier arg3, final List arg4, final boolean arg5) {
        return delegate.foreignKeyToEntityName(arg0, arg1, arg2, arg3, arg4, arg5);
    }

    public String foreignKeyToManyToManyName(final ForeignKey arg0, final TableIdentifier arg1, final ForeignKey arg2, final boolean arg3) {
        return delegate.foreignKeyToManyToManyName(arg0, arg1, arg2, arg3);
    }

    @SuppressWarnings("unchecked")
    public List getForeignKeys(final TableIdentifier arg0) {
        return delegate.getForeignKeys(arg0);
    }

    public String getOptimisticLockColumnName(final TableIdentifier arg0) {
        return delegate.getOptimisticLockColumnName(arg0);
    }

    @SuppressWarnings("unchecked")
    public List getPrimaryKeyColumnNames(final TableIdentifier arg0) {
        return delegate.getPrimaryKeyColumnNames(arg0);
    }

    @SuppressWarnings("unchecked")
    public List getSchemaSelections() {
        return delegate.getSchemaSelections();
    }

    public Properties getTableIdentifierProperties(final TableIdentifier arg0) {
        return delegate.getTableIdentifierProperties(arg0);
    }

    public String getTableIdentifierStrategyName(final TableIdentifier arg0) {
        return delegate.getTableIdentifierStrategyName(arg0);
    }

    @SuppressWarnings("unchecked")
    public boolean isForeignKeyCollectionInverse(final String arg0, final TableIdentifier arg1, final List arg2, final TableIdentifier arg3, final List arg4) {
        return delegate.isForeignKeyCollectionInverse(arg0, arg1, arg2, arg3, arg4);
    }

    @SuppressWarnings("unchecked")
    public boolean isForeignKeyCollectionLazy(final String arg0, final TableIdentifier arg1, final List arg2, final TableIdentifier arg3, final List arg4) {
        return delegate.isForeignKeyCollectionLazy(arg0, arg1, arg2, arg3, arg4);
    }

    public boolean isManyToManyTable(final Table arg0) {
        return delegate.isManyToManyTable(arg0);
    }

    public void setSettings(final ReverseEngineeringSettings arg0) {
        delegate.setSettings(arg0);
    }

    public String tableToClassName(final TableIdentifier table) {
        final String className = delegate.tableToClassName(table);
        final int idx = className.lastIndexOf('.');
        if (idx >= 0) {
            return className.substring(0, idx + 1) + "Old" + className.substring(idx + 1);
        }
        return "Old" + className;
    }

    public String tableToCompositeIdName(final TableIdentifier arg0) {
        return delegate.tableToCompositeIdName(arg0);
    }

    public String tableToIdentifierPropertyName(final TableIdentifier arg0) {
        return delegate.tableToIdentifierPropertyName(arg0);
    }

    @SuppressWarnings("unchecked")
    public Map tableToMetaAttributes(final TableIdentifier arg0) {
        return delegate.tableToMetaAttributes(arg0);
    }

    public boolean useColumnForOptimisticLock(final TableIdentifier arg0, final String arg1) {
        return delegate.useColumnForOptimisticLock(arg0, arg1);
    }

    String fixName(final String str) {
        final StringBuffer out = new StringBuffer();
        if (Character.isDigit(str.charAt(0))) {
            out.append("a");
        }
        for (final char ch : str.toCharArray()) {
            final String fix = nameFixes.get(ch);
            if (fix != null) {
                out.append(fix);
            } else {
                out.append(ch);
            }
        }
        return out.toString();
    }
}
