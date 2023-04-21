package org.neuroph.contrib.neat.gen.persistence.impl;

import java.io.File;
import org.neuroph.contrib.neat.gen.FitnessScores;
import org.neuroph.contrib.neat.gen.Generation;
import org.neuroph.contrib.neat.gen.Innovations;
import org.neuroph.contrib.neat.gen.persistence.PersistenceException;

/**
 * A <code>SerializationDelegate</code> is used to perform the serialization as part
 * of the <code>DirectoryOutputPersistence</code> implementation.
 * 
 * @author Aidan Morgan
 */
public interface SerializationDelegate {

    public void writeInnovations(File outFile, Innovations o) throws PersistenceException;

    public void writeFitnessScores(File outFile, FitnessScores s) throws PersistenceException;

    public void writeGeneration(File outFile, Generation s) throws PersistenceException;

    public Innovations readInnovations(File f) throws PersistenceException;

    public FitnessScores readFitnessScores(File f) throws PersistenceException;

    public Generation readGeneration(File f, Innovations innovations) throws PersistenceException;

    public String getFileExtension();
}
