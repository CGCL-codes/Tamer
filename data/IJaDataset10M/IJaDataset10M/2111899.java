package net.kano.joscar.snaccmd.search;

import net.kano.joscar.BinaryTools;
import net.kano.joscar.ByteBlock;
import net.kano.joscar.DefensiveTools;
import net.kano.joscar.flapcmd.SnacPacket;
import net.kano.joscar.snaccmd.DirInfo;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Collection;

/**
 * A SNAC command containing a list of search results. Normally sent in response
 * to a {@link SearchBuddiesCmd}.
 *
 * @snac.src server
 * @snac.cmd 0x0f 0x03
 *
 * @see SearchBuddiesCmd
 */
public class SearchResultsCmd extends SearchCommand {

    /** The only result code I've ever seen. */
    public static final int CODE_DEFAULT = 0x0005;

    /** The only result subcode I've ever seen. */
    public static final int SUBCODE_DEFAULT = 0x0000;

    /** Some sort of result code. */
    private final int code;

    /** Some sort of result subcode. */
    private final int subCode;

    /** A list of results. */
    private final List<DirInfo> results;

    /**
     * Generates a new search result list command from the given incoming SNAC
     * packet.
     *
     * @param packet an incoming search result list packet
     */
    protected SearchResultsCmd(SnacPacket packet) {
        super(CMD_RESULTS);
        DefensiveTools.checkNull(packet, "packet");
        ByteBlock snacData = packet.getData();
        code = BinaryTools.getUShort(snacData, 0);
        subCode = BinaryTools.getUShort(snacData, 2);
        if (snacData.getLength() >= 6) {
            int resultCount = BinaryTools.getUShort(snacData, 4);
            List<DirInfo> resultList = new ArrayList<DirInfo>();
            ByteBlock block = snacData.subBlock(6);
            for (int i = 0; i < resultCount; i++) {
                int tlvCount = BinaryTools.getUShort(block, 0);
                ByteBlock dirBlock = block.subBlock(2);
                DirInfo dirInfo = DirInfo.readDirInfo(dirBlock, tlvCount);
                if (dirInfo == null) break;
                resultList.add(dirInfo);
                block = block.subBlock(2 + dirInfo.getTotalSize());
            }
            results = DefensiveTools.getUnmodifiable(resultList);
        } else {
            results = null;
        }
    }

    /**
     * Creates a new outgoing search results command with the given list of
     * results. The code and subcode are set to {@link #CODE_DEFAULT} and
     * {@link #SUBCODE_DEFAULT}, respectively. Using this constructor is
     * equivalent to using {@link #SearchResultsCmd(int, int, Collection<DirInfo>) new
     * SearchResultsCmd(SearchResultsCmd.CODE_DEFAULT,
     * SearchResultsCmd.SUBCODE_DEFAULT, results)}.
     *
     * @param results the list of reuslts to send in this command
     */
    public SearchResultsCmd(Collection<DirInfo> results) {
        this(CODE_DEFAULT, SUBCODE_DEFAULT, results);
    }

    /**
     * Creates a new outgoing search results command with the given list of
     * results and the given code and subcode.
     *
     * @param code a result code, normally {@link #CODE_DEFAULT}
     * @param subCode a result subcode, normally {@link #SUBCODE_DEFAULT}
     * @param results a list of results, or <code>null</code> for none
     */
    public SearchResultsCmd(int code, int subCode, Collection<DirInfo> results) {
        super(CMD_RESULTS);
        DefensiveTools.checkRange(code, "code", 0);
        DefensiveTools.checkRange(subCode, "subCode", 0);
        this.code = code;
        this.subCode = subCode;
        this.results = DefensiveTools.getSafeNonnullListCopy(results, "results");
    }

    /**
     * Returns the result code sent in this command. Normally {@link
     * #CODE_DEFAULT}.
     *
     * @return the result code associated with these search results
     */
    public final int getResultCode() {
        return code;
    }

    /**
     * Returns the result "subcode" sent in this command. Normally {@link
     * #SUBCODE_DEFAULT}.
     *
     * @return the result "subcode" associated with these search results
     */
    public final int getResultSubCode() {
        return subCode;
    }

    /**
     * Returns the list of results sent in this command, or <code>null</code>
     * if none were sent.
     *
     * @return the search results
     */
    public List<DirInfo> getResults() {
        return results;
    }

    public void writeData(OutputStream out) throws IOException {
        BinaryTools.writeUShort(out, code);
        BinaryTools.writeUShort(out, subCode);
        if (results != null) {
            BinaryTools.writeUShort(out, results.size());
            for (DirInfo result : results) {
                BinaryTools.writeUShort(out, result.getTlvCount());
                result.write(out);
            }
        }
    }

    public String toString() {
        return "SearchResultsCmd: " + results.size() + " results";
    }
}
