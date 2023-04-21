package com.daffodilwoods.daffodildb.server.sql99.fulltext.common;

import java.io.*;

/**
 * This Class represents  list of stopwords that are ignored during parsing
 * like a,an,the etc.It provides functionality to check whether token is among of
 * stop word or not.
  */
public class StopWords {

    /**
   * English_stop_word is byte array which contain all list of stop words
   * that we have to ignored during parsing.
   */
    public static final byte[][] ENGLISH_STOP_WORDS = { { (byte) 39 }, { (byte) 97 }, { (byte) 97, (byte) 110 }, { (byte) 97, (byte) 110, (byte) 100 }, { (byte) 97, (byte) 114, (byte) 101 }, { (byte) 97, (byte) 115 }, { (byte) 97, (byte) 116 }, { (byte) 98, (byte) 101 }, { (byte) 98, (byte) 117, (byte) 116 }, { (byte) 98, (byte) 121 }, { (byte) 102, (byte) 111, (byte) 114 }, { (byte) 105, (byte) 102 }, { (byte) 105, (byte) 110 }, { (byte) 105, (byte) 110, (byte) 116, (byte) 111 }, { (byte) 105, (byte) 115 }, { (byte) 105, (byte) 116 }, { (byte) 110, (byte) 111 }, { (byte) 110, (byte) 111, (byte) 116 }, { (byte) 111, (byte) 102 }, { (byte) 111, (byte) 110 }, { (byte) 111, (byte) 114 }, { (byte) 115 }, { (byte) 115, (byte) 117, (byte) 99, (byte) 104 }, { (byte) 116 }, { (byte) 116, (byte) 104, (byte) 97, (byte) 116 }, { (byte) 116, (byte) 104, (byte) 101 }, { (byte) 116, (byte) 104, (byte) 101, (byte) 105, (byte) 114 }, { (byte) 116, (byte) 104, (byte) 101, (byte) 110 }, { (byte) 116, (byte) 104, (byte) 101, (byte) 114, (byte) 101 }, { (byte) 116, (byte) 104, (byte) 101, (byte) 115, (byte) 101 }, { (byte) 116, (byte) 104, (byte) 101, (byte) 121 }, { (byte) 116, (byte) 104, (byte) 105, (byte) 115 }, { (byte) 116, (byte) 111 }, { (byte) 119, (byte) 97, (byte) 115 }, { (byte) 119, (byte) 105, (byte) 108, (byte) 108 }, { (byte) 119, (byte) 105, (byte) 116, (byte) 104 }, { (byte) 10 } };

    public StopWords() {
    }

    /**
   * It checks whether token passed is stopword or not.if it is stopword return true
   * else return false.
   * @param token
   * @return
   */
    public static boolean checkStopWords(byte[] token) {
        Outer: for (int i = 0; i < ENGLISH_STOP_WORDS.length; i++) {
            byte[] stopWord = ENGLISH_STOP_WORDS[i];
            if (token.length == stopWord.length) {
                for (int j = 0; j < token.length; j++) {
                    if (token[j] != stopWord[j]) continue Outer;
                }
                return true;
            }
        }
        return false;
    }
}
