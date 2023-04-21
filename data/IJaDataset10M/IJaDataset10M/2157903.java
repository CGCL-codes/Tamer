package org.eclipse.bpel.names;

/**
 * @author Michal Chmielewski (michal.chmielewski@oracle.com)
 * @date Aug 6, 2007
 *
 */
public class NCNameWordDetector extends XMLNameWordDetector {

    /**
	 * 
	 */
    @Override
    public boolean isWordPart(char c) {
        return c != ':' && super.isWordPart(c);
    }
}
