package au.edu.diasb.danno.example;

import static au.edu.diasb.danno.constants.AnnoteaSchemaConstants.ANNOTATES_PROPERTY;
import static au.edu.diasb.danno.constants.AnnoteaSchemaConstants.CONTEXT_PROPERTY;
import static au.edu.diasb.danno.constants.AnnoteaSchemaConstants.TITLE_PROPERTY;
import static au.edu.diasb.danno.constants.DannotateProtocolConstants.DANNOTATE_CONTEXT;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.servlet.http.HttpServletRequest;
import org.json.JSONObject;
import org.openrdf.model.Resource;
import org.openrdf.model.ValueFactory;
import org.openrdf.repository.RepositoryConnection;
import org.openrdf.repository.RepositoryException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.Assert;
import au.edu.diasb.annotation.dannotate.AnnotationSchema;
import au.edu.diasb.annotation.dannotate.TextAnnotationSchema;
import au.edu.diasb.chico.config.NamedConstantsRegistry;
import au.edu.diasb.chico.config.PropertyTree;
import au.edu.diasb.chico.mvc.BaseController;
import au.edu.diasb.chico.mvc.InvalidRequestParameterException;
import au.edu.diasb.chico.mvc.RequestFailureException;
import au.edu.diasb.chico.mvc.RequestParameterException;
import au.edu.diasb.danno.constants.DannotateProtocolConstants;

/**
 * This is the schema class for the example correction schema
 * 
 * @author scrawley
 */
public class CorrectionSchema extends TextAnnotationSchema implements AnnotationSchema, InitializingBean {

    public static final String RDF_EXAMPLE_URL = "http://metadata.net/2011/01/example#";

    public static final String CORRECTION_RECORD_PROPERTY = RDF_EXAMPLE_URL + "record";

    public static final String CORRECTION_FIELD_PROPERTY = RDF_EXAMPLE_URL + "field";

    public static final String CORRECTION_OLD_PROPERTY = RDF_EXAMPLE_URL + "old";

    public static final String CORRECTION_NEW_PROPERTY = RDF_EXAMPLE_URL + "new";

    static {
        NamedConstantsRegistry.getInstance().registerConstants(CorrectionSchema.class);
    }

    private String baseUrl;

    private DAO dao;

    private static final Pattern recordNamePattern = Pattern.compile("(?:.*?\\|)?#record-([\\w\\._]+).*\\|#field-\\1-([\\w\\._]+).*");

    @Override
    public void afterPropertiesSet() {
        Assert.notNull(dao, "dao property not set");
    }

    public final void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public final void setDao(DAO dao) {
        this.dao = dao;
    }

    @Override
    public void addCreateProperties(HttpServletRequest request, Properties props, PropertyTree tree) throws RequestFailureException {
        super.addCreateProperties(request, props, tree);
        String xc = BaseController.getOptionalParameter(request, DannotateProtocolConstants.DANNOTATE_XCONTEXT, "");
        Matcher m = recordNamePattern.matcher(xc);
        if (!m.matches()) {
            throw new InvalidRequestParameterException(DannotateProtocolConstants.DANNOTATE_XCONTEXT, "The 'xc' doesn't contain '#record-...' and '#field-...' markers");
        }
        String recordName = m.group(1);
        String fieldName = m.group(2);
        String old = dao.getRecord(recordName).getFields().get(fieldName);
        tree.put(CORRECTION_RECORD_PROPERTY, recordName);
        tree.put(CORRECTION_FIELD_PROPERTY, fieldName);
        tree.put(CORRECTION_OLD_PROPERTY, old == null ? "" : old);
        tree.put(TITLE_PROPERTY, "Proposed correction for record '" + recordName + "'");
        props.setProperty(DANNOTATE_CONTEXT, "");
        tree.unset(CONTEXT_PROPERTY);
        tree.put(ANNOTATES_PROPERTY, baseUrl + recordName);
    }

    @Override
    public void addEditProperties(HttpServletRequest request, JSONObject obj, Properties props, PropertyTree tree) {
        super.addEditProperties(request, obj, props, tree);
        tree.put(CORRECTION_RECORD_PROPERTY, obj.optString(CORRECTION_RECORD_PROPERTY));
        tree.put(CORRECTION_FIELD_PROPERTY, obj.optString(CORRECTION_FIELD_PROPERTY));
        tree.put(CORRECTION_OLD_PROPERTY, obj.optString(CORRECTION_OLD_PROPERTY));
        tree.put(CORRECTION_NEW_PROPERTY, obj.optString(CORRECTION_NEW_PROPERTY));
    }

    @Override
    public void addRDFStatements(RepositoryConnection conn, Resource subj, HttpServletRequest request) throws RequestParameterException, RepositoryException {
        ValueFactory vf = conn.getValueFactory();
        addLiteralTriple(conn, vf, subj, CORRECTION_RECORD_PROPERTY, request.getParameter(CORRECTION_RECORD_PROPERTY));
        addLiteralTriple(conn, vf, subj, CORRECTION_FIELD_PROPERTY, request.getParameter(CORRECTION_FIELD_PROPERTY));
        addLiteralTriple(conn, vf, subj, CORRECTION_OLD_PROPERTY, request.getParameter(CORRECTION_OLD_PROPERTY));
        addLiteralTriple(conn, vf, subj, CORRECTION_NEW_PROPERTY, request.getParameter(CORRECTION_NEW_PROPERTY));
        super.addRDFStatements(conn, subj, request);
    }

    @Override
    public String getCreateViewName() {
        return "correctionCreateForm";
    }

    @Override
    public String getEditViewName() {
        return "correctionEditForm";
    }

    @Override
    public String getDisplayViewName() {
        return "correction";
    }

    @Override
    public String getName() {
        return "correction";
    }

    @Override
    public String getHumanReadableName() {
        return "Correction";
    }
}
