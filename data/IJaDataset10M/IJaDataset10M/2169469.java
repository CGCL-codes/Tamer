package org.torweg.pulse.component.util.representative.admin;

import javax.xml.bind.annotation.XmlAccessOrder;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorOrder;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import org.torweg.pulse.component.util.admin.EditorResult;
import org.torweg.pulse.component.util.model.Representative;

/**
 * The result of the {@code RepresentativeEditor}.
 * 
 * @author Daniel Dietz
 * @version $Revision: 1577 $
 * 
 */
@XmlRootElement(name = "representative-editor-result")
@XmlAccessorOrder(XmlAccessOrder.UNDEFINED)
@XmlAccessorType(XmlAccessType.FIELD)
public class RepresentativeEditorResult extends EditorResult {

    /**
	 * The {@code Representative} of the
	 * {@code RepresentativeEditorResult}.
	 */
    @XmlElement(name = "representative")
    private Representative resultRepresentative;

    /**
	 * Default constructor.
	 */
    protected RepresentativeEditorResult() {
        super();
    }

    /**
	 * Sets the {@code Representative} for the
	 * {@code RepresentativeEditorResult}.
	 * 
	 * @param representative
	 *            the {@code Representative} to set
	 */
    protected final void setRepresentative(final Representative representative) {
        this.resultRepresentative = representative;
    }

    /**
	 * Returns the {@code Representative} of the
	 * {@code RepresentativeEditorResult}.
	 * 
	 * @return the {@code Representative}
	 */
    protected final Representative getRepresentative() {
        return this.resultRepresentative;
    }
}
