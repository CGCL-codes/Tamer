package uk.ac.dl.dp.coreutil.interfaces;

import java.util.Collection;
import uk.ac.dl.dp.coreutil.exceptions.QueryException;
import uk.ac.dl.dp.coreutil.exceptions.SessionException;
import uk.ac.dl.dp.coreutil.util.DPEvent;
import uk.ac.dl.dp.coreutil.util.EventMessage;
import uk.ac.dl.dp.coreutil.util.KeywordMessage;
import uk.ac.dl.dp.coreutil.util.LoginICATMessage;
import uk.ac.dl.dp.coreutil.util.QueryRequest;

/**
 *
 * @author gjd37
 */
public interface SendMDBLocal {

    public QueryRequest queryICATs(String sid, QueryRequest q_request) throws QueryException;

    public void downloadKeywords(String sid, KeywordMessage keywordMessage);

    public void downloadKeywords(String sid, KeywordMessage keywordMessage, String facility);

    public void loginICATs(String sid, LoginICATMessage loginICATMessage) throws SessionException;

    public void sendEvent(String sid, EventMessage eventMessage);

    public void sendEvent(String sid, DPEvent event, String description);

    public void sendKeywordEvent(String sid, Collection<String> facilities, Collection<String> keywords, DPEvent type);

    public void sendKeywordEvent(String sid, Collection<String> facilities, Collection<String> keywords);

    public void sendDownloadEvent(String sid, String message, Collection<String> srburls);
}
