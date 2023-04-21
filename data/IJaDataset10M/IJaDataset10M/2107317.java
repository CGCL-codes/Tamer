package org.tmapiutils.query.tolog.predicates;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.tmapiutils.query.tolog.TologParserException;
import org.tmapiutils.query.tolog.TologProcessingException;
import org.tmapiutils.query.tolog.parser.PlayerRolePair;
import org.tmapiutils.query.tolog.parser.PlayerRoleVarPair;
import org.tmapiutils.query.tolog.parser.Variable;
import org.tmapiutils.query.tolog.utils.VariableSet;
import uk.co.jezuk.mango.Predicate;
import uk.co.jezuk.mango.UnaryFunction;
import uk.co.jezuk.mango.iterators.PredicatedIterator;
import uk.co.jezuk.mango.iterators.TransformingIterator;
import org.tmapiutils.utils.IndexUtils;
import org.tmapi.core.Association;
import org.tmapi.core.AssociationRole;
import org.tmapi.core.TopicMap;
import org.tmapi.core.Topic;
import org.tmapi.core.Occurrence;
import org.tmapi.core.TopicName;
import org.tmapi.core.TMAPIException;
import org.tmapi.index.core.AssociationsIndex;
import org.tmapi.index.core.AssociationRolesIndex;

/**
 *
 * @author Kal Ahmed (kal[at]techquila.com)
 * @author Lars Heuer (heuer[at]semagia.com)
 */
public class DynamicAssociationPredicate extends PredicateBase {

    private TopicMap m_tm;

    private Topic m_assocType = null;

    private AssociationsIndex _assocIdx;

    private AssociationRolesIndex _roleIdx;

    private static final Log m_log = LogFactory.getLog(DynamicAssociationPredicate.class);

    public void initialise(TopicMap tm) throws TologParserException {
        m_tm = tm;
        try {
            _assocIdx = (AssociationsIndex) tm.getHelperObject(AssociationsIndex.class);
            _roleIdx = (AssociationRolesIndex) tm.getHelperObject(AssociationRolesIndex.class);
        } catch (TMAPIException e) {
            throw new TologParserException("Unable to get indexes requred for AssociationPredicate", e);
        }
    }

    public VariableSet matches(List params) throws TologProcessingException {
        if (m_log.isInfoEnabled()) m_log.info("START " + toString() + "with parameters " + params);
        int bestMatchIx = -1;
        Object bestMatch = null;
        Object assocType = params.get(0);
        if (assocType instanceof Topic) {
            bestMatch = assocType;
            bestMatchIx = 0;
        }
        int paramLength = getParameters().size();
        for (int i = 1; i < paramLength; i++) {
            PlayerRoleVarPair pr = (PlayerRoleVarPair) params.get(i);
            if (pr.getPlayerTopic() != null) {
                VariableSet ret = matchOnRolePlayer(i, pr.getPlayerTopic(), pr.getRoleTopic() == null ? (Object) pr.getRoleVar() : (Object) pr.getRoleTopic(), params);
                if (m_log.isDebugEnabled()) {
                    m_log.info("END " + toString() + " with results set: ");
                    ret.dump(m_log);
                }
                return ret;
            } else if ((bestMatch == null) && (pr.getRoleTopic() != null)) {
                bestMatch = pr;
                bestMatchIx = i;
            }
        }
        VariableSet ret = null;
        if (bestMatch instanceof Topic) {
            ret = matchOnAssocType(params);
        } else if (bestMatch instanceof PlayerRolePair) {
            ret = matchOnRoleType(((PlayerRolePair) bestMatch).getRoleTopic(), params);
        } else {
            ret = openMatch(params);
        }
        if (m_log.isInfoEnabled()) {
            m_log.info("END " + toString() + " with results set: ");
            ret.dump(m_log);
        }
        return ret;
    }

    /**
     * @param params
     * @return
     */
    private VariableSet openMatch(List params) throws TologProcessingException {
        return matchAssociations(m_tm.getAssociations().iterator(), params);
    }

    /**
     * @param bestMatchIx
     * @param topic
     * @return
     */
    private VariableSet matchOnRoleType(Topic roleType, List params) throws TologProcessingException {
        IndexUtils.updateIndex(_roleIdx);
        Collection roles = _roleIdx.getAssociationRolesByType(roleType);
        return matchAssociations(new TransformingIterator(roles.iterator(), new AssociationExtractor()), params);
    }

