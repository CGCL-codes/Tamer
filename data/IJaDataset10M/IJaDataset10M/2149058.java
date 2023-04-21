package com.gantzgulch.life.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import com.gantzgulch.life.model.Board;

public class PatternLoader {

    public void loadPattern(String resourceName, Board board) {
        InputStream inputStream = getClass().getResourceAsStream(resourceName);
        loadPattern(inputStream, board);
    }

    public void loadPattern(InputStream inputStream, Board board) {
        Reader reader = IOUtils.toReader(inputStream);
        loadPattern(reader, board);
    }

    public void loadPattern(Reader reader, Board board) {
        BufferedReader br = IOUtils.toBufferedReader(reader);
        int yCoordinate = 0;
        try {
            for (String data = br.readLine(); data != null; data = br.readLine()) {
                for (int xCoordinate = 0; xCoordinate < data.length(); xCoordinate++) {
                    if (!Character.isWhitespace(data.charAt(xCoordinate))) {
                        board.setAlive(xCoordinate, yCoordinate);
                    }
                }
                yCoordinate++;
            }
        } catch (IOException ioe) {
            throw new RuntimeException(ioe);
        }
    }
}
