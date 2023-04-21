package vaj2cvs;

/**
 * The class called by the <I>Tools, CVS, diff</I> menus.
 */
public final class CvsDiff {

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
            if (args.length == 0) {
                selections = VAJ.buildFileListFromProjectArgs(new String[] { "", "VAJ-Workspace" }, conn, progressDialog);
            } else if (args[0].equals("-P")) {
                selections = VAJ.buildFileListFromProjectArgs(args, conn, progressDialog);
            } else if (args[0].equals("-p")) {
                selections = VAJ.buildFileListFromPackageArgs(args);
            } else if (args[0].equals("-c")) {
                selections = VAJ.buildFileListFromClassArgs(args);
            } else if (args[0].equals("-R")) {
                selections = VAJ.buildFileListFromResourceArgs(args);
            } else {
                progressDialog.appendProgress("Bad parameter list.");
                throw new CvsException("Bad parameter list.");
            }
            progressDialog.setStatus("Sending arguments...");
            conn.sendArgument("-luw");
            for (int i = 0; i < selections.length; ++i) {
                conn.sendArgument(selections[i]);
            }
            progressDialog.setStatus("Sending status of current files...");
            conn.sendStatus(selections, "diff", null);
            conn.sendRequest("diff");
            progressDialog.setStatus("Processing server response...");
            conn.processResponseStream("diff", null);
            progressDialog.appendProgress("----- Done. -----");
            progressDialog.setStatus("Done, no differences found.");
        } catch (CvsErrorResponseException e) {
            progressDialog.setStatus("Done.  Differences or problems found by CVS server.");
        } catch (Exception e) {
            progressDialog.setStatus("Done.  ***** Java exception thrown by tool. *****");
            e.printStackTrace();
        } finally {
            if (conn != null) conn.close();
        }
    }
}
