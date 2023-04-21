package org.monet.kernel.producers;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Date;
import java.util.EventObject;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map.Entry;
import org.monet.kernel.agents.AgentLogger;
import org.monet.kernel.agents.AgentNotifier;
import org.monet.kernel.constants.Common;
import org.monet.kernel.constants.Database;
import org.monet.kernel.constants.ErrorCode;
import org.monet.kernel.constants.Producers;
import org.monet.kernel.constants.Strings;
import org.monet.kernel.constants.TaskState;
import org.monet.kernel.exceptions.DataException;
import org.monet.kernel.guice.InjectorFactory;
import org.monet.kernel.library.LibrarySearch;
import org.monet.kernel.library.LibraryString;
import org.monet.kernel.library.LibraryXML;
import org.monet.kernel.model.DatabaseRepositoryQuery;
import org.monet.kernel.model.Fact;
import org.monet.kernel.model.Language;
import org.monet.kernel.model.LinkType;
import org.monet.kernel.model.MonetEvent;
import org.monet.kernel.model.Node;
import org.monet.kernel.model.Task;
import org.monet.kernel.model.TaskEnrolment;
import org.monet.kernel.model.definition.SyncLockDeclaration;
import org.monet.kernel.model.definition.TaskDefinition;
import org.monet.kernel.model.definition.TimerLockDeclaration;
import org.monet.kernel.workmap.engine.Process;
import org.monet.kernel.workmap.engine.WorkMapEngine;
import org.monet.kernel.workmap.lock.LockAction;
import org.monet.kernel.workmap.lock.SyncLock;
import org.monet.kernel.workmap.lock.TimerLock;
import org.monet.kernel.workmap.lock.Worklock;
import org.monet.kernel.workmap.lock.WorklockFactory;
import org.monet.kernel.workmap.lock.WorklockFactoryByType;
import com.google.inject.Injector;

public class ProducerTask extends Producer {

    public ProducerTask() {
        super();
    }

    public void updateDefinition(TaskDefinition taskDefinition) {
        HashMap<String, Object> parameters = new HashMap<String, Object>();
        for (Entry<String, String> label : taskDefinition.getLabelsMap().entrySet()) {
            String language = label.getKey();
            String text = label.getValue();
            parameters.put(Database.QueryFields.CODE, taskDefinition.getCode());
            parameters.put(Database.QueryFields.LANGUAGE, language);
            parameters.put(Database.QueryFields.LABEL, text);
            this.agentDatabase.executeRepositoryUpdateQuery(Database.Queries.TASK_DEFINITION_INSERT, parameters);
        }
    }

    public void cleanDefinitions() {
        this.agentDatabase.executeRepositoryUpdateQuery(Database.Queries.TASK_DEFINITION_CLEAN);
    }

    private void loadProperties(Task task) {
        ResultSet result;
        HashMap<String, Object> parameters = new HashMap<String, Object>();
        parameters.put(Database.QueryFields.ID_TASK, task.getId());
        result = this.agentDatabase.executeRepositorySelectQuery(Database.Queries.TASK_LOAD, parameters);
        try {
            if (!result.next()) throw new Exception(String.format("Task '%s' not found", task.getId()));
            task.setId(result.getString("id"));
            task.setTargetId(result.getString("id_target"));
            task.setInputId(result.getString("id_input"));
            task.setOutputId(result.getString("id_output"));
            task.setType(result.getString("type"));
            if (result.getString("label") != null) task.setLabel(result.getString("label"));
            if (result.getString("description") != null) task.setDescription(result.getString("description"));
            if (result.getString("sender") != null) task.setSender(result.getString("sender"));
            task.setCreateDate(result.getTimestamp("create_date"));
            task.setExpireDate(result.getTimestamp("expire_date"));
            task.setFinishDate(result.getTimestamp("finish_date"));
            task.setUpdateDate(result.getTimestamp("update_date"));
            task.setAborted((result.getInt("aborted") == 1));
            task.setOrdering(result.getInt("ordering"));
            task.setPendingForAction(result.getString("main_lock_type"));
        } catch (Exception exception) {
            throw new DataException(ErrorCode.LOAD_TASK, Strings.ALL, exception);
        } finally {
            this.agentDatabase.closeQuery(result);
        }
    }

    private void loadTarget(Task task) {
        ProducerNode producerNode = (ProducerNode) this.oProducersFactory.get(Producers.NODE);
        String idTarget = task.getTargetId();
        if ((idTarget == null) || (idTarget.equals(Strings.UNDEFINED_ID))) return;
        task.setTarget(producerNode.load(idTarget));
    }

