package controller.SGCA;

/**
 *
 * @author wldutra
 */
public class Prestador {

    private RegNegocio rn;

    private String idPrestador;

    private String nome;

    private String cnpj;

    private String telefone;

    private String telefoneFax;

    private String email;

    private String contato;

    private String statusPrestador;

    /** Creates a new instance of Prestador */
    public Prestador() {
        rn = new RegNegocio();
    }

    public void setIdPrestador(String idPrestador) {
        this.idPrestador = idPrestador;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setCnpj(String cnpj) {
        this.cnpj = cnpj;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public void setTelefoneFax(String telefoneFax) {
        this.telefoneFax = telefoneFax;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setContato(String contato) {
        this.contato = contato;
    }

    public void setStatusPrestador(String statusPrestador) {
        this.statusPrestador = statusPrestador;
    }

    public void setUrl(String url) {
        rn.setUrlString(url);
    }

    public void setDrv(String drv) {
        rn.setConDriver(drv);
    }

    public String getQry() {
        return rn.getQry();
    }

    public int getInserir() {
        return rn.exeQuery("INSERT INTO Prestador (" + "nome," + "cnpj," + "telefone," + "telefoneFax," + "email," + "contato," + "statusPrestador) VALUES (" + rn.devStr(nome) + "," + rn.devStr(cnpj) + "," + rn.devStr(telefone) + "," + rn.devStr(telefoneFax) + "," + rn.devStr(email) + "," + rn.devStr(contato) + "," + rn.devStr(statusPrestador) + ")");
    }

    public int getAlterar() {
        return rn.exeQuery("UPDATE Prestador SET " + "nome = " + rn.devStr(nome) + "," + "cnpj = " + rn.devStr(cnpj) + "," + "telefone = " + rn.devStr(telefone) + "," + "telefoneFax = " + rn.devStr(telefoneFax) + "," + "email = " + rn.devStr(email) + "," + "contato = " + rn.devStr(contato) + "," + "statusPrestador = " + rn.devStr(statusPrestador) + " WHERE idPrestador = " + rn.devStr(idPrestador));
    }
}
