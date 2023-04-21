package net.sf.ideoreport.tools;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import org.apache.commons.lang.ClassUtils;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import net.sf.ideoreport.api.datastructure.containers.parameter.IParameterValues;
import net.sf.ideoreport.api.enginestructure.exception.EngineException;
import net.sf.ideoreport.common.config.IDataProviderConfig;
import net.sf.ideoreport.engines.jasperengine.JasperEngineDefaultValues;
import net.sf.ideoreport.jasper.IdeoReportGroupWrapper;
import net.sf.ideoreport.jasper.IdeoReportSubReportWrapper;

/**
 * Velocity ne permet PAS l'utilisation de m�thodes statiques, mais utilise toujours un contexte dans lequel on
 * positionne des INSTANCES d'objets ; cette classe permet d'acc�der � des m�thodes utilitaires habituellement
 * statiques, � l'aide d'une instance d'objet.
 */
public class VelocityTools {

    /**
	 * Logger for this class
	 */
    private static final Log LOGGER = LogFactory.getLog(VelocityTools.class);

    /**
	 * Une seule instance de cette classe, que l'on utilise la plupart du temps en mode statique.
	 */
    private static VelocityTools INSTANCE;

    public VelocityTools() {
        super();
    }

    /**
    * Utilis� depuis les templates velocity pour lancer une exception explicite
    * @since 1.3.0
    */
    public void throwException(String pMessage) throws EngineException {
        throw new EngineException(pMessage);
    }

