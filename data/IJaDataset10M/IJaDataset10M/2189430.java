package de.zeiban.loppe;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import de.zeiban.loppe.dbcore.*;

final class ExportSelectionAdapter extends SelectionAdapter {

    private final Shell shell;

    private final DbOperations dbTemplate;

    public ExportSelectionAdapter(final Shell shell, final Connection connection) {
        this.shell = shell;
        dbTemplate = new DbTemplate(connection);
    }

    @Override
    public void widgetSelected(final SelectionEvent e) {
        final FileDialog dlg = new FileDialog(shell, SWT.SAVE);
        dlg.setFilterExtensions(new String[] { "*.csv" });
        dlg.setFilterNames(new String[] { "Comma Separated Values" });
        final String fileName = dlg.open();
        if (fileName != null) {
            final MessageBox messageBox = new MessageBox(shell, SWT.ICON_QUESTION | SWT.YES | SWT.NO);
            messageBox.setMessage("Daten jetzt exportieren ?");
            if (messageBox.open() == SWT.YES) {
                final File file = new File(fileName);
                try {
                    final PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter(file)));
                    dbTemplate.execute("select inst, kunde, nummer, preis from kauf", new ResultCallback() {

                        public void doWithResultset(final ResultSet rs) throws SQLException {
                            while (rs.next()) {
                                final int inst = rs.getInt("inst");
                                final int kunde = rs.getInt("kunde");
                                final int nummer = rs.getInt("nummer");
                                final BigDecimal preis = rs.getBigDecimal("preis");
                                final StringBuffer sb = new StringBuffer();
                                sb.append(inst).append(";").append(kunde).append(";").append(nummer).append(";").append(new DecimalFormat("###.##").format(preis));
                                writer.println(sb.toString());
                            }
                            writer.close();
                        }
                    });
                    try {
                        writer.close();
                    } catch (final Exception ignore) {
                    }
                } catch (final IOException e1) {
                    e1.printStackTrace();
                }
            }
        }
    }
}
