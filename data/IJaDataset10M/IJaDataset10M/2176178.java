package com.continuent.tungsten.manager.client.api;

import java.util.List;
import java.util.Stack;
import javax.management.remote.JMXConnector;
import org.antlr.runtime.CommonToken;
import org.antlr.runtime.Token;
import org.apache.log4j.Logger;
import com.continuent.tungsten.cluster.manager.ClusterManagementHelper;
import com.continuent.tungsten.commons.config.TungstenProperties;
import com.continuent.tungsten.commons.jmx.JmxManager;
import com.continuent.tungsten.commons.utils.CLUtils;
import com.continuent.tungsten.manager.client.ClientException;
import com.continuent.tungsten.manager.client.ClusterServices;
import com.continuent.tungsten.manager.core.ServiceManager;
import com.continuent.tungsten.manager.core.ServiceManagerMBean;
import com.continuent.tungsten.manager.exception.ClusterManagerException;
import com.continuent.tungsten.manager.resource.proxy.DirectoryProxy;
import com.continuent.tungsten.manager.service.ClusterConfigurationService;

/**
 * This class defines a ClusterCtrl that implements a simple utility control
 * many cluster-wide operations.
 * 
 * @author <a href="mailto:edward.archibald@continuent.com">Ed Archibald</a>
 * @version 1.0
 */
public class TMLInterpreter {

    private static Logger logger = Logger.getLogger(TMLInterpreter.class);

    TungstenProperties props = null;

    ClusterServices clusterServices = null;

    Stack<Token> stack = new Stack<Token>();

    Token startToken = null;

    public TMLInterpreter(ClusterServices clusterServices) throws ClusterManagerException {
        this.clusterServices = clusterServices;
    }

    public TMLInterpreter() throws ClusterManagerException {
        this(null);
    }

    /**
     * This function interprets the tree passed in by using a simple stack
     * machine. It translates the tokens labeled as OP_* into executable
     * operations. Other tokens serve as operands for executable operations.
     * 
     * @param tree
     */
    @SuppressWarnings("unchecked")
    public void interpret(TMLTree tree) throws Exception {
        List<TMLTree> children = null;
        startToken = tree.getToken();
        children = tree.getChildren();
        if (children != null) {
            for (TMLTree child : children) {
                interpret(child);
            }
        }
        Token token = tree.getToken();
        switch(token.getType()) {
            case TMLEval.OP_CREATE_DATASOURCE:
                createDataSource();
                break;
            case TMLEval.OP_CREATE_GLOBAL_DATASERVICE:
                createGlobalDataService(token);
                break;
            case TMLEval.OP_CREATE_LOCAL_DATASERVICE:
                createLocalDataService(token);
                break;
            case TMLEval.OP_CREATE_SHARD:
                createShard(token);
                break;
            case TMLEval.OP_CONCAT:
                push(concat(token));
            case TMLEval.OP_CWD:
                push(cwd(token));
                break;
            case TMLEval.OP_DISABLE:
                disable(token);
                break;
            case TMLEval.OP_DOTNAME:
                push(dotName(token));
                break;
            case TMLEval.OP_DROP_DATASOURCE:
                dropDataSource(token);
                break;
            case TMLEval.OP_ENABLE:
                enable(token);
                break;
            case TMLEval.OP_DROP_LOCAL_DATASERVICE:
                dropLocalDataService(token);
                break;
            case TMLEval.OP_FQN_SITE:
                push(FQNSite(token));
                break;
            case TMLEval.OP_FQN_CLUSTER:
                push(FQNCluster(token));
                break;
            case TMLEval.OP_FQN_DATASERVICE:
                push(FQNDataService(token));
                break;
            case TMLEval.OP_FQN_SHARD:
                push(FQNShard(token));
                break;
            case TMLEval.OP_FQN_FACET:
                push(FQNFacet(token));
                break;
            case TMLEval.OP_SET_PROPERTY:
                setProperty(token);
                break;
            default:
                push(token);
        }
    }

    /**
     * OP_CREATE_DATASOURCE
     */
    private void createDataSource() throws Exception {
        System.out.println(String.format("OP_CREATE_DATASOURCE\n%s\n", props.toNameValuePairs()));
    }

    /**
     * OP_CREATE_LOCAL_DATASERVICE
     */
    private void createGlobalDataService(Token token) {
        System.out.println(CLUtils.formatPropertiesOld(token.getText(), props, "", false));
    }

    /**
     * OP_CREATE_LOCAL_DATASERVICE
     */
    private void createLocalDataService(Token token) {
        System.out.println(CLUtils.formatPropertiesOld(token.getText(), props, "", false));
    }

