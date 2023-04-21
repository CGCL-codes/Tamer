package org.wsmostudio.bpmo.ui.preferences;

import org.eclipse.jface.preference.*;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.*;
import org.eclipse.ui.*;
import org.wsmostudio.bpmo.Activator;
import org.wsmostudio.bpmo.model.*;
import org.wsmostudio.bpmo.model.connectors.GraphConnector;
import org.wsmostudio.bpmo.model.connectors.MessageConnector;
import org.wsmostudio.bpmo.model.events.*;
import org.wsmostudio.bpmo.model.gateway.GatewayNode;
import org.wsmostudio.bpmo.model.merge.MergeNode;
import org.wsmostudio.bpmo.model.tasks.*;
import org.wsmostudio.bpmo.ui.editor.BpmoEditor;

/**
 * 
 * @author not attributable
 * @version $Revision: 1583 $ $Date: 2008-12-17 07:31:03 -0500 (Wed, 17 Dec 2008) $
 */
public class DiagramColorsPage extends FieldEditorPreferencePage implements IWorkbenchPreferencePage {

    private static final String BACKGROUND_KEY = "$BpmoEditor$bg_color";

    private static final String PROCESS_KEY = "$BpmoEditor$process_color";

    private static final String SEQUENCE_KEY = "$BpmoEditor$seq_color";

    private static final String SEQUENCE_LAB_KEY = "$BpmoEditor$seq_lab_color";

    private static final String MESSAGE_KEY = "$BpmoEditor$message_color";

    private static final String START_KEY = "$BpmoEditor$start_color";

    private static final String GATE_KEY = "$BpmoEditor$gate_color";

    private static final String CONDITION_KEY = "$BpmoEditor$cond_color";

    private static final String GOAL_TASK_KEY = "$BpmoEditor$goal_task_color";

    private static final String WS_TASK_KEY = "$BpmoEditor$ws_task_color";

    private static final String MANUAL_TASK_KEY = "$BpmoEditor$manual_task_color";

    public static final RGB DEFAULT_COLOR_BACKGROUND = new RGB(255, 255, 255);

    public static final RGB DEFAULT_COLOR_PROCESS = new RGB(232, 232, 255);

    public static final RGB DEFAULT_COLOR_SEQUENCE = new RGB(128, 127, 254);

    public static final RGB DEFAULT_COLOR_SEQUENCE_LABEL = new RGB(255, 255, 255);

    public static final RGB DEFAULT_COLOR_MESSAGE = new RGB(128, 127, 254);

    public static final RGB DEFAULT_COLOR_START_END = new RGB(255, 255, 255);

    public static final RGB DEFAULT_COLOR_GATE_MERGE = new RGB(255, 255, 255);

    public static final RGB DEFAULT_COLOR_GOAL_TASK = new RGB(255, 255, 180);

    public static final RGB DEFAULT_COLOR_WS_TASK = new RGB(205, 254, 233);

    public static final RGB DEFAULT_COLOR_MANUAL_TASK = new RGB(255, 255, 255);

    public static final RGB DEFAULT_COLOR_CONDITION = new RGB(255, 255, 255);

    private ColorFieldEditor bgChooser, processChooser, seqChooser, seqLabChooser, messageChooser, startChooser, gateChooser, goalTaskChooser, wsTaskChooser, manualTaskChooser, condChooser;

    public static Color COLOR_BACKGROUND = null;

    public static Color COLOR_PROCESS = null;

    public static Color COLOR_SEQUENCE = null;

    public static Color COLOR_SEQUENCE_LABEL = null;

    public static Color COLOR_MESSAGE = null;

    public static Color COLOR_START_END = null;

    public static Color COLOR_GATE_MERGE = null;

    public static Color COLOR_GOAL_TASK = null;

    public static Color COLOR_WS_TASK = null;

    public static Color COLOR_MANUAL_TASK = null;

    public static Color COLOR_CONDITION = null;

