package com.mockturtlesolutions.snifflib.mcmctools.database;

import java.util.Vector;
import com.mockturtlesolutions.snifflib.reposconfig.database.RepositoryStorage;
import com.mockturtlesolutions.snifflib.reposconfig.database.RepositoryMaintenance;
import com.mockturtlesolutions.snifflib.reposconfig.database.RepositoryStorageNameQuery;
import com.mockturtlesolutions.snifflib.reposconfig.database.ReposConfig;
import com.mockturtlesolutions.snifflib.reposconfig.database.RepositoryElement;

/**
This class defines the state of the asynchronous and distributed MCMC run process.
*/
public interface MCMCRunStorage extends RepositoryStorage {

    public String getMCMCRunState();

    public void setMCMCRunState(String state);

    public void advanceMCMCRunState();

    public void setMCMCStorageConfigClass(String sampler);

    public String getMCMCStorageConfigClass();

    public void setMCMCStorageRepositoryName(String sampler);

    public String getMCMCStorageRepositoryName();

    public void setMCMCStorageNickname(String sampler);

    public String getMCMCStorageNickname();

    public void setMCMCTraceStorageConfigClass(String sampler);

    public String getMCMCTraceStorageConfigClass();

    public void setMCMCTraceStorageRepositoryName(String sampler);

    public String getMCMCTraceStorageRepositoryName();

    public void setMCMCTraceStorageNickname(String sampler);

    public String getMCMCTraceStorageNickname();

    public void setParameterStorageConfigClass(String n);

    public String getParameterStorageConfigClass();

    public void setParameterStorageRepositoryName(String n);

    public String getParameterStorageRepositoryName();

    public void setObjectStorageConfigClass(String n);

    public String getObjectStorageConfigClass();

    public void setObjectStorageRepositoryName(String n);

    public String getObjectStorageRepositoryName();

    public void setMCMCRunIdentifier(String sampler);

    public String getMCMCRunIdentifier();

    public String getIsStillRunning();

    public void setIsStillRunning(String boole);

    public String getShouldHalt();

    public void setShouldHalt(String boole);

    public String getCurrentIterationNumber();

    public void setCurrentIterationNumber(String v);

    /**
	Link count is used to name the parameter storages of the trace for example.
	*/
    public String getCurrentLinkCount();

    public void setCurrentLinkCount(String v);

    public String getCurrentAlpha();

    public void setCurrentAlpha(String prob);

    public String getMaximumIterationNumber();

    public void setMaximumIterationNumber(String v);

    public String getRejectCount();

    public void setRejectCount(String v);

    public String getAcceptCount();

    public void setAcceptCount(String v);

    public int getParameterBlockCount();

    public String getCurrentParameterBlockIndex();

    public void setCurrentParameterBlockIndex(String x);

    public String getCurrentParameterBlockCycleNumber();

    public void setCurrentParameterBlockCycleNumber(String x);

    public void addParameterBlock();

    public void insertParameterBlock(int j);

    public void removeParameterBlock(int index);

    public void setParameterBlockName(int index, String name);

    public String getParameterBlockName(int index);

    public String getParameterBlockNumberOfCycles(int index);

    public void setParameterBlockNumberOfCycles(int index, String num);

    public Vector getParameterBlockParameters(int index);

    public void addParameterBlockParameter(int index, String pname);

    public void removeParameterBlockParameter(int index, String pname);

    public String getLastLinkInParameterChainConfigClass();

    public String getLastLinkInObjectChainConfigClass();

    public String getLastLinkInParameterChainRepositoryName();

    public String getLastLinkInObjectChainRepositoryName();

    public String getLastLinkInParameterChainNickname();

    public String getLastLinkInObjectChainNickname();

    public void addLinkInParameterChain(RepositoryElement elem);

    public void addLinkInObjectChain(RepositoryElement elem);

    public String getMCMCSamplerConfigClass();

    public String getMCMCSamplerRepositoryName();

    public String getMCMCSamplerNickname();

    public void setMCMCSamplerConfigClass(String config);

    public void setMCMCSamplerRepositoryName(String repos);

    public void setMCMCSamplerNickname(String repos);

    public RepositoryElement getProposalDistributionRepositoryElement();

    public void setProposalDistributionRepositoryElement(RepositoryElement elem);

    public void setALIKEStorageElement(RepositoryElement el);

    public RepositoryElement getALIKEStorageElement();

    public void setAPRIORStorageElement(RepositoryElement el);

    public RepositoryElement getAPRIORStorageElement();

    public void setAPROPStorageElement(RepositoryElement el);

    public RepositoryElement getAPROPStorageElement();

    public void setBLIKEStorageElement(RepositoryElement el);

    public RepositoryElement getBLIKEStorageElement();

    public void setBPRIORStorageElement(RepositoryElement el);

    public RepositoryElement getBPRIORStorageElement();

    public void setBPROPStorageElement(RepositoryElement el);

    public RepositoryElement getBPROPStorageElement();
}
