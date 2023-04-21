package br.com.mcampos.ejb.cloudsystem.anoto.pgcpage;

import br.com.mcampos.dto.anoto.PgcPageDTO;
import br.com.mcampos.ejb.cloudsystem.anoto.page.AnotoPage;
import br.com.mcampos.ejb.cloudsystem.anoto.pgc.Pgc;
import br.com.mcampos.ejb.cloudsystem.anoto.pgcpage.attachment.PgcPageAttachment;
import br.com.mcampos.ejb.cloudsystem.anoto.pgcpage.field.PgcField;
import br.com.mcampos.ejb.cloudsystem.system.revisedstatus.RevisionStatusUtil;
import br.com.mcampos.ejb.cloudsystem.system.revisedstatus.entity.RevisionStatus;
import br.com.mcampos.ejb.entity.core.EntityCopyInterface;
import java.io.Serializable;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@NamedQueries({ @NamedQuery(name = "PgcPage.findAll", query = "select o from PgcPage o"), @NamedQuery(name = PgcPage.getAnotherPageStatus, query = "select o from PgcPage o where o.pgc = ?1 and o.revisionStatus <> ?2"), @NamedQuery(name = PgcPage.getAllByForm, query = "select o from PgcPage o where o.anotoPage.pad.form = ?1 order by o.pgcId, o.bookId, o.pageId") })
@Table(name = "pgc_page")
@IdClass(PgcPagePK.class)
public class PgcPage implements Serializable, EntityCopyInterface<PgcPageDTO> {

    public static final String getAnotherPageStatus = "PgcPage.anotherPageStatus";

    public static final String getAllByForm = "PgcPage.getAllByForm";

    @Id
    @Column(name = "pgc_id_in", nullable = false, insertable = false, updatable = false)
    private Integer pgcId;

    @Id
    @Column(name = "ppg_book_id", nullable = false)
    private Integer bookId;

    @Id
    @Column(name = "ppg_page_id", nullable = false)
    private Integer pageId;

    @ManyToOne
    @JoinColumn(name = "pgc_id_in", referencedColumnName = "pgc_id_in")
    private Pgc pgc;

    @OneToMany(mappedBy = "pgcPage")
    private List<PgcPageAttachment> attachments;

    @OneToMany(mappedBy = "pgcPage")
    private List<PgcField> fields;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumns({ @JoinColumn(name = "frm_id_in", referencedColumnName = "frm_id_in", nullable = false), @JoinColumn(name = "pad_id_in", referencedColumnName = "pad_id_in", nullable = false), @JoinColumn(name = "apg_id_ch", referencedColumnName = "apg_id_ch", nullable = false) })
    private AnotoPage anotoPage;

    @ManyToOne(optional = false)
    @JoinColumn(name = "rst_id_in", referencedColumnName = "rst_id_in", nullable = false, columnDefinition = "Integer")
    private RevisionStatus revisionStatus;

    public PgcPage() {
    }

    public PgcPage(Pgc pgc, Integer bookId, Integer ppg_page_id) {
        setPgc(pgc);
        this.bookId = bookId;
        this.pageId = ppg_page_id;
    }

    public Integer getPgcId() {
        return pgcId;
    }

    public void setPgcId(Integer pgc_id_in) {
        this.pgcId = pgc_id_in;
    }

    public Integer getBookId() {
        return bookId;
    }

    public void setBookId(Integer ppg_book_id) {
        this.bookId = ppg_book_id;
    }

    public Integer getPageId() {
        return pageId;
    }

    public void setPageId(Integer ppg_page_id) {
        this.pageId = ppg_page_id;
    }

    public void setPgc(Pgc pgc) {
        this.pgc = pgc;
        if (pgc != null) setPgcId(pgc.getId());
    }

    public Pgc getPgc() {
        return pgc;
    }

    public void setAttachments(List<PgcPageAttachment> attachments) {
        this.attachments = attachments;
    }

    public List<PgcPageAttachment> getAttachments() {
        return attachments;
    }

    public void setFields(List<PgcField> fields) {
        this.fields = fields;
    }

    public List<PgcField> getFields() {
        return fields;
    }

    public PgcPageDTO toDTO() {
        PgcPageDTO dto = new PgcPageDTO();
        dto.setBookId(getBookId());
        dto.setPageId(getPageId());
        dto.setPgc(getPgc().toDTO());
        dto.setAnotoPage(getAnotoPage().toDTO());
        dto.setRevisionStatus(RevisionStatusUtil.copy(getRevisionStatus()));
        return dto;
    }

    public void setAnotoPage(AnotoPage page) {
        this.anotoPage = page;
    }

    public AnotoPage getAnotoPage() {
        return anotoPage;
    }

    public void setRevisionStatus(RevisionStatus revisionStatus) {
        this.revisionStatus = revisionStatus;
    }

    public RevisionStatus getRevisionStatus() {
        return revisionStatus;
    }
}
