package alfresco.module.sword;

/**
 *
 * @author clayton
 */
public interface IServiceDetails {

    public String getGenerator();

    public void setGenerator(String generator);

    public int getMaxUploadSize();

    public void setMaxUploadSize(int maxUploadSize);

    public boolean isMediation();

    public void setMediation(boolean mediation);

    public boolean isMD5Permitted();

    public void setMD5Permitted(boolean md5Permitted);

    public boolean isNoOp();

    public void setNoOp(boolean noOp);

    public boolean isVerbose();

    public void setVerbose(boolean verbose);

    public boolean isOnBehalfOfPermitted();

    public void setOnBehalfOfPermitted(boolean onBehalfOfPermitted);

    public String getVersion();

    public void setVersion(String version);

    public String getUrl();

    public void setUrl(String url);
}
