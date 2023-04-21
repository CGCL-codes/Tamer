package cn.myapps.core.homepage.ejb;

import java.util.Collection;
import cn.myapps.base.ejb.IDesignTimeProcess;

public interface HomePageProcess extends IDesignTimeProcess {

    public Collection doViewByApplication(String applicationId) throws Exception;
}
