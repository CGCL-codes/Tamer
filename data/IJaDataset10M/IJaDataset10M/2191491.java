package de.fzi.injectj.services.recoder;

import recoder.CrossReferenceServiceConfiguration;
import recoder.DefaultServiceConfiguration;
import recoder.service.SourceInfo;

/**
 * @author genssler
 * This class is part of the Inject/J (http://injectj.sf.net) project. 
 * Inject/J is free software, available under the terms and conditions
 * of the GNU public license.
 *
 * 
 */
public class ByteCodeCrossReferenceServiceConfiguration extends CrossReferenceServiceConfiguration {

    /**
	   The cross reference source info of this service configuration.
	   This is a copy of the sourceInfo attribute, to avoid type casts.
	*/
    private ByteCodeCrossReferenceSourceInfo crossReferencer;

    public static boolean ENABLE_BYTECODE_CROSSREFERENCER = true;

    /**
	 * 
	 */
    public ByteCodeCrossReferenceServiceConfiguration() {
        super();
    }

    protected void makeServices() {
        super.makeServices();
        crossReferencer = (ByteCodeCrossReferenceSourceInfo) super.getSourceInfo();
    }

    protected void initServices() {
        super.initServices();
    }

    /**
	   The cross reference source info is a subclass of the source info,
	   so this class simply overrides the source info factory method.
	 */
    protected SourceInfo makeSourceInfo() {
        return new ByteCodeCrossReferenceSourceInfo(this);
    }
}
