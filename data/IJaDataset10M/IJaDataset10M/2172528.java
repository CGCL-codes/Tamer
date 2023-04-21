package net.sf.colossus.parser;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import net.sf.colossus.ai.objectives.AbstractObjectiveHelper.AllThereIsToKnowAboutYourCreature;
import net.sf.colossus.ai.objectives.AbstractObjectiveHelper.AllThereIsToKnowAboutYourFight;

/**
 *
 * @author dolbeau
 */
public class TacticalObjectiveParserIntValue_GlobalProperty implements TacticalObjectiveParserIntValue {

    private static final Logger LOGGER = Logger.getLogger(TacticalObjectiveParserIntValue_GlobalProperty.class.getName());

    private String p;

    private List<Object> par;

    protected TacticalObjectiveParserIntValue_GlobalProperty(String p, List<Object> par) {
        this.p = p;
        this.par = par;
    }

    public int eval(Map<String, AllThereIsToKnowAboutYourCreature> data, AllThereIsToKnowAboutYourFight global) {
        try {
            if (par == null) {
                Field f = global.getClass().getField(p);
                return f.getInt(global);
            } else {
                if (par.isEmpty()) {
                    Method m = global.getClass().getMethod(p);
                    return (int) (Integer) m.invoke(global);
                } else {
                    Object parametersArray[] = new Object[par.size()];
                    for (int i = 0; i < parametersArray.length; i++) {
                        Object o = par.get(i);
                        if ((o instanceof String) && (((String) o).startsWith("My:") || ((String) o).startsWith("Other:"))) {
                            parametersArray[i] = data.get((String) o).creature;
                        } else if (o instanceof TacticalObjectiveParserBoolValue) {
                            TacticalObjectiveParserBoolValue temp = (TacticalObjectiveParserBoolValue) o;
                            parametersArray[i] = new Boolean(temp.eval(data, global));
                        } else if (o instanceof TacticalObjectiveParserIntValue) {
                            TacticalObjectiveParserIntValue temp = (TacticalObjectiveParserIntValue) o;
                            parametersArray[i] = new Integer(temp.eval(data, global));
                        } else {
                            parametersArray[i] = o;
                        }
                    }
                    Class classArrays[] = new Class[parametersArray.length];
                    for (int i = 0; i < classArrays.length; i++) {
                        classArrays[i] = parametersArray[i].getClass();
                    }
                    Method m = global.getClass().getMethod(p, classArrays);
                    return (int) (Integer) m.invoke(global, parametersArray);
                }
            }
        } catch (Exception e) {
            LOGGER.severe("OUPS, no such field " + p + " in " + global + " (" + e + ")");
            StringBuffer buf = new StringBuffer();
            for (String n : data.keySet()) {
                buf.append("\t" + n + " --> " + data.get(n) + "\n");
            }
            if (par != null) {
                buf.append("\targuments are:\n");
                for (Object o : par) {
                    buf.append("\t\t" + o.toString());
                }
            }
            LOGGER.severe(buf.toString());
            return -1;
        }
    }
}
