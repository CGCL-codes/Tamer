package pe.edu.upc.dsd.epica.dao;

import java.util.List;
import pe.edu.upc.dsd.epica.model.PromocionxUsuario;

public interface PromocionxUsuarioDao {

    public void save(PromocionxUsuario usuarioPromocion);

    public void update(PromocionxUsuario usuarioPromocion);

    public void delete(PromocionxUsuario usuarioPromocion);

    public List<PromocionxUsuario> findByPromocionCode(String codigo);

    public List<PromocionxUsuario> findByUsuarioCode(String codigo);

    public int getCountByPromocionCode(String codigo);

    public PromocionxUsuario findByUsuarioPromocion(PromocionxUsuario promoUsuario);
}
