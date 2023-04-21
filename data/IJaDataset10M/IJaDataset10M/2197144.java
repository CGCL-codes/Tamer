package net.sourceforge.ivi.core.dfio;

import java.util.Vector;

public class iviDFIOTimeTraceIndexer implements IDFIOTraceIndexer {

    public static final String TimeTraceIndexerKey = "DFIOTrace.TimeIndexer";

    public iviDFIOTimeTraceIndexer(IDFIOTrace trace) {
        d_trace = trace;
        d_timeBlockInfoList = new Vector();
        d_timeInfoBlocks = new TimeTraceBlockInfo[0];
        updateBlockInfo();
    }

    public static iviDFIOTimeTraceIndexer getIndexer(IDFIOTrace trace) {
        Object obj = trace.getData(TimeTraceIndexerKey);
        if (obj == null) {
            iviDFIOTimeTraceIndexer ti = new iviDFIOTimeTraceIndexer(trace);
            trace.setData(TimeTraceIndexerKey, ti);
            obj = ti;
        }
        return (iviDFIOTimeTraceIndexer) obj;
    }

    private void updateBlockInfo() {
        Vector il = d_trace.getRecordBlockList();
        boolean changed = false;
        int ex_count = d_timeBlockInfoList.size();
        iviDFIOTraceTimeRecord rec = new iviDFIOTraceTimeRecord();
        if (il.size() > d_timeBlockInfoList.size()) {
            changed = true;
            int delta = il.size() - d_timeBlockInfoList.size();
            int limit = d_trace.getBlockRecordCount();
            for (int i = 0; i < delta; i++) {
                IDFIOTraceRecordBlockInfo block = (IDFIOTraceRecordBlockInfo) il.elementAt(ex_count + i);
                if (i + 1 == delta) {
                    limit = d_trace.getLastBlockValidCount();
                    d_lastBlockValidCount = limit;
                }
                if (limit == 0) {
                    continue;
                }
                d_trace.indexBlock(rec, block, 0);
                long start_time = rec.getTime();
                d_trace.indexBlock(rec, block, limit - 1);
                long end_time = rec.getTime();
                TimeTraceBlockInfo bi = new TimeTraceBlockInfo();
                bi.start_time = start_time;
                bi.end_time = end_time;
                bi.block = block;
                d_timeBlockInfoList.add(bi);
            }
            if (ex_count > 0) {
                IDFIOTraceRecordBlockInfo block = (IDFIOTraceRecordBlockInfo) il.elementAt(ex_count - 1);
                limit = d_trace.getBlockRecordCount();
                TimeTraceBlockInfo bi = d_timeInfoBlocks[ex_count - 1];
                d_trace.indexBlock(rec, block, 0);
                bi.start_time = rec.getTime();
                d_trace.indexBlock(rec, block, limit - 1);
                bi.end_time = rec.getTime();
            }
            d_timeInfoBlocks = (TimeTraceBlockInfo[]) d_timeBlockInfoList.toArray(new TimeTraceBlockInfo[d_timeBlockInfoList.size()]);
        } else {
            if (d_lastBlockValidCount != d_trace.getLastBlockValidCount()) {
                changed = true;
                IDFIOTraceRecordBlockInfo last_block = (IDFIOTraceRecordBlockInfo) il.elementAt(il.size() - 1);
                d_trace.indexBlock(rec, last_block, d_lastBlockValidCount - 1);
                long end_time = rec.getTime();
                d_timeInfoBlocks[d_timeInfoBlocks.length - 1].end_time = end_time;
            }
        }
    }

    public IDFIOTraceIterator iterate(long start_time, long end_time, int flags) {
        updateBlockInfo();
        Vector blocks = d_trace.getRecordBlockList();
        iviDFIOTimeTraceIterator it = new iviDFIOTimeTraceIterator(d_trace);
        int start_rec_idx = 0;
        int end_rec_idx = 0;
        if (blocks.size() > d_timeBlockInfoList.size()) {
            int delta = (blocks.size() - d_timeBlockInfoList.size());
            int st_blk = d_timeBlockInfoList.size();
            for (int i = 0; i < delta; i++) {
                TimeTraceBlockInfo info = new TimeTraceBlockInfo();
                IDFIOTraceRecordBlockInfo block = (IDFIOTraceRecordBlockInfo) blocks.elementAt(st_blk + i);
                iviDFIOTraceTimeRecord rec = new iviDFIOTraceTimeRecord();
                int last_idx = d_trace.getBlockRecordCount() - 1;
                if ((i + 1) == delta) {
                    last_idx = d_trace.getLastBlockValidCount() - 1;
                }
                if (last_idx < 0) {
                    continue;
                }
                d_trace.indexBlock(rec, block, 0);
                info.start_time = rec.getTime();
                d_trace.indexBlock(rec, block, last_idx);
                info.end_time = rec.getTime();
                d_timeBlockInfoList.add(info);
            }
            d_timeInfoBlocks = (TimeTraceBlockInfo[]) d_timeBlockInfoList.toArray(new TimeTraceBlockInfo[d_timeBlockInfoList.size()]);
        }
        if ((d_timeInfoBlocks.length == 0) || (start_time > d_timeInfoBlocks[d_timeInfoBlocks.length - 1].end_time) || (end_time < d_timeInfoBlocks[0].start_time)) {
            return it;
        }
        start_rec_idx = findTraceIdx(start_time, flags, false);
        end_rec_idx = findTraceIdx(end_time, flags, true);
        if (start_rec_idx > 0 && (flags & iviDFIOTimeTraceIndexer.IdxFlags_MinusOne) != 0) {
            start_rec_idx--;
        }
        it.d_startIdx = start_rec_idx;
        if (start_rec_idx == end_rec_idx) {
            int last_block_valid = d_trace.getLastBlockValidCount();
            if (start_rec_idx > 0 || last_block_valid > 0) {
                it.d_length = 1;
            } else {
                it.d_length = 0;
            }
        } else {
            it.d_length = (end_rec_idx - start_rec_idx) + 1;
        }
        return it;
    }

