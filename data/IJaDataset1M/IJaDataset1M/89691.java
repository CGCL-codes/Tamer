package org.openeccos.gui;

import java.util.Calendar;
import javax.swing.Action;
import nextapp.echo2.app.Alignment;
import nextapp.echo2.app.ApplicationInstance;
import nextapp.echo2.app.Color;
import nextapp.echo2.app.Column;
import nextapp.echo2.app.Extent;
import nextapp.echo2.app.Grid;
import nextapp.echo2.app.Insets;
import nextapp.echo2.app.Row;
import nextapp.echo2.app.SplitPane;
import nextapp.echo2.app.event.ActionEvent;
import nextapp.echo2.app.event.ActionListener;
import org.openeccos.EccosAppInstance;
import org.openeccos.EccosDesktop;
import org.openeccos.dao.EccosService;
import org.openeccos.model.MDiscussion;
import org.openeccos.model.MDiscussionMessage;
import org.openeccos.model.MTask;
import org.openeccos.model.MUser;
import org.openeccos.util.PDUtil;
import org.openeccos.widgets.PDLabel;
import org.openeccos.widgets.PDTextField;
import com.echoguicmp.RoundedColumn;
import com.sas.framework.expojo.ModelExposer;
import echopointng.ButtonEx;
import echopointng.ContentPaneEx;
import echopointng.LabelEx;
import echopointng.Strut;

/**
 * A window displaying a chat/discussion
 *
 * @author cgspinner@web.de
 */
public class FrmDiscussion extends PDWindowPane {

    private ButtonEx btnFinish;

    private MDiscussion discussion;

    private MessageRow lastRow;

    private MTask newTask;

    private boolean noAction = false;

    private MTask originalTask;

    private ContentPaneEx pnlHistory;

    private PDTextField txtText;

    private LabelEx lblTheme;

    private Row rowCommands;

    public FrmDiscussion(MDiscussion discussion, MTask originalTask, MTask newTask) {
        this.discussion = discussion;
        this.originalTask = originalTask;
        this.newTask = newTask;
        initGUI();
        loadData();
    }

    public FrmDiscussion(MDiscussion discussion, MTask originalTask) {
        this(discussion, originalTask, null);
    }

    public void addAction(final Action action) {
        ButtonEx btn = new ButtonEx(action.toString());
        btn.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                action.actionPerformed(null);
            }
        });
        rowCommands.add(btn);
    }

    private void addMessage() {
        if (!PDUtil.isEmpty(txtText.getText())) {
            MDiscussionMessage msg = discussion.addMessage(txtText.getText(), EccosDesktop.getCurrentUser());
            addMessageRow(msg);
            EccosService service = (EccosService) ModelExposer.get().getService(EccosService.NAME);
            service.persist(discussion);
            service.persist(msg);
            txtText.setText("");
        }
        ApplicationInstance.getActive().setFocusedComponent(txtText);
    }

    private void addMessageRow(MDiscussionMessage msg) {
        if (lastRow == null || !lastRow.author.equals(msg.getAuthor()) || msg.getText().equals("xxx")) {
            pnlHistory.add(new Strut(0, 6));
            lastRow = new MessageRow(msg.getAuthor());
            pnlHistory.add(lastRow);
        }
        LabelEx lblText = new LabelEx(msg.getText());
        pnlHistory.add(lblText);
        pnlHistory.setVerticalScroll(new Extent(-1));
    }

    private void doFinish() {
        if (discussion == null) {
            return;
        }
        EccosService service = (EccosService) ModelExposer.get().getService(EccosService.NAME);
        if (originalTask != null) {
            originalTask.setDoneDate(Calendar.getInstance().getTime());
            originalTask.setDoneBy(EccosAppInstance.getActiveUserSession().getUser());
            service.persist(originalTask);
        }
        if (newTask != null && lastRow != null) {
            if (lastRow.author == EccosDesktop.getCurrentUser()) {
                newTask.setCreatedDate(Calendar.getInstance().getTime());
                newTask.setCreatedBy(EccosDesktop.getCurrentUser());
                service.persist(newTask);
            }
        }
        EccosDesktop.getDesktop().refreshTaskDisplay();
    }

    protected void initGUI() {
        setWidth(new Extent(450));
        setHeight(new Extent(500));
        setResizable(true);
        setTitle("Offline Chat");
        SplitPane split1 = new SplitPane(SplitPane.ORIENTATION_VERTICAL_TOP_BOTTOM);
        split1.setSeparatorPosition(new Extent(70));
        add(split1);
        lblTheme = new PDLabel(PDLabel.BORDERED);
        lblTheme.setOutsets(new Insets(6, 6, 6, 0));
        lblTheme.setLineWrap(true);
        split1.add(lblTheme);
        SplitPane split2 = new SplitPane(SplitPane.ORIENTATION_VERTICAL_BOTTOM_TOP);
        split2.setSeparatorPosition(new Extent(60));
        split1.add(split2);
        Column mainCol = new Column();
        split2.add(mainCol);
        pnlHistory = new ContentPaneEx();
        pnlHistory.setInsets(new Insets(3, 3, 3, 3));
        split2.add(pnlHistory);
        Grid grd = new Grid(2);
        grd.setColumnWidth(0, new Extent(100, Extent.PERCENT));
        grd.setInsets(new Insets(6, 0, 6, 3));
        mainCol.add(grd);
        txtText = new PDTextField();
        txtText.setActionCausedOnChange(true);
        txtText.setWidth(new Extent(98, Extent.PERCENT));
        txtText.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent arg0) {
                if (noAction) {
                    return;
                }
                addMessage();
            }
        });
        grd.add(txtText);
        ButtonEx btnSend = new ButtonEx("Senden");
        btnSend.setInsets(new Insets(6, 2, 9, 2));
        btnSend.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent arg0) {
                noAction = true;
                addMessage();
                noAction = false;
            }
        });
        btnSend.setWidth(new Extent(45));
        grd.add(btnSend);
        rowCommands = new Row();
        rowCommands.setAlignment(Alignment.ALIGN_RIGHT);
        grd.add(rowCommands);
        btnFinish = new ButtonEx("Fertig");
        btnFinish.setInsets(new Insets(6, 2, 9, 2));
        btnFinish.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                doFinish();
                FrmDiscussion.this.setVisible(false);
            }
        });
        grd.add(btnFinish);
    }

    private void loadData() {
        lblTheme.setText("Theme: " + discussion.getTheme());
        pnlHistory.removeAll();
        for (MDiscussionMessage msg : discussion.getMessages()) {
            addMessageRow(msg);
        }
    }

    class MessageRow extends RoundedColumn {

        MUser author;

        MessageRow(MUser author) {
            this.author = author;
            setTopRoundedCorner(true);
            setBottomRoundedCorner(false);
            setBackground(Color.LIGHTGRAY);
            LabelEx lblAuthor = new LabelEx(author.toString());
            lblAuthor.setBackground(Color.LIGHTGRAY);
            lblAuthor.setOutsets(new Insets(3, 0, 0, 2));
            add(lblAuthor);
        }

        void addMessage(String msg) {
            LabelEx lblText = new LabelEx(msg);
            add(lblText);
        }
    }
}
