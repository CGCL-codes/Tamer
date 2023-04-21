package com.lepidllama.packageeditor.resources;

import com.lepidllama.packageeditor.dbpf.Header;
import com.lepidllama.packageeditor.dbpf.IndexBlock;
import com.lepidllama.packageeditor.fileio.DataReader;
import com.lepidllama.packageeditor.fileio.DataWriter;
import com.lepidllama.packageeditor.resources.interfaces.TextInputting;
import com.lepidllama.packageeditor.resources.interfaces.TextOutputting;
import com.lepidllama.packageeditor.utility.StringUtils;

public class PlainText extends Resource implements TextOutputting, TextInputting {

    byte[] unk1;

    String text;

    @Override
    public void read(DataReader in, Header header, IndexBlock indexBlock) {
        long start = in.getFilePointer();
        unk1 = in.readChunk(3);
        if (unk1[0] == 0x3C) {
            in.seek(start);
            unk1 = new byte[0];
        }
        text = StringUtils.parseStringByCharset(in.readChunk((int) indexBlock.getDecompressedSize() - unk1.length), "UTF-8");
    }

    @Override
    public void write(DataWriter out, Header header, IndexBlock indexBlock) {
        out.writeChunk(unk1);
        out.writeChunk(StringUtils.writeStringByCharset(text, "UTF-8"));
    }

    public byte[] getUnk1() {
        return unk1;
    }

    public String getText() {
        return text;
    }

    public void setUnk1(byte[] unk1) {
        Object old = this.unk1;
        this.unk1 = unk1;
        this.propChangeSupp.firePropertyChange("leading", old, unk1);
    }

    public void setText(String text) {
        Object old = this.text;
        this.text = text;
        this.propChangeSupp.firePropertyChange("text", old, text);
    }
}
