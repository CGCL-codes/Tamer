package org.springframework.transaction.annotation;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import org.springframework.transaction.interceptor.AbstractFallbackTransactionAttributeSource;
import org.springframework.transaction.interceptor.NoRollbackRuleAttribute;
import org.springframework.transaction.interceptor.RollbackRuleAttribute;
import org.springframework.transaction.interceptor.RuleBasedTransactionAttribute;
import org.springframework.transaction.interceptor.TransactionAttribute;

/**
 * <p>Implementation of <code>TransactionAttributeSource</code> for working
 * with transaction metadata in JDK 1.5+ annotation format.
 *
 * <p>This class reads the JDK 1.5+ <code>Transactional</code> annotation
 * and exposes corresponding transaction attributes to Spring's transaction
 * infrastructure.
 *
 * <p>This is a direct alternative to <code>AttributesTransactionAttributeSource</code>,
 * which is able to read in source-level attributes via Commons Attributes.
 *
 * @author Colin Sampaleanu
 * @author Juergen Hoeller
 * @since 1.2
 * @see Transactional
 * @see org.springframework.transaction.interceptor.TransactionInterceptor#setTransactionAttributeSource
 * @see org.springframework.transaction.interceptor.TransactionProxyFactoryBean#setTransactionAttributeSource
 * @see org.springframework.transaction.interceptor.AttributesTransactionAttributeSource
 * @see org.springframework.metadata.commons.CommonsAttributes
 */
public class AnnotationTransactionAttributeSource extends AbstractFallbackTransactionAttributeSource {

    /**
	 * Returns all JDK 1.5+ annotations found for the given method.
	 */
    protected Collection findAllAttributes(Method method) {
        return Arrays.asList(method.getAnnotations());
    }

    /**
	 * Returns all JDK 1.5+ annotations found for the given class.
	 */
    protected Collection findAllAttributes(Class clazz) {
        return Arrays.asList(clazz.getAnnotations());
    }

    /**
	 * Return the transaction attribute, given this set of attributes
	 * attached to a method or class. Overrides method from parent class.
	 * This version actually converts JDK 5.0+ Annotations to the Spring
	 * classes. Returns null if it's not transactional.
	 * @param atts attributes attached to a method or class. May
	 * be <code>null</code>, in which case a null TransactionAttribute will be returned.
	 * @return TransactionAttribute configured transaction attribute,
	 * or <code>null</code> if none was found
	 */
    protected TransactionAttribute findTransactionAttribute(Collection atts) {
        if (atts == null) {
            return null;
        }
        for (Object att : atts) {
            if (att instanceof Transactional) {
                Transactional ruleBasedTx = (Transactional) att;
                RuleBasedTransactionAttribute rbta = new RuleBasedTransactionAttribute();
                rbta.setPropagationBehavior(ruleBasedTx.propagation().value());
                rbta.setIsolationLevel(ruleBasedTx.isolation().value());
                rbta.setReadOnly(ruleBasedTx.readOnly());
                ArrayList<RollbackRuleAttribute> rollBackRules = new ArrayList<RollbackRuleAttribute>();
                Class[] rbf = ruleBasedTx.rollbackFor();
                for (int i = 0; i < rbf.length; ++i) {
                    RollbackRuleAttribute rule = new RollbackRuleAttribute(rbf[i]);
                    rollBackRules.add(rule);
                }
                String[] rbfc = ruleBasedTx.rollbackForClassName();
                for (int i = 0; i < rbfc.length; ++i) {
                    RollbackRuleAttribute rule = new RollbackRuleAttribute(rbfc[i]);
                    rollBackRules.add(rule);
                }
                Class[] nrbf = ruleBasedTx.noRollbackFor();
                for (int i = 0; i < nrbf.length; ++i) {
                    NoRollbackRuleAttribute rule = new NoRollbackRuleAttribute(nrbf[i]);
                    rollBackRules.add(rule);
                }
                String[] nrbfc = ruleBasedTx.noRollbackForClassName();
                for (int i = 0; i < nrbfc.length; ++i) {
                    NoRollbackRuleAttribute rule = new NoRollbackRuleAttribute(nrbfc[i]);
                    rollBackRules.add(rule);
                }
                rbta.getRollbackRules().addAll(rollBackRules);
                return rbta;
            }
        }
        return null;
    }
}
