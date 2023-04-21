package uk.ac.ebi.rhea.util;

import static org.junit.Assert.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import uk.ac.ebi.rhea.domain.Compound;
import uk.ac.ebi.rhea.domain.Database;
import uk.ac.ebi.rhea.domain.Reaction;
import uk.ac.ebi.rhea.domain.Side;

public class ChebiIdEquationWriterTest {

    Compound lactate, formate, acetaldehyde;

    Reaction reaction;

    @Before
    public void setUp() throws Exception {
        lactate = Compound.valueOf(1l, "(A)-lactate", "C3H5O3", 0, "CHEBI", "CHEBI:16651", null, null);
        formate = Compound.valueOf(2l, "formate", "CHO2", 0, "CHEBI", "CHEBI:15740", null, null);
        acetaldehyde = Compound.valueOf(3l, "acetaldehyde", "C2H4O", 0, "CHEBI", "CHEBI:15343", null, null);
        reaction = new Reaction(null, Database.INTENZ);
        reaction.modifyParticipant(lactate, 1, null, Side.LEFT, null);
        reaction.modifyParticipant(acetaldehyde, 1, null, Side.RIGHT, null);
        reaction.modifyParticipant(formate, 1, null, Side.RIGHT, null);
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void testWrite() {
        assertEquals("CHEBI:16651 <?> CHEBI:15343 + CHEBI:15740", new ChebiIdEquationWriter().getChebiEquation(reaction));
    }
}
