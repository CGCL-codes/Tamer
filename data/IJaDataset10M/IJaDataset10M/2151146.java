package org.fao.fenix.domain.filterbuilder;

import java.util.ArrayList;
import java.util.List;
import org.fao.fenix.domain.exception.FenixException;
import org.fao.fenix.domain.info.dataset.Dataset;
import org.fao.fenix.domain.info.pattern.DatasetUtil;

public class FilterBuilder4FunctionOn2Datasets implements FilterBuilder {

    Dataset dataset1;

    Dataset dataset2;

    String joinField;

    String valueField1;

    String valueField2;

    Function function;

    public void setDataset1(Dataset dataset1) {
        if (!DatasetUtil.getDatasetClassMap().containsKey(dataset1.getClass())) throw new FenixException("Dataset " + dataset1.getClass() + " is not valid");
        this.dataset1 = dataset1;
    }

    public void setDataset2(Dataset dataset2) {
        if (!DatasetUtil.getDatasetClassMap().containsKey(dataset2.getClass())) throw new FenixException("Dataset " + dataset2.getClass() + " is not valid");
        this.dataset2 = dataset2;
    }

    public void setValueField1(String valueField1) {
        this.valueField1 = valueField1;
    }

    public void setValueField2(String valueField2) {
        this.valueField2 = valueField2;
    }

    void validate(Dataset dataset, String field) {
        if (dataset == null) throw new FenixException("Dataset is null");
        List<String> contentFields = new DatasetUtil().getContentFields(dataset);
        boolean found = false;
        for (String string : contentFields) {
            if (field.equals(string)) found = true;
        }
        if (!found) throw new FenixException("This field (" + field + ") is not part of the content of this Dataset (" + dataset + ")");
    }

    public void validateInput() {
        List<Object> inputList = new ArrayList<Object>();
        inputList.add(dataset1);
        inputList.add(dataset2);
        inputList.add(joinField);
        inputList.add(valueField1);
        inputList.add(valueField2);
        inputList.add(function);
        boolean nullsFound = false;
        for (Object object : inputList) {
            if (object == null) nullsFound = true;
        }
        if (nullsFound) throw new FenixException("On or more of the 6 input values contains null values");
        if (dataset1.getResourceId() == null || dataset2.getResourceId() == null) throw new FenixException("ResourceId of on or both of the dataset(s) are null");
        DatasetUtil du = new DatasetUtil();
        boolean foundValueField1 = false;
        boolean foundjoinField = false;
        for (String contentField : du.getContentFields(dataset1)) {
            if (contentField.equals(valueField1)) foundValueField1 = true;
            if (contentField.equals(joinField)) foundjoinField = true;
        }
        if (!foundValueField1) throw new FenixException("The valueField1 is invalid (" + valueField1 + ") because it is not one of the contentfields of dataset1.");
        if (!foundjoinField) throw new FenixException("The joinField is invalid (" + joinField + ") because it is not one of the contentfields of dataset1.");
        boolean foundValueField2 = false;
        foundjoinField = false;
        for (String contentField : du.getContentFields(dataset2)) {
            if (contentField.equals(valueField2)) foundValueField2 = true;
            if (contentField.equals(joinField)) foundjoinField = true;
        }
        if (!foundValueField2) throw new FenixException("The valueField2 is invalid (" + valueField2 + ") because it is not one of the contentfields of dataset2.");
        if (!foundjoinField) throw new FenixException("The joinField is invalid (" + joinField + ") because it is not one of the contentfields of dataset2.");
    }

    public void setJoinField(String joinField) {
        this.joinField = joinField;
    }

    public void setFunction(Function function) {
        this.function = function;
    }

    public String composeFilter() {
        validateInput();
        String dataset1ClassName = dataset1.getClass().getSimpleName();
        String dataset2ClassName = dataset1.getClass().getSimpleName();
        String f = Function.functionMap.get(function);
        String queryString = "select new org.fao.fenix.domain.filterbuilder.FilterResultObject(c1, c2, c1." + valueField1 + f + "c2." + valueField2 + ")" + " from " + dataset1ClassName + " d1 inner join d1.contentList c1, " + dataset2ClassName + "  d2 inner join d2.contentList c2" + " where c1." + joinField + " = c2." + joinField + " and d1.resourceId = " + dataset1.getResourceId() + " and d2.resourceId = " + dataset2.getResourceId();
        System.out.println(queryString);
        return queryString;
    }
}
