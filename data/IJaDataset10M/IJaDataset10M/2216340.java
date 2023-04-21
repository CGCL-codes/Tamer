package net.sf.brightside.mobilestock.view.web.tapestry.pages;

import net.sf.brightside.mobilestock.metamodel.api.Share;
import net.sf.brightside.mobilestock.service.api.generic.IGetByType;
import net.sf.brightside.mobilestock.view.web.tapestry.core.Bean;
import net.sf.brightside.mobilestock.view.web.tapestry.models.share.ShareEncoder;
import net.sf.brightside.mobilestock.view.web.tapestry.models.share.ShareSelectModel;
import org.apache.tapestry.SelectModel;
import org.apache.tapestry.ValueEncoder;
import org.apache.tapestry.annotations.OnEvent;
import org.apache.tapestry.ioc.annotations.Inject;

public class SelectDemo {

    private Share selectedShare;

    @Inject
    @Bean("net.sf.brightside.mobilestock.service.api.generic.IGetByType")
    private IGetByType getByType;

    private SelectModel shareSelectModel;

    @Inject
    @Bean("net.sf.brightside.mobilestock.view.web.tapestry.models.share.ShareEncoder")
    private ShareEncoder shareEncoder;

    @SuppressWarnings("unchecked")
    public ValueEncoder<Share> getShareEncoder() {
        return (ValueEncoder<Share>) shareEncoder;
    }

    public void setShareSelectModel(SelectModel model) {
        this.shareSelectModel = model;
    }

    public SelectModel getShareSelectModel() {
        if (shareSelectModel == null) {
            shareSelectModel = new ShareSelectModel(getByType.getByType(Share.class));
        }
        return shareSelectModel;
    }

    public Share getSelectedShare() {
        return selectedShare;
    }

    public void setSelectedShare(Share selectedShare) {
        this.selectedShare = selectedShare;
    }

    @OnEvent(value = "submit", component = "chooseShare")
    public Object onFormSubmit() {
        return Start.class;
    }
}
