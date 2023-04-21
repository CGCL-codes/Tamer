package ru.adv.xml.newt;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import ru.adv.cache.Expirable;
import ru.adv.cache.Expires;
import ru.adv.cache.Include;
import ru.adv.db.filter.FilterMap;
import ru.adv.db.handler.HandlerException;
import ru.adv.logger.TLogger;
import ru.adv.util.BadDateException;
import ru.adv.util.ErrorCodeException;
import ru.adv.util.XmlUtils;
import ru.adv.util.timing.Stopwatch;
import ru.adv.xml.parser.XmlDoc;

/**
 * Обработка Newt тэгов.
 * @version $Revision: 1.16 $
 */
public class NewtTraverse implements Expirable {

    private static NewtFactory newt_factory = NewtFactory.newInstance();

    private static final int MAX_TRANSFORM_RECURSION = 10;

    private static final String ERROR_TAG = "newt-error";

    private NewtContext context;

    private Expires expires = new Expires();

    private HashSet<Include> includes = new HashSet<Include>();

    private Set<Include> extraIncludes = null;

    private TLogger logger = new TLogger(NewtTraverse.class);

    /**
	 * Constructor()
	 */
    public NewtTraverse() {
    }

    public void setExpiresTime(long time) {
        this.expires.setExpiresTime(time);
    }

    public void setExpiresTime(String time) throws BadDateException {
        this.expires.setExpiresTime(time);
    }

    public long getExpiresTime() {
        return this.expires.getExpiresTime();
    }

    public Collection<Include> getIncludes() {
        return Collections.unmodifiableCollection(this.includes);
    }

    private void checkMaxReqursion(NewtContext context) throws NewtException {
        if (context.getRecursionLevel() > MAX_TRANSFORM_RECURSION) {
            throw new NewtException(null, NewtException.DEEP_RECURSION, "Maximum recursive level reached while traversing dinamic newt nodes");
        }
    }

    /**
     * Run newts for node
     */
    public void run(NewtContext context, Node node, XmlDoc owner, Set<Include> additionalIncludes) throws NewtException {
        this.context = context;
        this.includes.clear();
        this.extraIncludes = null;
        if (additionalIncludes != null) {
            this.extraIncludes = new HashSet<Include>(additionalIncludes);
        }
        HashSet<Include> childIncludes = new HashSet<Include>();
        try {
            traverse(node, owner, this.expires, childIncludes);
        } catch (DebugStopException e) {
        } finally {
            this.includes.addAll(childIncludes);
        }
    }

    private void upNewtLevel(NewtContext context) {
        context.setRecursionLevel(context.getRecursionLevel() - 1);
    }

    private void downNewtLevel(NewtContext context) throws NewtException {
        checkMaxReqursion(context);
        context.setRecursionLevel(context.getRecursionLevel() + 1);
    }

    private Stopwatch createStopwatch(Newt newt) {
        if (!context.isDebug() && context.getStopwatchFactory() != null) {
            return context.getStopwatchFactory().newStopwatch("newt", newtIdForStopwatch(newt));
        }
        return null;
    }

    private String newtIdForStopwatch(Newt newt) {
        StringBuilder buff = new StringBuilder(newt.toString());
        for (int i = 1; i < context.getRecursionLevel(); i++) {
            buff.insert(0, "-");
        }
        return buff.toString();
    }

    /**
	 * find Newt tags and eval apropos newt
     * @param toParentIncludes передавать пустой Set, после завершения работы должен содержать накопленные Includes
	 * @return false - stop eval newts
	 */
    private boolean traverse(Node node, XmlDoc owner, Expires parentExpires, Set<ru.adv.cache.Include> toParentIncludes) throws NewtException {
        Stopwatch stopwatch = null;
        Expires currentExpires = new Expires();
        switch(node.getNodeType()) {
            case Node.DOCUMENT_NODE:
            case Node.ELEMENT_NODE:
                Newt newt = null;
                try {
                    newt = createNewt(node, owner);
                    if (newt != null) {
                        downNewtLevel(context);
                        try {
                            if (context.isDebug()) {
                                initNewtForDebug(newt, (Element) node);
                            }
                            if (!context.isTransactionAborted()) {
                                logger.debug("Newt.onStartTag(): " + newt.toString());
                                stopwatch = createStopwatch(newt);
                                newt.runELPropcessor();
                                newt.onStartTag();
                            }
                        } catch (DebugStopException e) {
                            insertFilterMap(newt);
                            parentExpires.setExpiresTime(0);
                            return false;
                        } catch (NewtFromCacheException e) {
                            insertFilterMap(newt);
                            parentExpires.setExpiresTime(newt.getExpiresTime());
                            toParentIncludes.addAll(newt.getIncludes());
                            return true;
                        }
                    }
                    HashSet<Include> childrenIncludes = new HashSet<Include>();
                    if (processChildren(node, owner, currentExpires, parentExpires, childrenIncludes)) {
                        toParentIncludes.addAll(childrenIncludes);
                        return false;
                    }
                    toParentIncludes.addAll(childrenIncludes);
                    if (newt != null) {
                        try {
                            newt.addIncludes(childrenIncludes);
                            if (!context.isTransactionAborted() || isTransaction(newt)) {
                                logger.debug("Newt.onEndTag(): " + newt.toString());
                                newt.onEndTag();
                            }
                            currentExpires.setExpiresTime(newt.getExpiresTime());
                        } catch (DebugStopException e) {
                            parentExpires.setExpiresTime(0);
                            return false;
                        } finally {
                            toParentIncludes.addAll(newt.getIncludes());
                            insertFilterMap(newt);
                        }
                    }
                } catch (Throwable t) {
                    parentExpires.setExpiresTime(0);
                    handleException(node, newt, t);
                } finally {
                    if (newt != null) {
                        newt.destroy();
                        upNewtLevel(context);
                        if (stopwatch != null) {
                            stopwatch.finish();
                        }
                    }
                }
                break;
            default:
                return true;
        }
        parentExpires.setExpiresTime(currentExpires.getExpiresTime());
        return true;
    }

