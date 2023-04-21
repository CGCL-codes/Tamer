package org.openscience.jmol;

import java.io.*;
import java.util.Vector;

/**
 * A reader for Gaussian92 output.
 * Gaussian 92 is a quantum chemistry program
 * by Gaussian, Inc. (http://www.gaussian.com/).
 *
 * <p> Molecular coordinates, energies, and normal coordinates of
 * vibrations are read. Each set of coordinates is added to the
 * ChemFile in the order they are found. Energies and vibrations
 * are associated with the previously read set of coordinates.
 *
 * <p> This reader was developed from a small set of
 * example output files, and therefore, is not guaranteed to
 * properly read all Gaussian92 output. If you have problems,
 * please contact the author of this code, not the developers
 * of Gaussian92.
 *
 * @author Bradley A. Smith (yeldar@home.com)
 * @version 1.0
 */
public class Gaussian92Reader implements ChemFileReader {

    /**
	 * Create an Gaussian92 output reader.
	 *
	 * @param input source of Gaussian92 data
	 */
    public Gaussian92Reader(Reader input) {
        this.input = new BufferedReader(input);
    }

    /**
	 * Read the Gaussian92 output.
	 *
	 * @return a ChemFile with the coordinates, energies, and vibrations.
	 * @exception IOException if an I/O error occurs
	 */
    public ChemFile read() throws IOException, Exception {
        ChemFile file = new ChemFile();
        ChemFrame frame = null;
        String line = input.readLine();
        while (input.ready() && line != null) {
            if (line.indexOf("Standard orientation:") >= 0) {
                frame = new ChemFrame();
                readCoordinates(frame);
                break;
            }
            line = input.readLine();
        }
        if (frame != null) {
            line = input.readLine();
            while (input.ready() && line != null) {
                if (line.indexOf("Standard orientation:") >= 0) {
                    file.frames.addElement(frame);
                    frame = new ChemFrame();
                    readCoordinates(frame);
                } else if (line.indexOf("SCF Done:") >= 0) {
                    frame.setInfo(line.trim());
                } else if (line.indexOf("Harmonic frequencies") >= 0) {
                    readFrequencies(frame);
                }
                line = input.readLine();
            }
            file.frames.addElement(frame);
        }
        return file;
    }

    /**
	 * Reads a set of coordinates into ChemFrame.
	 *
	 * @param frame  the destination ChemFrame
	 * @exception IOException  if an I/O error occurs
	 */
    private void readCoordinates(ChemFrame frame) throws IOException, Exception {
        String line;
        line = input.readLine();
        line = input.readLine();
        line = input.readLine();
        line = input.readLine();
        while (input.ready()) {
            line = input.readLine();
            if (line == null || line.indexOf("-----") >= 0) {
                break;
            }
            int atomicNumber;
            StringReader sr = new StringReader(line);
            StreamTokenizer token = new StreamTokenizer(sr);
            token.nextToken();
            if (token.nextToken() == StreamTokenizer.TT_NUMBER) {
                atomicNumber = (int) token.nval;
                if (atomicNumber == 0) {
                    continue;
                }
            } else throw new IOException("Error reading coordinates");
            double x;
            double y;
            double z;
            if (token.nextToken() == StreamTokenizer.TT_NUMBER) {
                x = token.nval;
            } else throw new IOException("Error reading coordinates");
            if (token.nextToken() == StreamTokenizer.TT_NUMBER) {
                y = token.nval;
            } else throw new IOException("Error reading coordinates");
            if (token.nextToken() == StreamTokenizer.TT_NUMBER) {
                z = token.nval;
            } else throw new IOException("Error reading coordinates");
            frame.addVert(atomicNumber, (float) x, (float) y, (float) z);
        }
    }

    /**
	 * Reads a set of vibrations into ChemFrame.
	 *
	 * @param frame  the destination ChemFrame
	 * @exception IOException  if an I/O error occurs
	 */
    private void readFrequencies(ChemFrame frame) throws IOException, Exception {
        String line;
        line = input.readLine();
        while (input.ready() && line.indexOf("Harmonic frequencies") < 0) {
            line = input.readLine();
        }
        line = input.readLine();
        line = input.readLine();
        line = input.readLine();
        line = input.readLine();
        line = input.readLine();
        while (line.startsWith(" Frequencies --")) {
            Vector currentVibs = new Vector();
            StringReader freqValRead = new StringReader(line.substring(15));
            StreamTokenizer token = new StreamTokenizer(freqValRead);
            while (token.nextToken() != StreamTokenizer.TT_EOF) {
                Vibration freq = new Vibration(Double.toString(token.nval));
                currentVibs.addElement(freq);
            }
            line = input.readLine();
            line = input.readLine();
            line = input.readLine();
            line = input.readLine();
            line = input.readLine();
            line = input.readLine();
            for (int i = 0; i < frame.getNvert(); ++i) {
                line = input.readLine();
                StringReader vectorRead = new StringReader(line);
                token = new StreamTokenizer(vectorRead);
                token.nextToken();
                token.nextToken();
                for (int j = 0; j < currentVibs.size(); ++j) {
                    double[] v = new double[3];
                    if (token.nextToken() == StreamTokenizer.TT_NUMBER) {
                        v[0] = token.nval;
                    } else throw new IOException("Error reading frequencies");
                    if (token.nextToken() == StreamTokenizer.TT_NUMBER) {
                        v[1] = token.nval;
                    } else throw new IOException("Error reading frequencies");
                    if (token.nextToken() == StreamTokenizer.TT_NUMBER) {
                        v[2] = token.nval;
                    } else throw new IOException("Error reading frequencies");
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

    /**
	 * The source for Gaussian92 data.
	 */
    private BufferedReader input;
}