    /**
     * @param params
     * @return
     */
    private VariableSet matchOnAssocType(List params) throws TologProcessingException {
        IndexUtils.updateIndex(_assocIdx);
        Collection assocsToMatch = _assocIdx.getAssociationsByType((Topic) params.get(0));
        return matchAssociations(assocsToMatch.iterator(), params);
    }

    private VariableSet matchOnRolePlayer(int paramNum, Topic player, Object role, List params) throws TologProcessingException {
        if (m_log.isDebugEnabled()) {
            if (role != null) {
                m_log.debug("matchOnRolePlayer( player:" + player.getObjectId() + ", role:" + ((role instanceof Topic) ? ((Topic) role).getObjectId() : "$" + ((Variable) role).getName()) + " )");
            } else {
                m_log.debug("matchOnRolePlayer( player:" + player.getObjectId() + ", role:<unspecified>");
            }
        }
        Iterator it;
        if (role instanceof Topic) {
            it = new PredicatedIterator(player.getRolesPlayed().iterator(), new TypeTester((Topic) role));
        } else {
            it = player.getRolesPlayed().iterator();
        }
        if (m_log.isDebugEnabled()) {
            if (it.hasNext()) {
                m_log.debug("Match iterator has elements.");
            } else {
                m_log.debug("Match iterator is empty!");
            }
        }
        return matchAssociations(new TransformingIterator(it, new AssociationExtractor()), params);
    }

    private VariableSet matchAssociations(Iterator assocsToMatch, List params) throws TologProcessingException {
        Object assocTypeParam = params.get(0);
        Object assocTypePredicateParam = getParameters().get(0);
        m_log.debug("Association type parameter is " + assocTypeParam);
        VariableSet ret = createVariableSet();
        Map baseMapping = createParameterMap(params);
        Map mapping = new HashMap();
        while (assocsToMatch.hasNext()) {
            mapping.putAll(baseMapping);
            Association a = (Association) assocsToMatch.next();
            m_log.debug("Association type is " + String.valueOf(a.getType()));
            if (a.getAssociationRoles().size() < (getParameters().size() - 1)) {
                m_log.debug("Association has too few members. Got " + a.getAssociationRoles().size() + " need at least " + String.valueOf(getParameters().size() - 1));
                continue;
            }
            if (assocTypeParam instanceof Topic) {
                if (a.getType() == null) {
                    m_log.debug("Association is untyped and a type was expected.");
                    continue;
                } else if (!a.getType().equals(assocTypeParam)) {
                    m_log.debug("Association has the wrong type.");
                    continue;
                }
            }
            if (assocTypePredicateParam instanceof Variable) {
                mapping.put((Variable) assocTypePredicateParam, a.getType());
            }
            List rolePlayers = extractRolePlayers(a);
            matchRole(rolePlayers, 1, mapping, ret);
            mapping.clear();
        }
        return ret;
    }

    /**
     * @param params
     * @return
     */
    private Map _createParameterMap(List params) {
        m_log.debug("Parameter Map:");
        HashMap parameterMap = new HashMap();
        int max = getParameters().size();
        int offset = 0;
        for (int i = 0; i < max; i++) {
            Object p = getParameters().get(i);
            if (p instanceof Variable) {
                m_log.debug("  " + p.toString() + " = " + params.get(offset));
                parameterMap.put(p, params.get(offset++));
            } else if (p instanceof PlayerRoleVarPair) {
                PlayerRoleVarPair prvp = (PlayerRoleVarPair) p;
                if (params.get(offset) instanceof PlayerRoleVarPair) {
                    PlayerRoleVarPair valuePair = (PlayerRoleVarPair) params.get(offset++);
                    if (prvp.getPlayerVar() != null) {
                        m_log.debug("  " + prvp.getPlayerVar().toString() + " = " + getPlayerValue(valuePair));
                        parameterMap.put(prvp.getPlayerVar(), getPlayerValue(valuePair));
                    }
                    if (prvp.getRoleVar() != null) {
                        m_log.debug("  " + prvp.getRoleVar().toString() + " = " + getRoleValue(valuePair));
                        parameterMap.put(prvp.getRoleVar(), getRoleValue(valuePair));
                    }
                } else {
                    if (prvp.getPlayerVar() != null) {
                        m_log.debug("  " + prvp.getPlayerVar().toString() + " = " + params.get(offset));
                        parameterMap.put(prvp.getPlayerVar(), params.get(offset++));
                    }
                    if (prvp.getRoleVar() != null) {
                        m_log.debug("  " + prvp.getRoleVar().toString() + " = " + params.get(offset));
                        parameterMap.put(prvp.getRoleVar(), params.get(offset++));
                    }
                }
            }
        }
        return parameterMap;
    }

