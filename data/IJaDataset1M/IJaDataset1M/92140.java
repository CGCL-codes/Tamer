package com.yoursway.crashkit.internal.model;

import java.util.List;
import java.util.Map;
import com.yoursway.crashkit.internal.utils.YsDigest;
import com.yoursway.jyp.BeanEncoding;
import com.yoursway.jyp.JSON;

public class Report {

    private final String severity;

    private final String userActionOrScreenNameOrBackgroundProcess;

    private final List<ExceptionInfo> exceptions;

    private final Map<String, String> data;

    private final Map<String, String> env;

    private final String role;

    private final String hash;

    private final String date;

    private int count = 1;

    private final String language;

    private final String clientVersion;

    public Report(@BeanEncoding.Property("severity") String severity, @BeanEncoding.Property("date") String date, @BeanEncoding.Property("userActionOrScreenNameOrBackgroundProcess") String userActionOrScreenNameOrBackgroundProcess, @BeanEncoding.Property("exceptions") List<ExceptionInfo> exceptions, @BeanEncoding.Property("data") Map<String, String> data, @BeanEncoding.Property("env") Map<String, String> env, @BeanEncoding.Property("role") String role, @BeanEncoding.Property("language") String language, @BeanEncoding.Property("client_version") String clientVersion) {
        if (severity == null) throw new NullPointerException("severity is null");
        if (date == null) throw new NullPointerException("date is null");
        if (userActionOrScreenNameOrBackgroundProcess == null) throw new NullPointerException("userActionOrScreenNameOrBackgroundProcess is null");
        if (exceptions == null) throw new NullPointerException("exceptions is null");
        if (data == null) throw new NullPointerException("data is null");
        if (env == null) throw new NullPointerException("env is null");
        if (role == null) throw new NullPointerException("role is null");
        this.severity = severity;
        this.date = date;
        this.userActionOrScreenNameOrBackgroundProcess = userActionOrScreenNameOrBackgroundProcess;
        this.exceptions = exceptions;
        this.data = data;
        this.env = env;
        this.role = role;
        this.language = language;
        this.clientVersion = clientVersion;
        this.hash = YsDigest.sha1(severity + date + userActionOrScreenNameOrBackgroundProcess + JSON.encode(BeanEncoding.simplify(exceptions)) + JSON.encode(BeanEncoding.simplify(data)) + JSON.encode(BeanEncoding.simplify(env)) + role + language + clientVersion);
    }

    public String getSeverity() {
        return severity;
    }

    public String getDate() {
        return date;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getUserActionOrScreenNameOrBackgroundProcess() {
        return userActionOrScreenNameOrBackgroundProcess;
    }

    public List<ExceptionInfo> getExceptions() {
        return exceptions;
    }

    public Map<String, String> getData() {
        return data;
    }

    public Map<String, String> getEnv() {
        return env;
    }

    public String getRole() {
        return role;
    }

    public String getLanguage() {
        return language;
    }

    public String getClientVersion() {
        return clientVersion;
    }

    @BeanEncoding.Transient
    public String getHash() {
        return hash;
    }

    public void claimPackages(ClaimedPackages claimedPackages) {
        for (ExceptionInfo ei : exceptions) ei.claimPackages(claimedPackages);
    }
}
