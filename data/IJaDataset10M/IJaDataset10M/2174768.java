package br.com.arsmachina.authentication.dao.hibernate.ioc;

import org.hibernate.SessionFactory;
import org.springframework.config.java.annotation.Bean;
import org.springframework.config.java.annotation.Configuration;
import org.springframework.config.java.annotation.ExternalBean;
import org.springframework.config.java.annotation.Import;
import org.springframework.config.java.annotation.Lazy;
import br.com.arsmachina.authentication.controller.PasswordEncrypter;
import br.com.arsmachina.authentication.controller.PermissionController;
import br.com.arsmachina.authentication.controller.PermissionGroupController;
import br.com.arsmachina.authentication.controller.UserController;
import br.com.arsmachina.authentication.controller.impl.PermissionControllerImpl;
import br.com.arsmachina.authentication.controller.impl.PermissionGroupControllerImpl;
import br.com.arsmachina.authentication.controller.impl.UserControllerImpl;
import br.com.arsmachina.authentication.dao.PermissionDAO;
import br.com.arsmachina.authentication.dao.PermissionGroupDAO;
import br.com.arsmachina.authentication.dao.UserDAO;
import br.com.arsmachina.authentication.dao.hibernate.PermissionDAOImpl;
import br.com.arsmachina.authentication.dao.hibernate.PermissionGroupDAOImpl;
import br.com.arsmachina.authentication.dao.hibernate.UserDAOImpl;
import br.com.arsmachina.dao.hibernate.ioc.PersistenceConfiguration;

/**
 * Spring JavaConfig configuration class that declares beans provided by
 * generic-authentication-hibernate.
 * 
 * @author Thiago H. de Paula Figueiredo
 */
@Configuration(defaultLazy = Lazy.TRUE)
@Import({ PersistenceConfiguration.class })
public class GenericAuthenticationHibernateConfiguration {

    @Bean
    public UserDAO userDAO() {
        return new UserDAOImpl(sessionFactory(), passwordEncrypter());
    }

    @Bean(lazy = Lazy.FALSE)
    public UserController userController() {
        return new UserControllerImpl(userDAO(), passwordEncrypter(), permissionController(), permissionGroupController());
    }

    @Bean
    public PermissionDAO permissionDAO() {
        return new PermissionDAOImpl(sessionFactory());
    }

    @Bean
    public PermissionController permissionController() {
        return new PermissionControllerImpl(permissionDAO());
    }

    @Bean
    public PermissionGroupDAO permissionGroupDAO() {
        return new PermissionGroupDAOImpl(sessionFactory());
    }

    @Bean
    public PermissionGroupController permissionGroupController() {
        return new PermissionGroupControllerImpl(permissionGroupDAO());
    }

    @ExternalBean
    public SessionFactory sessionFactory() {
        return null;
    }

    @ExternalBean
    public PasswordEncrypter passwordEncrypter() {
        return null;
    }
}
