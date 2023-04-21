package net.sf.jukebox.service;

import javax.management.MXBean;

/**
 * @author Copyright &copy; <a href="mailto:vt@freehold.crocodile.org">Vadim Tkachenko</a> 2000-2008
 */
@MXBean
public interface PassiveServiceMXBean {

    ServiceStatus getStatus();
}
