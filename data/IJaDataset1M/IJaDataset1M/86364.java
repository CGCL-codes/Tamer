package de.bioutils.gff.parser;

import java.io.File;
import java.io.IOException;
import de.bioutils.gff.GFFFormatErrorException;
import de.bioutils.gff.file.NewGFFFile;

/**
 * 
 * @author Alexander Kerner
 * @lastVisit 2009-11-09
 *
 */
public interface GFFFileParser {

    NewGFFFile parseFile(File file) throws IOException, GFFFormatErrorException;

    NewGFFFile parseString(String string) throws GFFFormatErrorException;
}