    public static VelocityTools getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new VelocityTools();
        }
        return INSTANCE;
    }

    /**
	 * Splits the provided text into an array, using whitespace as the separator.
	 * 
	 * @param pString
	 *            la string � s�parer
	 * @param pSeparator
	 *            le s�parateur
	 * @return String[] contenant la chaine
	 * @see org.apache.commons.lang.StringUtils
	 */
    public String[] split(String pString, String pSeparator) {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("split(\"" + pString + "\", \"" + pSeparator + "\")");
        }
        return org.apache.commons.lang.StringUtils.split(pString, pSeparator);
    }

    /**
	 * Splits the provided text into an array, using whitespace as the separator.
	 * 
	 * @param pString
	 *            la string � s�parer
	 * @param pSeparator
	 *            le s�parateur
	 * @return list de valeur contenu dans la string
	 * @see org.apache.commons.lang.StringUtils
	 */
    public List splitAsList(String pString, String pSeparator) {
        List vRetour = new ArrayList();
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("split(\"" + pString + "\", \"" + pSeparator + "\")");
        }
        vRetour = Arrays.asList(org.apache.commons.lang.StringUtils.split(pString, pSeparator));
        return vRetour;
    }

    /**
	 * Permet d'afficher un message sur la sortie standard
	 * 
	 * @param pMessage
	 *            message � afficher
	 */
    public void systemOut(String pMessage) {
        System.out.println(pMessage);
    }

    /**
    * @since 1.3.0
    * renvoie l'�l�ment situ� � l'index pIndex dans la liste pList, et null si non trouv�
    */
    public Object listGetAt(List pList, int pIndex) {
        Object vRetour = null;
        vRetour = pList.get(pIndex);
        return vRetour;
    }

    /**
	 * Permet de convertir une String en int (par d�faut 0)
	 * 
	 * @param pString
	 *            cha�ne � convertir
	 * @return la valeur convertie
	 */
    public Integer toInteger(String pString) {
        Integer vRetour = null;
        try {
            vRetour = new Integer(pString);
        } catch (Exception e) {
        }
        return vRetour;
    }

    /**
	 * Cette m�thode permet de r�cup�rer le pattern d'un champ ou d'une variable Si le pattern est sp�cifi� dans la
	 * config on le r�cup�re sinon par d�faut voici les pattern sp�cifi�s :
	 * <li><code>java.lang.Integer</code> : INTEGER_DEFAULT_FORMAT_PATTERN
	 * <li><code>java.lang.Number</code> : NUMBER_DEFAULT_FORMAT_PATTERN
	 * <li><code>java.util.Date</code> : DATE_DEFAULT_FORMAT_PATTERN
	 * 
	 * @param pClass
	 *            la classe de la variable ou du champs
	 * @param pDataProviderConfig
	 *            ensemble des DataContainer contenue dans la config afin de pouvoir r�cup�rer le pattern passer en
	 *            param�tre dans la config
	 * @param pParams
	 *            les IParameterValues sont necessaire pour r�cuperer les param�tres de la config
	 * @param pFieldName
	 *            le nom du champ ou de la variable qur le quel il faut appliquer le pattern
	 * @return renvoie une string pour l'attribut pattern d'un textfield
	 */
    public String getPattern(Class pClass, IDataProviderConfig pDataProviderConfig, IParameterValues pParams, String pFieldName) {
        String vRetour = pDataProviderConfig.getParameter("format.pattern." + pFieldName.toLowerCase(), pParams);
        List vClassAndParents = ClassUtils.getAllSuperclasses(pClass);
        vClassAndParents.add(0, pClass);
        if (StringUtils.isEmpty(vRetour)) {
            if (vClassAndParents.contains(java.lang.Integer.class)) {
                vRetour = pDataProviderConfig.getParameter("format.pattern.entier", pParams, JasperEngineDefaultValues.FORMAT_PATTERN_INTEGERS);
            } else if (vClassAndParents.contains(java.lang.Number.class)) {
                vRetour = pDataProviderConfig.getParameter("format.pattern.reel", pParams, JasperEngineDefaultValues.FORMAT_PATTERN_REEL);
            } else if (vClassAndParents.contains(java.util.Date.class)) {
                vRetour = pDataProviderConfig.getParameter("format.pattern.dates", pParams, JasperEngineDefaultValues.FORMAT_PATTERN_DATES);
            } else {
                vRetour = "";
            }
        }
        return vRetour;
    }

    /**
	 * Permet de r�cuperer une string pour que Jasper puisse interpr�ter la resource demand�e
	 * 
	 * @param pCode
	 *            le code dans la config
	 * @param pIDataProviderConfig
	 *            l'ensemble de la config
	 * @param pParams
	 *            l'ensemble des param�tres
	 * @return une string avec la cl� entour� : $R{XXXX}
	 */
    public String getRessource(String pCode, IDataProviderConfig pIDataProviderConfig, IParameterValues pParams) {
        String vRetour = getRessource(pIDataProviderConfig.getParameter(pCode, pParams, ""));
        return vRetour;
    }

    /**
    * Renvoie une r�f�rence � une ressource au sens jasper
    * @param pCode
    *            le code dans la config
    * @since 1.3.0
    * @return l'expression jasper
    */
    public String getRessource(String pCode) {
        String vRetour = "$R{" + pCode + "}";
        return vRetour;
    }

    /**
    * Renvoie une cha�ne de caract�re �chap�e au sens java
    * @param pExpression cha�ne en entr�e
    * @return la cha�ne �chapp�e correctement
    * @since 1.3.0
    */
    public String escapeJava(String pExpression) {
        String vRetour = StringEscapeUtils.escapeJava(pExpression);
        return vRetour;
    }

    /**
    * Permet de r�cuperer une string pour que Jasper puisse interpr�ter le field demand�
    * 
    * @param pCode
    *            le code dans la config
    * @param pIDataProviderConfig
    *            l'ensemble de la config
    * @param pParams
    *            l'ensemble des param�tres
    * @return une string avec le nom du champ entour� : $F{XXXX}
    */
    public String getFieldAsJRXML(String pField) {
        String vRetour = "";
        vRetour = "$F{" + pField + "}";
        return vRetour;
    }

    /**
    * Permet de r�cuperer une string pour que Jasper puisse interpr�ter le param�tre demand�
    * 
    * @param pCode
    *            le code dans la config
    * @param pIDataProviderConfig
    *            l'ensemble de la config
    * @param pParams
    *            l'ensemble des param�tres
    * @return une string avec le nom du param�tre entour� : $P{XXXX}
    */
    public String getParamAsJRXML(String pField) {
        String vRetour = "";
        vRetour = "$P{" + pField + "}";
        return vRetour;
    }

    /**
	 * Permet de cacher le champ passer en parametre pour ne pas l'afficher dans un tableau.
	 * 
	 * @param pFieldName
	 *            le nom du champs � cacher ou non
	 * @param pDataProviderConfig
	 *            la configuration
	 * @param pParams
	 *            les param�tres
	 * @return true sir le champ est cach� et false s'il est affich�
	 */
    public boolean fieldIsHiddenInTable(String pFieldName, IDataProviderConfig pDataProviderConfig, IParameterValues pParams) {
        boolean vRetour = false;
        List vList = this.splitAsList(pDataProviderConfig.getParameter("data.fields.hide", pParams), ",");
        for (Iterator iter = vList.iterator(); iter.hasNext(); ) {
            String vElement = (String) iter.next();
            if (vElement.equalsIgnoreCase(pFieldName)) {
                vRetour = true;
            }
        }
        return vRetour;
    }

    /**
    * Renvoie le nombre de champs cach�s pour un data provider donn�
    * @since 1.3.0
    * @param pDataProviderConfig
    * @param pParams
    * @return
    */
    public int nbHiddenFields(IDataProviderConfig pDataProviderConfig, IParameterValues pParams) {
        int vRetour = 0;
        List vList = this.splitAsList(pDataProviderConfig.getParameter("data.fields.hide", pParams), ",");
        if (vList != null) {
            vRetour = vList.size();
        }
        return vRetour;
    }

    /**
	 * Cette m�thode permet de gerer l'entete de fiche afin de savoir s'il y a un prefixe ou un suffixe ou les deux.
	 * 
	 * @param pField
	 *            le nom du champ � afficher.
	 * @param pPrefix
	 *            le prefixe
	 * @param pSuffix
	 *            le suffixe
	 * @return une String avec le code JRXML qui correspond � l'entete de fiche
	 */
    public String getCardLabel(String pField, String pPrefix, String pSuffix) {
        String vRetour = "";
        if (!pPrefix.equalsIgnoreCase("$R{}") && !pSuffix.equalsIgnoreCase("$R{}")) {
            vRetour = "" + pPrefix + " + $F{" + pField + "} + " + pSuffix + "";
        } else if (!pPrefix.equalsIgnoreCase("$R{}")) {
            vRetour = "" + pPrefix + " + $F{" + pField + "}";
        } else if (!pSuffix.equalsIgnoreCase("$R{}")) {
            vRetour = "$F{" + pField + "} + " + pSuffix + "";
        } else if (!pField.equalsIgnoreCase("")) {
            vRetour = "$F{" + pField + "}";
        }
        return vRetour;
    }

    /**
	 * Cette m�thode permet de savoir si un tableau est vide ou null.
	 * Elle �vite de lever une exception velocity. 
	 * @param pList
	 *            le tableau � tester
	 * @return true s'il est vide ou null et false dans les autres cas
	 */
    public boolean isNullOrEmpty(List pList) {
        boolean vRetour = false;
        if (pList == null || pList.isEmpty()) {
            vRetour = true;
        }
        return vRetour;
    }

    /**
    * Renvoie true si au moins un groupe de la liste pass�e en param�tre est un vrai gorupe (pas un
    * groupe technique). Permet de savoir si on doit afficher les ent�tes de colonnes lorsqu'on
    * n'a pas de groupe.
    * @since 1.3.0
    * @param pList
    * @return
    */
    public boolean hasNonTechnicalGroup(List pList) {
        boolean vRetour = false;
        if (pList != null) {
            for (Iterator vIt = pList.iterator(); vIt.hasNext(); ) {
                IdeoReportGroupWrapper vGroup = (IdeoReportGroupWrapper) vIt.next();
                vRetour = !(vGroup.isTechnicalGroup());
                if (vRetour) {
                    break;
                }
            }
        }
        return vRetour;
    }

    /**
    * Rajoute des quote � une string 
    * @param pStringToChange
    * @return une string avec des "
	 */
    public String addQuote(String pStringToChange) {
        String vRetour = null;
        vRetour = "\"" + pStringToChange + "\"";
        return vRetour;
    }

    public String getSubReportPosition(List pSubReportList) {
        String vRetour = "detail";
        for (Iterator iter = pSubReportList.iterator(); iter.hasNext(); ) {
            IdeoReportSubReportWrapper element = (IdeoReportSubReportWrapper) iter.next();
            vRetour = element.getPosition();
        }
        return vRetour;
    }
}
