package de.bwb.ekp.entities;

import javax.faces.application.FacesMessage;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.faces.FacesMessages;
import de.bwb.ekp.interceptors.MeasureCalls;

@MeasureCalls
@Name("verfahrensartHome")
public class VerfahrensartHome extends EKPEntityHome<Verfahrensart> {

    private static final long serialVersionUID = 1L;

    @In(required = false)
    private VerfahrensartList verfahrensartList;

    @In
    private FacesMessages facesMessages;

    public VerfahrensartHome() {
        super(LogObjekt.Verfahrensart, true);
    }

    public void setVerfahrensartId(final Integer id) {
        this.setId(id);
    }

    public Integer getVerfahrensartId() {
        return (Integer) this.getId();
    }

    @Override
    protected Verfahrensart createInstance() {
        final Verfahrensart verfahrensart = new Verfahrensart();
        return verfahrensart;
    }

    public void wire() {
    }

    public boolean isWired() {
        return true;
    }

    public Verfahrensart getDefinedInstance() {
        return this.isIdDefined() ? this.getInstance() : null;
    }

    @Override
    protected void setGeloescht() {
        this.getInstance().setGeloescht();
        this.verfahrensartList.refresh();
    }

    @Override
    protected String getBezeichnung() {
        return this.getInstance().getVaBezeichnung();
    }

    @Override
    public String update() {
        this.facesMessages.add(FacesMessage.SEVERITY_WARN, "ACHTUNG! Ge�nderte Bezeichnungen von Verfahrensarten m�ssen im Filenet nachgepflegt werden.");
        return super.update();
    }

    @Override
    public String persist() {
        this.facesMessages.add(FacesMessage.SEVERITY_WARN, "ACHTUNG! Neue Verfahrensarten m�ssen im Filenet nachgepflegt werden.");
        return super.persist();
    }
}
