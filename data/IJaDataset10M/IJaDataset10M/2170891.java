package org.eugenes.index;

import java.io.*;
import java.util.*;
import java.util.regex.*;
import java.text.SimpleDateFormat;
import org.apache.lucene.analysis.CharTokenizer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.TokenFilter;
import org.apache.lucene.analysis.Token;
import org.apache.lucene.analysis.standard.StandardTokenizer;
import org.apache.lucene.analysis.standard.StandardTokenizerConstants;
import org.eugenes.index.biodata.DataFilter;
import org.eugenes.index.biodata.DataTokenizer;

/**
 * BiodataFilters
   
   A collection of small classes for lucene indexing of biology data, mostly
   to allow for field values with symbols, case-sensitivity, various
   'word' breaking, 
   
   Includes both lucene.analysis.Tokenizer (1st to handle data stream)
     and lucene.analysis.TokenFilter (further token parsing from Tokenizers)
     
   Separated from BiodataAnalyzer class, nov 2004
   
   See org.eugenes.index.LuceneBaseIndexer
   
   Author: D.Gilbert, gilbertd@eugenes.org
 */
public class BiodataFilters {

    public static class NumberField {

        private NumberField() {
        }

        private static int NUM_RADIX = 10;

        private static int NUM_LEN = Integer.toString(1 + Integer.MAX_VALUE, NUM_RADIX).length();

        public static String MIN_NUM_STRING() {
            return numToString(0);
        }

        public static String MAX_NUM_STRING() {
            char[] buffer = new char[NUM_LEN];
            char c = Character.forDigit(NUM_RADIX - 1, NUM_RADIX);
            for (int i = 0; i < NUM_LEN; i++) buffer[i] = c;
            return new String(buffer);
        }

        public static String numToString(int num) {
            boolean neg = (num < 0);
            if (neg) num = -num;
            String s = Integer.toString(num, NUM_RADIX);
            if (s.length() > NUM_LEN) throw new RuntimeException("num too long");
            if (s.length() < NUM_LEN) {
                StringBuffer sb = new StringBuffer(s);
                while (sb.length() < NUM_LEN) sb.insert(0, 0);
                if (neg) sb.insert(0, '-');
                s = sb.toString();
            }
            return s;
        }

        public static int stringToNum(String s) {
            return Integer.parseInt(s, NUM_RADIX);
        }
    }

    public static class LongNumberField {

        private LongNumberField() {
        }

        private static int NUM_RADIX = 10;

        private static int NUM_LEN = Long.toString(1 + Long.MAX_VALUE, NUM_RADIX).length();

        public static String MIN_NUM_STRING() {
            return numToString(0);
        }

        public static String MAX_NUM_STRING() {
            char[] buffer = new char[NUM_LEN];
            char c = Character.forDigit(NUM_RADIX - 1, NUM_RADIX);
            for (int i = 0; i < NUM_LEN; i++) buffer[i] = c;
            return new String(buffer);
        }

        public static String numToString(long num) {
            boolean neg = (num < 0);
            if (neg) num = -num;
            String s = Long.toString(num, NUM_RADIX);
            if (s.length() > NUM_LEN) throw new RuntimeException("num too long");
            if (s.length() < NUM_LEN) {
                StringBuffer sb = new StringBuffer(s);
                while (sb.length() < NUM_LEN) sb.insert(0, 0);
                if (neg) sb.insert(0, '-');
                s = sb.toString();
            }
            return s;
        }

        public static long stringToNum(String s) {
            return Long.parseLong(s, NUM_RADIX);
        }
    }

    public static class PlainDataFilter extends DataFilter {

        public PlainDataFilter() {
            super();
        }

        public PlainDataFilter(TokenStream input) {
            super(input);
        }
    }

    public static class LowerDataFilter extends DataFilter {

        public LowerDataFilter() {
            super();
        }

        public LowerDataFilter(TokenStream input) {
            super(input);
        }

        public Token next() throws IOException {
            Token t = input.next();
            if (t == null) return null;
            return new Token(t.termText().toLowerCase(), t.startOffset(), t.endOffset(), t.type());
        }
    }

    public static class DebugFilter extends DataFilter {

        public Token next() throws IOException {
            Token t = input.next();
            if (t == null) return null;
            if (LuceneBaseIndexer.debug) LuceneBaseIndexer.logp.println(this.getField() + ":" + t.termText());
            return t;
        }
    }

    public static class NullFilter extends DataFilter {

        public Token next() {
            return null;
        }
    }

    public static class NumberFilter extends DataFilter {

        public Token next() throws IOException {
            Token t = input.next();
            if (t == null) return null;
            try {
                String text = t.termText();
                String nums = NumberField.numToString(Integer.parseInt(text));
                t = new Token(nums, t.startOffset(), t.endOffset(), t.type());
                return t;
            } catch (Exception e) {
                return null;
            }
        }
    }

    public static class DateTokens extends DataTokenizer {

        public DateTokens(Reader in) {
            super(in);
        }

        public DateTokens() {
            super();
        }

        protected boolean isTokenChar(char c) {
            return !(c == ';');
        }

        protected char normalize(char c) {
            return c;
        }
    }

    public static class DateFilter extends DataFilter {

        static SimpleDateFormat df, todf;

        static {
            df = new SimpleDateFormat("dd MMM yy");
            df.setLenient(true);
            todf = new SimpleDateFormat("yyyyMMdd");
        }

        public Token next() throws IOException {
            Token t = input.next();
            if (t != null) try {
                String text = t.termText();
                Date d1 = df.parse(text);
                text = todf.format(d1);
                return new Token(text, t.startOffset(), t.endOffset(), t.type());
            } catch (Exception e) {
            }
            return t;
        }
    }

    public static class PlainDataTokenizer extends DataTokenizer {

        public PlainDataTokenizer(Reader in) {
            super(in);
        }

        public PlainDataTokenizer() {
            super();
        }
    }

    public static class LowerDataTokenizer extends DataTokenizer {

        public LowerDataTokenizer(Reader in) {
            super(in);
        }

        public LowerDataTokenizer() {
            super();
        }

        protected char normalize(char c) {
            return Character.toLowerCase(c);
        }
    }

    public static class WordTokenizer extends DataTokenizer {

        public WordTokenizer(Reader in) {
            super(in);
        }

        public WordTokenizer() {
            super();
        }

        protected boolean isTokenChar(char c) {
            return Character.isLetterOrDigit(c);
        }
    }

    public static class LowerWordTokenizer extends DataTokenizer {

        public LowerWordTokenizer(Reader in) {
            super(in);
        }

        public LowerWordTokenizer() {
            super();
        }

        protected boolean isTokenChar(char c) {
            return Character.isLetterOrDigit(c);
        }

        protected char normalize(char c) {
            return Character.toLowerCase(c);
        }
    }
}