    private boolean isTransaction(Newt newt) {
        return newt instanceof Transaction;
    }

    /**
     *
     * @param node
     * @param owner
     * @param currentExpires
     * @param parentExpires
     * @param toParentIncludes
     * @return true если необходимо прервать прохождение newt
     * @throws NewtException
     */
    private boolean processChildren(Node node, XmlDoc owner, Expires currentExpires, Expires parentExpires, Set<Include> toParentIncludes) throws NewtException {
        Node[] children = XmlUtils.childrenArray(node);
        for (int i = 0; i < children.length; i++) {
            if (children[i].getParentNode() != null) {
                HashSet<Include> childIncludes = new HashSet<Include>();
                if (!traverse(children[i], owner, currentExpires, childIncludes)) {
                    parentExpires.setExpiresTime(currentExpires.getExpiresTime());
                    toParentIncludes.addAll(childIncludes);
                    return true;
                }
                toParentIncludes.addAll(childIncludes);
            }
        }
        return false;
    }

    private Newt createNewt(Node node, XmlDoc owner) throws ErrorCodeException {
        Newt result = null;
        if (isNewt(node)) {
            result = newt_factory.createNewt(node.getLocalName(), (Element) node, owner, this.context);
            if (this.extraIncludes != null) {
                result.addIncludes(this.extraIncludes);
            }
        }
        return result;
    }

    private void insertFilterMap(Newt newt) {
        if (newt.getContext().isDebug() && context.showFiltersInDebugMode() && newt.usesDatabase()) {
            FilterMap fm = newt.getFilterMap();
            if (fm != null) {
                Element node = newt.getNode();
                Element filters = fm.toXML(node.getOwnerDocument());
                node.appendChild(filters);
            }
        }
    }

    /**
	 * Обработка неожиданных Exception
	 * Создает приемлемый NewtException для выброса из NewtTraverse
	 */
    private void handleException(Node node, Newt newt, Throwable t) throws NewtException {
        logger.logStackTrace(t);
        this.context.setTransactionAborted(true);
        if (t instanceof NewtException && ((NewtException) t).getCode() == HandlerException.DB_TEMPORARY_UNAVAILABLE) {
            throw (NewtException) t;
        } else if (this.context.isDebug()) {
            insertErrorInDebugMode(node, t);
            throw new DebugStopException(newt, new Exception(t));
        } else if (isFirstLevel() && this.context.isHideNewtExceptions()) {
            logger.logFatalStackTrace(t);
            replaceNewtToErrorElement(node, t);
            return;
        } else if (t instanceof NewtException) {
            throw (NewtException) t;
        } else if (t instanceof ErrorCodeException) {
            throw new NewtException(newt, (ErrorCodeException) t);
        }
        throw new NewtException(newt, new ErrorCodeException(t));
    }

    private void replaceNewtToErrorElement(Node node, Throwable t) {
        if (XmlUtils.isElement(node)) {
            Node parent = node.getParentNode();
            if (parent != null) {
                Element errorElement = node.getOwnerDocument().createElement("error");
                errorElement.setAttribute("description", t.toString());
                parent.insertBefore(errorElement, node);
                Element newtElement = node.getOwnerDocument().createElement("newt");
                errorElement.appendChild(newtElement);
                newtElement.appendChild(node);
                Element stackTraceElement = node.getOwnerDocument().createElement("stackTrace");
                errorElement.appendChild(stackTraceElement);
                StringWriter buff = new StringWriter();
                t.printStackTrace(new PrintWriter(buff));
                stackTraceElement.setTextContent(buff.toString());
            }
        }
    }

    private boolean isFirstLevel() {
        return 1 == this.context.getRecursionLevel();
    }

    /**
	 * Вставляет елемент newt-erorr в node
	 */
    private void insertErrorInDebugMode(Node node, Throwable t) {
        if (node.getNodeType() == Node.DOCUMENT_NODE) {
            node = ((Document) node).getDocumentElement();
        }
        if (node.getNodeType() == Node.ELEMENT_NODE) {
            Element err = node.getOwnerDocument().createElement(ERROR_TAG);
            String message = (t instanceof NewtException) ? ((NewtException) t).getShortMessage() : t.getMessage();
            err.appendChild(node.getOwnerDocument().createTextNode(message));
            node.insertBefore(err, node.getFirstChild());
        }
    }

    /**
	 * test for Newt node
	 */
    protected static boolean isNewt(Node node) {
        return XmlUtils.isElement(node) && NewtProcessor.NEWT_NAMESPACE_URI.equals(node.getNamespaceURI());
    }

    private void initNewtForDebug(Newt newt, Element node) {
        if (node.getAttribute(NewtProcessor.NEWT_ATTR_ID).equals(context.getDebugId())) {
            newt.setDebugStopStep(context.getDebugStep());
        }
    }
}
