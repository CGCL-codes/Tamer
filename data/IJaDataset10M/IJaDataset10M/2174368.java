package br.com.rasoft.academix.persistence.dao;

import java.util.Collection;
import br.com.rasoft.academix.persistence.exception.DAOException;
import br.com.rasoft.academix.persistence.to.AlunoTO;

public interface AlunoDAO {

    public void createAluno(AlunoTO alunoTO) throws DAOException;

    public void editAluno(AlunoTO alunoTO) throws DAOException;

    public void deleteAluno(AlunoTO alunoTO) throws DAOException;

    public Collection findAll() throws DAOException;

    public AlunoTO findAluno(String nome) throws DAOException;
}
