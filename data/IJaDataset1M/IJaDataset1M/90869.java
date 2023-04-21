package org.iwtemplatingj;

import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class AccessorParserBase<T> implements AccessorParser<T> {

    public Pattern nameAndIndex = Pattern.compile("^\\s*(\\S+)\\s*\\[\\s*(\\d+)\\s*\\]\\s*$");

    public abstract T followAccessorToken(String name, int index, T element) throws InvalidAccessorException;

    public T getNode(String accessor, T startNode) throws InvalidAccessorException {
        StringTokenizer st = getTokenizer(accessor);
        T currentNode = startNode;
        while (st.hasMoreTokens()) {
            String token = st.nextToken();
            Matcher matcher = nameAndIndex.matcher(token);
            String name;
            int index = 0;
            if (matcher.matches()) {
                name = matcher.group(1);
                index = Integer.parseInt(matcher.group(2));
            } else {
                name = token;
                index = 0;
            }
            currentNode = followAccessorToken(name, index, currentNode);
        }
        return currentNode;
    }

    public StringTokenizer getTokenizer(String accessor) {
        return new StringTokenizer(accessor, ".");
    }
}
