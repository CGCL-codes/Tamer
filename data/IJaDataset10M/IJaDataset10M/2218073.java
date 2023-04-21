package org.wsmostudio.repository.webservice.ordi;

import java.util.*;
import java.net.URL;
import org.omwg.ontology.Ontology;
import org.wsmo.common.*;
import org.wsmo.common.exception.InvalidModelException;
import org.wsmo.common.exception.SynchronisationException;
import org.wsmo.datastore.WsmoRepository;
import org.wsmo.mediator.Mediator;
import org.wsmo.service.*;
import org.wsmo.wsml.Parser;
import org.wsmo.wsml.ParserException;
import org.wsmo.wsml.Serializer;
import org.wsmostudio.runtime.extension.Initialisable;
import org.wsmo.factory.WsmoFactory;
import org.wsmostudio.runtime.WSMORuntime;
import org.wsmostudio.runtime.LogManager;
import org.apache.axis.client.Call;
import org.apache.axis.client.Service;

public class ORDIProxyClient implements WsmoRepository, Initialisable {

    private static final String SERV_OP_VERSION = "getVersion";

    private static final String SERV_OP_ADD_GOAL = "addGoal";

    private static final String SERV_OP_ADD_MEDIATOR = "addMediator";

    private static final String SERV_OP_ADD_ONTOLOGY = "addOntology";

    private static final String SERV_OP_ADD_WS = "addWebService";

    private static final String SERV_OP_DEL_GOAL = "deleteGoal";

    private static final String SERV_OP_DEL_MEDIATOR = "deleteMediator";

    private static final String SERV_OP_DEL_ONTOLOGY = "deleteOntology";

    private static final String SERV_OP_DEL_WS = "deleteWebService";

    private static final String SERV_OP_GET_GOAL = "getGoal";

    private static final String SERV_OP_GET_MEDIATOR = "getMediator";

    private static final String SERV_OP_GET_ONTOLOGY = "getOntology";

    private static final String SERV_OP_GET_WS = "getWebService";

    private static final String SERV_OP_LIST_GOALS = "listGoals";

    private static final String SERV_OP_LIST_MEDIATORS = "listMediators";

    private static final String SERV_OP_LIST_ONTOLOGIES = "listOntologies";

    private static final String SERV_OP_LIST_WS = "listWebServices";

    private static final String SERV_OP_SAVE_GOAL = "saveGoal";

    private static final String SERV_OP_SAVE_MEDIATOR = "saveMediator";

    private static final String SERV_OP_SAVE_ONTOLOGY = "saveOntology";

    private static final String SERV_OP_SAVE_WS = "saveWebService";

    private static final String SERV_OP_NFP_GET = "getNFP";

    private static final String SERV_OP_NFP_REPLACE = "replaceNFP";

    private String serviceEndPoint, description;

    private Serializer wsmoSer;

    private Parser wsmoParser;

    private WsmoFactory wsmoFact;

    public void initialise(Map params) throws Exception {
        this.serviceEndPoint = (String) params.get(ORDIWSRepositoryConfigurator.SERVICE_END_POINT_ID);
        System.out.println(serviceEndPoint);
        this.wsmoSer = WSMORuntime.getRuntime().getWsmlSerializer();
        this.wsmoParser = WSMORuntime.getRuntime().getWsmlParser();
        this.wsmoFact = WSMORuntime.getRuntime().getWsmoFactory();
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String desc) {
        description = desc;
    }

    public String getVersion() throws SynchronisationException {
        try {
            Call call = _createCall();
            String version = (String) call.invoke(SERV_OP_VERSION, new Object[0]);
            return version;
        } catch (Exception ex) {
            throw new SynchronisationException(ex);
        }
    }

    public void addOntology(Ontology ont) throws SynchronisationException {
        _checkArgument(ont);
        try {
            Call call = _createCall();
            Object[] param = new Object[] { _serializeEntity(ont) };
            call.invoke(SERV_OP_ADD_ONTOLOGY, param);
        } catch (Exception ex) {
            throw new SynchronisationException(ex);
        }
    }

    public void saveOntology(Ontology ont) throws SynchronisationException {
        _checkArgument(ont);
        try {
            Call call = _createCall();
            Object[] param = new Object[] { _serializeEntity(ont) };
            call.invoke(SERV_OP_SAVE_ONTOLOGY, param);
        } catch (Exception ex) {
            throw new SynchronisationException(ex);
        }
    }

