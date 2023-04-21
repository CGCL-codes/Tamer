package dendrarium.portal.answer;

import dendrarium.trees.AnswerType;
import dendrarium.trees.disamb.Variant;
import dendrarium.core.entities.Answer;
import dendrarium.core.entities.User;
import dendrarium.core.entities.TaskBusinessProcess;
import dendrarium.core.entities.TaskBusinessProcessState;
import dendrarium.trees.Node;
import dendrarium.trees.NonterminalNode;
import dendrarium.trees.disamb.CollisionDisambiguator;
import dendrarium.trees.xml.ForestExportFormat;
import dendrarium.trees.xml.TreeXMLExporter;
import dendrarium.trees.disamb.Disambiguator;
import dendrarium.trees.disamb.NKJPDisambiguator;
import dendrarium.trees.xml.TreeXMLParser;
import dendrarium.utils.StringUtils;
import java.io.Serializable;
import java.sql.BatchUpdateException;
import java.sql.Date;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;
import org.hibernate.Session;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Begin;
import org.jboss.seam.annotations.End;
import org.jboss.seam.annotations.Factory;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Out;
import org.jboss.seam.annotations.RaiseEvent;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.annotations.datamodel.DataModel;
import org.jboss.seam.annotations.web.RequestParameter;
import org.jboss.seam.core.Conversation;
import org.jboss.seam.log.Log;
import org.jboss.seam.security.Identity;

/**
 * PROZA
 * 
 * @author ks
 */
@Name("workOnAnswer")
@Scope(ScopeType.CONVERSATION)
public class WorkOnAnswer implements Serializable {

    @In
    Session session;

    @In
    private Conversation conversation;

    @Logger
    Log log;

    @In
    User user;

    @In
    Identity identity;

    @In(create = true)
    TreeRenderer treeRenderer;

    @RequestParameter
    Long answerId;

    @RequestParameter
    Long colMode;

    @RequestParameter
    Long roMode;

    @Out
    Answer answer;

    @Out(required = false)
    Answer originalAnswer;

    @DataModel
    List<Variant> variants;

    @DataModel
    List<NonterminalNode> accessibleNodes;

    static final int MAX_VARIANTS_WITHOUT_FILTERING = 10;

    private Disambiguator disambiguator;

    private String leftParagraphContext, rightParagraphContext;

    private String leftSentenceContext, rightSentenceContext;

    private String subsentenceContext;

    private boolean collisionMode;

    private boolean readOnlyMode;

    private boolean showsSuggested;

    private boolean superHasClicked;

    private int numUnfilteredVariants;

    enum SelectionState {

        SELECT_FILTER, SELECT_FINAL
    }

    SelectionState selectionState;

    private int roCause;

    /**
     * rozstrzyga, czy jesteśmy dendrologiem rozwiązującym zadanie, czy superem
     * rozstrzygającym kolizję, i wywołuje adekwatną metodę inicjującą
     */
    @Begin(join = true)
    public void select() {
        collisionMode = (colMode != null && colMode == 1);
        readOnlyMode = (roMode != null && roMode == 1);
        if (!readOnlyMode) {
            log.info("HISTORIA: wybrano do edycji odpowiedź " + answerId);
        }
        if (!collisionMode) {
            originalAnswer = (Answer) session.get(Answer.class, answerId);
            Answer ans = originalAnswer;
            if (TaskBusinessProcessState.DENDROLOG_RO_STATES.contains(ans.getTaskBusinessProcess().getState())) {
                readOnlyMode = true;
            }
            doSelect(ans);
        } else {
            doCollisionSelect((TaskBusinessProcess) session.get(TaskBusinessProcess.class, answerId));
        }
        roCause = computeReadOnlyCause();
    }

