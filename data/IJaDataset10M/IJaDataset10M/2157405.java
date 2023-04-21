package com.sierra.agi.logic;

import java.io.*;

public interface LogicProvider {

    public Logic loadLogic(short logicNumber, InputStream inputStream, int size) throws IOException, LogicException;
}
