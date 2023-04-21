package org.wikipediacleaner.api.check.algorithm;

import java.util.Collection;
import org.wikipediacleaner.api.check.CheckErrorResult;
import org.wikipediacleaner.api.data.PageAnalysis;

/**
 * Algorithm for analyzing error 43 of check wikipedia project.
 * Error 43: Template not correct end
 */
public class CheckErrorAlgorithm043 extends CheckErrorAlgorithmBase {

    public CheckErrorAlgorithm043() {
        super("Template not correct end");
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
        int startIndex = contents.length();
        boolean result = false;
        int beginIndex = contents.lastIndexOf("{{", startIndex);
        int endIndex = contents.lastIndexOf("}}", startIndex);
        int count = 0;
        while (startIndex > 0) {
            if ((beginIndex < 0) && (endIndex < 0)) {
                startIndex = 0;
            } else if ((endIndex >= 0) && ((beginIndex < endIndex) || (beginIndex < 0))) {
                count++;
                startIndex = endIndex;
                endIndex = contents.lastIndexOf("}}", startIndex - 1);
            } else {
                count--;
                if (count < 0) {
                    if (errors == null) {
                        return true;
                    }
                    result = true;
                    boolean errorReported = false;
                    int nextEnd = contents.indexOf('}', beginIndex + 2);
                    if (nextEnd > 0) {
                        int nextCR = contents.indexOf('\n', beginIndex + 2);
                        int nextBegin = contents.indexOf('{', beginIndex + 2);
                        if (((nextCR < 0) || (nextCR > nextEnd)) && ((nextBegin < 0) || (nextBegin > nextEnd))) {
                            CheckErrorResult errorResult = createCheckErrorResult(pageAnalysis.getPage(), beginIndex, nextEnd + 1);
                            errorResult.addReplacement(contents.substring(beginIndex, nextEnd + 1) + "}");
                            errors.add(errorResult);
                            errorReported = true;
                        }
                    }
                    if (!errorReported) {
                        errors.add(createCheckErrorResult(pageAnalysis.getPage(), beginIndex, beginIndex + 2));
                    }
                    count = 0;
                }
                startIndex = beginIndex;
                beginIndex = contents.lastIndexOf("{{", startIndex - 1);
            }
        }
        return result;
    }
}
