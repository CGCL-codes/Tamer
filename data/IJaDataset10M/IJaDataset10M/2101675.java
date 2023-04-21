package uk.ac.ebi.intact.model;

import uk.ac.ebi.intact.annotation.EditorTopic;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

/**
 * Controlled vocabulary for description topics.
 *
 * @author hhe
 * @version $Id: CvTopic.java 5133 2006-06-29 10:25:22Z baranda $
 */
@Entity
@DiscriminatorValue("uk.ac.ebi.intact.model.CvTopic")
@EditorTopic
public class CvTopic extends CvObject implements Editable {

    public static final String CURATED_COMPLEX = "curated-complex";

    public static final String ASSEMBLY = "assembly";

    public static final String THREE_D_R_FACTORS = "3d-r-factors";

    public static final String THREE_D_R_FACTORS_MI_REF = "MI:0631";

    public static final String THREE_D_R_RESOLUTION = "3d-resolution";

    public static final String THREE_D_R_RESOLUTION_MI_REF = "MI:0632";

    public static final String XREF_VALIDATION_REGEXP = "id-validation-regexp";

    public static final String XREF_VALIDATION_REGEXP_MI_REF = "MI:0628";

    public static final String AGONIST = "agonist";

    public static final String CAUTION = "caution";

    public static final String REMARK_INTERNAL = "remark-internal";

    public static final String RESULTING_PTM = "resulting-ptm";

    public static final String PREREQUISITE_PTM = "prerequisite-ptm";

    public static final String COMMENT = "comment";

    public static final String COMMENT_MI_REF = "MI:0612";

    public static final String INTERNAL_REMARK = "remark-internal";

    public static final String UNIPROT_DR_EXPORT = "uniprot-dr-export";

    public static final String UNIPROT_CC_EXPORT = "uniprot-cc-note";

    public static final String NEGATIVE = "negative";

    public static final String FUNCTION = "function";

    public static final String COPYRIGHT = "copyright";

    public static final String AUTHOR_CONFIDENCE = "author-confidence";

    public static final String AUTHOR_CONFIDENCE_MI_REF = "MI:0621";

    public static final String CONFIDENCE_MAPPING = "confidence-mapping";

    public static final String CONFIDENCE_MAPPING_MI_REF = "MI:0622";

    public static final String SEARCH_URL_ASCII = "search-url-ascii";

    public static final String SEARCH_URL = "search-url";

    public static final String SEARCH_URL_MI_REF = "MI:0615";

    public static final String ISOFORM_COMMENT = "isoform-comment";

    public static final String NON_UNIPROT = "no-uniprot-update";

    public static final String CC_NOTE = "uniprot-cc-note";

    public static final String OBSOLETE = "obsolete term";

    public static final String OBSOLETE_MI_REF = "MI:0431";

    public static final String NO_EXPORT = "no-export";

    public static final String ON_HOLD = "on-hold";

    public static final String ACCEPTED = "accepted";

    public static final String TO_BE_REVIEWED = "to-be-reviewed";

    public static final String URL = "url";

    public static final String URL_MI_REF = "MI:0614";

    public static final String CONTACT_EMAIL = "contact-email";

    public static final String CONTACT_EMAIL_MI_REF = "MI:0634";

    public static final String AUTHOR_LIST = "author-list";

    public static final String AUTHOR_LIST_MI_REF = "MI:0636";

    public static final String USED_IN_CLASS = "used-in-class";

    public static final String DEFINITION = "definition";

    public static final String HIDDEN = "hidden";

    public static final String JOURNAL = "journal";

    public static final String PUBLICATION_YEAR = "publication-year";

    public static final String REVIEWER = "reviewer";

    public static final String IMEX_RANGE_REQUESTED = "imex-range-requested";

    public static final String IMEX_RANGE_ASSIGNED = "imex-range-assigned";

    public static final String IMEX_EXPORTED = "imex-exported";

    /**
     * This constructor should <b>not</b> be used as it could result in objects with invalid state. It is here for
     * object mapping purposes only and if possible will be made private.
     *
     * @deprecated Use the full constructor instead
     */
    @Deprecated
    public CvTopic() {
        super();
    }

    /**
     * Creates a valid CvTopic instance. Requires at least a shortLabel and an owner to be specified.
     *
     * @param shortLabel The memorable label to identify this CvTopic
     * @param owner      The Institution which owns this CvTopic
     *
     * @throws NullPointerException thrown if either parameters are not specified
     */
    public CvTopic(Institution owner, String shortLabel) {
        super(owner, shortLabel);
    }
}
