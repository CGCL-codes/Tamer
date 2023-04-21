package org.helium.transept.util;

import java.net.InetAddress;
import java.util.StringTokenizer;
import com.sun.java.util.collections.ArrayList;
import com.sun.java.util.collections.Collections;
import com.sun.java.util.collections.List;
import org.helium.transept.exception.MalformedPatternException;

public class IPPatterns {

    private IPPattern[] patterns = null;

    private boolean[] negatePattern = null;

    private boolean singleSense = false;

    public IPPatterns(String patternText) throws MalformedPatternException {
        parsePatterns(patternText);
    }

    public void parsePatterns(String patternsText) throws MalformedPatternException {
        StringTokenizer tokens = new StringTokenizer(patternsText, "\r\n", false);
        List patternsList = new ArrayList();
        List negateList = new ArrayList();
        while (tokens.hasMoreTokens()) {
            String token = tokens.nextToken();
            if (token == null) {
                continue;
            }
            String trimmedToken = token.trim();
            if (token.trim().length() == 0) {
                continue;
            }
            Boolean negate = Boolean.FALSE;
            if (trimmedToken.charAt(0) == '!') {
                negate = Boolean.TRUE;
                trimmedToken = trimmedToken.substring(1).trim();
            }
            if (trimmedToken.length() == 0) {
                throw new MalformedPatternException("Attempted to negate an empty line");
            }
            IPPattern pattern = new IPPattern();
            pattern.parsePattern(trimmedToken);
            patternsList.add(pattern);
            negateList.add(negate);
        }
        patterns = (IPPattern[]) (patternsList.toArray(new IPPattern[1]));
        negatePattern = new boolean[negateList.size()];
        boolean allNegated = true;
        boolean allPositive = true;
        for (int i = 0; i < negatePattern.length; i++) {
            negatePattern[i] = ((Boolean) (negateList.get(i))).booleanValue();
            allNegated &= negatePattern[i];
            allPositive &= !(negatePattern[i]);
        }
        singleSense = (allNegated ^ allPositive);
    }

    public boolean match(InetAddress addr) {
        return match(addr.getAddress());
    }

    public boolean match(byte[] addr) {
        if (singleSense) {
            for (int i = 0; i < patterns.length; i++) {
                if (patterns[i].match(addr) ^ negatePattern[i]) {
                    return true;
                }
            }
            return false;
        } else {
            boolean isMatch = false;
            for (int i = 0; i < patterns.length; i++) {
                boolean match = patterns[i].match(addr);
                if (match) {
                    isMatch = !(negatePattern[i]);
                }
            }
            return isMatch;
        }
    }
}
