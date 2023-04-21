package heisig.output;

import heisig.data.KanjiFrame;

/**
 * Outputs KanjiFrame in the same format as read from heisig-data.txt.
 * It is used to compute a heisig-data.txt that is sorted by kanji id.
 * (Sorting is done by OutputFormatter)
 * 
 * @author Andi
 *
 */
public class NoChangeOutputFormat extends OutputFormat {

    public String computeHeader() {
        return "#file version 1.0.11 2006-05-27\n#heisignumber:kanji:keyword:strokecount:indexordinal:lessonnumber\n";
    }

    public String computeOutputFormat(KanjiFrame frame) {
        System.out.println(frame);
        StringBuilder sb = new StringBuilder();
        sb.append(frame.getHeisigNumber()).append(":");
        sb.append(frame.getKanji()).append(":");
        sb.append(frame.getKeyword()).append(":");
        sb.append(frame.getStrokeCount()).append(":");
        sb.append(frame.getIndexOrdinal()).append(":");
        sb.append(frame.getLessonNumber()).append("\n");
        return sb.toString();
    }

    public String computeFooter() {
        return "";
    }
}
