package pe.edu.upc.dsd.epica.dao;

import java.util.List;
import pe.edu.upc.dsd.epica.model.Promocion;

public interface PromocionDao {

    public void save(Promocion promocion);

    public void update(Promocion promocion);

    public void delete(String codigo);

    public Promocion findByPromocionCode(String codigo);

    public List<Promocion> findByEstablecimientoCode(String codigo);

    public int getConteoPromocion();
}
