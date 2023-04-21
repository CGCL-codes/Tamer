package pl.prv.consept.gestionnaire.dialogs;

import java.sql.SQLException;
import java.util.Date;
import java.util.Calendar;
import pl.prv.consept.gestionnaire.database.*;

/**
 * Calcul des factures task
 *
 * @author Sebastian Solnica
 * @version $20.02.2006 v1.00$
 */
public class SuppressionDesFacturesTask extends AbstractTask {

    private final int lineStart;

    private final int lineEnd;

    private final DatabaseAccess dbaccess;

    private final DataProvider facturesDP;

    private final RubriquesDeFacturesFDP rubriquesDeFacturesFDP;

    /**
	 * Constructor
	 */
    public SuppressionDesFacturesTask(DatabaseAccess da, int lineStart, int lineEnd) throws SQLException {
        super(Math.abs(lineEnd - lineStart) + 1);
        this.dbaccess = da;
        this.lineStart = Math.min(lineStart, lineEnd);
        this.lineEnd = Math.max(lineStart, lineEnd);
        this.facturesDP = FacturesDP.getDataProvider(da);
        this.rubriquesDeFacturesFDP = (RubriquesDeFacturesFDP) RubriquesDeFacturesFDP.getFilteredDataProvider(da, null);
    }

    public void go() {
        final SwingWorker worker = new SwingWorker() {

            public Object construct() {
                current = 0;
                done = false;
                canceled = false;
                statMessage = null;
                return new ActualTask();
            }
        };
        worker.start();
    }

    public void cleanUp() {
    }

    class ActualTask {

        ActualTask() {
            try {
                int end = Math.abs(lineEnd - lineStart);
                for (int i = 0; i <= end; i++) {
                    Object[] row = facturesDP.getRow(lineStart);
                    statMessage = "Facture: " + row[0].toString();
                    rubriquesDeFacturesFDP.deleteFactureRubriques(row[0]);
                    facturesDP.deleteRow(lineStart);
                    current++;
                    Thread.sleep(ProgressPane.ONE_TICK);
                }
                done = true;
            } catch (Exception e) {
                canceled = true;
            }
        }
    }
}
