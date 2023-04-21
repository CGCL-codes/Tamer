package ie.ucd.lis.ojax;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import javax.servlet.*;
import javax.servlet.http.*;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.index.TermEnum;

/**
 * Value object to hold basic info about a repository (Archive) in the Ojax index.
 * @author P�draig O'hIceadha
 */
public class Archive {

    private Term repositoryTerm;

    private int numDocuments = 0;

    /** Creates a new instance of Archive */
    public Archive(Term repositoryTerm, int numDocuments) {
        this.repositoryTerm = repositoryTerm;
        this.numDocuments = numDocuments;
    }

    public int getNumDocuments() {
        return this.numDocuments;
    }

    public Term getRepositoryTerm() {
        return this.repositoryTerm;
    }

    public String getRepositoryName() {
        return this.repositoryTerm.text();
    }
}
