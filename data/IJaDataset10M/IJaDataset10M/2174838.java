package net.cygeek.tech.client.service;

import com.google.gwt.user.client.rpc.AsyncCallback;
import net.cygeek.tech.client.data.JobtitEmpstat;
import java.util.Date;

public interface JobtitEmpstatServiceAsync {

    void getJobtitEmpstats(AsyncCallback async);

    void addJobtitEmpstat(JobtitEmpstat mJobtitEmpstat, boolean isNew, AsyncCallback async);

    void deleteJobtitEmpstat(String jobtitCode, String estatCode, AsyncCallback async);

    void getJobtitEmpstat(String jobtitCode, String estatCode, AsyncCallback async);
}
