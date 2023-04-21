package ti.plato.components.logger.dialogs;

import java.util.ArrayList;
import java.util.List;
import org.eclipse.jface.action.Action;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import ti.plato.components.logger.constants.LoggerConstants;
import ti.plato.components.logger.filters.FilterCombinationContent;
import ti.plato.components.logger.filters.FilteringManagement;
import ti.plato.components.logger.filters.FormulaCombinationContent;
import ti.plato.shared.types.ScriptCompiler;
import ti.plato.ui.images.util.ImagesUtil;
import ti.plato.ui.views.properties.PropertiesElement;

public class ConfigureFiltersFormula extends ConfigureFiltersTab {

    public ConfigureFiltersFormula(TabFolder tabFolder, TabItem tabItem, String note) {
        super(FilteringManagement.FORMULA_FILTERS_ID, "Formula", "e_formulaDiagram", tabFolder, tabItem, LoggerConstants.formula_ConfigureFiltersTreeColumnItems);
    }

    @Override
    protected ArrayList<PropertiesElement> createPropertiesActions() {
        ArrayList<PropertiesElement> propertiesElements = new ArrayList<PropertiesElement>();
        int formulaTreeColumnCount = LoggerConstants.formula_ConfigureFiltersTreeColumnItems.length;
        List<FilterCombinationContent> formula_CombinationList = getCombos();
        int formulaCombinationListCount = formula_CombinationList.size();
        int formulaCombinationListIndex;
        for (formulaCombinationListIndex = 0; formulaCombinationListIndex < formulaCombinationListCount; formulaCombinationListIndex++) {
            final FormulaCombinationContent content = (FormulaCombinationContent) formula_CombinationList.get(formulaCombinationListIndex);
            String field = content.getField();
            String formula = content.getFormula();
            String timeShift = content.getTimeShift();
            String unit = content.getUnit();
            int output = content.getOutput();
            final Action[] actionRootArray = new Action[formulaTreeColumnCount];
            int[] operationRootArray = new int[formulaTreeColumnCount];
            actionRootArray[LoggerConstants.formula_TreeColumnPos] = new Action() {

                public void run() {
                }
            };
            actionRootArray[LoggerConstants.formula_TreeColumnPos].setText(content.name);
            actionRootArray[LoggerConstants.formula_TreeColumnPos].setImageDescriptor(ImagesUtil.getImageDescriptor("e_lozenge"));
            operationRootArray[LoggerConstants.formula_TreeColumnPos] = -1;
            actionRootArray[LoggerConstants.formula_TreeFieldPos] = new Action() {

                public void run() {
                    String newField = getId();
                    content.setField(newField);
                    setToolTipText(getId());
                    FormulaCombinationContent formulaTemplate = new FormulaCombinationContent(content.name, content.getField(), null, null, null, -1);
                    FilteringManagement.getFormulaFilters().modifyFilter(tree.getFilterName(), formulaTemplate);
                }
            };
            actionRootArray[LoggerConstants.formula_TreeFieldPos].setId(field);
            actionRootArray[LoggerConstants.formula_TreeFieldPos].setToolTipText(field);
            operationRootArray[LoggerConstants.formula_TreeFieldPos] = PropertiesElement.operationTypeRegExp;
            String comboContent = "";
            for (int j = 0; j < ScriptCompiler.TRANSFORMS.length; j++) {
                if (j != 0) comboContent += "\n";
                comboContent += ScriptCompiler.TRANSFORMS[j][1];
            }
            actionRootArray[LoggerConstants.formula_TreeFormulaPos] = new Action() {

                public void run() {
                    String newFormula = getId();
                    content.setFormula(newFormula);
                    FormulaCombinationContent formulaTemplate = new FormulaCombinationContent(content.name, null, content.getFormula(), null, null, -1);
                    FilteringManagement.getFormulaFilters().modifyFilter(tree.getFilterName(), formulaTemplate);
                }
            };
            actionRootArray[LoggerConstants.formula_TreeFormulaPos].setToolTipText(comboContent);
            actionRootArray[LoggerConstants.formula_TreeFormulaPos].setId(formula);
            operationRootArray[LoggerConstants.formula_TreeFormulaPos] = PropertiesElement.operationTypeFormulaCombo;
            actionRootArray[LoggerConstants.formula_TreeTimeShiftPos] = new Action() {

                public void run() {
                    String newTimeShift = getId();
                    content.setTimeShift(newTimeShift);
                    FormulaCombinationContent formulaTemplate = new FormulaCombinationContent(content.name, null, null, content.getTimeShift(), null, -1);
                    FilteringManagement.getFormulaFilters().modifyFilter(tree.getFilterName(), formulaTemplate);
                    double[][] result = content.getTimeShiftArray();
                    if (result == null) {
                        System.err.println("result = null");
                    } else {
                        int resultCount = result.length;
                        int resultIndex;
                        for (resultIndex = 0; resultIndex < resultCount; resultIndex++) {
                            System.err.println("result" + resultIndex + " = " + result[resultIndex]);
                        }
                    }
                }
            };
            actionRootArray[LoggerConstants.formula_TreeTimeShiftPos].setId(timeShift);
            actionRootArray[LoggerConstants.formula_TreeTimeShiftPos].setToolTipText(timeShift);
            operationRootArray[LoggerConstants.formula_TreeTimeShiftPos] = PropertiesElement.operationTypeFormulaEdit;
            actionRootArray[LoggerConstants.formula_TreeUnitPos] = new Action() {

                public void run() {
                    String newUnit = getId();
                    content.setUnit(newUnit);
                    setToolTipText(getId());
                    FormulaCombinationContent formulaTemplate = new FormulaCombinationContent(content.name, null, null, null, content.getUnit(), -1);
                    FilteringManagement.getFormulaFilters().modifyFilter(tree.getFilterName(), formulaTemplate);
                }
            };
            actionRootArray[LoggerConstants.formula_TreeUnitPos].setId(unit);
            actionRootArray[LoggerConstants.formula_TreeUnitPos].setToolTipText(unit);
            operationRootArray[LoggerConstants.formula_TreeUnitPos] = PropertiesElement.operationTypeEdit;
            comboContent = "";
            int comboIndex;
            int comboCount = LoggerConstants.outputItems.length;
            for (comboIndex = 0; comboIndex < comboCount; comboIndex++) {
                if (!comboContent.equals("")) comboContent += "\n";
                comboContent += LoggerConstants.outputItems[LoggerConstants.outputItemsDisplayOrder[comboIndex]];
            }
            actionRootArray[LoggerConstants.formula_TreeOutputPos] = new Action() {

                public void run() {
                    String newOutputStr = getId();
                    int newOutputInt = -1;
                    int outputItemsCount = LoggerConstants.outputItems.length;
                    int outputItemsIndex;
                    for (outputItemsIndex = 0; outputItemsIndex < outputItemsCount; outputItemsIndex++) {
                        if (LoggerConstants.outputItems[outputItemsIndex].equals(newOutputStr)) {
                            newOutputInt = outputItemsIndex;
                            break;
                        }
                    }
                    content.setOutput(newOutputInt);
                    FormulaCombinationContent formulaTemplate = new FormulaCombinationContent(content.name, null, null, null, null, content.getOutput());
                    FilteringManagement.getFormulaFilters().modifyFilter(tree.getFilterName(), formulaTemplate);
                }
            };
            actionRootArray[LoggerConstants.formula_TreeOutputPos].setToolTipText(comboContent);
            actionRootArray[LoggerConstants.formula_TreeOutputPos].setId(LoggerConstants.outputItems[output]);
            operationRootArray[LoggerConstants.formula_TreeOutputPos] = PropertiesElement.operationTypeCombo;
            PropertiesElement filter = PropertiesElement.newElement(actionRootArray, operationRootArray);
            propertiesElements.add(filter);
        }
        return propertiesElements;
    }

    @Override
    protected String getCurrentFilterName() {
        return FilteringManagement.getFormulaFilters().getCurrentFilterName();
    }

    @Override
    protected void setCurrentFilterName(String filterName) {
        FilteringManagement.getFormulaFilters().setCurrentFilterName(filterName);
    }
}
