package eof.administration;

/**
 *
 * @author Administrator
 */
public class InactivateExpiredBudgetAllocations {

    /** Creates a new instance of InactivateExpiredBudgetAllocations */
    public InactivateExpiredBudgetAllocations() {
    }

    private static boolean isExecuting = true;

    private static int count = 0;

    private String message = "";

    private int current = 0;

    private boolean done = false;

    private boolean canceled = false;

    public boolean isExecuting() {
        return isExecuting;
    }

    public int getCount() {
        return count;
    }

    public String getMessage() {
        message = "\nThe no budget allocations inactivated = " + count + "\n";
        return message;
    }

    public void execute() {
        try {
            isExecuting = true;
            java.sql.Connection con = reports.utility.database.PostgresConnectionPool.getInstance().getConnection();
            String query1 = "select library_id,budget_id,fiscal_year from acc_library_budget where upper(carry_forward_status) like 'N' and upper(status) like 'O'";
            java.sql.Statement stmt1 = con.createStatement();
            java.sql.ResultSet rs1 = stmt1.executeQuery(query1);
            while (rs1.next()) {
                String fiscalYear = tools.StringProcessor.getInstance().verifyStringTrim(rs1.getString(3));
                String query2 = "select end_date from acc_fiscal_year where fiscal_year like '" + fiscalYear + "'";
                java.sql.Statement stmt2 = con.createStatement();
                java.sql.ResultSet rs2 = stmt2.executeQuery(query2);
                while (rs2.next()) {
                    java.sql.Timestamp endDate = rs2.getTimestamp(1);
                    java.sql.Timestamp currentDate = reports.utility.StaticValues.getInstance().getReferenceDate();
                    if (endDate.before(currentDate)) {
                        count++;
                        String query3 = "update acc_library_budget set status='C' where library_id = '" + rs1.getString(1) + "' and budget_id = '" + rs1.getString(2) + "'";
                        java.sql.Statement stmt3 = con.createStatement();
                        stmt3.executeUpdate(query3);
                        stmt3.close();
                    }
                }
                rs2.close();
                stmt2.close();
            }
            rs1.close();
            stmt1.close();
            isExecuting = false;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void go() {
        final tools.SwingWorker worker = new tools.SwingWorker() {

            public Object construct() {
                current = 0;
                done = false;
                canceled = false;
                return new ActualTask();
            }
        };
        worker.start();
    }

    class ActualTask {

        ActualTask() {
            while (!canceled && !done) {
                try {
                    Thread.sleep(1000);
                    InactivateExpiredBudgetAllocations inactExpBudall = new InactivateExpiredBudgetAllocations();
                    inactExpBudall.execute();
                    current = inactExpBudall.getCount();
                    if (!inactExpBudall.isExecuting()) {
                        done = true;
                        current = inactExpBudall.getCount();
                    }
                } catch (InterruptedException e) {
                    System.out.println("ActualTask interrupted");
                }
            }
        }
    }
}
