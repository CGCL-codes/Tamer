package org.opengoss.snmphibernate.mib.rdbms;

import java.io.Serializable;
import org.opengoss.snmphibernate.api.SmiType;
import org.opengoss.snmphibernate.api.annotation.MibIndex;
import org.opengoss.snmphibernate.api.annotation.MibObjectType;
import org.opengoss.snmphibernate.api.annotation.MibTable;
import org.opengoss.snmphibernate.api.annotation.MibObjectType.Access;

@SuppressWarnings("serial")
@MibTable
public class OraDbSysEntry implements Serializable {

    @MibIndex(no = 0, length = 1)
    @MibObjectType(oid = "1.3.6.1.2.1.27.1.1.1", smiType = SmiType.INTEGER, access = Access.READ)
    private int applIndex;

    @MibObjectType(oid = "1.3.6.1.4.1.111.4.1.1.1.1", smiType = SmiType.COUNTER32, access = Access.READ)
    private long oraDbSysConsistentChanges;

    @MibObjectType(oid = "1.3.6.1.4.1.111.4.1.1.1.2", smiType = SmiType.COUNTER32, access = Access.READ)
    private long oraDbSysConsistentGets;

    @MibObjectType(oid = "1.3.6.1.4.1.111.4.1.1.1.3", smiType = SmiType.COUNTER32, access = Access.READ)
    private long oraDbSysDbBlockChanges;

    @MibObjectType(oid = "1.3.6.1.4.1.111.4.1.1.1.4", smiType = SmiType.COUNTER32, access = Access.READ)
    private long oraDbSysDbBlockGets;

    @MibObjectType(oid = "1.3.6.1.4.1.111.4.1.1.1.5", smiType = SmiType.COUNTER32, access = Access.READ)
    private long oraDbSysFreeBufferInspected;

    @MibObjectType(oid = "1.3.6.1.4.1.111.4.1.1.1.6", smiType = SmiType.COUNTER32, access = Access.READ)
    private long oraDbSysFreeBufferRequested;

    @MibObjectType(oid = "1.3.6.1.4.1.111.4.1.1.1.7", smiType = SmiType.COUNTER32, access = Access.READ)
    private long oraDbSysParseCount;

    @MibObjectType(oid = "1.3.6.1.4.1.111.4.1.1.1.8", smiType = SmiType.COUNTER32, access = Access.READ)
    private long oraDbSysPhysReads;

    @MibObjectType(oid = "1.3.6.1.4.1.111.4.1.1.1.9", smiType = SmiType.COUNTER32, access = Access.READ)
    private long oraDbSysPhysWrites;

    @MibObjectType(oid = "1.3.6.1.4.1.111.4.1.1.1.10", smiType = SmiType.COUNTER32, access = Access.READ)
    private long oraDbSysRedoEntries;

    @MibObjectType(oid = "1.3.6.1.4.1.111.4.1.1.1.11", smiType = SmiType.COUNTER32, access = Access.READ)
    private long oraDbSysRedoLogSpaceRequests;

    @MibObjectType(oid = "1.3.6.1.4.1.111.4.1.1.1.12", smiType = SmiType.COUNTER32, access = Access.READ)
    private long oraDbSysRedoSyncWrites;

    @MibObjectType(oid = "1.3.6.1.4.1.111.4.1.1.1.13", smiType = SmiType.COUNTER32, access = Access.READ)
    private long oraDbSysSortsDisk;

    @MibObjectType(oid = "1.3.6.1.4.1.111.4.1.1.1.14", smiType = SmiType.COUNTER32, access = Access.READ)
    private long oraDbSysSortsMemory;

    @MibObjectType(oid = "1.3.6.1.4.1.111.4.1.1.1.15", smiType = SmiType.COUNTER32, access = Access.READ)
    private long oraDbSysSortsRows;

    @MibObjectType(oid = "1.3.6.1.4.1.111.4.1.1.1.16", smiType = SmiType.COUNTER32, access = Access.READ)
    private long oraDbSysTableFetchRowid;

    @MibObjectType(oid = "1.3.6.1.4.1.111.4.1.1.1.17", smiType = SmiType.COUNTER32, access = Access.READ)
    private long oraDbSysTableFetchContinuedRow;

