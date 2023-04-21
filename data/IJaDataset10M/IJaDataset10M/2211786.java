package com.protomatter.util;

import java.util.*;

/**
 *  A string tokenizer.  Behaves like <tt>java.util.StringTokenizer</tt>
 *  extept that two delimiters next to eachother are recognized as
 *  two separate delimiters.  For instance, splitting "<tt>foo||bar</tt>"
 *  using "<tt>|</tt>" as the delimiter will return three tokens
 *  ("<tt>foo</tt>", "" (empty string), and "<tt>bar</tt>").
 */
public class ProtoStringTokenizer {

    private String string;

    private int index = 0;

    private int length = 0;

    private Dictionary tokens = new Hashtable();

    /**
   *  Create a new ProtoStringTokenizer with the given string
   *  to tokenize and the given list of tokens.
   */
    public ProtoStringTokenizer(String string, String tokens) {
        this.string = string;
        this.length = string.length();
        String value = "";
        for (int i = 0; i < tokens.length(); i++) this.tokens.put(new Character(tokens.charAt(i)), value);
    }

    /**
   *  Determine if there are more tokens available
   */
    public boolean hasMoreTokens() {
        return (index < length);
    }

    /**
   *  Get the next token
   */
    public String nextToken() {
        String s = this.string;
        for (int i = index; i < length; i++) {
            if (tokens.get(new Character(s.charAt(i))) != null) {
                String ret = s.substring(index, i);
                this.index = i + 1;
                return ret;
            }
        }
        String ret = s.substring(index, length);
        this.index = length;
        return ret;
    }

    /**
   *  A test program.
   */
    public static void main(String args[]) {
        if (args.length != 2) {
            System.out.println("Usage: ProtoStringTokenizer string delim");
            System.exit(0);
        }
        ProtoStringTokenizer st = new ProtoStringTokenizer(args[0], args[1]);
        System.out.println("String = '" + args[0] + "'");
        System.out.println("Delim  = '" + args[1] + "'");
        while (st.hasMoreTokens()) {
            System.out.println("Token = '" + st.nextToken() + "'");
        }
    }
}
