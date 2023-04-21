package org.fao.waicent.kids.editor.SplitFile;

import java.io.File;

/**
 * Interface to define the output names when splitting files
 *
 * @author (c) LuisM Pena, October-1997.
 * @version 1.0
 * SOFTWARE IS PROVIDED "AS IS", WITHOUT ANY WARRANTY OF ANY KIND
 */
public interface NamingAlgorythm {

    /**
     * Initialize the algorythm
     * @param inputFile the File object of the input file
     * @param chunks the number total of chunks to create
     * @return null if no error is done, or an errorSplittingEvent
     */
    ErrorSplittingEvent init(File inputFile, long chunks);

    /**
     * This procedure allows to get an output file
     * @param sequence the sequence number of the output file
     * @return a name for this chunk of the splitted file
     */
    public File getOutputFile(long sequence);
}
