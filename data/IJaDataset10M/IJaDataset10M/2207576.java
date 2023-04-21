package org.eclipse.bpel.names;

/**
 * @author Michal Chmielewski (michal.chmielewski@oracle.com)
 * @date Aug 6, 2007
 *
 */
public class QNameWordDetector extends XMLNameWordDetector {

    int colCount = 0;

    /**
	 * These always start with a $
	 */
    @Override
    public boolean isWordStart(char c) {
        colCount = 0;
        return super.isWordStart(c);
    }

    /** 
	 *
	 */
    @Override
    public boolean isWordPart(char c) {
        if (c == ':') {
            if (colCount == 0) {
                colCount += 1;
                return true;
            }
            return false;
        }
        return super.isWordPart(c);
    }
}
