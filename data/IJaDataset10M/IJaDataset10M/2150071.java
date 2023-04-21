package com.gorillalogic.dal.common;

import com.gorillalogic.config.Preferences;
import com.gorillalogic.dal.*;
import com.gorillalogic.dal.model.*;
import com.gorillalogic.dal.model.morph.ModelMorpher;
import java.io.PrintWriter;

public abstract class CommonPkgTable extends ProxyCohortTable.WithRowLookup implements PkgTable, CommonPkgExtender {

    public String kind() {
        return "package";
    }

    public final World getWorld() {
        return commonWorld();
    }

    public abstract CommonWorld commonWorld();

    public CommonTable commonShave() {
        return commonPkg();
    }

    public boolean isRootInWorld() {
        return false;
    }

    public CommonPkgTable getRootCommonPkgInWorld() {
        if (isRootInWorld()) {
            return this;
        }
        return super.getRootCommonPkgInWorld();
    }

    /**
	 * Override of default CommonTable behavior: do not wrap dereferenced
	 * cells, since they already contain complete context information
	 *
	 */
    protected final CommonTable derefCell(CommonRow row, int column, TableStrategy strategy) throws AccessException {
        CommonColumnHdr hdr = commonColumn(column);
        CommonTable table = hdr.computeTable(row, strategy.raw(true));
        return table;
    }

    protected boolean satisfies(PathStrategy.Origin origin) {
        if (origin == PathStrategy.Origin.name) return true;
        if (origin == PathStrategy.Origin.pkg) return true;
        if (origin == PathStrategy.Origin.world) {
            return isRootInWorld();
        }
        return false;
    }

    public CommonTable up() {
        return commonPkg();
    }

    /**
	 * In the DAL Java API PkgTable subclasses from Table,
	 * so this routine must be defined. However, in the meta 
	 * model, package is not a subtype of entity. The only
	 * reasonable thing to do is to return null.
	 *
	 * metaPkgRow() is valid, however.
	 *
	 * @return an <code>Entity.Row</code> value
	 */
    public Entity.Row metaRow() {
        return null;
    }

    public final PkgExtender extendPkg() {
        return extendCommonPkg();
    }

    public final PkgExtender extendPkg(boolean toss) throws OperationException {
        return extendCommonPkg(toss);
    }

    public final CommonPkgExtender extendCommonPkg() {
        try {
            return extendCommonPkg(false);
        } catch (OperationException e) {
            throw new InternalException(e);
        }
    }

    public CommonPkgExtender extendCommonPkg(boolean toss) throws OperationException {
        return this;
    }

    public final PkgTable defPkg() throws AccessException {
        return defCommonPkg();
    }

    public final PkgTable defPkg(String name) throws AccessException {
        return defCommonPkg(name);
    }

    public final PkgTable defPkg(String name, Pkg.Row pkgRow) throws AccessException {
        return defCommonPkg(name, pkgRow);
    }

    public final Table defTable() throws AccessException {
        return defCommonTable();
    }

    public final Table defTable(String name, boolean makePersistent) throws AccessException {
        return defCommonTable(name, makePersistent);
    }

    public final Table defTable(String name, Entity.Row metaRow) throws AccessException {
        return defCommonTable(name, metaRow);
    }

    private static int counter = 1;

    public final CommonPkgTable defCommonPkg() throws AccessException {
        return defCommonPkg("P" + counter++);
    }

    public final CommonPkgTable defCommonPkg(String name) throws AccessException {
        return defCommonPkg(name, null);
    }

    public abstract CommonPkgTable defCommonPkg(String name, Pkg.Row metaRow) throws AccessException;

    public final CommonTable defCommonTable() throws AccessException {
        return defCommonTable("P" + counter++, true);
    }

    public final CommonTable defCommonTable(String name, boolean makePersistent) throws AccessException {
        return defCommonTable(name, makePersistent, null);
    }

    public final CommonTable defCommonTable(String name, Entity.Row metaRow) throws AccessException {
        return defCommonTable(name, true, metaRow);
    }

    protected abstract CommonTable defCommonTable(String name, boolean makePersistent, Entity.Row metaRow) throws AccessException;

    public final Table getExtent(String name) throws AccessException {
        return commonExtent(name);
    }

    public CommonTable commonExtent(String name) throws AccessException {
        long rowId = rowId(name, true);
        return rawDeref(rowId, entryColumnIndex());
    }

    public final PkgTable getNestedPkg(String name) throws AccessException {
        return getNestedCommonPkg(name);
    }

    public CommonPkgTable getNestedCommonPkg(String name) throws AccessException {
        return commonExtent(name).asCommonPkgTable();
    }

    protected abstract int entryColumnIndex();

    protected abstract int nameColumnIndex();