    private void loadInput(Task task) {
        ProducerNode producerNode = (ProducerNode) this.oProducersFactory.get(Producers.NODE);
        String idInput = task.getInputId();
        if ((idInput == null) || (idInput.equals(Strings.UNDEFINED_ID))) return;
        task.setInput(producerNode.load(task.getInputId()));
    }

    private void loadOutput(Task task) {
        ProducerNode producerNode = (ProducerNode) this.oProducersFactory.get(Producers.NODE);
        String idOutput = task.getOutputId();
        if ((idOutput == null) || (idOutput.equals(Strings.UNDEFINED_ID))) return;
        task.setOutput(producerNode.load(task.getOutputId()));
    }

    private void loadEnrolments(Task task) {
        ResultSet result;
        HashMap<String, Object> parameters = new HashMap<String, Object>();
        parameters.put(Database.QueryFields.ID_TASK, task.getId());
        result = this.agentDatabase.executeRepositorySelectQuery(Database.Queries.TASK_ENROLMENTS_LOAD, parameters);
        try {
            while (result.next()) {
                String idUser = result.getString("id_user");
                task.addEnrolment(new TaskEnrolment(idUser, result.getTimestamp("create_date")));
            }
        } catch (Exception exception) {
            throw new DataException(ErrorCode.LOAD_TASK, Strings.ALL, exception);
        } finally {
            this.agentDatabase.closeQuery(result);
        }
    }

    private void loadData(Task oTask) {
        ResultSet oResult;
        HashMap<String, Object> hmParameters = new HashMap<String, Object>();
        StringBuffer oData = new StringBuffer();
        String sData;
        hmParameters.put(Database.QueryFields.ID, oTask.getId());
        oResult = this.agentDatabase.executeRepositorySelectQuery(Database.Queries.TASK_LOAD_DATA, hmParameters);
        try {
            if (!oResult.next()) throw new Exception(String.format("Task '%s' not found", oTask.getId()));
            sData = oResult.getString("data");
            if (sData == null) sData = Strings.EMPTY;
            sData = LibraryXML.clearHeader(sData);
            while ((sData.length() > 0) && (!sData.substring(sData.length() - 1).equals(">"))) sData = sData.substring(0, sData.length() - 1);
            oData.append(LibraryString.cleanSpecialChars(sData));
        } catch (Exception oException) {
            throw new DataException(ErrorCode.LOAD_TASK, oTask.getId(), oException);
        } finally {
            this.agentDatabase.closeQuery(oResult);
        }
        oTask.setData(oData);
    }

    private void saveProperties(Task task) {
        String data;
        HashMap<String, Object> parameters = new HashMap<String, Object>();
        data = task.serializeToXML().toString();
        parameters.put(Database.QueryFields.ID, task.getId());
        if (task.getTargetId() != null) parameters.put(Database.QueryFields.ID_TARGET, task.getTargetId()); else parameters.put(Database.QueryFields.ID_TARGET, Strings.UNDEFINED_ID);
        if (task.getInputId() != null) parameters.put(Database.QueryFields.ID_INPUT, task.getInputId()); else parameters.put(Database.QueryFields.ID_INPUT, Strings.UNDEFINED_ID);
        if (task.getOutputId() != null) parameters.put(Database.QueryFields.ID_OUTPUT, task.getOutputId()); else parameters.put(Database.QueryFields.ID_OUTPUT, Strings.UNDEFINED_ID);
        parameters.put(Database.QueryFields.TYPE, task.getType());
        parameters.put(Database.QueryFields.LABEL, task.getLabel());
        parameters.put(Database.QueryFields.DESCRIPTION, task.getDescription());
        parameters.put(Database.QueryFields.STATE, task.getInternalState());
        parameters.put(Database.QueryFields.SENDER, task.getSender());
        parameters.put(Database.QueryFields.EXPIRE_DATE, this.agentDatabase.getDateAsTimestamp(task.getInternalExpireDate()));
        parameters.put(Database.QueryFields.UPDATE_DATE, this.agentDatabase.getDateAsTimestamp(new Date()));
        parameters.put(Database.QueryFields.DATA, data);
        parameters.put(Database.QueryFields.ABORTED, task.isAborted());
        parameters.put(Database.QueryFields.ORDERING, task.getOrdering());
        parameters.put(Database.QueryFields.MAIN_LOCK_TYPE, task.getMainLockType());
        this.agentDatabase.executeRepositoryUpdateQuery(Database.Queries.TASK_SAVE, parameters);
        this.saveWords(task.getId(), Common.WordsSections.LABEL, task.getLabel());
        this.saveWords(task.getId(), Common.WordsSections.DESCRIPTION, task.getDescription());
        this.saveWords(task.getId(), Common.WordsSections.CONTENT, data);
    }

