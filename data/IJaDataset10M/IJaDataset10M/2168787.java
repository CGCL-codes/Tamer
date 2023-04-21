package org.ccpo.engine.core;

import org.ccpo.common.api.FieldConstants;
import org.ccpo.common.api.annotations.*;
import org.ccpo.common.api.IOperator;
import org.ccpo.common.core.MetaDataAdapter;
import org.ccpo.common.data.FieldMetadataWrapper;
import org.ccpo.storage.core.StorageManager;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class InjectionEngine {

    private Map<String, Field> inputFields;

    private Map<Integer, Field> inputLayerFields;

    private Field popInjection;

    private Field indInjection;

    private Field rawData;

    private Field rawColumns;

    private Map<String, Field> outputFields;

    private Map<String, Field> setGlobalVars;

    private Map<String, Field> getGlobalVars;

    private IOperator operator;

    public void register(IOperator operator) {
        this.operator = operator;
        inputFields = new HashMap<String, Field>();
        inputLayerFields = new HashMap<Integer, Field>();
        outputFields = new HashMap<String, Field>();
        setGlobalVars = new HashMap<String, Field>();
        getGlobalVars = new HashMap<String, Field>();
        Class classname = operator.getClass();
        FieldMetadataWrapper fmw;
        for (Field field : classname.getDeclaredFields()) {
            for (Annotation annot : field.getDeclaredAnnotations()) {
                if ((fmw = MetaDataAdapter.getMetadataWrapper(annot)) != null) {
                    field.setAccessible(true);
                    if (fmw.getDirection() == FieldConstants.INPUT) {
                        if (!FieldConstants.LAYER.equals(fmw.getName())) {
                            inputFields.put(fmw.getName(), field);
                        } else {
                            inputLayerFields.put(fmw.getLayer(), field);
                        }
                    } else if (fmw.getDirection() == FieldConstants.INPUTOUTPUT) {
                        if (!FieldConstants.LAYER.equals(fmw.getName())) {
                            inputFields.put(fmw.getName(), field);
                        } else {
                            inputLayerFields.put(fmw.getLayer(), field);
                        }
                        outputFields.put(fmw.getName(), field);
                    } else {
                        outputFields.put(fmw.getName(), field);
                    }
                } else if (annot instanceof PopulationId) {
                    field.setAccessible(true);
                    popInjection = field;
                } else if (annot instanceof RawData) {
                    field.setAccessible(true);
                    rawData = field;
                } else if (annot instanceof RawColumns) {
                    field.setAccessible(true);
                    rawColumns = field;
                } else if (annot instanceof IndividualId) {
                    field.setAccessible(true);
                    indInjection = field;
                } else if (annot instanceof GlobalField) {
                    field.setAccessible(true);
                    GlobalField gf = (GlobalField) annot;
                    if ("set".equals(gf.method())) {
                        setGlobalVars.put(gf.name(), field);
                    } else if ("both".equals(gf.method())) {
                        setGlobalVars.put(gf.name(), field);
                        getGlobalVars.put(gf.name(), field);
                    } else {
                        getGlobalVars.put(gf.name(), field);
                    }
                }
            }
        }
    }

    public void inject(Map<String, Object> fields) {
        for (String keys : fields.keySet()) {
            try {
                inputFields.get(keys).set(operator, fields.get(keys));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void injectGlobal(String popuid) {
        if (getGlobalVars != null) {
            for (String names : getGlobalVars.keySet()) {
                try {
                    getGlobalVars.get(names).set(operator, StorageManager.getGlobalVar(popuid, names));
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void extractGlobal(String popuid) {
        if (setGlobalVars != null) {
            for (String names : setGlobalVars.keySet()) {
                try {
                    StorageManager.setGlobalVar(popuid, names, setGlobalVars.get(names).get(operator));
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void injectPopulation(String popuid) {
        if (popInjection != null) {
            try {
                popInjection.set(operator, popuid);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }

    public void injectRawData(List<List<Object>> raw) {
        if (rawData != null) {
            try {
                rawData.set(operator, raw);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }

    public void injectIndividual(String induid) {
        if (indInjection != null) {
            try {
                indInjection.set(operator, induid);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }

    public void injectLayer(Map<Integer, Object> fields) {
        for (Integer keys : inputLayerFields.keySet()) {
            try {
                inputLayerFields.get(keys).set(operator, fields.get(keys));
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }

    public Map<String, Object> extract() {
        Map<String, Object> values = new HashMap<String, Object>();
        for (String field : outputFields.keySet()) {
            try {
                values.put(field, outputFields.get(field).get(operator));
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return values;
    }

    public List<String> getInputFields() {
        return new ArrayList(inputFields.keySet());
    }

    public List<Integer> getLayerInput() {
        return new ArrayList(inputLayerFields.keySet());
    }

    public void injectRawColumns(List<String> columns) {
        if (rawColumns != null) {
            try {
                rawColumns.set(operator, columns);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }
}
