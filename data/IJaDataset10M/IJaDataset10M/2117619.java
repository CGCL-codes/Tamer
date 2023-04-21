package Interfaces;

import Dados.*;

public interface IRepositorio {

    public void cadastrarAdministrador(Administrador adm);

    public void editarAdministrador(Administrador adm);

    public void removerAdministrador(String cpf);

    public Administrador consultarAdministrador(String cpf);

    public boolean Paciente(String cpf);

    public void cadastrarMedico(Medico medico);

    public void editarMedico(Medico medico);

    public void removerMedico(String cpf);

    public Medico consultarMedico(String cpf);

    public boolean verificarMedico(String cpf);

    public void cadastrarSecretaria(Secretaria sec);

    public void editarSecretaria(Secretaria sec);

    public void removerSecretaria(String cpf);

    public Secretaria consultarSecretaria(String cpf);

    public boolean verificarSecretaria(String cpf);

    public void cadastrarPaciente(Paciente pac);

    public void editarPaciente(Paciente pac);

    public void removerPaciente(String cpf);

    public Paciente consultarPaciente(String cpf);

    public boolean verificarPaciente(String cpf);

    public void cadastrarConsulta(Consulta cons);

    public void editarConsulta(Consulta cons);

    public void removerConsulta(int codigo);

    public Consulta consultarConsulta(int codigo);
}
