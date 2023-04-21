package com.softwaresmithy.metadata;

import android.net.Uri;
import com.softwaresmithy.BookJB;

/**
 * Interface for a book Metadata Provider.
 *
 * @author matt
 */
public interface MetadataProvider {

    /**
   * Given an ISBN, the MetadataProvider should populate it to the best of its abilities.
   *
   * @param isbn 10 or 13 digit valid isbn number
   * @return populated BookJB
   */
    BookJB getInfo(String isbn);

    Uri getBookInfoPage(BookJB book);

    String getProviderName();
}
