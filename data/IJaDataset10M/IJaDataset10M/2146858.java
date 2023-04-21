package net.sf.brightside.petspace.tapestry.filter;

import java.io.IOException;
import org.apache.tapestry5.services.Request;
import org.apache.tapestry5.services.RequestFilter;
import org.apache.tapestry5.services.RequestHandler;
import org.apache.tapestry5.services.Response;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import org.springframework.transaction.annotation.Transactional;

public class OpenSessionInViewFilter extends HibernateDaoSupport implements RequestFilter {

    @Override
    @Transactional
    public boolean service(Request arg0, Response arg1, RequestHandler arg2) throws IOException {
        getSessionFactory().getCurrentSession().beginTransaction();
        arg2.service(arg0, arg1);
        return true;
    }
}
