package uk.ac.shef.wit.runes.rune.csv;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import uk.ac.shef.wit.commons.UtilCollections;
import uk.ac.shef.wit.runes.Runes;
import uk.ac.shef.wit.runes.exceptions.RunesExceptionCannotHandle;
import uk.ac.shef.wit.runes.exceptions.RunesExceptionNoSuchContent;
import uk.ac.shef.wit.runes.exceptions.RunesExceptionNoSuchStructure;
import uk.ac.shef.wit.runes.exceptions.RunesExceptionRuneExecution;
import uk.ac.shef.wit.runes.rune.Rune;
import uk.ac.shef.wit.runes.rune.RuneContentGrabber;
import uk.ac.shef.wit.runes.rune.RuneFiller;
import uk.ac.shef.wit.runes.runestone.Runestone;
import uk.ac.shef.wit.runes.runestone.Structure;
import uk.ac.shef.wit.runes.runestone.StructureAndContent;
import au.com.bytecode.opencsv.CSVReader;

/**
 * Implements a loader for CSV files.
 *
 * @author <a href="mailto:mark@dcs.shef.ac.uk">Mark A. Greenwood</a>
 * @version $Id: RuneCSVLoader.java 523 2008-09-29 13:07:31Z greenwoodma $
 */
public class RuneCSVLoader implements Rune {

    private static final double VERSION = 0.1;

    static {
        Runes.registerRune(RuneCSVLoader.class, MessageFormat.format("CSV document loader v{0}", VERSION));
        Runes.registerAlias("csv_document", "csv_document_url");
    }

    public static void main(String[] args) throws RunesExceptionNoSuchContent, RunesExceptionNoSuchStructure, RunesExceptionCannotHandle, RunesExceptionRuneExecution, MalformedURLException {
        Set<String> text = new HashSet<String>();
        Runes.carve(new RuneFiller<String>("csv_document_url", (new File(args[0])).toURI().toURL().toString()), new RuneContentGrabber<String>("cell_text", text));
        System.out.println("Number of unique cells = " + text.size());
    }

    public Set<String> analyseRequired(Runestone stone) throws RunesExceptionCannotHandle, RunesExceptionRuneExecution {
        return Collections.singleton("csv_document_url");
    }

    public Set<String> analyseProvided(Runestone stone) throws RunesExceptionCannotHandle, RunesExceptionRuneExecution {
        return UtilCollections.add(new HashSet<String>(), "cell", "cell_has_text|cell|cell_text", "cell_text", "cell_above|cell|cell", "cell_below|cell|cell", "cell_left|cell|cell", "cell_right|cell|cell", "cell_in_row|cell|row", "cell_in_column|cell|column", "row", "row_has_first_cell|row|cell", "previous_row|row|row", "next_row|row|row", "column", "column_has_first_cell|column|cell", "previous_column|column|column", "next_column|column|column", "document_has_first_cell|document|cell", "document_has_first_column|document|column", "document_has_first_row|document|row");
    }

    public void carve(Runestone stone) throws RunesExceptionNoSuchStructure, RunesExceptionNoSuchContent, RunesExceptionCannotHandle, RunesExceptionRuneExecution {
        try {
            final StructureAndContent<String> urls = stone.getStructureAndContent("csv_document_url");
            final Structure documentHasURL = stone.getStructure("csv_document_has_url");
            final Structure cells = stone.getStructure("cell");
            final StructureAndContent<String> texts = stone.getStructureAndContent("cell_text");
            final Structure cellHasText = stone.getStructure("cell_has_text");
            final Structure cellAbove = stone.getStructure("cell_above");
            final Structure cellBelow = stone.getStructure("cell_below");
            final Structure cellLeft = stone.getStructure("cell_left");
            final Structure cellRight = stone.getStructure("cell_right");
            final Structure cellInRow = stone.getStructure("cell_in_row");
            final Structure cellInColumn = stone.getStructure("cell_in_column");
            final Structure rows = stone.getStructure("row");
            final Structure rowHasFirstCell = stone.getStructure("row_has_first_cell");
            final Structure rowPrevious = stone.getStructure("previous_row");
            final Structure rowNext = stone.getStructure("next_row");
            final Structure columns = stone.getStructure("column");
            final Structure colHasFirstCell = stone.getStructure("column_has_first_cell");
            final Structure colPrevious = stone.getStructure("previous_column");
            final Structure colNext = stone.getStructure("next_column");
            final Structure docHasFirstCell = stone.getStructure("document_has_first_cell");
            final Structure docHasFirstRow = stone.getStructure("document_has_first_row");
            final Structure docHasFirstCol = stone.getStructure("document_has_first_column");
            for (final Map.Entry<int[], String> url : urls) {
                final int docID = url.getKey()[0];
                documentHasURL.inscribe(docID, docID);
                CSVReader reader = new CSVReader(new InputStreamReader((new URL(url.getValue())).openStream()));
                List<Integer> previousRow = null;
                List<Integer> colIDs = new ArrayList<Integer>();
                int previousRowID = -1;
                String[] cols;
                while ((cols = reader.readNext()) != null) {
                    final int rowID = rows.encode();
                    final List<Integer> currentRow = new ArrayList<Integer>();
                    for (int c = 0; c < cols.length; ++c) {
                        final int cellID = cells.encode();
                        cellInRow.inscribe(cellID, rowID);
                        currentRow.add(cellID);
                        final int textID = texts.encode(cols[c].trim());
                        cellHasText.inscribe(cellID, textID);
                        if (previousRow != null && previousRow.size() > c) {
                            cellAbove.inscribe(cellID, previousRow.get(c));
                            cellBelow.inscribe(previousRow.get(c), cellID);
                            cellInColumn.inscribe(cellID, colIDs.get(c));
                        }
                        if (c == 0) {
                            rowHasFirstCell.inscribe(rowID, cellID);
                        } else {
                            cellRight.inscribe(currentRow.get(c - 1), cellID);
                            cellLeft.inscribe(cellID, currentRow.get(c - 1));
                        }
                    }
                    if (previousRowID != -1) {
                        rowPrevious.inscribe(rowID, previousRowID);
                        rowNext.inscribe(previousRowID, rowID);
                    } else {
                        int previousCol = -1;
                        docHasFirstCell.inscribe(docID, currentRow.get(0));
                        docHasFirstRow.inscribe(docID, rowID);
                        for (int i = 0; i < currentRow.size(); ++i) {
                            int colID = columns.encode();
                            colIDs.add(colID);
                            cellInColumn.inscribe(currentRow.get(i), colID);
                            if (i == 0) docHasFirstCol.inscribe(docID, colID);
                            colHasFirstCell.inscribe(colID, currentRow.get(i));
                            if (previousCol != -1) {
                                colPrevious.inscribe(colID, previousCol);
                                colNext.inscribe(previousCol, colID);
                            }
                            previousCol = colID;
                        }
                    }
                    previousRow = currentRow;
                    previousRowID = rowID;
                }
            }
        } catch (final IOException e) {
            throw new RunesExceptionRuneExecution(e, this);
        }
    }
}
