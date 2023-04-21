package org.opensih.comunes.Actions;

import java.io.IOException;
import javax.ejb.Local;

@Local
public interface IManualSIQ {

    public void create();

    public void destroy();

    public String downloadPDF() throws IOException;

    public String getUrlManual();

    public void setUrlManual(String urlManual);
}
