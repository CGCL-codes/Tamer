package org.vramework.mvc.web.compiletimetags;

import org.vramework.commons.config.VConf;
import org.vramework.commons.datatypes.Triple;
import org.vramework.commons.datatypes.VStrBuilder;
import org.vramework.mvc.IMarkupTag;
import org.vramework.mvc.TagParser;
import org.vramework.mvc.web.ICompileTimeTagRenderer;

/**
 * The ForEach renders a loop and declares a loop-local variable which can be used inside each loop. It starts the
 * iteration with the object at the index defined by the local variable "startRowLocalVar".
 * 
 * @author thomas.mahringer
 */
public class ForEach implements ICompileTimeTagRenderer {

    protected static final String lf = VConf.getLineSep();

    /**
   * The declartion of the iteration. Required. <br />
   * Sample: <code>iterate=Customer customer: model</code>
   */
    public static final String Iterate = "iterate";

    /**
   * The max number of rows to be rendered. Required.
   */
    public static final String pageSize = "pagesize";

    /**
   * The id of the list we are iterating/paging over. Required.
   */
    public static final String listId = "listId";

    /**
   * The name of the page local variable defining the start row.
   */
    public static final String startRowLocalVar = "startRowLocalVar";

    @Override
    public String render(IMarkupTag tag) {
        VStrBuilder htmlGen = new VStrBuilder(256);
        AttributeList attributeContainer = tag.getAttributeList();
        String iterateExpression = (String) attributeContainer.getAttribute(Iterate, true, true).getValue();
        Triple<String, String, String> classVarAndList = TagParser.extractClassVarAndListName(iterateExpression, tag);
        String clazz = classVarAndList.getKey();
        String iteratedVariableName = classVarAndList.getValue();
        String listName = classVarAndList.getThird();
        int tagBeginPos = tag.getBeginPos();
        String pageSizeStr = attributeContainer.getAttributeStringValue(pageSize, "Integer.MAX_VALUE", false);
        String pageSizeVarName = "pageSize" + tagBeginPos;
        htmlGen.append("  int ", pageSizeVarName, " = ", pageSizeStr, ";", lf);
        String castedListName = iteratedVariableName + "List" + tagBeginPos;
        htmlGen.append("  List<", clazz, "> ", castedListName, " = model  == null ? null : (List<", clazz, ">) ", listName, ";", lf);
        String loopInxVarName = "inx" + tagBeginPos;
        String listSizeVarName = castedListName + "Size";
        htmlGen.append("  int ", listSizeVarName, " = ", castedListName, " == null ? 0 : ", castedListName, ".size();", lf);
        String startRowAttrib = attributeContainer.getAttributeStringValue(startRowLocalVar, false);
        if (startRowAttrib != null) {
            String startRowVarName = (String) startRowAttrib;
            htmlGen.append("  ", startRowVarName, " = ", startRowVarName, " < 0 ? 0: ", startRowVarName, ";", lf);
            htmlGen.append("  ", startRowVarName, " = ", startRowVarName, " > ", listSizeVarName, " ? ", listSizeVarName, ": ", startRowVarName, ";", lf);
            htmlGen.append("  int ", loopInxVarName, " = ", startRowVarName, ";", lf);
            htmlGen.append("  ", pageSizeVarName, " = ", pageSizeVarName, " + ", startRowVarName, ";", lf);
        } else {
            htmlGen.append("  int ", loopInxVarName, " = 0;", lf);
        }
        htmlGen.append("  while (", loopInxVarName, " < ", pageSizeVarName, " && ", loopInxVarName, " < ", listSizeVarName, ") {", lf);
        htmlGen.append("  ", clazz, " ", iteratedVariableName, " = ", castedListName, ".get(", loopInxVarName, ");", lf);
        htmlGen.append("  ", loopInxVarName, "++", ";", lf);
        return htmlGen.toString();
    }
}
