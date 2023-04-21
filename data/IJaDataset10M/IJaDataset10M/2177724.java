package com.tenline.pinecone.platform.web.service.restful;

import java.io.IOException;
import java.util.Collection;
import javax.jdo.PersistenceManagerFactory;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.orm.jdo.support.JdoDaoSupport;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.tenline.pinecone.platform.model.Item;
import com.tenline.pinecone.platform.model.Record;
import com.tenline.pinecone.platform.web.service.RecordService;
import com.tenline.pinecone.platform.web.service.oauth.OAuthProvider;
import com.tenline.pinecone.platform.web.service.oauth.OAuthUtils;

/**
 * @author Bill
 *
 */
@Service
@Transactional
public class RecordRestfulService extends JdoDaoSupport implements RecordService, ApplicationContextAware {

    private OAuthProvider provider;

    /**
	 * 
	 */
    @Autowired
    public RecordRestfulService(PersistenceManagerFactory persistenceManagerFactory) {
        setPersistenceManagerFactory(persistenceManagerFactory);
    }

    @Override
    public Response delete(String id) {
        getJdoTemplate().deletePersistent(getJdoTemplate().getObjectById(Record.class, id));
        return Response.status(Status.OK).build();
    }

    @Override
    public Record create(Record record) {
        record.setItem(getJdoTemplate().getObjectById(Item.class, record.getItem().getId()));
        return getJdoTemplate().makePersistent(record);
    }

    @Override
    public Record update(Record record) {
        Record detachedRecord = (Record) getJdoTemplate().getObjectById(Record.class, record.getId());
        if (record.getTimestamp() != null) detachedRecord.setTimestamp(record.getTimestamp());
        return getJdoTemplate().makePersistent(detachedRecord);
    }

    @Override
    @SuppressWarnings("unchecked")
    public Collection<Record> show(String filter) {
        String queryString = "select from " + Record.class.getName();
        if (!filter.equals("all")) queryString += " where " + filter;
        return getJdoTemplate().find(queryString);
    }

    @Override
    public Collection<Record> showByItem(String filter, HttpServletRequest request, HttpServletResponse response) {
        try {
            OAuthUtils.doFilter(request, response, provider);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ServletException e) {
            e.printStackTrace();
        }
        return getJdoTemplate().getObjectById(Item.class, filter.substring(filter.indexOf("'") + 1, filter.lastIndexOf("'"))).getRecords();
    }

    @Override
    public void setApplicationContext(ApplicationContext arg0) throws BeansException {
        provider = OAuthUtils.getOAuthProvider(arg0);
    }
}
