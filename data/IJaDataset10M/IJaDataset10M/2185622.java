package dsb.demo.javaee5.dto.converter;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import dsb.demo.javaee5.dto.model.BonDTO;
import dsb.demo.javaee5.jpa.dao.BonDAO;
import dsb.demo.javaee5.jpa.model.Bon;

@Stateless
public class BonConverterBean extends AbstractConverterBean<Bon, BonDTO> implements BonConverter {

    @EJB
    private BonDAO dao;

    @Override
    public Bon fromDTO(BonDTO dto) {
        return this.dao.findById(dto.getId());
    }

    @Override
    public BonDTO toDTO(Bon bon) {
        return new BonDTO(bon.getId(), bon.getDebiteur());
    }
}
