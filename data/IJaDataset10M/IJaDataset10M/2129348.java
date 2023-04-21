package net.sf.brightside.eterminals.pages.okrug;

import net.sf.brightside.eterminals.core.spring.ApplicationContextProviderSingleton;
import net.sf.brightside.eterminals.core.tapestry.SpringBean;
import net.sf.brightside.eterminals.metamodel.Okrug;
import net.sf.brightside.eterminals.metamodel.Regija;
import net.sf.brightside.eterminals.pages.message.MessagePage;
import net.sf.brightside.eterminals.service.Get;
import net.sf.brightside.eterminals.service.GetById;
import net.sf.brightside.eterminals.service.Save;
import net.sf.brightside.eterminals.util.RegijaEncoder;
import net.sf.brightside.eterminals.util.RegijaSelectModel;
import org.apache.tapestry5.SelectModel;
import org.apache.tapestry5.ValueEncoder;
import org.apache.tapestry5.annotations.InjectPage;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.springframework.context.ApplicationContext;

public class Edit {

    @InjectPage
    private MessagePage messagePage;

    @Persist
    private String message = "initial value";

    @Persist
    private Okrug okrug;

    @Inject
    @SpringBean("net.sf.brightside.eterminals.service.Get")
    private Get<Regija> getRegija;

    @Inject
    @SpringBean("net.sf.brightside.eterminals.service.GetById")
    private GetById<Okrug> getByIdCommand;

    @Inject
    @SpringBean("net.sf.brightside.eterminals.service.Save")
    private Save<Okrug> saveCommand;

    @Inject
    @SpringBean("net.sf.brightside.eterminals.util.RegijaEncoder")
    private RegijaEncoder regijaEncoder;

    @SuppressWarnings("unchecked")
    public ValueEncoder getRegijaEncoder() {
        return (ValueEncoder) regijaEncoder;
    }

    public SelectModel getRegijaModel() {
        getRegija.setType(Regija.class);
        return new RegijaSelectModel(getRegija.execute());
    }

    public void setRegijaEncoder(RegijaEncoder regijaEncoder) {
        this.regijaEncoder = regijaEncoder;
    }

    private ApplicationContext applicationContext() {
        return new ApplicationContextProviderSingleton().getContext();
    }

    public Okrug createOkrug() {
        return (Okrug) applicationContext().getBean(Okrug.class.getName());
    }

    public Okrug getOkrug() {
        if (okrug == null) okrug = createOkrug();
        return okrug;
    }

    public Object onSubmit() {
        saveCommand.setObject(okrug);
        saveCommand.execute();
        messagePage.setMessage("You succesfully edited Okrug: " + okrug.getNaziv());
        return messagePage;
    }

    void onActivate(long id) {
        if (id != 0) {
            getByIdCommand.setType(Okrug.class);
            getByIdCommand.setId(id);
            okrug = getByIdCommand.execute();
            message = "Edit okrug: " + okrug.getNaziv();
        }
        message = "Add new okrug";
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public MessagePage getMessagePage() {
        return messagePage;
    }

    public void setMessagePage(MessagePage messagePage) {
        this.messagePage = messagePage;
    }

    public void setOkrug(Okrug okrug) {
        this.okrug = okrug;
    }
}
