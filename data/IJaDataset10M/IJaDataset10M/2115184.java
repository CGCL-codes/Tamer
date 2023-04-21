package org.gamenet.application.mm8leveleditor.data;

public interface DDecListBin {

    public int initialize(byte dataSrc[], int offset);

    public byte[] getDDecListBinData();

    public int getDDecListBinOffset();
}
