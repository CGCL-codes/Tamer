package hu.sztaki.lpds.wfi.zen.pools;

import hu.sztaki.lpds.logging.Logger;
import hu.sztaki.lpds.wfi.service.zen.Base;
import hu.sztaki.lpds.wfi.service.zen.xml.objects.PSInstance;
import hu.sztaki.lpds.wfi.zen.pools.inf.InstancePool;
import java.util.Enumeration;
import java.util.Hashtable;

/**
 *
 * @author krisztian
 */
public class RAMInstancePoolImpl implements InstancePool {

    protected Hashtable<String, Hashtable<String, Hashtable<String, PSInstance>>> datas = new Hashtable<String, Hashtable<String, Hashtable<String, PSInstance>>>();

    @Override
    public void createWorkflow(String pZenID) {
        if (datas.get(pZenID) == null) datas.put(pZenID, new Hashtable<String, Hashtable<String, PSInstance>>());
    }

    @Override
    public void createJob(String pZenID, String pJobID) {
        createWorkflow(pZenID);
        if (datas.get(pZenID).get(pJobID) == null) datas.get(pZenID).put(pJobID, new Hashtable<String, PSInstance>());
    }

    @Override
    public void createJobInstance(String pZenID, String pJobID, long pPID) {
    }

    @Override
    public void createInput(String pZeniD, String pJobID, long pPID, String inputName, long inputPid, String preJob) {
        createJobInstance(pZeniD, pJobID, pPID);
        if (datas.get(pZeniD).get(pJobID).get("" + pPID) != null) datas.get(pZeniD).get(pJobID).get("" + pPID).addInput(inputName, "" + inputPid, -1, preJob == null);
    }

    @Override
    public void finishJobInstance(String pZeniD, String pJobID, long pPID) {
    }

    @Override
    public void addInstanceJobPool(String pZeniD, String pJobID, long pPID, PSInstance pInstance) {
    }

    @Override
    public void finishWorkflow(String pZeniD) {
        Logger.getI().pool("jobinstance", "add workflow=\"" + pZeniD + "\"", datas.get(pZeniD).size());
        datas.remove(pZeniD);
    }

    @Override
    public Hashtable<String, PSInstance> getAllJobInstances(String pZenID, String pJobName) {
        return datas.get(pZenID).get(pJobName);
    }

    @Override
    public RunableInstanceBean runnableJob(String pZenID, String pJobID, long pPID) throws NullPointerException {
        if (datas.get(pZenID).get(pJobID).get("" + pPID) != null) {
            RunableInstanceBean res = new RunableInstanceBean();
            res.setJob(Base.getI().getZenRunner(pZenID).getJob(pJobID));
            res.setPid(pPID);
            return res;
        }
        throw new NullPointerException();
    }

    @Override
    public PSInstance getPSInstance(String pZenID, String pJobName, long pPID) {
        return datas.get(pZenID).get(pJobName).get("" + pPID);
    }

    @Override
    public int getManagedInstance(String pZenId) {
        int res = 0;
        Hashtable<String, Hashtable<String, PSInstance>> tmp = datas.get(pZenId);
        Enumeration<String> enm = tmp.keys();
        while (enm.hasMoreElements()) {
            res += tmp.get(enm.nextElement()).size();
        }
        return res;
    }

    @Override
    public long getInstanceCount(String pZenID, String pJobName) {
        return datas.get(pZenID).get(pJobName).size();
    }

    public long getOutputCounts(String pZenID, String pJobName, String pOutputName) {
        return 0;
    }

    @Override
    public long getStreamIndex(String pZenID, String pJobName, long pPID, String pOutputName, long pIndex) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Hashtable<Long, Long> getPSInstanceInPreJobInstance(String pZenID, String pJobName, String pOutputName, long pIndex) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void cleaning(String pZenID, String pJobName, long pPid) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public long getFirstSreamIndexForPID(String pZenID, String pJobName, String pOutputName, long pPid) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
