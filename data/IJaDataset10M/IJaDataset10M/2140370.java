package br.ufg.integrate.xml;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import br.ufg.integrate.exception.IntegrateException;

/**
 * @author Rogerio
 * @version 0.1
 *
 * Formata um ResultSet recebido como argumento em XML.
 */
public class XMLResultSetFile {

    private ArrayList<String> cols;

    private ArrayList<ArrayList<String>> values;

    private ResultSetMetaData rsmd;

    private ResultSet rs;

    private String idTarget;

    private String encoding;

    private String namespace;

    private int tab = 0;

    private String xmlVersion;

    /**
	 * Construtor default que define a vers�o do XML,
	 * a codifica��o e a localiza��o do namespace que define
	 * as vari�veis definidas pelo XML Schema.
	 * 
	 * Os valores default s�o:
	 * 	- vers�o do XML: 	1.0
	 *  - encoding:			UTF-8
	 *  - namespace:		http://www.w3.org/2001/XMLSchema
	 * 
	 * Antes de utilizar o m�todo getXml(), deve-se executar o
	 * m�todo setTarget(), passando o Resultset que ser� utilizado.
	 * 
	 */
    public XMLResultSetFile() {
        this.xmlVersion = "1.0";
        this.encoding = "UTF-8";
        this.namespace = "http://www.w3.org/2001/XMLSchema";
    }

    /**
	 * 
	 * @param idTarget
	 * @param rs
	 * @throws IntegrateException
	 */
    public XMLResultSetFile(String idTarget, ResultSet rs) throws IntegrateException {
        try {
            this.idTarget = idTarget;
            this.rs = rs;
            rsmd = rs.getMetaData();
            this.xmlVersion = "1.0";
            this.encoding = "UTF-8";
            this.namespace = "http://www.w3.org/2001/XMLSchema";
        } catch (SQLException e) {
            throw new IntegrateException("XMLResultSetFile: error with getMetadata " + "from ResultSet.", e);
        }
    }

    /**
	  * Construtor que permite a defini��o da vers�o do arquivo XML,
	  * a codifica��o e o local do namespace.
	  * 
	  * @param xmlVersion Xml version. Default value is "1.0".
	  * @param encoding Character encoding. Default value is "UTF-8".
	  * @param namespace Namespace location. Default value is "hhtp://www.w3.org/2001/XMLSchema"
	 * @throws XMLException 
	  */
    public XMLResultSetFile(String idTarget, ResultSet rs, String xmlVersion, String encoding, String namespace) throws IntegrateException {
        try {
            this.idTarget = idTarget;
            this.rs = rs;
            rsmd = rs.getMetaData();
            this.xmlVersion = xmlVersion;
            this.encoding = encoding;
            this.namespace = namespace;
        } catch (SQLException e) {
            throw new IntegrateException("XMLResultSetFile: error with getMetadata " + "from ResultSet.", e);
        }
    }

    /**
	 * @throws XMLException 
	 * 
	 */
    private ArrayList<String> getColumns() throws IntegrateException {
        try {
            cols = new ArrayList<String>(rsmd.getColumnCount());
            for (int i = 0; i <= rsmd.getColumnCount() - 1; i++) {
                cols.add(rsmd.getColumnName(i + 1));
            }
            return cols;
        } catch (SQLException e) {
            throw new IntegrateException("XMLResultSetFile: error with metadata " + "from ResultSet.", e);
        }
    }

    /**
	 * Gera String com o conte�do do arquivo XML que
	 * representa o ResultSet informado.
	 * 
	 * @return ResultSet formatado em XML.
	 * @throws XMLException
	 */
    public String getXml() throws IntegrateException {
        StringBuilder sb = new StringBuilder();
        identation(sb, ++tab);
        sb.append("<result id='" + idTarget + "'>\n");
        insertTuples(sb);
        identation(sb, ++tab);
        sb.append("</result>\n");
        identation(sb, --tab);
        return sb.toString();
    }