    @MibObjectType(oid = "1.3.6.1.4.1.111.4.1.1.1.18", smiType = SmiType.COUNTER32, access = Access.READ)
    private long oraDbSysTableScanBlocks;

    @MibObjectType(oid = "1.3.6.1.4.1.111.4.1.1.1.19", smiType = SmiType.COUNTER32, access = Access.READ)
    private long oraDbSysTableScanRows;

    @MibObjectType(oid = "1.3.6.1.4.1.111.4.1.1.1.20", smiType = SmiType.COUNTER32, access = Access.READ)
    private long oraDbSysTableScansLong;

    @MibObjectType(oid = "1.3.6.1.4.1.111.4.1.1.1.21", smiType = SmiType.COUNTER32, access = Access.READ)
    private long oraDbSysTableScansShort;

    @MibObjectType(oid = "1.3.6.1.4.1.111.4.1.1.1.22", smiType = SmiType.COUNTER32, access = Access.READ)
    private long oraDbSysUserCalls;

    @MibObjectType(oid = "1.3.6.1.4.1.111.4.1.1.1.23", smiType = SmiType.COUNTER32, access = Access.READ)
    private long oraDbSysUserCommits;

    @MibObjectType(oid = "1.3.6.1.4.1.111.4.1.1.1.24", smiType = SmiType.COUNTER32, access = Access.READ)
    private long oraDbSysUserRollbacks;

    @MibObjectType(oid = "1.3.6.1.4.1.111.4.1.1.1.25", smiType = SmiType.COUNTER32, access = Access.READ)
    private long oraDbSysWriteRequests;

    public int getApplIndex() {
        return applIndex;
    }

    public void setApplIndex(int applIndex) {
        this.applIndex = applIndex;
    }

    public long getOraDbSysConsistentChanges() {
        return oraDbSysConsistentChanges;
    }

    public void setOraDbSysConsistentChanges(long oraDbSysConsistentChanges) {
        this.oraDbSysConsistentChanges = oraDbSysConsistentChanges;
    }

    public long getOraDbSysConsistentGets() {
        return oraDbSysConsistentGets;
    }

    public void setOraDbSysConsistentGets(long oraDbSysConsistentGets) {
        this.oraDbSysConsistentGets = oraDbSysConsistentGets;
    }

    public long getOraDbSysDbBlockChanges() {
        return oraDbSysDbBlockChanges;
    }

    public void setOraDbSysDbBlockChanges(long oraDbSysDbBlockChanges) {
        this.oraDbSysDbBlockChanges = oraDbSysDbBlockChanges;
    }

    public long getOraDbSysDbBlockGets() {
        return oraDbSysDbBlockGets;
    }

    public void setOraDbSysDbBlockGets(long oraDbSysDbBlockGets) {
        this.oraDbSysDbBlockGets = oraDbSysDbBlockGets;
    }

    public long getOraDbSysFreeBufferInspected() {
        return oraDbSysFreeBufferInspected;
    }

    public void setOraDbSysFreeBufferInspected(long oraDbSysFreeBufferInspected) {
        this.oraDbSysFreeBufferInspected = oraDbSysFreeBufferInspected;
    }

    public long getOraDbSysFreeBufferRequested() {
        return oraDbSysFreeBufferRequested;
    }

    public void setOraDbSysFreeBufferRequested(long oraDbSysFreeBufferRequested) {
        this.oraDbSysFreeBufferRequested = oraDbSysFreeBufferRequested;
    }

    public long getOraDbSysParseCount() {
        return oraDbSysParseCount;
    }

    public void setOraDbSysParseCount(long oraDbSysParseCount) {
        this.oraDbSysParseCount = oraDbSysParseCount;
    }

    public long getOraDbSysPhysReads() {
        return oraDbSysPhysReads;
    }

    public void setOraDbSysPhysReads(long oraDbSysPhysReads) {
        this.oraDbSysPhysReads = oraDbSysPhysReads;
    }

    public long getOraDbSysPhysWrites() {
        return oraDbSysPhysWrites;
    }

    public void setOraDbSysPhysWrites(long oraDbSysPhysWrites) {
        this.oraDbSysPhysWrites = oraDbSysPhysWrites;
    }

    public long getOraDbSysRedoEntries() {
        return oraDbSysRedoEntries;
    }

