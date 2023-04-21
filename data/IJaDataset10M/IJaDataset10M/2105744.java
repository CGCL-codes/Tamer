package com.loribel.tools.java.check;

import java.io.File;
import java.io.IOException;
import org.apache.commons.io.filefilter.AbstractFileFilter;
import org.apache.commons.io.filefilter.IOFileFilter;
import com.loribel.commons.abstraction.GB_LabelIcon;
import com.loribel.commons.util.CTools;
import com.loribel.commons.util.FTools;
import com.loribel.commons.util.GB_LabelIconTools;
import com.loribel.commons.util.GB_RegexTools;
import com.loribel.commons.util.STools;
import com.loribel.tools.java.abstraction.GB_JavaProject;
import com.loribel.tools.java.bo.GB_JavaReportBO;
import com.loribel.tools.java.bo.GB_JavaReportFileBO;
import com.loribel.tools.java.bo.GB_JavaReportLineBO;

public class GB_ExtractString extends GB_JavaChecker {

    private int count = 0;

    private GB_LabelIcon labelIcon = GB_LabelIconTools.newLabelIcon("Liste les String");

    public GB_ExtractString(GB_JavaProject a_project) {
        super(a_project);
    }

    protected void explore(File a_file, GB_JavaReportBO a_report) throws IOException {
        String l_className = a_file.getAbsolutePath();
        l_className = l_className.substring(dirSrc.getAbsolutePath().length() + 1, l_className.length() - 5);
        l_className = STools.replace(l_className, File.separator, ".");
        String l_src = FTools.readFile(a_file);
        String[] retour = GB_RegexTools.extractToArray(l_src, "\"([^\"\n]+)\"");
        int len = CTools.getSize(retour);
        GB_JavaReportFileBO l_toAdd;
        if (len > 0) {
            l_toAdd = new GB_JavaReportFileBO(l_className);
            l_toAdd.setFile(a_file);
            a_report.addReportFile(l_toAdd);
            count++;
            System.out.println(count + " - " + l_className);
            for (int i = 0; i < len; i++) {
                String s = retour[i];
                if (acceptString(s)) {
                    System.out.println("  " + s);
                    GB_JavaReportLineBO l_lineReport = new GB_JavaReportLineBO();
                    l_lineReport.setMsg(s);
                    l_lineReport.setPosition(l_src.indexOf("\"" + s + "\""));
                    l_toAdd.addReportLine(l_lineReport);
                }
            }
        }
    }

    private boolean acceptString(String a_str) {
        if (a_str.length() < 4) {
            return false;
        }
        if (GB_RegexTools.matches(a_str, "[\\.\\[\\]0-9,: ]+")) {
            return false;
        }
        return true;
    }

    protected IOFileFilter newFileFilter() {
        return new MyFilter();
    }

    protected String getName() {
        return "Extract String";
    }

    public GB_LabelIcon getLabelIcon() {
        return labelIcon;
    }

    /**
     * Inner class to filter AA.java file.
     */
    private class MyFilter extends AbstractFileFilter {

        public boolean accept(File a_file) {
            String l_name = a_file.getName();
            if ((l_name.endsWith(".java")) && (!l_name.equals("AA.java")) && (!l_name.equals("AAInit.java")) && (!l_name.endsWith("Gen.java")) && (a_file.getAbsolutePath().startsWith(packageSrc))) {
                return true;
            }
            return false;
        }
    }
}
