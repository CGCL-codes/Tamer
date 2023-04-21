package vaj2cvs;

/**
 * The class called by the <I>Tools, CVS, log</I> menus.
 * Returns log information on checked out files.
 * <P>
 * This can be run at the class level.
 */
public final class CvsLog {

    /**
 * Starts the application.
 * @param args an array of command-line arguments
 */
    public static void main(java.lang.String[] args) {
        CvsProgressDialog progressDialog = new CvsProgressDialog();
        progressDialog.setDefaultSize();
        progressDialog.show();
        CvsConnection conn = null;
        try {
            conn = CvsConnection.getNewConnection(progressDialog);
            String[] selections;
            if (args[0].equals("-c")) {
                selections = VAJ.buildFileListFromClassArgs(args);
            } else if (args[0].equals("-R")) {
                selections = VAJ.buildFileListFromResourceArgs(args);
            } else {
                progressDialog.appendProgress("Bad parameter list.");
                throw new CvsException("Bad parameter list.");
            }
            progressDialog.setStatus("Sending arguments...");
            for (int i = 0; i < selections.length; ++i) {
                conn.sendArgument(selections[i]);
            }
            progressDialog.setStatus("Sending status of current files...");
            conn.sendStatus(selections, "log", null);
            conn.sendRequest("log");
            progressDialog.setStatus("Processing server response...");
            conn.processResponseStream("log", null);
            progressDialog.appendProgress("----- Done. -----");
            progressDialog.setStatus("Done.");
        } catch (CvsErrorResponseException e) {
            progressDialog.setStatus("Done.  Problem found by CVS server.");
        } catch (Exception e) {
            progressDialog.setStatus("Done.  ***** Java exception thrown by tool. *****");
            e.printStackTrace();
        } finally {
            if (conn != null) conn.close();
        }
    }
}
