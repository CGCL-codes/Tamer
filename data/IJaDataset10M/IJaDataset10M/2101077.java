package saadadb.relationship;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.StringReader;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import saadadb.cache.CacheMeta;
import saadadb.collection.Category;
import saadadb.command.ArgsParser;
import saadadb.database.Database;
import saadadb.database.Repository;
import saadadb.exceptions.FatalException;
import saadadb.exceptions.QueryException;
import saadadb.exceptions.SaadaException;
import saadadb.kdtree.CounterPart;
import saadadb.kdtree.FirstNearestNeighbour;
import saadadb.kdtree.HasKCoo;
import saadadb.kdtree.KDTree;
import saadadb.kdtree.KNearestNeighbour;
import saadadb.kdtree.SigmaFirstNearestNeighbour;
import saadadb.kdtree.SigmaKNearestNeighbour;
import saadadb.meta.MetaClass;
import saadadb.meta.MetaCollection;
import saadadb.meta.MetaRelation;
import saadadb.query.executor.Query;
import saadadb.query.parser.SqlParser;
import saadadb.query.parser.WhereUCD;
import saadadb.sqltable.SQLLargeQuery;
import saadadb.sqltable.SQLTable;
import saadadb.util.Messenger;
import saadaql.correlator.Parser;
import saadaql.correlator.Parser.ConditionDist;
import saadaql.correlator.Parser.ConditionKnn;

public final class CorrQueryTranslator {

    private final String query;

    private KDTree neighbour_detector = null;

    private int number_neighbour;

    private double dist_max;

    private String dist_unit;

    private double unit2rad_factor;

    private Parser.PrincipalNode pnode;

    private MetaRelation mr;

    private String[] primary_classes;

    private String[] secondary_classes;

    private WhereUCD p_ucd;

    private WhereUCD s_ucd;

    private final List<UcdCond> ucdCondList = new ArrayList<UcdCond>();

    private final List<String> conditionUcdList = new ArrayList<String>();

    private final List<String> attrQual_p = new ArrayList<String>();

    private final List<String> attrCond_p = new ArrayList<String>();

    private final List<String> attrQual_s = new ArrayList<String>();

    private final List<String> attrCond_s = new ArrayList<String>();

    protected String tempo_tbl, tempo_tbl_format;

    protected TreeSet<String> tables_to_lock;

    private static Parser parser = null;

    private final RelationManager manager;

    public CorrQueryTranslator(RelationManager manager, String query, String tempo_tbl) throws FatalException {
        this.manager = manager;
        this.tempo_tbl = tempo_tbl;
        this.query = query;
    }

    /**
	 * @throws Exception
	 */
    final void parse() throws Exception {
        BufferedReader br = new BufferedReader(new StringReader(this.query));
        if (parser == null) {
            parser = new Parser(br);
        }
        Parser.ReInit(br);
        this.pnode = Parser.parse();
        this.checkSyntax();
        if ((this.mr = Database.getCachemeta().getRelation(pnode.getRelationName())) == null) {
            FatalException.throwNewException(SaadaException.METADATA_ERROR, "Exception: relation \"" + pnode.getRelationName() + "\" not found in the MetaData Cache!");
        }
        this.checkMeta();
    }

