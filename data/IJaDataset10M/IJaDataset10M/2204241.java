package org.wikipediacleaner.api.check.algorithm;

import java.util.Collection;
import org.wikipediacleaner.api.check.CheckErrorResult;
import org.wikipediacleaner.api.data.PageAnalysis;
import org.wikipediacleaner.api.data.PageElementTitle;

/**
 * Algorithm for analyzing error 7 of check wikipedia project.
 * Error 7: Headlines all start with three "="
 */
public class CheckErrorAlgorithm007 extends CheckErrorAlgorithmBase {

    public CheckErrorAlgorithm007() {
        super("Headlines all start with three \"=\"");
    }

    /**
   * Analyze a page to check if errors are present.
   * 
   * @param pageAnalysis Page analysis.
   * @param errors Errors found in the page.
   * @return Flag indicating if the error was found.
   */
    public boolean analyze(PageAnalysis pageAnalysis, Collection<CheckErrorResult> errors) {
        if (pageAnalysis == null) {
            return false;
        }
        String contents = pageAnalysis.getContents();
        PageElementTitle firstTitle = pageAnalysis.getNextTitle(0);
        if (firstTitle == null) {
            return false;
        }
        if (firstTitle.getFirstLevel() < 3) {
            return false;
        }
        int startIndex = firstTitle.getEndIndex();
        while (startIndex < contents.length()) {
            PageElementTitle title = pageAnalysis.getNextTitle(startIndex);
            if (title == null) {
                startIndex = contents.length();
            } else {
                if (title.getFirstLevel() < 3) {
                    return false;
                }
                startIndex = title.getEndIndex();
            }
        }
        if (errors == null) {
            return true;
        }
        errors.add(createCheckErrorResult(pageAnalysis.getPage(), firstTitle.getBeginIndex(), firstTitle.getEndIndex()));
        return true;
    }
}