    /**
     * @param params
     * @return
     */
    private Map createParameterMap(List params) {
        m_log.debug("Parameter Map:");
        HashMap parameterMap = new HashMap();
        int max = getParameters().size();
        for (int i = 0; i < max; i++) {
            Object p = getParameters().get(i);
            if (p instanceof Variable) {
                m_log.debug("  " + p.toString() + " = " + params.get(i));
                parameterMap.put(p, params.get(i));
            } else if (p instanceof PlayerRoleVarPair) {
                PlayerRoleVarPair prvp = (PlayerRoleVarPair) p;
                if (params.get(i) instanceof PlayerRoleVarPair) {
                    PlayerRoleVarPair valuePair = (PlayerRoleVarPair) params.get(i);
                    if (prvp.getPlayerVar() != null) {
                        m_log.debug("  " + prvp.getPlayerVar().toString() + " = " + getPlayerValue(valuePair));
                        parameterMap.put(prvp.getPlayerVar(), getPlayerValue(valuePair));
                    }
                    if (prvp.getRoleVar() != null) {
                        m_log.debug("  " + prvp.getRoleVar().toString() + " = " + getRoleValue(valuePair));
                        parameterMap.put(prvp.getRoleVar(), getRoleValue(valuePair));
                    }
                } else {
                    if (prvp.getPlayerVar() != null) {
                        m_log.debug("  " + prvp.getPlayerVar().toString() + " = " + params.get(i));
                        parameterMap.put(prvp.getPlayerVar(), params.get(i));
                    }
                    if (prvp.getRoleVar() != null) {
                        throw new RuntimeException("Unexpected role!");
                    }
                }
            }
        }
        return parameterMap;
    }

    /**
     * Returns the value or variable stored for the player part of
     * the specified PlayerRoleVarPair
     * @param prvp
     */
    private Object getPlayerValue(PlayerRoleVarPair prvp) {
        if (prvp.getPlayerTopic() != null) return prvp.getPlayerTopic();
        if (prvp.getPlayerPlaceholder() != null) return prvp.getPlayerPlaceholder();
        if (prvp.getPlayerVar() != null) return prvp.getPlayerVar();
        return null;
    }

    /**
     * Returns the value or variable stored for the role part of
     * the specified PlayerRoleVarPair
     * @param prvp
     */
    private Object getRoleValue(PlayerRoleVarPair prvp) {
        if (prvp.getRoleTopic() != null) return prvp.getRoleTopic();
        if (prvp.getRolePlaceholder() != null) return prvp.getRolePlaceholder();
        if (prvp.getRoleVar() != null) return prvp.getRoleVar();
        return null;
    }

    private void matchRole(List rolePlayers, int paramIx, Map bindings, VariableSet varSet) throws TologProcessingException {
        if (m_log.isDebugEnabled()) m_log.debug("matchRole index " + paramIx);
        if (paramIx == getParameters().size()) {
            if (m_log.isDebugEnabled()) {
                m_log.debug("matchRole: Reached end of parameters. Adding variable mapping.");
                Iterator it = bindings.keySet().iterator();
                while (it.hasNext()) {
                    Object o = it.next();
                    m_log.debug("  " + o.toString() + " = " + bindings.get(o).toString());
                }
            }
            varSet.addRowFromMap(bindings);
            return;
        }
        PlayerRoleVarPair playerRoleParam = (PlayerRoleVarPair) getParameterValue(paramIx, bindings);
        if (m_log.isDebugEnabled()) m_log.debug("matchRole: playerRoleParam = " + playerRoleParam.toString());
        HashMap localBindings = new HashMap();
        localBindings.putAll(bindings);
        for (int i = 0; i < rolePlayers.size(); i++) {
            PlayerRolePair prp = (PlayerRolePair) rolePlayers.get(i);
            if (m_log.isDebugEnabled()) m_log.debug("matchRole: playerRolePair = " + prp.toString());
            if (m_log.isDebugEnabled()) m_log.debug("matchRole: playerRoleParam = " + playerRoleParam.toString());
            if (playerRoleParam.matches(prp)) {
                m_log.debug("Got match!");
                if (playerRoleParam.getPlayerVar() != null) {
                    if (m_log.isDebugEnabled()) {
                        m_log.debug("Binding $" + playerRoleParam.getPlayerVar().getName() + " to topic " + prp.getPlayerTopic().getObjectId());
                    }
                    localBindings.put(playerRoleParam.getPlayerVar(), prp.getPlayerTopic());
                }
                if (playerRoleParam.getRoleVar() != null) {
                    if (m_log.isDebugEnabled()) {
                        m_log.debug("Binding $" + playerRoleParam.getRoleVar().getName() + " to topic " + prp.getRoleTopic().getObjectId());
                    }
                    localBindings.put(playerRoleParam.getRoleVar(), prp.getRoleTopic());
                }
                matchRole(rolePlayers, paramIx + 1, localBindings, varSet);
            }
        }
    }

