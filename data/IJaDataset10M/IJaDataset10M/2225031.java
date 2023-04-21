package jmotor.core.ioc.parser;

import jmotor.core.ioc.exception.ContextLoaderException;
import jmotor.core.ioc.meta.DocumentQueue;
import org.dom4j.Document;
import java.util.List;

/**
 * Component:
 * Description:
 * Date: 11-8-23
 *
 * @author Andy.Ai
 */
public interface ContextQueueParser {

    List<DocumentQueue> loadQueue(Document document) throws ContextLoaderException;
}