    public DiagramColorsPage() {
        super("BPMO Diagram Colors", FieldEditorPreferencePage.GRID);
        setPreferenceStore(Activator.getDefault().getPreferenceStore());
        initColors();
    }

    protected void createFieldEditors() {
        Composite parent = getFieldEditorParent();
        parent.setLayout(new GridLayout(2, false));
        Group generalGroup = new Group(parent, SWT.NONE);
        generalGroup.setLayout(new GridLayout(2, false));
        generalGroup.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        generalGroup.setText("General");
        new Label(parent, SWT.NONE);
        String filler = "                     ";
        bgChooser = new ColorFieldEditor(BACKGROUND_KEY, " Diagram Background :    " + filler, generalGroup);
        addField(bgChooser);
        processChooser = new ColorFieldEditor(PROCESS_KEY, " Process Background :    " + filler, generalGroup);
        addField(processChooser);
        Group workflowsGroup = new Group(parent, SWT.NONE);
        workflowsGroup.setLayout(new GridLayout(2, false));
        workflowsGroup.setText("Workflow Entities");
        workflowsGroup.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        new Label(parent, SWT.NONE);
        startChooser = new ColorFieldEditor(START_KEY, " Events Background : " + filler, workflowsGroup);
        addField(startChooser);
        gateChooser = new ColorFieldEditor(GATE_KEY, " Gateways Background : " + filler, workflowsGroup);
        addField(gateChooser);
        condChooser = new ColorFieldEditor(CONDITION_KEY, " Condition Background : " + filler, workflowsGroup);
        addField(condChooser);
        Group tasksGroup = new Group(parent, SWT.NONE);
        tasksGroup.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        tasksGroup.setLayout(new GridLayout(2, false));
        tasksGroup.setText("Tasks");
        new Label(parent, SWT.NONE);
        goalTaskChooser = new ColorFieldEditor(GOAL_TASK_KEY, " Goal Tasks Background :         ", tasksGroup);
        addField(goalTaskChooser);
        wsTaskChooser = new ColorFieldEditor(WS_TASK_KEY, " Send/Receive Tasks Background :       ", tasksGroup);
        addField(wsTaskChooser);
        manualTaskChooser = new ColorFieldEditor(MANUAL_TASK_KEY, " Manual Tasks Background :         ", tasksGroup);
        addField(manualTaskChooser);
        Group connGroup = new Group(parent, SWT.NONE);
        connGroup.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        connGroup.setLayout(new GridLayout(2, false));
        connGroup.setText("Connections");
        new Label(parent, SWT.NONE);
        seqChooser = new ColorFieldEditor(SEQUENCE_KEY, " Sequence Connection : " + filler, connGroup);
        addField(seqChooser);
        messageChooser = new ColorFieldEditor(MESSAGE_KEY, " Message Connection : " + filler, connGroup);
        addField(messageChooser);
        seqLabChooser = new ColorFieldEditor(SEQUENCE_LAB_KEY, " Connection Label: " + filler, connGroup);
        addField(seqLabChooser);
    }

    public void init(IWorkbench workbench) {
    }

    public boolean performOk() {
        boolean stat = super.performOk();
        COLOR_BACKGROUND = null;
        COLOR_PROCESS = null;
        COLOR_SEQUENCE = null;
        COLOR_SEQUENCE_LABEL = null;
        COLOR_MESSAGE = null;
        COLOR_START_END = null;
        COLOR_GATE_MERGE = null;
        COLOR_GOAL_TASK = null;
        COLOR_WS_TASK = null;
        COLOR_MANUAL_TASK = null;
        COLOR_CONDITION = null;
        doUpdateOpenedEditors();
        return stat;
    }