    private List extractRolePlayers(Association assoc) {
        ArrayList ret = new ArrayList();
        Iterator it = assoc.getAssociationRoles().iterator();
        while (it.hasNext()) {
            AssociationRole role = (AssociationRole) it.next();
            ret.add(new PlayerRolePair(role.getPlayer(), role.getType()));
        }
        return ret;
    }

    public String getPredicateName() {
        return "dynamic association predicate";
    }

    public String toString() {
        StringBuffer sb = new StringBuffer();
        Iterator it = getParameters().iterator();
        Object param0 = it.next();
        if (param0 instanceof Topic) {
            sb.append(((Topic) param0).getObjectId());
        } else {
            sb.append(param0.toString());
        }
        sb.append("(");
        while (it.hasNext()) {
            sb.append(it.next().toString());
            if (it.hasNext()) {
                sb.append(", ");
            }
        }
        sb.append(")");
        return sb.toString();
    }

    public int getMaxParamCount() {
        return Integer.MAX_VALUE;
    }

    public int getMinParamCount() {
        return 1;
    }

    public ParameterInfo[] getParamInfo() {
        return null;
    }

    public boolean isVariableAllowed(int ix) {
        return true;
    }

    public boolean validateParamType(int ix, Class paramType) {
        return Topic.class.isAssignableFrom(paramType) || PlayerRoleVarPair.class.isAssignableFrom(paramType);
    }

    private VariableSet createVariableSet() {
        VariableSet ret = new VariableSet();
        Iterator it = getParameters().iterator();
        while (it.hasNext()) {
            Object p = it.next();
            if (p instanceof Variable) {
                ret.addColumn((Variable) p);
            } else if (p instanceof PlayerRoleVarPair) {
                PlayerRoleVarPair prvp = (PlayerRoleVarPair) p;
                if (prvp.getPlayerVar() != null) {
                    ret.addColumn(prvp.getPlayerVar());
                }
                if (prvp.getRoleVar() != null) {
                    ret.addColumn(prvp.getRoleVar());
                }
            }
        }
        return ret;
    }

    /**
     * A simplified version of the 
     * org.tm4j.topicmap.utils.testers.TypeTester
     * 
     * Returns <code>true</code> if the topic bounded to this
     * predicate is used as type of the specified TopicMapObject.
     */
    private class TypeTester implements Predicate {

        private Topic _type;

        public TypeTester(Topic type) {
            _type = type;
        }

        /**
         * Performs the test.
         * @param o The object to be tested.
         * @return True if the object is a Topic, Association,
         *     AssociationRole, Occurrence or TopicName
         *     and if there is one or more type-specifying
         *     topic for that object which is the allowed type.
         */
        public boolean test(Object o) {
            Topic type = null;
            if (o instanceof Topic) {
                return ((Topic) o).getTypes().contains(_type);
            } else if (o instanceof Association) {
                type = ((Association) o).getType();
            } else if (o instanceof AssociationRole) {
                type = ((AssociationRole) o).getType();
            } else if (o instanceof Occurrence) {
                type = ((Occurrence) o).getType();
            } else if (o instanceof TopicName) {
                type = ((TopicName) o).getType();
            }
            return (type != null && _type.equals(type));
        }
    }

    private class AssociationExtractor implements UnaryFunction {

        public Object fn(Object in) {
            if (in instanceof Collection) {
                return extractCollection((Collection) in);
            } else {
                return _extract(in);
            }
        }

        public Object _extract(Object in) {
            if (in instanceof AssociationRole) {
                return ((AssociationRole) in).getAssociation();
            }
            return null;
        }

        private Collection extractCollection(Collection in) {
            ArrayList ret = new ArrayList();
            Iterator it = in.iterator();
            while (it.hasNext()) {
                Object item = (Object) it.next();
                Object out = _extract(item);
                if (out != null) {
                    if (out instanceof Collection) {
                        ret.addAll((Collection) out);
                    } else {
                        ret.add(out);
                    }
                }
            }
            return ret;
        }
    }
}