    /**
	 * Find an index within the trace blocks.
	 * 
	 * @param time    Target time to search for
	 * @param flags   Extra flags to consider
	 * @param forward whether the time is forward-looking
	 * @return
	 */
    private int findTraceIdx(long time, int flags, boolean forward) {
        iviDFIOTraceTimeRecord rec = new iviDFIOTraceTimeRecord();
        int idx = 0;
        log("    ----> findTraceIdx(" + time + ", forward=" + forward + ")");
        if (d_timeInfoBlocks.length == 0 || d_timeInfoBlocks[0].start_time > time) {
            idx = 0;
        } else {
            boolean block_found = false;
            for (int i = 0; i < d_timeInfoBlocks.length; i++) {
                log("        check block: " + d_timeInfoBlocks[i].start_time + " - " + d_timeInfoBlocks[i].end_time);
                if (time >= d_timeInfoBlocks[i].start_time && time <= d_timeInfoBlocks[i].end_time) {
                    boolean found = false;
                    TimeTraceBlockInfo block_inf = d_timeInfoBlocks[i];
                    block_found = true;
                    int block_valid_count;
                    if ((i + 1) == d_timeInfoBlocks.length) {
                        block_valid_count = d_trace.getLastBlockValidCount();
                    } else {
                        block_valid_count = d_trace.getBlockRecordCount();
                    }
                    if (forward) {
                        log("        narrowing in forward direction");
                        for (int x = 0; x < block_valid_count; x++) {
                            d_trace.indexBlock(rec, block_inf.block, x);
                            long rec_time = rec.getTime();
                            log("        record @ " + rec_time);
                            if (rec_time >= time) {
                                idx = (i * d_trace.getBlockRecordCount() + x);
                                found = true;
                                if ((flags & iviDFIOTimeTraceIndexer.IdxFlags_PlusOne) != 0) {
                                    if (x < d_trace.getBlockRecordCount() - 1 || i < d_timeInfoBlocks.length - 1) {
                                        idx++;
                                    }
                                }
                                break;
                            }
                        }
                    } else {
                        log("        narrowing in reverse direction");
                        for (int x = block_valid_count - 1; x >= 0; x--) {
                            d_trace.indexBlock(rec, block_inf.block, x);
                            long rec_time = rec.getTime();
                            log("        record @ " + rec_time);
                            if (rec_time <= time) {
                                idx = (i * d_trace.getBlockRecordCount() + x);
                                found = true;
                                break;
                            }
                        }
                        if (idx != 0 && (flags & iviDFIOTimeTraceIndexer.IdxFlags_MinusOne) != 0) {
                            idx--;
                        }
                    }
                } else if (i + 1 < d_timeInfoBlocks.length && time > d_timeInfoBlocks[i].end_time && time < d_timeInfoBlocks[i].start_time) {
                    idx = d_trace.getBlockRecordCount() * (i + 1);
                    if (!forward && idx > 0) {
                        idx--;
                    }
                }
                if (block_found) {
                    break;
                }
            }
            if (!block_found && d_timeInfoBlocks.length > 0) {
                idx = (d_timeInfoBlocks.length - 1) * d_trace.getBlockRecordCount();
                if (d_trace.getLastBlockValidCount() > 0) {
                    idx += (d_trace.getLastBlockValidCount() - 1);
                }
            }
        }
        log("    <---- findTraceIdx() => " + idx);
        return idx;
    }

    private class TimeTraceBlockInfo {

        long start_time;

        long end_time;

        IDFIOTraceRecordBlockInfo block;
    }

    private void log(String msg) {
        if (d_debug) {
            System.out.println(msg);
        }
    }

    private static final boolean d_debug = false;

    private IDFIOTrace d_trace;

    private Vector d_timeBlockInfoList;

    private TimeTraceBlockInfo d_timeInfoBlocks[];

    private int d_lastBlockValidCount;
}
