package sk.sigp.tetras.crawl.parser;

import java.util.List;
import sk.sigp.tetras.entity.Firma;

public interface IParserAdapter {

    /**
	 * asynchronous callback return point if succesful
	 * @param companies
	 * @param callbackId
	 */
    void parserAsyncReturnPoint(List<Firma> companies, long callbackId);

    /**
	 * asynchronous callback return point if failed
	 * @param companies
	 * @param callbackId
	 */
    void parserAsyncFailNotification(long callbackId, Exception e);
}
