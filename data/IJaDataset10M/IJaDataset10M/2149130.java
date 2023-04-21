package org.atlantal.impl.app.transform;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamSource;
import org.atlantal.api.app.exception.ServiceException;
import org.atlantal.api.app.transform.TransformerException;
import org.atlantal.utils.wrapper.ObjectWrapper;

/**
 * <p>Titre : Ewaloo</p>
 * <p>Description : Moteur de recherche structur�</p>
 * <p>Copyright : Copyright (c) 2002</p>
 * <p>Soci�t� : Mably Multim�dia</p>
 * @author <a href="mailto:masurel@mably.com">Francois MASUREL</a>
 * @version 1.0
 */
public abstract class AbstractXSLTransformerService extends TransformerServiceInstance {

    private String xsldir;

    private TransformerFactory tFactory = null;

    /**
     * Init
     * {@inheritDoc}
     */
    public void init(Map params) throws ServiceException {
        tFactory = TransformerFactory.newInstance();
        xsldir = (String) params.get("xsldir");
    }

    /**
     * {@inheritDoc}
     */
    public abstract ObjectWrapper getTransformer(String name) throws TransformerException;

    /**
     * {@inheritDoc}
     */
    protected File getXSLFile(String name) throws IOException {
        String xslpath = xsldir + File.separator + name;
        xslpath = getRealPath(xslpath);
        return new File(xslpath);
    }

    /**
     * {@inheritDoc}
     */
    protected Transformer getTransformer(File xslfile) throws TransformerConfigurationException {
        Transformer trans = tFactory.newTransformer(new StreamSource(xslfile));
        trans.setOutputProperty("encoding", "iso-8859-1");
        return trans;
    }
}
