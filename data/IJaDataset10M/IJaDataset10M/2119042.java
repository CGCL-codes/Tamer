package org.remus.infomngmnt.link.script;

import java.util.HashMap;
import java.util.Map;
import org.eclipse.remus.InformationUnit;
import org.eclipse.remus.ui.rules.execution.IGroovyEvaluationBinding;
import org.eclipse.remus.ui.rules.extension.IGroovyBinding;

/**
 * @author Tom Seidel <tom.seidel@remus-software.org>
 */
public class LinkGroovyBinding implements IGroovyBinding {

    /**
	 * 
	 */
    public LinkGroovyBinding() {
    }

    public void beforeEvaluation(IGroovyEvaluationBinding binding) {
    }

    public Map afterEvaluation(IGroovyEvaluationBinding binding, InformationUnit createdObject) {
        return new HashMap();
    }
}