    /**
	 * @throws Exception
	 */
    private final void checkSyntax() throws Exception {
        this.checkFromSyntax(this.pnode.getPrimary());
        this.checkFromSyntax(this.pnode.getSecondary());
        for (Parser.Condition cond : this.pnode.getCondList()) {
            Pattern p = Pattern.compile("[ps]\\.[\\w]+");
            Matcher m = p.matcher(cond.getStr());
            while (m.find()) {
                String att = m.group(0);
                if (att.startsWith("p")) {
                    attrCond_p.add(att.substring(2));
                } else if (att.startsWith("s")) {
                    attrCond_s.add(att.substring(2));
                }
            }
        }
        for (Parser.Qualif qual : this.pnode.getQualList()) {
            String words[] = qual.getStr().split(" ");
            for (String word : words) {
                if (word.matches("^(?i)(P)\\..+$")) attrQual_p.add(word.substring(2)); else if (word.matches("^(?i)(S)\\..+$")) attrQual_s.add(word.substring(2));
            }
        }
        int i = 0;
        for (Parser.ConditionUCD cond : this.pnode.getCondListUCD()) {
            String condition = cond.getStr().trim();
            String unit = "none";
            String UNIT_REGEX = "[+\\-/\\.\\[\\]\\w]+";
            Matcher m = Pattern.compile("^.+(\\s+\\[\\s*" + UNIT_REGEX + "\\s*\\]\\s*)$").matcher(condition);
            if (m.find()) {
                String capG = m.group(1);
                unit = capG.substring(capG.indexOf("["), capG.lastIndexOf("]")).trim();
                condition = condition.substring(0, condition.length() - capG.length());
            }
            String base = "attr_";
            String UCD1p_REGEX = "(?:(?:[\\w\\-]+\\.)*(?:[\\w\\-]+);)*(?:(?:[\\w\\-]+\\.)*(?:[\\w\\-]+))";
            String regex = "(?i)(P|S)\\.(?i)(?:UCD)\\[\\s*(" + UCD1p_REGEX + ")\\s*\\]";
            m = Pattern.compile(regex).matcher(condition);
            while (m.find()) {
                boolean prim = m.group(1).matches("(?i)p");
                String ucd = m.group(2);
                String substitute = base + (++i);
                this.ucdCondList.add(new UcdCond(ucd, substitute, unit, prim));
                condition = condition.replaceFirst(regex, substitute);
            }
            SqlParser.parseAttributes(condition);
            this.conditionUcdList.add(condition);
        }
    }

    /**
	 * @param from
	 * @throws QueryException 
	 */
    private final void checkFromSyntax(Parser.From from) throws QueryException {
        if (from.hasWAS()) SqlParser.parseAttributes(from.getWAS());
        if (from.hasWAC()) SqlParser.parseAttributes(from.getWAC());
    }

    /**
	 * @throws SaadaException
	 */
    private final void checkMeta() throws SaadaException {
        checkFromMeta(this.pnode.getPrimary(), this.mr.getPrimary_coll(), this.mr.getPrimary_category());
        this.primary_classes = this.pnode.getPrimary().getListClass();
        checkFromMeta(this.pnode.getSecondary(), this.mr.getSecondary_coll(), this.mr.getSecondary_category());
        this.secondary_classes = this.pnode.getSecondary().getListClass();
        for (Parser.Qualif qual : this.pnode.getQualList()) {
            if (!this.mr.getQualifier_names().contains(qual.getName())) FatalException.throwNewException(SaadaException.METADATA_ERROR, "Qualifier \"" + qual.getName() + "\" not found in the relation \"" + this.mr.getName() + "\"!");
        }
    }

    /**
	 * @param from
	 * @param col
	 * @param cat
	 * @throws SaadaException
	 */
    private final void checkFromMeta(Parser.From from, String col, int cat) throws SaadaException {
        CacheMeta meta = Database.getCachemeta();
        if (from.hasWAS()) {
            for (String attr : SqlParser.parseAttributes(from.getWAS())) if (!MetaCollection.attributeExistIn(attr, cat)) FatalException.throwNewException(SaadaException.METADATA_ERROR, "Attribute \"" + attr + "\" not found in the metadata cache in the collection \"" + col + "\" and category \"" + Category.explain(cat) + "\"!");
        }
        String[] listClass = from.getListClass();
        if (!listClass[0].equals("*")) {
            if (cat == Category.FLATFILE) FatalException.throwNewException(SaadaException.METADATA_ERROR, "There is no class in category \"" + Category.explain(cat) + "\" (collection \"" + col + "\"): the list must be \"*\"!");
            for (String className : listClass) if (!meta.classExistsIn(className, col, cat)) FatalException.throwNewException(SaadaException.METADATA_ERROR, "Classe \"" + className + "\" not found in the metadata cache in the collection \"" + col + "\" and category \"" + Category.explain(cat) + "\"!");
        } else {
            if (cat == Category.FLATFILE && from.hasWUCD()) FatalException.throwNewException(SaadaException.METADATA_ERROR, "WhereUCD not allowed with category \"" + Category.explain(cat) + "\" (collection \"" + col + "\"): that category can't contain classes!");
        }
        if (from.hasWAC()) {
            MetaClass mc = meta.getClass(listClass[0]);
            for (String attr : SqlParser.parseAttributes(from.getWAC())) if (!mc.attributeExists(attr)) FatalException.throwNewException(SaadaException.METADATA_ERROR, "Attribute \"" + attr + "\" not found in the metadata cache in the classe \"" + listClass[0] + "\"!");
        }
    }

