package dsb.bar.tks.server.beans.administration;

import java.math.BigDecimal;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import dsb.bar.tks.server.api.administration.local.VATCodeServerLocal;
import dsb.bar.tks.server.api.administration.remote.VATCodeServerRemote;
import dsb.bar.tks.server.api.model.administration.VATCodeDTO;
import dsb.bar.tks.server.converter.administration.VATCodeConverter;
import dsb.bar.tks.server.dao.administration.VATCodeDAO;
import dsb.bar.tks.server.exceptions.api.ObjectAlreadyExistsException;
import dsb.bar.tks.server.persistence.model.administration.VATCode;
import dsb.bar.tks.support.assertions.Assertion;

/**
 * TODO Write comments.
 */
@Stateless
public class VATCodeServerBean implements VATCodeServerLocal, VATCodeServerRemote {

    @EJB
    private VATCodeDAO vatCodeDAO;

    @EJB
    private VATCodeConverter vatCodeConverter;

    public VATCodeServerBean() {
        this(null, null);
    }

    public VATCodeServerBean(VATCodeDAO vatCodeDAO, VATCodeConverter vatCodeConverter) {
        this.vatCodeDAO = vatCodeDAO;
        this.vatCodeConverter = vatCodeConverter;
    }

    public VATCodeDAO getVatCodeDAO() {
        return vatCodeDAO;
    }

    public void setVatCodeDAO(VATCodeDAO vatCodeDAO) {
        this.vatCodeDAO = vatCodeDAO;
    }

    public VATCodeConverter getVatCodeConverter() {
        return vatCodeConverter;
    }

    public void setVatCodeConverter(VATCodeConverter vatCodeConverter) {
        this.vatCodeConverter = vatCodeConverter;
    }

    @Override
    public VATCodeDTO createVATCode(String description, BigDecimal percentage) {
        Assertion.notNull(description, "description");
        Assertion.notNull(percentage, "percentage");
        VATCode existing = this.vatCodeDAO.getByDescription(description);
        if (existing != null) throw new ObjectAlreadyExistsException("VATCode: " + description);
        VATCode vatCode = new VATCode();
        vatCode.setDescription(description);
        vatCode.setPercentage(percentage);
        this.vatCodeDAO.persist(vatCode);
        return this.vatCodeConverter.toDTO(vatCode);
    }

    @Override
    public List<VATCodeDTO> getAll() {
        List<VATCode> results = this.vatCodeDAO.getAll();
        return this.vatCodeConverter.toDTOs(results);
    }
}
