package edu.url.lasalle.campus.scorm2004rte.server.ActivityTree.Elements;

import java.io.Serializable;
import edu.url.lasalle.campus.scorm2004rte.server.DataBase.DynamicData.UserObjective;
import edu.url.lasalle.campus.scorm2004rte.system.Constants.SequencingStatus;

/**
 * $Id: ObjectiveCluster.java,v 1.7 2008/01/18 18:07:24 ecespedes Exp $ * 
 * <b>T�tol:</b> ObjectiveCluster <br /><br />
 * <b>Descripci�:</b> En aquesta classe hi guardarem un objective, ja sigui<br>
 * un definit sota demanda pel manifest o un que crearem autom�ticament per <br>
 * enmagatzemar l'estat del cluster i d'aquesta manera evitar-nos baixar a <br>
 * nivell dels fills per saber l'estat.<br><br>
 * 
 * @author Eduard C�spedes i Borr�s /Enginyeria LaSalle/ ecespedes@salle.url.edu
 * 
 * @version 1.0 $Revision: 1.7 $ $Date: 2008/01/18 18:07:24 $
 * $Log: ObjectiveCluster.java,v $
 * Revision 1.7  2008/01/18 18:07:24  ecespedes
 * Serialitzades TOTES les classes per tal de que els altres puguin fer proves
 * en paral�lel amb el proc�s de desenvolupament del gestor de BD.
 *
 * Revision 1.6  2007/12/21 17:12:44  ecespedes
 * Implementant CourseManager.OverallSequencingProcess
 *
 * Revision 1.5  2007/12/20 20:45:53  ecespedes
 * Implementat l'Objective Map
 *
 * Revision 1.4  2007/12/13 15:25:12  ecespedes
 * Problemes amb el sistema d'arbre de clusters.
 * Falla l'ObjectiveStatusKnown.
 *
 * Revision 1.3  2007/12/11 16:00:43  ecespedes
 * Suprimint par�metres: ObjectiveInterface el par�metre RollupRule.
 *
 * Revision 1.2  2007/12/11 15:29:27  ecespedes
 * Arreglant bugs i optimitzant solucions.
 *
 * Revision 1.1  2007/12/09 22:32:16  ecespedes
 * Els objectius dels clusters es tracten i es guarden de manera diferent.
 * 
 */
public final class ObjectiveCluster implements ObjectiveInterface, Serializable {

    /**
	 * 
	 */
    private static final long serialVersionUID = 5826929093703858778L;

    /**
	 * �s per no fer una relaci� amb el SequencingStatus.Unknown.
	 * int Type = 0
	 */
    private static final int UNKNOWN = 0;

    /**
	 * �s per no fer una relaci� amb el SequencingStatus.Passed.
	 * int Type = 1
	 */
    private static final int PASSED = 1;

    /**
	 * �s per no fer una relaci� amb el SequencingStatus.Failed.
	 * int Type = 2
	 */
    private static final int FAILED = 2;

    /**
	 * Aquest ser� el valor inicial o en cas d'error s'assignar� 
	 * aquest valor a les variables. De manera que el seq�enciament
	 * quan rebi un -1 el que far� ser� fer un rollup i actualitzar
	 * aquesta variable. 
	 * 
	 * int Type = -1
	 */
    private static final int NULL = -1;

    /**
	 * �s l'identificador de l'Item en el que estem.
	 */
    public String itemID = null;

    /**
	 * Identificador de l'objectiu.
	 * String Type.
	 */
    public String ObjectiveID = null;

    /**
	 * Indica quan l'actual objectiu te un valor satisfactori. 
	 * Forma part de 'Objective Progress Information'
	 */
    public boolean ObjectiveProgressStatus = false;

    /**
	 * Indica que l'objectiu te un valor v�lid per la mesura.
	 * Forma part de 'Objective Progress Information'
	 * AKA: SatisfiedByMeasure
	 */
    public boolean satisfiedByMeasure = false;

    /**
	 * La mesura de l'objectiu. 
	 * Forma part de 'Objective Progress Information'
	 * AKA: minNormalizedMeasure
	 */
    public Double minNormalizedMeasure = null;

