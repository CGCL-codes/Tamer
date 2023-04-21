package fr.gedeon.telnetservice.syntaxtree;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Set;
import javax.xml.bind.JAXBException;
import org.apache.log4j.Logger;
import fr.gedeon.telnetservice.ActionListener;
import fr.gedeon.telnetservice.ActionListenerAbstractImpl;
import fr.gedeon.telnetservice.ActionListenerInitializationException;
import fr.gedeon.telnetservice.SessionState;
import fr.gedeon.telnetservice.syntaxtree.ast.BeanOperationLoadException;
import fr.gedeon.telnetservice.syntaxtree.ast.EntityName;
import fr.gedeon.telnetservice.syntaxtree.ast.EntityNamePartType;
import fr.gedeon.telnetservice.syntaxtree.ast.Node;
import fr.gedeon.telnetservice.syntaxtree.ast.NodeNotFoundException;
import fr.gedeon.telnetservice.syntaxtree.ast.SyntaxTree;
import fr.gedeon.telnetservice.syntaxtree.ast.Transition;
import fr.gedeon.telnetservice.syntaxtree.ast.TransitionFailedException;
import fr.gedeon.telnetservice.syntaxtree.ast.impl.bop.BeanOperationHelper;
import fr.gedeon.telnetservice.syntaxtree.data.Notification;
import fr.gedeon.telnetservice.syntaxtree.operations.xml.Bean;
import fr.gedeon.telnetservice.syntaxtree.operations.xml.OperationsBeans;
import fr.gedeon.telnetservice.syntaxtree.operations.xml.OperationsBeansHelper;

public class ActionListenerSyntaxTreeImpl extends ActionListenerAbstractImpl implements ActionListener {

    SyntaxTree baseSyntaxTree;

    Set<BeanInfo> beanOperations;

    private boolean isTokenAcceptCaseInsentive = false;

    private boolean notifyNewLineAsToken = false;

    private char[] customTokens;

    public ActionListenerSyntaxTreeImpl(SyntaxTree baseSyntaxTree) {
        this.beanOperations = new HashSet<BeanInfo>();
        this.baseSyntaxTree = baseSyntaxTree;
    }

    public void setIsTokenAcceptCaseInsentive(boolean isTokenAcceptCaseInsentive) {
        this.isTokenAcceptCaseInsentive = isTokenAcceptCaseInsentive;
    }

    public void setNotifyNewLineAsToken(boolean notifyNewLineAsToken) {
        this.notifyNewLineAsToken = notifyNewLineAsToken;
    }

    public boolean getNotifyNewLineAsToken() {
        return this.notifyNewLineAsToken;
    }

    public void initialize(SessionState sessionState) throws ActionListenerInitializationException {
        Logger logger = Logger.getLogger(this.getClass());
        try {
            loadClasspathBeanOperations(sessionState);
        } catch (IOException e) {
            logger.error("Error loading classpath bean operations: " + e.getMessage(), e);
        }
        SyntaxTreeWalkCursor cursor = SyntaxTreeWalkCursor.retrieveFromSessionState(sessionState);
        cursor.setIsTokenAcceptCaseInsentive(this.isTokenAcceptCaseInsentive);
        cursor.setNotifyNewLineAsToken(this.notifyNewLineAsToken);
        cursor.setCustomTokens(this.customTokens);
        cursor.pushCursorLayer(this.baseSyntaxTree);
    }

    static final String URI_OPERATIONBEANS_XML = "fr/gedeon/telnetservice/syntaxtree/operations/OperationBeans.xml";

    protected void loadClasspathBeanOperations(SessionState sessionState) throws IOException {
        Logger logger = Logger.getLogger(this.getClass());
        logger.debug("Now loading classpath bean operations. . .");
        Enumeration<URL> reslist;
        try {
            reslist = this.getClass().getClassLoader().getResources(URI_OPERATIONBEANS_XML);
        } catch (IOException e) {
            logger.error("Could not get resources '" + URI_OPERATIONBEANS_XML + "', classpath bean operations will not be available", e);
            return;
        }
        logger.debug("Got:");
        while (reslist.hasMoreElements()) {
            URL url = reslist.nextElement();
            try {
                loadOperationsBeans(sessionState, url);
            } catch (JAXBException e) {
                logger.error("Could not parse bean operations descriptor from '" + url + "', bean operations from this descriptor will not be available", e);
                return;
            }
        }
    }

