package org.arastreju.core.model.lexis;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.QueryHint;
import javax.persistence.Table;
import org.arastreju.api.common.ChangeState;
import org.arastreju.api.terminology.TermMetaInfo;
import org.arastreju.api.terminology.Terminology;
import org.arastreju.api.terminology.WordDefinition;
import org.eclipse.persistence.config.QueryHints;

/**
 * 
 * Link between {@link WordDefinition} and {@link Terminology} with meta infos.
 * 
 * Created: 27.11.2008
 *
 * @author Oliver Tigges
 */
@NamedQueries({ @NamedQuery(name = TermMetaInfoDBO.FIND_BY_STATE, query = "SELECT t FROM TermMetaInfoDBO t JOIN FETCH t.definition " + " WHERE t.definition.terminology = :terminology AND t.state = :state"), @NamedQuery(name = TermMetaInfoDBO.FIND_BY_OPP_STATE, query = "SELECT t FROM TermMetaInfoDBO t  JOIN FETCH t.definition " + " WHERE t.definition.terminology = :terminology AND t.state <> :state"), @NamedQuery(name = TermMetaInfoDBO.FIND_BY_REVISION, query = "SELECT t FROM TermMetaInfoDBO t JOIN t.definition wd " + " WHERE wd.terminology = :terminology AND t.revision >= :revision ORDER BY wd.wordClass, wd.normalized", hints = { @QueryHint(name = QueryHints.FETCH, value = "t.definition"), @QueryHint(name = QueryHints.LEFT_FETCH, value = "t.definition.genAttrites"), @QueryHint(name = QueryHints.LEFT_FETCH, value = "t.definition.baseDef"), @QueryHint(name = QueryHints.LEFT_FETCH, value = "t.definition.flectionClass") }) })
@Entity
@Table(name = "TERM_MI")
public class TermMetaInfoDBO implements TermMetaInfo {

    public static final String FIND_BY_STATE = "TermMetaInfoDBO:findByState";

    public static final String FIND_BY_OPP_STATE = "TermMetaInfoDBO:findByOppState";

    public static final String FIND_BY_REVISION = "TermMetaInfoDBO:findByRevision";

    public static final String PARAM_WORD_DEF = "definition";

    public static final String PARAM_STATE = "state";

    public static final String PARAM_REVISION = "revision";

    public static final String PARAM_TERMINOLOGY = "terminology";

    @Id
    @GeneratedValue
    private Long id;

    @OneToOne(fetch = FetchType.EAGER, targetEntity = WordDefinitionDBO.class, optional = false)
    private WordDefinition definition;

    @Column(name = "CREATE_REVISION")
    private Long createRevision;

    private Long revision;

    @Enumerated(value = EnumType.STRING)
    private ChangeState state;

    public TermMetaInfoDBO(WordDefinitionDBO def, Terminology terminology, ChangeState changeState) {
        this.definition = def;
        this.createRevision = terminology.getRevision();
        this.revision = terminology.getRevision();
        this.state = changeState;
        def.setMetaInfo(this);
    }

    /**
	 * JPA Constructor
	 */
    protected TermMetaInfoDBO() {
    }

    public Long getId() {
        return id;
    }

    public WordDefinition getWordDefinition() {
        return definition;
    }

    public Long getRevision() {
        return revision;
    }

    public Long getCreateRevision() {
        return createRevision;
    }

    public ChangeState getState() {
        return state;
    }

    public void modified() {
        state = ChangeState.MODIFIED;
        if (definition.getTerminology() != null && definition.getTerminology().isDomestic()) {
            revision++;
        }
    }

    public void deleted() {
        state = ChangeState.DELETED;
    }

    public void synced() {
        state = ChangeState.UNMODIFIED;
    }

    public void synced(Long version) {
        this.revision = version;
        this.state = ChangeState.UNMODIFIED;
    }

    @Override
    public String toString() {
        return state + " (" + revision + "): " + definition;
    }
}
