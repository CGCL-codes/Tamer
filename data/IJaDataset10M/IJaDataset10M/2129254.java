package org.configora.parser;

import java.util.Arrays;
import java.util.List;

/**
 * @auther: sandeep dixit.<a href="mailto:dixitsandeep@gmail.com">dixitsandeep@gmail.com</a>
 * @Date: 21-Jun-2009
 * @Time: 20:44:25
 */
public class JavaLanguageUtil {

    public static boolean isKeyword(String word) {
        return keywordList.contains(word);
    }

    static String[] keywords = { "abstract", "continue", "for", "new", "switch", "assert", "default", "if", "package", "synchronized", "boolean", "do", "goto", "private", "this", "break", "double", "implements", "protected", "throw", "byte", "else", "import", "public", "throws", "case", "enum", "instanceof", "return", "transient", "catch", "extends", "int", "short", "try", "char", "final", "interface", "static", "void", "class", "finally", "long", "strictfp", "volatile", "const", "float", "native", "super", "while" };

    static List<String> keywordList = Arrays.asList(keywords);
}
