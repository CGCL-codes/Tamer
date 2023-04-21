package org.jecars.tools.workflow;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Future;
import java.util.logging.Level;
import javax.jcr.*;
import javax.security.auth.login.CredentialExpiredException;
import org.jecars.CARS_Main;
import org.jecars.tools.CARS_FileClassLoader;
import org.jecars.tools.CARS_ToolInterface;
import org.jecars.tools.CARS_ToolsFactory;
import org.jecars.wfplugin.IWFP_Interface;
import org.jecars.wfplugin.WFP_Context;
import org.jecars.wfplugin.WFP_Tool;

/**
 *
 */
public class WF_WorkflowRunner extends WF_Default implements IWF_WorkflowRunner {

    public static final IWF_WorkflowRunner NULL = new WF_WorkflowRunner(null, null);

    private final CARS_Main mMain;

    /** WF_WorkflowRunner
   * 
   * @param pMain
   * @param pNode 
   */
    public WF_WorkflowRunner(final CARS_Main pMain, final Node pNode) {
        super(pNode);
        if (pMain != null) {
            CARS_Main main = null;
            try {
                main = pMain.getFactory().createMain(pMain.getContext());
            } catch (AccessDeniedException ae) {
                WF_Default.LOG.log(Level.WARNING, ae.getMessage(), ae);
            } catch (CredentialExpiredException ce) {
                WF_Default.LOG.log(Level.WARNING, ce.getMessage(), ce);
            } finally {
                mMain = main;
            }
        } else {
            mMain = null;
        }
        return;
    }

    @Override
    public CARS_Main getMain() {
        return mMain;
    }

    /** getWorkflow
   * 
   * @return
   * @throws RepositoryException 
   */
    @Override
    public IWF_Workflow getWorkflow() throws RepositoryException {
        return new WF_Workflow(getNode().getParent().getParent());
    }

    @Override
    public IWF_Context getContext() throws RepositoryException {
        return new WF_Context(getNode().getNode("context"));
    }

    @Override
    public void restart() throws RepositoryException {
        getNode().setProperty("jecars:SingleStep", 0);
        setCurrentTask("");
        setCurrentLink("");
        removeTools();
        save();
        getContext().restore(0);
        removeContexts();
        save();
        return;
    }

    private void removeContexts() throws RepositoryException {
        final NodeIterator ni = getNode().getNodes();
        while (ni.hasNext()) {
            Node ctx = ni.nextNode();
            if (ctx.getName().startsWith("context_")) {
                ctx.remove();
            }
        }
        return;
    }

    private void removeTools() throws RepositoryException {
        final NodeIterator ni = getNode().getNodes();
        while (ni.hasNext()) {
            Node tool = ni.nextNode();
            if (tool.isNodeType("jecars:Tool")) {
                tool.remove();
            }
        }
        return;
    }

    @Override
    public boolean singleStep() throws Exception {
        System.out.println("single step " + getNode().getPath());
        final boolean stillRunning = nextStep();
        save();
        return stillRunning;
    }

    @Override
    public void setCurrentTask(final String pCurrentTask) throws RepositoryException {
        getNode().setProperty("jecars:currentTask", pCurrentTask);
        return;
    }

    @Override
    public void setCurrentLink(final String pCurrentLink) throws RepositoryException {
        getNode().setProperty("jecars:currentLink", pCurrentLink);
        return;
    }

    /** getCurrentTask
   * 
   * @return
   * @throws RepositoryException 
   */
    public IWF_Task getCurrentTask() throws RepositoryException {
        if (getNode().hasProperty("jecars:currentTask")) {
            String ctp = getNode().getProperty("jecars:currentTask").getString();
            if ("".equals(ctp)) {
                return WF_Task.NULL;
            }
            return new WF_Task(getNode().getSession().getNode(ctp));
        } else {
            setCurrentTask("");
            save();
        }
        return WF_Task.NULL;
    }

    public IWF_Link getCurrentLink() throws RepositoryException {
        if (getNode().hasProperty("jecars:currentLink")) {
            String ctp = getNode().getProperty("jecars:currentLink").getString();
            if ("".equals(ctp)) {
                return WF_Link.NULL;
            }
            return new WF_Link(getNode().getSession().getNode(ctp));
        } else {
            setCurrentLink("");
            save();
        }
        return WF_Link.NULL;
    }

    public long getStepNumber() throws RepositoryException {
        return getNode().getProperty("jecars:SingleStep").getLong();
    }

