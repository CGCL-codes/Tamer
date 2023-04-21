package org.stars.daostars.core.parser;

import org.stars.dao.exception.parser.BracketNotBalancedException;
import org.stars.daostars.core.SqlQuery;
import org.stars.daostars.sqlmapper.SqlMapper;

/**
 * Controlla le parentesi nelle varie query. Tutte le query devono essere
 * aperte/chiuse correttamente.
 * 
 * @author Francesco Benincasa (908099)
 * 
 */
public abstract class VerifyParenthesis {

    /**
	 * Esegue un controllo sulle parentesi aperte.
	 * 
	 * @param definition
	 *            dao definition
	 * @return <code>true</code> se tutto è andato bene
	 * @throws Exception
	 *             in caso di errore
	 */
    public static boolean execute(SqlMapper definition) throws Exception {
        String sql;
        for (SqlQuery query : definition.getQuerySet().values()) {
            sql = query.getPlainSql();
            if (!execute(sql)) {
                throw (new BracketNotBalancedException("Dao " + definition.getName() + ", query " + query.getName() + ": error during parsing of parenthesis ( [. The number of the opened parenthesis is different from the number of the closed parenthesis."));
            }
            if (!executeParentesiGraffe(sql)) {
                throw (new BracketNotBalancedException("Dao " + definition.getName() + ", query " + query.getName() + ": error during parsing of parenthesis {. The number of the opened parenthesis is different from the number of the closed parenthesis."));
            }
        }
        return true;
    }

    /**
	 * Il metodo controlla il numero e la sequenza delle parentesi quadre e
	 * tonde, ignorando le parentesi tra apici.
	 * 
	 * @param s
	 *            stringa da controllare
	 * @return <code>true</code> se tutto va bene.
	 */
    public static boolean execute(String s) {
        String sequenza = "";
        boolean errore = false;
        boolean apiceAperto = false;
        for (int idx = 0; idx < s.length() && !errore; idx++) {
            char c1 = s.charAt(idx);
            if (c1 == '\'') {
                apiceAperto = !apiceAperto;
            } else if (!apiceAperto && (c1 == '[' || c1 == '(')) {
                sequenza += c1;
            } else if (!apiceAperto && (c1 == ']' || c1 == ')')) {
                errore = sequenza.length() == 0;
                if (!errore) {
                    char c2 = sequenza.charAt(sequenza.length() - 1);
                    errore = ((c1 == ']' && c2 != '[') || (c1 == ')' && c2 != '('));
                    if (!errore) sequenza = sequenza.substring(0, sequenza.length() - 1);
                }
            }
        }
        errore |= sequenza.length() > 0 || apiceAperto;
        return !errore;
    }

    /**
	 * Il metodo controlla il numero e la sequenza delle parentesi graffa,
	 * quadra e tonda, ignorando le parentesi tra apici.
	 * 
	 * @param s
	 *            stringa da controllare
	 * @return <code>true</code> se tutto va bene.
	 */
    public static boolean executeParentesiGraffe(String s) {
        String sequenza = "";
        boolean errore = false;
        boolean apiceAperto = false;
        for (int idx = 0; idx < s.length() && !errore; idx++) {
            char c1 = s.charAt(idx);
            if (c1 == '\'') {
                apiceAperto = !apiceAperto;
            } else if (!apiceAperto && (c1 == '{')) {
                sequenza += c1;
            } else if (!apiceAperto && (c1 == '}')) {
                errore = sequenza.length() == 0;
                if (!errore) {
                    char c2 = sequenza.charAt(sequenza.length() - 1);
                    errore = (c1 == '}' && c2 != '{');
                    if (!errore) sequenza = sequenza.substring(0, sequenza.length() - 1);
                }
            }
        }
        errore |= sequenza.length() > 0 || apiceAperto;
        return !errore;
    }
}
