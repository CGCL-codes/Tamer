package websiteschema.service;

import java.util.Map;
import java.util.Date;
import websiteschema.dwr.response.ListRange;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import websiteschema.model.domain.Job;
import websiteschema.persistence.rdbms.JobMapper;
import static websiteschema.persistence.rdbms.utils.ParameterUtil.*;

/**
 *
 * @author ray
 */
@Service
public class JobService {

    @Autowired
    private JobMapper jobMapper;

    public ListRange getResults(Map map) {
        ListRange listRange = new ListRange();
        Map params = buildParamWithInt(map, "start", "limit");
        listRange.setData(jobMapper.getJobs(params).toArray());
        listRange.setTotalSize(jobMapper.getTotalResults());
        return listRange;
    }

    public Job getById(long id) {
        return jobMapper.getById(id);
    }

    @Transactional
    public long insert(Job job) {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        job.setCreateUser(userDetails.getUsername());
        job.setLastUpdateUser(job.getCreateUser());
        jobMapper.insert(job);
        return job.getId();
    }

    @Transactional
    public void update(Job job) {
        job.setUpdateTime(new Date());
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        job.setLastUpdateUser(userDetails.getUsername());
        jobMapper.update(job);
    }

    @Transactional
    public void deleteRecord(Job job) {
        jobMapper.delete(job);
    }
}
