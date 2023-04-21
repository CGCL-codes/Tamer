package org.xmlfield.tests.pack1;

import org.xmlfield.annotations.FieldXPath;
import org.xmlfield.annotations.ResourceXPath;

/**
 * @author Nicolas Richeton <nicolas.richeton@capgemini.com>
 */
@ResourceXPath("/Catalog")
public interface Catalog {

    @FieldXPath("Cd")
    Cd[] getCd();

    void setCd(Cd[] cds);

    Cd addToCd();

    void removeFromCd(Cd cd);

    @FieldXPath("Others/Misc1/Misc2/Misc3/Cd")
    OtherCd[] getOtherCd();

    OtherCd addToOtherCd();

    int sizeOfCd();

    @Override
    String toString();
}
