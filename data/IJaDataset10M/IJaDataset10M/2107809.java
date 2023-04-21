package interfaces;

import java.rmi.Remote;
import application.ObjInit;
import application.Studente;

public interface ProfessoreInterface extends Remote {

    public ObjInit r_join(Studente s);

    public void r_leave(String matricola);

    public void pubblicaPagina(int pag);

    public void r_richiestaControllo(String matricola);

    public void passaControllo(Studente richiedente);

    public void revocaControllo();

    public void r_cambiaPagina(int pag);

    public String getNomeProfessore();

    public void setNomeProfessore(String nomeProfessore);

    public String getNomeLezione();

    public void setNomeLezione(String nomeLezione);
}
