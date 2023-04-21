package grobid.impl.document;

import grobid.BibDataSet;
import grobid.BiblioItem;
import grobid.Engine;
import grobid.impl.engines.EngineImpl;
import grobid.impl.data.BiblioItemImpl;
import grobid.impl.features.*;
import grobid.impl.layout.*;
import grobid.impl.utilities.TextUtilities;
import grobid.impl.utilities.Utilities;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *  Class for additional information for patent document.
 */
public class PatentDocument extends Document {

    private int beginBlockPAReport = -1;

    public static Pattern searchReport = Pattern.compile("((international|interna(\\s)+Η(\\s)+onal)(\\s)+(search)(\\s)+(report))|" + "((internationaler)(\\s)+(recherchenberich))|" + "(I(\\s)+N(\\s)+T(\\s)+E(\\s)+R(\\s)+N(\\s)+A(\\s)+T(\\s)+I(\\s)+O(\\s)+N(\\s)+A(\\s)+L(\\s)+S(\\s)+E(\\s)+A(\\s)+R(\\s)+C(\\s)+H)", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE);

    public static Pattern FamilyMembers = Pattern.compile("(patent)(\\s)+(famil(v|y))(\\s)+(members)?", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE);

    public PatentDocument() {
    }

    public int getBeginBlockPAReport() {
        return beginBlockPAReport;
    }

    public void setBeginBlockPAReport(int begin) {
        beginBlockPAReport = begin;
    }

    /**
     *  Return all blocks corresponding to the prior art report of a WO patent publication
     */
    public String getWOPriorArtBlocks() {
        System.out.println("getWOPriorArtBlocks");
        StringBuffer accumulated = new StringBuffer();
        int i = 0;
        boolean PAReport = false;
        boolean newPage = false;
        if (blocks != null) {
            for (Block block : blocks) {
                Integer ii = new Integer(i);
                String content = block.getText();
                if (content != null) {
                    content = content.trim();
                    if (newPage & (!PAReport)) {
                        Matcher m = PatentDocument.searchReport.matcher(content);
                        if (m.find()) {
                            PAReport = true;
                            beginBlockPAReport = i;
                        }
                    }
                    if (content.startsWith("@PAGE")) {
                        newPage = true;
                    } else newPage = false;
                    if (PAReport) accumulated.append(content + "\n");
                }
                i++;
            }
        }
        System.out.println(accumulated.toString());
        return accumulated.toString();
    }
}