    /**
     * akceptacja odpowiedzi
     *
     * @return false oznacza, że spowodowaliśmy kolizję i musimy ją natychmiast
     * potwierdzić (potrzebne do nawigacji)
     */
    @RaiseEvent("tbpchangestate")
    public boolean accept() {
        TaskBusinessProcess tbp = answer.getTaskBusinessProcess();
        if (answer.isAccepted()) {
            log.info("HISTORIA: Nieudana próba ponownej akceptacji odpowiedzi " + answer.getId());
            log.info(answer.info());
            log.info(tbp.info());
            return true;
        }
        session.refresh(tbp);
        if (!user.getUsername().equals(tbp.getUser1Answer().getUser().getUsername())) session.refresh(tbp.getUser1Answer()); else if (tbp.getUser2Answer() != null) session.refresh(tbp.getUser2Answer());
        answer.setAccepted(true);
        disambiguator.clearNotAccessible();
        answer.setAnswer(new TreeXMLExporter().export(disambiguator.getForest(), ForestExportFormat.NO_METADATA));
        if (!collisionMode && tbp.getUser1Answer().isEquivalentTo(tbp.getUser2Answer())) {
            Answer finalAnswer = new Answer(tbp);
            finalAnswer.setAnswer(answer.getAnswer());
            finalAnswer.setType(answer.getType());
            finalAnswer.setComment("AUTO");
            finalAnswer.setAccepted(true);
            session.persist(finalAnswer);
            tbp.setSuperAnswer(finalAnswer);
            tbp.updateState();
            log.info("HISTORIA: Automatyczna superodpowiedź " + finalAnswer.getId() + " z powodu zgodności przy akceptacji odpowiedzi " + answer.getId());
            log.info(finalAnswer.info());
        }
        tbp.updateState();
        renewDate();
        session.flush();
        log.info("HISTORIA: Akceptacja odpowiedzi " + answer.getId());
        log.info(answer.info());
        log.info(tbp.info());
        if (isCollision() && !answer.isConfirmed()) {
            return false;
        } else {
            conversation.end();
            leave();
            return true;
        }
    }

    @RaiseEvent("tbpchangestate")
    public void cancel() {
        session.refresh(answer.getTaskBusinessProcess());
        if (answer.getTaskBusinessProcess().getSuperAnswer() != null && answer.getTaskBusinessProcess().getSuperAnswer().getUser() == null) {
            log.info("HISTORIA: usuwamy automatyczną superodpowiedź " + answer.getTaskBusinessProcess().getSuperAnswer().getId() + " z powodu cofnięcia odpowiedzi " + answer.getId());
            session.delete(answer.getTaskBusinessProcess().getSuperAnswer());
            answer.getTaskBusinessProcess().setSuperAnswer(null);
        }
        if (isCollision()) {
            answer.setConfirmed(true);
        }
        answer.setAccepted(false);
        answer.getTaskBusinessProcess().updateState();
        log.info("HISTORIA: dendrolog wycofał odpowiedź " + answer.getId());
        log.info(answer.info());
        session.flush();
    }

    @End
    @RaiseEvent("tbpchangestate")
    public void confirm() {
        TaskBusinessProcess tbp = answer.getTaskBusinessProcess();
        session.refresh(tbp);
        if (!user.getUsername().equals(tbp.getUser1Answer().getUser().getUsername())) session.refresh(tbp.getUser1Answer()); else if (tbp.getUser2Answer() != null) session.refresh(tbp.getUser2Answer());
        answer.setConfirmed(true);
        tbp.updateState();
        renewDate();
        log.info("HISTORIA: Potwierdzenie odpowiedzi " + answer.getId());
        log.info(answer.info());
        leave();
    }

    @RaiseEvent("tbpchangestate")
    @End
    public void leave() {
        disambiguator.clearNotAccessible();
        answer.setAnswer(new TreeXMLExporter().export(disambiguator.getForest(), ForestExportFormat.NO_METADATA));
        session.flush();
    }

    /**
     * @author pta
     */
    public void restart() {
        answer.setType(AnswerType.FULL);
        answer.setAnswer(answer.getTaskBusinessProcess().getTask().getForestXML());
        answerId = answer.getId();
        disambiguator = null;
        select();
    }

    @RaiseEvent("tbpchangestate")
    @End
    public void resign() {
        answer = (Answer) session.merge(answer);
        TaskBusinessProcess tbp = answer.getTaskBusinessProcess();
        if (tbp.getUser1Answer().isEquivalentTo(tbp.getUser2Answer())) {
            Answer finalAnswer = new Answer(tbp);
            finalAnswer.setAnswer(tbp.getUser1Answer().getAnswer());
            finalAnswer.setType(tbp.getUser1Answer().getType());
            finalAnswer.setComment("AUTO");
            finalAnswer.setAccepted(true);
            tbp.setSuperAnswer(finalAnswer);
            session.persist(finalAnswer);
            log.info("HISTORIA: Automatyczna superodpowiedź " + finalAnswer.getId() + " odtworzona z powodu rezygnacji supera");
            log.info(finalAnswer.info());
        } else {
            tbp.setSuperAnswer(null);
        }
        tbp.updateState();
        try {
            session.flush();
            session.delete(answer);
        } catch (Exception e) {
            SQLException b = (SQLException) e;
            SQLException next = b.getNextException();
            log.info("wyjątek", b);
            log.info("D!!!!\n\n\n\n");
            log.info("2. wyjątek", e);
        }
        log.info("HISTORIA: super rezygnuje ze swojej odpowiedzi i oddaje dendrologm zadanie " + tbp.getId());
        log.info(tbp.info());
    }

