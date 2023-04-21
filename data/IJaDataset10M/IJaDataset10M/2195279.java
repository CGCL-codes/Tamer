package eu.funcnet.gwt_client.client;

import java.util.ArrayList;
import java.util.List;
import com.google.gwt.core.client.GWT;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;
import com.google.gwt.json.client.JSONString;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FileUpload;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.FormHandler;
import com.google.gwt.user.client.ui.FormPanel;
import com.google.gwt.user.client.ui.FormSubmitCompleteEvent;
import com.google.gwt.user.client.ui.FormSubmitEvent;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
import eu.funcnet.gwt_client.server.FileUploadServlet;

/**
 * This class defines a self-contained widget for accepting a file from the user's hard drive,
 * uploading it to the {@link FileUploadServlet} for parsing, displaying a message indicating
 * the number of protein IDs found,and holding the resulting list of strings in an array ready
 * for submission to FuncNet. Essentially, it acts as a subform within a larger form (the
 * {@link UploadView}. There are two such objects on the standard UploadView, for the query
 * proteins and reference proteins.
 *
 * Apparently there's no way in GWT (we think) to read a file off the user's hard drive and
 * into the browser to parse there, hence the round trip between the browser and the servlet
 * which does the parsing server-side.
 */
public class UploadBox extends FlexTable {

    private final HTML _header;

    private final FileUpload _fileUpload;

    private final Button _uploadBtn;

    private final FormPanel _uploadForm;

    private final String _noProteinsCaption = "No proteins selected.";

    private final Label _proteinsRead = new Label(_noProteinsCaption);

    private final List<String> _selectedIds = new ArrayList<String>();

    private Alert _alerter;

    public UploadBox(final String boxTitle) {
        _uploadForm = new FormPanel();
        _uploadForm.setAction(GWT.getModuleBaseURL() + "/FileUploadServlet");
        _uploadForm.setEncoding(FormPanel.ENCODING_MULTIPART);
        _uploadForm.setMethod(FormPanel.METHOD_POST);
        _header = new HTML(boxTitle);
        _fileUpload = new FileUpload();
        _fileUpload.setName("uploadFormElement");
        _uploadForm.setWidget(_fileUpload);
        _uploadBtn = new Button("Upload");
        _uploadForm.addFormHandler(new FormHandler() {

            public void onSubmit(final FormSubmitEvent event) {
                _uploadBtn.setEnabled(false);
            }

            public void onSubmitComplete(final FormSubmitCompleteEvent event) {
                try {
                    final JSONArray resultsArray = parseUploadResults(event.getResults());
                    JSONObject jobj;
                    JSONString jstr;
                    String id;
                    for (int i = 0; i < resultsArray.size(); i++) {
                        jobj = resultsArray.get(i).isObject();
                        jstr = jobj.get("id").isString();
                        id = jstr.stringValue();
                        _selectedIds.add(id);
                    }
                    _proteinsRead.setVisible(false);
                    _proteinsRead.setText(resultsArray.size() + " proteins selected.");
                    _proteinsRead.removeStyleName("redWarning");
                    _proteinsRead.addStyleName("blueWarning");
                    _proteinsRead.setVisible(true);
                } catch (final Exception e) {
                    _proteinsRead.setText("Parse error, try another file");
                    _alerter.showError(e.getMessage());
                } finally {
                    _uploadBtn.setEnabled(true);
                }
            }
        });
        final FlexCellFormatter cf = super.getFlexCellFormatter();
        setWidget(0, 0, _header);
        cf.setColSpan(0, 0, 2);
        setWidget(1, 0, new Label("Select a file from your local computer, then click Upload."));
        cf.setColSpan(1, 0, 2);
        setWidget(2, 0, _uploadForm);
        cf.setColSpan(2, 0, 2);
        setWidget(3, 0, _uploadBtn);
        setWidget(3, 1, _proteinsRead);
        _proteinsRead.addStyleName("redWarning");
        cf.setWidth(3, 0, "50%");
    }

    /**
     * Wraps the list of selected IDs into a new-line separated string.
     *
     * @return the id list (one per line).
     */
    protected String getSelectedIdsAsString() {
        String foo = "";
        for (int i = 0; i < _selectedIds.size(); i++) {
            foo += _selectedIds.get(i) + "\n";
        }
        return foo;
    }

    /**
     * Parses the JSON string returned by the file parser servlet.
     *
     * @param results the string returned from the servlet
     * @return the IDs in array form
     */
    protected JSONArray parseUploadResults(final String results) {
        final String[] foo = results.split("msg");
        final String jsonStr = foo[1].trim();
        if (jsonStr.startsWith("{error: '")) {
            final int beginIndex = jsonStr.indexOf("'") + 1;
            final int endIndex = jsonStr.lastIndexOf("'");
            final String err = jsonStr.substring(beginIndex, endIndex);
            throw new RuntimeException(err);
        }
        final JSONArray output = JSONParser.parse(jsonStr).isArray();
        if (output == null) {
            throw new RuntimeException("Unexpectedly received non-array result from file parser: " + jsonStr);
        }
        return output;
    }

    /**
     * Adds the upload listener control to the upload button.
     *
     * @param uploadListener
     *            what the button should do when clicked.
     */
    public void addUploadClickListener(final UploadView uploadListener) {
        _uploadBtn.addClickListener(uploadListener);
    }

    /**
     * Performs the actual upload action -- basically validating and submitting the form.
     */
    public void upload() {
        final String filename = _fileUpload.getFilename();
        if (filename.length() != 0) {
            _proteinsRead.setText("Parsing file...");
            _uploadForm.submit();
        } else {
            _alerter.showError("Please click the Browse button to select a file from your computer.");
        }
    }

    /**
     * Returns the submit button. This lets the view in which this widget is embedded differentiate
     * between different instance of the widget.
     *
     * @return the button
     */
    public Widget getUploadButton() {
        return _uploadBtn;
    }

    /**
     * Returns the list of protein IDs once they've been parsed by the server.
     *
     * @return the IDs
     */
    public List<String> getSelectedIds() {
        return _selectedIds;
    }

    public void clearProteins() {
        clearProteins(_noProteinsCaption);
    }

    public void clearProteins(final String newCaption) {
        _selectedIds.clear();
        _proteinsRead.setText(newCaption);
    }

    public void setAlerter(final Alert alerter) {
        _alerter = alerter;
    }
}