    private void saveLinks(Task oTask) {
        HashMap<String, Object> parameters = new HashMap<String, Object>();
        LinkedHashSet<DatabaseRepositoryQuery> queries = new LinkedHashSet<DatabaseRepositoryQuery>();
        parameters.put(Database.QueryFields.ID_SOURCE, oTask.getId());
        parameters.put(Database.QueryFields.TYPE_SOURCE, LinkType.TASK);
        queries.add(new DatabaseRepositoryQuery(Database.Queries.LINK_DELETE, parameters));
        if ((oTask.getTargetId() != null) && (!oTask.getTargetId().equals(Strings.UNDEFINED_ID))) {
            HashMap<String, Object> queryParameters = new HashMap<String, Object>();
            queryParameters.put(Database.QueryFields.ID_SOURCE, oTask.getId());
            queryParameters.put(Database.QueryFields.TYPE_SOURCE, LinkType.TASK);
            queryParameters.put(Database.QueryFields.ID_TARGET, oTask.getTargetId());
            queryParameters.put(Database.QueryFields.TYPE_TARGET, LinkType.NODE);
            queryParameters.put(Database.QueryFields.DATA, LinkType.TASK);
            queryParameters.put(Database.QueryFields.DELETE_DATE, null);
            queries.add(new DatabaseRepositoryQuery(Database.Queries.LINK_ADD, queryParameters));
        }
        this.agentDatabase.executeRepositoryUpdateTransaction(queries.toArray(new DatabaseRepositoryQuery[0]));
    }

    private void saveEnrolments(Task task) {
        HashMap<String, Object> parameters = new HashMap<String, Object>();
        HashMap<String, TaskEnrolment> enrolments = task.getEnrolments();
        Iterator<String> iter = enrolments.keySet().iterator();
        LinkedHashSet<DatabaseRepositoryQuery> queries = new LinkedHashSet<DatabaseRepositoryQuery>();
        parameters.put(Database.QueryFields.ID_TASK, task.getId());
        queries.add(new DatabaseRepositoryQuery(Database.Queries.TASK_ENROLMENTS_DELETE_ALL, parameters));
        while (iter.hasNext()) {
            String idUser = iter.next();
            TaskEnrolment enrolment = enrolments.get(idUser);
            HashMap<String, Object> queryParameters = new HashMap<String, Object>();
            queryParameters.put(Database.QueryFields.ID_TASK, task.getId());
            queryParameters.put(Database.QueryFields.ID_USER, idUser);
            queryParameters.put(Database.QueryFields.CREATE_DATE, this.agentDatabase.getDateAsTimestamp(enrolment.getInternalCreateDate()));
            queries.add(new DatabaseRepositoryQuery(Database.Queries.TASK_ENROLMENTS_ADD, queryParameters));
        }
        this.agentDatabase.executeRepositoryUpdateTransaction(queries.toArray(new DatabaseRepositoryQuery[0]));
    }

    private void saveWords(String id, String sSection, String sData) {
        HashSet<String> hsWords = LibrarySearch.analyzeContent(sData);
        Iterator<String> oIterator = hsWords.iterator();
        HashMap<String, Object> hmParameters = new HashMap<String, Object>();
        ResultSet oResultLoadWord = null;
        String idWord;
        DatabaseRepositoryQuery[] aQueries = new DatabaseRepositoryQuery[hsWords.size()];
        Integer iPos = 0;
        hmParameters.put(Database.QueryFields.ID_TASK, id);
        hmParameters.put(Database.QueryFields.SECTION, sSection);
        this.agentDatabase.executeRepositoryUpdateQuery(Database.Queries.WORDS_TASKS_REMOVE, hmParameters);
        while (oIterator.hasNext()) {
            String sWord = oIterator.next();
            try {
                hmParameters.clear();
                hmParameters.put(Database.QueryFields.WORD, sWord);
                oResultLoadWord = this.agentDatabase.executeRepositorySelectQuery(Database.Queries.WORDS_LOAD_WORD, hmParameters);
                if (!oResultLoadWord.next()) {
                    this.agentDatabase.closeQuery(oResultLoadWord);
                    hmParameters.clear();
                    hmParameters.put(Database.QueryFields.WORD, sWord);
                    idWord = this.agentDatabase.executeRepositoryUpdateQueryAndGetGeneratedKey(Database.Queries.WORDS_CREATE, hmParameters);
                } else {
                    idWord = oResultLoadWord.getString("id");
                }
                HashMap<String, Object> hmQueryParameters = new HashMap<String, Object>();
                hmQueryParameters.put(Database.QueryFields.ID_TASK, id);
                hmQueryParameters.put(Database.QueryFields.ID_WORD, idWord);
                hmQueryParameters.put(Database.QueryFields.SECTION, sSection);
                aQueries[iPos] = new DatabaseRepositoryQuery(Database.Queries.WORDS_TASKS_CREATE, hmQueryParameters);
                iPos++;
            } catch (Exception oException) {
                throw new DataException(ErrorCode.LOAD_WORDS, id, oException);
            } finally {
                if (oResultLoadWord != null) this.agentDatabase.closeQuery(oResultLoadWord);
                oResultLoadWord = null;
            }
        }
        this.agentDatabase.executeRepositoryUpdateTransaction(aQueries);
    }

