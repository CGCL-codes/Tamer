package net.sf.bacchus.integration.spreadsheet;

import org.apache.poi.ss.usermodel.Workbook;

/** Factory to create new {@link Workbook} instances. */
public interface WorkbookFactory {

    /**
     * Generates a {@link Workbook}.
     * @return the workbook.
     */
    Workbook create();
}
