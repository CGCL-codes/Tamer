package org.nexopenframework.workflow.providers.jbpm31;

import java.io.Serializable;
import java.util.Iterator;
import java.util.Map;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jbpm.JbpmContext;
import org.jbpm.context.exe.ContextInstance;
import org.jbpm.graph.exe.ProcessInstance;
import org.nexopenframework.workflow.ConcurrentProcessException;
import org.nexopenframework.workflow.EntityClassHolder;
import org.nexopenframework.workflow.context.ProcessContext;
import org.nexopenframework.workflow.context.ProcessContextHolder;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationAdapter;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import org.springframework.util.Assert;

/**
 * <p>NexTReT Open Framework</p>
 * 
 * <p>Adapter for dealing with transaction callbacks in a Spring transaction architecture.
 *    It has an specific order in the {@link TransactionSynchronization} chain</p>
 * 
 * @author <a href="mailto:fme@nextret.net">Francesc Xavier Magdaleno</a>
 * @see #getOrder()
 * @see JbpmUtils#JBPM_SYNCHRONIZATION_ORDER
 * @version 1.0
 * @since 1.0
 */
public class JbpmSpringSynchronization extends TransactionSynchronizationAdapter implements TransactionSynchronization {

    /**holder of the jBPM configuration and context*/
    private final JbpmContextHolder holder;

    /**
	 * <p>Creates this object thru a {@link JbpmContextHolder} object which holds the current
	 *   jBPM context</p>
	 * 
	 * @param holder
	 */
    public JbpmSpringSynchronization(JbpmContextHolder holder) {
        Assert.notNull(holder, "Must provide a not null JbpmConfiguration");
        this.holder = holder;
    }

    /**
	 * <p>Returns the order of this {@link TransactionSynchronization} implementation.
	 *    This number MUST be lower than the order of the implementation of Hibernate
	 *    in order to guarantee that will be executed before this (Hibernate implementation)</p>
	 * 
	 * @see org.springframework.transaction.support.TransactionSynchronizationAdapter#getOrder()
	 * @see JbpmUtils#JBPM_SYNCHRONIZATION_ORDER
	 */
    public int getOrder() {
        return JbpmUtils.JBPM_SYNCHRONIZATION_ORDER;
    }

    public void suspend() {
        TransactionSynchronizationManager.unbindResource(holder.getJbpmConfiguration());
    }

    public void resume() {
        TransactionSynchronizationManager.bindResource(holder.getJbpmConfiguration(), holder);
    }

    /**
	 * <p>Here we save all attributes in the {@link ProcessContext} to our {@link ProcessInstance}</p>
	 * 
	 * @see org.springframework.transaction.support.TransactionSynchronizationAdapter#beforeCommit(boolean)
	 */
    public void beforeCommit(boolean readOnly) {
        if (!readOnly) {
            ProcessContext ctx = ProcessContextHolder.currentProcessContext();
            if (ctx.getProcessId() != null) {
                JbpmContext jbpmContext = this.holder.getJbpmContext();
                Long id = (Long) ctx.getProcessId();
                ProcessInstance processInstance = jbpmContext.getProcessInstance(id.longValue());
                if (processInstance != null) {
                    if (ctx.getOriginal() != null) {
                        ProcessInstance _processInstance = (ProcessInstance) ctx.getOriginal();
                        if (_processInstance.getId() != processInstance.getId()) {
                            Log logger = LogFactory.getLog(this.getClass());
                            logger.error("Different process instances found");
                            logger.error("Found in jBPM context [" + processInstance.getId() + "] and in current [" + _processInstance.getId() + "]");
                            throw new ConcurrentProcessException("Concurrent process instances found. " + "jBPM Context [" + processInstance.getId() + "] current [" + _processInstance.getId() + "]");
                        }
                        ctx.setOriginal(processInstance);
                    } else {
                        ctx.setOriginal(processInstance);
                    }
                    ContextInstance ci = processInstance.getContextInstance();
                    Map attributes = ctx.getAttributes();
                    Iterator it_attributes = attributes.keySet().iterator();
                    while (it_attributes.hasNext()) {
                        String name = (String) it_attributes.next();
                        Serializable value = ctx.getAttribute(name);
                        ci.setVariable(name, value);
                    }
                    Iterator it_removed = ctx.getAttributesRemoved().iterator();
                    while (it_removed.hasNext()) {
                        String name = (String) it_removed.next();
                        ci.deleteVariable(name);
                    }
                    attributes.clear();
                    jbpmContext.save(processInstance);
                }
            }
        }
    }

    /**
	 * <p>clean up resources</p>
	 * 
	 * @see ProcessContextHolder#resetProcessContext()
	 * @see org.springframework.transaction.support.TransactionSynchronizationAdapter#afterCompletion(int)
	 */
    public void afterCompletion(int status) {
        if (this.holder != null) {
            JbpmContext ctx = this.holder.getJbpmContext();
            JbpmUtils.closeAlwaysJbpmContext(ctx);
            this.holder.setSynchronizedWithTransaction(false);
            TransactionSynchronizationManager.unbindResource(holder.getJbpmConfiguration());
        }
        {
            ProcessContextHolder.resetProcessContext();
            EntityClassHolder.setCurrent(null);
        }
    }
}