    /**
	 * M�todo que auxilia na inser��o de identa��es em cada linha.
	 */
    private StringBuilder identation(StringBuilder sb, int count) {
        for (int i = 0; i < count; i++) {
            sb.append("\t");
        }
        return sb;
    }

    /**
	 * Insere o header do arquivo XML, comum para todos eles.
	 */
    public String insertHeader() {
        return "<?xml version=\"" + xmlVersion + "\" encoding=\"" + encoding + "\"?>\n";
    }

    /**
	 * Insere o namespace definido no arquivo XML.
	 */
    public String insertNamespace() {
        return "<xs:schema xmlns:xs='" + namespace + "'>";
    }

    /**
	 * Insere os valores do ResultSet, montando o corpo
	 * do XML que cont�m os registros, com o nome e o tipo
	 * da coluna e o seu respectivo valor.
	 * 
	 * @throws IntegrateException 
	 */
    private StringBuilder insertTuples(StringBuilder sb) throws IntegrateException {
        setValues();
        for (int i = 0; i < values.size(); i++) {
            identation(sb, ++tab);
            sb.append("<tuple>\n");
            ++tab;
            for (int j = 0; j < cols.size(); j++) {
                identation(sb, tab);
                sb.append("<attribute name='" + cols.get(j) + "'>");
                if ((values.get(i)).get(j).getClass().toString().equals("class java.sql.Date")) {
                    sb.append((new SimpleDateFormat("dd/MM/yyyy").format((values.get(i)).get(j))) + "</attribute>\n");
                } else {
                    sb.append((values.get(i)).get(j).toString() + "</attribute>\n");
                }
            }
            identation(sb, --tab);
            sb.append("</tuple>\n");
            --tab;
        }
        identation(sb, --tab);
        return sb;
    }

    /**
	 * 
	 */
    public void setTargets(String idTarget, ResultSet rs) throws IntegrateException {
        try {
            this.idTarget = idTarget;
            this.rs = rs;
            rsmd = rs.getMetaData();
        } catch (SQLException e) {
            throw new IntegrateException("XMLResultSetFile: error with getMetadata " + "from ResultSet.", e);
        }
    }

    /**
	 * 
	 */
    public void setTargets(String idTarget, ResultSet rs, String xmlVersion, String encoding, String namespace) throws IntegrateException {
        try {
            this.idTarget = idTarget;
            this.rs = rs;
            rsmd = rs.getMetaData();
            this.xmlVersion = xmlVersion;
            this.encoding = encoding;
            this.namespace = namespace;
        } catch (SQLException e) {
            throw new IntegrateException("XMLResultSetFile: error with getMetadata " + "from ResultSet.", e);
        }
    }

    /**
	 * Cria os objetos ArrayList a partir do ResultSet retornado pela
	 * consulta SQL, convertendo cada objeto em String.
	 */
    @SuppressWarnings("unchecked")
    private void setValues() throws IntegrateException {
        try {
            cols = getColumns();
            values = new ArrayList<ArrayList<String>>();
            ArrayList<String> row = new ArrayList<String>(rsmd.getColumnCount());
            while (rs.next()) {
                row.clear();
                for (int i = 1; i <= rsmd.getColumnCount(); i++) {
                    if (rs.getObject(i) != null) {
                        if ((rs.getObject(i).getClass().toString().equals("class java.sql.Date")) || (rs.getObject(i).getClass().toString().equals("class java.sql.Timestamp"))) {
                            row.add(new SimpleDateFormat("dd/MM/yyyy").format(rs.getDate(i)));
                        } else {
                            if ((rs.getObject(i).getClass().toString().equals("class java.sql.Blob")) || (rs.getObject(i).getClass().toString().equals("class java.sql.Clob"))) {
                                row.add("BLOB/CLOB field");
                            } else {
                                row.add(rs.getObject(i).toString());
                            }
                        }
                    } else {
                        row.add("");
                    }
                }
                values.add((ArrayList<String>) row.clone());
            }
        } catch (SQLException e) {
            throw new IntegrateException("XMLResultSetFile: SQLException in setValues().", e);
        }
    }
}
