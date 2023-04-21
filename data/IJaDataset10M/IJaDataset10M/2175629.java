package br.com.mcampos.ejb.cloudsystem.anoto.upload;

import br.com.mcampos.dto.anoto.AnotoPageDTO;
import br.com.mcampos.dto.anoto.PGCDTO;
import br.com.mcampos.dto.anoto.PgcAttachmentDTO;
import br.com.mcampos.dto.anoto.PgcFieldDTO;
import br.com.mcampos.dto.anoto.PgcPageDTO;
import br.com.mcampos.dto.anoto.PgcPenPageDTO;
import br.com.mcampos.dto.anoto.PgcPropertyDTO;
import br.com.mcampos.dto.system.MediaDTO;
import br.com.mcampos.ejb.cloudsystem.anode.utils.AnotoUtils;
import br.com.mcampos.ejb.cloudsystem.anoto.pad.PadSessionLocal;
import br.com.mcampos.ejb.cloudsystem.anoto.page.AnotoPage;
import br.com.mcampos.ejb.cloudsystem.anoto.page.AnotoPagePK;
import br.com.mcampos.ejb.cloudsystem.anoto.page.field.entity.AnotoPageField;
import br.com.mcampos.ejb.cloudsystem.anoto.page.field.entity.AnotoPageFieldPK;
import br.com.mcampos.ejb.cloudsystem.anoto.page.field.session.PageFieldSessionLocal;
import br.com.mcampos.ejb.cloudsystem.anoto.pen.AnodePenSessionLocal;
import br.com.mcampos.ejb.cloudsystem.anoto.pen.AnotoPen;
import br.com.mcampos.ejb.cloudsystem.anoto.penpage.entity.AnotoPenPage;
import br.com.mcampos.ejb.cloudsystem.anoto.pgc.PGCSessionLocal;
import br.com.mcampos.ejb.cloudsystem.anoto.pgc.Pgc;
import br.com.mcampos.ejb.cloudsystem.anoto.pgc.PgcUtil;
import br.com.mcampos.ejb.cloudsystem.anoto.pgc.attachment.entity.PgcAttachment;
import br.com.mcampos.ejb.cloudsystem.anoto.pgc.property.entity.PgcProperty;
import br.com.mcampos.ejb.cloudsystem.anoto.pgcpage.PgcPage;
import br.com.mcampos.ejb.cloudsystem.anoto.pgcpage.PgcPagePK;
import br.com.mcampos.ejb.cloudsystem.anoto.pgcpage.PgcPageSessionLocal;
import br.com.mcampos.ejb.cloudsystem.anoto.pgcpage.PgcPageUtil;
import br.com.mcampos.ejb.cloudsystem.anoto.pgcpage.attachment.PgcPageAttachment;
import br.com.mcampos.ejb.cloudsystem.anoto.pgcpage.attachment.PgcPageAttachmentSessionLocal;
import br.com.mcampos.ejb.cloudsystem.anoto.pgcpage.field.PgcField;
import br.com.mcampos.ejb.cloudsystem.anoto.pgcpage.field.PgcFieldUtil;
import br.com.mcampos.ejb.cloudsystem.anoto.pgcpage.image.PgcProcessedImage;
import br.com.mcampos.ejb.cloudsystem.anoto.pgcpenpage.PgcPenPage;
import br.com.mcampos.ejb.cloudsystem.anoto.pgcstatus.PgcStatus;
import br.com.mcampos.ejb.cloudsystem.media.MediaUtil;
import br.com.mcampos.ejb.cloudsystem.media.Session.MediaSessionLocal;
import br.com.mcampos.ejb.cloudsystem.media.entity.Media;
import br.com.mcampos.ejb.cloudsystem.system.fieldtype.entity.FieldType;
import br.com.mcampos.ejb.core.AbstractSecurity;
import br.com.mcampos.ejb.core.util.DTOFactory;
import br.com.mcampos.exception.ApplicationException;
import br.com.mcampos.sysutils.SysUtils;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Stateless(name = "UploadFacade", mappedName = "CloudSystems-EjbPrj-UploadFacade")
@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
public class UploadFacadeBean extends AbstractSecurity implements UploadFacade {

