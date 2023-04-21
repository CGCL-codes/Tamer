package org.amse.bedrosova.logic.model.impl;

import java.io.*;
import org.amse.bedrosova.logic.model.*;

public class CrossWriter {

    public void saveToFile(INet net, String fn) {
        File f = new File(fn);
        final int size = net.getSize();
        try {
            PrintWriter w = new PrintWriter(f);
            w.println("" + size);
            for (int i = 0; i < size; ++i) {
                StringBuffer b = new StringBuffer();
                for (int j = 0; j < size; ++j) {
                    ICell c = net.getCell(i, j);
                    if (c.isChecked()) {
                        if (!c.isFilled()) {
                            b.append('*');
                        } else {
                            b.append(c.getLetter());
                        }
                    } else {
                        b.append("_");
                    }
                }
                w.println(b.toString());
            }
            if (w.checkError()) {
                throw new CrossException("IO error");
            }
            w.close();
        } catch (IOException e) {
            throw new CrossException("IO error: " + e.getMessage());
        }
    }
}