    /**
	 * Variable que utilitzarem per marcar si tenim o no una refer�ncia
	 * a una variable global. 
	 */
    public boolean hasMapInfo = false;

    /**
	 * El mapInfo est� enmagatzemat aqu�.
	 */
    public MapInfo mapInfo = null;

    /**
	 * Ens ha de retorar un boole� indicant-nos si �s coneix 
	 * la mesura de la progressi� dels objectius (passed),
	 * no hi ha mesura (failed) o no ho sabem (unknown).
	 * 
	 * A aquesta funci� li passarem tota la regla de RollupRule perqu�
	 * cada 'node' pugui agafar tots els par�metres necessaris per 
	 * tractar la condici�: operador (not|noOp), i sobretot
	 * el childActivitySetType (all, any, none, atLeastCount, atLeastPercent),
	 * per als dos �ltims valors tamb� haurem d'agafar minimumCount i 
	 * el minimumPercent.
	 * 
	 * @param userObjective : UserObjective Type.
	 * @return Constants.SequencingStatus: { Passed, Failed, Unknown }.
	*/
    public SequencingStatus activityProgressKnown(final UserObjective userObjective) {
        int getType = 0;
        if (ObjectiveID != null) {
            getType = userObjective.clusterTree.get(itemID).objective.get(ObjectiveID).activityProgressKnown;
        } else {
            getType = userObjective.clusterTree.get(itemID).objective.get(itemID).activityProgressKnown;
        }
        switch(getType) {
            case PASSED:
                return SequencingStatus.Passed;
            case FAILED:
                return SequencingStatus.Failed;
            case UNKNOWN:
                return SequencingStatus.Unknown;
            default:
                return null;
        }
    }

    /**
	 * Aquesta funci� actualitza el valor del ObjectiveCluster
	 * en cas de que el primaryObjective sigui una instancia 
	 * d'aquesta classe.
	 * 
	 * @param userObjective : UserObjective Type
	 * @param newValue : SequencingStatus Type
	 * @return boolean Type: False si hi ha hagut algun error.
	 */
    public boolean updateActivityProgressKnown(final UserObjective userObjective, final SequencingStatus newValue) {
        int resultValue = NULL;
        boolean returnValue = true;
        if (newValue == null) {
            returnValue = false;
        } else {
            if (newValue.equals(SequencingStatus.Passed)) {
                resultValue = PASSED;
            } else if (newValue.equals(SequencingStatus.Failed)) {
                resultValue = FAILED;
            } else if (newValue.equals(SequencingStatus.Unknown)) {
                resultValue = UNKNOWN;
            } else {
                returnValue = false;
            }
        }
        if (ObjectiveID != null) {
            userObjective.clusterTree.get(itemID).objective.get(ObjectiveID).activityProgressKnown = resultValue;
        } else {
            userObjective.clusterTree.get(itemID).objective.get(itemID).activityProgressKnown = resultValue;
        }
        return returnValue;
    }

    /**
	 * Ens ha de retorar un boole� indicant-nos si l'activitat ha
	 * estat accedida (passed), no ha estat accedida (failed) o 
	 * no ho sabem (unknown).
	 * 
	 * A aquesta funci� li passarem tota la regla de RollupRule perqu�
	 * cada 'node' pugui agafar tots els par�metres necessaris per 
	 * tractar la condici�: operador (not|noOp), i sobretot
	 * el childActivitySetType (all, any, none, atLeastCount, atLeastPercent),
	 * per als dos �ltims valors tamb� haurem d'agafar minimumCount i 
	 * el minimumPercent.
	 * 
	 * @param userObjective : UserObjective Type.
	 * @return Constants.SequencingStatus: { Passed, Failed, Unknown }.
	*/
    public SequencingStatus attempted(final UserObjective userObjective) {
        int getType = 0;
        if (ObjectiveID != null) {
            getType = userObjective.clusterTree.get(itemID).objective.get(ObjectiveID).attempted;
        } else {
            getType = userObjective.clusterTree.get(itemID).objective.get(itemID).attempted;
        }
        switch(getType) {
            case PASSED:
                return SequencingStatus.Passed;
            case FAILED:
                return SequencingStatus.Failed;
            case UNKNOWN:
                return SequencingStatus.Unknown;
            default:
                return null;
        }
    }