    private void loadProcess(Task task) {
        Injector injector = InjectorFactory.getInstance();
        WorkMapEngine workMapEngine;
        Process process;
        workMapEngine = injector.getInstance(WorkMapEngine.class);
        if (workMapEngine.isProcessActive(task.getId())) {
            task.setProcess(workMapEngine.getProcess(task.getId()));
        } else {
            process = injector.getInstance(Process.class);
            process.setId(task.getId());
            process.setWorkMap(task.getDefinition().getWorkmapDeclaration());
            process.setAgentNotifier(AgentNotifier.getInstance());
            task.setProcess(process);
            task.deserializeFromXML(task.getData().toString());
            workMapEngine.cacheProcess(process);
            workMapEngine.resumeProcess(process.getId());
        }
    }

    public void rollbackTask(Task task) {
        task.setProcess(null);
        task.removeLoadedAttribute(Task.PROCESS);
        Injector injector = InjectorFactory.getInstance();
        WorkMapEngine workMapEngine;
        workMapEngine = injector.getInstance(WorkMapEngine.class);
        if (workMapEngine.isProcessActive(task.getId())) {
            workMapEngine.destroyProcess(task.getId());
        }
    }

    public Task load(String id) {
        Task task;
        task = new Task();
        task.setId(id);
        task.linkLoadListener(this);
        task.enablePartialLoading();
        return task;
    }

    public void save(Task task) {
        this.saveProperties(task);
        this.saveLinks(task);
        this.saveEnrolments(task);
        if (task.getProcess().getState().equals(TaskState.FINISHED)) {
            HashMap<String, Object> parameters = new HashMap<String, Object>();
            parameters.put(Database.QueryFields.ID, task.getId());
            parameters.put(Database.QueryFields.DATE, this.agentDatabase.getDateAsTimestamp(new Date()));
            parameters.put(Database.QueryFields.UPDATE_DATE, this.agentDatabase.getDateAsTimestamp(new Date()));
            parameters.put(Database.QueryFields.ABORTED, 0);
            this.agentDatabase.executeRepositoryUpdateQuery(Database.Queries.TASK_SAVE_FINISH, parameters);
            this.oAgentNotifier.notify(new MonetEvent(MonetEvent.TASK_FINISHED, null, task));
        }
        if (task.getProcess().getState().equals(TaskState.ABORTED)) {
            HashMap<String, Object> parameters = new HashMap<String, Object>();
            parameters.put(Database.QueryFields.ID, task.getId());
            parameters.put(Database.QueryFields.DATE, this.agentDatabase.getDateAsTimestamp(new Date()));
            parameters.put(Database.QueryFields.UPDATE_DATE, this.agentDatabase.getDateAsTimestamp(new Date()));
            parameters.put(Database.QueryFields.ABORTED, 1);
            this.agentDatabase.executeRepositoryUpdateQuery(Database.Queries.TASK_SAVE_FINISH, parameters);
            this.oAgentNotifier.notify(new MonetEvent(MonetEvent.TASK_FINISHED, null, task));
        }
    }

    public void saveProcess(Task task) {
        HashMap<String, Object> parameters = new HashMap<String, Object>();
        String data;
        data = task.serializeToXML().toString();
        parameters.put(Database.QueryFields.ID, task.getId());
        parameters.put(Database.QueryFields.DATA, data);
        parameters.put(Database.QueryFields.MAIN_LOCK_TYPE, task.getMainLockType());
        parameters.put(Database.QueryFields.UPDATE_DATE, this.agentDatabase.getDateAsTimestamp(new Date()));
        this.agentDatabase.executeRepositoryUpdateQuery(Database.Queries.TASK_SAVE_PROCESS, parameters);
    }