    protected void performDefaults() {
        PreferenceConverter.setDefault(getPreferenceStore(), BACKGROUND_KEY, DEFAULT_COLOR_BACKGROUND);
        PreferenceConverter.setDefault(getPreferenceStore(), PROCESS_KEY, DEFAULT_COLOR_PROCESS);
        PreferenceConverter.setDefault(getPreferenceStore(), SEQUENCE_KEY, DEFAULT_COLOR_SEQUENCE);
        PreferenceConverter.setDefault(getPreferenceStore(), SEQUENCE_LAB_KEY, DEFAULT_COLOR_SEQUENCE_LABEL);
        PreferenceConverter.setDefault(getPreferenceStore(), MESSAGE_KEY, DEFAULT_COLOR_MESSAGE);
        PreferenceConverter.setDefault(getPreferenceStore(), START_KEY, DEFAULT_COLOR_START_END);
        PreferenceConverter.setDefault(getPreferenceStore(), GATE_KEY, DEFAULT_COLOR_GATE_MERGE);
        PreferenceConverter.setDefault(getPreferenceStore(), GOAL_TASK_KEY, DEFAULT_COLOR_GOAL_TASK);
        PreferenceConverter.setDefault(getPreferenceStore(), WS_TASK_KEY, DEFAULT_COLOR_WS_TASK);
        PreferenceConverter.setDefault(getPreferenceStore(), MANUAL_TASK_KEY, DEFAULT_COLOR_MANUAL_TASK);
        PreferenceConverter.setDefault(getPreferenceStore(), CONDITION_KEY, DEFAULT_COLOR_CONDITION);
        super.performDefaults();
    }

    private void initColors() {
        initColorEntry(BACKGROUND_KEY, DEFAULT_COLOR_BACKGROUND);
        initColorEntry(PROCESS_KEY, DEFAULT_COLOR_PROCESS);
        initColorEntry(SEQUENCE_KEY, DEFAULT_COLOR_SEQUENCE);
        initColorEntry(SEQUENCE_LAB_KEY, DEFAULT_COLOR_SEQUENCE_LABEL);
        initColorEntry(MESSAGE_KEY, DEFAULT_COLOR_MESSAGE);
        initColorEntry(START_KEY, DEFAULT_COLOR_START_END);
        initColorEntry(GATE_KEY, DEFAULT_COLOR_GATE_MERGE);
        initColorEntry(GOAL_TASK_KEY, DEFAULT_COLOR_GOAL_TASK);
        initColorEntry(WS_TASK_KEY, DEFAULT_COLOR_WS_TASK);
        initColorEntry(MANUAL_TASK_KEY, DEFAULT_COLOR_MANUAL_TASK);
        initColorEntry(CONDITION_KEY, DEFAULT_COLOR_CONDITION);
    }

    private void doUpdateOpenedEditors() {
        IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
        if (page == null) {
            return;
        }
        IEditorReference[] editors = page.getEditorReferences();
        if (editors == null) {
            return;
        }
        for (int i = 0; i < editors.length; i++) {
            IEditorPart editor = editors[i].getEditor(true);
            if (editor != null && editor instanceof BpmoEditor) {
                ((BpmoEditor) editor).updateView();
            }
        }
    }

    private void initColorEntry(String key, RGB colorInfo) {
        if (getPreferenceStore().contains(key)) {
            return;
        }
        PreferenceConverter.setValue(getPreferenceStore(), key, colorInfo);
        PreferenceConverter.setDefault(getPreferenceStore(), key, colorInfo);
    }

    public void dispose() {
        bgChooser.dispose();
        processChooser.dispose();
        seqChooser.dispose();
        seqLabChooser.dispose();
        messageChooser.dispose();
        startChooser.dispose();
        gateChooser.dispose();
        goalTaskChooser.dispose();
        wsTaskChooser.dispose();
        manualTaskChooser.dispose();
        condChooser.dispose();
    }

