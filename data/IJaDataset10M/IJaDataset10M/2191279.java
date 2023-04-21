package websiteschema.crawler.fb;

import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import websiteschema.element.DocumentUtil;
import websiteschema.fb.annotation.Algorithm;
import websiteschema.fb.annotation.DI;
import websiteschema.fb.annotation.DO;
import websiteschema.fb.annotation.Description;
import websiteschema.fb.annotation.EI;
import websiteschema.fb.annotation.EO;
import websiteschema.fb.core.FunctionBlock;
import websiteschema.utils.StringUtil;
import websiteschema.utils.UrlLinkUtil;

/**
 *
 * @author mgd
 */
@EI(name = { "EI:EXT" })
@EO(name = { "EO" })
@Description(desc = "抽取页面中一个信息单元包含的信息点")
public class FBUnitExtractor extends FunctionBlock {

    @DI(name = "DOC", desc = "要抽取的文档")
    public Document doc;

    @DI(name = "UPATH", desc = "Unit XPath")
    public String unitXPath;

    @DI(name = "PTS", desc = "要抽取的信息点")
    public List<Map<String, String>> points;

    @DI(name = "URL", desc = "文档来自的URL")
    public String url;

    @DI(name = "MUST", desc = "每个单元中必须包含的信息点")
    public List<String> mustHave;

    @DO(name = "OUT", relativeEvents = { "EO" })
    public List<Map<String, String>> table;

    private UnitConf unitConf;

    XPathExpression[] xPathExps = null;

    static final XPath X_PATH = XPathFactory.newInstance().newXPath();

    @Algorithm(name = "EXT")
    public void extractUnits() {
        try {
            init();
            table = assembleUnits(unitConf);
            triggerEvent("EO");
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new RuntimeException(ex.getMessage());
        }
    }

    private void init() {
        unitConf = new UnitConf();
        unitConf.setPoints(points);
        unitConf.setUnitPath(unitXPath);
    }

    private NodeList getUnits_(Document doc, String unitXPath) throws XPathExpressionException {
        XPathExpression xPathExp = X_PATH.compile(unitXPath);
        NodeList units = (NodeList) xPathExp.evaluate(doc, XPathConstants.NODESET);
        return units;
    }

    private List<Node> getUnits(Document doc, String unitXPath) {
        return DocumentUtil.getByXPath(doc, unitXPath);
    }

    private List<Map<String, String>> assembleUnits(UnitConf unitConf) throws XPathExpressionException {
        List<Node> units = getUnits(doc, unitConf.getUnitPath());
        if (null != units) {
            List<Map<String, String>> ret = new ArrayList<Map<String, String>>();
            for (int i = 0; i < units.size(); ++i) {
                Node iterNode = units.get(i);
                Map<String, String> row = extractRow(iterNode);
                if (null != row) {
                    ret.add(row);
                }
            }
            return ret;
        }
        return null;
    }

    private Map<String, String> extractRow(Node iterNode) throws XPathExpressionException {
        int columnCount = unitConf.getPoints().size();
        Map<String, String> row = new HashMap<String, String>(columnCount);
        for (int i = 0; i < columnCount; i++) {
            String name = unitConf.getConfig(i, "name");
            String path = unitConf.getConfig(i, "path");
            String regex = unitConf.getConfig(i, "regex");
            String value = extractCell(iterNode, path, regex);
            if (path.trim().endsWith("@href")) {
                String ret = UrlLinkUtil.getInstance().getURL(url, value).toString();
                if (null != ret) {
                    value = ret;
                }
            }
            if (StringUtil.isNotEmpty(value)) {
                row.put(name, value);
            }
        }
        if (!isValidRow(row, mustHave)) {
            return null;
        }
        return row;
    }

    private String extractCell(Node unitNode, String xpath, String regex) throws XPathExpressionException {
        String value = null;
        XPathExpression xPathExp = X_PATH.compile(xpath);
        value = (String) xPathExp.evaluate(unitNode, XPathConstants.STRING);
        if (StringUtil.isNotEmpty(regex)) {
            value = StringUtil.trim(value);
            Pattern pat = Pattern.compile(regex);
            Matcher m = pat.matcher(value);
            if (m.matches()) {
                String group1 = m.group(1);
                if (StringUtil.isNotEmpty(group1)) {
                    return group1;
                }
            } else {
                return null;
            }
        }
        return value;
    }

    private boolean isValidRow(Map<String, String> row, List<String> mustHave) {
        if (null != mustHave && !mustHave.isEmpty()) {
            for (String must : mustHave) {
                String value = row.get(must);
                if (!StringUtil.isNotEmpty(value)) {
                    return false;
                }
            }
        }
        return true;
    }

    public void printUnits() {
        final int rowCount = this.table.size();
        Map<String, String> row = null;
        for (int i = 0; i < rowCount; ++i) {
            row = this.table.get(i);
            System.out.println(row);
        }
    }

    class UnitConf {

        private List<Map<String, String>> points;

        private String unitPath;

        public List<Map<String, String>> getPoints() {
            return points;
        }

        public Map<String, String> getPoint(int i) {
            return points.get(i);
        }

        public String getConfig(int i, String key) {
            return points.get(i).get(key);
        }

        public String getUnitPath() {
            return unitPath;
        }

        public void setUnitPath(String unitPath) {
            this.unitPath = unitPath;
        }

        public void setPoints(List<Map<String, String>> points) {
            this.points = points;
        }
    }
}