    @RaiseEvent("tbpchangestate")
    public void takeover() {
        superHasClicked = true;
        readOnlyMode = false;
        TaskBusinessProcess tbp = answer.getTaskBusinessProcess();
        if (tbp.getSuperAnswer() == null) {
            answer = new Answer(tbp);
            answer.setType(null);
            tbp.setSuperAnswer(answer);
            session.persist(answer);
        }
        user = (User) session.merge(user);
        answer.setUser(user);
        tbp.updateState();
        doCollisionSelect(answer.getTaskBusinessProcess());
        roCause = computeReadOnlyCause();
        session.flush();
        log.info("HISTORIA: super przejął zadanie " + tbp.getId() + ", utworzono superodpowiedź " + answer.getId());
        log.info(tbp.info());
        log.info(answer.info());
    }

    /** po wybraniu wariantu każemy dezambiguatorowi dokonać automatycznych
     * wyborów, system przenosi nas do kolejnego pytania; wybór wariantu
     * anuluje wybraną odpowiedź specjalną
     */
    public void choose(Variant variant) {
        switch(selectionState) {
            case SELECT_FILTER:
                disambiguator.chooseFilteringVariant(variant);
                break;
            case SELECT_FINAL:
                disambiguator.chooseVariant(variant);
                answer.setType(AnswerType.FULL);
                disambiguator.saturate();
                break;
        }
        generateAll();
    }

    public void chooseSpecialAnswer(AnswerType answerType) {
        answer.setType(answerType);
        generateAll();
    }

    public void goToNode(NonterminalNode n) {
        disambiguator.setCurrent(n);
        generateAll();
    }

    public void showAll() {
        showsSuggested = false;
        generateVariants();
    }

    public void showSuggested() {
        showsSuggested = true;
        generateVariants();
    }

    public boolean showsAll() {
        return !showsSuggested;
    }

    public boolean showsSuggested() {
        return showsSuggested;
    }

    public boolean hasSuggestedVariants() {
        return disambiguator.hasSuggestedVariants();
    }

    public boolean hasNotSuggestedVariants() {
        return disambiguator.hasNotSuggestedVariants();
    }

    public boolean isDone() {
        if (answer.getType() == null) {
            return false;
        }
        return (answer.getType() == AnswerType.FULL && disambiguator.isDone()) || (answer.getType() != AnswerType.FULL && !answer.getComment().equals("")) || (answer.getType() == AnswerType.NO_TREE && noTrees());
    }

    public boolean needsSettingType() {
        return answer.getType() == null;
    }

    public boolean hasChoice() {
        return disambiguator.hasChoice() || needsSettingType();
    }

    public boolean isAccepted() {
        return answer.isAccepted();
    }

    public boolean isConfirmed() {
        return answer.isConfirmed();
    }

    public boolean isCollisionMode() {
        return collisionMode;
    }

    public boolean isReadOnlyMode() {
        return readOnlyMode;
    }

    /**
     * @author janek37
     * Przyczyna, dlaczego jest tryb RO.
     * @return
     * 0, jeśli nie ma trybu RO;
     * 1, jeśli dendrolog zagląda do drzewa, które już zrobił, a
     * następnie w drzewie zainterweniował superdendrolog;
     * 2, jeśli superdendrolog zagląda do drzewa, ale jeszcze nie
     * przejął go do obróbki;
     * 3, jeśli gramatyk ogląda zdanie
     */
    public int computeReadOnlyCause() {
        if (!readOnlyMode) return 0;
        TaskBusinessProcess tbp = answer.getTaskBusinessProcess();
        user = (User) session.merge(user);
        User user1 = null, user2 = null;
        Answer answer1 = tbp.getUser1Answer();
        Answer answer2 = tbp.getUser2Answer();
        if (answer1 != null) user1 = answer1.getUser();
        if (answer2 != null) user2 = answer2.getUser();
        if ((user1 != null && user1.getUsername().equals(user.getUsername())) || (user2 != null && user2.getUsername().equals(user.getUsername()))) return 1;
        if (identity.hasRole("superdendrolog") && isCollisionMode() && !hasSuperClicked()) return 2;
        return 3;
    }

