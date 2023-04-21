package com.schwidder.petrinet.simulator.runtime;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.StringTokenizer;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;
import com.schwidder.nucleus.helper.interfaces.ITemplateStore;
import com.schwidder.nucleus.objects.interfaces.IAttributes;
import com.schwidder.nucleus.objects.interfaces.ICommandRequest;
import com.schwidder.nucleus.objects.interfaces.INet;
import com.schwidder.nucleus.objects.interfaces.INetGroup;
import com.schwidder.nucleus.runtime.BreakCommandImpl;
import com.schwidder.nucleus.runtime.CommandMode;
import com.schwidder.nucleus.runtime.QuitCommandImpl;
import com.schwidder.nucleus.runtime.RunCommandImpl;
import com.schwidder.nucleus.runtime.StepCommandImpl;
import com.schwidder.petrinet.parser.PTXMLParser;
import com.schwidder.petrinet.spring.parser.IPTSpringParser;

/**
 * @author kai@schwidder.com
 * @version 1.0
 */
public class PTSimulator {

    protected ArrayList<INetGroup> netGroups;

    public PTSimulator() {
        netGroups = new ArrayList<INetGroup>();
    }

    public void setBreak(INetGroup pApp) {
        INet aNet;
        Iterator aIndex;
        ArrayList<INet> objects = pApp.getNets();
        for (aIndex = objects.iterator(); aIndex.hasNext(); ) {
            aNet = (INet) aIndex.next();
            aNet.pushCommand(new BreakCommandImpl());
        }
    }

    private void breakPointNet(ICommandRequest aCmd) {
        INet aNet = getNet(aCmd);
        if (aNet != null) {
            StringTokenizer tokens = new StringTokenizer((String) ((HashMap) aCmd.getAttributes()).get("object"), ".");
            Object aObj = aNet.find(tokens, null);
            if (aObj != null) ((HashMap) ((IAttributes) aObj).getAttributes()).put("breakpoint", true);
        }
    }

    public void loadSpring(ICommandRequest aCmd) {
        HashMap aMap;
        INetGroup pApp = null;
        ITemplateStore aStore;
        ApplicationContext ctx;
        String filename = (String) ((HashMap) aCmd.getAttributes()).get("filename");
        ctx = new FileSystemXmlApplicationContext(filename);
        IPTSpringParser g = (IPTSpringParser) ctx.getBean("parserDemo");
        g.setCtx(ctx);
        try {
            pApp = g.start();
            if (pApp.isEmpty()) pApp.registerNet(g.startNet(pApp));
            aMap = (HashMap) pApp.getAttributes();
            aMap.put("spring.ctx", ctx);
            aStore = g.getTemplateStore();
            aStore = (ITemplateStore) aStore.get(pApp);
            if (pApp.getLazyLoad()) pApp.setTemplateStore(aStore);
            setBreak(pApp);
            netGroups.add(pApp);
        } catch (Exception e) {
        }
        Thread aThread = new Thread(new PTSimulatorNetGroup(pApp));
        aThread.start();
    }

