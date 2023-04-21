package com.licenta.fbApp.server;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.HashMap;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.licenta.fbApp.shared.TipsService;

public class TipsServiceImpl extends RemoteServiceServlet implements TipsService {

    private static final long serialVersionUID = 5742796160437323678L;

    public HashMap<Integer, String> getLikes(int start, int stop) {
        RandomAccessFile raf1 = null;
        try {
            raf1 = new RandomAccessFile(new File("aCos.txt"), "r");
        } catch (FileNotFoundException e) {
            System.err.println(e.getMessage());
        }
        String line = "";
        int key;
        String value;
        try {
            line = raf1.readLine();
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
        HashMap<Integer, String> likes = new HashMap<Integer, String>();
        for (int i = 0; i < start - 1; i++) {
            try {
                line = raf1.readLine();
            } catch (IOException e) {
                System.err.println(e.getMessage());
            }
        }
        for (int i = start; i < stop; i++) {
            try {
                line = raf1.readLine();
            } catch (IOException e) {
                System.err.println(e.getMessage());
                break;
            }
            if (line != null) {
                key = Integer.parseInt(line.split(" ")[0]);
                if (line.contains(" ")) value = line.substring(line.indexOf(" ")); else value = " ";
                likes.put(key, value);
            }
        }
        try {
            raf1.close();
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
        return likes;
    }
}
