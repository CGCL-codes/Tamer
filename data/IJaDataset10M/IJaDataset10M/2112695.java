package it.unibo.mortemale.cracker;

import it.unibo.mortemale.tuples.*;
import alice.tucson.api.*;
import alice.logictuple.*;

public interface ICrackerAdapter {

    public Dictionary getDictionary() throws LogicTupleException, TucsonException;

    public Task[] getTasks(String algorithm) throws LogicTupleException, TucsonException;

    public void putSolution(Solution s) throws TucsonException;
}
