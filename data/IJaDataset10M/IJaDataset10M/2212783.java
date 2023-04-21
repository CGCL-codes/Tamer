package com.crypticbit.ipa.results;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import com.crypticbit.ipa.central.BackupFileView;
import com.crypticbit.ipa.central.FileParseException;
import com.crypticbit.ipa.central.NavigateException;
import com.crypticbit.ipa.central.backupfile.BackupFile;
import com.crypticbit.ipa.io.parser.BadFileFormatException;

public interface ParsedData {

    public Collection<Class<?>> getSubInterfaces();

    public <I> I getContentbyInterface(Class<I> interfaceDef) throws BadFileFormatException;

    public <I> List<I> getRecordsByInterface(Class<I> interfaceDef) throws FileParseException;

    /**
	 * Generate a verbose textual view of the parsed file
	 * 
	 * @return a verbose textual view of the parsed file
	 * @throws FileParseException 
	 */
    public String getContents() throws FileParseException;

    /**
	 * Generate a one line textual summary of the parsed file
	 * 
	 * @return the summary
	 */
    public String getSummary();

    public BackupFileView getViews();

    /**
	 * Search for the given String amongst all different views (text, hex,
	 * metadata, pList, etc. that might exist for this result type
	 * 
	 * @param searchString
	 *            string to search for
	 * @return a set containing all the found locatins
	 * @throws NavigateException
	 * @throws FileParseException 
	 * 
	 */
    public Set<Location> search(TextSearchAlgorithm searchType, String searchString) throws NavigateException, FileParseException;

    BackupFile getBackupFile();
}
