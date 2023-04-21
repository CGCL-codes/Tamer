package com.griddynamics.convergence.demo.utils.cluster.ssh;

import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;

public interface SshSessionFactory {

    public Session connect(String host) throws JSchException;
}
