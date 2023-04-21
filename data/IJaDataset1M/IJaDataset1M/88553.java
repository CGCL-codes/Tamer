package com.trackerdogs.testing;

import java.io.*;
import java.util.*;
import javax.servlet.*;
import com.trackerdogs.ui.servlet.skin.*;

/**
 * A layer between HTML and Servlets
 */
public class TestSkin {

    private SkinInterface skinInterface_;

    private Writer out_;

    private Reader in_;

    /************************************************************
     * Initializes the skin
     *
     * @param si the SkinInterface that this skin will call
     */
    public TestSkin(SkinInterface si) {
        this.skinInterface_ = si;
    }

    /************************************************************
     * generate the skin
     *
     * @param fin the input stream of the skin
     * @param theout where to write the result
     */
    public void generate(Reader fin, Writer theout) throws IOException {
        this.out_ = theout;
        this.in_ = fin;
        char ch;
        boolean toKey = false;
        String key = new String();
        char endOfStream = (char) -1;
        ch = (char) in_.read();
        try {
            while (ch != endOfStream) {
                if (!toKey && ch == '$') {
                    toKey = true;
                    key = new String();
                } else if (toKey && ch == '$') {
                    try {
                        executeKey(key);
                    } catch (IOException ex) {
                        System.out.println("Skin 1 " + ex);
                    }
                    toKey = false;
                } else if (!toKey) {
                    try {
                        out_.write(ch);
                    } catch (IOException ex) {
                        System.out.println("Skin 2 " + ex);
                    }
                } else if (toKey) {
                    key += ch;
                }
                ch = (char) in_.read();
            }
        } catch (IOException ex) {
            System.out.println("Skin 4 " + ex);
        }
    }

    private void executeKey(String key) throws IOException {
        int open = key.indexOf('('), close = key.indexOf(')'), buf = key.indexOf('{');
        if ((open != -1) && (close != -1)) {
            String params = key.substring(open + 1, close);
            Vector vec = new Vector();
            int comma = params.indexOf(',');
            while (comma > -1) {
                vec.addElement(params.substring(0, comma));
                params = params.substring(comma + 1);
                comma = params.indexOf(',');
            }
            vec.addElement(params);
            String[] parsArray = new String[vec.size()];
            for (int i = 0; i < vec.size(); i++) {
                parsArray[i] = (String) vec.elementAt(i);
            }
            key = key.substring(0, open);
            out_.write(this.skinInterface_.getFunctionValue(key, parsArray));
        } else if (buf != -1) {
            key = key.substring(0, key.length() - 1);
            String buffer = new String();
            char ch;
            int nestedBuffers = 0;
            while (in_.ready() && nestedBuffers > -1) {
                ch = (char) in_.read();
                buffer += ch;
                if (buffer.endsWith("$}$")) {
                    nestedBuffers--;
                } else if (buffer.endsWith("{$")) {
                    nestedBuffers++;
                }
            }
            if (buffer.endsWith("$}$")) {
                buffer = buffer.substring(0, buffer.length() - 3);
            }
            while (this.skinInterface_.loopBlock(key)) {
                Skin skin = new Skin(this.skinInterface_);
                skin.generate(new StringReader(buffer), out_);
            }
        } else {
            if (!key.equals("")) {
                out_.write(this.skinInterface_.getVariableValue(key));
            } else {
                out_.write('$');
            }
        }
    }
}
