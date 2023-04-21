package org.couchunit.doc.format;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import java.util.List;
import org.junit.Before;
import org.junit.Test;

public class Couch_DatasetFormatCheckerUTest {

    private IDatasetChecker testSubject;

    private String datasetAsString;

    @Before
    public void startTheTest() {
        testSubject = new DatasetChecker();
    }

    @Test
    public void checkDataset_XmlTagsExistInString() {
        datasetAsString = "<dataset><doc something /></dataset>";
        assertTrue(testSubject.containsCouchUnitFormat(datasetAsString));
    }

    @Test
    public void checkDatasetWithNotagsFlaggedAsFalse() {
        assertFalse(testSubject.containsCouchUnitFormat(""));
    }

    @Test
    public void checkDatasetWithBadlyFormedTagsFlaggedAsFalse() {
        datasetAsString = "<dataset><dataset>";
        assertFalse(testSubject.containsCouchUnitFormat(datasetAsString));
    }

    @Test
    public void checkDatasetWithMisspeltDatasetTagsFlaggedAsFalse() {
        datasetAsString = "<dataset></wataset>";
        assertFalse(testSubject.containsCouchUnitFormat(datasetAsString));
    }

    @Test
    public void checkDatasetWithout_DocBasicElement_DoesNotFly() {
        datasetAsString = "<dataset>someContentWithoutDocElement</dataset>";
        assertFalse(testSubject.containsCouchUnitFormat(datasetAsString));
    }

    @Test
    public void checkDatasetWith_DocBasicElementUnspaced_DoesNotFly() {
        datasetAsString = "<dataset><doc/></dataset>";
        assertFalse(testSubject.containsCouchUnitFormat(datasetAsString));
    }

    @Test
    public void checkDatasetWith_DocBasicElementSpaced_DoesFly() {
        datasetAsString = "<dataset><doc /></dataset>";
        assertFalse(testSubject.containsCouchUnitFormat(datasetAsString));
    }

    @Test
    public void checkDatasetWith_DocElementClosingSlashNotSpaced_DoesNotFly() {
        datasetAsString = "<dataset><doc whatever/></dataset>";
        assertFalse(testSubject.containsCouchUnitFormat(datasetAsString));
    }

    @Test
    public void checkDatasetWith_DocElementClosingSlashSpaced_DoesFly() {
        datasetAsString = "<dataset><doc whatever /></dataset>";
        assertTrue(testSubject.containsCouchUnitFormat(datasetAsString));
    }

    @Test
    public void checkDatasetWith_StartingWhitespaceBetweenDATASET_AND_DOC_DoesFly() {
        datasetAsString = "<dataset>    <doc whatever /></dataset>";
        assertTrue(testSubject.containsCouchUnitFormat(datasetAsString));
    }

    @Test
    public void checkDatasetWith_NO_StartingWhitespaceBetweenDATASET_AND_DOC_DoesFly() {
        datasetAsString = "<dataset><doc whatever /></dataset>";
        assertTrue(testSubject.containsCouchUnitFormat(datasetAsString));
    }

    @Test
    public void checkDatasetWith_EndingWhitespaceBetweenDOC_AND_DATASET_DoesFly() {
        datasetAsString = "<dataset><doc whatever />   </dataset>";
        assertTrue(testSubject.containsCouchUnitFormat(datasetAsString));
    }

    @Test
    public void checkDatasetWith_NO_EndingWhitespaceBetweenDOC_AND_DATASET_DoesFly() {
        datasetAsString = "<dataset><doc whatever /></dataset>";
        assertTrue(testSubject.containsCouchUnitFormat(datasetAsString));
    }

    @Test
    public void checkDatasetWith_Ending_AND_Starting_WhitespaceBetweenDOC_AND_DATASET_DoesFly() {
        datasetAsString = "<dataset>   <doc whatever />                        </dataset>";
        assertTrue(testSubject.containsCouchUnitFormat(datasetAsString));
    }

    @Test
    public void checkDatasetWith_MoreThanOneDocElement_DoesFly() {
        datasetAsString = "<dataset>   <doc whatever /> <doc you /> <doc want /> </dataset>";
        assertTrue(testSubject.containsCouchUnitFormat(datasetAsString));
    }

    @Test
    public void checkDatasetWith_MoreThanOneDocElement_AndNoWhitespaceAtAll_DoesFly() {
        datasetAsString = "<dataset><doc whatever /><doc you /><doc want /></dataset>";
        assertTrue(testSubject.containsCouchUnitFormat(datasetAsString));
    }

    @Test
    public void checkDatasetFromIntegrationTestThatSurprisinglyFailed() {
        datasetAsString = "<dataset><doc id:1234, hello:bienvenidos /></dataset>";
        assertTrue(testSubject.containsCouchUnitFormat(datasetAsString));
    }

    @Test
    public void checkDatasetWithWhitespaceBeforeValueConformsToFormat() {
        datasetAsString = "<dataset><doc  1: 2 /></dataset>";
        assertTrue(testSubject.containsCouchUnitFormat(datasetAsString));
    }
}
