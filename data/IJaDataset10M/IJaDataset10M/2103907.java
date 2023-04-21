package monkey.generator;

import java.util.Hashtable;

public abstract class GeneratorData {

    protected Hashtable additionalUserCode;

    public GeneratorData() {
        additionalUserCode = new Hashtable();
    }

    /**
     * Adds some user code to the scanner data.<br>
     * For different languages exists different kinds of user code which can be inserted in the generates scanner.
     *
     * @param codeId   the code ID
     * @param userCode the user code in the appropriate language
     */
    public void addUserCode(String codeId, String userCode) {
        if (userCode != null) additionalUserCode.put(codeId, userCode);
    }

    /**
     * @param codeId the code Id
     * @return user code
     */
    public String getUserCode(String codeId) {
        return (String) additionalUserCode.get(codeId);
    }
}
