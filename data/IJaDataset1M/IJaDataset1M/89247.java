package org.elf.weblayer.controllers.genericmant;

import java.awt.Color;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;
import java.net.URLEncoder;
import java.util.*;
import javax.servlet.http.HttpServletResponse;
import org.elf.businesslayer.BLSession;
import org.elf.businesslayer.dictionary.*;
import org.elf.common.HTMLUtil;
import org.elf.common.JavaScriptUtil;
import org.elf.datalayer.*;
import org.elf.weblayer.*;
import org.elf.weblayer.controls.Button;
import org.elf.weblayer.controls.ColumnDefinitions;
import org.elf.weblayer.controls.Grid;
import org.elf.weblayer.controls.TabbedPane;

/**
 * Controlador que permite hacer un mantenimiento generico de una tabla.
 * @author  <a href="mailto:logongas@users.sourceforge.net">Lorenzo Gonz�lez</a>
 */
public class GenericMant extends Controller {

    private String tableName = null;

    private String searchFieldsLayout = null;

    private String mantFieldsLayout = null;

    private static int maxDefaultFields = 5;

    private static int pageSize = 30;

    private static int gridHeight = 200;

    /**
     * Llamar a este m�todo desde el cliente cuando queramos mostrar la pantalla de b�squeda
     * @param parameters Lista de argumentos para personalizar la forma de la pantalla de b�squeda.
     * @return Una p�gina HTML para realizar la b�squeda de una tabla.
     */
    public final View search(Map parameters) {
        try {
            tableName = (String) parameters.get("tableName");
            searchFieldsLayout = (String) parameters.get("searchFieldsLayout");
            Object[] method = { getTableName(), this.getClass().getMethod("search", java.util.Map.class) };
            if (DLSession.getSecurity().isAccessAuthorized(this, method) == false) {
                return new ViewHTTPError("No tienes permisos para acceder al mantenimiento generico de la tabla:" + getTableName(), HttpServletResponse.SC_FORBIDDEN);
            }
            Button buttonLimpiar = WLSession.getControls().createButton("buttonLimpiar", "Limpiar");
            Button buttonBuscar = WLSession.getControls().createButton("buttonBuscar", "Buscar");
            buttonBuscar.setDefaultButton(true);
            Button buttonNuevo = WLSession.getControls().createButton("buttonNuevo", "Nuevo");
            Button buttonEditar = WLSession.getControls().createButton("buttonEditar", "Editar");
            Button buttonBorrar = WLSession.getControls().createButton("buttonBorrar", "Borrar");
            Grid grid = WLSession.getControls().createGrid("grid", null, getSearchNumPrimaryKeys(), getSearchGridHeight());
            grid.setPageable(true);
            ColumnDefinitions columnDefinitions = getSearchGridColumnDefinitions();
            grid.setColumnDefinitions(columnDefinitions);
            StringBuffer htmlGrid = new StringBuffer();
            htmlGrid.append(grid.toHTML());
            StringBuffer sb = new StringBuffer();
            sb.append("<html>\n");
            sb.append("<head>\n");
            sb.append("<meta http-equiv=\"Content-Type\" content=\"text/html; charset=ISO-8859-1\">\n");
            sb.append("<title>" + HTMLUtil.toHTML(getSearchTitle()) + "</title>\n");
            sb.append("<LINK href=\"" + WLSession.getPaths().getAbsolutePath() + "/controls/css\" rel=\"stylesheet\" type=\"text/css\">\n");
            sb.append("<script type=\"text/javascript\" src=\"" + WLSession.getPaths().getAbsolutePath() + "/common/javascript\"></script>\n");
            sb.append("<script type=\"text/javascript\" src=\"" + WLSession.getPaths().getAbsolutePath() + "/controls/javascript\"></script>\n");
            sb.append("<script type=\"text/javascript\" charset=\"UTF-8\" src=\"" + WLSession.getPaths().getAbsolutePath() + "/common/dictionary/" + getTableName() + "\"></script>\n");
            sb.append("<script type=\"text/javascript\" charset=\"ISO-8859-1\" src=\"" + WLSession.getPaths().getAbsolutePath() + "/common/file/busqueda.js\"></script>");
            sb.append("<script type=\"text/javascript\" src=\"" + WLSession.getPaths().getAbsolutePath() + "/controller/" + this.getControllerName() + "/action/getSearchJavaScript\"></script>\n");
            sb.append("<script type=\"text/javascript\">\n");
            sb.append("  var sqlBusqueda=" + JavaScriptUtil.primitiveObjectToString(getSQLWLSession()) + ";\n");
            sb.append("  var tableName=" + JavaScriptUtil.primitiveObjectToString(getTableName()) + ";\n");
            String jspPage = getMantPage();
            if (parameters.get("mantFieldsLayout") != null) {
                try {
                    jspPage = jspPage + "&mantFieldsLayout=" + URLEncoder.encode((String) parameters.get("mantFieldsLayout"), "UTF-8");
                } catch (RuntimeException rex) {
                    throw rex;
                } catch (Exception ex) {
                    throw new RuntimeException(ex);
                }
            }
            sb.append("  var jspPage=" + JavaScriptUtil.primitiveObjectToString(jspPage) + ";\n");
            sb.append("  var pageSize=" + JavaScriptUtil.primitiveObjectToString(getSearchPageSize()) + ";\n");
            sb.append("  var useBusinessConnection=" + JavaScriptUtil.primitiveObjectToString(useBusinessConnection()) + ";\n");
            sb.append("  var initialSearch=" + JavaScriptUtil.primitiveObjectToString(isInitialSearch()) + ";\n");
            sb.append("</script>\n");
            sb.append(getSearchHead());
            sb.append("\n");
            sb.append("</head>\n");
            sb.append("<body style=\"background-color:#" + Integer.toHexString(getSearchBackgroundColor().getRGB()).substring(2).toUpperCase() + ";margin:0;padding:0;\" >\n");
            sb.append("<table style=\"padding:2px;margin:2px;\"   cellpadding=\"5px\" cellspacing=\"0\" border=\"0\">");
            sb.append("<tr><td>");
            LayoutParser layoutParser = new LayoutParser(new StringReader(getSearchFieldsLayout()));
            StringBuffer layout = new StringBuffer();
            try {
                layoutParser.parse(layout, 0, getTableName(), null, htmlGrid);
            } catch (Throwable ex) {
                throw new RuntimeException("El layout no es adecuado:" + getSearchFieldsLayout(), ex);
            }
            sb.append(layout);
            sb.append("</td></tr>");
            if ((layoutParser.getButtons().containsKey("buttonLimpiar") == false) && (layoutParser.getButtons().containsKey("buttonBuscar") == false)) {
                boolean with100 = false;
                sb.append("<tr><td>");
                sb.append("                <table width=\"100%\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\">");
                sb.append("                    <tr>");
                if (layoutParser.getButtons().containsKey("buttonLimpiar") == false) {
                    sb.append("                        <td ");
                    if (with100 == false) {
                        sb.append("width=\"100%\"");
                        with100 = true;
                    }
                    sb.append(">&nbsp;</td>");
                    sb.append("                        <td  >" + buttonLimpiar.toHTML() + "</td>");
                }
                if (layoutParser.getButtons().containsKey("buttonBuscar") == false) {
                    sb.append("                        <td ");
                    if (with100 == false) {
                        sb.append("width=\"100%\"");
                        with100 = true;
                    }
                    sb.append(">&nbsp;</td>");
                    sb.append("                        <td  >" + buttonBuscar.toHTML() + "</td>");
                }
                sb.append("                    </tr>");
                sb.append("                </table>");
                sb.append("</td></tr>");
            }
            if (layoutParser.hasGrid() == false) {
                sb.append("<tr><td>");
                sb.append(htmlGrid);
                sb.append("</td></tr>");
            }
            if ((layoutParser.getButtons().containsKey("buttonNuevo") == false) && (layoutParser.getButtons().containsKey("buttonEditar") == false) && (layoutParser.getButtons().containsKey("buttonBorrar") == false) && (layoutParser.getButtons().containsKey("buttonVer") == false)) {
                sb.append("<tr><td>");
                sb.append("                <table width=\"100%\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\">");
                sb.append("                    <tr>");
                sb.append("                        <td width=\"100%\" >&nbsp;</td>");
                sb.append("                        <td  >" + buttonNuevo.toHTML() + "</td>");
                sb.append("                        <td>&nbsp;</td>");
                sb.append("                        <td  >" + buttonEditar.toHTML() + "</td>");
                sb.append("                        <td>&nbsp;</td>");
                sb.append("                        <td  >" + buttonBorrar.toHTML() + "</td>");
                sb.append("                    </tr>");
                sb.append("                </table>");
                sb.append("</td></tr>");
            }
            sb.append("</table>");
            sb.append("</body>\n");
            sb.append("</html>\n");
            ViewTextStream viewTextStream = new ViewTextStream(new StringReader(sb.toString()), "text/html", false);
            return viewTextStream;
        } catch (RuntimeException rex) {
            throw rex;
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    /**
     * Retorna el JavaScript que se usa en la pantalla de b�squeda.
     * Para obtener el JavaScript se busca en fichero con extensi�n JavaScript
     * con el mismo nombre que la clase pero acabado en "Search" o sino que se llame como la clase
     * @param parameters No necesita ning�n par�metro en el Map
     * @return El JavaScript que usa la p�gina HTML
     */
    public View getSearchJavaScript(Map parameters) {
        try {
            Reader reader;
            String name;
            Object[] method = { getTableName(), this.getClass().getMethod("getSearchJavaScript", java.util.Map.class) };
            if (DLSession.getSecurity().isAccessAuthorized(this, method) == false) {
                return new ViewHTTPError("No tienes permisos para acceder al mantenimiento generico de la tabla:" + getTableName(), HttpServletResponse.SC_FORBIDDEN);
            }
            name = this.getClass().getName().replace(".", "/") + "Search.js";
            reader = getResourceAsReader(name);
            if (reader == null) {
                name = this.getClass().getName().replace(".", "/") + ".js";
                reader = getResourceAsReader(name);
                if (reader == null) {
                    reader = new StringReader("//No hay codigo especifico JavaScript en el Search");
                }
            }
            ViewTextStream viewTextStream = new ViewTextStream(reader, "text/javascript", false);
            return viewTextStream;
        } catch (RuntimeException rex) {
            throw rex;
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    /**
     * Llamar a este m�todo desde el cliente cuando queramos mostrar la pantalla de b�squeda
     * @param parameters Lista de argumentos para personalizar la forma de la pantalla de b�squeda.
     * @return Una p�gina HTML para realizar la b�squeda de una tabla.
     */
    public final View mant(Map parameters) {
        try {
            tableName = (String) parameters.get("tableName");
            mantFieldsLayout = (String) parameters.get("mantFieldsLayout");
            Object[] method = { getTableName(), this.getClass().getMethod("search", java.util.Map.class) };
            if (DLSession.getSecurity().isAccessAuthorized(this, method) == false) {
                return new ViewHTTPError("No tienes permisos para acceder al mantenimiento generico de la tabla:" + getTableName(), HttpServletResponse.SC_FORBIDDEN);
            }
            List<String> detailTables = getDetailTables();
            Button buttonAceptar = WLSession.getControls().createButton("buttonAceptar", "Aceptar");
            buttonAceptar.setDefaultButton(true);
            Button buttonCancelar = WLSession.getControls().createButton("buttonCancelar", "Cancelar");
            StringBuffer sb = new StringBuffer();
            sb.append("<html>\n");
            sb.append("<head>\n");
            sb.append("<meta http-equiv=\"Content-Type\" content=\"text/html; charset=ISO-8859-1\">\n");
            sb.append("<title>" + HTMLUtil.toHTML(getMantTitle()) + "</title>\n");
            sb.append("<LINK href=\"" + WLSession.getPaths().getAbsolutePath() + "/controls/css\" rel=\"stylesheet\" type=\"text/css\">\n");
            sb.append("<script type=\"text/javascript\" src=\"" + WLSession.getPaths().getAbsolutePath() + "/common/javascript\"></script>\n");
            sb.append("<script type=\"text/javascript\" src=\"" + WLSession.getPaths().getAbsolutePath() + "/controls/javascript\"></script>\n");
            StringBuffer dictionaryTables = new StringBuffer(getTableName());
            if (detailTables != null) {
                for (int i = 0; i < detailTables.size(); i++) {
                    dictionaryTables.append("," + detailTables.get(i));
                }
            }
            sb.append("<script type=\"text/javascript\" charset=\"UTF-8\" src=\"" + WLSession.getPaths().getAbsolutePath() + "/common/dictionary/" + dictionaryTables + "\"></script>\n");
            sb.append("<script type=\"text/javascript\" charset=\"ISO-8859-1\" src=\"" + WLSession.getPaths().getAbsolutePath() + "/common/file/mant.js\"></script>");
            sb.append("<script type=\"text/javascript\" charset=\"ISO-8859-1\" src=\"" + WLSession.getPaths().getAbsolutePath() + "/common/file/detail.js\"></script>");
            sb.append("<script type=\"text/javascript\" src=\"" + WLSession.getPaths().getAbsolutePath() + "/controller/" + this.getControllerName() + "/action/getMantJavaScript\"></script>\n");
            sb.append("<script type=\"text/javascript\">\n");
            sb.append("  var ancho=" + JavaScriptUtil.primitiveObjectToString(0) + ";");
            sb.append("  var alto=" + JavaScriptUtil.primitiveObjectToString(0) + ";");
            sb.append("  var titulo=" + JavaScriptUtil.primitiveObjectToString("Mantenimiento de " + getDefTable().getCaption()) + ";");
            sb.append("</script>\n");
            sb.append(getMantHead());
            sb.append("\n");
            sb.append("</head>\n");
            sb.append("<body style=\"background-color:#" + Integer.toHexString(getMantBackgroundColor().getRGB()).substring(2).toUpperCase() + ";margin:0;padding:0;\" >\n");
            sb.append("<table style=\"padding:2px;margin:2px;\"   cellpadding=\"5px\" cellspacing=\"0\" border=\"0\">");
            sb.append("<tr><td>");
            LayoutParser layoutParser = new LayoutParser(new StringReader(getMantFieldsLayout()));
            StringBuffer layout = new StringBuffer();
            HTMLDetail htmlDetail = new HTMLDetail(detailTables);
            try {
                layoutParser.parse(layout, 0, getTableName(), htmlDetail, null);
            } catch (Throwable ex) {
                throw new RuntimeException("El layout no es adecuado:" + getMantFieldsLayout(), ex);
            }
            sb.append(layout);
            sb.append("</td></tr>");
            if (htmlDetail.isAllGenerated() == false) {
                sb.append("<tr><td>");
                htmlDetail.generateHTMLDetail(sb);
                sb.append("</td></tr>");
            }
            if ((layoutParser.getButtons().containsKey("buttonAceptar") == false) && (layoutParser.getButtons().containsKey("buttonCancelar") == false)) {
                sb.append("<tr><td>");
                sb.append("                <table width=\"100%\" style=\"padding-top:5px\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\">");
                sb.append("                    <tr>");
                sb.append("                        <td  width=\"100%\" align=\"right\">" + buttonAceptar.toHTML() + "</td>     ");
                sb.append("                        <td  align=\"left\">" + buttonCancelar.toHTML() + "</td>");
                sb.append("                    </tr>");
                sb.append("                </table>");
                sb.append("</td></tr>");
            }
            sb.append("</table>");
            sb.append("</body>\n");
            sb.append("</html>\n");
            ViewTextStream viewTextStream = new ViewTextStream(new StringReader(sb.toString()), "text/html", false);
            return viewTextStream;
        } catch (RuntimeException rex) {
            throw rex;
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    /**
     * Retorna el JavaScript que se usa en la pantalla de mantenimiento.
     * Para obtener el JavaScript se busca en fichero con extensi�n JavaScript
     * con el mismo nombre que la clase pero acabado en "Mant" o sino que se llame como la clase
     * @param parameters No necesita ning�n par�metro en el Map
     * @return El JavaScript que usa la p�gina HTML
     */
    public View getMantJavaScript(Map parameters) {
        try {
            Reader reader;
            String name;
            Object[] method = { getTableName(), this.getClass().getMethod("getMantJavaScript", java.util.Map.class) };
            if (DLSession.getSecurity().isAccessAuthorized(this, method) == false) {
                return new ViewHTTPError("No tienes permisos para acceder al mantenimiento generico de la tabla:" + getTableName(), HttpServletResponse.SC_FORBIDDEN);
            }
            name = this.getClass().getName().replace(".", "/") + "Mant.js";
            reader = getResourceAsReader(name);
            if (reader == null) {
                name = this.getClass().getName().replace(".", "/") + ".js";
                reader = getResourceAsReader(name);
                if (reader == null) {
                    reader = new StringReader("//No hay codigo especifico JavaScript en el Mantenimiento");
                }
            }
            ViewTextStream viewTextStream = new ViewTextStream(reader, "text/javascript", false);
            return viewTextStream;
        } catch (RuntimeException rex) {
            throw rex;
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    /**
     * El nombre de la tabla del diccionario de la que se est� haciendo el mantenimiento.
     * @return Nombre de la tabla del diccionario
     */
    protected String getTableName() {
        return tableName;
    }

    /**
     * Obtiene la SQL de la pantalla de b�squeda
     * @TODO: Problema de seguridad. No deber�a verse desde la Web.
     * @param tableName Nombre de la tabla en base a la que se genera la SQL de b�squeda.
     * @param parameters La lista de par�metros para filtrar la b�squeda.
     * @return La SQL
     */
    public String getSearchSQL(String tableName, Parameters parameters) {
        StringBuffer sbPK = new StringBuffer();
        StringBuffer sbColumns = new StringBuffer();
        StringBuffer sbColumnsWhere = new StringBuffer();
        Parameters newParameters = new Parameters();
        DefTable defTable = BLSession.getDictionary().getDefTables().getDefTable(tableName);
        DefColumns defColumns = defTable.getDefColumns();
        int numColumns = Math.min(maxDefaultFields, defColumns.size());
        for (int i = 0; i < numColumns; i++) {
            DefColumn defColumn = defColumns.getDefColumn(i);
            if ((defColumn.isVirtual() == false) && (defColumn.isMultivalue() == false)) {
                if (defColumn.isPrimaryKey() == true) {
                    if (sbPK.length() != 0) {
                        sbPK.append(",");
                    }
                    sbPK.append(defColumn.getColumnName());
                }
                if (sbColumns.length() != 0) {
                    sbColumns.append(",");
                }
                sbColumns.append(defColumn.getColumnName());
                if (parameters.getParameter(i).isEmpty() == false) {
                    if (sbColumnsWhere.length() != 0) {
                        sbColumnsWhere.append(" AND ");
                    }
                    if (defColumn.getDataType().getMetaType() == MetaType.STRING) {
                        sbColumnsWhere.append(defColumn.getColumnName() + " LIKE ? ");
                    } else {
                        sbColumnsWhere.append(defColumn.getColumnName() + "=? ");
                    }
                    newParameters.addParameter(parameters.getParameter(i));
                }
            }
        }
        if (sbColumnsWhere.length() > 0) {
            sbColumnsWhere.insert(0, " WHERE ");
        }
        parameters.clear();
        parameters.addAll(newParameters);
        return "(" + defTable.getConnectionName() + ")SELECT " + sbPK + "," + sbColumns + " FROM " + tableName + sbColumnsWhere;
    }

    /**
     * String con el layout de los campos de busqueda
     * @return String con el layout de los campos de b�squeda
     */
    protected String getSearchFieldsLayout() {
        if (searchFieldsLayout == null) {
            DefColumns defColumns = getDefTable().getDefColumns();
            String tableCaption = getDefTable().getCaption();
            if ((tableCaption == null) || (tableCaption.trim().equalsIgnoreCase(""))) {
                tableCaption = getDefTable().getTableName();
            }
            StringBuffer layout = new StringBuffer();
            layout.append("{F \'B�squeda de " + tableCaption + "\'[");
            int numColumns = Math.min(maxDefaultFields, defColumns.size());
            for (int i = 0; i < numColumns; i++) {
                DefColumn defColumn = defColumns.getDefColumn(i);
                if ((defColumn.isVirtual() == false) && (defColumn.isMultivalue() == false)) {
                    if (i != 0) {
                        layout.append("|");
                    }
                    layout.append("#" + defColumn.getTableName() + "." + defColumn.getColumnName());
                    layout.append("," + defColumn.getTableName() + "." + defColumn.getColumnName());
                }
            }
            layout.append("]}");
            return layout.toString();
        } else {
            return searchFieldsLayout;
        }
    }

    /**
     * Lista de columnas que se mostrar�n en el Grid
     * @return Informaci�n de las columna del grid
     */
    protected ColumnDefinitions getSearchGridColumnDefinitions() {
        ColumnDefinitions columnDefinitions = new ColumnDefinitions();
        DefColumns defColumns = getDefTable().getDefColumns();
        int numColumns = Math.min(maxDefaultFields, defColumns.size());
        for (int i = 0; i < numColumns; i++) {
            DefColumn defColumn = defColumns.getDefColumn(i);
            if ((defColumn.isVirtual() == false) && (defColumn.isMultivalue() == false)) {
                int size = defColumn.getLength();
                if ((defColumn.getCaption() != null) && (size <= defColumn.getCaption().length())) {
                    size = defColumn.getCaption().length();
                }
                if (size > 15) {
                    size = 45;
                }
                columnDefinitions.addColumnDefinition(defColumn.getColumnName(), defColumn.getCaption(), size * 10);
            }
        }
        return columnDefinitions;
    }

    /**
     * El t�tulo de la ventana de b�squeda
     * @return T�tulo de la ventana de b�squeda
     */
    protected String getSearchTitle() {
        return "Busqueda de " + getDefTable().getCaption();
    }

    /**
     * C�digo HTML a incluir en el HEAD de las pantallas de b�squeda
     * Suele ser alg�n c�digo JavaScript o CSS
     * @return El texto a incluir en el HEAD
     */
    protected String getSearchHead() {
        return "";
    }

    /**
     * P�gina Web que permite realizar el mantenimiento de la fila
     * @return URL relativa a la aplicaci�n que indica la p�gina de mantenimiento
     */
    protected String getMantPage() {
        String jspPage = "/controller/" + this.getControllerName() + "/action/mant?tableName=" + getTableName();
        return jspPage;
    }

    /**
     * Altura del Grid en la pantalla de b�squeda
     * @return Altura del Grid en la pantalla de b�squeda
     */
    protected int getSearchGridHeight() {
        return gridHeight;
    }

    /**
     * Tama�o del p�gina del Grid. Es dcir que N� maximo de filas que se ven en el grid.
     * @return Tama�o del p�gina del Grid
     */
    protected int getSearchPageSize() {
        return pageSize;
    }

    /**
     * Si se usan para las querys la conexiones de la capa de negocio en vez de la de datos.
     * Por defecto este m�todo retorna 'false' con lo que se usan las conexiones de datos.
     * @return Si vale 'true' se usa la de negocio sino se usa la de datos.
     */
    protected boolean useBusinessConnection() {
        return false;
    }

    /**
     * La codificaci�n que tienen los ficheros JavaScript asociado al mantenimiento.
     * @return Por defecto retorna "ISO-8859-1"
     */
    protected String getCharsetJavaScriptFiles() {
        return "ISO-8859-1";
    }

    /**
     * Layout de los controles de mantenimiento
     * @return UnString con los campos y el layout de todos ellos.
     */
    protected String getMantFieldsLayout() {
        if (mantFieldsLayout == null) {
            DefColumns defColumns = getDefTable().getDefColumns();
            StringBuffer layout = new StringBuffer();
            layout.append("{F\'Datos\'[");
            for (int i = 0; i < defColumns.size(); i++) {
                DefColumn defColumn = defColumns.getDefColumn(i);
                if (i != 0) {
                    layout.append("|");
                }
                layout.append("#" + defColumn.getTableName() + "." + defColumn.getColumnName());
                layout.append("," + defColumn.getTableName() + "." + defColumn.getColumnName());
            }
            layout.append("]}");
            return layout.toString();
        } else {
            return mantFieldsLayout;
        }
    }

    /**
     * El t�tulo de la ventana de mantenimiento
     * @return T�tulo de la ventana de mantenimiento
     */
    protected String getMantTitle() {
        return "Mantenimiento de " + getDefTable().getCaption();
    }

    /**
     * C�digo HTML a incluir en el HEAD de las pantallas de mantenimiento
     * Suele ser alg�n c�digo JavaScript o CSS
     * @return El texto a incluir en el HEAD. Por defecto est� vacio.
     */
    protected String getMantHead() {
        return "";
    }

    /**
     * El color de fondo de la pantalla de mantenimiento.
     * @return El color de fondo de la pantalla de mantenimiento.
     */
    protected Color getMantBackgroundColor() {
        return new Color(0xF0, 0xF0, 0xF0);
    }

    /**
     * El color de fondo de la pantalla de b�squeda.
     * @return El color de fondo de la pantalla de b�squeda.
     */
    protected Color getSearchBackgroundColor() {
        return new Color(0xF0, 0xF0, 0xF0);
    }

    /**
     * Si se realiza una b�squeda en el grid al cargar la p�gina
     * @return Si retorna <code>true</code> se buscar�n los datos nada m�s cargar la pagina.<br>
     * No es aconsejable usar esta caracter�stica en tablas con muchos datos , ya que no se filtrar�n
     * al no haber inicialmente criterios de b�squeda.
     */
    protected boolean isInitialSearch() {
        return false;
    }

    /**
     * Lista de tabla "Detalle", para una relaci�n Maestro-Detalle
     * @return Lista con el nombre de la tablas "Detalle".Por defecto est� vacio.
     */
    protected List<String> getDetailTables() {
        return new ArrayList();
    }

    /**
     * Obtener las columnas que se mostrar�n en el Grid de un detalle detarminado
     * @param detailName Nombre de la tabla del detalle
     * @return Definici�n de las columnas que apareceren en el grid del detalle.
     */
    protected ColumnDefinitions getDetailGridColumnDefinitions(String detailName) {
        ColumnDefinitions columnDefinitions = new ColumnDefinitions();
        DefColumns defColumns = BLSession.getDictionary().getDefTables().getDefTable(detailName).getDefColumns();
        int numColumns = Math.min(maxDefaultFields, defColumns.size());
        for (int i = 0; i < numColumns; i++) {
            DefColumn defColumn = defColumns.getDefColumn(i);
            if ((defColumn.isVirtual() == false) && (defColumn.isMultivalue() == false)) {
                int size = defColumn.getLength();
                if ((defColumn.getCaption() != null) && (size <= defColumn.getCaption().length())) {
                    size = defColumn.getCaption().length();
                }
                if (size > 15) {
                    size = 45;
                }
                columnDefinitions.addColumnDefinition(defColumn.getColumnName(), defColumn.getCaption(), size * 10);
            }
        }
        return columnDefinitions;
    }

    /**
     * Obtener el alto en el Grid de un detalle determinado
     * @param detailName Nombre de la tabla del detalle
     * @return Alto en pixeles del Grid
     */
    protected int getDetailGridHeight(String detailName) {
        return gridHeight;
    }

    /**
     * P�gina Web que permite realizar el mantenimiento del detalle.
     * @param detailName Nombre del detalle
     * @return URL relativa a la aplicaci�n que indica la p�gina de mantenimiento
     */
    protected String getDetailMantPage(String detailName) {
        String jspPage = "/controller/" + this.getControllerName() + "/action/mant?tableName=" + detailName;
        return jspPage;
    }

    /**
     * N� de columna de la clave primaria
     * @return El n� de columnas que forman la clave primaria
     */
    protected int getSearchNumPrimaryKeys() {
        int numPrimaryKeys = 0;
        DefColumns defColumns = getDefTable().getDefColumns();
        for (int i = 0; i < defColumns.size(); i++) {
            DefColumn defColumn = defColumns.getDefColumn(i);
            if (defColumn.isPrimaryKey() == true) {
                numPrimaryKeys++;
            }
        }
        return numPrimaryKeys;
    }

    /**
     * SQL para obtener los datos del Grid de un detalle.
     * @TODO: Problema de seguridad. No deber�a verse desde la Web.
     * @param tableName Nombre de la tabla que contiene el detalle
     * @param detailName Nombre de la tabla del detalle
     * @param parameters Parametros con la clave primaria de la tabla padre.
     * @return La SQL con los datos a mostrar en el Grid del detalle.
     */
    public String getDetailSQL(String tableName, String detailName, Parameters parameters) {
        StringBuffer sbPK = new StringBuffer();
        StringBuffer sbColumns = new StringBuffer();
        StringBuffer sbColumnsWhere = new StringBuffer();
        Parameters newParameters = new Parameters();
        DefColumns defColumns = BLSession.getDictionary().getDefTables().getDefTable(detailName).getDefColumns();
        int numColumns = Math.min(maxDefaultFields, defColumns.size());
        for (int i = 0; i < numColumns; i++) {
            DefColumn defColumn = defColumns.getDefColumn(i);
            if ((defColumn.isVirtual() == false) && (defColumn.isMultivalue() == false)) {
                if (defColumn.isPrimaryKey() == true) {
                    if (sbPK.length() != 0) {
                        sbPK.append(",");
                    }
                    sbPK.append(defColumn.getColumnName());
                }
                if (sbColumns.length() != 0) {
                    sbColumns.append(",");
                }
                sbColumns.append(defColumn.getColumnName());
                if (parameters.getParameter(i).isEmpty() == false) {
                    if (defColumn.getDataType().getMetaType() == MetaType.STRING) {
                        sbColumnsWhere.append(" AND " + defColumn.getColumnName() + " LIKE ? ");
                    } else {
                        sbColumnsWhere.append(" AND " + defColumn.getColumnName() + "=? ");
                    }
                    newParameters.addParameter(parameters.getParameter(i));
                }
            }
        }
        parameters.clear();
        parameters.addAll(newParameters);
        return "SELECT " + sbPK + "," + sbColumns + " FROM " + getTableName() + " WHERE 1=1 " + sbColumnsWhere;
    }

    private DefTable getDefTable() {
        DefTable defTable = BLSession.getDictionary().getDefTables().getDefTable(getTableName());
        if (defTable == null) {
            throw new RuntimeException("No existe la tabla del diccionario:" + getTableName());
        }
        return defTable;
    }

    /**
     * N� de claves primarias del detalle
     * @param detailName Nombre del detalle.
     * @return N� de claves primarias.
     */
    private int getDetailNumPrimaryKeys(String detailName) {
        int numPrimaryKeys = 0;
        DefColumns defColumns = BLSession.getDictionary().getDefTables().getDefTable(detailName).getDefColumns();
        for (int i = 0; i < defColumns.size(); i++) {
            DefColumn defColumn = defColumns.getDefColumn(i);
            if (defColumn.isPrimaryKey() == true) {
                numPrimaryKeys++;
            }
        }
        return numPrimaryKeys;
    }

    private Reader getResourceAsReader(String resourceName) {
        try {
            Reader reader = null;
            InputStream is;
            ClassLoader cl = this.getClass().getClassLoader();
            if (cl == null) {
                is = ClassLoader.getSystemResourceAsStream(resourceName);
            } else {
                is = cl.getResourceAsStream(resourceName);
            }
            if (is != null) {
                reader = new InputStreamReader(is, getCharsetJavaScriptFiles());
            }
            return reader;
        } catch (RuntimeException rex) {
            throw rex;
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    /**
     * SQL que se lanza desde JavaScript para obtener los datos del grid de b�squeda.
     * @return La SQL de la DataLayer que se lanza desde JavaScript.
     * @see org.elf.weblayer.webconnections.WebConnectionImplGenericMant
     */
    private String getSQLWLSession() {
        if (useBusinessConnection() == true) {
            return getSearchSQL(getTableName(), null);
        } else {
            return "(GenericMant)SELECT * FROM " + this.getControllerName() + ".search WHERE tableName='" + getTableName() + "'";
        }
    }

    /**
     * SQL que se lanza desde JavaScript para obtener los datos del grid de un detalle.
     * @return La SQL de la DataLAyer que se lanza desde JavaScript para un detalle.
     * @see org.elf.weblayer.webconnections.WebConnectionImplGenericMant
     */
    private String getSQLWLSessionDetail(String detailName) {
        if (useBusinessConnection() == true) {
            return getDetailSQL(getTableName(), detailName, null);
        } else {
            return "(GenericMant)SELECT * FROM " + this.getControllerName() + ".detail WHERE tableName='" + getTableName() + "' AND detailName='" + detailName + "'";
        }
    }

    /**
     * Genera el HTML asociado a los detalles
     */
    class HTMLDetail {

        private List<String> detailTables;

        private Map<String, Boolean> generatedTables = new LinkedHashMap<String, Boolean>();

        private boolean generatedTop = false;

        public HTMLDetail(List<String> detailTables) {
            if (detailTables == null) {
                this.detailTables = new ArrayList<String>();
            } else {
                this.detailTables = detailTables;
            }
            for (int i = 0; i < detailTables.size(); i++) {
                generatedTables.put(this.detailTables.get(i), false);
            }
        }

        /**
         * Obtiene si todos los detalles han sido generados
         * @return Retorna 'true' si todos los detalles han sino generados, sino retorna 'false'.
         */
        public boolean isAllGenerated() {
            int numGenerated = numGenerated();
            int numTables = detailTables.size();
            if (numGenerated == numTables) {
                return true;
            } else {
                return false;
            }
        }

        /**
         * Genera todos los detalles que faltan por generar
         * @param sb
         */
        public void generateHTMLDetail(StringBuffer sb) {
            int numGenerated = numGenerated();
            int numTables = detailTables.size();
            generateTop(sb);
            if (numGenerated == numTables) {
            } else if ((numTables == (numGenerated + 1))) {
                generateHTMLDetail(sb, getDetailNameUniqueWithoutGenerated());
            } else {
                TabbedPane tpDetail = WLSession.getControls().createTabbedPane("tpDetail", 0, 0);
                for (int i = 0; i < detailTables.size(); i++) {
                    String detailName = detailTables.get(i);
                    if (generatedTables.get(detailName) == false) {
                        DefTable defTable = BLSession.getDictionary().getDefTables().getDefTable(detailName);
                        tpDetail.getPaneDefinitions().addPaneDefinition(defTable.getTableName(), defTable.getCaption());
                    }
                }
                sb.append(tpDetail.toHTML());
                for (int i = 0; i < detailTables.size(); i++) {
                    String detailName = detailTables.get(i);
                    if (generatedTables.get(detailName) == false) {
                        generateHTMLDetail(sb, detailName);
                        sb.append(tpDetail.toHTML());
                    }
                }
            }
        }

        /**
         * Genera un �nico detalle
         * @param sb
         * @param detailName Nombre del detalle a generar
         */
        public void generateHTMLDetail(StringBuffer sb, String detailName) {
            DefTable defTable = BLSession.getDictionary().getDefTables().getDefTable(detailName);
            if (defTable == null) {
                throw new RuntimeException("No existe el detalle:" + detailName);
            }
            if (generatedTables.get(detailName) == true) {
                throw new RuntimeException("El detalle ya est� generado:" + detailName);
            }
            generateTop(sb);
            generatedTables.put(detailName, true);
            Grid grid = WLSession.getControls().createGrid("grid" + defTable.getTableName(), null, getDetailNumPrimaryKeys(defTable.getTableName()), getDetailGridHeight(defTable.getTableName()));
            grid.setColumnDefinitions(getDetailGridColumnDefinitions(defTable.getTableName()));
            Button buttonNuevo = WLSession.getControls().createButton("buttonNuevo" + defTable.getTableName(), "Nuevo");
            Button buttonEditar = WLSession.getControls().createButton("buttonEditar" + defTable.getTableName(), "Editar");
            Button buttonBorrar = WLSession.getControls().createButton("buttonBorrar" + defTable.getTableName(), "Borrar");
            sb.append("<table cellpadding=\"0\" cellspacing=\"0px\" border=\"0\"><tr><td >");
            sb.append(grid.toHTML());
            sb.append("</td></tr>      ");
            sb.append("<tr><td  >");
            sb.append("    <table cellpadding=\"0\" cellspacing=\"0px\" border=\"0\">");
            sb.append("        <tr>");
            sb.append("            <td  >" + buttonNuevo.toHTML() + "</td>");
            sb.append("            <td  >&nbsp;</td>");
            sb.append("            <td  >" + buttonEditar.toHTML() + "</td>");
            sb.append("            <td  >&nbsp;</td>");
            sb.append("            <td  >" + buttonBorrar.toHTML() + "</td>");
            sb.append("        </tr>");
            sb.append("    </table>");
            sb.append("</td></tr></table>");
        }

        /**
         * codigo JavaScript a generar antes de cualquier detalle.
         * @param sb
         */
        private void generateTop(StringBuffer sb) {
            if (generatedTop == true) {
                return;
            }
            generatedTop = true;
            if (detailTables.size() > 0) {
                sb.append("<script type=\"text/javascript\">\n");
                sb.append("function getDetailControllers() {\n");
                sb.append("  var detailControllers=new List();\n");
                sb.append("  var detailController;\n");
                for (int i = 0; i < detailTables.size(); i++) {
                    DefTable defTable = BLSession.getDictionary().getDefTables().getDefTable(detailTables.get(i));
                    if (defTable == null) {
                        throw new RuntimeException("No existe la tabla:" + detailTables.get(i));
                    }
                    sb.append("    detailController=new DetailController(");
                    sb.append(JavaScriptUtil.primitiveObjectToString(defTable.getTableName()));
                    sb.append(",");
                    sb.append(JavaScriptUtil.primitiveObjectToString(getSQLWLSessionDetail(defTable.getTableName())));
                    sb.append(",");
                    sb.append(JavaScriptUtil.primitiveObjectToString(getDetailMantPage(defTable.getTableName())));
                    sb.append(",");
                    sb.append("undefined");
                    sb.append(",");
                    sb.append(JavaScriptUtil.primitiveObjectToString(useBusinessConnection()));
                    sb.append(")\n");
                    sb.append("    detailControllers.add(detailController);\n");
                }
                sb.append("  return detailControllers;\n");
                sb.append("}\n");
                sb.append("</script>\n");
            }
        }

        /**
         * Obtiene cuantas tablas ya se han generado
         * @return N� de tablas que se han ya generado
         */
        private int numGenerated() {
            int numGenerated = 0;
            Iterator<String> it = generatedTables.keySet().iterator();
            StringBuffer sb = new StringBuffer();
            while (it.hasNext()) {
                String detailName = it.next();
                boolean generated = generatedTables.get(detailName);
                if (generated == true) {
                    numGenerated++;
                }
            }
            return numGenerated;
        }

        /**
         * Obtiene el nombre de la unica tabla sin generar
         * @return Nombre de la �nica tabla aun sin generar.
         */
        private String getDetailNameUniqueWithoutGenerated() {
            if (detailTables.size() != (numGenerated() + 1)) {
                throw new RuntimeException("Solo puede quedar una tabla sin generar.Hay " + detailTables.size() + " detalles y se han generado:" + numGenerated());
            }
            for (int i = 0; i < detailTables.size(); i++) {
                String detailName = detailTables.get(i);
                if (generatedTables.get(detailName) == false) {
                    return detailName;
                }
            }
            throw new RuntimeException("No hay ninguna tabla sin generar");
        }
    }
}