    public void setOraDbSysRedoEntries(long oraDbSysRedoEntries) {
        this.oraDbSysRedoEntries = oraDbSysRedoEntries;
    }

    public long getOraDbSysRedoLogSpaceRequests() {
        return oraDbSysRedoLogSpaceRequests;
    }

    public void setOraDbSysRedoLogSpaceRequests(long oraDbSysRedoLogSpaceRequests) {
        this.oraDbSysRedoLogSpaceRequests = oraDbSysRedoLogSpaceRequests;
    }

    public long getOraDbSysRedoSyncWrites() {
        return oraDbSysRedoSyncWrites;
    }

    public void setOraDbSysRedoSyncWrites(long oraDbSysRedoSyncWrites) {
        this.oraDbSysRedoSyncWrites = oraDbSysRedoSyncWrites;
    }

    public long getOraDbSysSortsDisk() {
        return oraDbSysSortsDisk;
    }

    public void setOraDbSysSortsDisk(long oraDbSysSortsDisk) {
        this.oraDbSysSortsDisk = oraDbSysSortsDisk;
    }

    public long getOraDbSysSortsMemory() {
        return oraDbSysSortsMemory;
    }

    public void setOraDbSysSortsMemory(long oraDbSysSortsMemory) {
        this.oraDbSysSortsMemory = oraDbSysSortsMemory;
    }

    public long getOraDbSysSortsRows() {
        return oraDbSysSortsRows;
    }

    public void setOraDbSysSortsRows(long oraDbSysSortsRows) {
        this.oraDbSysSortsRows = oraDbSysSortsRows;
    }

    public long getOraDbSysTableFetchContinuedRow() {
        return oraDbSysTableFetchContinuedRow;
    }

    public void setOraDbSysTableFetchContinuedRow(long oraDbSysTableFetchContinuedRow) {
        this.oraDbSysTableFetchContinuedRow = oraDbSysTableFetchContinuedRow;
    }

    public long getOraDbSysTableFetchRowid() {
        return oraDbSysTableFetchRowid;
    }

    public void setOraDbSysTableFetchRowid(long oraDbSysTableFetchRowid) {
        this.oraDbSysTableFetchRowid = oraDbSysTableFetchRowid;
    }

    public long getOraDbSysTableScanBlocks() {
        return oraDbSysTableScanBlocks;
    }

    public void setOraDbSysTableScanBlocks(long oraDbSysTableScanBlocks) {
        this.oraDbSysTableScanBlocks = oraDbSysTableScanBlocks;
    }

    public long getOraDbSysTableScanRows() {
        return oraDbSysTableScanRows;
    }

    public void setOraDbSysTableScanRows(long oraDbSysTableScanRows) {
        this.oraDbSysTableScanRows = oraDbSysTableScanRows;
    }

    public long getOraDbSysTableScansLong() {
        return oraDbSysTableScansLong;
    }

    public void setOraDbSysTableScansLong(long oraDbSysTableScansLong) {
        this.oraDbSysTableScansLong = oraDbSysTableScansLong;
    }

    public long getOraDbSysTableScansShort() {
        return oraDbSysTableScansShort;
    }

    public void setOraDbSysTableScansShort(long oraDbSysTableScansShort) {
        this.oraDbSysTableScansShort = oraDbSysTableScansShort;
    }

    public long getOraDbSysUserCalls() {
        return oraDbSysUserCalls;
    }

    public void setOraDbSysUserCalls(long oraDbSysUserCalls) {
        this.oraDbSysUserCalls = oraDbSysUserCalls;
    }

    public long getOraDbSysUserCommits() {
        return oraDbSysUserCommits;
    }

    public void setOraDbSysUserCommits(long oraDbSysUserCommits) {
        this.oraDbSysUserCommits = oraDbSysUserCommits;
    }

    public long getOraDbSysUserRollbacks() {
        return oraDbSysUserRollbacks;
    }

    public void setOraDbSysUserRollbacks(long oraDbSysUserRollbacks) {
        this.oraDbSysUserRollbacks = oraDbSysUserRollbacks;
    }

    public long getOraDbSysWriteRequests() {
        return oraDbSysWriteRequests;
    }

    public void setOraDbSysWriteRequests(long oraDbSysWriteRequests) {
        this.oraDbSysWriteRequests = oraDbSysWriteRequests;
    }
}
