package de.fzi.injectj.model;

/**
 * @author genssler
 * This class is part of the Inject/J (http://injectj.sf.net) project. 
 * Inject/J is free software, available under the terms and conditions
 * of the GNU public license.
 * @inject export name=FragmentType package=lang.model
 * 
 */
public abstract class FragmentType extends InjectModelType implements SourceElement {

    /** 
	 * @return
	 * @see de.fzi.injectj.script.model.Type#getAskIdentifier()
	 */
    public String getAskIdentifier() {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    /**
	 * 
	 * @return the source of this fragment as a string
	 * @inject export modifier=query
	 */
    public abstract String getSourceString();
}