    public void load(ICommandRequest aCmd) {
        ITemplateStore aStore;
        String filename = (String) ((HashMap) aCmd.getAttributes()).get("filename");
        PTXMLParser g = new PTXMLParser(filename);
        try {
            INetGroup pApp = null;
            pApp = g.start();
            if (pApp.isEmpty()) pApp.registerNet(g.startNet(pApp));
            aStore = g.getTemplateStore();
            aStore = (ITemplateStore) aStore.get(pApp);
            if (pApp.getLazyLoad()) pApp.setTemplateStore(aStore);
            netGroups.add(pApp);
            setBreak(pApp);
            Thread aThread = new Thread(new PTSimulatorNetGroup(pApp));
            aThread.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public ICommandRequest readCommand() {
        ICommandRequest aCmd = null;
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
            String input = "";
            input = in.readLine();
            if ("".equals(input)) {
            } else if ("lng".equals(input)) {
                aCmd = new ListNetGroupCommand();
            } else if ("ln".equals(input)) {
                System.out.println("Enter index of NetGroup : ");
                input = in.readLine();
                Integer netGroupIndex = new Integer(input);
                aCmd = new ListNetCommand(netGroupIndex);
            } else if ("load".equals(input)) {
                System.out.println("Enter filename : ");
                input = in.readLine();
                aCmd = new LoadCommand(input);
            } else if ("loadspring".equals(input)) {
                System.out.println("Enter filename : ");
                input = in.readLine();
                aCmd = new LoadSpringCommand(input);
            } else if ("run".equals(input)) {
                System.out.println("Enter index of NetGroup : ");
                input = in.readLine();
                Integer netGroupIndex = new Integer(input);
                System.out.println("Enter index of Net : ");
                input = in.readLine();
                Integer netIndex = new Integer(input);
                aCmd = new RunNetCommand(netGroupIndex, netIndex);
            } else if ("break".equals(input)) {
                System.out.println("Enter index of NetGroup : ");
                input = in.readLine();
                Integer netGroupIndex = new Integer(input);
                System.out.println("Enter index of Net : ");
                input = in.readLine();
                Integer netIndex = new Integer(input);
                aCmd = new BreakNetCommand(netGroupIndex, netIndex);
            } else if ("step".equals(input)) {
                System.out.println("Enter index of NetGroup : ");
                input = in.readLine();
                Integer netGroupIndex = new Integer(input);
                System.out.println("Enter index of Net : ");
                input = in.readLine();
                Integer netIndex = new Integer(input);
                aCmd = new StepNetCommand(netGroupIndex, netIndex);
            } else if ("quit".equals(input)) {
                System.out.println("Enter index of NetGroup : ");
                input = in.readLine();
                Integer netGroupIndex = new Integer(input);
                System.out.println("Enter index of Net : ");
                input = in.readLine();
                Integer netIndex = new Integer(input);
                aCmd = new QuitNetCommand(netGroupIndex, netIndex);
            } else if ("exit".equals(input)) {
                aCmd = new ExitCommand();
            } else if ("breakpoint".equals(input)) {
                System.out.println("Enter index of NetGroup : ");
                input = in.readLine();
                Integer netGroupIndex = new Integer(input);
                System.out.println("Enter index of Net : ");
                input = in.readLine();
                Integer netIndex = new Integer(input);
                System.out.println("Enter object : ");
                input = in.readLine();
                String object = new String(input);
                aCmd = new BreakPointCommand(netGroupIndex, netIndex, object);
            }
        } catch (Exception e) {
        }
        return aCmd;
    }

    public boolean execute(ICommandRequest aCmd) {
        boolean cmdDone = false;
        if (aCmd == null) return false;
        switch(aCmd.getType()) {
            case CommandMode.RUN:
                runNet(aCmd);
                cmdDone = true;
                break;
            case CommandMode.BREAK:
                breakNet(aCmd);
                cmdDone = true;
                break;
            case CommandMode.BREAKPOINT:
                breakPointNet(aCmd);
                cmdDone = true;
                break;
            case CommandMode.STEP:
                stepNet(aCmd);
                cmdDone = true;
                break;
            case CommandMode.QUIT:
                quitNet(aCmd);
                cmdDone = true;
                break;
            case CommandMode.LOAD:
                load(aCmd);
                cmdDone = true;
                break;
            case CommandMode.LOADSPRING:
                loadSpring(aCmd);
                cmdDone = true;
                break;
            case CommandMode.LISTNETGROUP:
                listNetGroup(aCmd);
                cmdDone = true;
                break;
            case CommandMode.LISTNET:
                listNet(aCmd);
                cmdDone = true;
                break;
            case CommandMode.EXIT:
                exitCommand();
                return true;
        }
        return false;
    }

    private void exitCommand() {
        INetGroup aGroup = null;
        Iterator aIndex;
        if (netGroups.isEmpty()) return;
        for (aIndex = netGroups.iterator(); aIndex.hasNext(); ) {
            INetGroup aObj = (INetGroup) aIndex.next();
            Iterator aIndex2;
            ArrayList<INet> objects = aObj.getNets();
            for (aIndex2 = objects.iterator(); aIndex2.hasNext(); ) {
                INet aNet = (INet) aIndex2.next();
                aNet.pushCommand(new QuitCommandImpl());
            }
        }
    }

    private void quitNet(ICommandRequest aCmd) {
        INet aNet = getNet(aCmd);
        if (aNet != null) aNet.pushCommand(new QuitCommandImpl());
    }

    private void stepNet(ICommandRequest aCmd) {
        INet aNet = getNet(aCmd);
        if (aNet != null) aNet.pushCommand(new StepCommandImpl());
    }

    private void breakNet(ICommandRequest aCmd) {
        INet aNet = getNet(aCmd);
        if (aNet != null) aNet.pushCommand(new BreakCommandImpl());
    }

    private void runNet(ICommandRequest aCmd) {
        INet aNet = getNet(aCmd);
        if (aNet != null) aNet.pushCommand(new RunCommandImpl());
    }

    public INet getNet(ICommandRequest aCmd) {
        String netGroupName = (String) ((HashMap) aCmd.getAttributes()).get("netgroup");
        Integer indexNetGroup = (Integer) ((HashMap) aCmd.getAttributes()).get("netgroup_index");
        String netName = (String) ((HashMap) aCmd.getAttributes()).get("net");
        Integer indexNet = (Integer) ((HashMap) aCmd.getAttributes()).get("net_index");
        INetGroup aGroup = getGroup(netGroupName, indexNetGroup);
        if (aGroup == null) return null;
        INet aNet = getNet(aGroup, netName, indexNet);
        return aNet;
    }

    public INet getNet(INetGroup aGroup, Integer aIndex) {
        try {
            return aGroup.getNets().get(aIndex);
        } catch (Exception e) {
            return null;
        }
    }

    public INet getNet(INetGroup aGroup, String aName) {
        INet aNet = null;
        Iterator aIndex;
        if (aGroup.getNets().isEmpty()) return null;
        for (aIndex = aGroup.getNets().iterator(); aIndex.hasNext(); ) {
            INet aObj = (INet) aIndex.next();
            if (aObj.getIdentifier().equals(aName)) {
                aNet = aObj;
                break;
            }
        }
        return aNet;
    }

    private INet getNet(INetGroup aGroup, String aNetName, Integer index) {
        Iterator aIndex;
        INet aNet = null;
        INet aNet2 = null;
        if (aNetName == null && index == null) return null; else if (aNetName == null && index != null) {
            try {
                return getNet(aGroup, index);
            } catch (Exception e) {
                return null;
            }
        } else if (index == null && (aNet = getNet(aGroup, aNetName)) == null) return null; else if (index != null && (aNet2 = getNet(aGroup, aNetName)) != null) {
            try {
                if (!(aNet = (INet) aGroup.getNets().get(index)).getIdentifier().equals(aNet2.getIdentifier())) return null;
            } catch (Exception e) {
                return null;
            }
        }
        return aNet;
    }

    public INetGroup getGroup(Integer aIndex) {
        try {
            return netGroups.get(aIndex);
        } catch (Exception e) {
            return null;
        }
    }

    public INetGroup getGroup(String aName) {
        INetGroup aGroup = null;
        Iterator aIndex;
        if (netGroups.isEmpty()) return null;
        for (aIndex = netGroups.iterator(); aIndex.hasNext(); ) {
            INetGroup aObj = (INetGroup) aIndex.next();
            if (aObj.getIdentifier().equals(aName)) {
                aGroup = aObj;
                break;
            }
        }
        return aGroup;
    }

    private INetGroup getGroup(String netGroupName, Integer index) {
        INetGroup aGroup = null;
        INetGroup aGroup2 = null;
        if (netGroupName == null && index == null) return null; else if (netGroupName == null && index != null) {
            try {
                return getGroup(index);
            } catch (Exception e) {
                return null;
            }
        } else if (index == null && (aGroup = getGroup(netGroupName)) == null) return null; else if (index != null && (aGroup2 = getGroup(netGroupName)) != null) {
            try {
                if (!(aGroup = (INetGroup) netGroups.get(index)).getIdentifier().equals(aGroup2.getIdentifier())) return null;
            } catch (Exception e) {
                return null;
            }
        }
        return aGroup;
    }

    private void listNet(ICommandRequest aCmd) {
        INetGroup aGroup = null;
        INetGroup aGroup2 = null;
        Iterator aIndex;
        String netGroupName = (String) ((HashMap) aCmd.getAttributes()).get("netgroup");
        Integer index = (Integer) ((HashMap) aCmd.getAttributes()).get("netgroup_index");
        if ((aGroup = getGroup(netGroupName, index)) == null) return;
        for (aIndex = aGroup.getNets().iterator(); aIndex.hasNext(); ) {
            INet aNet = (INet) aIndex.next();
            System.out.println("Net : " + aGroup.getIdentifier() + "(" + netGroups.indexOf(aGroup) + ")" + "." + aNet.getIdentifier() + "(" + aGroup.getNets().indexOf(aNet) + ")");
        }
    }

    private void listNetGroup(ICommandRequest aCmd) {
        Iterator aIndex;
        String netGroupName = (String) ((HashMap) aCmd.getAttributes()).get("netgroup");
        if (netGroups.isEmpty()) {
            System.out.println("No Groups");
            return;
        }
        for (aIndex = netGroups.iterator(); aIndex.hasNext(); ) {
            INetGroup aGroup = (INetGroup) aIndex.next();
            if (netGroupName != null) {
                if (netGroupName.equals(aGroup.getIdentifier())) System.out.println("NetGroup : " + aGroup.getIdentifier() + "(" + netGroups.indexOf(aGroup) + ")");
            } else System.out.println("NetGroup : " + aGroup.getIdentifier() + "(" + netGroups.indexOf(aGroup) + ")");
        }
    }
}
