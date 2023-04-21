package au.edu.uq.itee.maenad.pronto.resources;

import au.edu.uq.itee.maenad.pronto.Pronto;
import au.edu.uq.itee.maenad.pronto.dataaccess.OntologyDao;
import au.edu.uq.itee.maenad.pronto.model.Ontology;
import au.edu.uq.itee.maenad.pronto.model.OntologyVersion;
import au.edu.uq.itee.maenad.pronto.model.User;
import au.edu.uq.itee.maenad.restlet.EntityResource;
import au.edu.uq.itee.maenad.restlet.errorhandling.InitializationException;
import au.edu.uq.itee.maenad.restlet.errorhandling.NoDataFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class OntologyHistoryResource extends EntityResource<Ontology, OntologyDao, User> {

    public static final class Matrix {

        private final List<String> columnNames;

        private final Map<String, Set<String>> data;

        public Matrix() {
            this.columnNames = new ArrayList<String>();
            this.data = new HashMap<String, Set<String>>();
        }

        public List<String> getColumnNames() {
            return columnNames;
        }

        public Map<String, Set<String>> getData() {
            return data;
        }

        public Set<String> getRow(String rowLabel) {
            Set<String> result = data.get(rowLabel);
            if (result == null) {
                result = new HashSet<String>();
                data.put(rowLabel, result);
            }
            return result;
        }

        public Map<String, List<Boolean>> getAsCrosstable() {
            Map<String, List<Boolean>> result = new HashMap<String, List<Boolean>>();
            for (String rowLabel : data.keySet()) {
                List<Boolean> resultRow = new ArrayList<Boolean>(columnNames.size());
                Set<String> row = getRow(rowLabel);
                for (String colName : columnNames) {
                    if (row.contains(colName)) {
                        resultRow.add(Boolean.TRUE);
                    } else {
                        resultRow.add(Boolean.FALSE);
                    }
                }
                result.put(rowLabel, resultRow);
            }
            return result;
        }
    }

    public OntologyHistoryResource() throws InitializationException {
        super(Pronto.getConfiguration().getOntologyDao());
        setContentTemplateName("ontology_history.html");
    }

    @Override
    protected void fillDatamodel(Map<String, Object> datamodel) throws NoDataFoundException {
        super.fillDatamodel(datamodel);
        Ontology ontology = (Ontology) datamodel.get(getTemplateObjectName());
        Matrix[] result = createNameMatrices(ontology);
        datamodel.put("classMatrix", result[0]);
        datamodel.put("propertyMatrix", result[1]);
    }

    static Matrix[] createNameMatrices(Ontology ontology) {
        Matrix classNameMatrix = new Matrix();
        Matrix propertyNameMatrix = new Matrix();
        int colCount = 0;
        for (OntologyVersion version : ontology.getVersions()) {
            colCount++;
            classNameMatrix.columnNames.add(version.getNumber());
            for (String className : version.getClassNames()) {
                classNameMatrix.getRow(className).add(version.getNumber());
            }
            propertyNameMatrix.columnNames.add(version.getNumber());
            for (String propertyName : version.getPropertyNames()) {
                propertyNameMatrix.getRow(propertyName).add(version.getNumber());
            }
        }
        Matrix[] result = new Matrix[] { classNameMatrix, propertyNameMatrix };
        return result;
    }
}
