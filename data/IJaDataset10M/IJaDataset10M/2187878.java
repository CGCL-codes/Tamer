package net.simpleframework.content.component.vote;

import net.simpleframework.content.AbstractContentBean;
import net.simpleframework.organization.IJob;
import net.simpleframework.web.page.PageDocument;
import net.simpleframework.web.page.component.IComponentHandle;
import net.simpleframework.web.page.component.IComponentRegistry;
import org.dom4j.Element;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public class VoteBean extends AbstractContentBean {

    private String jobSubmit = IJob.sj_account_normal;

    public VoteBean(final IComponentRegistry componentRegistry, final PageDocument pageDocument, final Element element) {
        super(componentRegistry, pageDocument, element);
    }

    @Override
    protected Class<? extends IComponentHandle> getDefaultHandleClass() {
        return DefaultVoteHandle.class;
    }

    public String getJobSubmit() {
        return jobSubmit;
    }

    public void setJobSubmit(final String jobSubmit) {
        this.jobSubmit = jobSubmit;
    }
}
