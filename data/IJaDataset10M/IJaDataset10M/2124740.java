package phylipalrequin.readers;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URI;
import java.util.Scanner;
import phylipalrequin.model.DataModel;
import phylipalrequin.model.PhylipDataModel;

/**
 * Read Phylip format input.
 * @author bchisham
 */
public class PhylipMatrixReader implements Reader {

    private URI modelLocation;

    private URI modelType;

    private char[][] characterStates;

    private String[] taxonLabels;

    private int[] filledTo;

    private int nTax;

    private int nChar;

    private static final int TAX_LABEL_SIZE = 10;

    /**
     *
     * @return
     */
    private boolean matrixFull() {
        boolean ret = false;
        for (int i = 0; i < this.filledTo.length && (ret = this.filledTo[i] == this.nChar); ++i) ;
        return ret;
    }

    /**
     *
     * @param in
     */
    protected void readNTax(InputStream in) {
        Scanner sin = new Scanner(in);
        this.nTax = sin.nextInt();
    }

    /**
     *
     * @param in
     */
    protected void readNChar(InputStream in) {
        Scanner sin = new Scanner(in);
        this.nChar = sin.nextInt();
    }

    /**
     * 
     * @param in
     * @param taxNo
     */
    private void readTaxonLabel(InputStream in, int taxNo) {
        Scanner sin = new Scanner(in);
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < TAX_LABEL_SIZE; ++i) {
            sb.append(sin.nextByte());
        }
        this.taxonLabels[taxNo] = sb.toString();
    }

    /**
     * 
     * @param in
     * @param taxNo
     */
    private void readCharacters(InputStream in, int taxNo) {
        Scanner sin = new Scanner(in);
        String line = sin.next("/[-.? a-zA-Z0-9]+/");
        for (int i = 0; i < line.length(); ++i) {
            this.characterStates[taxNo][this.filledTo[taxNo] + i] = line.charAt(i);
        }
        this.filledTo[taxNo] += line.length();
        return;
    }

    /**
     * 
     * @param in
     * @param taxNo
     */
    protected void readLine(InputStream in, int taxNo) {
        if (0 == this.filledTo[taxNo]) {
        }
    }

    /**
     * 
     * @return
     * @throws MalformedURLException
     * @throws IOException
     */
    protected InputStream openStream() throws MalformedURLException, IOException {
        return this.modelLocation.toURL().openStream();
    }

    /**
     *
     * @param modelLocation
     * @return
     */
    public DataModel readModel(URI modelLocation, URI modelType) throws MalformedURLException, IOException {
        PhylipDataModel ret = null;
        this.modelLocation = modelLocation;
        this.modelType = modelType;
        InputStream in = this.openStream();
        this.readNTax(in);
        this.readNChar(in);
        this.characterStates = new char[this.nTax][this.nChar];
        this.filledTo = new int[this.nTax];
        this.taxonLabels = new String[this.nTax];
        for (int i = 0; i < this.filledTo.length; ++i) {
            this.filledTo[i] = 0;
        }
        int currentTax = 0;
        while (!this.matrixFull()) {
            this.readLine(in, currentTax);
            currentTax = (currentTax++) % this.nTax;
        }
        return ret;
    }
}