    private void loadOperationsBeans(SessionState sessionState, URL url) throws IOException, JAXBException {
        Logger logger = Logger.getLogger(this.getClass());
        logger.debug("  + " + url);
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new InputStreamReader(url.openStream()));
            OperationsBeans beansDefs = OperationsBeansHelper.load(reader);
            logger.debug("Loaded operations beans descriptor from " + url);
            int i = 0;
            for (Bean beanDesc : beansDefs.getBean()) {
                try {
                    loadBeanOperations(sessionState, beanDesc);
                } catch (BeanOperationLoadException e) {
                    logger.error("Could not load bean (" + i + ") from '" + url + "': " + e.getMessage());
                }
                i++;
            }
        } catch (IOException e) {
            logger.error("Could not read from " + url + ", contents will not be available", e);
            throw (IOException) e.fillInStackTrace();
        } catch (JAXBException e) {
            logger.error("Could not parse " + url + ", contents will not be available", e);
            throw (JAXBException) e.fillInStackTrace();
        } finally {
            if (reader != null) {
                reader.close();
            }
        }
    }

    static class BeanInfo {

        String beanClass;

        Object beanInstance;

        Method create;

        Method start;

        Method stop;

        Method destroy;
    }

    private void loadBeanOperations(SessionState sessionState, Bean beanDesc) throws BeanOperationLoadException {
        Logger logger = Logger.getLogger(this.getClass());
        SyntaxTreeWalkCursor cursor = SyntaxTreeWalkCursor.retrieveFromSessionState(sessionState);
        String className = beanDesc.getBeanClass();
        if (className == null) {
            throw new BeanOperationLoadException("beanClass cannot be null or empty");
        }
        className = className.trim();
        if (className.length() == 0) {
            throw new BeanOperationLoadException("beanClass cannot be null or empty");
        }
        logger.debug("Loading class " + className);
        Class<?> beanClass;
        try {
            beanClass = this.getClass().getClassLoader().loadClass(className);
            BeanInfo info = new BeanInfo();
            info.beanClass = className;
            info.beanInstance = beanClass.newInstance();
            if (beanDesc.getCreate() != null) {
                String createMethodName = beanDesc.getCreate().getMethod();
                try {
                    info.create = beanClass.getMethod(createMethodName, new Class[0]);
                } catch (Throwable t) {
                    logger.error("Unable to get method '" + createMethodName + "' from class '" + className + "' for create event, this bean's operations will not be available: " + t.getMessage(), t);
                }
            }
            if (beanDesc.getStart() != null) {
                String startMethodName = beanDesc.getStart().getMethod();
                try {
                    info.start = beanClass.getMethod(startMethodName, new Class[0]);
                } catch (Throwable t) {
                    logger.error("Unable to get method '" + startMethodName + "' from class '" + className + "' for start event, this bean's operations will not be available: " + t.getMessage(), t);
                }
            }
            if (beanDesc.getStop() != null) {
                String stopMethodName = beanDesc.getStop().getMethod();
                try {
                    info.stop = beanClass.getMethod(stopMethodName, new Class[0]);
                } catch (Throwable t) {
                    logger.error("Unable to get method '" + stopMethodName + "' from class '" + className + "' for stop event, this bean's operations will not be available: " + t.getMessage(), t);
                }
            }
            if (beanDesc.getDestroy() != null) {
                String destroyMethodName = beanDesc.getDestroy().getMethod();
                try {
                    info.destroy = beanClass.getMethod(destroyMethodName, new Class[0]);
                } catch (Throwable t) {
                    logger.error("Unable to get method '" + destroyMethodName + "' from class '" + className + "' for destroy event, this bean's operations will not be available: " + t.getMessage(), t);
                }
            }
            try {
                info.create.invoke(info.beanInstance, new Object[0]);
            } catch (Exception e) {
                logger.error("Could not call create event method '" + info.create + "' on bean " + info.beanClass);
            }
            BeanOperationHelper.registerTarget(cursor.getSyntaxTree(), info.beanInstance);
            this.beanOperations.add(info);
        } catch (Throwable t) {
            logger.error("Unable to load class '" + className + "'," + " this bean's operations will not be available: " + t.getMessage(), t);
        }
    }

    public interface ProcessInputCallback {

        boolean processToken(SessionState sessionState, SyntaxTreeWalkCursor cursor, TransitionToken token, boolean isLastChar);
    }

    class ProcessInputCallbackImpl implements ProcessInputCallback {

        /**
         * <code>true</code> to continue the parse, <code>false</code> for abort
         * 
         * @param cursor
         * @param sessionState
         * @param isLastChar
         */
        public boolean processToken(SessionState sessionState, SyntaxTreeWalkCursor cursor, TransitionToken token, boolean isLastChar) {
            if (token.getTokenBuffer().toString().length() == 0) {
                return true;
            }
            Logger logger = Logger.getLogger(this.getClass());
            logger.debug("Selecting transition candidates for token [" + token + "]");
            AcceptedTransitionToken acceptedTransitionToken = cursor.selectNextTransition(sessionState);
            if (acceptedTransitionToken != null) {
                Transition tr = acceptedTransitionToken.getTransition();
                TransitionToken acceptedToken = acceptedTransitionToken.getTransitionToken();
                logger.debug("    Transition " + tr + " about to be performed");
                try {
                    tr.performTransitionAction(acceptedToken, sessionState);
                    EntityName nextNodeName = tr.getTargetNodeName();
                    logger.debug("    Transition " + tr + " performed, target node name is " + nextNodeName);
                    if (tr.pushTargetAsLayer()) {
                        Node node = cursor.getSyntaxTree().lookupNode(nextNodeName);
                        if (node instanceof SyntaxTree) {
                            logger.debug("Pushing now cursor layer");
                            cursor.pushCursorLayer((SyntaxTree) node);
                            cursor.goToNode(EntityName.ROOT);
                        } else {
                            logger.error("Target node of transition '" + tr.getName() + "' of node '" + cursor.getCurrentNode().getName() + "' must be a syntax tree");
                        }
                    } else {
                        cursor.goToNode(nextNodeName);
                    }
                } catch (TransitionFailedException e) {
                    logger.debug("Transition " + tr.getName() + " failed", e);
                    sessionState.getSession().getConsoleDelegate().writeln(e.getMessage());
                } catch (NodeNotFoundException e) {
                    logger.warn("Node lookup failed, not changing current node", e);
                }
                if (cursor.isInContext) {
                    if (!tr.getType().equals(EntityNamePartType.CONTEXT)) {
                        cursor.flushTransitionTokens();
                        cursor.isInContext = false;
                    }
                } else {
                    if (tr.getType().equals(EntityNamePartType.CONTEXT)) {
                        cursor.flushTransitionTokens();
                        cursor.isInContext = true;
                    }
                }
            } else {
                logger.debug("    No Transition performed!");
                sessionState.getSession().output(sessionState, new Notification("Unknown Operation: " + token.getTokenBuffer()));
            }
            return true;
        }
    }

    public boolean notifyNewLine(SessionState sessionState, String currentBuffer, int relOffset) {
        Logger logger = Logger.getLogger(this.getClass());
        logger.debug("Got line [" + currentBuffer + "]");
        SyntaxTreeWalkCursor cursor = SyntaxTreeWalkCursor.retrieveFromSessionState(sessionState);
        cursor.processInput(sessionState, currentBuffer + "\n", relOffset, new ProcessInputCallbackImpl());
        return true;
    }

    public void setCustomTokens(char... customTokens) {
        this.customTokens = customTokens;
    }
}