    /**
	 * Aquesta funci� actualitza el valor del ObjectiveCluster
	 * en cas de que el primaryObjective sigui una instancia 
	 * d'aquesta classe.
	 * 
	 * @param userObjective : UserObjective Type
	 * @param newValue : SequencingStatus Type
	 * @return boolean Type: False si hi ha hagut algun error.
	 */
    public boolean updateAttempted(final UserObjective userObjective, final SequencingStatus newValue) {
        int resultValue = NULL;
        boolean returnValue = true;
        if (newValue == null) {
            returnValue = false;
        } else {
            if (newValue.equals(SequencingStatus.Passed)) {
                resultValue = PASSED;
            } else if (newValue.equals(SequencingStatus.Failed)) {
                resultValue = FAILED;
            } else if (newValue.equals(SequencingStatus.Unknown)) {
                resultValue = UNKNOWN;
            } else {
                returnValue = false;
            }
        }
        if (ObjectiveID != null) {
            userObjective.clusterTree.get(itemID).objective.get(ObjectiveID).attempted = resultValue;
        } else {
            userObjective.clusterTree.get(itemID).objective.get(itemID).attempted = resultValue;
        }
        return returnValue;
    }

    /**
	 * Si volem saber si una activitat est� completada preguntarem 
	 * passant com a par�metre afirmmative = TRUE, d'aquesta manera
	 * si ens retorna un Passed voldr� dir que s� que est� completed.
	 * 
	 * Si volem saber si una activitat �s incomplete preguntarem
	 * passant com a par�metre afirmative = FALSE. D'aquesta manera
	 * si ens retorna un Passed voldr� dir que �s incomplete.
	 * 
	 * @param userObjective : UserObjective Type.
	 * @param afirmative : boolean Type { True:completed, False:incomplete }
	 * @return Constants.SequencingStatus: { Passed, Failed, Unknown }.
	 */
    public SequencingStatus completed(final UserObjective userObjective, final boolean afirmative) {
        int getType = 0;
        if (ObjectiveID != null) {
            getType = userObjective.clusterTree.get(itemID).objective.get(ObjectiveID).completed;
        } else {
            getType = userObjective.clusterTree.get(itemID).objective.get(itemID).completed;
        }
        switch(getType) {
            case PASSED:
                if (afirmative) {
                    return SequencingStatus.Passed;
                } else {
                    return SequencingStatus.Failed;
                }
            case FAILED:
                if (afirmative) {
                    return SequencingStatus.Failed;
                } else {
                    return SequencingStatus.Passed;
                }
            case UNKNOWN:
                return SequencingStatus.Unknown;
            default:
                return null;
        }
    }

    /**
	 * Aquesta funci� actualitza el valor del ObjectiveCluster
	 * en cas de que el primaryObjective sigui una instancia 
	 * d'aquesta classe.
	 * 
	 * @param userObjective : UserObjective Type
	 * @param newValue : SequencingStatus Type
	 * @return boolean Type: False si hi ha hagut algun error.
	 */
    public boolean updateCompleted(final UserObjective userObjective, final SequencingStatus newValue) {
        int resultValue = NULL;
        boolean returnValue = true;
        if (newValue == null) {
            returnValue = false;
        } else {
            if (newValue.equals(SequencingStatus.Passed)) {
                resultValue = PASSED;
            } else if (newValue.equals(SequencingStatus.Failed)) {
                resultValue = FAILED;
            } else if (newValue.equals(SequencingStatus.Unknown)) {
                resultValue = UNKNOWN;
            } else {
                returnValue = false;
            }
        }
        if (ObjectiveID != null) {
            userObjective.clusterTree.get(itemID).objective.get(ObjectiveID).completed = resultValue;
        } else {
            userObjective.clusterTree.get(itemID).objective.get(itemID).completed = resultValue;
        }
        return returnValue;
    }

