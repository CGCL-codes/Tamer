package org.iocframework.conf;

/**
 * @author Sledge
 */
public final class TemplateParameter extends JobDetails {

    private final Object key;

    private final boolean required;

    private final JobDetails dfault;

    /**
   * @param key
   * @throws ExternalConfigException
   */
    public TemplateParameter(Object key, String srcDescr) throws ExternalConfigException {
        this(key, false, null, srcDescr);
    }

    /**
   * @param key
   * @param required
   * @throws ExternalConfigException
   */
    public TemplateParameter(Object key, boolean required, String srcDescr) throws ExternalConfigException {
        this(key, required, null, srcDescr);
    }

    /**
   * @param key
   * @param dfault
   * @throws ExternalConfigException
   */
    public TemplateParameter(Object key, JobDetails dfault, String srcDescr) throws ExternalConfigException {
        this(key, false, dfault, srcDescr);
    }

    /**
   * @param key
   * @param dfault
   * @throws ExternalConfigException
   */
    private TemplateParameter(Object key, boolean required, JobDetails dfault, String srcDescr) throws ExternalConfigException {
        super(srcDescr);
        this.key = key;
        this.required = required;
        if (!(dfault instanceof JobDetails)) {
            throw new ExternalConfigException(srcDescr);
        }
        this.dfault = dfault;
    }

    /**
   * @return the key
   */
    public Object getKey() {
        return key;
    }

    /**
   * @return the required
   */
    public boolean isRequired() {
        return required;
    }

    /**
   * @return the default
   */
    public JobDetails getDefault() {
        return dfault;
    }
}