    public static Color getColorForEntity(WorkflowEntityNode entity) {
        if (entity instanceof BpmoModel) {
            if (COLOR_BACKGROUND == null) {
                COLOR_BACKGROUND = new Color(Display.getCurrent(), readPropertyValue(BACKGROUND_KEY, DEFAULT_COLOR_BACKGROUND));
            }
            return COLOR_BACKGROUND;
        }
        if (entity instanceof ProcessNode) {
            if (COLOR_PROCESS == null) {
                COLOR_PROCESS = new Color(Display.getCurrent(), readPropertyValue(PROCESS_KEY, DEFAULT_COLOR_PROCESS));
            }
            return COLOR_PROCESS;
        }
        if (entity instanceof GatewayNode || entity instanceof MergeNode) {
            if (COLOR_GATE_MERGE == null) {
                COLOR_GATE_MERGE = new Color(Display.getCurrent(), readPropertyValue(GATE_KEY, DEFAULT_COLOR_GATE_MERGE));
            }
            return COLOR_GATE_MERGE;
        }
        if (entity instanceof EventNode || entity instanceof ReceiveMessageEventNode) {
            if (COLOR_START_END == null) {
                COLOR_START_END = new Color(Display.getCurrent(), readPropertyValue(START_KEY, DEFAULT_COLOR_START_END));
            }
            return COLOR_START_END;
        }
        if (entity instanceof GoalTaskNode) {
            if (COLOR_GOAL_TASK == null) {
                COLOR_GOAL_TASK = new Color(Display.getCurrent(), readPropertyValue(GOAL_TASK_KEY, DEFAULT_COLOR_GOAL_TASK));
            }
            return COLOR_GOAL_TASK;
        }
        if (entity instanceof ReceiveTaskNode || entity instanceof SendTaskNode) {
            if (COLOR_WS_TASK == null) {
                COLOR_WS_TASK = new Color(Display.getCurrent(), readPropertyValue(WS_TASK_KEY, DEFAULT_COLOR_WS_TASK));
            }
            return COLOR_WS_TASK;
        }
        if (entity instanceof ManualTaskNode) {
            if (COLOR_MANUAL_TASK == null) {
                COLOR_MANUAL_TASK = new Color(Display.getCurrent(), readPropertyValue(MANUAL_TASK_KEY, DEFAULT_COLOR_MANUAL_TASK));
            }
            return COLOR_MANUAL_TASK;
        }
        if (entity instanceof ConditionNode) {
            if (COLOR_CONDITION == null) {
                COLOR_CONDITION = new Color(Display.getCurrent(), readPropertyValue(CONDITION_KEY, DEFAULT_COLOR_CONDITION));
            }
            return COLOR_CONDITION;
        }
        return COLOR_BACKGROUND;
    }

    public static Color getColorForConnection(GraphConnector conn) {
        if (conn instanceof MessageConnector) {
            if (COLOR_MESSAGE == null) {
                COLOR_MESSAGE = new Color(Display.getCurrent(), readPropertyValue(MESSAGE_KEY, DEFAULT_COLOR_MESSAGE));
            }
            return COLOR_MESSAGE;
        }
        if (COLOR_SEQUENCE == null) {
            COLOR_SEQUENCE = new Color(Display.getCurrent(), readPropertyValue(SEQUENCE_KEY, DEFAULT_COLOR_SEQUENCE));
        }
        return COLOR_SEQUENCE;
    }

    public static Color getConnectionLabelBackground() {
        if (COLOR_SEQUENCE_LABEL == null) {
            COLOR_SEQUENCE_LABEL = new Color(Display.getCurrent(), readPropertyValue(SEQUENCE_LAB_KEY, DEFAULT_COLOR_SEQUENCE_LABEL));
        }
        return COLOR_SEQUENCE_LABEL;
    }

    private static RGB readPropertyValue(String prop, RGB defaultValue) {
        if (false == Activator.getDefault().getPreferenceStore().contains(prop)) {
            return defaultValue;
        }
        return PreferenceConverter.getColor(Activator.getDefault().getPreferenceStore(), prop);
    }
}