    public final int getPrimaryKeyIndex(boolean toss) {
        return 1;
    }

    public final int getPrimaryColumnIndex(boolean toss) {
        return nameColumnIndex();
    }

    public final void morphToModel() throws AccessException {
        morphToModel(false);
    }

    public final void morphToModel(boolean primordial) throws AccessException {
        TxnProxy.serializeWrite(this);
        try {
            ModelMorpher.modelize(this, primordial);
        } finally {
            TxnProxy.finalizeWrite(this);
        }
    }

    public final Universe progenerate() throws AccessException {
        return commonProgenerate();
    }

    public abstract CommonUniverse commonProgenerate() throws AccessException;

    public final PkgRow pkgRow(long rowId) throws BoundsException {
        return commonPkgRow(rowId);
    }

    public final PkgRow pkgRow(String name) throws AccessException {
        return commonPkgRow(name);
    }

    public CommonPkgRow commonPkgRow(long rowId) throws BoundsException {
        return new CommonPkgRow(commonRow(rowId));
    }

    public CommonPkgRow commonPkgRow(String name) throws AccessException {
        CommonRow row = commonRow(name, true);
        return new CommonPkgRow(row);
    }

    protected CommonPkgRow commonPkgRow(CommonRow row) throws BoundsException {
        return new CommonPkgRow(row);
    }

    public PkgRow nthPkgRow(int ordinalPos) throws BoundsException {
        return new CommonPkgRow(nthCommonRow(ordinalPos));
    }

    public CommonPkgTable asCommonPkgTable(boolean toss) throws StructureException {
        return this;
    }

    public final PkgRow asPkgRow() throws StructureException {
        return asCommonPkgRow();
    }

    public CommonPkgRow asCommonPkgRow() throws StructureException {
        try {
            return new CommonPkgRow(commonRowWithNoCheck(asRowId()));
        } catch (BoundsException e) {
            throw new StructureException("Multi-row package cannot be represented in a single row: " + path());
        }
    }

    public final Universe getSpawnedUniverse() {
        return getSpawnedCommonUniverse();
    }

    public CommonUniverse getSpawnedCommonUniverse() {
        return null;
    }

    CommonTable getInterestingCommonTable(CommonRow row, CommonColumnHdr hdr) throws AccessException {
        if (hdr.getName().equals(ScopedElement.ELEMENT_NAME)) {
            hdr = commonColumn(Pkg.ELEMENT_CONTAINS);
            return hdr.computeInterestingTable(row);
        }
        return null;
    }

    public PkgItr pkgLoopLock(boolean includeSoftLinks) {
        FilterStrategy strategy = FilterStrategy.defaultStrategy;
        if (!includeSoftLinks) {
            strategy = strategy.softLinks(false);
        }
        return pkgLoopLock(strategy);
    }

    public final PkgItr pkgLoopLock(FilterStrategy strategy) {
        return commonPkgLoopLock(strategy);
    }

    public CommonPkgItr commonPkgLoopLock(FilterStrategy strategy) {
        return new CommonPkgItr(commonLoopLock(), strategy);
    }

    public class CommonPkgRow extends ProxyRow.NamedRow implements PkgRow {

        protected CommonPkgRow(CommonRow row) {
            super(row);
        }

        public PkgTable pkgOwner() {
            return CommonPkgTable.this;
        }

        public CommonPkgTable commonPkgOwner() {
            return CommonPkgTable.this;
        }

        public CommonTable commonOwner() {
            return CommonPkgTable.this;
        }

        protected int nameColumnIndex() {
            return CommonPkgTable.this.nameColumnIndex();
        }

        public boolean isSoftLink() {
            String nm = getName();
            if ("home".equals(nm)) return true;
            if ("sys".equals(nm)) return true;
            if ("model".equals(nm)) return true;
            if ("universe".equals(nm)) return true;
            return false;
        }

        public boolean isLeading() {
            try {
                Entity.Row entityRow = commonEntry().metaRow();
                if (entityRow != null && !entityRow.isLeading()) {
                    return false;
                }
            } catch (AccessException e) {
                throw new InternalException(e);
            }
            return true;
        }

        public boolean isPkg() {
            return commonEntry().asCommonPkgTable() != null;
        }

        public CommonPkgTable.CommonPkgRow asCommonPkgRow(boolean toss) throws AccessException {
            return this;
        }

        public PkgTable getPkgEntry() throws StructureException {
            return commonPkgEntry();
        }

        public CommonPkgTable commonPkgEntry() throws StructureException {
            return commonEntry().asCommonPkgTable(true);
        }

        public Table getEntry() {
            return commonEntry();
        }

        public CommonTable commonEntry() {
            try {
                return commonTable(entryColumnIndex());
            } catch (AccessException e) {
                Log.internal("CommonPkgTable.commonEntry()", e);
                return null;
            }
        }
    }

