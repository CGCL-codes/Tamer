package net.teqlo.components.standard.emailV0_1;

import net.teqlo.TeqloException;
import net.teqlo.components.OutputSet;
import net.teqlo.db.ActivityLookup;
import net.teqlo.runtime.Context;

/**
 * This class uses the Javax mail library to obtain emails from the specified Email server
 * 
 * @author bhsubram, June 26, 2007
 * 
 */
public class EmailImapActivity extends AbstractEmailActivity implements Runnable {

    private AbstractEmailExecutor.DocumentLocator locator;

    private FetchResults gateData = null;

    /**
	 * Constructor as per abstract script activity
	 * 
	 * @param executor
	 * @param context
	 * @param fqn
	 * @throws TeqloException
	 */
    public EmailImapActivity(Context context, AbstractEmailExecutor executor, ActivityLookup al) throws TeqloException {
        super(context.getUser(), executor, al);
        locator = executor.getDocumentLocator(user);
        Object gate = context.getGate();
        if (gate != null && gate instanceof FetchResults) gateData = (FetchResults) gate;
    }

    @Override
    protected void actionsOnOpen() throws TeqloException {
        super.actionsOnOpen();
    }

    @Override
    protected void actionsOnRun() throws Exception {
        OutputSet output = this.addOutputSet(NOT_READ_OUTPUT_KEY);
        if (gateData != null) {
            if (gateData.errorMessage != null) handleMailError(output, gateData.errorMessage, gateData.exception); else output.setOutputKey(READ_OUTPUT_KEY);
            return;
        }
        String lockName = "MailFetch" + locator.documentFqn;
        synchronized (this.user) {
            Object data = this.user.getAttribute(lockName);
            if (data != null) {
                output.setOutputKey(IN_PROGRESS_OUTPUT_KEY);
                return;
            }
            this.user.setAttribute(lockName, new Long(System.currentTimeMillis()));
        }
        new ImapMailFetcher(this, this.user, locator, new FetchResults(), lockName).start();
        output.setOutputKey(IN_PROGRESS_OUTPUT_KEY);
    }

    public class FetchResults {

        String errorMessage;

        Exception exception;
    }
}
