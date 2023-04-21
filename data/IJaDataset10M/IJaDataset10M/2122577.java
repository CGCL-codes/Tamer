package org.chess.quasimodo.pgn.logic;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import org.chess.quasimodo.domain.logic.Definitions;
import org.chess.quasimodo.errors.InvalidPGNException;
import org.chess.quasimodo.errors.PGNParseException;
import org.chess.quasimodo.pgn.domain.PGNGame;
import org.chess.quasimodo.pgn.domain.PGNGame.TagType;

public abstract class AbstractPGNParser implements PGNParser {

    private BufferedReader reader;

    public AbstractPGNParser(String filepath) throws FileNotFoundException {
        reader = new BufferedReader(new FileReader(filepath));
    }

    @Override
    public final void parse() throws PGNParseException {
        long lineCounter = 0;
        int gameCounter = 0;
        boolean whithinGame = false;
        boolean foundMoves = false;
        StringBuffer moveBuffer = new StringBuffer();
        Map<TagType, String> tagMap = new HashMap<TagType, String>();
        try {
            String line;
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                lineCounter++;
                if (line.startsWith("[")) {
                    if (!line.endsWith("]")) {
                        throw new InvalidPGNException("Missing right square bracket on line [" + lineCounter + "]");
                    }
                    if (!whithinGame) {
                        whithinGame = true;
                    }
                    PGNGame.processTag(line, tagMap);
                } else {
                    if ("".equals(line)) {
                        if (foundMoves) {
                            whithinGame = false;
                        } else {
                            continue;
                        }
                    } else {
                        if (!foundMoves) {
                            foundMoves = true;
                        }
                        if (line.indexOf('%') > 0) {
                            line = line.substring(0, line.indexOf('%'));
                        }
                        moveBuffer.append(line);
                        if (!line.endsWith(".")) {
                            moveBuffer.append(" ");
                        }
                        if (line.endsWith(Definitions.WHITE_WINS) || line.endsWith(Definitions.BLACK_WINS) || line.endsWith(Definitions.DRAW) || line.endsWith(Definitions.UNDECIDED)) {
                            whithinGame = false;
                        }
                    }
                    if (!whithinGame && moveBuffer.length() > 0) {
                        gameCounter++;
                        processGameData(tagMap, moveBuffer, gameCounter);
                        moveBuffer.setLength(0);
                        tagMap.clear();
                        foundMoves = false;
                    }
                }
            }
        } catch (Exception e) {
            throw new PGNParseException(e);
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                }
            }
        }
    }

    @Override
    public void close() {
    }

    @Override
    public abstract void processGameData(Map<TagType, String> tagMap, StringBuffer moveBuffer, int gameCounter) throws Exception;
}
