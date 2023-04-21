package org.jcvi.vics.web.gwt.blast.client.panel;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.ServiceDefTarget;
import com.google.gwt.user.client.ui.*;
import org.jcvi.vics.model.common.UserDataNodeVO;
import org.jcvi.vics.model.tasks.blast.CreateBlastDatabaseTask;
import org.jcvi.vics.model.user_data.Node;
import org.jcvi.vics.web.gwt.common.client.jobs.JobStatusListener;
import org.jcvi.vics.web.gwt.common.client.jobs.JobStatusTimer;
import org.jcvi.vics.web.gwt.common.client.jobs.JobSubmissionListener;
import org.jcvi.vics.web.gwt.common.client.panel.CenteredWidgetHorizontalPanel;
import org.jcvi.vics.web.gwt.common.client.panel.FileChooserPanel;
import org.jcvi.vics.web.gwt.common.client.popup.ErrorPopupPanel;
import org.jcvi.vics.web.gwt.common.client.popup.launcher.PopupCenteredLauncher;
import org.jcvi.vics.web.gwt.common.client.service.DataService;
import org.jcvi.vics.web.gwt.common.client.service.DataServiceAsync;
import org.jcvi.vics.web.gwt.common.client.service.log.Logger;
import org.jcvi.vics.web.gwt.common.client.ui.ButtonSet;
import org.jcvi.vics.web.gwt.common.client.ui.RoundedButton;
import org.jcvi.vics.web.gwt.common.client.ui.SelectionListener;
import org.jcvi.vics.web.gwt.common.client.util.HtmlUtils;
import org.jcvi.vics.web.gwt.common.client.util.StringUtils;
import java.util.ArrayList;

/**
 * Created by IntelliJ IDEA.
 * User: tsafford
 * Date: Jun 8, 2009
 * Time: 3:14:05 PM
 */
public class AddBlastDatabasePanel extends VerticalPanel {

    private static Logger _logger = Logger.getLogger("org.jcvi.vics.web.gwt.blast.client.panel.AddBlastDatabasePanel");

    private TextBox _name = new TextBox();

    private TextArea _description = new TextArea();

    private ListBox _visibility = new ListBox();

    private FileChooserPanel _fileChooserPanel;

    private RoundedButton _saveButton, _clearButton;

    private Label _loadingLabel = new Label();

    private String _dataNodeKey = "";

    private CreateBlastDatabaseTask _createDBTask = new CreateBlastDatabaseTask();

    private JobSubmissionListener _listener;

    private static DataServiceAsync _dataservice = (DataServiceAsync) GWT.create(DataService.class);

    static {
        ((ServiceDefTarget) _dataservice).setServiceEntryPoint("data.srv");
    }

    public AddBlastDatabasePanel(JobSubmissionListener listener) {
        super();
        _listener = listener;
        init();
    }

    private void init() {
        VerticalPanel _mainPanel = new VerticalPanel();
        createButtons();
        _description.setCharacterWidth(50);
        Grid _grid = new Grid(5, 2);
        _grid.setWidget(0, 0, HtmlUtils.getHtml("Name:", "text"));
        _grid.setWidget(0, 1, _name);
        _grid.setWidget(1, 0, HtmlUtils.getHtml("Description:", "text"));
        _grid.setWidget(1, 1, _description);
        _grid.setWidget(2, 0, HtmlUtils.getHtml("Visibility:", "text"));
        _grid.setWidget(2, 1, getVisibilityWidget());
        _grid.setWidget(3, 0, HtmlUtils.getHtml("FASTA File:", "text"));
        _grid.setWidget(3, 1, getUploadPanel());
        _grid.setWidget(4, 0, _loadingLabel);
        _grid.setWidget(4, 1, HtmlUtils.getHtml("", "text"));
        _mainPanel.add(_grid);
        add(_mainPanel);
        add(getSubmitButtonPanel());
    }

    private FileChooserPanel getUploadPanel() {
        ArrayList<FileChooserPanel.FILE_TYPE> types = new ArrayList<FileChooserPanel.FILE_TYPE>();
        types.add(FileChooserPanel.FILE_TYPE.mpfa);
        types.add(FileChooserPanel.FILE_TYPE.seq);
        types.add(FileChooserPanel.FILE_TYPE.ffn);
        types.add(FileChooserPanel.FILE_TYPE.fa);
        types.add(FileChooserPanel.FILE_TYPE.faa);
        types.add(FileChooserPanel.FILE_TYPE.fna);
        types.add(FileChooserPanel.FILE_TYPE.fsa);
        types.add(FileChooserPanel.FILE_TYPE.fasta);
        types.add(FileChooserPanel.FILE_TYPE.frg);
        _fileChooserPanel = new FileChooserPanel(new SelectionListener() {

            public void onSelect(String value) {
                _dataNodeKey = _fileChooserPanel.getSessionDataNodeKey();
                _saveButton.setEnabled(true);
            }

            public void onUnSelect(String value) {
                _saveButton.setEnabled(false);
            }
        }, types);
        return _fileChooserPanel;
    }

