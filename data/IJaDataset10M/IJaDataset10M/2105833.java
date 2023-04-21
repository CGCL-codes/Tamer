package lv.odylab.evemanage.client.rpc.action.blueprints;

import lv.odylab.evemanage.client.rpc.action.Action;
import lv.odylab.evemanage.client.rpc.action.RunnedBy;

@RunnedBy(BlueprintsImportActionRunner.class)
public class BlueprintsImportAction implements Action<BlueprintsImportActionResponse> {

    private String importXml;

    private String importCsv;

    private String oneTimeFullApiKey;

    private Long oneTimeUserID;

    private Long oneTimeCharacterID;

    private String oneTimeLevel;

    private Long fullApiKeyCharacterID;

    private String fullApiKeyLevel;

    private Long attachedCharacterID;

    private String sharingLevel;

    public String getImportXml() {
        return importXml;
    }

    public void setImportXml(String importXml) {
        this.importXml = importXml;
    }

    public String getImportCsv() {
        return importCsv;
    }

    public void setImportCsv(String importCsv) {
        this.importCsv = importCsv;
    }

    public String getOneTimeFullApiKey() {
        return oneTimeFullApiKey;
    }

    public void setOneTimeFullApiKey(String oneTimeFullApiKey) {
        this.oneTimeFullApiKey = oneTimeFullApiKey;
    }

    public Long getOneTimeUserID() {
        return oneTimeUserID;
    }

    public void setOneTimeUserID(Long oneTimeUserID) {
        this.oneTimeUserID = oneTimeUserID;
    }

    public Long getOneTimeCharacterID() {
        return oneTimeCharacterID;
    }

    public void setOneTimeCharacterID(Long oneTimeCharacterID) {
        this.oneTimeCharacterID = oneTimeCharacterID;
    }

    public String getOneTimeLevel() {
        return oneTimeLevel;
    }

    public void setOneTimeLevel(String oneTimeLevel) {
        this.oneTimeLevel = oneTimeLevel;
    }

    public Long getFullApiKeyCharacterID() {
        return fullApiKeyCharacterID;
    }

    public void setFullApiKeyCharacterID(Long fullApiKeyCharacterID) {
        this.fullApiKeyCharacterID = fullApiKeyCharacterID;
    }

    public String getFullApiKeyLevel() {
        return fullApiKeyLevel;
    }

    public void setFullApiKeyLevel(String fullApiKeyLevel) {
        this.fullApiKeyLevel = fullApiKeyLevel;
    }

    public Long getAttachedCharacterID() {
        return attachedCharacterID;
    }

    public void setAttachedCharacterID(Long attachedCharacterID) {
        this.attachedCharacterID = attachedCharacterID;
    }

    public String getSharingLevel() {
        return sharingLevel;
    }

    public void setSharingLevel(String sharingLevel) {
        this.sharingLevel = sharingLevel;
    }
}
