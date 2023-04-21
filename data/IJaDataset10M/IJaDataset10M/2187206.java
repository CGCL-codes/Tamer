package net.sf.doolin.app.svena.gui.bean.factory;

import net.sf.doolin.app.svena.gui.bean.ManageRepositoriesBean;
import net.sf.doolin.app.svena.gui.service.RepositoryStoreService;
import net.sf.doolin.util.factory.DataFactory;
import org.springframework.beans.factory.annotation.Autowired;

public class ManageRepositoriesBeanFactory implements DataFactory<ManageRepositoriesBean> {

    private final RepositoryStoreService repositoryStoreService;

    @Autowired
    public ManageRepositoriesBeanFactory(RepositoryStoreService repositoryStoreService) {
        this.repositoryStoreService = repositoryStoreService;
    }

    public ManageRepositoriesBean create(Object rootBean) {
        ManageRepositoriesBean bean = new ManageRepositoriesBean(this.repositoryStoreService.getRepositories());
        return bean;
    }
}
