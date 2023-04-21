package com.br.fsm.projectdelivery.controlador;

import java.util.ArrayList;
import com.br.fsm.projectdelivery.basica.Endereco;
import com.br.fsm.projectdelivery.excecao.ControladorException;

public interface IControladorEndereco {

    public Endereco getEnderecoById(int id) throws ControladorException;

    public ArrayList<Endereco> getAllEndereco() throws ControladorException;

    public void insertEndereco(Endereco endereco) throws ControladorException;

    public void updateEndereco(Endereco endereco) throws ControladorException;

    public void removeEndereco(Endereco endereco) throws ControladorException;
}
