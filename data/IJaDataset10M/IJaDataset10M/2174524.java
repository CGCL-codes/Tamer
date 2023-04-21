package library.corba.proxies;

import library.LibraryBaseClientLibrarian;
import library.corba._CORBALibraryClientLibrarianImplBase;
import library.enums.Library;
import library.proxies.ActualizeBookedItem;

public class CORBALibraryClientLibrarian extends _CORBALibraryClientLibrarianImplBase {

    protected Library library;

    protected LibraryBaseClientLibrarian baseClient;

    protected ActualizeBookedItem actualizable;

    public void setClient(LibraryBaseClientLibrarian client, Library lib) {
        baseClient = client;
        library = lib;
    }

    public void setActualizable(ActualizeBookedItem actualizable) {
        this.actualizable = actualizable;
    }

    @Override
    public boolean _equals(String library) {
        boolean eq = this.library.equals(Library.valueOf(library));
        return eq;
    }

    @Override
    public boolean actualizeBookedItems(int itemID) {
        boolean actualized = actualizable.actualizeBookedItem(itemID);
        return actualized;
    }

    public LibraryBaseClientLibrarian getClient() {
        return baseClient;
    }
}
