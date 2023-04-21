package com.volantis.mcs.runtime.pipeline;

import com.volantis.mcs.localization.LocalizationFactory;
import com.volantis.synergetics.log.LogDispatcher;
import com.volantis.xml.namespace.ExpandedName;
import com.volantis.xml.pipeline.sax.XMLProcess;
import com.volantis.xml.pipeline.sax.dynamic.DynamicProcess;
import com.volantis.xml.pipeline.sax.dynamic.rules.AbstractAddProcessRule;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

/**
 * An {@link AbstractAddProcessRule} that uses reflection to create the rule
 * that is added to the pipeline. Note - it would be nice to use Introspection
 * to call map form the attributes passed in via the {@link #createProcess}
 * methods to the appropriate "set" method on the process.
 */
public class IntrospectingAddProcessRule extends AbstractAddProcessRule {

    /**
     * Used for logging
     */
    private static final LogDispatcher logger = LocalizationFactory.createLogger(IntrospectingAddProcessRule.class);

    /**
     * Fully qualified class name of the {@link XMLProcess} that this rule
     * will create, initialize and then add to the pipeline
     */
    private String processClassName;

    /**
     * Initializes a <code>IntrospectingAddProcessRule</code> instance
     * @param processClassName the fully qualified class name of the
     * {@link XMLProcess} that this rule will create, initialize and then add
     * to the pipeline
     */
    public IntrospectingAddProcessRule(String processClassName) {
        if (processClassName == null) {
            throw new IllegalStateException("processClassName cannot be null");
        }
        this.processClassName = processClassName;
    }

    protected XMLProcess createProcess(DynamicProcess dynamicProcess, ExpandedName elementName, Attributes attributes) throws SAXException {
        XMLProcess process = null;
        try {
            Class processClass = Class.forName(processClassName);
            process = (XMLProcess) processClass.newInstance();
        } catch (ClassNotFoundException e) {
            logger.error("introspecting-rule-class-not-found", processClassName, e);
        } catch (InstantiationException e) {
            logger.error("introspecting-rule-cannot-instantiate-class", processClassName, e);
        } catch (IllegalAccessException e) {
            logger.error("introspecting-rule-illegal-access", processClassName, e);
        }
        return process;
    }
}
