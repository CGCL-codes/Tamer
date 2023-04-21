package library.proxies;

import library.enums.Library;

public interface LibraryClientLibrarian {

    public boolean actualizeBookedItems(int itemID) throws Exception;

    public boolean equals(Library lib) throws Exception;
}
