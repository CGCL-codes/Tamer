package joelib2.molecule;

import joelib2.io.BasicIOTypeHolder;
import joelib2.io.IOType;

/**
 *
 * @.author       wegner
 * @.wikipedia Molecule
 * @.license      GPL
 * @.cvsversion   $Revision: 1.4 $, $Date: 2005/02/17 16:48:36 $
 */
public abstract class AbstractMolecule implements Molecule {

    public static final IOType DEFAULT_IO_TYPE = BasicIOTypeHolder.instance().getIOType("SDF");

    public AbstractMolecule() {
        this(DEFAULT_IO_TYPE, DEFAULT_IO_TYPE);
    }

    public AbstractMolecule(final Molecule source) {
        this(source, false, null);
    }

    public AbstractMolecule(IOType itype, IOType otype) {
    }

    public AbstractMolecule(final Molecule source, boolean cloneDesc) {
        this(source, cloneDesc, null);
    }

    public AbstractMolecule(final Molecule source, boolean cloneDesc, String[] descriptors) {
        this();
        set(source, cloneDesc, descriptors);
    }

    public abstract Object clone();
}
