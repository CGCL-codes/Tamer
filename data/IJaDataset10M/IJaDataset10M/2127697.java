package plugin.net.ehelix.di.zip;

import java.util.List;
import java.util.Map;
import org.pentaho.di.core.CheckResultInterface;
import org.pentaho.di.core.Counter;
import org.pentaho.di.core.annotations.Step;
import org.pentaho.di.core.database.DatabaseMeta;
import org.pentaho.di.core.exception.KettleException;
import org.pentaho.di.core.exception.KettleXMLException;
import org.pentaho.di.core.row.RowMetaInterface;
import org.pentaho.di.core.xml.XMLHandler;
import org.pentaho.di.repository.Repository;
import org.pentaho.di.trans.Trans;
import org.pentaho.di.trans.TransMeta;
import org.pentaho.di.trans.step.BaseStepMeta;
import org.pentaho.di.trans.step.StepCategory;
import org.pentaho.di.trans.step.StepDataInterface;
import org.pentaho.di.trans.step.StepInterface;
import org.pentaho.di.trans.step.StepMeta;
import org.pentaho.di.trans.step.StepMetaInterface;
import org.w3c.dom.Node;

/**
 * Step to retrieve file information from the specified ZIP file.
 * 
 * @author Neyko Neykov, 2009
 */
@Step(category = StepCategory.CATEGORY_INPUT, image = "ui/images/ZIP.png", name = { "ZipFileNames" }, description = "ZipFileNames.Description", tooltip = "ZipFileNames.Tooltip")
public class ZipFileNamesMeta extends BaseStepMeta implements StepMetaInterface {

    private String path;

    @Override
    public void check(List<CheckResultInterface> remarks, TransMeta transMeta, StepMeta stepMeta, RowMetaInterface prev, String[] input, String[] output, RowMetaInterface info) {
    }

    @Override
    public StepInterface getStep(StepMeta stepMeta, StepDataInterface stepDataInterface, int copyNr, TransMeta transMeta, Trans trans) {
        return new ZipFileNames(stepMeta, stepDataInterface, copyNr, transMeta, trans);
    }

    @Override
    public StepDataInterface getStepData() {
        return new ZipFileNamesData();
    }

    @Override
    public void loadXML(Node stepnode, List<DatabaseMeta> databases, Map<String, Counter> counters) throws KettleXMLException {
        path = XMLHandler.getTagValue(stepnode, "path");
    }

    @Override
    public void readRep(Repository rep, long id_step, List<DatabaseMeta> databases, Map<String, Counter> counters) throws KettleException {
        path = rep.getStepAttributeString(id_step, "path");
    }

    @Override
    public void saveRep(Repository rep, long id_trans, long id_step) throws KettleException {
        rep.saveStepAttribute(id_trans, id_step, "path", path);
    }

    @Override
    public void setDefault() {
    }

    @Override
    public String getXML() {
        StringBuilder sb = new StringBuilder();
        sb.append(XMLHandler.addTagValue("path", path));
        return sb.toString();
    }

    public void setPath(String path) {
        this.path = path;
    }

    /** @return Full name of the zip file to be used for browsing the names */
    public String getPath() {
        return path != null ? path : "";
    }
}