    /** nextStep
   * 
   * @throws RepositoryException 
   */
    private boolean nextStep() throws Exception {
        boolean stillRunning = true;
        final IWF_Workflow workflow = getWorkflow();
        final IWF_Task currentTask = getCurrentTask();
        final IWF_Link currentLink = getCurrentLink();
        if (currentTask.isNULL() && currentLink.isNULL()) {
            final List<IWF_Task> tasks = workflow.getTaskByType(EWF_TaskType.START);
            final IWF_Task starttask = tasks.get(0);
            setCurrentTask(starttask.getNode().getPath());
            setCurrentLink("");
            backupContext();
        } else {
            if (currentTask.isNULL()) {
                getContext().setUsedLink(currentLink);
                save();
                backupContext();
                final IWF_Task toTask = currentLink.getToEndPoint().getEndPoint();
                setCurrentTask(toTask.getNode().getPath());
                setCurrentLink("");
                final List<IWF_LinkEndPoint> leps = new ArrayList<IWF_LinkEndPoint>();
                leps.add(currentLink.getFromEndPoint());
                getContext().filter(leps);
                final List<IWF_LinkEndPoint> oleps = new ArrayList<IWF_LinkEndPoint>();
                oleps.add(currentLink.getToEndPoint());
                getContext().convertTo(oleps);
            } else {
                getContext().setUsedTask(currentTask);
                runTask(currentTask);
                final List<IWF_Link> links = workflow.getFromLinkByTask(currentTask);
                if (links.isEmpty()) {
                    stillRunning = false;
                } else {
                    boolean first = true;
                    for (IWF_Link link : links) {
                        if (first) {
                            final IWF_Task toTask = link.getToEndPoint().getEndPoint();
                            setCurrentTask("");
                            setCurrentLink(link.getNode().getPath());
                        } else {
                            IWF_WorkflowRunner newwr = getWorkflow().createRunner(this, currentTask);
                            newwr.setCurrentTask("");
                            newwr.setCurrentLink(link.getNode().getPath());
                            newwr.getContext().copyFrom(getContext());
                            save();
                        }
                        first = false;
                    }
                }
            }
        }
        getNode().setProperty("jecars:SingleStep", getStepNumber() + 1);
        return stillRunning;
    }

    /** backupContext
   * 
   * @throws RepositoryException 
   */
    private void backupContext() throws RepositoryException {
        final Workspace ws = getNode().getSession().getWorkspace();
        final String newws = getNode().getPath() + "/context_" + getStepNumber();
        if (getNode().getSession().nodeExists(newws)) {
            getNode().getSession().getNode(newws).remove();
            save();
        }
        ws.copy(ws.getName(), getContext().getNode().getPath(), newws);
        save();
        return;
    }

    /** searchForIWFP_Interface
   * 
   * @param pNode
   * @return
   * @throws RepositoryException
   * @throws IOException 
   */
    private IWFP_Interface searchForIWFP_Interface(final Node pNode) throws RepositoryException, IOException {
        IWFP_Interface result = null;
        NodeIterator ni = pNode.getNodes();
        while (ni.hasNext()) {
            Node n = ni.nextNode();
            if (n.getName().endsWith(".class")) {
                final InputStream nis = n.getProperty("jcr:data").getBinary().getStream();
                try {
                    CARS_FileClassLoader fcl = new CARS_FileClassLoader(getClass().getClassLoader());
                    try {
                        Class c = fcl.createClass(nis);
                        for (Class ic : c.getInterfaces()) {
                            if ("org.jecars.wfplugin.IWFP_Interface".equals(ic.getName())) {
                                result = (IWFP_Interface) c.newInstance();
                            }
                        }
                    } catch (Throwable t) {
                        t.printStackTrace();
                        throw new RepositoryException(t);
                    }
                } finally {
                    nis.close();
                }
            } else {
                result = searchForIWFP_Interface(n);
            }
            if (result != null) {
                break;
            }
        }
        return result;
    }

    /** runJavaTask
   * 
   * @param pTask
   * @throws RepositoryException
   * @throws IOException 
   */
    private void runJavaTask(IWF_Task pTask) throws RepositoryException, IOException {
        Node dataNode = pTask.getNode().getNode("data");
        IWFP_Interface iface = searchForIWFP_Interface(dataNode);
        final WFP_Tool tool = new WFP_Tool();
        final WFP_Context context = new WFP_Context(getContext());
        iface.start(tool, context);
        return;
    }

