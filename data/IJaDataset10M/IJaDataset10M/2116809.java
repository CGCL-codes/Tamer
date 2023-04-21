package net.jadoth.sqlengine.sql.definitions;

import java.lang.reflect.Field;
import net.jadoth.sqlengine.SqlName;
import net.jadoth.sqlengine.sql.descriptors.BaseTableDescriptor;

public interface BaseTableMember<T extends SqlBaseTable> extends TableMember<T> {

    /**
	 * Returns {@code true} if this base table member does not reference any object outside of its parent
	 * base table (e.g. FOREIGN KEYS, COLUMNs with a default value derived from a query or using a SEQUENCE,
	 * CHECK CONSTRAINTs using another object, etc.)
	 *
	 * @return {@code true} if the definition scope of this member is restricted to its parent base table.
	 */
    public boolean isLocal();

    public BaseTableMember<? extends SqlBaseTable> sqlDescriptorMember();

    public interface Mutable<T extends SqlBaseTable> extends BaseTableMember<T>, TableMember.Initializable<T> {

        public BaseTableMember.Mutable<T> linkTable(T owner);

        @Override
        public T sqlOwner();
    }

    public interface Recalling<T extends SqlBaseTable> extends BaseTableMember.Mutable<T>, SqlNamed.Recalling {

        @Override
        public SqlIdentifier getOldName();

        @Override
        public void setOldName(SqlIdentifier oldName);
    }

    public interface Generatable<T extends SqlBaseTable> extends Mutable<T> {

        public boolean isGenerated();

        public boolean isReferentialGenerated();
    }

    public interface InitItem {

        public Object getEnclosingInstance();

        public Field getDeclaringField();

        public TABLE getTable();

        public BaseTableMember.Mutable<? extends SqlBaseTable> getMember();

        public void replaceMember(TableMember<?> member);

        public boolean isDefinedInArray();

        public String getFieldDefinitionName();
    }

    public final class Static {

        public static String getMemberName(final InitItem item) {
            final BaseTableMember.Mutable<? extends SqlBaseTable> member = item.getMember();
            if (member.isIdentifierInitialized()) {
                return member.sqlName();
            }
            return SqlName.deriveTableMemberName(item);
        }
    }

    public abstract class FlagState<T extends SqlBaseTable> implements Generatable<T> {

        private static final int FLAG_GENERATED_REFERENTIAL = 1;

        private static final boolean isReferentialGenerated(final int flags) {
            return (flags & FLAG_GENERATED_REFERENTIAL) > 0;
        }

        private static final boolean isGenerated(final int flags) {
            return flags > 0;
        }

        private int generatedFlags = 0;

        @Override
        public boolean isReferentialGenerated() {
            return isReferentialGenerated(this.generatedFlags);
        }

        @Override
        public boolean isGenerated() {
            return isGenerated(this.generatedFlags);
        }

        protected void internalSetReferentialGenerated(final boolean set) {
            if (set) {
                this.generatedFlags |= FLAG_GENERATED_REFERENTIAL;
            } else {
                this.generatedFlags ^= FLAG_GENERATED_REFERENTIAL;
            }
        }
    }

    public abstract class ImplementationSqlTableDescriptor extends FlagState<SqlBaseTable> implements BaseTableMember.Recalling<SqlBaseTable> {

        private SqlIdentifier name;

        private BaseTableDescriptor owner;

        private SqlIdentifier oldIdentifier;

        private boolean initialized;

        public ImplementationSqlTableDescriptor() {
            this(null, null, null, false);
        }

        public ImplementationSqlTableDescriptor(final SqlIdentifier identifier, final BaseTableDescriptor owner, final SqlIdentifier oldIdentifier, final boolean initialized) {
            super();
            this.name = identifier;
            this.owner = owner;
            this.oldIdentifier = oldIdentifier;
            this.initialized = initialized;
        }

        @Override
        public BaseTableDescriptor sqlOwner() {
            return this.owner;
        }

        @Override
        public SqlIdentifier sqlIdentifier() {
            return this.name;
        }

        @Override
        public boolean isIdentifierInitialized() {
            return this.initialized;
        }

        @Override
        public String sqlName() {
            return this.name == null ? null : this.name.sqlName();
        }

        @Override
        public SqlIdentifier getOldName() {
            return this.oldIdentifier;
        }

        @Override
        public ImplementationSqlTableDescriptor sqlDescriptorMember() {
            return this;
        }

        @Override
        public ImplementationSqlTableDescriptor setIdentifier(final SqlIdentifier identifier) {
            this.name = identifier;
            this.initialized = true;
            return this;
        }

        protected abstract void internalRegisterAtOwner(final BaseTableDescriptor.Mutable owner);

        protected void internalSetOwner(final BaseTableDescriptor owner) {
            this.owner = owner;
            if (owner instanceof BaseTableDescriptor.Mutable) {
                this.internalRegisterAtOwner((BaseTableDescriptor.Mutable) owner);
            }
        }

        @Override
        public ImplementationSqlTableDescriptor linkTable(final SqlBaseTable owner) {
            this.internalSetOwner(owner.sqlDescriptor());
            return this;
        }

        @Override
        public void setOldName(final SqlIdentifier oldName) {
            this.oldIdentifier = oldName;
        }
    }
}