    public class CommonPkgItr extends CommonPkgRow implements PkgItr {

        private final CommonItr itr;

        private final FilterStrategy strategy;

        private int skippedEntries = 0;

        CommonPkgItr(CommonItr itr, FilterStrategy strategy) {
            super(itr);
            this.itr = itr;
            this.strategy = strategy;
        }

        protected CommonItr itrTarget() {
            return itr;
        }

        public CommonPkgRow asCommonPkgRow(boolean toss) {
            return new CommonPkgRow(itr);
        }

        public boolean next() {
            do {
                if (!super.next()) return false;
                if (include()) {
                    break;
                }
                skippedEntries++;
            } while (true);
            return true;
        }

        private boolean include() {
            if ((!strategy.softLinks()) && isSoftLink()) return false;
            if (strategy.leading()) {
                try {
                    Entity.Row entityRow = commonEntry().metaRow();
                    if (entityRow != null && !entityRow.isLeading()) {
                        return false;
                    }
                } catch (AccessException e) {
                    throw new InternalException(e);
                }
            }
            return true;
        }

        public boolean hasNext() {
            do {
                if (!super.hasNext()) return false;
                if (!include()) {
                    skippedEntries++;
                    super.next();
                } else {
                    break;
                }
            } while (true);
            return true;
        }

        public int count() {
            return super.count() - skippedEntries;
        }

        public void restart() {
            super.restart();
            skippedEntries = 0;
        }
    }

    public final void deleteAllEntries(boolean recursive) throws AccessException {
        TxnProxy.serializeWrite(this);
        try {
            doDeleteAllEntries(recursive);
        } finally {
            TxnProxy.finalizeWrite(this);
        }
    }

    final void doDeleteAllEntries(boolean recursive) throws AccessException {
        final boolean isModel = commonWorld().getRootCommonPkg().isModel();
        CommonPkgItr itr = commonPkgLoopLock(FilterStrategy.recursing);
        while (itr.next()) {
            if (itr.isPkg()) {
                if (recursive) {
                    itr.commonPkgEntry().doDeleteAllEntries(recursive);
                }
            } else {
                if (isModel && itr.getName().equals("Properties")) {
                    continue;
                }
                if (!Preferences.isRdbEnabled() || !Preferences.importDatabase() || itr.commonEntry().getRootCommonPkgInWorld().getWorld().getName().equals("defaultModel")) {
                    itr.commonEntry().doDeleteAllRows(true);
                }
            }
        }
    }

    /**
	 * Model tables that shouldn't be deleted.
	 */
    public static boolean shouldPreserve(CommonPkgRow row) {
        String nm = row.getName();
        if (nm == null) return false;
        if (nm.equals("Properties")) return true;
        if (nm.equals("DataWorldType")) return true;
        if (nm.equals("IFMType")) return true;
        if (nm.equals("SZMType")) return true;
        if (nm.equals("XMLNamespace")) return true;
        if (nm.equals("SZMFieldProps")) return true;
        if (nm.equals("MessageType")) return true;
        if (nm.equals("MessageFactory")) return true;
        if (nm.equals("Trigger")) return true;
        if (nm.equals("Interpretation")) return true;
        if (nm.equals("Condition")) return true;
        if (nm.equals("Formatter")) return true;
        if (nm.equals("state")) return true;
        if (nm.equals("usecase")) return true;
        return false;
    }

    public void generateSelfValidate(CommonTable.Commander out) throws AccessException {
        FilterStrategy strategy = FilterStrategy.defaultStrategy;
        strategy = strategy.softLinks(false);
        CommonPkgItr itr = commonPkgLoopLock(strategy);
        while (itr.next()) {
            CommonTable next = itr.commonEntry();
            next.generateSelfValidate(out);
        }
    }

    public void generateCheckpoint(CommonTable.Commander out, int pass) throws AccessException {
        FilterStrategy strategy = FilterStrategy.defaultStrategy;
        strategy = strategy.softLinks(false);
        CommonPkgItr itr = commonPkgLoopLock(strategy);
        while (itr.next()) {
            CommonTable next = itr.commonEntry();
            next.generateCheckpoint(out, pass);
        }
    }

    public void verifyAllConstraints(final AccessException.Accumulator acc) throws AccessException {
        FilterStrategy strategy = FilterStrategy.defaultStrategy;
        strategy = strategy.softLinks(false);
        CommonPkgItr itr = commonPkgLoopLock(strategy);
        while (itr.next()) {
            CommonTable next = itr.commonEntry();
            next.verifyAllConstraints(acc);
        }
    }

    public static void main(String[] argv) {
        Remain.go(argv);
    }
}
