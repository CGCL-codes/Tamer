package org.smx.captcha.impl;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.util.ArrayList;
import java.util.Random;
import org.smx.captcha.IWordFactory;

public class FactoryLanguageImpl extends IWordFactory {

    private String language = "EN";

    private String languageDirectory;

    private String encoding;

    private static ArrayList wordData;

    private int min = 0;

    private int max = 20;

    public FactoryLanguageImpl() {
    }

    public static IWordFactory getInstance() {
        if (instance == null) {
            instance = new FactoryLanguageImpl();
        }
        return instance;
    }

    public void setLanguage(final String language) {
        this.language = language;
    }

    /**
	 * Absolute Directory Location of language files,
	 * The path should end with \ if not then we will add it
	 * @param languageDirectory
	 */
    public void setLanguageDirectory(String lang) {
        languageDirectory = lang;
        if (!languageDirectory.endsWith("\\") || !languageDirectory.endsWith("/")) {
            if (languageDirectory.indexOf("/") > -1) {
                languageDirectory += "/";
            } else {
                languageDirectory += "\\";
            }
        }
    }

    /**
	 * Range the returned word should be in 
	 * @param min
	 * @param max
	 */
    public void setRange(final int min, final int max) {
        this.min = min;
        this.max = max;
        if (min == max) {
            this.max++;
        }
        if (min > max) {
            this.min = max;
            this.max = min;
        }
    }

    /**
	 * Return the word in it's natural Encoding
	 * If we use System.out.println we will get ?????? 
	 * We will need to change our system encoding first to view 
	 */
    public String getWord() {
        if (wordData == null) {
            load();
        }
        word = "";
        int len = 0;
        Random rnd = new Random(System.currentTimeMillis());
        do {
            word = (String) wordData.get(rnd.nextInt(wordData.size()));
            len = word.length();
        } while (len >= max || len < min);
        CharsetDecoder decoder = Charset.forName(encoding).newDecoder();
        try {
            CharBuffer cbuf = decoder.decode(ByteBuffer.wrap(word.getBytes()));
            word = cbuf.toString();
        } catch (CharacterCodingException e) {
            e.printStackTrace();
        }
        return (word = word.toUpperCase());
    }

    public String getHashCode() {
        return getHashCode(word);
    }

    public String getHashCode(String str) {
        String out = "";
        CharsetDecoder decoder = Charset.forName(encoding).newDecoder();
        try {
            CharBuffer cbuf = decoder.decode(ByteBuffer.wrap(str.getBytes()));
            out = cbuf.toString();
        } catch (CharacterCodingException e) {
            e.printStackTrace();
        }
        return "" + out.hashCode();
    }

    private void load() {
        BufferedReader bis = null;
        BufferedReader encodingReader = null;
        try {
            if (languageDirectory == null || languageDirectory.equals("")) {
                throw new Exception(" languageDirectory not set ");
            }
            String sFilename = language + ".dic";
            wordData = new ArrayList();
            File srcFileEncoding = new File(languageDirectory + language + ".aff");
            encodingReader = new BufferedReader(new FileReader(srcFileEncoding));
            if (encodingReader != null) {
                encoding = encodingReader.readLine();
                encoding = (encoding == null) ? "ISO-8859-1" : encoding;
            }
            File srcFile = new File(languageDirectory + sFilename);
            bis = new BufferedReader(new InputStreamReader(new FileInputStream(srcFile)));
            String line = null;
            while ((line = bis.readLine()) != null) {
                wordData.add(line);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            try {
                if (bis != null) {
                    bis.close();
                    bis = null;
                }
                if (encodingReader != null) {
                    encodingReader.close();
                    encodingReader = null;
                }
            } catch (Exception e) {
            }
        }
    }
}