    private final List<UcdCond> createListUcdCondPrim() {
        List<UcdCond> s = new ArrayList<UcdCond>();
        for (UcdCond ucdc : this.ucdCondList) {
            if (ucdc.prim) s.add(ucdc);
        }
        return s;
    }

    private final List<UcdCond> createListUcdCondSec() {
        List<UcdCond> s = new ArrayList<UcdCond>();
        for (UcdCond ucdc : this.ucdCondList) {
            if (!ucdc.prim) s.add(ucdc);
        }
        return s;
    }

    /**
	 * @return
	 * @throws Exception 
	 */
    public final String buildQuery() throws Exception {
        this.tables_to_lock = new TreeSet<String>();
        ConditionKnn cknn = this.pnode.getCondKnn();
        boolean trivial = true;
        if (cknn != null) {
            this.number_neighbour = cknn.getK();
            this.dist_max = cknn.getDist();
            this.dist_unit = cknn.getUnit();
            if ("sigma".equalsIgnoreCase(this.dist_unit)) {
                this.neighbour_detector = new SigmaKNearestNeighbour();
                Messenger.printMsg(Messenger.TRACE, "Mode SIGMA max " + this.number_neighbour + "neighbour at less than " + this.dist_max + " sigma.");
            } else {
                this.neighbour_detector = new KNearestNeighbour();
                Messenger.printMsg(Messenger.TRACE, "Mode DISTANCE max " + this.number_neighbour + "neighbour at less than " + this.dist_max + " " + this.dist_unit);
            }
        } else {
            ConditionDist cdist = this.pnode.getCondDist();
            if (cdist != null) {
                this.number_neighbour = -1;
                this.dist_max = cdist.getDist();
                this.dist_unit = cdist.getUnit();
                if ("sigma".equalsIgnoreCase(this.dist_unit)) {
                    this.neighbour_detector = new SigmaFirstNearestNeighbour();
                    System.out.println("DIST SIGMA " + this.number_neighbour + " " + this.dist_max + " " + this.dist_unit + " " + this.neighbour_detector);
                } else {
                    this.neighbour_detector = new FirstNearestNeighbour();
                    System.out.println("DIST " + this.number_neighbour + " " + this.dist_max + " " + this.dist_unit + " " + this.neighbour_detector);
                }
            }
        }
        List<UcdCond> primUcdConds = this.createListUcdCondPrim();
        List<UcdCond> secUcdConds = this.createListUcdCondSec();
        RelationUCDsManager rmPrim = null, rmSec = null;
        if (primUcdConds.size() > 0) {
            rmPrim = new RelationUCDsManager(primUcdConds, this.mr.getPrimary_coll(), this.mr.getPrimary_category());
        }
        if (secUcdConds.size() > 0) {
            rmSec = new RelationUCDsManager(secUcdConds, this.mr.getSecondary_coll(), this.mr.getSecondary_category());
        }
        String[] qual_col = new String[2 + this.mr.getQualifier_names().size()];
        String[] qual_alias = new String[2 + this.mr.getQualifier_names().size()];
        qual_col[0] = "oidprimary";
        qual_col[1] = "oidsecondary";
        qual_alias[0] = "p.oidsaada";
        qual_alias[1] = "s.oidsaada";
        this.tempo_tbl_format = "oidprimary int8, oidsecondary int8";
        int qi = 0;
        for (String q : this.mr.getQualifier_names()) {
            String express = null;
            for (Parser.Qualif qual : pnode.getQualList()) {
                if (qual.getName().equals(q)) {
                    express = qual.getStr();
                    break;
                }
            }
            if (express == null) {
                express = "NULL";
            } else {
                trivial = false;
            }
            qual_col[qi + 2] = q;
            qual_alias[qi + 2] = express;
            this.tempo_tbl_format += ", " + q + " float8";
            qi++;
        }
        StringBuffer strBuff = new StringBuffer(Database.getWrapper().selectIntoTempoTable(Database.getWrapper().getTempoTableName(this.tempo_tbl), qual_col, qual_alias));
        strBuff.append("\nFROM\n");
        strBuff.append("(").append(buildFromQuery(this.pnode.getPrimary(), this.mr.getPrimary_coll(), this.mr.getPrimary_category(), this.attrQual_p, this.attrCond_p, true, rmPrim)).append(" ) p");
        strBuff.append("\nCROSS JOIN\n");
        strBuff.append("(").append(buildFromQuery(this.pnode.getSecondary(), this.mr.getSecondary_coll(), this.mr.getSecondary_category(), this.attrQual_s, this.attrCond_s, false, rmSec)).append(" ) s");
        ;
        List<Parser.Condition> lcond = pnode.getCondList();
        boolean empty = true;
        if (lcond.size() > 0) {
            empty = false;
            trivial = false;
            strBuff.append("\nWHERE ").append(lcond.get(0).getStr());
            for (int i = 1; i < lcond.size(); i++) strBuff.append(" AND ").append(lcond.get(i).getStr());
        }
        if (empty && this.conditionUcdList.size() > 0) {
            trivial = false;
            strBuff.append("\nWHERE ").append(this.conditionUcdList.get(0));
            for (int i = 1; i < this.conditionUcdList.size(); i++) strBuff.append(this.conditionUcdList.get(i));
        } else {
            for (String condition : this.conditionUcdList) strBuff.append(" AND ").append(condition);
        }
        strBuff.append(";");
        if (!trivial) return strBuff.toString(); else return null;
    }

