package com.misgod.pdbreader.pdb;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import android.util.Log;

public class TxtBookInfo extends AbstractBookInfo {

    private boolean isProgressing;

    private static final float MAX_LINE = 300;

    private int mPageCount;

    public TxtBookInfo(long id) {
        super(id);
    }

    @Override
    public void setFile(File pdb, boolean headerOnly) throws IOException {
        mFile = pdb;
        String name = pdb.getName();
        int end = name.indexOf(".");
        if (end > 0) {
            mName = name.substring(0, name.lastIndexOf("."));
        } else {
            mName = name;
        }
        if (!headerOnly) {
            LineNumberReader input = new LineNumberReader(new FileReader(mFile), 8192 * 2);
            while (input.readLine() != null) {
            }
            input.close();
            mPageCount = (int) Math.ceil(input.getLineNumber() / MAX_LINE);
        }
    }

    @Override
    public int getPageCount() {
        return mPageCount;
    }

    boolean isStop;

    public void stop() {
        isStop = true;
    }

    public boolean isProgressing() {
        return isProgressing;
    }

    public CharSequence getText() throws IOException {
        isProgressing = true;
        StringBuilder body = new StringBuilder();
        try {
            BufferedReader input = new BufferedReader(new InputStreamReader(new FileInputStream(mFile), mEncode));
            String lineStr;
            int line = -1;
            while ((lineStr = input.readLine()) != null) {
                line++;
                if (mPage * MAX_LINE > line) {
                    continue;
                } else if ((mPage + 1) * MAX_LINE < line) {
                    break;
                }
                body.append(lineStr.replace("    ", " ").replace("\t", "  ")).append("\n");
                if (isStop) {
                    isStop = false;
                    break;
                }
            }
            input.close();
        } finally {
            isProgressing = false;
        }
        return body;
    }

    @Override
    public boolean supportFormat() {
        return false;
    }
}
