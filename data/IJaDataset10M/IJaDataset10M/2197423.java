package net.sourceforge.smartconversion.apiextension.data.wrapper;

import net.sourceforge.smartconversion.api.data.item.DataEntry;
import net.sourceforge.smartconversion.api.data.wrapper.DataWrapper;

/**
 * Wraps a {@link DataEntry} as data of the current conversion. This is to be used typically for
 * sub-conversions (i.e. this holds the entry of a sub-conversion).
 *
 * @author Ovidiu Dolha
 */
public interface EntryDataWrapper extends DataWrapper {
}