    /**
	 * Replace buildQuery: Build the correlation tempo table from temporary tables instead of multiple joins
	 * and SQLIte says MANY THANKS
	 * @return
	 * @throws Exception
	 */
    public final String[] buildQueries() throws Exception {
        ArrayList<String> retour = new ArrayList<String>();
        this.tables_to_lock = new TreeSet<String>();
        ConditionKnn cknn = this.pnode.getCondKnn();
        boolean trivial = true;
        if (cknn != null) {
            this.number_neighbour = cknn.getK();
            this.dist_max = cknn.getDist();
            this.dist_unit = cknn.getUnit();
            if ("sigma".equalsIgnoreCase(this.dist_unit)) {
                this.neighbour_detector = new SigmaKNearestNeighbour();
                Messenger.printMsg(Messenger.TRACE, "Mode SIGMA max " + this.number_neighbour + "neighbour at less than " + this.dist_max + " sigma.");
            } else {
                this.neighbour_detector = new KNearestNeighbour();
                Messenger.printMsg(Messenger.TRACE, "Mode DISTANCE max " + this.number_neighbour + "neighbour at less than " + this.dist_max + " " + this.dist_unit);
            }
        } else {
            ConditionDist cdist = this.pnode.getCondDist();
            if (cdist != null) {
                this.number_neighbour = -1;
                this.dist_max = cdist.getDist();
                this.dist_unit = cdist.getUnit();
                if ("sigma".equalsIgnoreCase(this.dist_unit)) {
                    this.neighbour_detector = new SigmaFirstNearestNeighbour();
                    if (Messenger.debug_mode) Messenger.printMsg(Messenger.DEBUG, "DIST SIGMA " + this.number_neighbour + " " + this.dist_max + " " + this.dist_unit + " " + this.neighbour_detector);
                } else {
                    this.neighbour_detector = new FirstNearestNeighbour();
                    if (Messenger.debug_mode) Messenger.printMsg(Messenger.DEBUG, "DIST " + this.number_neighbour + " " + this.dist_max + " " + this.dist_unit + " " + this.neighbour_detector);
                }
            }
        }
        List<UcdCond> primUcdConds = this.createListUcdCondPrim();
        List<UcdCond> secUcdConds = this.createListUcdCondSec();
        RelationUCDsManager rmPrim = null, rmSec = null;
        if (primUcdConds.size() > 0) {
            rmPrim = new RelationUCDsManager(primUcdConds, this.mr.getPrimary_coll(), this.mr.getPrimary_category());
        }
        if (secUcdConds.size() > 0) {
            rmSec = new RelationUCDsManager(secUcdConds, this.mr.getSecondary_coll(), this.mr.getSecondary_category());
        }
        String[] qual_col = new String[2 + this.mr.getQualifier_names().size()];
        String[] qual_alias = new String[2 + this.mr.getQualifier_names().size()];
        qual_col[0] = "oidprimary";
        qual_col[1] = "oidsecondary";
        qual_alias[0] = "p.oidsaada";
        qual_alias[1] = "s.oidsaada";
        this.tempo_tbl_format = "oidprimary int8, oidsecondary int8";
        int qi = 0;
        for (String q : this.mr.getQualifier_names()) {
            String express = null;
            for (Parser.Qualif qual : pnode.getQualList()) {
                if (qual.getName().equals(q)) {
                    express = qual.getStr();
                    break;
                }
            }
            if (express == null) {
                express = "NULL";
            } else {
                trivial = false;
            }
            qual_col[qi + 2] = q;
            qual_alias[qi + 2] = express;
            this.tempo_tbl_format += ", " + q + " float8";
            qi++;
        }
        String p_tempo_table = "tempo_" + mr.getName() + "primary";
        String s_tempo_table = "tempo_" + mr.getName() + "secondary";
        retour.add(Database.getWrapper().getDropTempoTable(p_tempo_table));
        retour.add(Database.getWrapper().getCreateTempoTable(p_tempo_table, "") + " AS " + buildFromQuery(this.pnode.getPrimary(), this.mr.getPrimary_coll(), this.mr.getPrimary_category(), this.attrQual_p, this.attrCond_p, true, rmPrim));
        retour.add(Database.getWrapper().getDropTempoTable(s_tempo_table));
        retour.add(Database.getWrapper().getCreateTempoTable(s_tempo_table, "") + " AS " + buildFromQuery(this.pnode.getSecondary(), this.mr.getSecondary_coll(), this.mr.getSecondary_category(), this.attrQual_s, this.attrCond_s, false, rmSec));
        String matcher = Database.getWrapper().selectIntoTempoTable(Database.getWrapper().getTempoTableName(this.tempo_tbl), qual_col, qual_alias) + "\nFROM " + Database.getWrapper().getTempoTableName(p_tempo_table) + " p CROSS JOIN " + Database.getWrapper().getTempoTableName(s_tempo_table) + " s\n";
        List<Parser.Condition> lcond = pnode.getCondList();
        boolean empty = true;
        if (lcond.size() > 0) {
            empty = false;
            trivial = false;
            matcher += "WHERE " + lcond.get(0).getStr();
            for (int i = 1; i < lcond.size(); i++) matcher += " AND " + lcond.get(i).getStr();
        }
        if (empty && this.conditionUcdList.size() > 0) {
            trivial = false;
            matcher += "WHERE " + this.conditionUcdList.get(0);
            for (int i = 1; i < this.conditionUcdList.size(); i++) {
                matcher += this.conditionUcdList.get(i);
            }
        } else {
            for (String condition : this.conditionUcdList) matcher += " AND " + condition;
        }
        if (!trivial) {
            retour.add(matcher + ";");
            return retour.toArray(new String[0]);
        } else {
            return null;
        }
    }