    private ButtonSet createButtons() {
        _clearButton = new RoundedButton("Clear", new ClickListener() {

            public void onClick(Widget sender) {
                _name.setText("");
                _description.setText("");
                _fileChooserPanel.clear();
                _dataNodeKey = "";
            }
        });
        _saveButton = new RoundedButton("Save Database", new ClickListener() {

            public void onClick(Widget sender) {
                submitJob();
            }
        });
        _saveButton.setEnabled(false);
        return new ButtonSet(new RoundedButton[] { _saveButton, _clearButton });
    }

    private Widget getSubmitButtonPanel() {
        HorizontalPanel panel = new HorizontalPanel();
        panel.add(_clearButton);
        panel.add(HtmlUtils.getHtml("&nbsp;", "spacer"));
        panel.add(_saveButton);
        return new CenteredWidgetHorizontalPanel(panel);
    }

    private void submitJob() {
        if (!StringUtils.hasValue(_name.getText())) {
            new PopupCenteredLauncher(new ErrorPopupPanel("A name for the blast database is required.")).showPopup(null);
        } else if (!StringUtils.hasValue(_description.getText())) {
            new PopupCenteredLauncher(new ErrorPopupPanel("A description for the blast database is required.")).showPopup(null);
        } else {
            _dataservice.saveUserDefinedFastaNode(_name.getText(), _dataNodeKey, Node.VISIBILITY_PRIVATE_DEPRECATED, new AsyncCallback<UserDataNodeVO>() {

                public void onFailure(Throwable throwable) {
                    new PopupCenteredLauncher(new ErrorPopupPanel("There was a problem submitting your job.")).showPopup(null);
                }

                public void onSuccess(UserDataNodeVO nodeVO) {
                    _createDBTask.setJobName(_name.getText());
                    _createDBTask.setParameter(CreateBlastDatabaseTask.PARAM_BLAST_DB_NAME, _name.getText());
                    _createDBTask.setParameter(CreateBlastDatabaseTask.PARAM_BLAST_DB_DESCRIPTION, _description.getText());
                    _createDBTask.setParameter(CreateBlastDatabaseTask.PARAM_FASTA_NODE_ID, nodeVO.getDatabaseObjectId());
                    _createDBTask.setParameter(CreateBlastDatabaseTask.PARAM_DB_VISIBILITY, _visibility.getValue(_visibility.getSelectedIndex()));
                    _loadingLabel.setText("Loading...");
                    _dataservice.submitJob(_createDBTask, new AsyncCallback<String>() {

                        public void onFailure(Throwable throwable) {
                            _loadingLabel.setText("");
                            new PopupCenteredLauncher(new ErrorPopupPanel("An error occurred creating the blast db.")).showPopup(null);
                        }

                        public void onSuccess(String jobNumber) {
                            _loadingLabel.setText("");
                            createUpdateBlastNodesTimer(jobNumber);
                        }
                    });
                }
            });
        }
    }

    /**
     * This method uses the JobStatusTimer to monitor the job.
     *
     * @param jobNumber - grid job number
     */
    private void createUpdateBlastNodesTimer(final String jobNumber) {
        new JobStatusTimer(jobNumber, new JobStatusListener() {

            public void onJobRunning(org.jcvi.vics.shared.tasks.JobInfo ignore) {
            }

            public void onJobFinished(org.jcvi.vics.shared.tasks.JobInfo newJobInfo) {
                _logger.debug("Create DB task " + newJobInfo.getJobId() + " completed, status = " + newJobInfo.getStatus());
                _listener.onSuccess(newJobInfo.getJobId());
            }

            public void onCommunicationError() {
                _logger.error("Create DB task failed");
            }
        });
    }

    private Widget getVisibilityWidget() {
        _visibility.addItem("private - only I can use", Node.VISIBILITY_PRIVATE);
        _visibility.addItem("public - data available for everyone", Node.VISIBILITY_PUBLIC);
        _visibility.addItem("inactive - create database but no one can use yet", Node.VISIBILITY_INACTIVE);
        return _visibility;
    }
}
