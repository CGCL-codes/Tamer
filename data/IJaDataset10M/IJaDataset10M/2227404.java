package org.virbo.dods;

import dods.dap.BaseType;
import dods.dap.BaseTypeFactory;
import dods.dap.DArray;
import dods.dap.DDS;
import dods.dap.DDSException;
import dods.dap.DFloat64;
import dods.dap.DSequence;
import dods.dap.DefaultFactory;
import dods.dap.NoSuchVariableException;
import dods.dap.Server.InvalidParameterException;
import dods.dap.parser.DDSParser;
import dods.dap.parser.ParseException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Enumeration;

/**
 *
 * @author jbf
 */
public class MyDDSParser {

    DDS myDDS;

    /** Creates a new instance of DDSParser */
    public MyDDSParser() {
    }

    public void parse(InputStream in) throws ParseException, DDSException {
        DDSParser p = new DDSParser(in);
        myDDS = new DDS();
        BaseTypeFactory factory = new DefaultFactory();
        p.Dataset(myDDS, factory);
    }

    /**
     * return the dimensions, or null for Sequences
     * @param variable
     * @return
     * @throws dods.dap.NoSuchVariableException
     */
    public int[] getRecDims(String variable) throws NoSuchVariableException {
        BaseType t = myDDS.getVariable(variable);
        int[] result;
        if (t instanceof DSequence) {
            return null;
        } else {
            DArray darray = (DArray) t;
            result = new int[darray.numDimensions()];
            for (int i = 0; i < result.length; i++) {
                try {
                    result[i] = darray.getDimension(i).getStop();
                } catch (InvalidParameterException ex) {
                    ex.printStackTrace();
                }
            }
        }
        return result;
    }

    String[] getVariableNames() {
        Enumeration en = myDDS.getVariables();
        ArrayList<String> result = new ArrayList<String>();
        while (en.hasMoreElements()) {
            result.add(((BaseType) en.nextElement()).getName());
        }
        return result.toArray(new String[result.size()]);
    }
}
