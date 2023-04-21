package net.simpleframework.my.file.component.fileselect;

import net.simpleframework.organization.IJob;
import net.simpleframework.util.LocaleI18n;
import net.simpleframework.web.page.PageDocument;
import net.simpleframework.web.page.component.IComponentHandle;
import net.simpleframework.web.page.component.IComponentRegistry;
import net.simpleframework.web.page.component.ui.dictionary.DictionaryBean;
import org.dom4j.Element;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public class FileSelectBean extends DictionaryBean {

    private String jobDownload = IJob.sj_account_normal;

    public FileSelectBean(final IComponentRegistry componentRegistry, final PageDocument pageDocument, final Element element) {
        super(componentRegistry, pageDocument, element);
        setTitle(LocaleI18n.getMessage("My.folder_file_select.0"));
        setShowHelpTooltip(false);
        setWidth(720);
        setHeight(540);
    }

    @Override
    protected Class<? extends IComponentHandle> getDefaultHandleClass() {
        return DefaultFileSelectHandle.class;
    }

    public String getJobDownload() {
        return jobDownload;
    }

    public void setJobDownload(final String jobDownload) {
        this.jobDownload = jobDownload;
    }
}
