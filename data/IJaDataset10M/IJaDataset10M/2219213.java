package tr.extract.exports.information;

import java.io.File;
import java.net.URL;
import java.util.List;
import java.util.Vector;
import javax.swing.JOptionPane;
import tr.extract.Extract;
import tr.extract.Extract.FormatType;
import tr.extract.Param;
import tr.extract.Param.Item;
import tr.extract.ParamBoolean;
import tr.extract.ParamList;
import tr.extract.ParamsDialog;
import tr.extract.prefs.ExtractPrefs;
import tr.model.Data;

/**
 * Export to a text file extract implementation.
 *
 * @author Jeremy Moore (jimoore@netspace.net.au)
 */
public class ExportText extends Extract {

    /** Overridden to return the extract ID. */
    public String getID() {
        return "information-text";
    }

    /** Overridden to return the export name. */
    public String getName() {
        return getString("CTL_ExportTextAction");
    }

    /** Overridden to return report parameters. */
    public List<Param> getParams() {
        List<Param> params = new Vector<Param>(2);
        params.add(new ParamBoolean("include-topic", getString("param-include-topic")));
        params.add(new ParamBoolean("include-notes", getString("param-include-notes")));
        List<Item> separatorItems = new Vector<Item>(3);
        separatorItems.add(new Item(getString("comma"), "comma"));
        separatorItems.add(new Item(getString("semicolon"), "semicolon"));
        separatorItems.add(new Item(getString("tab"), "tab"));
        params.add(new ParamList("separator", getString("param-separator"), separatorItems));
        params.add(new ParamBoolean("output-headings", getString("output-headings")));
        return params;
    }

    /** Overriden to process the report. */
    public void process(Data data) throws Exception {
        List<Param> params = getParams();
        String title = getDialogTitleExport(getName());
        ParamsDialog dlg = new ParamsDialog(title, getID(), params);
        if (dlg.showDialog() == JOptionPane.CANCEL_OPTION) {
            return;
        }
        params.add(new Param("heading-descr", getString("heading-descr")));
        params.add(new Param("heading-topic", getString("heading-topic")));
        params.add(new Param("heading-notes", getString("heading-notes")));
        File xmlfile = getTmpFile("data.xml");
        extractData(data, xmlfile, FormatType.CSV);
        URL xslURL = getClass().getResource("reference-text.xsl");
        File outfile = getOutFile("reference-" + getTimeStamp() + ".txt");
        String encoding = ExtractPrefs.getEncoding();
        transformXSL(xmlfile, xslURL, params, outfile, encoding, false);
        openTextFile(outfile);
    }
}