    /**
	 * Si objectiveMeasureKnown �s igual a 'passed' aleshores aquesta
	 * funci� ens retornar� aquesta mesura, que ser� un flotant.
	 * 
	 * A aquesta funci� li passarem tota la regla de RollupRule perqu�
	 * cada 'node' pugui agafar tots els par�metres necessaris per 
	 * tractar la condici�: operador (not|noOp), i sobretot
	 * el childActivitySetType (all, any, none, atLeastCount, atLeastPercent),
	 * per als dos �ltims valors tamb� haurem d'agafar minimumCount i 
	 * el minimumPercent.
	 * 
	 * @param userObjective : UserObjective Type.
	 * @return Double: Puntuaci� amb un m�xim de quatre decimals.
	*/
    public Double objectiveMeasure(final UserObjective userObjective) {
        Double getType = null;
        if (hasMapInfo && mapInfo != null) {
            if (mapInfo.readNormalizedMeasure && mapInfo.targetObjectiveID != null) {
                getType = userObjective.globalObjectiveMap.objective.get(mapInfo.targetObjectiveID).objectiveMeasure;
            }
        } else {
            if (ObjectiveID != null) {
                getType = userObjective.clusterTree.get(itemID).objective.get(ObjectiveID).objectiveMeasure;
            } else {
                getType = userObjective.clusterTree.get(itemID).objective.get(itemID).objectiveMeasure;
            }
        }
        return getType;
    }

    /**
	 * Aquesta funci� actualitza el valor del ObjectiveCluster
	 * en cas de que el primaryObjective sigui una instancia 
	 * d'aquesta classe.
	 * 
	 * @param userObjective : UserObjective Type
	 * @param enewValue : Double Type, la puntuaci�.
	 * @return boolean Type: False si hi ha hagut algun error.
	 */
    public boolean updateObjectiveMeasure(final UserObjective userObjective, final Double enewValue) {
        Double newValue = enewValue;
        if (hasMapInfo && mapInfo != null) {
            if (mapInfo.readNormalizedMeasure && mapInfo.targetObjectiveID != null) {
                newValue = userObjective.globalObjectiveMap.objective.get(mapInfo.targetObjectiveID).objectiveMeasure;
            }
        }
        if (newValue == null) {
            return false;
        }
        if (ObjectiveID != null) {
            userObjective.clusterTree.get(itemID).objective.get(ObjectiveID).objectiveMeasure = newValue;
        } else {
            userObjective.clusterTree.get(itemID).objective.get(itemID).objectiveMeasure = newValue;
        }
        if (hasMapInfo && mapInfo != null) {
            if (mapInfo.writeNormalizedMeasure && mapInfo.targetObjectiveID != null) {
                userObjective.globalObjectiveMap.objective.get(mapInfo.targetObjectiveID).objectiveMeasure = newValue;
            }
        }
        return true;
    }

    /**
	 * Ens ha de retorar un boole� indicant-nos si �s coneix 
	 * la mesura dels objectius (Objective Measure == passed),
	 * no hi ha mesura (failed) o no ho sabem (unknown).
	 * 
	 * A aquesta funci� li passarem tota la regla de RollupRule perqu�
	 * cada 'node' pugui agafar tots els par�metres necessaris per 
	 * tractar la condici�: operador (not|noOp), i sobretot
	 * el childActivitySetType (all, any, none, atLeastCount, atLeastPercent),
	 * per als dos �ltims valors tamb� haurem d'agafar minimumCount i 
	 * el minimumPercent.
	 * 
	 * @param userObjective : UserObjective Type.
	 * @return Constants.SequencingStatus: { Passed, Failed, Unknown }.
	*/
    public SequencingStatus objectiveMeasureKnown(final UserObjective userObjective) {
        int getType = 0;
        if (ObjectiveID != null) {
            getType = userObjective.clusterTree.get(itemID).objective.get(ObjectiveID).objectiveMeasureKnown;
        } else {
            getType = userObjective.clusterTree.get(itemID).objective.get(itemID).objectiveMeasureKnown;
        }
        switch(getType) {
            case PASSED:
                return SequencingStatus.Passed;
            case FAILED:
                return SequencingStatus.Failed;
            case UNKNOWN:
                return SequencingStatus.Unknown;
            default:
                return null;
        }
    }