    public static final Integer messageId = 25;

    @PersistenceContext(unitName = "EjbPrj")
    private transient EntityManager em;

    @EJB
    private MediaSessionLocal mediaSession;

    @EJB
    private PGCSessionLocal pgcSession;

    @EJB
    private AnodePenSessionLocal penSession;

    @EJB
    private PadSessionLocal padSession;

    @EJB
    private PgcPageAttachmentSessionLocal pageAttachSession;

    @EJB
    private PageFieldSessionLocal pageFieldSession;

    @EJB
    private PgcPageSessionLocal pgcPageSession;

    protected EntityManager getEntityManager() {
        return em;
    }

    public Integer getMessageTypeId() {
        return messageId;
    }

    public PGCDTO add(PGCDTO dto, List<String> addresses, ArrayList<MediaDTO> medias, List<PgcPropertyDTO> properties) throws ApplicationException {
        Pgc pgc = PgcUtil.createEntity(dto);
        Media media = mediaSession.add(pgc.getMedia());
        pgc.setMedia(media);
        pgc.setInsertDate(new Date());
        PgcStatus status = new PgcStatus(1);
        pgc.setPgcStatus(status);
        pgc = pgcSession.add(pgc);
        addPgcAttachment(pgc, medias);
        addProperties(pgc, properties);
        verifyBindings(pgc, addresses);
        return pgc.toDTO();
    }

    private void addPgcAttachment(Pgc pgc, ArrayList<MediaDTO> medias) throws ApplicationException {
        Media media;
        if (SysUtils.isEmpty(medias) == false) {
            for (MediaDTO m : medias) {
                media = mediaSession.add(MediaUtil.createEntity(m));
                PgcAttachment attach = new PgcAttachment();
                attach.setMediaId(media.getId());
                attach.setPgcId(pgc.getId());
                getEntityManager().persist(attach);
            }
        }
    }

    private void addProperties(Pgc pgc, List<PgcPropertyDTO> properties) throws ApplicationException {
        int nSequence = 1;
        for (PgcPropertyDTO p : properties) {
            for (String s : p.getValues()) {
                s = s.replaceAll("'", "");
                if (SysUtils.isEmpty(s)) continue;
                PgcProperty property = new PgcProperty();
                property.setPgcId(pgc.getId());
                property.setId(p.getId());
                property.setValue(s);
                property.setSequence(nSequence);
                nSequence++;
                getEntityManager().persist(property);
            }
        }
    }

    private boolean verifyBindings(Pgc pgc, List<String> addresses) throws ApplicationException {
        AnotoPen anotoPen;
        anotoPen = penSession.get(pgc.getPenId());
        if (anotoPen == null) {
            pgcSession.setPgcStatus(pgc, PgcStatus.statusNoPen);
            return false;
        }
        return hasAnotoPages(pgc, addresses, anotoPen);
    }

    private boolean hasAnotoPages(Pgc pgc, List<String> addresses, AnotoPen anotoPen) throws ApplicationException {
        for (String address : addresses) {
            List<AnotoPage> list = padSession.getPages(address);
            if (SysUtils.isEmpty(list) == false) attachPenPage(list, anotoPen, pgc); else {
                if (pgc.getPgcStatus().getId() != PgcStatus.statusNoPenForm) pgcSession.setPgcStatus(pgc, PgcStatus.statusNoPenForm);
            }
        }
        return true;
    }

    private boolean attachPenPage(List<AnotoPage> pages, AnotoPen pen, Pgc pgc) throws ApplicationException {
        AnotoPenPage item;
        for (AnotoPage page : pages) {
            item = padSession.getPenPage(pen, page);
            if (item != null) {
                pgcSession.attach(pgc, item);
            }
        }
        return true;
    }

