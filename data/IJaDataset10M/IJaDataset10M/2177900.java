package org.jcvi.vics.shared.blast;

import org.jcvi.vics.model.genomics.BlastHit;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: lkagan
 * Date: Mar 24, 2009
 * Time: 5:42:48 PM
 * <p/>
 * Attempt to mimic the output of the JCVI BPbtab perl script (/usr/local/common/BPbtab);
 * this output format IS NOT the same as the NCBI btab tool
 */
public class BlastBtabWriter extends BlastWriter {

    private String dateOfAnalysis;

    public BlastBtabWriter() {
        SimpleDateFormat df = new SimpleDateFormat("MMM dd yyyy");
        dateOfAnalysis = df.format(new Date());
    }

    public void writeTopPortion() throws Exception {
    }

    public void writeBottomPortion() throws Exception {
    }

    public void startQueryPortion() throws Exception {
    }

    public void endQueryPortion() throws Exception {
    }

    /**
     * For this format each row in a file is an HSP
     *
     * @param qID
     * @param pbrList
     * @param queryCounter
     * @throws Exception
     */
    public void writeSingleQueryPortion(String qID, List<ParsedBlastResult> pbrList, long queryCounter) throws Exception {
        for (ParsedBlastResult pbr : pbrList) {
            for (ParsedBlastHSP hsp : pbr.getHspList()) {
                bufferedWriter.print(qID + "\t");
                bufferedWriter.print(dateOfAnalysis + "\t");
                bufferedWriter.print(String.valueOf(pbr.getQueryLength()) + "\t");
                bufferedWriter.print(task.getTaskName() + "\t");
                bufferedWriter.print(getBlastDB() + "\t");
                bufferedWriter.print(pbr.getSubjectId() + "\t");
                HSPCoordinates hspCoordinates = new HSPCoordinates(hsp);
                bufferedWriter.print(String.valueOf(hspCoordinates.getQueryBeginCoordinate()) + "\t");
                bufferedWriter.print(String.valueOf(hspCoordinates.getQueryEndCoordinate()) + "\t");
                bufferedWriter.print(String.valueOf(hspCoordinates.getSubjectBeginCoordinate()) + "\t");
                bufferedWriter.print(String.valueOf(hspCoordinates.getSubjectEndCoordinate()) + "\t");
                bufferedWriter.print(String.valueOf(getPercentIdentity(hsp)) + "\t");
                bufferedWriter.print(String.valueOf(getPercentSimilarity(hsp)) + "\t");
                bufferedWriter.print(String.valueOf(hsp.getHspScore()) + "\t");
                bufferedWriter.print(String.valueOf(hsp.getBitScore()) + "\t");
                bufferedWriter.print("\t");
                bufferedWriter.print(getSubjectComment(pbr) + "\t");
                bufferedWriter.print(String.valueOf(hsp.getQueryFrame()) + "\t");
                bufferedWriter.print(getStrandIdentifier(hsp.getQueryOrientation()) + "\t");
                bufferedWriter.print(String.valueOf(pbr.getSubjectLength()) + "\t");
                bufferedWriter.print(String.valueOf(hsp.getExpectScore()) + "\t");
                bufferedWriter.println("");
            }
        }
    }

    private String getSubjectComment(ParsedBlastResult pbr) {
        String comment = (pbr.getSubjectDefline() != null) ? pbr.getSubjectDefline() : deflineMap.get(pbr.getSubjectId());
        if (comment != null) {
            comment = comment.replace(pbr.getSubjectId(), "");
            comment = comment.trim();
        }
        return comment;
    }

    private String getStrandIdentifier(Integer orientation) {
        String strandIdentifier;
        if (BlastHit.ALGN_ORI_FORWARD.equals(orientation)) {
            strandIdentifier = "Plus";
        } else if (BlastHit.ALGN_ORI_REVERSE.equals(orientation)) {
            strandIdentifier = "Minus";
        } else {
            strandIdentifier = "null";
        }
        return strandIdentifier;
    }
}
