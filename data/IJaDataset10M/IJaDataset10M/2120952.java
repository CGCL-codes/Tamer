package pedro.io;

import pedro.mda.model.*;
import pedro.system.*;
import pedro.util.PedroXMLParsingUtility;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;

public class PedroDataFileWriter {

    private RecordModelFactory recordModelFactory;

    /**
     * the flag indicates whether only the record or the record and all
     * its child records are to be written to file.  for data files,
     * child records are always written but this is optional in templates
     */
    private boolean writeChildRecords;

    private RecordModel topRecordModel;

    private boolean writeTemplate;

    private boolean omitModelStamp;

    public PedroDataFileWriter(PedroFormContext pedroFormContext, boolean writeTemplate, boolean writeChildRecords) {
        this.recordModelFactory = (RecordModelFactory) pedroFormContext.getApplicationProperty(PedroApplicationContext.RECORD_MODEL_FACTORY);
        this.writeTemplate = writeTemplate;
        this.writeChildRecords = writeChildRecords;
    }

    /**
     * convenience method for getting an ID value associated with a
     * record model reference
     */
    private String getRecordIDValue(RecordModelReference reference) {
        RecordModel referencedRecord = reference.getRecordModel();
        IDFieldModel idFieldModel = referencedRecord.getIDField();
        return idFieldModel.getValue();
    }

    /**
     * Convenience method for escaping XML characters in a given String.
     * It translates:
     * <ul>
     * <li>&amp; - &gt; &amp;amp;</li>
     * <li>&lt; -&gt; &amp;lt;</li>
     * <li>&gt; -&gt; &amp;gt;</li>
     * <li>&quot; -&gt; &amp;quot;</li>
     * <li>&apos; -&gt; &amp;apos;</li>
     * </ul>
     * The method does not throw a NullPointerException if the given String
     * is null. Instead, it returns a null.
     *
     * @param str
     * @return
     */
    public static String escapeXml(String str) {
        if (str != null) {
            char[] chars = str.toCharArray();
            StringBuffer buffer = new StringBuffer();
            for (int iii = 0; iii < chars.length; iii++) {
                char aChar = chars[iii];
                switch(aChar) {
                    case '&':
                        buffer.append("&amp;");
                        break;
                    case '<':
                        buffer.append("&lt;");
                        break;
                    case '>':
                        buffer.append("&gt;");
                        break;
                    case '"':
                        buffer.append("&quot;");
                        break;
                    case '\'':
                        buffer.append("&apos;");
                        break;
                    default:
                        buffer.append(aChar);
                        break;
                }
            }
            return buffer.toString();
        } else {
            return null;
        }
    }

    public void write(OutputStream outputStream, RecordModel recordModel) throws IOException {
        OutputStreamWriter out = new OutputStreamWriter(outputStream, "UTF-8");
        topRecordModel = recordModel;
        out.write("<?xml version = \"1.0\" encoding = \"UTF-8\"?>");
        out.write("\n");
        if (writeTemplate == true) {
            if (omitModelStamp == true) {
                out.write("<template>");
            } else {
                out.write("<template ");
                out.write(recordModelFactory.getModelStamp());
                out.write(">");
            }
            out.flush();
            visitRecordModel(0, topRecordModel, out);
            out.write("</template>");
        } else {
            visitRecordModel(0, topRecordModel, out);
        }
        out.flush();
        out.close();
        outputStream.close();
    }

