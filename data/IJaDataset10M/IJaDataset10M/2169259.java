package com.ibm.realtime.flexotask.editor.dialogs;

/**
 * Interface to be implemented by runtime providers.  These contribute native libraries and
 *   proprietary Java libraries to the runtime.   The contribution always takes the form of
 *   a ZipFile.
 */
public interface RuntimeProvider {

    /**
	 * @return this provider's contribution to the runtime as the absolute path name of a zip archive
	 */
    public String contribute();

    /**
	 * @return this provider's one-sentence self-description of what it provides
	 */
    public String getDescription();

    /**
	 * @return true iff his RuntimeProvider provides the FlexotaskVMBridge service (e.g. supports true real-time
	 *   execution)
	 */
    public boolean isVMBridge();
}
