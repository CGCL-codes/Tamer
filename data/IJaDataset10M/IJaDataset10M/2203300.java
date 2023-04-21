package tml.annotators;

import java.io.IOException;
import java.util.ArrayList;

public class AbstractAnnotator {

    private String fieldName;

    protected ArrayList<String> types;

    public ArrayList<String> getTypes() {
        return types;
    }

    public AbstractAnnotator(String fieldName, String[] types) throws IOException {
        this.fieldName = fieldName;
        this.types = new ArrayList<String>();
        for (String type : types) {
            this.types.add(type);
        }
    }

    /**
	 * @return the fieldName
	 */
    public String getFieldName() {
        return fieldName;
    }
}
