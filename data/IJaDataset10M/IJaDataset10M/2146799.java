package android.text;

import android.content.res.Resources;
import android.content.res.XmlResourceParser;
import com.android.internal.util.XmlUtils;
import android.view.View;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;
import java.io.IOException;
import java.util.Locale;
import java.io.FileInputStream;
import java.io.BufferedInputStream;

/**
 * This class accesses a dictionary of corrections to frequent misspellings.
 */
public class AutoText {

    private static final int TRIE_C = 0;

    private static final int TRIE_OFF = 1;

    private static final int TRIE_CHILD = 2;

    private static final int TRIE_NEXT = 3;

    private static final int TRIE_SIZEOF = 4;

    private static final char TRIE_NULL = (char) -1;

    private static final int TRIE_ROOT = 0;

    private static final int INCREMENT = 1024;

    private static final int DEFAULT = 14337;

    private static final int RIGHT = 9300;

    private static AutoText sInstance = new AutoText(Resources.getSystem(), false);

    private static Object sLock = new Object();

    private static AutoText sUserInstance = new AutoText(null, true);

    private static final String TAG = "AutoText";

    private char[] mTrie;

    private char mTrieUsed;

    private String mText;

    private Locale mLocale;

    private int mSize;

    private boolean initialized;

    private AutoText(Resources resources, boolean isUserInstance) {
        if (!isUserInstance) {
            mLocale = resources.getConfiguration().locale;
        }
        initialized = init(resources, isUserInstance);
    }

    /**
     * Returns the instance of AutoText. If the locale has changed, it will create a new
     * instance of AutoText for the locale.
     * @param view to get the resources from
     * @return the single instance of AutoText
     */
    private static AutoText getInstance(View view) {
        Resources res = view.getContext().getResources();
        Locale locale = res.getConfiguration().locale;
        AutoText instance;
        synchronized (sLock) {
            instance = sInstance;
            if (!locale.equals(instance.mLocale)) {
                instance = new AutoText(res, false);
                sInstance = instance;
            }
        }
        return instance;
    }

    /**
     * Retrieves a possible spelling correction for the specified range
     * of text.  Returns null if no correction can be found.
     * The View is used to get the current Locale and Resources.
     */
    public static String get(CharSequence src, final int start, final int end, View view) {
        String userCorrection = null;
        if (sUserInstance.initialized) {
            userCorrection = sUserInstance.lookup(src, start, end);
        }
        if (userCorrection == null) {
            return getInstance(view).lookup(src, start, end);
        } else {
            return userCorrection;
        }
    }

    /**
     * Returns the size of the auto text dictionary. The return value can be zero if there is
     * no auto correction data available for the current locale.
     * @param view used to retrieve the current Locale and Resources.
     * @return the number of entries in the auto text dictionary
     */
    public static int getSize(View view) {
        return getInstance(view).getSize();
    }

    /**
     * Returns the size of the dictionary.
     */
    private int getSize() {
        return mSize;
    }

    /**
     * Refreshes the list of user-defined corrections in the file
     * /data/local/user_autotext.xml .
     */
    private static void refreshUserCorrections() {
        sUserInstance = new AutoText(null, true);
    }

    private String lookup(CharSequence src, final int start, final int end) {
        int here = mTrie[TRIE_ROOT];
        for (int i = start; i < end; i++) {
            char c = src.charAt(i);
            for (; here != TRIE_NULL; here = mTrie[here + TRIE_NEXT]) {
                if (c == mTrie[here + TRIE_C]) {
                    if ((i == end - 1) && (mTrie[here + TRIE_OFF] != TRIE_NULL)) {
                        int off = mTrie[here + TRIE_OFF];
                        int len = mText.charAt(off);
                        return mText.substring(off + 1, off + 1 + len);
                    }
                    here = mTrie[here + TRIE_CHILD];
                    break;
                }
            }
            if (here == TRIE_NULL) {
                return null;
            }
        }
        return null;
    }

    private boolean init(Resources r, boolean isUserInstance) {
        XmlPullParser parser = null;
        BufferedInputStream bufstream = null;
        FileInputStream fstream = null;
        if (isUserInstance) {
            try {
                parser = XmlPullParserFactory.newInstance().newPullParser();
                fstream = new FileInputStream("/data/local/user_autotext.xml");
                bufstream = new BufferedInputStream(fstream);
                parser.setInput(bufstream, null);
            } catch (IOException e) {
                return false;
            } catch (XmlPullParserException e) {
                return false;
            }
        } else {
            parser = r.getXml(com.android.internal.R.xml.autotext);
        }
        mTrie = new char[DEFAULT];
        mTrie[TRIE_ROOT] = TRIE_NULL;
        mTrieUsed = TRIE_ROOT + 1;
        StringBuilder right = new StringBuilder(RIGHT);
        try {
            XmlUtils.beginDocument(parser, "words");
            String odest = "";
            char ooff = 0;
            while (true) {
                XmlUtils.nextElement(parser);
                String element = parser.getName();
                if (element == null || !(element.equals("word"))) {
                    break;
                }
                String src = parser.getAttributeValue(null, "src");
                if (parser.next() == XmlPullParser.TEXT) {
                    String dest = parser.getText();
                    char off;
                    if (dest.equals(odest)) {
                        off = ooff;
                    } else {
                        off = (char) right.length();
                        right.append((char) dest.length());
                        right.append(dest);
                    }
                    add(src, off);
                }
            }
            if (r != null) {
                r.flushLayoutCache();
            }
        } catch (XmlPullParserException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            if (isUserInstance) {
                try {
                    fstream.close();
                    bufstream.close();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            } else {
                ((XmlResourceParser) parser).close();
            }
        }
        mText = right.toString();
        return true;
    }

    private void add(String src, char off) {
        int slen = src.length();
        int herep = TRIE_ROOT;
        mSize++;
        for (int i = 0; i < slen; i++) {
            char c = src.charAt(i);
            boolean found = false;
            for (; mTrie[herep] != TRIE_NULL; herep = mTrie[herep] + TRIE_NEXT) {
                if (c == mTrie[mTrie[herep] + TRIE_C]) {
                    if (i == slen - 1) {
                        mTrie[mTrie[herep] + TRIE_OFF] = off;
                        return;
                    }
                    herep = mTrie[herep] + TRIE_CHILD;
                    found = true;
                    break;
                }
            }
            if (!found) {
                char node = newTrieNode();
                mTrie[herep] = node;
                mTrie[mTrie[herep] + TRIE_C] = c;
                mTrie[mTrie[herep] + TRIE_OFF] = TRIE_NULL;
                mTrie[mTrie[herep] + TRIE_NEXT] = TRIE_NULL;
                mTrie[mTrie[herep] + TRIE_CHILD] = TRIE_NULL;
                if (i == slen - 1) {
                    mTrie[mTrie[herep] + TRIE_OFF] = off;
                    return;
                }
                herep = mTrie[herep] + TRIE_CHILD;
            }
        }
    }

    private char newTrieNode() {
        if (mTrieUsed + TRIE_SIZEOF > mTrie.length) {
            char[] copy = new char[mTrie.length + INCREMENT];
            System.arraycopy(mTrie, 0, copy, 0, mTrie.length);
            mTrie = copy;
        }
        char ret = mTrieUsed;
        mTrieUsed += TRIE_SIZEOF;
        return ret;
    }
}
