package org.icenigrid.gridsam.core.plugin.manager.auth.simple;

import javax.security.auth.Subject;
import org.icenigrid.schema.jsdl.y2005.m11.JobDefinitionDocument;

/**
 * @author wwhl
 *
 */
public class TrueRule implements AuthorisationRule {

    public boolean evaluate(Subject pSubject, JobDefinitionDocument pJobDesc) {
        return true;
    }
}