    /**
     * OP_CREATE_SHARD
     */
    private void createShard(Token token) {
        System.out.println(CLUtils.formatPropertiesOld(token.getText(), props, "", false));
    }

    /**
     * OP_CONCAT Concatenates the values in the children and returns the
     * concatenated value.
     */
    private Token concat(Token token) {
        String right = pop().getText();
        String left = pop().getText();
        token.setText(left + right);
        token.setType(TMLEval.ID);
        return token;
    }

    /**
     * OP_CWD Returns the current working directory.
     * 
     * @return
     */
    private Token cwd(Token token) {
        String cwd = "mv:c1";
        token.setText(cwd);
        token.setType(TMLEval.ID);
        System.out.println(String.format("OP_CWD: %s", cwd));
        return token;
    }

    /**
     * OP_DISABLE
     */
    private void disable(Token token) {
        Token fqn = pop();
        System.out.println(String.format("OP_DISABLE: %s", fqn.getText()));
    }

    /**
     * OP_DROP_DATASOURCE
     * 
     * @param dsName
     */
    private Token dropDataSource(Token token) {
        Token dsName = pop();
        System.out.println(String.format("OP_DROP_DATASOURCE %s", dsName.getText()));
        return token;
    }

    /**
     * OP_DROP_LOCAL_DATASERVICE
     * 
     * @param token
     */
    private void dropLocalDataService(Token token) {
        Token serviceName = pop();
        System.out.println(String.format("OP_DROP_DATASERVICE %s", serviceName.getText()));
    }

    /**
     * OP_DOTNAME
     * 
     * @param left
     * @param right
     * @return
     */
    private Token dotName(Token token) {
        String right = pop().getText();
        String left = pop().getText();
        token.setText(left + "." + right);
        token.setType(TMLEval.ID);
        return token;
    }

    /**
     * OP_ENABLE
     */
    private void enable(Token token) {
        Token fqn = pop();
        System.out.println(String.format("OP_ENABLE: %s", fqn.getText()));
    }

    /**
     * OP_FQN_SITE
     */
    private Token FQNSite(Token token) {
        Token name = pop();
        String fqn = name.getText();
        token.setText(fqn);
        token.setType(TMLEval.ID);
        System.out.println(String.format("OP_FQN_SITE: %s", fqn));
        return token;
    }

    /**
     * OP_FQN_CLUSTER
     */
    private Token FQNCluster(Token token) {
        Token cluster = pop();
        Token site = pop();
        String fqn = String.format("%s:%s", site.getText(), cluster.getText());
        token.setText(fqn);
        token.setType(TMLEval.ID);
        System.out.println(String.format("OP_FQN_CLUSTER: %s", fqn));
        return token;
    }

    /**
     * OP_FQN_DATASERVICE
     */
    private Token FQNDataService(Token token) {
        Token dataService = pop();
        Token cluster = pop();
        String fqn = String.format("%s//%s", cluster.getText(), dataService.getText());
        token.setText(fqn);
        token.setType(TMLEval.ID);
        System.out.println(String.format("OP_FQN_DATASERVICE %s", fqn));
        return token;
    }

    /**
     * OP_FQN_SHARD
     */
    private Token FQNShard(Token token) {
        Token shardName = pop();
        Token dataService = pop();
        String fqn = String.format("%s/%s", dataService.getText(), shardName.getText());
        token.setText(fqn);
        token.setType(TMLEval.ID);
        System.out.println(String.format("OP_FQN_SHARD: %s", fqn));
        return token;
    }

    /**
     * OP_FQN_FACET
     */
    private Token FQNFacet(Token token) {
        Token facetName = pop();
        Token shardName = pop();
        String fqn = String.format("%s@%s", shardName.getText(), facetName.getText());
        token.setText(fqn);
        token.setType(TMLEval.ID);
        System.out.println(String.format("OP_FQN_FACET: %s", fqn));
        return token;
    }

    /**
     * OP_SET_PROPERTY
     * 
     * @param key
     * @param value
     */
    private Token setProperty(Token token) {
        String value = pop().getText();
        String key = pop().getText().toLowerCase();
        System.out.println(String.format("OP_SET_PROPERTY: %s=%s", key, value));
        if (props == null) {
            props = new TungstenProperties();
        }
        String currentValue = props.getString(key);
        if (currentValue != null) {
            value = currentValue + "," + value;
        }
        props.setString(key, value);
        return token;
    }

    private Token pop() {
        Token token = stack.pop();
        System.out.println("POP " + token);
        return token;
    }

    private void push(Token token) {
        System.out.println("PUSH " + token);
        stack.push(token);
    }

    public Token cwd() {
        return new CommonToken(TMLEval.ID, "mv:c1");
    }
}
