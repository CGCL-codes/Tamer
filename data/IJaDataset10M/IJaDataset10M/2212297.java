package au.com.bytecode.opencsv.bean;

import au.com.bytecode.opencsv.CSVReader;
import java.beans.PropertyDescriptor;
import java.beans.PropertyEditor;
import java.beans.PropertyEditorManager;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

public class CsvToBean {

    public CsvToBean() {
    }

    public List parse(MappingStrategy mapper, Reader reader) {
        try {
            CSVReader csv = new CSVReader(reader);
            return parse(mapper, csv);
        } catch (Exception e) {
            throw new RuntimeException("Error parsing CSV!", e);
        }
    }

    public List parse(MappingStrategy mapper, CSVReader csv) {
        try {
            mapper.captureHeader(csv);
        } catch (Exception e) {
            throw new RuntimeException("Error parsing CSV header.", e);
        }
        int lineNum = 0;
        List list = new ArrayList();
        try {
            String[] line;
            while (null != (line = csv.readNext())) {
                Object obj = processLine(mapper, line);
                list.add(obj);
                lineNum++;
            }
        } catch (Exception e) {
            throw new RuntimeException("Parsing error on line [" + lineNum + "]", e);
        }
        return list;
    }

    protected Object processLine(MappingStrategy mapper, String[] line) {
        Object bean;
        try {
            bean = mapper.createBean();
        } catch (InstantiationException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
        for (int col = 0; col < line.length; col++) {
            try {
                String value = line[col];
                PropertyDescriptor prop;
                prop = mapper.findDescriptor(col);
                if (null != prop) {
                    Object obj = convertValue(value, prop);
                    prop.getWriteMethod().invoke(bean, new Object[] { obj });
                }
            } catch (Exception e) {
                throw new RuntimeException("Parsing erro on column [" + col + "]", e);
            }
        }
        return bean;
    }

    protected Object convertValue(String value, PropertyDescriptor prop) throws InstantiationException, IllegalAccessException {
        PropertyEditor editor = getPropertyEditor(prop);
        Object obj = value;
        if (null != editor) {
            editor.setAsText(value);
            obj = editor.getValue();
        }
        return obj;
    }

    protected PropertyEditor getPropertyEditor(PropertyDescriptor desc) throws InstantiationException, IllegalAccessException {
        Class cls = desc.getPropertyEditorClass();
        if (null != cls) return (PropertyEditor) cls.newInstance();
        return PropertyEditorManager.findEditor(desc.getPropertyType());
    }
}
