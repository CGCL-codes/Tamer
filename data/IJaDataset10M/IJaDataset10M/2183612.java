package org.openscience.jmol.io;

import org.openscience.jmol.DisplayControl;
import org.openscience.jmol.ChemFile;
import org.openscience.jmol.ChemFrame;
import org.openscience.jmol.Vibration;
import org.openscience.jmol.AtomicSymbol;
import org.openscience.cdk.Atom;
import java.util.Vector;
import java.util.Hashtable;
import java.io.Reader;
import java.io.StreamTokenizer;
import java.io.StringReader;
import java.io.BufferedReader;
import java.io.IOException;

/**
 * A reader for ADF output.
 * Amsterdam Density Functional (ADF) is a quantum chemistry program
 * by Scientific Computing & Modelling NV (SCM)
 * (http://www.scm.com/).
 *
 * <p> Molecular coordinates, energies, and normal coordinates of
 * vibrations are read. Each set of coordinates is added to the
 * ChemFile in the order they are found. Energies and vibrations
 * are associated with the previously read set of coordinates.
 *
 * <p> This reader was developed from a small set of
 * example output files, and therefore, is not guaranteed to
 * properly read all ADF output. If you have problems,
 * please contact the author of this code, not the developers
 * of ADF.
 *
 * @author Bradley A. Smith (yeldar@home.com)
 * @version 1.0
 */
public class ADFReader extends DefaultChemFileReader {

    /**
   * Create an ADF output reader.
   *
   * @param input source of ADF data
   */
    public ADFReader(DisplayControl control, Reader input) {
        super(control, input);
    }

    /**
   * Read the ADF output.
   *
   * @return a ChemFile with the coordinates, energies, and vibrations.
   * @exception IOException if an I/O error occurs
   */
    public ChemFile read() throws IOException {
        ChemFile file = new ChemFile(control, bondsEnabled);
        ChemFrame frame = null;
        String line = input.readLine();
        while (input.ready() && (line != null)) {
            if (line.indexOf("Coordinates (Cartesian)") >= 0) {
                frame = new ChemFrame(control);
                readCoordinates(frame);
                break;
            }
            line = input.readLine();
        }
        if (frame != null) {
            line = input.readLine();
            while (input.ready() && (line != null)) {
                if (line.indexOf("Coordinates (Cartesian)") >= 0) {
                    frame.rebond();
                    file.addFrame(frame);
                    frame = new ChemFrame(control);
                    readCoordinates(frame);
                } else if (line.indexOf("Energy:") >= 0) {
                    frame.setInfo(line.trim());
                } else if (line.indexOf("Vibrations") >= 0) {
                    readFrequencies(frame);
                }
                line = input.readLine();
            }
            frame.rebond();
            file.addFrame(frame);
        }
        return file;
    }

    /**
   * Reads a set of coordinates into ChemFrame.
   *
   * @param frame  the destination ChemFrame
   * @exception IOException  if an I/O error occurs
   */
    private void readCoordinates(ChemFrame frame) throws IOException {
        String line;
        line = input.readLine();
        line = input.readLine();
        line = input.readLine();
        line = input.readLine();
        line = input.readLine();
        while (input.ready()) {
            line = input.readLine();
            if ((line == null) || (line.indexOf("-----") > 0)) {
                break;
            }
            int atomicNumber;
            StringReader sr = new StringReader(line);
            StreamTokenizer token = new StreamTokenizer(sr);
            token.nextToken();
            String symbol = "";
            if (token.nextToken() == StreamTokenizer.TT_WORD) {
                symbol = token.sval;
                atomicNumber = AtomicSymbol.elementToAtomicNumber(symbol);
                if (atomicNumber == 0) {
                    continue;
                }
            } else {
                throw new IOException("Error reading coordinates");
            }
            token.nextToken();
            token.nextToken();
            token.nextToken();
            double x;
            double y;
            double z;
            if (token.nextToken() == StreamTokenizer.TT_NUMBER) {
                x = token.nval;
            } else {
                throw new IOException("Error reading coordinates");
            }
            if (token.nextToken() == StreamTokenizer.TT_NUMBER) {
                y = token.nval;
            } else {
                throw new IOException("Error reading coordinates");
            }
            if (token.nextToken() == StreamTokenizer.TT_NUMBER) {
                z = token.nval;
            } else {
                throw new IOException("Error reading coordinates");
            }
            Atom atom = new Atom(symbol);
            atom.setX3D(x);
            atom.setY3D(y);
            atom.setZ3D(z);
            atom.setAtomicNumber(atomicNumber);
            frame.addAtom(atom);
        }
    }

    /**
   * Reads a set of vibrations into ChemFrame.
   *
   * @param frame  the destination ChemFrame
   * @exception IOException  if an I/O error occurs
   */
    private void readFrequencies(ChemFrame frame) throws IOException {
        String line;
        line = input.readLine();
        line = input.readLine();
        line = input.readLine();
        line = input.readLine();
        line = input.readLine();
        line = input.readLine();
        line = input.readLine();
        while (line.indexOf('.') > 0) {
            Vector currentVibs = new Vector();
            StringReader freqValRead = new StringReader(line);
            StreamTokenizer token = new StreamTokenizer(freqValRead);
            while (token.nextToken() != StreamTokenizer.TT_EOF) {
                Vibration freq = new Vibration(Double.toString(token.nval));
                currentVibs.addElement(freq);
            }
            line = input.readLine();
            for (int i = 0; i < frame.getAtomCount(); ++i) {
                line = input.readLine();
                StringReader vectorRead = new StringReader(line);
                token = new StreamTokenizer(vectorRead);
                token.nextToken();
                if (token.nextToken() == StreamTokenizer.TT_WORD) {
                    int atomicNumber = AtomicSymbol.elementToAtomicNumber(token.sval);
                    if (atomicNumber == 0) {
                        --i;
                        continue;
                    }
                } else {
                    throw new IOException("Error reading frequencies");
                }
                for (int j = 0; j < currentVibs.size(); ++j) {
                    double[] v = new double[3];
                    if (token.nextToken() == StreamTokenizer.TT_NUMBER) {
                        v[0] = token.nval;
                    } else {
                        throw new IOException("Error reading frequencies");
                    }
                    if (token.nextToken() == StreamTokenizer.TT_NUMBER) {
                        v[1] = token.nval;
                    } else {
                        throw new IOException("Error reading frequencies");
                    }
                    if (token.nextToken() == StreamTokenizer.TT_NUMBER) {
                        v[2] = token.nval;
                    } else {
                        throw new IOException("Error reading frequencies");
                    }
                    ((Vibration) currentVibs.elementAt(j)).addAtomVector(v);
                }
            }
            for (int i = 0; i < currentVibs.size(); ++i) {
                frame.addVibration((Vibration) currentVibs.elementAt(i));
            }
            line = input.readLine();
            line = input.readLine();
            line = input.readLine();
        }
    }
}