    public void priorize(Task task) {
        HashMap<String, Object> parameters = new HashMap<String, Object>();
        ResultSet result;
        Long maxOrdering;
        result = this.agentDatabase.executeRepositorySelectQuery(Database.Queries.TASK_SELECT_MAX_ORDERING);
        try {
            maxOrdering = result.getLong("ordering");
        } catch (Exception oException) {
            throw new DataException(ErrorCode.CREATE_TASK, ErrorCode.GENERATE_ID, oException);
        } finally {
            this.agentDatabase.closeQuery(result);
        }
        parameters.put(Database.QueryFields.ID, task.getId());
        parameters.put(Database.QueryFields.ORDERING, maxOrdering + 1);
        parameters.put(Database.QueryFields.UPDATE_DATE, this.agentDatabase.getDateAsTimestamp(new Date()));
        this.agentDatabase.executeRepositoryUpdateQuery(Database.Queries.TASK_SAVE_ORDERING, parameters);
    }

    private void updateDate(Task task) {
        HashMap<String, Object> parameters = new HashMap<String, Object>();
        parameters.put(Database.QueryFields.ID, task.getId());
        parameters.put(Database.QueryFields.UPDATE_DATE, this.agentDatabase.getDateAsTimestamp(new Date()));
        this.agentDatabase.executeRepositoryUpdateQuery(Database.Queries.TASK_UPDATE, parameters);
    }

    public Boolean existsSuggestion(Task task, String target) {
        HashMap<String, Object> parameters = new HashMap<String, Object>();
        ResultSet resultSet;
        parameters.put(Database.QueryFields.ID_TASK, task.getId());
        parameters.put(Database.QueryFields.TARGET, target);
        resultSet = this.agentDatabase.executeRepositorySelectQuery(Database.Queries.TASK_SUGGESTIONS_EXISTS, parameters);
        try {
            return resultSet.next();
        } catch (Exception oException) {
            AgentLogger.getInstance().error(oException);
            return false;
        } finally {
            this.agentDatabase.closeQuery(resultSet);
        }
    }

    public void setSuggestionRead(Task task, String target, Boolean value, String notes) {
        HashMap<String, Object> hmParameters = new HashMap<String, Object>();
        hmParameters.put(Database.QueryFields.ID_TASK, task.getId());
        hmParameters.put(Database.QueryFields.TARGET, target);
        hmParameters.put(Database.QueryFields.READ, value ? 1 : 0);
        hmParameters.put(Database.QueryFields.NOTES, notes);
        this.agentDatabase.executeRepositoryUpdateQuery(Database.Queries.TASK_SUGGESTIONS_SAVE, hmParameters);
    }

    public void addSuggestion(Task task, String target, String notes) {
        HashMap<String, Object> parameters = new HashMap<String, Object>();
        if (this.existsSuggestion(task, target)) {
            this.setSuggestionRead(task, target, false, notes);
            return;
        }
        parameters.put(Database.QueryFields.ID_TASK, task.getId());
        parameters.put(Database.QueryFields.TARGET, target);
        parameters.put(Database.QueryFields.READ, 0);
        parameters.put(Database.QueryFields.NOTES, notes.replace("'", "''"));
        parameters.put(Database.QueryFields.CREATE_DATE, this.agentDatabase.getDateAsTimestamp(new Date()));
        this.agentDatabase.executeRepositoryUpdateQuery(Database.Queries.TASK_SUGGESTIONS_ADD, parameters);
    }

    public void deleteSuggestion(Task task, String target) {
        HashMap<String, Object> hmParameters = new HashMap<String, Object>();
        hmParameters.put(Database.QueryFields.ID_TASK, task.getId());
        hmParameters.put(Database.QueryFields.TARGET, target);
        this.agentDatabase.executeRepositoryUpdateQuery(Database.Queries.TASK_SUGGESTIONS_DELETE, hmParameters);
    }

    public Task create(String code, Node input) {
        Task task = new Task();
        task.setType(code);
        return this.create(task, input);
    }

