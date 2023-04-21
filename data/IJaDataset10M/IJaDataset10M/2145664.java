package pierre.util;

import java.util.ArrayList;
import pierre.system.BrowserServiceResources;

public class LineWidthAdjuster {

    private int numberOfColumns;

    public LineWidthAdjuster(int numberOfColumns) {
        this.numberOfColumns = numberOfColumns;
    }

    private ArrayList removeNewLineFeeds(String paragraph) {
        ArrayList lines = new ArrayList();
        String text = paragraph;
        int index = text.indexOf("\n");
        while (index != -1) {
            String currentLine = text.substring(0, index);
            lines.add(currentLine);
            text = text.substring(index + 1);
            index = text.indexOf("\n");
        }
        lines.add(text);
        return lines;
    }

    private ArrayList breakLongLines(String longLine) {
        ArrayList lines = new ArrayList();
        int lineLength = longLine.length();
        int numberOfLines = lineLength / numberOfColumns;
        if (lineLength % numberOfColumns > 0) {
            numberOfLines++;
        }
        int startIndex = 0;
        int endIndex = numberOfColumns;
        endIndex = makeIndexWithinBounds(endIndex, lineLength);
        for (int i = 0; i < numberOfLines; i++) {
            String currentLine = longLine.substring(startIndex, endIndex);
            lines.add(currentLine.trim());
            startIndex += numberOfColumns;
            startIndex = makeIndexWithinBounds(startIndex, lineLength);
            if (startIndex == lineLength) {
                endIndex = lineLength;
            } else {
                endIndex = startIndex + numberOfColumns;
                endIndex = makeIndexWithinBounds(endIndex, lineLength);
            }
        }
        return lines;
    }

    public ArrayList splitLines(String text) {
        ArrayList lines = new ArrayList();
        ArrayList basicChunks = removeNewLineFeeds(text);
        int numberOfChunks = basicChunks.size();
        for (int i = 0; i < numberOfChunks; i++) {
            String currentChunk = (String) basicChunks.get(i);
            lines.addAll(breakLongLines(currentChunk));
        }
        return lines;
    }

    private int makeIndexWithinBounds(int index, int lineLength) {
        if (index >= lineLength) {
            index = lineLength;
        }
        return index;
    }
}
