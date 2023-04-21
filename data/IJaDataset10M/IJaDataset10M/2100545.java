package be.vds.jtbdive.core.core.divecomputer.parser;

import java.util.List;
import be.vds.jtbdive.core.core.Dive;
import be.vds.jtbdive.core.core.MatCave;
import be.vds.jtbdive.core.exceptions.TransferException;
import be.vds.jtbdive.core.interfaces.DataCommInterface;

public interface DiveComputerDataParser {

    public void setDataComInterface(DataCommInterface dataComInterface);

    public List<Dive> read(MatCave matCave) throws TransferException;

    public List<Dive> convertBinaries(int[] binaries, MatCave matCave) throws TransferException;

    public int[] getBinaries();

    public void close();
}
