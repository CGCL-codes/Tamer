package com.android.internal.telephony;

import android.os.ServiceManager;
import android.test.suitebuilder.annotation.Suppress;
import java.util.List;
import junit.framework.TestCase;

@Suppress
public class SimPhoneBookTest extends TestCase {

    public void testBasic() throws Exception {
        IIccPhoneBook simPhoneBook = IIccPhoneBook.Stub.asInterface(ServiceManager.getService("simphonebook"));
        assertNotNull(simPhoneBook);
        int size[] = simPhoneBook.getAdnRecordsSize(IccConstants.EF_ADN);
        assertNotNull(size);
        assertEquals(3, size.length);
        assertEquals(size[0] * size[2], size[1]);
        assertTrue(size[2] >= 100);
        List<AdnRecord> adnRecordList = simPhoneBook.getAdnRecordsInEf(IccConstants.EF_ADN);
        adnRecordList = simPhoneBook.getAdnRecordsInEf(IccConstants.EF_ADN);
        assertNotNull(adnRecordList);
        int adnIndex, listIndex = 0;
        AdnRecord originalAdn = null;
        for (adnIndex = 3; adnIndex >= 1; adnIndex--) {
            listIndex = adnIndex - 1;
            originalAdn = adnRecordList.get(listIndex);
            assertNotNull("Original Adn is Null.", originalAdn);
            assertNotNull("Original Adn alpha tag is null.", originalAdn.getAlphaTag());
            assertNotNull("Original Adn number is null.", originalAdn.getNumber());
            if (originalAdn.getNumber().length() > 0 && originalAdn.getAlphaTag().length() > 0) {
                break;
            }
        }
        if (adnIndex == 0) return;
        AdnRecord emptyAdn = new AdnRecord("", "");
        AdnRecord firstAdn = new AdnRecord("John", "4085550101");
        AdnRecord secondAdn = new AdnRecord("Andy", "6505550102");
        String pin2 = null;
        boolean success = simPhoneBook.updateAdnRecordsInEfByIndex(IccConstants.EF_ADN, firstAdn.getAlphaTag(), firstAdn.getNumber(), adnIndex, pin2);
        adnRecordList = simPhoneBook.getAdnRecordsInEf(IccConstants.EF_ADN);
        AdnRecord tmpAdn = adnRecordList.get(listIndex);
        assertTrue(success);
        assertTrue(firstAdn.isEqual(tmpAdn));
        success = simPhoneBook.updateAdnRecordsInEfBySearch(IccConstants.EF_ADN, firstAdn.getAlphaTag(), firstAdn.getNumber(), secondAdn.getAlphaTag(), secondAdn.getNumber(), pin2);
        adnRecordList = simPhoneBook.getAdnRecordsInEf(IccConstants.EF_ADN);
        tmpAdn = adnRecordList.get(listIndex);
        assertTrue(success);
        assertFalse(firstAdn.isEqual(tmpAdn));
        assertTrue(secondAdn.isEqual(tmpAdn));
        success = simPhoneBook.updateAdnRecordsInEfBySearch(IccConstants.EF_ADN, secondAdn.getAlphaTag(), secondAdn.getNumber(), emptyAdn.getAlphaTag(), emptyAdn.getNumber(), pin2);
        adnRecordList = simPhoneBook.getAdnRecordsInEf(IccConstants.EF_ADN);
        tmpAdn = adnRecordList.get(listIndex);
        assertTrue(success);
        assertTrue(tmpAdn.isEmpty());
        success = simPhoneBook.updateAdnRecordsInEfByIndex(IccConstants.EF_ADN, originalAdn.getAlphaTag(), originalAdn.getNumber(), adnIndex, pin2);
        adnRecordList = simPhoneBook.getAdnRecordsInEf(IccConstants.EF_ADN);
        tmpAdn = adnRecordList.get(listIndex);
        assertTrue(success);
        assertTrue(originalAdn.isEqual(tmpAdn));
    }
}
