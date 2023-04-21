package org.expasy.jpl.msnanalysis.query;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import org.expasy.jpl.bio.sequence.io.fasta.JPLFastaEntry;
import org.expasy.jpl.bio.sequence.io.fasta.JPLFastaHeaderFormatManager;
import org.expasy.jpl.bio.sequence.io.fasta.JPLFastaReader;
import org.expasy.jpl.msnanalysis.reader.JPLPepXMLSaxHandler;
import org.expasy.jpl.utils.file.JPLFile;

/**
 * This object stores candidate proteins to make search against MS/MS spectra.
 * 
 * @author nikitin
 */
public class JPLProteinCandidateDb {

    private static JPLProteinCandidateStorage proteinPool;

    private final String dbName;

    private Set<String> proteinAccessionNumbers;

    private JPLFastaReader fastaReader;

    public JPLProteinCandidateDb(JPLPepXMLSaxHandler saxHandler, String dbName) throws IOException {
        this.dbName = dbName;
        fastaReader = new JPLFastaReader(dbName);
        JPLFastaHeaderFormatManager manager = saxHandler.getFastaHeaderFormatManager(new JPLFile(dbName).getBaseName());
        if (manager != null) {
            fastaReader.setHeaderFormatManager(manager);
        }
        proteinPool = JPLProteinCandidateStorage.getInstance();
        proteinAccessionNumbers = new HashSet<String>();
    }

    public String getDbName() {
        return dbName;
    }

    /**
	 * Add all protein entries for candidate only
	 * 
	 * @throws IOException if db filename cannot be found
	 */
    public void addCandidateEntries() throws IOException {
        while (fastaReader.hasNext()) {
            JPLFastaEntry nextEntry = fastaReader.next();
            String an = nextEntry.getHeader().getAccessionNumber();
            if (proteinAccessionNumbers.contains(an)) {
                addProteinEntry(an, nextEntry);
            }
        }
    }

    public void addProteinAccessionNumber(String id) {
        proteinAccessionNumbers.add(id);
    }

    /**
	 * Handle a pool of unique protein in {@JPLProteinFactory}
	 * and put the link of the created instance in our map.
	 * 
	 * @param id
	 * @param parentSequence
	 */
    private void addProteinEntry(String id, JPLFastaEntry entry) {
        proteinPool.addProteinEntry(id, entry);
    }

    public JPLFastaEntry getProteinEntry(String id) {
        if (proteinAccessionNumbers.contains(id)) {
            return proteinPool.getProteinEntry(id);
        } else {
            return null;
        }
    }

    public int getProteinNumber() {
        return proteinAccessionNumbers.size();
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("db name : " + dbName + "\n");
        sb.append("candidate protein# : " + proteinAccessionNumbers.size() + "\n");
        for (String an : proteinAccessionNumbers) {
            sb.append(proteinPool.getProteinEntry(an) + "\n");
        }
        return sb.toString();
    }
}
