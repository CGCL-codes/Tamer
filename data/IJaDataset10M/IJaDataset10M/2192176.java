package org.proteinshader.structure;

import org.proteinshader.structure.visitor.exceptions.*;
import org.proteinshader.structure.visitor.*;
import org.proteinshader.structure.exceptions.*;
import org.proteinshader.structure.enums.*;

/*******************************************************************************
This concrete subclass of the abstract class Residue stores
information on a heterogen in a PDB structure entry.  Because water
molecules are a special case of the heterogen concept, their
information is stored in a Water object rather than in a Heterogen
object.
*******************************************************************************/
public class Heterogen extends Residue {

    /** 'HET_' will be added to the beginning the residueID
        for a Heterogen (if it is not already present). */
    public static final String HETEROGEN_PREFIX = "HET_";

    private final String m_name;

    /***************************************************************************
    Creates a Heterogen with the name and IDs given as arguments
    (after adding the HETEROGEN_PREFIX ("HET_") to the beginning of
    residueID).  Any leading or trailing whitespace is trimmed from
    the residueID (before adding "HET_") and from the name.

    @param name         Heterogen's name (preferably the full name).
    @param residueID    ID of the Heterogen.
    @param chainID      ID of the Chain.
    @param modelID      ID of the Model.
    @param structureID  ID of the Structure.
    @throws MissingHetNameException  if name is null or does not have
                                     at least one non-whitespace
                                     character before add the
                                     HETEROGEN_PREFIX.
    @throws InvalidIDException  if residueID is null or does not have
                                at least one non-whitespace character.
    ***************************************************************************/
    public Heterogen(String name, String residueID, String chainID, String modelID, String structureID) throws MissingHetNameException, InvalidIDException {
        super(residueID, chainID, modelID, structureID);
        m_name = super.processID(name, "heterogen name");
    }

    /***************************************************************************
    Accepts a Visitor and does a callback.

    @param visitor  the Visitor to do a callback with.
    @throws VisitorException  if an error occurs while an object is
                              being visited.
    ***************************************************************************/
    public void accept(Visitor visitor) throws VisitorException {
        visitor.visit(this);
    }

    /***************************************************************************
    Returns the Heterogen's name.

    The String returned cannot be null or empty, because the
    constructor checks that this read-only attribute has at least
    one non-whitespace character.

    @return  The Heterogen name as a String.
    ***************************************************************************/
    public String getName() {
        return m_name;
    }

    /***************************************************************************
    Overrides the processID() method of Residue in order to add
    guarantee that the residueID of a Heterogen always begins with
    the HETEROGEN_PREFIX.  If the HETEROGEN_PREFIX is not already
    present, it will be added.  Also, any leading or trailing
    whitespace will be trimmed off of the residueID.

    @param id  Residue ID to process.
    @param typeOfID  type of ID (for possible use in error message).
    @return The trimmed Residue ID beginning with the
            HETEROGEN_PREFIX.
    @throws InvalidIDException  if the trimmed Residue ID does not
                                have at least one character before
                                adding the prefix.
    ***************************************************************************/
    public String processID(String id, String typeOfID) throws InvalidIDException {
        id = super.processID(id, typeOfID);
        int prefixLength = HETEROGEN_PREFIX.length();
        if (id.length() <= prefixLength || !id.substring(0, prefixLength).equals(HETEROGEN_PREFIX)) {
            id = HETEROGEN_PREFIX + id;
        }
        return id;
    }
}
