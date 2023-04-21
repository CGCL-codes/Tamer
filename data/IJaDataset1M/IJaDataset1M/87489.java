package de.lamasep.gui.addresses;

import de.lamasep.map.addresses.Address;
import de.lamasep.map.addresses.AddressBook;
import de.lamasep.util.RingBuffer;
import java.util.Observable;
import java.util.Set;

/**
 * Observable Wrapper (delegate) um eine AddressBook Instanz.
 *
 * @author sniechzial
 */
public class ObservableAddressBook extends Observable {

    /**
     * AddressBook Instanz.
     */
    private final AddressBook book;

    /**
     * Konstruktor - Initialisierung mit AddressBook Instanz.
     * @param book
     */
    public ObservableAddressBook(AddressBook book) {
        super();
        this.book = book;
    }

    /**
     * Gibt die AddressBook Instanz dieses Wrappres zurück.
     * @return verwaltete AddressBook Instanz.
     */
    public AddressBook getAddressBook() {
        return book;
    }

    /**
     * @param address zu entfernende Adresse
     * @return <code>true</code> wenn die Lösch operation erfoglreich war,
     *          <code>false</code> wenn die Addresse nicht bekannt ist
     * @see AddressBook#remove(de.lamasep.map.addresses.Address)
     */
    public boolean remove(Address address) {
        boolean result = book.remove(address);
        setChanged();
        notifyObservers();
        return result;
    }

    /**
     * @param prefix zu betrachtender Präfix
     * @return Valide Folgebuchstaben oder <code>null</code>, wenn der Präfix
     *              nicht bekannt ist.
     * @see AddressBook#getPrefixExtensions(java.lang.String)
     */
    public char[] getPrefixExtensions(String prefix) {
        return book.getPrefixExtensions(prefix);
    }

    /**
     * @return Ringpuffer, der die letzten angefahrenen Ziele enthält
     * @see AddressBook#getLastDestinations()
     */
    public RingBuffer<Address> getLastDestinations() {
        return book.getLastDestinations();
    }

    /**
     * @param prefix Zu suchender Präfix. <code>prefix != null</code>
     * @return Gefundene Adressen
     * @see AddressBook#get(java.lang.String)
     */
    public Set<Address> get(String prefix) {
        return book.get(prefix);
    }

    /**
     * @param dest letztes angefahrenes Ziel, <code>dest != null</code>
     * @see AddressBook#addDestination(de.lamasep.map.addresses.Address)
     */
    public void addDestination(Address dest) {
        book.addDestination(dest);
        setChanged();
        notifyObservers();
    }

    /**
     * @param address   Einzufügende Adresse <code>address != null</code>
     * @see AddressBook#add(de.lamasep.map.addresses.Address)
     */
    public void add(Address address) {
        book.add(address);
        setChanged();
        notifyObservers();
    }
}
