package mipt.aaf.edit;

/**
 * Interface for domain-specfic editor that includes view and part of controller (UI event listening)
 * Don't forget to call initView() from external class (after the moment all data for editors is initialized)
 * @author Evdokimov
 */
public interface UIEditDelegate {

    /**
	 * Returns not simply Editor because only AbstractEditor has viewDelegate - this object
	 * @return
	 */
    AbstractEditor getEditor();

    /**
	 * Creates specific view ()
	 */
    void initView();
}
