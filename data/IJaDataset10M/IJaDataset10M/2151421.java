package org.netxilia.api.impl.format;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import org.netxilia.api.INetxiliaSystem;
import org.netxilia.api.display.Style;
import org.netxilia.api.display.StyleAttribute;
import org.netxilia.api.display.StyleDefinition;
import org.netxilia.api.display.StyleGroup;
import org.netxilia.api.model.AbsoluteAlias;
import org.netxilia.api.model.CellData;
import org.netxilia.api.model.ISheet;
import org.netxilia.api.model.WorkbookId;
import org.netxilia.api.reference.AreaReference;
import org.netxilia.api.utils.Matrix;
import org.netxilia.api.value.IGenericValue;
import org.netxilia.api.value.NamedValue;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * This formatter provides the values for cell formatters using a set of cells for name and another one for values. The
 * content is dynamic, i.e. it takes the last status of the sheet. The pattern's format is: <br>
 * 
 * 
 * [name]:[workbook];[area ref name];[area ref values]
 * 
 * @author <a href='mailto:ax.craciun@gmail.com'>Alexandru Craciun</a>
 * 
 */
public class SheetValueListFormatter extends AbstractValueListFormatter {

    private static final long serialVersionUID = 1L;

    private static final String ATT_WORKBOOK = "workbook";

    private static final String ATT_NAME_REF = "name-ref";

    private static final String ATT_VALUE_REF = "value-ref";

    private AreaReference nameReference;

    private AreaReference valueReference;

    private AbsoluteAlias nameAlias;

    private AbsoluteAlias valueAlias;

    private WorkbookId workbook;

    @Autowired
    private INetxiliaSystem workbookProcessor;

    public SheetValueListFormatter(StyleDefinition definition) {
        super(definition);
        workbook = new WorkbookId(definition.getAttribute(ATT_WORKBOOK));
        String nameRef = definition.getAttribute(ATT_NAME_REF);
        if (nameRef.contains(":")) {
            nameReference = new AreaReference(nameRef);
        } else {
            nameAlias = new AbsoluteAlias(nameRef);
        }
        String valueRef = definition.getAttribute(ATT_VALUE_REF);
        if (valueRef.contains(":")) {
            valueReference = new AreaReference(valueRef);
        } else {
            valueAlias = new AbsoluteAlias(valueRef);
        }
    }

    public WorkbookId getWorkbook() {
        return workbook;
    }

    public INetxiliaSystem getWorkbookProcessor() {
        return workbookProcessor;
    }

    public void setWorkbookProcessor(INetxiliaSystem workbookProcessor) {
        this.workbookProcessor = workbookProcessor;
    }

    public static StyleDefinition buildDefinition(Style id, StyleGroup group, String name, String description, WorkbookId workbook, AreaReference nameReference, AreaReference valueReference) {
        Collection<StyleAttribute> atts = new ArrayList<StyleAttribute>();
        atts.add(new StyleAttribute(ATT_WORKBOOK, workbook.getKey()));
        atts.add(new StyleAttribute(ATT_NAME_REF, nameReference.toString()));
        atts.add(new StyleAttribute(ATT_VALUE_REF, valueReference.toString()));
        atts.add(new StyleAttribute(StyleAttribute.EDITOR, "select"));
        atts.add(new StyleAttribute(StyleAttribute.PATTERN_TYPE, "values"));
        return new StyleDefinition(id, group, name, description, atts);
    }

    public static StyleDefinition buildDefinition(Style id, StyleGroup group, String name, String description, WorkbookId workbook, AbsoluteAlias nameReference, AbsoluteAlias valueReference) {
        Collection<StyleAttribute> atts = new ArrayList<StyleAttribute>();
        atts.add(new StyleAttribute(ATT_WORKBOOK, workbook.getKey()));
        atts.add(new StyleAttribute(ATT_NAME_REF, nameReference.toString()));
        atts.add(new StyleAttribute(ATT_VALUE_REF, valueReference.toString()));
        atts.add(new StyleAttribute(StyleAttribute.EDITOR, "select"));
        atts.add(new StyleAttribute(StyleAttribute.PATTERN_TYPE, "values"));
        return new StyleDefinition(id, group, name, description, atts);
    }

    @Override
    public List<NamedValue> getValues() {
        ISheet nameSheet = null;
        ISheet valueSheet = null;
        try {
            nameSheet = workbookProcessor.getWorkbook(workbook).getSheet(nameReference != null ? nameReference.getSheetName() : nameAlias.getSheetName());
            AreaReference resolvedNameReference = nameReference;
            if (nameAlias != null) {
                resolvedNameReference = nameSheet.receiveSheet().getNonBlocking().resolveAlias(nameAlias.getAlias());
            }
            Matrix<CellData> nameCells = nameSheet.receiveCells(resolvedNameReference).getNonBlocking();
            Iterator<CellData> nameIterator = nameCells.iterator();
            valueSheet = workbookProcessor.getWorkbook(workbook).getSheet(valueReference != null ? valueReference.getSheetName() : valueAlias.getSheetName());
            AreaReference resolvedValueReference = valueReference;
            if (valueAlias != null) {
                resolvedValueReference = valueSheet.receiveSheet().getNonBlocking().resolveAlias(valueAlias.getAlias());
            }
            Matrix<CellData> valueCells = valueSheet.receiveCells(resolvedValueReference).getNonBlocking();
            Iterator<CellData> valueIterator = valueCells.iterator();
            List<NamedValue> values = new ArrayList<NamedValue>();
            while (nameIterator.hasNext()) {
                IGenericValue nameCell = nameIterator.next().getValue();
                IGenericValue valueCell = valueIterator.hasNext() ? valueIterator.next().getValue() : null;
                if (nameCell != null) {
                    values.add(new NamedValue(nameCell.getStringValue(), valueCell != null ? valueCell.getStringValue() : ""));
                }
            }
            return values;
        } catch (Exception e) {
            return Collections.emptyList();
        }
    }
}
