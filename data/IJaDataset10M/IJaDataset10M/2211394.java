package whf.framework.report.export;

import java.util.Iterator;
import java.util.List;
import whf.framework.report.export.util.ExportUtils;
import whf.framework.util.Utils;

/**
 * @author laidehua
 * 
 */
public class ExportHandler {

    private ReportBuilder exportMode;

    private List<Export> exportList = Utils.newArrayList();

    public ExportHandler(ReportBuilder mode) {
        exportMode = mode;
    }

    public void addExport(Export export) {
        this.exportList.add(export);
    }

    public List getExports() {
        return this.exportList;
    }

    public Export getExport(String name) {
        Iterator it = exportList.iterator();
        while (it.hasNext()) {
            Export export = (Export) it.next();
            if (export.getView().equals(name)) {
                return export;
            }
        }
        return null;
    }

    public Export getCurrentExport() {
        String currentStr = exportMode.getParameters().getParameter(ExportUtils.EXPORT_TYPE);
        return getExport(currentStr);
    }
}
