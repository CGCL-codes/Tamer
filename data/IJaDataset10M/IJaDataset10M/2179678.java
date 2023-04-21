package com.alesj.blade.reports;

/**
 * @author <a href="mailto:ales.justin@genera-lynx.com">Ales Justin</a>
 */
public interface Report {

    String progress();

    String finished();

    String history();

    String detailRequest();

    public String reset();

    public void destroy();
}
