package br.com.mcampos.ejb.cloudsystem.anoto.page;

import br.com.mcampos.ejb.cloudsystem.media.entity.Media;
import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@NamedQueries({ @NamedQuery(name = "BackgroundImage.findAll", query = "select o from BackgroundImage o"), @NamedQuery(name = BackgroundImage.pageImagesNamesQuery, query = "select o from BackgroundImage o where o.page = ?1") })
@Table(name = "background_image")
@IdClass(BackgroundImagePK.class)
public class BackgroundImage implements Serializable {

    public static final String pageImagesNamesQuery = "BackgroundImage.findPageImages";

    @Id
    @Column(name = "apg_id_ch", nullable = false, insertable = false, updatable = false)
    private String pageAddress;

    @Column(name = "bin_insert_dt", nullable = false)
    @Temporal(value = TemporalType.DATE)
    private Date insertDate;

    @Id
    @Column(name = "frm_id_in", nullable = false, insertable = false, updatable = false)
    private Integer formId;

    @Id
    @Column(name = "med_id_in", nullable = false, insertable = false, updatable = false)
    private Integer mediaId;

    @Id
    @Column(name = "pad_id_in", nullable = false, insertable = false, updatable = false)
    private Integer padId;

    @ManyToOne
    @JoinColumns({ @JoinColumn(name = "frm_id_in", referencedColumnName = "frm_id_in"), @JoinColumn(name = "pad_id_in", referencedColumnName = "pad_id_in"), @JoinColumn(name = "apg_id_ch", referencedColumnName = "apg_id_ch") })
    private AnotoPage page;

    @ManyToOne
    @JoinColumn(name = "med_id_in", referencedColumnName = "med_id_in")
    private Media media;

    @Column(name = "bin_description_ch", nullable = false)
    private String description;

    public BackgroundImage() {
    }

    public BackgroundImage(AnotoPage page, Media image) {
        setMedia(image);
        setPage(page);
    }

    protected void init(String apg_id_ch, Integer frm_id_in, Integer med_id_in, Integer pad_id_in) {
        setPageAddress(apg_id_ch);
        setPadId(pad_id_in);
        setFormId(frm_id_in);
        setMediaId(med_id_in);
    }

    public BackgroundImage(String apg_id_ch, Integer frm_id_in, Integer med_id_in, Integer pad_id_in) {
        init(apg_id_ch, frm_id_in, med_id_in, pad_id_in);
    }

    public String getPageAddress() {
        return pageAddress;
    }

    public void setPageAddress(String apg_id_ch) {
        this.pageAddress = apg_id_ch;
    }

    public Integer getFormId() {
        return formId;
    }

    public void setFormId(Integer frm_id_in) {
        this.formId = frm_id_in;
    }

    public Integer getMediaId() {
        return mediaId;
    }

    public void setMediaId(Integer med_id_in) {
        this.mediaId = med_id_in;
    }

    public Integer getPadId() {
        return padId;
    }

    public void setPadId(Integer pad_id_in) {
        this.padId = pad_id_in;
    }

    public void setPage(AnotoPage page) {
        this.page = page;
        if (page != null) {
            setPadId(page.getPadId());
            setFormId(page.getFormId());
            setPageAddress(page.getPageAddress());
        }
    }

    public AnotoPage getPage() {
        return page;
    }

    public void setMedia(Media media) {
        this.media = media;
        if (media != null) setMediaId(media.getId());
    }

    public Media getMedia() {
        return media;
    }

    public void setInsertDate(Date insertDate) {
        this.insertDate = insertDate;
    }

    public Date getInsertDate() {
        return insertDate;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
