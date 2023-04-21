package com.losalpes.catalogo.servicios;

import com.losalpes.catalogo.entities.Usuario;
import com.losalpes.catalogo.excepciones.InvalidDataException;
import com.losalpes.catalogo.excepciones.InvalidOperationException;
import java.util.ArrayList;
import javax.ejb.Remote;

/**
 * Contrato funcional de los servicios de adminsitración de usuarios brindados a la aplicación.
 * Estos métodos son expuestos de manera remota para utilizarlos en pruebas.
 * @author German Sotelo
 */
@Remote
public interface IUsuarioServicesRemote {

    /**
     * Isntancia un usuario nuevo para ser modificado
     * @return Un usuario nuevo
     */
    public Usuario newUsuario();

    /**
     * Crea el usuario dado como parámetro y lo agrega al sistema
     * @param user Usuario a crear
     * @throws com.losalpes.catalogo.excepciones.InvalidDataException Si los datos del usuario no son válidos
     * @throws com.losalpes.catalogo.excepciones.InvalidOperationException Si el usuario ya existía en el sistema.
     */
    public void crearUsuario(Usuario user) throws InvalidDataException, InvalidOperationException;

    /**
     * Retorna una lista con todos los usuarios del sistema
     * @return
     */
    public ArrayList<Usuario> findAll();

    /**
     * Borra un usuario dado su username
     * @param user
     * @throws com.losalpes.catalogo.excepciones.InvalidOperationException
     */
    public void delete(String user) throws InvalidOperationException;

    /**
     * Actualiza los datos del usuario pasado como parámetro
     * @param user
     * @throws com.losalpes.catalogo.excepciones.InvalidDataException Si los datos del usuario son inválidos
     */
    public void updateUsuario(Usuario user) throws InvalidDataException;
}
