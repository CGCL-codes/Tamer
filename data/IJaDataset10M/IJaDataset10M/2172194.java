package hu.sztaki.lpds.wfs.com;

import java.util.Hashtable;

/**
 * @author krisztian
 */
public class JobStatusBean {

    private Hashtable data = new Hashtable();

    private Hashtable outputs = new Hashtable();

    /**
 * Class construktor 
 */
    public JobStatusBean() {
    }

    /**
 * Class construktor 
 * @param pPortalID   Portal azonosito
 * @param pUserID   Felhasznalo azonosito
 * @param pWorkflowID   Workflow azonosito
 * @param pWrtID    Workflow runtime ID
 * @param pJobID    Job azonosito
 * @param pPID  Job Parametrikus ID
 * @param pStatus   Status
 * @param pResource Eroforras
 * @param pTim  Status valtozas bekovetkezesi idobelyege
 * @param pWstatus  Workflow Status
 */
    public JobStatusBean(String pPortalID, String pUserID, String pWorkflowID, String pWrtID, String pJobID, int pPID, int pStatus, String pResource, long pTim, String pWstatus) {
        setWorkflowStatus(pWstatus);
        setPortalID(pPortalID);
        setUserID(pUserID);
        setWorkflowID(pWorkflowID);
        setJobID(pJobID);
        setStatus(pStatus);
        setResource(pResource);
        setTim(pTim);
        setWrtID(pWrtID);
        setPID(pPID);
    }

    /**
 * Class construktor
 * @param pPortalID   Portal azonosito
 * @param pUserID   Felhasznalo azonosito
 * @param pWorkflowID   Workflow azonosito
 * @param pWrtID    Workflow runtime ID
 * @param pJobID    Job azonosito
 * @param pPID  Job Parametrikus ID
 * @param pStatus   Status
 * @param pResource Eroforras
 * @param pTim  Status valtozas bekovetkezesi idobelyege
 * @param pWstatus  Workflow Status
 * @param pSubmitID Submitaciohoz kapcsolodo egyedi azonosito
 */
    public JobStatusBean(String pPortalID, String pUserID, String pWorkflowID, String pWrtID, String pJobID, int pPID, int pStatus, String pResource, long pTim, String pWstatus, long pSubmitID) {
        setWorkflowStatus(pWstatus);
        setPortalID(pPortalID);
        setUserID(pUserID);
        setWorkflowID(pWorkflowID);
        setJobID(pJobID);
        setStatus(pStatus);
        setResource(pResource);
        setTim(pTim);
        setWrtID(pWrtID);
        setPID(pPID);
        setWorkflowSubmitID(pSubmitID);
    }

    /**
 * Class construktor 
 * @param pWrtID    Workflow runtime ID
 * @param pJobID    Job azonosito
 * @param pPID  Job Parametrikus ID
 * @param pStatus   Status
 * @param pResource Eroforras
 * @param pTim  Status valtozas bekovetkezesi idobelyege
 */
    public JobStatusBean(String pWrtID, String pJobID, int pPID, int pStatus, String pResource, long pTim) {
        setJobID(pJobID);
        setStatus(pStatus);
        setResource(pResource);
        setTim(pTim);
        setWrtID(pWrtID);
        setPID(pPID);
    }

    /**
  * Visszaadja a Portal azonositojot
  * @return A Portal azonositoja
  * @see String
  */
    public String getPortalID() {
        return (String) data.get("portalID");
    }

    /**
  * Status valtozas idobelyeg lekerdezese
  * @return idobelyeg
  * @see String
  */
    public long getTim() {
        return (data.get("tim") == null) ? 0 : ((Long) data.get("tim")).longValue();
    }

    /**
  * Status valtozas idobelyeg lekerdezese
  * @return idobelyeg
  */
    public String getWorkflowStatus() {
        return (String) data.get("workflowStatus");
    }

    /**
  * Workflow runtime id lekerdezese
  * @return ID
  * @see String
  */
    public String getWrtID() {
        return (String) data.get("wrtID");
    }

