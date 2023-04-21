package edu.ort.common.log;

import javax.ejb.Asynchronous;
import javax.ejb.LocalBean;

/**
 *
 * @author migueldiab
 */
@LocalBean
public interface Logger {

    @Asynchronous
    public void crit(String mensaje, String trace);

    @Asynchronous
    public void debug(String mensaje);

    @Asynchronous
    public void error(String mensaje, String trace);

    @Asynchronous
    public void info(String mensaje);

    @Asynchronous
    public void warn(String mensaje, String trace);
}
