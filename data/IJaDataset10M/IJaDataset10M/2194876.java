package at.jku.semwiq.swing;

import java.util.concurrent.ExecutionException;
import com.hp.hpl.jena.query.Query;

/**
 * @author dorgon, Andreas Langegger, al@jku.at
 *
 */
public class QueryProcessingTaskBoolean extends QueryProcessingTask<Boolean, Void> {

    /**
	 * @param client
	 * @param q
	 */
    public QueryProcessingTaskBoolean(SwingApp client, Query q) {
        super(client, q);
    }

    @Override
    protected Boolean queryInBackground() {
        return queryExec.execAsk();
    }

    @Override
    protected void queryDone() throws InterruptedException, ExecutionException {
        boolean answer = get();
        client.getTab().initResultTextArea("Answer: " + answer);
        client.getProgressBar().setString("Answer: " + answer);
    }
}