    /**
	 * @param from
	 * @param colName
	 * @param category
	 * @param ucdm
	 * @param attrQual
	 * @param attrCond
	 * @param primary
	 * @param rum
	 * @return
	 * @throws SaadaException
	 */
    public final String buildFromQuery(Parser.From from, String colName, int category, List<String> attrQual, List<String> attrCond, boolean primary, RelationUCDsManager rum) throws SaadaException {
        String allAttr = computeAllAttr(attrQual, attrCond);
        String tabCol = Database.getCachemeta().getCollectionTableName(colName, category);
        StringBuffer strBuff = new StringBuffer("");
        strBuff.append("SELECT ").append(tabCol).append(".oidsaada as oidsaada").append(allAttr);
        this.tables_to_lock.add(tabCol);
        if (rum == null) {
            strBuff.append("\nFROM ").append(tabCol);
            String[] tabClass = from.getListClass();
            if (from.hasWAC() || (tabClass.length == 1 && !tabClass[0].equals("*"))) {
                this.tables_to_lock.add(tabClass[0]);
                strBuff.append("\nINNER JOIN ").append(tabClass[0]);
                strBuff.append("\nON ").append(tabCol).append(".oidsaada=").append(tabClass[0]).append(".oidsaada");
                if (from.hasWAC() | from.hasWAS() | from.hasWUCD()) strBuff.append("\nWHERE ");
                boolean hasPrevious = false;
                if (from.hasWAC()) {
                    strBuff.append(from.getWAC());
                    hasPrevious = true;
                }
                if (from.hasWAS()) {
                    if (hasPrevious) strBuff.append(" AND "); else hasPrevious = true;
                    strBuff.append(from.hasWAS());
                }
            } else if (from.hasWUCD()) {
                strBuff.append("\nINNER JOIN (");
                StringBuffer sBuff = new StringBuffer();
                boolean empty = true;
                if (empty) QueryException.throwNewException(SaadaException.NO_QUERIED_CLASS, "No classe to query on! No classe have all requested UCDs!");
                strBuff.append(sBuff.substring(0, sBuff.length() - (new String("\nUNION ")).length())).append(") u").append(primary ? "prim" : "sec");
                strBuff.append("\nON ").append(tabCol).append(".oidsaada=oidunion");
                if (from.hasWAS()) strBuff.append("\nWHERE ").append(from.hasWAS());
            } else if (from.hasWAS()) {
                strBuff.append("\nWHERE ").append(from.getWAS());
                if (!tabClass[0].equals("*")) {
                    strBuff.append(Query.getCollectionConstraintOnClass(tabCol, from.getListClass()));
                }
            } else {
                if (!tabClass[0].equals("*")) strBuff.append("\nWHERE ").append(Query.getCollectionConstraintOnClass(tabCol, from.getListClass()));
            }
        } else {
            String[] tabClass = from.getListClass();
            tabClass = rum.classNamesHavingAllUCDs(tabClass).toArray(new String[0]);
            if (tabClass == null || tabClass.length == 0) QueryException.throwNewException(SaadaException.NO_QUERIED_CLASS, "No classe to query on! No classe have all requested UCDs!");
            if (from.hasWAC() || (tabClass.length == 1 && !tabClass[0].equals("*"))) {
                strBuff.append(computeFromList(rum.getAttributeListWithFunctionAndAlias(tabClass[0])));
                strBuff.append("\nFROM ").append(tabCol);
                this.tables_to_lock.add(tabCol);
                strBuff.append("\nINNER JOIN ").append(tabClass[0]);
                this.tables_to_lock.add(tabClass[0]);
                strBuff.append("\nON ").append(tabCol).append(".oidsaada=").append(tabClass[0]).append(".oidsaada");
                strBuff.append("\nWHERE ");
                boolean hasPrevious = false;
                if (from.hasWAC()) {
                    strBuff.append(from.getWAC());
                    hasPrevious = true;
                }
                if (from.hasWAS()) {
                    if (hasPrevious) strBuff.append(" AND "); else hasPrevious = true;
                    strBuff.append(from.hasWAS());
                }
            } else if (from.hasWUCD()) {
                strBuff.append(computeFromList(rum.getAliases()));
                strBuff.append("\nFROM ").append(tabCol);
                this.tables_to_lock.add(tabCol);
                strBuff.append("\nINNER JOIN (");
                StringBuffer sBuff = new StringBuffer();
                boolean empty = true;
                if (empty) QueryException.throwNewException(SaadaException.NO_QUERIED_CLASS, "No classe to query on! No classe have all requested UCDs!");
                strBuff.append(sBuff.substring(0, sBuff.length() - (new String("\nUNION ")).length())).append(") u").append(primary ? "prim" : "sec");
                strBuff.append("\nON ").append(tabCol).append("oidsaada=oidunion");
                if (from.hasWAS()) strBuff.append("\nWHERE ").append(from.hasWAS());
            } else {
                strBuff.append(computeFromList(rum.getAliases()));
                strBuff.append("\nFROM ").append(tabCol);
                this.tables_to_lock.add(tabCol);
                strBuff.append("\nINNER JOIN (");
                StringBuffer sBuff = new StringBuffer();
                for (String className : tabClass) {
                    sBuff.append("\nSELECT oidsaada as oidunion").append(computeFromList(rum.getAttributeListWithFunctionAndAlias(className)));
                    sBuff.append(" FROM ").append(className);
                    this.tables_to_lock.add(className);
                    sBuff.append("\nUNION ");
                }
                strBuff.append(sBuff.substring(0, sBuff.length() - (new String("\nUNION ")).length())).append(") u").append(primary ? "prim" : "sec");
                strBuff.append("\nON ").append(tabCol).append("oidsaada=oidunion");
                if (from.hasWAS()) strBuff.append("\nWHERE ").append(from.getWAS());
            }
        }
        return strBuff.toString();
    }