    public Task create(Task task, Node input) {
        HashMap<String, Object> parameters = new HashMap<String, Object>();
        Injector injector = InjectorFactory.getInstance();
        WorkMapEngine workMapEngine;
        Process process;
        TaskDefinition definition = this.getDictionary().getTaskDefinition(task.getType());
        task.setCreateDate(new Date());
        task.setLabel((input != null) ? input.getLabel() : Language.getInstance().getLabel(Common.Labels.NO_LABEL, Language.getCurrent()));
        task.setDescription(definition.getLabel());
        parameters.put(Database.QueryFields.ID_TARGET, Strings.UNDEFINED_ID);
        parameters.put(Database.QueryFields.ID_INPUT, (input != null) ? input.getId() : null);
        parameters.put(Database.QueryFields.ID_OUTPUT, null);
        parameters.put(Database.QueryFields.TYPE, task.getType());
        parameters.put(Database.QueryFields.LABEL, task.getLabel());
        parameters.put(Database.QueryFields.DESCRIPTION, task.getDescription());
        parameters.put(Database.QueryFields.SENDER, Strings.EMPTY);
        parameters.put(Database.QueryFields.CREATE_DATE, this.agentDatabase.getDateAsTimestamp(task.getInternalCreateDate()));
        parameters.put(Database.QueryFields.EXPIRE_DATE, this.agentDatabase.getDateAsTimestamp(task.getInternalExpireDate()));
        parameters.put(Database.QueryFields.UPDATE_DATE, this.agentDatabase.getDateAsTimestamp(new Date()));
        parameters.put(Database.QueryFields.DATA, task.serializeToXML().toString());
        parameters.put(Database.QueryFields.ABORTED, 0);
        parameters.put(Database.QueryFields.ORDERING, 0);
        parameters.put(Database.QueryFields.MAIN_LOCK_TYPE, Worklock.TYPE_NONE);
        String id = this.agentDatabase.executeRepositoryUpdateQueryAndGetGeneratedKey(Database.Queries.TASK_CREATE, parameters);
        task.setId(id);
        this.saveEnrolments(task);
        workMapEngine = injector.getInstance(WorkMapEngine.class);
        process = workMapEngine.buildProcess(task.getId(), definition.getWorkmapDeclaration(), AgentNotifier.getInstance());
        task.setProcess(process);
        this.oAgentNotifier.notify(new MonetEvent(MonetEvent.TASK_CREATED, null, task));
        task.linkLoadListener(this);
        this.saveWords(task.getId(), Common.WordsSections.LABEL, task.getLabel());
        this.saveWords(task.getId(), Common.WordsSections.DESCRIPTION, task.getDescription());
        this.saveWords(task.getId(), Common.WordsSections.CONTENT, task.serializeToXML().toString());
        return task;
    }

    public void remove(String id) {
        HashMap<String, Object> parameters = new HashMap<String, Object>();
        parameters.put(Database.QueryFields.ID, id);
        parameters.put(Database.QueryFields.DATE, this.agentDatabase.getDateAsTimestamp(new Date()));
        parameters.put(Database.QueryFields.UPDATE_DATE, this.agentDatabase.getDateAsTimestamp(new Date()));
        this.agentDatabase.executeRepositoryUpdateQuery(Database.Queries.TASK_SAVE_FINISH, parameters);
        this.oAgentNotifier.notify(new MonetEvent(MonetEvent.TASK_MOVED_TO_TRASH, null, id));
    }

    public void recoverFromTrash(String id) {
        HashMap<String, Object> hmParameters = new HashMap<String, Object>();
        hmParameters.put(Database.QueryFields.ID, id);
        hmParameters.put(Database.QueryFields.DATE, this.agentDatabase.getDateAsTimestamp(new Date()));
        hmParameters.put(Database.QueryFields.UPDATE_DATE, this.agentDatabase.getDateAsTimestamp(new Date()));
        this.agentDatabase.executeRepositoryUpdateQuery(Database.Queries.TASK_SAVE_FINISH, hmParameters);
        this.oAgentNotifier.notify(new MonetEvent(MonetEvent.TASK_RECOVERED_FROM_TRASH, null, id));
    }

    public void addFacts(Task task, Iterable<Fact> facts) {
        ProducerFactTask producerFactBookTask = (ProducerFactTask) this.oProducersFactory.get(Producers.FACTBOOKTASK);
        producerFactBookTask.addEntries(facts);
    }

    public boolean existsEnrolment(Task task, String idUser) {
        HashMap<String, Object> parameters = new HashMap<String, Object>();
        ResultSet resultSet;
        parameters.put(Database.QueryFields.ID_TASK, task.getId());
        parameters.put(Database.QueryFields.ID_USER, idUser);
        resultSet = this.agentDatabase.executeRepositorySelectQuery(Database.Queries.TASK_ENROLMENTS_EXISTS, parameters);
        try {
            return resultSet.next();
        } catch (Exception oException) {
            AgentLogger.getInstance().error(oException);
            return false;
        } finally {
            this.agentDatabase.closeQuery(resultSet);
        }
    }

