package br.ufmg.saotome.arangi.model;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRField;
import br.ufmg.saotome.arangi.dto.Parameter;

/**
 * 
 * @author Cesar Correia
 *
 */
public class BasicJRDataSource implements JRDataSource {

    private Iterator iterator;

    private Object[] register;

    private Map fieldIndexes;

    public BasicJRDataSource() {
    }

    public BasicJRDataSource(List data, Parameter[] fields) {
        fieldIndexes = new HashMap();
        for (int i = 0; i < fields.length; i++) {
            fieldIndexes.put(fields[i].getName(), new Integer(i));
        }
        iterator = data.iterator();
    }

    public Object getFieldValue(JRField jrField) throws JRException {
        int index = ((Integer) fieldIndexes.get(jrField.getName())).intValue();
        return register[index];
    }

    public boolean next() throws JRException {
        if (iterator.hasNext()) {
            register = (Object[]) iterator.next();
            return true;
        }
        return false;
    }
}
