package vaj2cvs;

/**
 * The class called by the <I>Tools, CVS, tag</I> menus.
 * Tags specified resources.
 * <P>
 * This can be run at the workspace, project, package, resource, or class level.
 */
public final class CvsTag {

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
            progressDialog.setStatus("Initializing...");
            CvsTagDialog tagDialog = new CvsTagDialog();
            Util.centerWindowOnScreen(tagDialog);
            tagDialog.show();
            if (!tagDialog.getCancelSelected()) {
                progressDialog.setStatus("Sending arguments...");
                if (tagDialog.getBranchSelected()) {
                    conn.sendArgument("-b");
                }
                if (tagDialog.getCurrentSelected()) {
                    conn.sendArgument("-c");
                } else if (tagDialog.getDateSelected()) {
                    conn.sendArgument("-D");
                    conn.sendArgument(tagDialog.getDateText());
                } else {
                    conn.sendArgument("-r");
                    conn.sendArgument(tagDialog.getRevisionText());
                }
                conn.sendArgument(tagDialog.getTagText());
                progressDialog.setStatus("Sending status of current files...");
                conn.sendStatus(selections, "status", null);
                conn.sendRequest("tag");
                progressDialog.setStatus("Processing server response...");
                conn.processResponseStream("status", null);
                progressDialog.appendProgress("----- Done. -----");
            } else {
                progressDialog.appendProgress("----- Cancelled -----");
            }
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
