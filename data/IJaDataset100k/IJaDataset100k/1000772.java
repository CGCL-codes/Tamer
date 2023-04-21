package org.dbunit.database.search;

import java.sql.SQLException;
import org.dbunit.database.AbstractImportedAndExportedKeysFilteredByPKsTestCase;
import org.dbunit.dataset.DataSetException;
import org.dbunit.util.search.SearchException;

public class ImportedAndExportedKeysFilteredByPKsTest extends AbstractImportedAndExportedKeysFilteredByPKsTestCase {

    public ImportedAndExportedKeysFilteredByPKsTest(String testName) {
        super(testName, "hypersonic_dataset.sql");
    }

    protected int[] setupTablesSizeFixture() {
        int[] sizes = new int[] { 2, 8, 4, 2, 4, 2, 2, 2 };
        return sizes;
    }

    private void addAllOutput() {
        addOutput(A, new String[] { A1, A2 });
        addOutput(B, new String[] { B1, B2, B3, B4, B5, B6, B7, B8 });
        addOutput(C, new String[] { C1, C2, C3, C4 });
        addOutput(D, new String[] { D1, D2 });
        addOutput(E, new String[] { E1, E2, E3, E4 });
        addOutput(F, new String[] { F1, F2 });
        addOutput(G, new String[] { G1, G2 });
        addOutput(H, new String[] { H1, H2 });
    }

    public void testBWithOne() throws DataSetException, SQLException, SearchException {
        addInput(B, new String[] { B1 });
        addOutput(A, new String[] { A1, A2 });
        addOutput(B, new String[] { B1 });
        addOutput(C, new String[] { C1, C2, C3, C4 });
        addOutput(D, new String[] { D1, D2 });
        addOutput(E, new String[] { E1, E2, E3, E4 });
        addOutput(F, new String[] { F1, F2 });
        addOutput(G, new String[] { G1, G2 });
        addOutput(H, new String[] { H1, H2 });
        doIt();
    }

    public void testBWithRepeated() throws DataSetException, SQLException, SearchException {
        addInput(B, new String[] { B1, B4, B3, B2, B1, B1, B8, B7 });
        addOutput(A, new String[] { A1, A2 });
        addOutput(B, new String[] { B1, B2, B3, B4, B7, B8 });
        addOutput(C, new String[] { C1, C2, C3, C4 });
        addOutput(D, new String[] { D1, D2 });
        addOutput(E, new String[] { E1, E2, E3, E4 });
        addOutput(F, new String[] { F1, F2 });
        addOutput(G, new String[] { G1, G2 });
        addOutput(H, new String[] { H1, H2 });
        doIt();
    }

    public void testBWithAllRepeated() throws DataSetException, SQLException, SearchException {
        addInput(B, new String[] { B1, B4, B3, B2, B1, B1, B1, B6, B6, B1, B5, B8, B7 });
        addAllOutput();
        doIt();
    }

    public void testDWithOne() throws DataSetException, SQLException, SearchException {
        addInput(D, new String[] { D1 });
        addInput(B, new String[] { B1 });
        addOutput(A, new String[] { A1, A2 });
        addOutput(B, new String[] { B1 });
        addOutput(C, new String[] { C1, C2, C3, C4 });
        addOutput(D, new String[] { D1 });
        addOutput(E, new String[] { E1, E2, E3, E4 });
        addOutput(F, new String[] { F1, F2 });
        addOutput(G, new String[] { G1, G2 });
        addOutput(H, new String[] { H1, H2 });
        doIt();
    }

    public void testDWithTwo() throws DataSetException, SQLException, SearchException {
        addInput(D, new String[] { D1, D2 });
        addAllOutput();
        doIt();
    }

    public void testDWithRepeated() throws DataSetException, SQLException, SearchException {
        addInput(D, new String[] { D1, D2, D2, D1, D1, D1, D2, D2 });
        addAllOutput();
        doIt();
    }
}