    public int readOnlyCause() {
        return roCause;
    }

    public boolean hasSuperClicked() {
        return superHasClicked;
    }

    /** jeśli prawdziwe, zmienia tło strony wykonywania zadania na niepokojące
     * paski oznaczające kolizje 
     */
    public boolean isCollision() {
        return answer.getTaskBusinessProcess().getState() == TaskBusinessProcessState.COLLISION;
    }

    public boolean isSpecial() {
        return answer.getType() != null && answer.getType() != AnswerType.FULL;
    }

    public boolean isCommentRequired() {
        return isSpecial() && !(isForestEmpty() && answer.getType() == AnswerType.NO_TREE);
    }

    /** potrzebne w interfejsie, do zaznaczania przycisku przy wybranej
     * odpowiedzi specjalnej
     */
    public boolean isSpecial(AnswerType type) {
        return answer.getType() == type;
    }

    public String decorateSpecial(AnswerType type) {
        return disambiguator.decorate(type);
    }

    /**
     * Czy obrabiamy zdanie bez rozbiorow
     */
    public boolean noTrees() {
        return disambiguator.noTrees();
    }

    public String specialAnswerType() {
        return answer.getType().getName();
    }

    public String getLeftParagraphContext() {
        return leftParagraphContext;
    }

    public String getLeftSentenceContext() {
        return leftSentenceContext;
    }

    public String getRightParagraphContext() {
        return rightParagraphContext;
    }

    public String getRightSentenceContext() {
        return rightSentenceContext;
    }

    public String getSubsentenceContext() {
        return subsentenceContext;
    }

    public boolean isForestEmpty() {
        return disambiguator.getForest().isEmpty();
    }

    @Factory("variants")
    public void generateVariants() {
        if (showsSuggested) {
            this.variants = disambiguator.getSuggestedVariants();
        } else {
            this.variants = disambiguator.getVariants();
        }
        this.numUnfilteredVariants = this.variants.size();
        if (this.variants.size() > MAX_VARIANTS_WITHOUT_FILTERING && !disambiguator.isFiltered()) {
            disambiguator.calcFilteringVariants(this.variants);
            this.variants = disambiguator.getFilteringVariants();
            selectionState = SelectionState.SELECT_FILTER;
        } else {
            selectionState = SelectionState.SELECT_FINAL;
        }
        treeRenderer.renderVariants(disambiguator.getHash(), variants);
    }

    /**
     * wierzchołki znajdujące się w aktualnym drzewie rozbioru (te, do których
     * możemy się przenieść kliknięciem)
     */
    @Factory("accessibleNodes")
    public void generateAccessibleNodes() {
        this.accessibleNodes = disambiguator.getAccessibleNodes();
    }

    @Factory("specialAnswers")
    public List<AnswerType> getSpecialAnswers() {
        List<AnswerType> specialAnswers = new LinkedList<AnswerType>();
        for (AnswerType a : AnswerType.values()) {
            specialAnswers.add(a);
        }
        if (answer.getType() != null) {
            specialAnswers.remove(AnswerType.FULL);
        }
        return specialAnswers;
    }

    private void renewDate() {
        Calendar cal = Calendar.getInstance();
        Date now = new Date(cal.getTimeInMillis());
        answer.setSelectDate(now);
        answer.setRefreshedByMUZG(false);
    }

    /** inicjowanie procesu wykonywania zadania, tworzy dezambiguator, jeśli
     * jeszcze nie istnieje, nasyca go (czyli każe wykonać wszystkie wybory,
     * których można dokonać automatycznie) i generuje wszysko potrzebne do
     * zadania pierwszego pytania (kontekst paragrafu oraz to, co robi
     * generateAll)
     */
    private void doSelect(Answer a) {
        if (readOnlyMode) {
            this.answer = new Answer(a);
        } else {
            this.answer = a;
        }
        try {
            this.disambiguator = new NKJPDisambiguator(new TreeXMLParser().parse(answer.getAnswer()));
            disambiguator.log = log;
        } catch (Exception ex) {
            log.error("Unable to parse forest XML in task " + answer.getTaskBusinessProcess().getId(), ex);
            return;
        }
        disambiguator.saturate();
        generateAll();
        generateParagraphContexts();
    }

