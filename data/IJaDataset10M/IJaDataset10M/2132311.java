package net.bos.groupware.addressbook;

/**
 *
 * @author pes1vec
 */
public interface Directory {

    int getContactCount();

    Contact getContactAt(int index);
}