    public Ontology getOntology(IRI id) throws SynchronisationException {
        Ontology retOnto;
        _checkArgument(id);
        try {
            Call call = _createCall();
            Object[] param = new Object[] { id.toString() };
            String callResult = (String) call.invoke(SERV_OP_GET_ONTOLOGY, param);
            retOnto = (Ontology) _parseEntity(callResult);
        } catch (Exception ex) {
            throw new SynchronisationException(ex);
        }
        assert retOnto != null;
        return retOnto;
    }

    public void deleteOntology(IRI id) throws SynchronisationException {
        _checkArgument(id);
        try {
            Call call = _createCall();
            Object[] param = new Object[] { id.toString() };
            call.invoke(SERV_OP_DEL_ONTOLOGY, param);
        } catch (Exception ex) {
            throw new SynchronisationException(ex);
        }
    }

    public List listOntologies() throws SynchronisationException {
        String[] results;
        try {
            Call call = _createCall();
            results = (String[]) call.invoke(SERV_OP_LIST_ONTOLOGIES, new Object[0]);
        } catch (Exception ex) {
            throw new SynchronisationException(ex);
        }
        return _createIdList(results);
    }

    public void addGoal(Goal goal) throws SynchronisationException {
        _checkArgument(goal);
        try {
            Call call = _createCall();
            Object[] param = new Object[] { _serializeEntity(goal) };
            call.invoke(SERV_OP_ADD_GOAL, param);
        } catch (Exception ex) {
            throw new SynchronisationException(ex);
        }
    }

    public void saveGoal(Goal goal) throws SynchronisationException {
        _checkArgument(goal);
        try {
            Call call = _createCall();
            Object[] param = new Object[] { _serializeEntity(goal) };
            call.invoke(SERV_OP_SAVE_GOAL, param);
        } catch (Exception ex) {
            throw new SynchronisationException(ex);
        }
    }

    public Goal getGoal(IRI id) throws SynchronisationException {
        Goal retGoal;
        _checkArgument(id);
        try {
            Call call = _createCall();
            Object[] param = new Object[] { id.toString() };
            String callResult = (String) call.invoke(SERV_OP_GET_GOAL, param);
            retGoal = (Goal) _parseEntity(callResult);
        } catch (Exception ex) {
            throw new SynchronisationException(ex);
        }
        assert retGoal != null;
        return retGoal;
    }

    public void deleteGoal(IRI id) throws SynchronisationException {
        _checkArgument(id);
        try {
            Call call = _createCall();
            Object[] param = new Object[] { id.toString() };
            call.invoke(SERV_OP_DEL_GOAL, param);
        } catch (Exception ex) {
            throw new SynchronisationException(ex);
        }
    }

    public List listGoals() throws SynchronisationException {
        String[] results;
        try {
            Call call = _createCall();
            results = (String[]) call.invoke(SERV_OP_LIST_GOALS, new Object[0]);
        } catch (Exception ex) {
            throw new SynchronisationException(ex);
        }
        return _createIdList(results);
    }

    public void addMediator(Mediator med) throws SynchronisationException {
        _checkArgument(med);
        try {
            Call call = _createCall();
            Object[] param = new Object[] { _serializeEntity(med) };
            call.invoke(SERV_OP_ADD_MEDIATOR, param);
        } catch (Exception ex) {
            throw new SynchronisationException(ex);
        }
    }

    public void saveMediator(Mediator med) throws SynchronisationException {
        _checkArgument(med);
        try {
            Call call = _createCall();
            Object[] param = new Object[] { _serializeEntity(med) };
            call.invoke(SERV_OP_SAVE_MEDIATOR, param);
        } catch (Exception ex) {
            throw new SynchronisationException(ex);
        }
    }

    public Mediator getMediator(IRI id) throws SynchronisationException {
        Mediator retMed;
        _checkArgument(id);
        try {
            Call call = _createCall();
            Object[] param = new Object[] { id.toString() };
            String callResult = (String) call.invoke(SERV_OP_GET_MEDIATOR, param);
            retMed = (Mediator) _parseEntity(callResult);
        } catch (Exception ex) {
            throw new SynchronisationException(ex);
        }
        assert retMed != null;
        return retMed;
    }

    public void deleteMediator(IRI id) throws SynchronisationException {
        _checkArgument(id);
        try {
            Call call = _createCall();
            Object[] param = new Object[] { id.toString() };
            call.invoke(SERV_OP_DEL_MEDIATOR, param);
        } catch (Exception ex) {
            throw new SynchronisationException(ex);
        }
    }

    public List listMediators() throws SynchronisationException {
        String[] results;
        try {
            Call call = _createCall();
            results = (String[]) call.invoke(SERV_OP_LIST_MEDIATORS, new Object[0]);
        } catch (Exception ex) {
            throw new SynchronisationException(ex);
        }
        return _createIdList(results);
    }