    /** runTask
   * 
   * @param pTask
   * @throws Exception 
   */
    private void runTask(IWF_Task pTask) throws Exception {
        save();
        backupContext();
        switch(pTask.getType()) {
            case JAVATASK:
                {
                    runJavaTask(pTask);
                    break;
                }
            case END:
                {
                    final List<Node> nl = getContext().getDataNodes();
                    final Node wfNode = getWorkflow().getNode();
                    for (final Node dataNode : nl) {
                        try {
                            wfNode.getSession().move(dataNode.getPath(), wfNode.getPath() + "/" + dataNode.getName());
                            Node n = wfNode.getNode(dataNode.getName());
                            if (!n.isNodeType("jecars:mix_outputresource")) {
                                n.addMixin("jecars:mix_outputresource");
                            }
                        } catch (ItemExistsException ie) {
                        }
                    }
                    save();
                    break;
                }
            case WORKFLOW:
                {
                    Node ttn = pTask.getToolTemplateNode();
                    final WF_Workflow wf = new WF_Workflow(ttn);
                    final WF_Workflow newWF = wf.copyTo(getNode(), "Workflow_" + getStepNumber());
                    save();
                    final Node n = newWF.getNode();
                    final String toolPath;
                    {
                        final CARS_ToolInterface ti = CARS_ToolsFactory.getTool(mMain, n, null);
                        final Node toolNode = ti.getTool();
                        toolPath = toolNode.getPath();
                        final List<Node> nl = getContext().getDataNodes();
                        for (final Node dataNode : nl) {
                            toolNode.getSession().move(dataNode.getPath(), toolNode.getPath() + "/" + dataNode.getName());
                        }
                        toolNode.getSession().save();
                        ti.setStateRequest(CARS_ToolInterface.STATEREQUEST_START);
                        System.out.println("waiting for ending");
                        Future res = ti.getFuture();
                        while (!res.isDone()) {
                            Thread.sleep(1000);
                        }
                        System.out.println("task ended");
                    }
                    IWF_Context context = getContext();
                    context.clear();
                    final Node thisTool = getNode().getSession().getNode(toolPath);
                    final Workspace ws = getNode().getSession().getWorkspace();
                    final String toPath = context.getNode().getPath();
                    final NodeIterator ni = thisTool.getNodes();
                    while (ni.hasNext()) {
                        final Node tnode = ni.nextNode();
                        Node nn = context.getNode().addNode(tnode.getName(), "jecars:root");
                        nn.addMixin("jecars:mix_link");
                        nn.setProperty("jecars:Link", tnode.getPath());
                    }
                    save();
                    break;
                }
            case TASK:
                {
                    Node ttn = pTask.getToolTemplateNode();
                    Node tool = getNode().addNode("Tool_" + getStepNumber(), "jecars:Tool");
                    tool.setProperty("jecars:ToolTemplate", ttn.getPath());
                    tool.addMixin("jecars:interfaceclass");
                    tool.setProperty("jecars:InterfaceClass", "org.jecars.tools.CARS_ToolInterfaceApp");
                    save();
                    final Node n = mMain.getSession().getNode(tool.getPath());
                    final String toolPath;
                    {
                        final CARS_ToolInterface ti = CARS_ToolsFactory.getTool(mMain, n, null);
                        final Node toolNode = ti.getTool();
                        toolPath = toolNode.getPath();
                        final List<Node> nl = getContext().getDataNodes();
                        for (final Node dataNode : nl) {
                            toolNode.getSession().move(dataNode.getPath(), toolNode.getPath() + "/" + dataNode.getName());
                        }
                        toolNode.getSession().save();
                        ti.setStateRequest(CARS_ToolInterface.STATEREQUEST_START);
                        System.out.println("waiting for ending");
                        Future res = ti.getFuture();
                        while (!res.isDone()) {
                            Thread.sleep(1000);
                        }
                        System.out.println("task ended");
                    }
                    IWF_Context context = getContext();
                    context.clear();
                    final Node thisTool = getNode().getSession().getNode(toolPath);
                    final Workspace ws = getNode().getSession().getWorkspace();
                    final String toPath = context.getNode().getPath();
                    final NodeIterator ni = thisTool.getNodes();
                    while (ni.hasNext()) {
                        final Node tnode = ni.nextNode();
                        Node nn = context.getNode().addNode(tnode.getName(), "jecars:root");
                        nn.addMixin("jecars:mix_link");
                        nn.setProperty("jecars:Link", tnode.getPath());
                    }
                    save();
                    break;
                }
        }
        return;
    }

    @Override
    public boolean isNULL() {
        return this == NULL;
    }
}
