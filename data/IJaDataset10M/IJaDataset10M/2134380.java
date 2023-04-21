package fca.io.context.reader.txt;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.StringTokenizer;
import fca.core.context.Context;
import fca.core.context.binary.BinaryContext;
import fca.exception.AlreadyExistsException;
import fca.exception.ReaderException;
import fca.exception.InvalidTypeException;
import fca.io.context.reader.ContextReaderTXT;
import fca.messages.IOMessages;

/**
 * Le LatticeReader pour la lecture de contexte binaire au format Lattice Miner
 * @author Ludovic Thomas
 * @version 1.0
 */
public class LMBinaryContextReader extends ContextReaderTXT {

    /**
	 * Constructeur du lecteur de contexte binaire de Lattice Miner
	 * @param file le fichier a lire
	 * @throws FileNotFoundException si le fichier ne peut �tre trouv�
	 * @throws ReaderException si une erreur de lecture arrive
	 */
    public LMBinaryContextReader(File file) throws FileNotFoundException, ReaderException {
        super(file, Context.HeaderType.LM_BINARY);
    }

    @Override
    protected void initContext() {
        context = new BinaryContext("");
    }

    @Override
    protected void readObjects() throws AlreadyExistsException {
        String objects = reader.readLine();
        StringTokenizer tok = new StringTokenizer(objects, "|");
        while (tok.hasMoreTokens()) {
            context.addObject(tok.nextToken().trim());
        }
    }

    @Override
    protected void readAttributes() throws AlreadyExistsException {
        String attributes = reader.readLine();
        StringTokenizer tok = new StringTokenizer(attributes, "|");
        while (tok.hasMoreTokens()) {
            context.addAttribute(tok.nextToken().trim());
        }
    }

    @Override
    protected void readData() throws ReaderException, InvalidTypeException {
        for (int i = 0; i < context.getObjectCount(); i++) {
            String values = reader.readLine();
            StringTokenizer tok = new StringTokenizer(values);
            if (tok.countTokens() != context.getAttributeCount()) {
                throw new ReaderException(IOMessages.getString("IO.ReaderException"));
            }
            int j = 0;
            while (tok.hasMoreTokens()) {
                String value = tok.nextToken();
                if (value.equals("1")) {
                    context.setValueAt(BinaryContext.TRUE, i, j);
                }
                j++;
            }
        }
    }
}
