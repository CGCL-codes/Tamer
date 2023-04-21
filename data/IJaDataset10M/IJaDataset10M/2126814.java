package galerias.daos.implementaciones;

import galerias.daos.interfaces.ImagenUsuarioDaoLocalInterface;
import galerias.entidades.ImagenUsuario;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Stateless
public class ImagenUsuarioDaoImpl implements ImagenUsuarioDaoLocalInterface {

    @PersistenceContext
    private EntityManager em;

    public ImagenUsuario obtenerImagenUsuario(String codigoUsuario) {
        System.out.println("obtenerImagenUsuario dao");
        StringBuffer queryJPQL = new StringBuffer();
        queryJPQL.append(" SELECT imagenUsuario FROM ImagenUsuario imagenUsuario");
        queryJPQL.append(" WHERE imagenUsuario.usuario.codigo = ");
        queryJPQL.append(codigoUsuario);
        ImagenUsuario imagenUsuario = null;
        try {
            imagenUsuario = (ImagenUsuario) (this.em.createQuery(queryJPQL.toString()).getSingleResult());
        } catch (Exception e) {
        }
        return imagenUsuario;
    }

    @Override
    public Boolean actualizarImagenUsuario(ImagenUsuario imagenUsuario) {
        System.out.println("entre actualizarImagenUsuario > " + imagenUsuario.getCodigo());
        try {
            ImagenUsuario imagenUsuarioBD = em.find(ImagenUsuario.class, imagenUsuario.getCodigo());
            System.out.println("imagenUsuarioBD > " + imagenUsuarioBD);
            if (imagenUsuarioBD != null) {
                imagenUsuarioBD.setImagen(imagenUsuario.getImagen());
                imagenUsuarioBD.setTipoImagen(imagenUsuario.getTipoImagen());
                imagenUsuarioBD.setNombreArchivo(imagenUsuario.getNombreArchivo());
            }
            this.em.flush();
            return new Boolean(true);
        } catch (Exception e) {
            e.printStackTrace();
            return new Boolean(false);
        }
    }

    @Override
    public Boolean guardarImagenUsuario(ImagenUsuario imagenUsuario) {
        try {
            this.em.persist(imagenUsuario);
            this.em.flush();
            return new Boolean(true);
        } catch (Exception e) {
            e.printStackTrace();
            return new Boolean(false);
        }
    }
}
