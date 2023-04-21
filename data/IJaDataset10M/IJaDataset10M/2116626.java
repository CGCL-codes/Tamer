package br.com.slv.database.dao.entity;

import br.com.slv.database.dao.entity.annotation.GPAEntity;
import br.com.slv.database.dao.entity.annotation.GPAField;
import br.com.slv.database.dao.entity.annotation.GPAPrimaryKey;

@GPAEntity(name = "tb_usuario")
public class Usuario extends Entity {

    @GPAPrimaryKey(name = "id", ignore = true)
    private int id;

    @GPAField(name = "nome_completo")
    private String nomeCompleto;

    @GPAField(name = "nome_usuario")
    private String nomeUsuario;

    @GPAField(name = "senha")
    private String senha;

    @GPAField(name = "id_device")
    private int idDevice;

    /**
	 * Used by insert without id, database are responsible 
	 * to generate a sequential id to primary key 
	 * @param nomeCompleto
	 * @param nomeUsuario
	 * @param senhaCharacter
	 * @param idDevice
	 */
    public Usuario(String nomeCompleto, String nomeUsuario, String senhaCharacter, int idDevice) {
        super();
        this.nomeCompleto = nomeCompleto;
        this.nomeUsuario = nomeUsuario;
        this.senha = senhaCharacter;
        this.idDevice = idDevice;
    }

    /**
	 * in this case the entity are instantied with the primary key information
	 * @param id
	 * @param nomeCompleto
	 * @param nomeUsuario
	 * @param senhaCharacter
	 * @param idDevice
	 */
    public Usuario(int id, String nomeCompleto, String nomeUsuario, String senhaCharacter, int idDevice) {
        super();
        this.id = id;
        this.nomeCompleto = nomeCompleto;
        this.nomeUsuario = nomeUsuario;
        this.senha = senhaCharacter;
        this.idDevice = idDevice;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNomeCompleto() {
        return nomeCompleto;
    }

    public void setNomeCompleto(String nomeCompleto) {
        this.nomeCompleto = nomeCompleto;
    }

    public String getNomeUsuario() {
        return nomeUsuario;
    }

    public void setNomeUsuario(String nomeUsuario) {
        this.nomeUsuario = nomeUsuario;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public int getIdDevice() {
        return idDevice;
    }

    public void setIdDevice(int idDevice) {
        this.idDevice = idDevice;
    }
}