    /** inicjowanie procesu rozwiązywania kolizji - jeśli jeszcze nie zajmowaliśmy
     * się daną kolizją, musimy odpowiednio zmodyfikować stan całego zadania (tbp);
     * tworzymy też odpowiedni do rozwiązywania kolizji dezambiguator, a następnie
     * odpalamy metodę doSelect
     */
    private void doCollisionSelect(TaskBusinessProcess tbp) {
        if (tbp.getSuperAnswer() == null) {
            superHasClicked = false;
            readOnlyMode = true;
            answer = new Answer(tbp);
        } else {
            answer = tbp.getSuperAnswer();
            if (answer.getUser() == null) {
                superHasClicked = false;
                readOnlyMode = true;
            } else {
                superHasClicked = true;
            }
        }
        try {
            this.disambiguator = new CollisionDisambiguator(new TreeXMLParser().parse(answer.getAnswer()), new TreeXMLParser().parse(tbp.getUser1Answer().getAnswer()), new TreeXMLParser().parse(tbp.getUser2Answer().getAnswer()), tbp.getUser1Answer().getUser().getName(), tbp.getUser2Answer().getUser().getName(), tbp.getUser1Answer().getType(), tbp.getUser2Answer().getType());
            disambiguator.log = log;
        } catch (Exception ex) {
            log.error("Unable to parse forest XML in task " + tbp.getId(), ex);
            return;
        }
        disambiguator.saturate();
        generateAll();
        generateParagraphContexts();
        if (answer.getType() == null && !((CollisionDisambiguator) disambiguator).hasTypeCollision()) {
            answer.setType(tbp.getUser1Answer().getType());
        }
    }

    private void generateParagraphContexts() {
        Integer index = answer.getTaskBusinessProcess().getIndex();
        List<String> paragraph = answer.getTaskBusinessProcess().getPacket().getParagraphText();
        this.leftParagraphContext = StringUtils.join(paragraph.subList(0, index).toArray(), " ");
        this.rightParagraphContext = StringUtils.join(paragraph.subList(index + 1, paragraph.size()).toArray(), " ");
    }

    private void generateSentenceContexts() {
        Node current = disambiguator.getCurrent();
        if (current != null && !isForestEmpty()) {
            int from = current.getFrom();
            int to = current.getTo();
            this.leftSentenceContext = disambiguator.getForest().wordRangeTo(from);
            this.subsentenceContext = disambiguator.getForest().wordRange(from, to);
            this.rightSentenceContext = disambiguator.getForest().wordRangeFrom(to);
        } else {
            this.leftSentenceContext = disambiguator.getForest().getText();
            this.subsentenceContext = "";
            this.rightSentenceContext = "";
        }
    }

    /** generuje wszystko, co jest potrzebne do zadania pytania: klikalne duże
     * drzewo aktualnego rozbioru, małe drzewa możliwych wariantów, oraz aktualny
     * kontekst w zdaniu (podświetla część zdania, którą się aktualnie zajmujemy)
     */
    private void generateAll() {
        generateAccessibleNodes();
        treeRenderer.renderImage(disambiguator.getForest(), disambiguator.getCurrent(), accessibleNodes);
        treeRenderer.generateTreeInfo(disambiguator.getForest(), disambiguator.getCurrent(), accessibleNodes, false);
        if (disambiguator.hasChoice()) {
            if (disambiguator.hasSuggestedVariants() && !disambiguator.chosenNotSuggested()) {
                showsSuggested = true;
            } else {
                showsSuggested = false;
            }
            generateVariants();
        }
        generateSentenceContexts();
    }

    /**
     * Czy oglądamy własną odpowiedź, czy cudzą?
     * @author janek37
     */
    public Boolean isMyAnswer() {
        user = (User) session.merge(user);
        if (originalAnswer == null || originalAnswer.getUser() == null) return false;
        return user.getUsername().equals(originalAnswer.getUser().getUsername());
    }

    public boolean showsFilteringVariants() {
        return selectionState == SelectionState.SELECT_FILTER;
    }

    public boolean isFilterActive() {
        return disambiguator.isFiltered();
    }

    public int numUnfilteredVariants() {
        return numUnfilteredVariants;
    }
}
