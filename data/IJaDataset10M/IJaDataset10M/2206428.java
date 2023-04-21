package org.marcont.services.definitions.grounding;

/**
 * Implementations of this listener may be registered with instances of org.marcont.services.definitions.grounding.ErrorsReport to 
 * receive notification when properties changed, added or removed.
 * <br>
 */
public interface ErrorsReportListener extends com.ibm.adtech.jastor.ThingListener {

    /**
	 * Called when a value of parameterValue has been added
	 * @param source the affected instance of org.marcont.services.definitions.grounding.ErrorsReport
	 * @param newValue the object representing the new value
	 */
    public void parameterValueAdded(org.marcont.services.definitions.grounding.ErrorsReport source, java.lang.String newValue);

    /**
	 * Called when a value of parameterValue has been removed
	 * @param source the affected instance of org.marcont.services.definitions.grounding.ErrorsReport
	 * @param oldValue the object representing the removed value
	 */
    public void parameterValueRemoved(org.marcont.services.definitions.grounding.ErrorsReport source, java.lang.String oldValue);

    /**
	 * Called when parameterType has changed
	 * @param source the affected instance of org.marcont.services.definitions.grounding.ErrorsReport
	 */
    public void parameterTypeChanged(org.marcont.services.definitions.grounding.ErrorsReport source);

    /**
	 * Called when appliesTo has changed
	 * @param source the affected instance of org.marcont.services.definitions.grounding.ErrorsReport
	 */
    public void appliesToChanged(org.marcont.services.definitions.grounding.ErrorsReport source);

    /**
	 * Called when a value of DatatypeProperty__3 has been added
	 * @param source the affected instance of org.marcont.services.definitions.grounding.ErrorsReport
	 * @param newValue the object representing the new value
	 */
    public void DatatypeProperty__3Added(org.marcont.services.definitions.grounding.ErrorsReport source, com.hp.hpl.jena.rdf.model.Literal newValue);

    /**
	 * Called when a value of DatatypeProperty__3 has been removed
	 * @param source the affected instance of org.marcont.services.definitions.grounding.ErrorsReport
	 * @param oldValue the object representing the removed value
	 */
    public void DatatypeProperty__3Removed(org.marcont.services.definitions.grounding.ErrorsReport source, com.hp.hpl.jena.rdf.model.Literal oldValue);

    /**
	 * Called when a value of hasError has been added
	 * @param source the affected instance of org.marcont.services.definitions.grounding.ErrorsReport
	 * @param newValue the object representing the new value
	 */
    public void hasErrorAdded(org.marcont.services.definitions.grounding.ErrorsReport source, org.marcont.services.definitions.grounding.Error newValue);

    /**
	 * Called when a value of hasError has been removed
	 * @param source the affected instance of org.marcont.services.definitions.grounding.ErrorsReport
	 * @param oldValue the object representing the removed value
	 */
    public void hasErrorRemoved(org.marcont.services.definitions.grounding.ErrorsReport source, org.marcont.services.definitions.grounding.Error oldValue);
}
