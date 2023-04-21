package pocu.data_structs;

/**
 * This allows centralized sorting using Sorter.
 */
interface SLinkedBin {

    SLinkedBin getNext();

    void setNext(SLinkedBin bin);
}