    public void addWebService(WebService ws) throws SynchronisationException {
        _checkArgument(ws);
        try {
            Call call = _createCall();
            Object[] param = new Object[] { _serializeEntity(ws) };
            call.invoke(SERV_OP_ADD_WS, param);
        } catch (Exception ex) {
            throw new SynchronisationException(ex);
        }
    }

    public void saveWebService(WebService ws) throws SynchronisationException {
        _checkArgument(ws);
        try {
            Call call = _createCall();
            Object[] param = new Object[] { _serializeEntity(ws) };
            call.invoke(SERV_OP_SAVE_WS, param);
        } catch (Exception ex) {
            throw new SynchronisationException(ex);
        }
    }

    public WebService getWebService(IRI id) throws SynchronisationException {
        WebService retWS;
        _checkArgument(id);
        try {
            Call call = _createCall();
            Object[] param = new Object[] { id.toString() };
            String callResult = (String) call.invoke(SERV_OP_GET_WS, param);
            retWS = (WebService) _parseEntity(callResult);
        } catch (Exception ex) {
            throw new SynchronisationException(ex);
        }
        assert retWS != null;
        return retWS;
    }

    public void deleteWebService(IRI id) throws SynchronisationException {
        _checkArgument(id);
        try {
            Call call = _createCall();
            Object[] param = new Object[] { id.toString() };
            call.invoke(SERV_OP_DEL_WS, param);
        } catch (Exception ex) {
            throw new SynchronisationException(ex);
        }
    }

    public List listWebServices() throws SynchronisationException {
        String[] results;
        try {
            Call call = _createCall();
            results = (String[]) call.invoke(SERV_OP_LIST_WS, new Object[0]);
        } catch (Exception ex) {
            throw new SynchronisationException(ex);
        }
        return _createIdList(results);
    }

    public Set load(Identifier id) {
        throw new UnsupportedOperationException("Not supported by the server!");
    }

    public Entity load(Identifier id, Class clazz) {
        throw new UnsupportedOperationException("Not supported by the server!");
    }

    public void save(Entity item) {
        throw new UnsupportedOperationException("Not supported by the server!");
    }

    public void remove(Identifier id) {
        throw new UnsupportedOperationException("Not supported by the server!");
    }

    public void remove(Identifier id, Class clazz) {
        throw new UnsupportedOperationException("Not supported by the server!");
    }

    /**
     * Replace NFP of a TopEntity. If TopEntity does not exist Exception is thrown.
     * @param wsml serialize TopEntity, only NFP sections is parsed.
     */
    public void replaceNFP(String wsml) {
        _checkArgument(wsml);
        try {
            Call call = _createCall();
            Object[] param = new Object[] { wsml.toString() };
            call.invoke(SERV_OP_NFP_REPLACE, param);
        } catch (Exception ex) {
            throw new SynchronisationException(ex);
        }
    }

    /**
     * Gets the NFP data for a TopEntity. If the IRI does not exist Exception is thrown.
     * @param id IRI of a TopEntity stored in the repository.
     * @return serialized TopEntity with NFP section
     */
    public String getNFP(String id) {
        _checkArgument(id);
        try {
            Call call = _createCall();
            Object[] param = new Object[] { id.toString() };
            return (String) call.invoke(SERV_OP_NFP_GET, param);
        } catch (Exception ex) {
            throw new SynchronisationException(ex);
        }
    }

    private Call _createCall() throws SynchronisationException {
        try {
            Service service = new Service();
            Call call = (Call) service.createCall();
            call.setTargetEndpointAddress(new URL(serviceEndPoint));
            return call;
        } catch (Exception e) {
            throw new SynchronisationException(e);
        }
    }

    private void _checkArgument(Object param) {
        if (param == null) {
            throw new IllegalArgumentException();
        }
    }

    private String _serializeEntity(TopEntity entity) {
        StringBuffer entityBuff = new StringBuffer();
        wsmoSer.serialize(new TopEntity[] { entity }, entityBuff);
        return entityBuff.toString();
    }

    private TopEntity _parseEntity(String entity) throws ParserException, InvalidModelException {
        TopEntity[] parseRes = wsmoParser.parse(new StringBuffer(entity));
        return parseRes[0];
    }

    private List<IRI> _createIdList(String[] ids) {
        List<IRI> result = new LinkedList<IRI>();
        for (int i = 0; i < ids.length; i++) {
            try {
                IRI curID = wsmoFact.createIRI(ids[i]);
                assert curID != null;
                result.add(curID);
            } catch (Exception iriEx) {
                LogManager.logError("Invalid identifier: " + ids[i]);
            }
        }
        return result;
    }
}
