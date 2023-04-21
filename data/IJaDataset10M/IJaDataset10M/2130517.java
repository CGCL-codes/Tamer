package net.sf.vcaperture.web.models;

import java.util.ArrayList;
import net.sf.vcaperture.model.AbstractRepository;
import net.sf.vcaperture.model.IApplicationContext;
import net.sf.vcaperture.services.IRepositoryService;
import net.sf.vcaperture.util.spring.ApplicationContextFactory;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;

public class RepositoryListModel extends LoadableDetachableModel implements IModel {

    private static final long serialVersionUID = 1L;

    @Override
    protected Object load() {
        ApplicationContextFactory factory = ApplicationContextFactory.getFactory();
        IApplicationContext context = factory.getContext();
        IRepositoryService svc = context.getBean(IRepositoryService.class);
        return new ArrayList<AbstractRepository>(svc.getRepositories());
    }
}
