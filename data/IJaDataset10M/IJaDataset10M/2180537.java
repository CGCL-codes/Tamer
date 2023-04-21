package com.groovytagger.interfaces;

public interface ID3Subject {

    public void addID3Observer(ID3Observer oID3Observer);

    public void removeID3Observer(ID3Observer oID3Observer);

    public void notifyID3Observers() throws Exception;
}