    /**
  * Job parametrikus ID lekerdezese
  * @return PID
  * @see String
  */
    public long getPID() {
        return (data.get("pID") == null) ? 0 : ((Long) data.get("pID")).longValue();
    }

    /**
  * Workflow SubmitID lekerdezese
  * @return SubmitID
  * @see long
  */
    public long getWorkflowSubmitID() {
        return (data.get("submitID") == null) ? 0 : ((Long) data.get("submitID")).longValue();
    }

    /**
  * Visszaadja a Felhasznalo azonositojot
  * @return A Felhasznalo azonositoja
  * @see String
  */
    public String getUserID() {
        return (String) data.get("userID");
    }

    /**
  * Visszaadja a workflow azonositojot
  * @return A workflow azonositoja
  * @see String
  */
    public String getWorkflowID() {
        return (String) data.get("workflowID");
    }

    /**
  * Visszaadja a Job azonositojot
  * @return A Job azonositoja
  * @see String
  */
    public String getJobID() {
        return (String) data.get("jobID");
    }

    /**
  * Visszaadja a Job Statusat
  * @return A Job Statusa
  */
    public int getStatus() {
        return ((Integer) data.get("status")).intValue();
    }

    /**
  * Visszaadja a hasznalt Eroforrast
  * @return A hasznalt Eroforras
  * @see String
  */
    public String getResource() {
        return (String) data.get("resource");
    }

    /**
 * Beallitja a Portal azonositojot
 * @param value A Portal azonositoja
 * @see String
 */
    public void setPortalID(String value) {
        data.put("portalID", value);
    }

    /**
 * Beallitja a Felhasznalo azonositojot
 * @param value A Felhasznalo azonositoja
 * @see String
 */
    public void setUserID(String value) {
        data.put("userID", value);
    }

    /**
 * Beallitja a workflow azonositojot
 * @param value A workflow azonositoja
 * @see String
 */
    public void setWorkflowID(String value) {
        data.put("workflowID", value);
    }

    /**
 * Beallitja a Job azonositojot
 * @param value A Job azonositoja
 * @see String
 */
    public void setJobID(String value) {
        data.put("jobID", value);
    }

    /**
 * Beallitja a Job Statusat
 * @param value A Job Statusa
 */
    public void setStatus(int value) {
        data.put("status", value);
    }

    /**
 * Beallitja a Job Eroforrasat
 * @param value A Jobot futtato Eroforras
 * @see String
 */
    public void setResource(String value) {
        data.put("resource", value);
    }

    /**
 * Status valtozas idobelyeg beallitasa
 * @param value idobelyeg
 */
    public void setTim(long value) {
        data.put("tim", value);
    }

    /**
 * Workflow Status beallitasa
 * @param value Status
 * @see String
 */
    public void setWorkflowStatus(String value) {
        data.put("workflowStatus", value);
    }

    /**
 * Workflow runtime id beallitasa
 * @param value RTID
 * @see String
 */
    public void setWrtID(String value) {
        data.put("wrtID", value);
    }

    /**
 * Job parametrikus ID beallitasa
 * @param value PID
 */
    public void setPID(long value) {
        data.put("pID", new Long(value));
    }

    /**
 * Workflow Submit ID beallitasa
 * @param value PID
 */
    public void setWorkflowSubmitID(long value) {
        data.put("submitID", new Long(value));
    }

    /**
 * Uj output felvetel a generalt kimeneti listahoz
 * @param pOutput output port neve
 * @param pCount generalt outputok szama
 */
    public void addOutputCount(String pOutput, int pCount) {
        outputs.put(pOutput, new Integer(pCount));
    }

    /**
 * Output port tenyleges szamossaganak lekerdezese
 * @param pOutput port neve
 * @return tenylegesen generalt outputfile-ok szama
 */
    public int getOutputCount(String pOutput) {
        return ((Integer) outputs.get(pOutput)).intValue();
    }

    /**
 * Outputs setter
 * @param pData outputok es szamossaguk
 */
    public void setOutputs(Hashtable pData) {
        outputs = pData;
    }

    /**
 * Outputs getter
 * @return outputok es szamossaguk
 */
    public Hashtable getOutputs() {
        return outputs;
    }
}
