package bias.core;

import bias.Constants;

/**
 * @author kion
 */
public abstract class TransferConfiguration {

    private String transferProvider;

    private String password;

    public TransferConfiguration() {
    }

    protected TransferConfiguration(String transferProvider, String password) {
        this.transferProvider = transferProvider;
        this.password = password;
    }

    public String getTransferProvider() {
        return transferProvider;
    }

    public void setTransferProvider(String transferProvider) {
        this.transferProvider = transferProvider;
    }

    public String getPassword() {
        return password != null ? password : Constants.EMPTY_STR;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