    /**
	 * @param attrQual
	 * @param attrCond
	 * @return
	 */
    private static final String computeAllAttr(List<String> attrQual, List<String> attrCond) {
        Set<String> ts = new TreeSet<String>(attrQual);
        ts.addAll(attrCond);
        if (ts.contains("oidsaada")) ts.remove("oidsaada");
        StringBuffer strBuff = new StringBuffer("");
        for (String attr : ts) strBuff.append(", ").append(attr);
        return strBuff.toString();
    }

    /**
	 * @param ls
	 * @return
	 */
    private static final String computeFromList(Set<String> ls) {
        if (ls.contains("oidsaada")) ls.remove("oidsaada");
        StringBuffer strBuff = new StringBuffer("");
        for (String attr : ls) strBuff.append(", ").append(attr);
        return strBuff.toString();
    }

    /**
	 * @return Returns the neighbour_detector.
	 */
    public KDTree getNeighbour_detector() {
        return neighbour_detector;
    }

    /**
	 * Init the KDTree with the primary coverage of correlator. This method builds the KDTree, that can take time and memory.
	 * It must be invoked
	 * @throws SQLException
	 * @throws SaadaException
	 */
    public void initNeighbour_detector() throws SaadaException {
        if (this.neighbour_detector != null) {
            if (this.dist_unit.equals("degree")) {
                this.unit2rad_factor = Math.PI / 180.0;
            } else if (this.dist_unit.equals("arcmin")) {
                this.unit2rad_factor = (Math.PI / 180.0) / 60.0;
            } else if (this.dist_unit.equals("arcsec")) {
                this.unit2rad_factor = (Math.PI / 180.0) / 3600.0;
            } else if (this.dist_unit.equals("mas")) {
                this.unit2rad_factor = (Math.PI / 180.0) / 3600000.0;
            } else if (this.dist_unit.equals("sigma")) {
                this.unit2rad_factor = 1;
            } else {
                QueryException.throwNewException(SaadaException.UNSUPPORTED_MODE, "unit <" + this.dist_unit + "> not supported");
            }
            dist_max *= this.unit2rad_factor;
            this.neighbour_detector.init(mr.getSecondary_coll(), this.secondary_classes, mr.getSecondary_category());
        }
    }

