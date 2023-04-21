package com.nhncorp.cubridqa.schedule.composite;

import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import org.eclipse.jface.viewers.CheckStateChangedEvent;
import org.eclipse.jface.viewers.CheckboxTreeViewer;
import org.eclipse.jface.viewers.ICheckStateListener;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.LabelProviderChangedEvent;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import com.nhncorp.cubridqa.replication.ParametersForRun;
import com.nhncorp.cubridqa.replication.compare.ScriptRunMode;
import com.nhncorp.cubridqa.replication.composite.ScriptListSelectionDialog.CaseDirFile;
import com.nhncorp.cubridqa.replication.groovy.CubridGroovy;
import com.nhncorp.cubridqa.replication.parameters.ReplicationParameters;
import com.nhncorp.cubridqa.replication.parameters.Replications;
import com.nhncorp.cubridqa.replication.parameters.bo.GroovyBO;
import com.nhncorp.cubridqa.replication.parameters.bo.ParametersBO;

/**
 *
 * A composite is used to select GROOVY scenarios for replication schedule test.
 * @ClassName: GroovyComposite
 * @date 2009-9-7
 * @version V1.0 Copyright (C) www.nhn.com
 */
public class GroovyComposite extends Composite {

    private boolean use;

    private Button useButton;

    private GridData data = new GridData();

    private Set<String> masterDbIpSet = new HashSet();

    private Map<Button, ParametersForRun> checkMap = new HashMap();

    private Map<String, Button> buttonMap = new HashMap();

    private boolean select;

    private Button replicationCompare;

    private Button performanceTest;

    private Group group;

    private boolean dropCreatedClassSelect;

    private Button dropCreatedClass;

    private boolean justDropSelect;

    private Button justDrop;

    private boolean testAllSelection = true;

    private Button testAll;

    private CaseDirFile caseDirFile;

    private boolean showCaseDirFile = true;

    final CheckboxTreeViewer tv;

    /**
	 * get the groovy configuration from the composite.
	 * 
	 * @return
	 */
    public CubridGroovy getCubridGroovy() {
        CubridGroovy cubridGroovy = null;
        if (use) {
            cubridGroovy = new CubridGroovy();
            List filePathList = getFilePathList();
            if (filePathList.size() == 0) return null;
            cubridGroovy.setScriptFilePaths(filePathList);
            cubridGroovy.setNeedCompare(true);
            cubridGroovy.setNeedDrop(true);
            cubridGroovy.setRunMode(ScriptRunMode.RUN_ALL);
        }
        return cubridGroovy;
    }

    public void loadData(CubridGroovy cubridGroovy) {
        if (cubridGroovy == null) {
            return;
        }
        setTreeCheckedFilePaths(cubridGroovy.getScriptFilePaths());
        setupUsedState();
    }

    /**
	 * set the checked node by pathList.
	 * 
	 * @param pathList
	 */
    private void setTreeCheckedFilePaths(List pathList) {
        for (Object path : pathList) {
            tv.setChecked(new File((String) path), true);
        }
    }

    /**
	 * set the ips data checked .
	 * 
	 * @param targetsIps
	 */
    private void setTargetsIpData(List targetsIps) {
        if (targetsIps != null && targetsIps.size() != 0) {
            for (Object param : targetsIps) {
                if (buttonMap.containsKey(((ParametersForRun) param).getIp() + ((ParametersForRun) param).getDbname())) buttonMap.get(((ParametersForRun) param).getIp() + ((ParametersForRun) param).getDbname()).setSelection(true);
            }
        }
    }

    /**
	 * 
	 * 
	 * @return List
	 */
    private List<ParametersForRun> getRunTargets() {
        List<ParametersForRun> result = new ArrayList();
        Iterator it = checkMap.entrySet().iterator();
        while (it.hasNext()) {
            Entry entry = (Entry) it.next();
            if (((Button) entry.getKey()).getSelection()) {
                result.add((ParametersForRun) entry.getValue());
            }
        }
        return result;
    }

    /**
	 * 
	 * 
	 * @return List
	 */
    private List getFilePathList() {
        Object[] dirs = tv.getCheckedElements();
        List resultList = new ArrayList();
        Set res = new HashSet();
        if (dirs == null || dirs.length == 0) return resultList;
        for (Object obj : dirs) {
            if (GroovyBO.getGroovyPath().indexOf(((File) obj).getPath()) >= 0) continue;
            resultList.add(((File) obj).getPath());
        }
        return resultList;
    }