    public void setPgcStatus(PGCDTO dto, Integer newStatus) throws ApplicationException {
        Pgc pgc = pgcSession.get(dto.getId());
        if (pgc != null) pgcSession.setPgcStatus(pgc, newStatus);
    }

    public List<PgcPenPageDTO> getPgcPenPages(PGCDTO pgc) throws ApplicationException {
        List<PgcPenPage> list = null;
        Pgc entity = pgcSession.get(pgc.getId());
        if (entity != null) {
            list = pgcSession.get(entity);
        }
        return AnotoUtils.toPgcPenPageList(list);
    }

    public void add(PgcPageDTO dto) throws ApplicationException {
        PgcPage entity = PgcPageUtil.createEntity(dto);
        pgcPageSession.add(entity);
    }

    public List<PgcAttachmentDTO> getBarCodes(String barCodeValue, String pageAddress, Integer padId, Integer currentPGC) throws ApplicationException {
        if (SysUtils.isEmpty(barCodeValue)) return Collections.emptyList();
        List<PgcPageAttachment> entities;
        entities = pageAttachSession.getAll(barCodeValue, pageAddress, padId, currentPGC);
        return AnotoUtils.toPgcAttachmentList(entities);
    }

    public byte[] getObject(MediaDTO key) throws ApplicationException {
        return mediaSession.getObject(key.getId());
    }

    public void addProcessedImage(PGCDTO pgc, MediaDTO media, int book, int page) throws ApplicationException {
        Pgc entity = pgcSession.get(pgc.getId());
        if (entity != null) {
            Media mediaEntity = mediaSession.add(MediaUtil.createEntity(media));
            PgcProcessedImage pi = new PgcProcessedImage(new PgcPage(entity, book, page), mediaEntity, book, page);
            pgcSession.add(pi);
        }
    }

    public void addPgcAttachment(PgcAttachmentDTO dto) throws ApplicationException {
        Media media = null;
        if (dto.getMedia() != null) media = mediaSession.add(MediaUtil.createEntity(dto.getMedia()));
        PgcPageAttachment entity = DTOFactory.copy(dto);
        entity.setMedia(media);
        pgcSession.add(entity);
    }

    public void addPgcField(PgcFieldDTO dto) throws ApplicationException {
        PgcField field = PgcFieldUtil.createEntity(dto);
        AnotoPageField pageField = pageFieldSession.get(new AnotoPageFieldPK(dto.getPgcPage().getAnotoPage(), dto.getName()));
        if (pageField != null) {
            field.setType(pageField.getType());
            if (pageField.getType().getId().equals(FieldType.typeBoolean) || pageField.hasIcr() == false) field.setMedia(null);
            field.setSequence(pageField.getSequence());
        } else field.setType(getEntityManager().find(FieldType.class, field.getType().getId()));
        Media media = null;
        if (dto.getMedia() != null) media = mediaSession.add(MediaUtil.createEntity(dto.getMedia()));
        field.setMedia(media);
        field.setType(getEntityManager().find(FieldType.class, field.getType().getId()));
        PgcPageDTO p = dto.getPgcPage();
        PgcPagePK page = new PgcPagePK(p.getPgc().getId(), p.getBookId(), p.getPageId());
        PgcPage pe = getEntityManager().find(PgcPage.class, page);
        field.setPgcPage(pe);
        pgcSession.add(field);
    }

    public List<MediaDTO> getImages(AnotoPageDTO page) throws ApplicationException {
        return AnotoUtils.toMediaList(padSession.getImages(getPageEntity(page)));
    }

    private AnotoPage getPageEntity(AnotoPageDTO page) {
        AnotoPagePK key = new AnotoPagePK(page);
        AnotoPage entity = padSession.getPage(key);
        return entity;
    }
}
