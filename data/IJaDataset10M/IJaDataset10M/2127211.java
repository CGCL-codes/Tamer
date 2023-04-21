package eu.planets_project.pp.plato.action.interfaces;

import java.util.List;
import org.jboss.annotation.ejb.Local;
import eu.planets_project.pp.plato.application.ErrorClass;
import eu.planets_project.pp.plato.application.NewsClass;

@Local
public interface IMessages {

    public List<ErrorClass> getErrors();

    public List<NewsClass> getNews();

    public void addErrorMessage(ErrorClass error);

    public void addNewsMessage(NewsClass news);

    public void clearErrors();

    public void clearNews();

    public void destroy();
}
