package Messages;

public class KeyGeneration extends Configuration {

    public static final String KEY = "key";

    public KeyGeneration(String key) throws InvalidVarListException {
        m_context = "regenerateKey";
        m_renderOrder = new String[] { KEY };
        this.PutStringInVarList(key);
    }
}
