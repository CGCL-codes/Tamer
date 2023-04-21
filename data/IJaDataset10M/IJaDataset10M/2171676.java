package de.str.prettysource.demoweb;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.faces.event.ActionEvent;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.log4j.Logger;
import de.str.prettysource.InputNode;
import de.str.prettysource.InputNodeParentProc;
import de.str.prettysource.InputNodeProcessor;
import de.str.prettysource.OutputNode;
import de.str.prettysource.format.html.HtmlFormatFactory;
import de.str.prettysource.format.html.HtmlOutputFormat;

public class ConfigureFormatsComponent extends PrettySourceComponent {

    private static final Logger log = Logger.getLogger(ConfigureFormatsComponent.class);

    public static class ParserNodeMetaData {

        private int level = 0;

        private InputNodeProcessor proc = null;

        public int getLevel() {
            return this.level;
        }

        public InputNode getNode() {
            return this.proc.getInputNode();
        }

        public OutputNode getFormat() {
            return this.proc.getOutputNode();
        }

        public String getPatternDescription() {
            String result = "<span class=\"psDemo_patternType\">";
            String pattern = "<span class=\"psDemo_patternString\">";
            InputNode node = getNode();
            if (node.getStartPattern() != null) {
                String start = pattern + StringEscapeUtils.escapeHtml(node.getStartPattern().pattern()) + "</span>";
                if (node.getEndPattern() != null) {
                    String end = pattern + StringEscapeUtils.escapeHtml(node.getEndPattern().pattern()) + "</span>";
                    result = "from " + start + " to " + end;
                } else {
                    result = "matches " + start;
                }
            } else {
                result = result + "-</span>";
            }
            return result;
        }
    }

    private List<ParserNodeMetaData> parserTree = null;

    private Map<String, String> styleMap = null;

    public ConfigureFormatsComponent() {
        HtmlOutputFormat format = super.getSelectedFormatAsInstance();
        this.styleMap = format.getAllStyles();
    }

    public void select(ActionEvent e) {
        this.parserTree = null;
    }

    public void addStyle(ActionEvent e) {
        if (styleMap.size() < 20) {
            String key = "ps_style" + String.valueOf(styleMap.size());
            styleMap.put(key, "");
        }
    }

    public void reset(ActionEvent e) {
        String key = super.getSelectedFormat();
        HtmlOutputFormat format = HtmlFormatFactory.getAllFormats().get(key);
        super.availableFormatsMapping.put(key, format);
        this.parserTree = null;
    }

    public List<ParserNodeMetaData> getSourceParserTree() {
        if (this.parserTree == null) {
            HtmlOutputFormat format = super.getSelectedFormatAsInstance();
            InputNodeProcessor proc = format.getInputFormat().getProcessorTree();
            this.parserTree = new ArrayList<ParserNodeMetaData>();
            this.refresh(this.parserTree, proc, 0);
            this.styleMap = super.getSelectedFormatAsInstance().getAllStyles();
        }
        return parserTree;
    }

    public List<String> getStyleNames() {
        return new ArrayList<String>(this.styleMap.keySet());
    }

    public Map<String, String> getStyleMap() {
        return styleMap;
    }

    private void refresh(List<ParserNodeMetaData> list, InputNodeProcessor nodeProc, int level) {
        ParserNodeMetaData metaData = new ParserNodeMetaData();
        metaData.level = level;
        metaData.proc = nodeProc;
        list.add(metaData);
        if (nodeProc instanceof InputNodeParentProc) {
            InputNodeParentProc _proc = (InputNodeParentProc) nodeProc;
            for (InputNodeProcessor childProc : _proc.getChildProcessors()) {
                refresh(list, childProc, level + 1);
            }
        }
    }
}