    /**
	 * @param target
	 * @return
	 * @throws QueryException 
	 */
    public TreeMap<Double, HasKCoo> getNeighbour(final HasKCoo target) throws QueryException {
        if (this.neighbour_detector != null && this.neighbour_detector.getPointSize() > 0) {
            return this.neighbour_detector.getCounterparts(target, this.number_neighbour, this.dist_max);
        } else {
            return null;
        }
    }

    /**
	 * Return a query on the secondary subclasses with positon defined
	 * @return
	 * @throws SaadaException
	 */
    public String getQueryOnPrimaryCollection(boolean with_error) throws SaadaException {
        String class_select = "";
        if (this.primary_classes.length > 0 && !this.primary_classes[0].equals("*")) {
            for (String classe : this.primary_classes) {
                MetaClass mc;
                try {
                    mc = Database.getCachemeta().getClass(classe);
                } catch (SaadaException e) {
                    throw new IllegalArgumentException("Class <" + classe + "> Does not exist");
                }
                if (!mc.getCollection_name().equals(this.mr.getPrimary_coll())) {
                    throw new IllegalArgumentException("Class <" + classe + "> not from collection <" + this.mr.getPrimary_coll() + ">");
                }
                if (mc.getCategory() != this.mr.getPrimary_category()) {
                    throw new IllegalArgumentException("Class <" + classe + "> not from category <" + Category.explain(this.mr.getPrimary_category()) + ">");
                }
                if (class_select.length() > 0) {
                    class_select += " OR ";
                }
                class_select += " ((oidsaada>>32) & 65535) = " + mc.getId();
            }
            class_select = " AND (" + class_select + ") ";
        }
        String scat = null;
        try {
            scat = this.mr.getPrimary_coll() + "_" + Category.explain(this.mr.getPrimary_category()).toLowerCase();
        } catch (SaadaException e) {
            throw new IllegalArgumentException("Bad collection or category");
        }
        if (!with_error) {
            return "SELECT pos_x_csa, pos_y_csa, pos_z_csa, oidsaada \nFROM " + scat + " \nWHERE pos_x_csa is not null AND pos_y_csa is not null AND pos_z_csa is not null " + class_select;
        } else {
            return "SELECT pos_x_csa, pos_y_csa, pos_z_csa, error_maj_csa, oidsaada \nFROM " + scat + " \nWHERE pos_x_csa is not null AND pos_y_csa is not null AND pos_z_csa is not null " + class_select;
        }
    }