    /**
	 * Aquesta funci� actualitza el valor del ObjectiveCluster
	 * en cas de que el primaryObjective sigui una instancia 
	 * d'aquesta classe.
	 * 
	 * @param userObjective : UserObjective Type
	 * @param newValue : SequencingStatus Type
	 * @return boolean Type: False si hi ha hagut algun error.
	 */
    public boolean updateObjectiveMeasureKnown(final UserObjective userObjective, final SequencingStatus newValue) {
        int resultValue = NULL;
        boolean returnValue = true;
        if (newValue == null) {
            returnValue = false;
        } else {
            if (newValue.equals(SequencingStatus.Passed)) {
                resultValue = PASSED;
            } else if (newValue.equals(SequencingStatus.Failed)) {
                resultValue = FAILED;
            } else if (newValue.equals(SequencingStatus.Unknown)) {
                resultValue = UNKNOWN;
            } else {
                returnValue = false;
            }
        }
        if (ObjectiveID != null) {
            userObjective.clusterTree.get(itemID).objective.get(ObjectiveID).objectiveMeasureKnown = resultValue;
        } else {
            userObjective.clusterTree.get(itemID).objective.get(itemID).objectiveMeasureKnown = resultValue;
        }
        return returnValue;
    }

    /**
	 * Ens ha de retorar un boole� indicant-nos si �s coneix 
	 * l'estat de l'objectiu (passed), si no el coneixem (failed)
	 * o si no ho sabem (unknown).
	 * 
	 * A aquesta funci� li passarem tota la regla de RollupRule perqu�
	 * cada 'node' pugui agafar tots els par�metres necessaris per 
	 * tractar la condici�: operador (not|noOp), i sobretot
	 * el childActivitySetType (all, any, none, atLeastCount, atLeastPercent),
	 * per als dos �ltims valors tamb� haurem d'agafar minimumCount i 
	 * el minimumPercent.
	 * 
	 * @param userObjective : UserObjective Type.
	 * @return Constants.SequencingStatus: { Passed, Failed, Unknown }.
	*/
    public SequencingStatus objectiveStatusKnown(final UserObjective userObjective) {
        int getType = 0;
        if (ObjectiveID != null) {
            getType = userObjective.clusterTree.get(itemID).objective.get(ObjectiveID).objectiveStatusKnown;
        } else {
            getType = userObjective.clusterTree.get(itemID).objective.get(itemID).objectiveStatusKnown;
        }
        switch(getType) {
            case PASSED:
                return SequencingStatus.Passed;
            case FAILED:
                return SequencingStatus.Failed;
            case UNKNOWN:
                return SequencingStatus.Unknown;
            default:
                return null;
        }
    }

    /**
	 * Aquesta funci� actualitza el valor del ObjectiveCluster
	 * en cas de que el primaryObjective sigui una instancia 
	 * d'aquesta classe.
	 * 
	 * @param userObjective : UserObjective Type
	 * @param newValue : SequencingStatus Type
	 * @return boolean Type: False si hi ha hagut algun error.
	 */
    public boolean updateObjectiveStatusKnown(final UserObjective userObjective, final SequencingStatus newValue) {
        int resultValue = NULL;
        boolean returnValue = true;
        if (newValue == null) {
            returnValue = false;
        } else {
            if (newValue.equals(SequencingStatus.Passed)) {
                resultValue = PASSED;
            } else if (newValue.equals(SequencingStatus.Failed)) {
                resultValue = FAILED;
            } else if (newValue.equals(SequencingStatus.Unknown)) {
                resultValue = UNKNOWN;
            } else {
                returnValue = false;
            }
        }
        if (ObjectiveID != null) {
            userObjective.clusterTree.get(itemID).objective.get(ObjectiveID).objectiveStatusKnown = resultValue;
        } else {
            userObjective.clusterTree.get(itemID).objective.get(itemID).objectiveStatusKnown = resultValue;
        }
        return returnValue;
    }

