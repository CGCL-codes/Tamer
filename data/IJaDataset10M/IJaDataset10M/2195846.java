package uk.ac.ebi.pride.gui.component.model;

import uk.ac.ebi.pride.data.core.Chromatogram;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: rwang
 * Date: 12-Apr-2010
 * Time: 16:05:12
 */
public class ChromatogramTableModel extends DynamicDataTableModel<Chromatogram> {

    /** table column title */
    public enum TableHeader {

        CHROMATOGRAM_ID_COLUMN("Chromatogram ID", String.class);

        private final String header;

        private final Class headerClass;

        private TableHeader(String header, Class classType) {
            this.header = header;
            this.headerClass = classType;
        }

        public String getHeader() {
            return header;
        }

        public Class getHeaderClass() {
            return headerClass;
        }
    }

    @Override
    public void initializeTable() {
        TableHeader[] headers = TableHeader.values();
        for (TableHeader header : headers) {
            columnNames.put(header.getHeader(), header.getHeaderClass());
        }
    }

    @Override
    public void addData(Chromatogram newData) {
        List<Object> content = new ArrayList<Object>();
        content.add(newData.getId());
        this.addRow(content);
    }
}
