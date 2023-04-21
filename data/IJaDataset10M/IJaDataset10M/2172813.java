package fr.gouv.defense.terre.esat.formathlon.vue.uc005;

import fr.gouv.defense.terre.esat.formathlon.entity.Utilisateur;
import fr.gouv.defense.terre.esat.formathlon.metier.uc005.UC005Metier;
import fr.gouv.defense.terre.esat.formathlon.vue.utils.JsfUtils;
import fr.gouv.defense.terre.esat.formathlon.vue.utils.PdfUtils;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotNull;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.primefaces.event.SelectEvent;

/**
 * Bean managé par JSF servant à ...
 * @author aRemplacer.
 */
@ManagedBean
@ViewScoped
public class CreerAttestationBean {

    private static final transient Log trace = LogFactory.getLog(CreerAttestationBean.class);

    @EJB
    private UC005Metier metier;

    private List<Utilisateur> lstUtilisateurs;

    private List<Utilisateur> lstChefsDGF;

    @NotNull
    private String loginUti;

    @NotNull
    private String loginChefDGF;

    private Utilisateur selectedUtilisateurFromLdap;

    private Utilisateur selectedChefDGF;

    /**
     * Constructeur par défaut.
     */
    public CreerAttestationBean() {
    }

    @PostConstruct
    public void init() {
        lstUtilisateurs = metier.listerTousLesUtilisateursMemeCeuxMutes();
        lstChefsDGF = metier.listerTousLesChefsDeLaDGF();
        loginUti = null;
        loginChefDGF = null;
        selectedUtilisateurFromLdap = null;
        selectedChefDGF = null;
    }

    public List<String> complete(String query) {
        return metier.lstLogins(query);
    }

    private static ByteArrayOutputStream getOUtputStreamOFormateur(Attestation attestation) throws JAXBException, IOException {
        JAXBContext context = JAXBContext.newInstance(Attestation.class);
        Marshaller marshaller = context.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        marshaller.marshal(attestation, outputStream);
        outputStream.flush();
        outputStream.close();
        return outputStream;
    }

    public void creerAttestation() {
        ByteArrayOutputStream baos = null;
        HttpServletResponse response = null;
        if (selectedUtilisateurFromLdap != null && selectedUtilisateurFromLdap.getLstInscriptionEffectuee().isEmpty()) {
            JsfUtils.afficherMessage(FacesMessage.SEVERITY_INFO, "uc_005_gen_pdf_impossible_pas_de_formation");
        } else {
            try {
                Attestation attestation = new Attestation(selectedChefDGF, selectedUtilisateurFromLdap, selectedUtilisateurFromLdap.getLstInscriptionEffectuee());
                baos = PdfUtils.getPdf(getOUtputStreamOFormateur(attestation), "/resources/xsl/attestation.xsl");
                response = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();
                OutputStream outputStream = response.getOutputStream();
                outputStream.write(baos.toByteArray());
                response.setHeader("Expires", "0");
                response.setHeader("Cache-Control", "must-revalidate, post-check=0, pre-check=0");
                response.setHeader("Pragma", "public");
                response.setContentType("application/pdf");
                response.addHeader("Content-disposition", "attachment;filename=\"attestation.pdf\"");
                response.getOutputStream().write(baos.toByteArray());
                response.setStatus(200);
                response.getOutputStream().flush();
                FacesContext.getCurrentInstance().responseComplete();
            } catch (Exception e) {
                JsfUtils.afficherException(e);
            } finally {
                try {
                    if (response != null && response.getOutputStream() != null) {
                        response.getOutputStream().flush();
                        response.getOutputStream().close();
                    }
                    if (baos != null) {
                        baos.flush();
                        baos.close();
                    }
                } catch (IOException ex) {
                    trace.error(ex);
                }
            }
        }
    }

    public void selectionChefDgf() {
        try {
            if (loginChefDGF != null) {
                selectedChefDGF = metier.utilisateurPeuple(loginChefDGF);
            } else {
                init();
            }
        } catch (Exception e) {
            init();
            JsfUtils.afficherException(e);
        }
    }

    public void selectionUtilisateur(SelectEvent event) {
        try {
            if (event.getObject().toString() != null) {
                loginUti = event.getObject().toString();
                selectedUtilisateurFromLdap = metier.utilisateurPeuple(event.getObject().toString());
            } else {
                init();
            }
        } catch (Exception e) {
            init();
            JsfUtils.afficherException(e);
        }
    }

    public String getLoginChefDGF() {
        return loginChefDGF;
    }

    public void setLoginChefDGF(String loginChefDGF) {
        this.loginChefDGF = loginChefDGF;
    }

    public String getLoginUti() {
        return loginUti;
    }

    public void setLoginUti(String loginUti) {
        this.loginUti = loginUti;
    }

    public List<Utilisateur> getLstChefsDGF() {
        return lstChefsDGF;
    }

    public void setLstChefsDGF(List<Utilisateur> lstChefsDGF) {
        this.lstChefsDGF = lstChefsDGF;
    }

    public List<Utilisateur> getLstUtilisateurs() {
        return lstUtilisateurs;
    }

    public void setLstUtilisateurs(List<Utilisateur> lstUtilisateurs) {
        this.lstUtilisateurs = lstUtilisateurs;
    }

    public Utilisateur getSelectedUtilisateurFromLdap() {
        return selectedUtilisateurFromLdap;
    }

    public void setSelectedUtilisateurFromLdap(Utilisateur selectedUtilisateurFromLdap) {
        this.selectedUtilisateurFromLdap = selectedUtilisateurFromLdap;
    }
}