    protected void visitRecordModel(int indentationLevel, RecordModel recordModel, OutputStreamWriter out) throws IOException {
        StringBuffer recordIndentation = new StringBuffer();
        for (int i = 0; i < indentationLevel; i++) {
            recordIndentation.append("\t");
        }
        StringBuffer fieldIndentation = new StringBuffer();
        for (int i = 0; i < indentationLevel + 1; i++) {
            fieldIndentation.append("\t");
        }
        StringBuffer dataIndentation = new StringBuffer();
        for (int i = 0; i < indentationLevel + 2; i++) {
            dataIndentation.append("\t");
        }
        String name = recordModel.getRecordClassName();
        if (recordModel == topRecordModel) {
            String styleSheetTag = recordModelFactory.getStyleSheetStamp();
            if (styleSheetTag != null) {
                out.write(styleSheetTag);
            }
            out.write(recordIndentation.toString() + "<" + name);
            writeAttributes(out, recordModel);
            if (writeTemplate == false) {
                if (omitModelStamp == false) {
                    out.write(" ");
                    out.write(recordModelFactory.getModelStamp());
                }
            }
            out.write(">");
        } else {
            out.write("\n");
            out.write(recordIndentation.toString() + "<" + name);
            writeAttributes(out, recordModel);
            out.write(">");
        }
        out.flush();
        ArrayList fields = recordModel.getElementFields();
        int numberOfFields = fields.size();
        boolean hasAnyContent = false;
        for (int i = 0; i < numberOfFields; i++) {
            DataFieldModel currentFieldModel = (DataFieldModel) fields.get(i);
            String fieldName = currentFieldModel.getName().trim();
            if (currentFieldModel instanceof EditFieldModel) {
                EditFieldModel editField = (EditFieldModel) currentFieldModel;
                String editFieldValue = PedroXMLParsingUtility.escapeXml(editField.getValue());
                if (editFieldValue.equals(PedroResources.NO_ATTRIBUTE_VALUE) == false) {
                    hasAnyContent = true;
                    if (editFieldValue.equals(PedroResources.EMPTY_STRING) == false) {
                        if (editField.isContent()) {
                            out.write("\n");
                            out.write(fieldIndentation.toString());
                            out.write(editFieldValue);
                        } else {
                            out.write("\n");
                            out.write(fieldIndentation.toString() + "<" + fieldName + ">");
                            out.write(editFieldValue);
                            out.write("</" + fieldName + ">");
                        }
                    }
                    out.flush();
                }
            } else if ((currentFieldModel instanceof ListFieldModel) && (writeChildRecords == true)) {
                ListFieldModel listField = (ListFieldModel) currentFieldModel;
                ArrayList listChildren = listField.getChildren();
                int numberOfChildren = listChildren.size();
                for (int j = 0; j < numberOfChildren; j++) {
                    hasAnyContent = true;
                    Object currentChild = listChildren.get(j);
                    visitListChild(currentChild, indentationLevel, out);
                }
                out.flush();
            }
        }
        if (hasAnyContent) {
            out.write("\n");
            out.write(recordIndentation.toString());
        }
        out.write("</");
        out.write(name);
        out.write(">");
        out.write("\n");
        out.flush();
    }

    private void visitListChild(Object listChild, int indentationLevel, OutputStreamWriter out) throws IOException {
        if (listChild instanceof RecordModel) {
            RecordModel childRecordModel = (RecordModel) listChild;
            visitRecordModel(indentationLevel + 1, childRecordModel, out);
        }
    }

    /**
     * writes out all the attribute edit field values followed by all the
     * attributes that describe referenced list items
     */
    private void writeAttributes(OutputStreamWriter out, RecordModel recordModel) throws IOException {
        ArrayList attributeFields = recordModel.getAttributeFields();
        int numberOfAttributes = attributeFields.size();
        if (numberOfAttributes > 0) {
            out.write(" ");
        }
        for (int i = 0; i < numberOfAttributes; i++) {
            EditFieldModel attribute = (EditFieldModel) attributeFields.get(i);
            String name = attribute.getName();
            String value = PedroXMLParsingUtility.escapeXml(attribute.getValue());
            if (!value.equals(PedroResources.EMPTY_STRING)) {
                out.write(name);
                out.write("=");
                out.write("\"");
                out.write(value);
                out.write("\" ");
            }
        }
        RecordModelUtility recordModelUtility = new RecordModelUtility();
        ArrayList listFields = recordModelUtility.getListFields(recordModel);
        int numberOfListFields = listFields.size();
        for (int i = 0; i < numberOfListFields; i++) {
            ListFieldModel currentListModel = (ListFieldModel) listFields.get(i);
            if (currentListModel.supportsReferencing() == true) {
                String idRefsAttributeName = currentListModel.getIDREFSAttributeName();
                if (idRefsAttributeName != null) {
                    ArrayList referencedRecords = recordModelUtility.getReferenceListChildren(currentListModel);
                    writeIDRefsAttribute(idRefsAttributeName, referencedRecords, out);
                    out.write(" ");
                }
            }
        }
    }

    /**
     * convenience method for writing out attributeName="x1..x2..x3" from a collection
     * of referenced records
     */
    private void writeIDRefsAttribute(String attributeName, ArrayList referencedRecords, OutputStreamWriter out) throws IOException {
        int numberOfReferences = referencedRecords.size();
        if (numberOfReferences == 0) {
            return;
        }
        out.write(attributeName);
        out.write("=");
        out.write("\"");
        int lastRecordIndex = referencedRecords.size() - 1;
        for (int i = 0; i < numberOfReferences; i++) {
            RecordModelReference currentReference = (RecordModelReference) referencedRecords.get(i);
            out.write(getRecordIDValue(currentReference));
            if (i != lastRecordIndex) {
                out.write(" ");
            }
        }
        out.write("\"");
    }

    public void omitModelStamp() {
        omitModelStamp = true;
    }
}