    public void addEnrolment(Task task, String idUser) {
        HashMap<String, Object> parameters = new HashMap<String, Object>();
        if (this.existsEnrolment(task, idUser)) return;
        parameters.put(Database.QueryFields.ID_TASK, task.getId());
        parameters.put(Database.QueryFields.ID_USER, idUser);
        parameters.put(Database.QueryFields.CREATE_DATE, this.agentDatabase.getDateAsTimestamp(new Date()));
        this.agentDatabase.executeRepositoryUpdateQuery(Database.Queries.TASK_ENROLMENTS_ADD, parameters);
        this.updateDate(task);
    }

    public void deleteEnrolment(Task task, String idUser) {
        HashMap<String, Object> hmParameters = new HashMap<String, Object>();
        if (!this.existsEnrolment(task, idUser)) return;
        hmParameters.put(Database.QueryFields.ID_TASK, task.getId());
        hmParameters.put(Database.QueryFields.ID_USER, idUser);
        this.agentDatabase.executeRepositoryUpdateQuery(Database.Queries.TASK_ENROLMENTS_DELETE, hmParameters);
        this.updateDate(task);
    }

    public HashMap<String, TimerLock> loadTimerLocks() {
        ResultSet result;
        HashMap<String, Object> parameters = new HashMap<String, Object>();
        Injector injector = InjectorFactory.getInstance();
        WorklockFactory lockFactory = injector.getInstance(WorklockFactory.class);
        HashMap<String, TimerLock> locks = new HashMap<String, TimerLock>();
        parameters.put(Database.QueryFields.TYPE, Worklock.TYPE_TIMER);
        result = this.agentDatabase.executeRepositorySelectQuery(Database.Queries.TASK_LOCKS_LOAD_OF_TYPE, parameters);
        try {
            while (result.next()) {
                TimerLock lock = (TimerLock) lockFactory.create(TimerLockDeclaration.class);
                lock.setId(result.getString("id"));
                lock.setProcessId(result.getString("id_task"));
                lock.setIsMain((result.getInt("main") == 1) ? true : false);
                lock.setInternalCreateDate(result.getTimestamp("create_date"));
                lock.setXmlContent(result.getString("data"));
                locks.put(lock.getId(), lock);
            }
        } catch (Exception exception) {
            throw new DataException(ErrorCode.LOAD_TASK, Strings.ALL, exception);
        } finally {
            this.agentDatabase.closeQuery(result);
        }
        return locks;
    }

    public List<SyncLock> loadSyncLocks() {
        ResultSet result;
        HashMap<String, Object> parameters = new HashMap<String, Object>();
        Injector injector = InjectorFactory.getInstance();
        WorklockFactory lockFactory = injector.getInstance(WorklockFactory.class);
        ArrayList<SyncLock> locks = new ArrayList<SyncLock>();
        parameters.put(Database.QueryFields.TYPE, Worklock.TYPE_SYNC);
        result = this.agentDatabase.executeRepositorySelectQuery(Database.Queries.TASK_LOCKS_LOAD_OF_TYPE, parameters);
        try {
            while (result.next()) {
                SyncLock lock = (SyncLock) lockFactory.create(SyncLockDeclaration.class);
                lock.setId(result.getString("id"));
                lock.setProcessId(result.getString("id_task"));
                lock.setIsMain((result.getInt("main") == 1) ? true : false);
                lock.setInternalCreateDate(result.getTimestamp("create_date"));
                lock.setXmlContent(result.getString("data"));
                locks.add(lock);
            }
        } catch (Exception exception) {
            throw new DataException(ErrorCode.LOAD_TASK, Strings.ALL, exception);
        } finally {
            this.agentDatabase.closeQuery(result);
        }
        return locks;
    }

    public boolean existsOtherTasksToWait(String code, String targetId) {
        ResultSet resultSet;
        HashMap<String, Object> parameters = new HashMap<String, Object>();
        parameters.put(Database.QueryFields.CODE, code);
        parameters.put(Database.QueryFields.ID_TARGET, targetId);
        resultSet = this.agentDatabase.executeRepositorySelectQuery(Database.Queries.TASK_TARGET_OF_LOCK_COUNT, parameters);
        try {
            int result = 0;
            if (resultSet.next()) result = resultSet.getInt(1);
            return result > 0;
        } catch (Exception exception) {
            throw new DataException(ErrorCode.LOAD_TASK, Strings.ALL, exception);
        } finally {
            this.agentDatabase.closeQuery(resultSet);
        }
    }

