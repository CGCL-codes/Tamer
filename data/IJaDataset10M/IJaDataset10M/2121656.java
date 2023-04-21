package com.metasolutions.jfcml.helpers;

import com.metasolutions.util.SimpleStack;

/**
 * A utility class used to parse simple Java statements containing method calls. 
 *
 * @author Shawn Curry 
 * @version 0.9 May 28, 2005
 */
public class MethodParser {

    private static final int PARSE_NAME = 0;

    private static final int PARSE_ARGS = 1;

    public static void main(String[] args) {
        MethodParser p = new MethodParser();
        SimpleStack methods = p.parse("new URL('One, Two\\',( Three', 'One,{ Two\\', Three')");
        for (int i = 0; i < methods.size(); ++i) {
            MethodText t = (MethodText) methods.elementAt(i);
            System.out.println("---------------------------------------");
            System.out.println("methodtext: " + t.getMethodText());
            System.out.println("name: " + t.getName());
            System.out.println("ismethod: " + t.isMethod());
            SimpleStack s = t.getArguments();
            for (int j = 0; j < s.size(); ++j) System.out.println("arg" + j + ": " + (String) s.elementAt(j));
        }
    }

    public SimpleStack parse(String attrs) {
        SimpleStack methods = new SimpleStack();
        SimpleStack args = new SimpleStack();
        String name = null;
        StringBuffer buf = new StringBuffer();
        int state = PARSE_NAME;
        int paren = 0;
        int brace = 0;
        int len = attrs.length();
        for (int i = 0; i < len; ++i) {
            char c = attrs.charAt(i);
            switch(c) {
                case '\'':
                    int next = attrs.indexOf('\'', i + 1);
                    if (next == -1) throw new RuntimeException("Unmatched quotation mark for index: " + i);
                    String s = attrs.substring(i, next + 1);
                    buf.append(s);
                    i = next;
                    while (s.charAt(s.length() - 2) == '\\') {
                        next = attrs.indexOf('\'', i + 1);
                        if (next == -1) throw new RuntimeException("Unmatched quotation mark for escape char at index: " + i);
                        s = attrs.substring(i, next + 1);
                        buf.append(s);
                        i = next;
                    }
                    break;
                case '{':
                    ++brace;
                    if (state == PARSE_ARGS) buf.append(c); else {
                        name = buf.toString().trim();
                        buf = new StringBuffer();
                        state = PARSE_ARGS;
                    }
                    break;
                case '(':
                    ++paren;
                    if (state == PARSE_ARGS) buf.append(c); else {
                        name = buf.toString().trim();
                        buf = new StringBuffer();
                        state = PARSE_ARGS;
                    }
                    break;
                case '}':
                    --brace;
                    buf.append(c);
                    if (brace < 0 || paren < 0) throw new RuntimeException("Extra parenthesis/brace at index " + i + " " + attrs);
                    break;
                case ')':
                    --paren;
                    if (brace == 0 && paren == 0) {
                        String str = buf.toString().trim();
                        if (str.length() > 0) args.push(str);
                        methods.push(new MethodText(name, args));
                        buf = new StringBuffer();
                        args = new SimpleStack();
                        args.clear();
                        name = null;
                        state = PARSE_NAME;
                    } else if (paren == 0) {
                        String str = buf.toString().trim();
                        if (str.length() > 0) args.push(str);
                        buf = new StringBuffer();
                    } else buf.append(c);
                    if (brace < 0 || paren < 0) throw new RuntimeException("Extra parenthesis/brace at index " + i + " " + attrs);
                    break;
                case ',':
                    if (paren == 1 && brace == 0) {
                        String str = buf.toString().trim();
                        if (str.length() > 0) args.push(str);
                        buf = new StringBuffer();
                    } else buf.append(c);
                    break;
                case '.':
                    if (state == PARSE_NAME) {
                        String str = buf.toString().trim();
                        if (str.length() > 0) methods.push(new MethodText(str)); else if (methods.size() == 0) throw new RuntimeException("Statement must not begin with '.' : " + attrs);
                        buf = new StringBuffer();
                    } else buf.append(c);
                    break;
                default:
                    buf.append(c);
            }
        }
        if (paren != 0 || brace != 0) throw new RuntimeException("Missing parenthesis: " + attrs);
        String str = buf.toString().trim();
        if (str.length() > 0) methods.push(new MethodText(str));
        return methods;
    }
}
