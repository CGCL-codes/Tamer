package org.redsocial.dao;

import java.sql.SQLException;
import java.util.List;
import org.redsocial.model.PaginationList;
import org.redsocial.model.Usuario;

public interface UsuarioDAO {

    /**
     * Devuelve la lista de todos los usuarios.
     * 
     * @return
     * @throws SQLException
     */
    public List<Usuario> selectAllUsuarios() throws SQLException;

    /**
     * Obtiene los datos de un usuario concreto o null en caso de que no exista.
     * @param id El identificador �nico de usuario.
     */
    public Usuario select(int id) throws SQLException;

    /**
     * Devuelve un usuario cuyo LOGIN sea el indicado.
	 *
     * @param login El login a buscar
     * @return El usuario o null
     * @throws SQLException Si algo casca 
     */
    public Usuario select(String login) throws SQLException;

    /**
	 * Actualiza los datos de un usuario.
	 * @param user Los nuevos datos del usuario.
	 */
    public void modificarDatosUsuario(Usuario user) throws SQLException;

    /**
	 * Modifica la foto de un usuario.
	 * @param id Identificador del usuario a modificar.
	 * @param foto Imagen en formato de array de bytes.
	 */
    public void modificarFotoUsuario(int id, byte[] foto) throws SQLException;

    /**Borra el usuario del sistema*/
    public void delete(int idUsuario) throws SQLException;

    /**
	 * Almacena un nuevo usuario en la BBDD.
	 * @return El id del usuario insertado.
	 */
    public int insertarUsuario(Usuario newUser) throws SQLException;

    /**Devuelve los usuarios de la p�gina indicada que se parezcan al usuario proporcionado (Puede ser NULL!) (OJO, ROOT NUNCA SER� DEVUELTO)*/
    public PaginationList buscarUsuarios(Usuario usr, int numPagina, int pageSize) throws SQLException;

    /**Devuelve la lista de objetos Usuario que son amigos del indicado en ID*/
    public List<Usuario> selectFriends(int id);

    /**Devuelve los datos de un usuario en base a su NICK*/
    public Usuario selectByNick(String nick) throws SQLException;

    /**Devuelve los datos (sin FOTO) de los usuarios cuyo ID est� entre los inclu�dos*/
    public List<Usuario> buscarUsuarios(List<Integer> listaIds);

    /**Devuelve el tama�o m�ximo de una FOTO.*/
    public int getMaxFotoSize();
}
