package apollo.dataadapter;

import apollo.datamodel.*;
import java.util.*;
import org.bdgp.io.*;
import java.util.Properties;

public class DummySequenceAdapter extends AbstractApolloAdapter {

    IOOperation[] supportedOperations = { ApolloDataAdapterI.OP_READ_SEQUENCE };

    public void init() {
    }

    public String getName() {
        return "Dummy Sequence Adapter";
    }

    public String getType() {
        return "Dummy Sequence Data";
    }

    public DataInputType getInputType() {
        return null;
    }

    public String getInput() {
        return null;
    }

    public IOOperation[] getSupportedOperations() {
        return supportedOperations;
    }

    public DummySequenceAdapter() {
    }

    public SequenceI getSequence(String id) throws ApolloAdapterException {
        return getSequence(new DbXref("dum", "dum", "dum"), 1, 2000);
    }

    public SequenceI getSequence(DbXref dbxref) throws ApolloAdapterException {
        return getSequence(dbxref, 1, 2000);
    }

    public Vector getSequences(DbXref[] dbxref) throws ApolloAdapterException {
        Vector seqs = new Vector();
        for (int i = 0; i < dbxref.length; i++) {
            seqs.addElement(getSequence(dbxref[i]));
        }
        return seqs;
    }

    public SequenceI getSequence(DbXref dbxref, int start, int end) throws ApolloAdapterException {
        int len = (int) (end - start + 1);
        System.out.println("Getting sequence length " + len);
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < len; ) {
            for (int j = 0; j < 10 && i < len; j++, i++) {
                sb.append('T');
            }
            for (int j = 0; j < 10 && i < len; j++, i++) {
                sb.append('A');
            }
        }
        Sequence seq = new Sequence(dbxref.getIdValue(), sb.toString());
        System.out.println("Got sequence " + seq);
        return seq;
    }

    public Vector getSequences(DbXref[] dbxref, int[] start, int[] end) throws ApolloAdapterException {
        Vector seqs = new Vector();
        for (int i = 0; i < dbxref.length; i++) {
            seqs.addElement(getSequence(dbxref[i], start[i], end[i]));
        }
        return seqs;
    }

    public void setRegion(String extID) throws ApolloAdapterException {
    }

    public void addDataListener(DataListener d) {
    }

    /** No UI for this adapter */
    public DataAdapterUI getUI(IOOperation op) {
        return null;
    }
}