    public GroovyComposite(Composite parent, int style) {
        super(parent, style);
        setLayout(new GridLayout());
        data.horizontalIndent = 0;
        group = new Group(this, SWT.NONE);
        group.setText("Groovy");
        group.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false));
        final GridLayout gridLayout = new GridLayout();
        gridLayout.numColumns = 4;
        group.setLayout(gridLayout);
        useButton = new Button(group, SWT.NONE | SWT.CHECK);
        useButton.setText("Use:");
        useButton.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1));
        Label labelSqlSel = new Label(group, SWT.NONE);
        labelSqlSel.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false, 3, 1));
        labelSqlSel.setText("Sql Script Select");
        useButton.addSelectionListener(new SelectionListener() {

            public void widgetDefaultSelected(SelectionEvent e) {
                widgetSelected(e);
            }

            public void widgetSelected(SelectionEvent e) {
                use = !use;
                if (use) {
                    setupUsedState();
                } else {
                    setupUnusedState();
                }
            }
        });
        Label label4SelectDb = new Label(group, SWT.NONE);
        label4SelectDb.setText("Select The Master Db To Run");
        label4SelectDb.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 4));
        tv = new CheckboxTreeViewer(group);
        GridData tvGridData = new GridData(SWT.FILL, SWT.FILL, false, false, 3, 4);
        tvGridData.heightHint = 70;
        tv.getTree().setLayoutData(tvGridData);
        tv.setContentProvider(new FileTreeContentProvider());
        tv.setLabelProvider(new FileTreeLabelProvider());
        tv.setInput("root");
        tv.addCheckStateListener(new ICheckStateListener() {

            public void checkStateChanged(CheckStateChangedEvent event) {
                if (event.getChecked()) {
                    tv.setSubtreeChecked(event.getElement(), true);
                } else {
                    tv.setSubtreeChecked(event.getElement(), false);
                }
            }
        });
        tv.expandAll();
        createDialogArea(group);
        new Label(group, SWT.NONE);
        initializeCompositeState();
    }

    private void initMasters(Composite parent) {
        Replications allMaster = ParametersBO.getMaster();
        List<ReplicationParameters> replication = allMaster.getReplication();
        if (null == replication) {
            return;
        }
        data.horizontalIndent = 0;
        GridData gridData = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
        Label ipLabel = new Label(parent, SWT.NONE);
        ipLabel.setText("Ip");
        ipLabel.setLayoutData(gridData);
        Label nameLabel = new Label(parent, SWT.NONE);
        nameLabel.setText("Name");
        new Label(group, SWT.NONE);
        new Label(group, SWT.NONE);
        for (ReplicationParameters param : replication) {
            String name = (String) param.getDatabase().get("name");
            final String ip = (String) param.getServer().get("ip");
            final Button button = new Button(parent, SWT.NONE | SWT.CHECK);
            button.setText(ip);
            button.setLayoutData(data);
            button.addSelectionListener(new SelectionListener() {

                public void widgetDefaultSelected(SelectionEvent e) {
                    widgetSelected(e);
                }

                public void widgetSelected(SelectionEvent e) {
                    if (!masterDbIpSet.contains(ip)) {
                        masterDbIpSet.add(ip);
                    } else {
                        masterDbIpSet.remove(ip);
                    }
                }
            });
            Label label = new Label(parent, SWT.NONE);
            label.setText(name);
            checkMap.put(button, new ParametersForRun(ip, name));
            buttonMap.put(ip + name, button);
            new Label(group, SWT.NONE);
            new Label(group, SWT.NONE);
        }
    }

    protected void createDialogArea(Composite composite) {
    }

    private void radio(boolean testAll) {
        performanceTest.setSelection(testAll);
        replicationCompare.setEnabled(testAll);
        if (!testAll) {
            replicationCompare.setSelection(false);
        }
        dropCreatedClass.setEnabled(testAll);
        if (!testAll) {
            dropCreatedClass.setSelection(false);
        }
    }

    public void initializeCompositeState() {
        useButton.setSelection(false);
        setupUnusedState();
    }

    /**
	 * 
	 */
    public void setupUsedState() {
        enableAllGroupControl();
    }

    /**
	 * 
	 */
    public void setupUnusedState() {
        disableAllGroupControl();
    }

    public void enableAllGroupControl() {
        setGroupControlState(true);
    }

    public void disableAllGroupControl() {
        setGroupControlState(false);
    }

    public void setGroupControlState(boolean bool) {
        useButton.setSelection(bool);
        use = bool;
        Control[] controls = group.getChildren();
        for (Control control : controls) {
            if (control != useButton) control.setEnabled(bool);
        }
    }

    static class FileTreeContentProvider implements ITreeContentProvider {

        /**
		 * Gets the children of the specified object
		 * 
		 * @param arg0
		 *            the parent object
		 * @return Object[]
		 */
        public Object[] getChildren(Object arg0) {
            return ((File) arg0).listFiles(new FileFilter() {

                public boolean accept(File dir) {
                    if (dir.isHidden()) return false;
                    if (dir.isDirectory()) return true; else return false;
                }
            });
        }

        /**
		 * Gets the parent of the specified object
		 * 
		 * @param arg0
		 *            the object
		 * @return Object
		 */
        public Object getParent(Object arg0) {
            return ((File) arg0).getParentFile();
        }

        /**
		 * Returns whether the passed object has children
		 * 
		 * @param arg0
		 *            the parent object
		 * @return boolean
		 */
        public boolean hasChildren(Object arg0) {
            Object[] obj = getChildren(arg0);
            return obj == null ? false : obj.length > 0;
        }

        /**
		 * Gets the root element(s) of the tree
		 * 
		 * @param arg0
		 *            the input data
		 * @return Object[]
		 */
        public Object[] getElements(Object arg0) {
            return new File[] { new File(GroovyBO.getGroovyPath()) };
        }

        /**
		 * Disposes any created resources
		 */
        public void dispose() {
        }

        /**
		 * Called when the input changes
		 * 
		 * @param arg0
		 *            the viewer
		 * @param arg1
		 *            the old input
		 * @param arg2
		 *            the new input
		 */
        public void inputChanged(Viewer arg0, Object arg1, Object arg2) {
        }
    }

    static class FileTreeLabelProvider implements ILabelProvider {

        private List listeners;

        private Image file;

        private Image dir;

        boolean preserveCase;

        /**
		 * Constructs a FileTreeLabelProvider
		 */
        public FileTreeLabelProvider() {
            listeners = new ArrayList();
            try {
                file = new Image(null, new FileInputStream("images/file.gif"));
                dir = new Image(null, new FileInputStream("images/directory.gif"));
            } catch (FileNotFoundException e) {
            }
        }

        /**
		 * Sets the preserve case attribute
		 * 
		 * @param preserveCase
		 *            the preserve case attribute
		 */
        public void setPreserveCase(boolean preserveCase) {
            this.preserveCase = preserveCase;
            LabelProviderChangedEvent event = new LabelProviderChangedEvent(this);
            for (int i = 0, n = listeners.size(); i < n; i++) {
                ILabelProviderListener ilpl = (ILabelProviderListener) listeners.get(i);
                ilpl.labelProviderChanged(event);
            }
        }

        /**
		 * Gets the image to display for a node in the tree
		 * 
		 * @param arg0
		 *            the node
		 * @return Image
		 */
        public Image getImage(Object arg0) {
            return ((File) arg0).isDirectory() ? dir : file;
        }

        /**
		 * Gets the text to display for a node in the tree
		 * 
		 * @param arg0
		 *            the node
		 * @return String
		 */
        public String getText(Object arg0) {
            String text = ((File) arg0).getName();
            if (text.length() == 0) {
                text = ((File) arg0).getPath();
            }
            return preserveCase ? text : text.toUpperCase();
        }

        /**
		 * Adds a listener to this label provider
		 * 
		 * @param arg0
		 *            the listener
		 */
        public void addListener(ILabelProviderListener arg0) {
            listeners.add(arg0);
        }

        /**
		 * Called when this LabelProvider is being disposed
		 */
        public void dispose() {
            if (dir != null) dir.dispose();
            if (file != null) file.dispose();
        }

        /**
		 * Returns whether changes to the specified property on the specified
		 * element would affect the label for the element
		 * 
		 * @param arg0
		 *            the element
		 * @param arg1
		 *            the property
		 * @return boolean
		 */
        public boolean isLabelProperty(Object arg0, String arg1) {
            return false;
        }

        /**
		 * Removes the listener
		 * 
		 * @param arg0
		 *            the listener to remove
		 */
        public void removeListener(ILabelProviderListener arg0) {
            listeners.remove(arg0);
        }
    }
}