    /**
	 * Si volem saber si una activitat est� satisfeta preguntarem 
	 * passant com a par�metre afirmmative = TRUE, d'aquesta manera
	 * si ens retorna un Passed voldr� dir que s� que est� satisfeta.
	 * 
	 * Si volem saber si una activitat NO est� satisfeta preguntarem
	 * passant com a par�metre afirmative = FALSE. D'aquesta manera
	 * si ens retorna un Passed voldr� dir que NO est� satisfeta.
	 * 
	 * @param userObjective : UserObjective Type.
	 * @param afirmative : boolean Type { True:satisfied, False:notSatisfied }
	 * @return Constants.SequencingStatus: { Passed, Failed, Unknown }.
	 */
    public SequencingStatus satisfied(final UserObjective userObjective, final boolean afirmative) {
        int getType = 0;
        if (hasMapInfo && mapInfo != null) {
            if (mapInfo.readSatisfiedStatus && mapInfo.targetObjectiveID != null) {
                getType = userObjective.globalObjectiveMap.objective.get(mapInfo.targetObjectiveID).satisfied;
            }
        } else {
            if (ObjectiveID != null) {
                getType = userObjective.clusterTree.get(itemID).objective.get(ObjectiveID).satisfied;
            } else {
                getType = userObjective.clusterTree.get(itemID).objective.get(itemID).satisfied;
            }
        }
        switch(getType) {
            case PASSED:
                if (afirmative) {
                    return SequencingStatus.Passed;
                } else {
                    return SequencingStatus.Failed;
                }
            case FAILED:
                if (afirmative) {
                    return SequencingStatus.Failed;
                } else {
                    return SequencingStatus.Passed;
                }
            case UNKNOWN:
                return SequencingStatus.Unknown;
            default:
                return null;
        }
    }

    /**
	 * Aquesta funci� actualitza el valor del ObjectiveCluster
	 * en cas de que el primaryObjective sigui una instancia 
	 * d'aquesta classe.
	 * 
	 * @param userObjective : UserObjective Type
	 * @param enewValue : SequencingStatus Type
	 * @return boolean Type: False si hi ha hagut algun error.
	 */
    public boolean updateSatisfied(final UserObjective userObjective, final SequencingStatus enewValue) {
        SequencingStatus newValue = enewValue;
        int resultValue = NULL;
        boolean returnValue = true;
        if (hasMapInfo && mapInfo != null) {
            if (mapInfo.readSatisfiedStatus && mapInfo.targetObjectiveID != null) {
                newValue = satisfied(userObjective, true);
            }
        }
        if (newValue == null) {
            returnValue = false;
        } else {
            if (newValue.equals(SequencingStatus.Passed)) {
                resultValue = PASSED;
            } else if (newValue.equals(SequencingStatus.Failed)) {
                resultValue = FAILED;
            } else if (newValue.equals(SequencingStatus.Unknown)) {
                resultValue = UNKNOWN;
            } else {
                returnValue = false;
            }
        }
        if (hasMapInfo && mapInfo != null) {
            if (mapInfo.writeSatisfiedStatus && mapInfo.targetObjectiveID != null) {
                userObjective.globalObjectiveMap.objective.get(mapInfo.targetObjectiveID).satisfied = resultValue;
            }
        }
        if (ObjectiveID != null) {
            userObjective.clusterTree.get(itemID).objective.get(ObjectiveID).satisfied = resultValue;
        } else {
            userObjective.clusterTree.get(itemID).objective.get(itemID).satisfied = resultValue;
        }
        return returnValue;
    }

    /**
	 * Amb aquesta funci� ens solucionem la vida a l'hora de passar aquest
	 * valor entre classes que implementin aquesta interf�cie.
	 * 
	 * @return Double Type.
	 */
    public Double getMinNormalizedMeasure() {
        return minNormalizedMeasure;
    }

    /**
	 * Amb aquesta funci� ens solucionem la vida a l'hora de passar aquest
	 * par�metre entre classes que implementin aquesta interf�cie. 
	 * 
	 * @return boolean Type. 
	 */
    public boolean getSatisfiedByMeasure() {
        return satisfiedByMeasure;
    }
}
