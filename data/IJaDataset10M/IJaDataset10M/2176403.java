package net.sf.snver.gui.riena.controller;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintStream;
import java.lang.reflect.InvocationTargetException;
import net.sf.snver.gui.riena.Application;
import net.sf.snver.gui.riena.NodeFactory;
import net.sf.snver.gui.riena.model.SnverVo;
import net.sf.snver.gui.riena.util.IJobChangeAdapter;
import net.sf.snver.gui.riena.util.SnverUIConstant;
import net.sf.snver.gui.riena.view.ResultView;
import net.sf.snver.pileup.IProcess;
import net.sf.snver.pileup.ISnverListener;
import net.sf.snver.pileup.SNVerIndividual;
import net.sf.snver.pileup.SNVerPool;
import net.sf.snver.pileup.SnverJob;
import net.sf.snver.pileup.meta.ProcessMessage;
import net.sf.snver.pileup.util.SnverFactory;
import org.eclipse.core.databinding.observable.IChangeListener;
import org.eclipse.core.databinding.observable.IDisposeListener;
import org.eclipse.core.databinding.observable.IStaleListener;
import org.eclipse.core.databinding.observable.Realm;
import org.eclipse.core.databinding.observable.value.AbstractObservableValue;
import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.core.databinding.observable.value.IValueChangeListener;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.IJobChangeEvent;
import org.eclipse.core.runtime.jobs.IJobChangeListener;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.riena.internal.ui.ridgets.swt.uiprocess.UIProcessRidget;
import org.eclipse.riena.navigation.ApplicationNodeManager;
import org.eclipse.riena.navigation.IApplicationNode;
import org.eclipse.riena.navigation.IModuleGroupNode;
import org.eclipse.riena.navigation.IModuleNode;
import org.eclipse.riena.navigation.ISubModuleNode;
import org.eclipse.riena.navigation.NavigationNodeId;
import org.eclipse.riena.navigation.ui.controllers.ApplicationController;
import org.eclipse.riena.navigation.ui.controllers.SubModuleController;
import org.eclipse.riena.ui.core.uiprocess.UIProcess;
import org.eclipse.riena.ui.ridgets.IActionListener;
import org.eclipse.riena.ui.ridgets.IActionRidget;
import org.eclipse.riena.ui.ridgets.ILabelRidget;
import org.eclipse.riena.ui.ridgets.IMessageBoxRidget;
import org.eclipse.riena.ui.ridgets.ITextRidget;
import org.eclipse.riena.ui.swt.uiprocess.UIProcessControl;
import org.eclipse.riena.ui.swt.uiprocess.UIProcessWindow;
import org.eclipse.riena.ui.swt.utils.UIControlsFactory;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.progress.IProgressConstants;

public class ConsoleController extends SubModuleController {

    ConsoleBean console;

    Job job;

    ApplicationController ac;

    boolean openResult = false;

    public ConsoleController(ISubModuleNode node) {
        super(node);
        console = new ConsoleBean();
    }

    @Override
    public void configureRidgets() {
        super.configureRidgets();
        ITextRidget text = getRidget(ITextRidget.class, "txt.console");
        text.bindToModel(console, "message");
        text.setOutputOnly(true);
        IActionRidget btn = getRidget(IActionRidget.class, "btn.cancel");
        btn.addListener(new IActionListener() {

            @Override
            public void callback() {
                if (job != null) {
                    job.cancel();
                    final IMessageBoxRidget messageBoxRidget = (IMessageBoxRidget) getRidget("messageBox");
                    messageBoxRidget.setType(IMessageBoxRidget.Type.ERROR);
                    messageBoxRidget.setTitle("Job Summary");
                    messageBoxRidget.setOptions(IMessageBoxRidget.OPTIONS_OK);
                    messageBoxRidget.setText("Terminate by user!");
                    messageBoxRidget.show();
                }
            }
        });
        ac = (ApplicationController) getNavigationNode().getParentOfType(IApplicationNode.class).getNavigationNodeController();
    }

    private void refresh() {
        updateAllRidgetsFromModel();
    }

    public void clear() {
        console.reset();
    }

    public void printLogFile(String path) {
        console.targetLog(path);
    }

    public void closeLog() {
        console.close();
    }

