package fi.hip.gb.portlet.jobs.agent;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.Set;
import fi.hip.gb.core.AgentPacket;
import fi.hip.gb.core.Config;
import fi.hip.gb.core.WorkDescription;
import fi.hip.gb.mobile.Job;
import fi.hip.gb.mobile.Processor;
import fi.hip.gb.net.Service;
import fi.hip.gb.net.SessionFactory;
import fi.hip.gb.portlet.jobs.FlagDescription;
import fi.hip.gb.server.scheduler.DefaultScheduler;
import fi.hip.gb.utils.ClassLoaderUtils;

/**
 * Grid Blocks Agent implemention of the Job interface
 *
 * @author Antti Ahvenlampi
 * @version $Id: AgentJob.java 1074 2006-06-07 12:20:46Z jkarppin $
 */
public class AgentJob implements fi.hip.gb.portlet.jobs.Job {

    private WorkDescription wds;

    private List supportedFlags;

    private List supportedParameters;

    /**
	 * @param agent
	 */
    public AgentJob(AgentPacket agent) {
        wds = new WorkDescription(Config.getInstance().getServiceURL());
        wds.getExecutable().setClassName(agent.getServiceClass());
        wds.getInfo().setJobName("");
        try {
            ClassLoaderUtils clu = new ClassLoaderUtils(this, agent.getServers()[0].jars(), Config.getWorkingDir(null));
            Job job = (Job) clu.loadInstance(agent.getServiceClass());
            Processor pr = job.getProcessor();
            Object[][] flags = pr.getSupportedFlags();
            String[] params = pr.getSupportedParameters();
            supportedFlags = new ArrayList();
            for (int i = 0; i < flags.length; i++) {
                String name = (String) flags[i][0];
                String[] examples = (String[]) flags[i][1];
                String description = (String) flags[i][2];
                supportedFlags.add(new FlagDescription(name, examples, description));
            }
            flags = DefaultScheduler.getSupportedFlags();
            for (int i = 0; i < flags.length; i++) {
                String name = (String) flags[i][0];
                String[] examples = (String[]) flags[i][1];
                String description = (String) flags[i][2];
                supportedFlags.add(new FlagDescription(name, examples, description));
            }
            supportedParameters = new ArrayList();
            for (int i = 0; i < params.length; i++) {
                String description = params[i];
                supportedParameters.add(new String(description));
            }
            String[] jarURLs = agent.getServers()[0].getJars();
            for (int i = 0; i < jarURLs.length; i++) wds.getExecutable().attachFile(new URL("jar:" + jarURLs[i] + "!/"));
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (RemoteException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean submit() {
        if (wds.getInfo().getJobName() == null || wds.getInfo().getJobName().equals("")) wds.getInfo().setJobName("unnamed");
        try {
            Service service = SessionFactory.createClientConnection(Config.getInstance().getServiceURL(), Config.getWorkingDir(wds.getCurrentID()));
            service.dispatch(wds);
        } catch (RemoteException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public String getName() {
        return wds.getInfo().getJobName();
    }

    public void setName(String name) {
        wds.getInfo().setJobName(name);
    }

    public void addParameter(String param) {
        wds.getExecutable().getParameters().addElement(param);
    }

    /**
	 * @see fi.hip.gb.portlet.jobs.Job#getParameters()
	 */
    public List getParameters() {
        return wds.getExecutable().getParameters();
    }

    public void setFlagValue(String name, String value) {
        boolean found = false;
        Iterator it = supportedFlags.iterator();
        while (it.hasNext() && !found) {
            if (name.equals(((FlagDescription) it.next()).getName())) found = true;
        }
        if (!found) return;
        Properties p = wds.getExecutable().getFlags();
        if (p == null) {
            wds.getExecutable().setFlags(new Properties());
            p = wds.getExecutable().getFlags();
        }
        p.put(name, value);
    }

    public void removeFlag(String name) {
        Properties p = wds.getExecutable().getFlags();
        if (p == null) return;
        p.remove(name.toLowerCase());
    }

    public String getFlagValue(String name) {
        Properties p = wds.getExecutable().getFlags();
        if (p == null) return null;
        return (String) p.get(name);
    }

    public Set getFlags() {
        Properties p = wds.getExecutable().getFlags();
        if (p == null) return null;
        return p.keySet();
    }

    public List getSupportedFlags() {
        return supportedFlags;
    }

    public FlagDescription getFlagDescription(String name) {
        Iterator it = getSupportedFlags().iterator();
        while (it.hasNext()) {
            FlagDescription fd = (FlagDescription) it.next();
            if (name.equalsIgnoreCase(fd.getName())) return fd;
        }
        return null;
    }

    public List getSupportedParameters() {
        return supportedParameters;
    }
}
