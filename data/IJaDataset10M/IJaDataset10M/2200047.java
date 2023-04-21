package ar.com.datos.input;

import ar.com.datos.input.bo.StringProcessBo;
import ar.com.datos.tools.ObjectFactory;

public class WordTextProcessor {

    private String buff;

    private boolean processed;

    private String[] iter;

    private int pos;

    private StringProcessBo sbo;

    private int size;

    public WordTextProcessor() {
        buff = "";
        processed = false;
        sbo = (StringProcessBo) ObjectFactory.getObject(StringProcessBo.class);
    }

    public void setText(String s) {
        this.buff = s;
        processed = false;
    }

    public String getText() {
        return this.buff;
    }

    public boolean hasNext() {
        if (!processed) {
            processBuffer();
        }
        return pos < size;
    }

    public String next() {
        if (!processed) {
            processBuffer();
        }
        return iter[pos++];
    }

    private void processBuffer() {
        String s = sbo.getLowerCase(buff);
        s = sbo.cleanPunctuation(s);
        if (s.length() > 0) {
            iter = s.split(" ");
            pos = 0;
            size = iter.length;
        } else size = 0;
        processed = true;
    }
}