    public void runJob(final SnverVo bean, final int type) {
        printArgs(bean, type);
        job = new Job("") {

            SnverJob snver = null;

            @Override
            public IStatus run(final IProgressMonitor monitor) {
                try {
                    monitor.beginTask("snver", 100);
                    Display.getDefault().asyncExec(new Runnable() {

                        @Override
                        public void run() {
                            ac.getStatusline().info("Initialize the pileup task...");
                        }
                    });
                    snver = SnverFactory.createJob(type);
                    snver.addListener(new ISnverListener() {

                        @Override
                        public void callback(final Object event) {
                            if (event instanceof ProcessMessage) {
                                monitor.worked(1);
                                Display.getDefault().asyncExec(new Runnable() {

                                    @Override
                                    public void run() {
                                        ac.getStatusline().info(((ProcessMessage) event).getMessage());
                                    }
                                });
                            } else {
                                if (event == null) {
                                    console.setMessage("get a unknown internal error");
                                } else {
                                    console.setMessage(event.toString());
                                }
                                refresh();
                            }
                        }
                    });
                    if (!snver.doJob(bean.getArgments(type))) return Status.CANCEL_STATUS;
                    if (snver.isCancel()) {
                        openResult = false;
                    } else {
                        openResult = true;
                    }
                } finally {
                    job = null;
                    monitor.done();
                }
                return Status.OK_STATUS;
            }

            @Override
            protected void canceling() {
                if (this.snver != null) snver.cancel();
                super.canceling();
            }
        };
        job.addJobChangeListener(new IJobChangeAdapter() {

            @Override
            public void done(IJobChangeEvent event) {
                super.done(event);
                closeLog();
                if (event.getResult().isOK()) {
                    if (openResult) jump2Result(bean);
                } else {
                    final IMessageBoxRidget messageBoxRidget = (IMessageBoxRidget) getRidget("messageBox");
                    messageBoxRidget.setType(IMessageBoxRidget.Type.ERROR);
                    messageBoxRidget.setTitle("Problems Summary");
                    messageBoxRidget.setOptions(IMessageBoxRidget.OPTIONS_OK);
                    messageBoxRidget.setText("Error in the result!");
                    Display.getDefault().asyncExec(new Runnable() {

                        @Override
                        public void run() {
                            messageBoxRidget.show();
                        }
                    });
                }
            }
        });
        job.setProperty(UIProcess.PROPERTY_CONTEXT, getNavigationNode());
        job.schedule();
    }

    private void jump2Result(final SnverVo bean) {
        Display.getDefault().asyncExec(new Runnable() {

            @Override
            public void run() {
                IApplicationNode node = ApplicationNodeManager.getApplicationNode();
                IModuleGroupNode group = (IModuleGroupNode) node.findNode(new NavigationNodeId(Application.ID_GROUP_MBOXES));
                String title = "Result@" + bean.getDist();
                IModuleNode moduleAccount1 = NodeFactory.createModule(title, group);
                ISubModuleNode subNode = NodeFactory.createSubMobule("Inbox", moduleAccount1, ResultView.ID);
                subNode.setContext("target", bean.getDist());
                subNode.setContext("csv", bean.getDistWithoutPost() + ".filter.csv");
                node.jump(subNode.getNodeId());
            }
        });
    }

    private void printArgs(SnverVo bean, int type) {
        final String[] args = bean.getArgments(type);
        console.setMessage("-----------------argments list--------------------------\n");
        for (int i = 0; i < args.length; i += 2) {
            console.setMessage(args[i] + "\t" + args[i + 1] + "\n");
        }
        console.setMessage("\n");
        console.setMessage("------------------target----------------------------------\n");
        console.setMessage(bean.getDist() + "\n");
        console.setMessage("-------------------------------------------------------------\n");
    }

    private class ConsoleBean {

        String message = "";

        private FileWriter log = null;

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message += message;
            if (log != null) try {
                log.write(message);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        public void reset() {
            message = "";
        }

        public void targetLog(String path) {
            try {
                log = new FileWriter(new File(path));
            } catch (IOException e) {
                log = null;
            }
        }

        public void close() {
            if (log != null) try {
                log.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            log = null;
        }
    }

    private void updateStatusLine() {
        ApplicationController ac = (ApplicationController) getNavigationNode().getParentOfType(IApplicationNode.class).getNavigationNodeController();
    }
}
