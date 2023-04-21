package com.arykow.applications.ugabe.server;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.EOFException;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipInputStream;
import com.arykow.applications.ugabe.client.UGABEService;

public class UGABEServiceController implements UGABEService {

    public List<Integer> loadCartridge(String fileName) throws Exception {
        InputStream inputStream = new FileInputStream(fileName);
        if (fileName.endsWith(".zip")) {
            inputStream = new ZipInputStream(inputStream);
        }
        return loadCartridge(inputStream);
    }

    public List<Integer> loadCartridge(InputStream inputStream) throws IOException {
        DataInputStream dataInputStream = new DataInputStream(new BufferedInputStream(inputStream));
        List<Integer> integers = new ArrayList<Integer>();
        boolean finished = false;
        do {
            try {
                integers.add(dataInputStream.readUnsignedByte());
            } catch (EOFException e) {
                finished = true;
            }
        } while (!finished);
        return integers;
    }
}
