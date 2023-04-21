package br.com.arsmachina.example.dao.hibernate;

import org.hibernate.SessionFactory;
import br.com.arsmachina.dao.hibernate.GenericDAOImpl;
import br.com.arsmachina.example.controller.ProjectController;
import br.com.arsmachina.example.dao.ProjectDAO;
import br.com.arsmachina.example.entity.Project;

/**
 * {@link ProjectController} implementation using Hibernate.
 * 
 * @author Thiago H. de Paula Figueiredo
 */
public class ProjectDAOImpl extends GenericDAOImpl<Project, Integer> implements ProjectDAO {

    /**
	 * @param sessionFactory
	 */
    public ProjectDAOImpl(SessionFactory sessionFactory) {
        super(sessionFactory);
    }
}
