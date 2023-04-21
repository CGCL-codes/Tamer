package org.pubcurator.gui.test.models;

/**
 * @author  Kai Schlamp (schlamp@gmx.de)
 */
public class AnalyzerDelegateTestModel {

    private String analyzerId;

    private String analyzerName;

    private String delegateName;

    private String analyzerMinVersion;

    private String analyzerMaxVersion;

    public String getAnalyzerId() {
        return analyzerId;
    }

    public void setAnalyzerId(String analyzerId) {
        this.analyzerId = analyzerId;
    }

    public String getAnalyzerName() {
        return analyzerName;
    }

    public void setAnalyzerName(String analyzerName) {
        this.analyzerName = analyzerName;
    }

    public String getDelegateName() {
        return delegateName;
    }

    public void setDelegateName(String delegateName) {
        this.delegateName = delegateName;
    }

    public String getAnalyzerMinVersion() {
        return analyzerMinVersion;
    }

    public void setAnalyzerMinVersion(String analyzerMinVersion) {
        this.analyzerMinVersion = analyzerMinVersion;
    }

    public String getAnalyzerMaxVersion() {
        return analyzerMaxVersion;
    }

    public void setAnalyzerMaxVersion(String analyzerMaxVersion) {
        this.analyzerMaxVersion = analyzerMaxVersion;
    }
}
