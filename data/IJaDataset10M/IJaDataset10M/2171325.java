package org.frameworker.action.master;

import org.frameworker.action.reader.AbstractReaderAction;
import org.frameworker.form.EntityForm;
import org.frameworker.manager.EntityManager;

/**
 * @author Marcel Mauricio (marcel.wiskecidu@gmail.com)
 * @since 19/06/2007
 * 
 */
public class AbstractMasterAction<F extends EntityForm, M extends EntityManager> extends AbstractReaderAction<F, M> implements MasterAction<F, M> {

    /**
	 * 
	 * @see @see br.com.frameworker.action.master.MasterAction#save(br.com.frameworker.form.EntityForm)
	 */
    public void save(F f) {
    }

    /**
	 * 
	 * @see @see br.com.frameworker.action.master.MasterAction#update(br.com.frameworker.form.EntityForm)
	 */
    public void update(F f) {
    }

    /**
	 * 
	 * @see @see br.com.frameworker.action.master.MasterAction#delete(br.com.frameworker.form.EntityForm)
	 */
    public void delete(F f) {
    }
}