    /**
	 * @param relation_name
	 * @return
	 * @throws Exception
	 */
    public String computeNeighbourhoodLinks(String relation_name) throws Exception {
        String table_name = "tempo_" + relation_name + "_kdtree";
        if (Messenger.debug_mode) Messenger.printMsg(Messenger.DEBUG, "Build temporay table for correlation computed with KDTree");
        SQLTable.createTemporaryTable(table_name, "oidprimary bigint,oidsecondary bigint, distance double precision ", "", true);
        String tempo_table_name = Database.getWrapper().getTempoTableName(table_name);
        SQLLargeQuery squery = new SQLLargeQuery();
        ResultSet primary_rs = squery.run(this.getQueryOnPrimaryCollection(true));
        TreeMap<Double, HasKCoo> ret;
        String kdtreedumpfile = Repository.getTmpPath() + Database.getSepar() + relation_name + ".kdtree.psql";
        BufferedWriter kdtreetmpfile = new BufferedWriter(new FileWriter(kdtreedumpfile));
        int cpt = 0;
        int lcpt = 0;
        while (primary_rs.next()) {
            cpt++;
            if ((cpt % 10) == 0) {
                manager.processUserRequest();
                if ((cpt % 1000) == 0) {
                    Messenger.printMsg(Messenger.TRACE, cpt + " counterpart candidates processed (" + lcpt + " links)");
                }
            }
            CounterPart cp = this.neighbour_detector.getCounterpart(primary_rs);
            ret = this.getNeighbour(cp);
            if (ret == null) break;
            if (ret.size() > 0) {
                for (HasKCoo hkc : ret.values()) {
                    lcpt++;
                    CounterPart retval = (CounterPart) hkc;
                    kdtreetmpfile.write(cp.getOidsaada() + "\t" + retval.getOidsaada() + "\t" + retval.getDistance() / this.getUnit2rad_factor() + "\n");
                }
            }
        }
        Messenger.printMsg(Messenger.TRACE, cpt + " counterpart candidates processed (" + lcpt + " links)");
        kdtreetmpfile.close();
        Messenger.incrementeProgress();
        SQLTable.addQueryToTransaction("LOADTSVTABLE " + tempo_table_name + " 3 " + kdtreedumpfile);
        SQLTable.addQueryToTransaction("CREATE INDEX " + table_name.toLowerCase() + "_oidprimary ON " + tempo_table_name + "(oidprimary)");
        SQLTable.addQueryToTransaction("CREATE INDEX " + table_name.toLowerCase() + "_oidsecondary ON " + tempo_table_name + "(oidsecondary)");
        SQLTable.indexTable(tempo_table_name, this.manager);
        squery.close();
        return tempo_table_name;
    }

    /**
	 * @param args
	 */
    public static void main(String[] args) {
        ArgsParser ap;
        try {
            String rel_name = "SelfQual";
            Database.init("SQLITE");
            CorrQueryTranslator cqt = new CorrQueryTranslator(null, "PopulateRelation Knn\n" + "PrimaryFrom *\n" + "SecondaryFrom *\n" + "ConditionKnn{100, 15 [arcmin]}", "tempo_" + rel_name);
            cqt.parse();
            System.out.println(cqt.buildQuery());
            SQLTable.beginTransaction();
            (new RelationManager("Knn")).populateWithQuery();
            SQLTable.commitTransaction();
        } catch (Exception e) {
            Messenger.printStackTrace(e);
        }
    }

    /**
	 * @return Returns the unit2rad_factor.
	 */
    public double getUnit2rad_factor() {
        return unit2rad_factor;
    }

    final class UcdCond {

        private final String ucd;

        private final String substitute;

        private final String unit;

        private final boolean prim;

        private UcdCond(String ucd, String subs, String unit, boolean prim) {
            this.ucd = ucd;
            this.substitute = subs;
            this.unit = unit;
            this.prim = prim;
        }

        public final String getUcd() {
            return this.ucd;
        }

        public final String getUnit() {
            return this.unit;
        }

        public final String getSubstitute() {
            return this.substitute;
        }
    }
}
