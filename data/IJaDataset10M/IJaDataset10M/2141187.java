package org.amse.bedrosova.logic.dictionary.impl;

import java.math.BigInteger;
import java.util.List;
import org.amse.bedrosova.logic.dictionary.*;
import org.amse.bedrosova.logic.dictionary.impl.SimpleWordContainer.Word;
import org.amse.bedrosova.logic.model.CrossException;

public class WordContainerWithHash extends AbstractWordContainer {

    private final HashTable[] myTables;

    public WordContainerWithHash(int maxlen) {
        super(maxlen);
        myTables = new HashTable[maxlen];
    }

    public IWord getWord(IWord prev, int len, int[] ilist, char[] clist) {
        Word prevw;
        BigInteger mask;
        if (prev == null) {
            if (myTables[len - 1] == null) {
                return null;
            }
            mask = myTables[len - 1].getSuitableWords(ilist, clist);
        } else if (prev instanceof Word) {
            prevw = (Word) prev;
            prevw.getStringInfo().setUsed(false);
            BigInteger m = prevw.getMask();
            mask = m.clearBit(m.getLowestSetBit());
        } else {
            throw new CrossException("Wrong class of word prev.");
        }
        int i = mask.getLowestSetBit();
        while (i != -1 && getWords(len).get(i).isUsed()) {
            mask = mask.clearBit(i);
            i = mask.getLowestSetBit();
        }
        if (i == -1) {
            return null;
        }
        StringInfo si = getWords(len).get(i);
        si.setUsed(true);
        return new Word(si, i, mask, clist, ilist);
    }

    public void addWords(List<String> arr) {
        super.addWords(arr);
        if (arr.size() == 0) {
            return;
        }
        int length = arr.get(0).length();
        myTables[length - 1] = new HashTable(getWords(length));
    }

    public void returnWord(IWord w) {
        if (w instanceof Word) {
            Word word = (Word) w;
            final int len = word.getString().length();
            getWords(len).get(word.getIndex()).setUsed(false);
        } else {
            throw new CrossException("Wrong class of word object.");
        }
    }

    private class HashTable {

        public static final int LETTER_NUMBER = 64;

        public static final int A_LETTER_CODE = 1040;

        public static final char EMPTY_LETTER = (char) 1;

        private final BigInteger[][] myTable;

        public HashTable(List<StringInfo> words) {
            int size = words.get(0).getString().length();
            myTable = new BigInteger[HashTable.LETTER_NUMBER][size];
            for (int i = 0; i < HashTable.LETTER_NUMBER; ++i) {
                for (int j = 0; j < size; ++j) {
                    myTable[i][j] = BigInteger.ZERO;
                }
            }
            for (int w = 0; w < words.size(); ++w) {
                for (int j = 0; j < size; ++j) {
                    StringInfo si = words.get(w);
                    String wordstr = si.getString();
                    int i = (int) wordstr.charAt(j) - HashTable.A_LETTER_CODE;
                    if (i >= 0 && i <= HashTable.LETTER_NUMBER) {
                        myTable[i][j] = myTable[i][j].setBit(w);
                    } else {
                        System.out.println("Invalid word = " + wordstr + " with");
                        System.out.println("Invalid index with letter = " + wordstr.charAt(j));
                        System.out.println(i);
                        throw new CrossException("Invalid word.");
                    }
                }
            }
        }

        public BigInteger getSuitableWords(int[] ilist, char[] clist) {
            if (ilist.length != clist.length) {
                throw new CrossException("Mask's sizes don't match.");
            }
            final int wnum = getWords(myTable[0].length).size();
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < wnum; ++i) {
                sb.append('1');
            }
            BigInteger result = new BigInteger(sb.toString(), 2);
            for (int i = 0; i < ilist.length; ++i) {
                int j = ilist[i];
                int letter = (int) clist[i];
                BigInteger t = myTable[letter - HashTable.A_LETTER_CODE][j];
                result = result.and(t);
            }
            return result;
        }
    }

    protected class Word implements IWord {

        private final StringInfo myStringInfo;

        private final int myIndex;

        private BigInteger myMask;

        private final char[] myFixedChars;

        private final int[] myFixedPositions;

        public Word(StringInfo stri, int index, BigInteger mask, char[] clist, int[] ilist) {
            myStringInfo = stri;
            myIndex = index;
            myMask = mask;
            myFixedChars = clist;
            myFixedPositions = ilist;
        }

        public String getString() {
            return myStringInfo.getString();
        }

        public int getIndex() {
            return myIndex;
        }

        public StringInfo getStringInfo() {
            return myStringInfo;
        }

        public BigInteger getMask() {
            return myMask;
        }

        public void setMask(BigInteger mask) {
            myMask = mask;
        }

        public char[] getFixedChars() {
            return myFixedChars;
        }

        public int[] getFixedPositions() {
            return myFixedPositions;
        }
    }
}