    public List<LockAction> loadTaskToWait(String code, String targetId) {
        ResultSet resultSet;
        HashMap<String, Object> parameters = new HashMap<String, Object>();
        parameters.put(Database.QueryFields.CODE, code);
        parameters.put(Database.QueryFields.ID_TARGET, targetId);
        resultSet = this.agentDatabase.executeRepositorySelectQuery(Database.Queries.TASK_TARGET_OF_LOCK, parameters);
        try {
            ArrayList<LockAction> lockActions = new ArrayList<LockAction>();
            while (resultSet.next()) {
                LockAction lockAction = new LockAction();
                lockAction.setParameters(resultSet.getString("id"));
                lockAction.setLabel(resultSet.getString("label"));
                lockActions.add(lockAction);
            }
            return lockActions;
        } catch (Exception exception) {
            throw new DataException(ErrorCode.LOAD_TASK, Strings.ALL, exception);
        } finally {
            this.agentDatabase.closeQuery(resultSet);
        }
    }

    public HashMap<String, Worklock<?>> loadLocks(Task task) {
        ResultSet result;
        HashMap<String, Object> parameters = new HashMap<String, Object>();
        Injector injector = InjectorFactory.getInstance();
        WorklockFactoryByType lockFactory = injector.getInstance(WorklockFactoryByType.class);
        HashMap<String, Worklock<?>> locks = new HashMap<String, Worklock<?>>();
        parameters.put(Database.QueryFields.ID_TASK, task.getId());
        result = this.agentDatabase.executeRepositorySelectQuery(Database.Queries.TASK_LOCKS_LOAD, parameters);
        try {
            while (result.next()) {
                Worklock<?> lock = lockFactory.create(result.getString("type"));
                lock.setId(result.getString("id"));
                lock.setProcessId(task.getId());
                lock.setIsMain((result.getInt("main") == 1) ? true : false);
                lock.setInternalCreateDate(result.getTimestamp("create_date"));
                lock.setXmlContent(result.getString("data"));
                locks.put(lock.getId(), lock);
            }
        } catch (Exception exception) {
            throw new DataException(ErrorCode.LOAD_TASK, Strings.ALL, exception);
        } finally {
            this.agentDatabase.closeQuery(result);
        }
        return locks;
    }

    public void addLock(Task task, Worklock<?> lock) {
        HashMap<String, Object> parameters = new HashMap<String, Object>();
        Date date = lock.getInternalCreateDate();
        if (date == null) date = new Date();
        parameters.put(Database.QueryFields.ID_TASK, task.getId());
        parameters.put(Database.QueryFields.TYPE, lock.getType());
        parameters.put(Database.QueryFields.MAIN, lock.isMain() ? 1 : 0);
        parameters.put(Database.QueryFields.DATA, lock.serialize());
        parameters.put(Database.QueryFields.CREATE_DATE, this.agentDatabase.getDateAsTimestamp(date));
        String id = this.agentDatabase.executeRepositoryUpdateQueryAndGetGeneratedKey(Database.Queries.TASK_LOCKS_ADD, parameters);
        lock.setId(id);
    }

    public void saveLock(Worklock<?> lock) {
        HashMap<String, Object> parameters = new HashMap<String, Object>();
        parameters.put(Database.QueryFields.ID, lock.getId());
        parameters.put(Database.QueryFields.DATA, lock.serialize());
        this.agentDatabase.executeRepositoryUpdateQuery(Database.Queries.TASK_LOCKS_SAVE, parameters);
    }

    public void deleteLock(Task task, String idLock) {
        HashMap<String, Object> hmParameters = new HashMap<String, Object>();
        hmParameters.put(Database.QueryFields.ID, idLock);
        hmParameters.put(Database.QueryFields.ID_TASK, task.getId());
        this.agentDatabase.executeRepositoryUpdateQuery(Database.Queries.TASK_LOCKS_DELETE, hmParameters);
    }

    public void run(Task task) {
        Injector injector = InjectorFactory.getInstance();
        WorkMapEngine workMapEngine;
        workMapEngine = injector.getInstance(WorkMapEngine.class);
        workMapEngine.resumeProcess(task.getId());
    }

    public Object newObject() {
        return new Task();
    }

    public void loadAttribute(EventObject oEventObject, String sAttribute) {
        Task task = (Task) oEventObject.getSource();
        if (sAttribute.equals(Task.PROPERTIES)) {
            this.loadProperties(task);
        } else if (sAttribute.equals(Task.ENROLMENTS)) {
            this.loadEnrolments(task);
        } else if (sAttribute.equals(Task.PROCESS)) {
            this.loadProcess(task);
        } else if (sAttribute.equals(Task.DATA)) {
            this.loadData(task);
        } else if (sAttribute.equals(Task.TARGET)) {
            this.loadTarget(task);
        } else if (sAttribute.equals(Task.INPUT)) {
            this.loadInput(task);
        } else if (sAttribute.equals(Task.OUTPUT)) {
            this.loadOutput(task);
        }
    }
}
