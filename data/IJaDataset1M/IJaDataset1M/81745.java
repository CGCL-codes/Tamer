package mykeynote.client;

public interface PersistentClientInterface {

    public void setKey(String[] keys);

    public void setEnv(String[] envs);

    public void setCret(String[] creds);

    public void setResource(String resource);

    public boolean push();

    public String getAnswer();

    public static String LET = "<<<LET>>>";

    public static String END = "<<<END>>>";

    public static final String fastmode = "<<<fast>>>";

    public static final String key = "<key>";

    public static final String endKey = "</key>";

    public static final String cred = "<cred>";

    public static final String endCred = "</cred>";

    public static final String env = "<env>";

    public static final String endEnv = "</env>";

    public static final String resource = "<resource>";

    public static final String endResource = "</resource>";
}
